package linenux.command.filter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.result.CommandResult;
import linenux.control.TimeParserManager;
import linenux.model.Reminder;
import linenux.model.Task;
import linenux.util.ArrayListUtil;
import linenux.util.Either;

//@@author A0140702X
/**
 * Parser for the argument portion of list command.
 **/
public class ListArgumentFilter {
    public static String COMMAND_FORMAT;
    public static String CALLOUTS;

    private TimeParserManager timeParserManager;

    /**
     * Public constructor.
     * @param timeParserManager A {@code TimeParserManager} representing all accepted time format.
     * @param commandFormat A {@code String} representing the format of the command that is using this class.
     * @param callouts A {@code String} representing the extra message shown to the user in case of errors.
     */
    public ListArgumentFilter(TimeParserManager timeParserManager, String commandFormat, String callouts) {
        this.timeParserManager = timeParserManager;
        ListArgumentFilter.COMMAND_FORMAT = commandFormat;
        ListArgumentFilter.CALLOUTS = callouts;
    }

    /**
     * Filter the list of tasks based on argument specified by the user.
     * @param argument The argument, which is part of the user input.
     * @param tasks The search space.
     * @param doneOnly Set this to true if and only if we are interested in only done tasks.
     * @return An {@code Either}. If search was successful, the left slot will contain the list of filtered tasks.
     * Otherwise, the right slot will contain a {@code CommandResult} describing the failure.
     */
    public Either<ArrayList<Task>, CommandResult> filter(String argument, ArrayList<Task> tasks, Boolean doneOnly) {
        ArrayList<Task> filteredTasks = tasks;

        Either<LocalDateTime, CommandResult> startTime = extractStartTime(argument);
        if (startTime.isRight()) {
            return Either.right(startTime.getRight());
        }

        Either<LocalDateTime, CommandResult> endTime = extractEndTime(argument);
        if (endTime.isRight()) {

            return Either.right(endTime.getRight());
        }

        Either<ArrayList<String>, CommandResult> tags = extractTags(argument);
        if (tags.isRight()) {
            return Either.right(tags.getRight());
        }

        LocalDateTime actualStartTime = startTime.getLeft();
        LocalDateTime actualEndTime = endTime.getLeft();
        ArrayList<String> actualTags = tags.getLeft();

        if (actualStartTime != null && actualEndTime != null && actualEndTime.compareTo(actualStartTime) < 0) {
            return Either.right(makeEndTimeBeforeStartTimeResult());
        }

        if (doneOnly) {
            filteredTasks = new ArrayListUtil.ChainableArrayListUtil<>(filteredTasks)
                .filter(Task::isDone)
                .value();
        }

        //filter the tasks by the time parameters
        if (actualStartTime != null && actualEndTime != null) {
            filteredTasks = new ArrayListUtil.ChainableArrayListUtil<>(filteredTasks).filter(task -> {
                            boolean checker = task.isTodo()
                                    || task.getEndTime().isEqual(actualStartTime) || task.getEndTime().isEqual(actualEndTime)
                                    || (task.getEndTime().isAfter(actualStartTime) && task.getEndTime().isBefore(actualEndTime));
                            if (task.isEvent()) {
                                LocalDateTime taskStartTime = task.getStartTime();
                                return checker || taskStartTime.isEqual(actualStartTime) || taskStartTime.isEqual(actualEndTime)
                            || (taskStartTime.isAfter(actualStartTime) && taskStartTime.isBefore(actualEndTime))
                            || (taskStartTime.isBefore(actualStartTime) && task.getEndTime().isAfter(actualEndTime));
                            }
                            return checker; })
                        .value();
        } else if (actualStartTime != null) {
            filteredTasks = new ArrayListUtil.ChainableArrayListUtil<>(filteredTasks)
                    .filter(task -> task.isTodo() || task.getEndTime().isAfter(actualStartTime)
                            || task.getEndTime().isEqual(actualStartTime))
                    .value();
        } else if (actualEndTime != null) {
            filteredTasks = new ArrayListUtil.ChainableArrayListUtil<>(filteredTasks)
                    .filter(task -> {
                        boolean checker = task.isTodo() || task.getEndTime().isBefore(actualEndTime)
                            || task.getEndTime().isEqual(actualEndTime);
                        if (task.isEvent()) {
                            return checker || task.getStartTime().isBefore(actualEndTime) || task.getStartTime().isEqual(actualEndTime);
                        };

                        return checker; })
                    .value();
        }

        //filter tasks by tags
        if (!actualTags.isEmpty()) {
            for (String tag : actualTags) {
                filteredTasks = new ArrayListUtil.ChainableArrayListUtil<>(filteredTasks)
                        .filter(task -> task.hasTag(tag)).value();

            }
        }

        return Either.left(filteredTasks);
    }

    /**
     * Filter the list of reminders based on user argument.
     * @param argument A {@code String} representing the argument given by the user.
     * @param reminders The list of {@code Reminder} to search from. This is the search space.
     * @return An {@Either}. If search is successful, its left slot is a list of {@code Reminder}. Otherwise, its
     * right slot is a {@code CommandResult} describing the failure.
     */
    public Either<ArrayList<Reminder>, CommandResult> filterReminders(String argument, ArrayList<Reminder> reminders) {
        ArrayList<Reminder> filteredReminders = new ArrayListUtil.ChainableArrayListUtil<>(reminders)
                            .sortBy(Reminder::getTimeOfReminder)
                            .value();

        Either<LocalDateTime, CommandResult> startTime = extractStartTime(argument);
        if (startTime.isRight()) {
            return Either.right(startTime.getRight());
        }

        Either<LocalDateTime, CommandResult> endTime = extractEndTime(argument);
        if (endTime.isRight()) {
            return Either.right(endTime.getRight());
        }

        Either<ArrayList<String>, CommandResult> tags = extractTags(argument);
        if (tags.isRight()) {
            return Either.right(tags.getRight());
        }

        ArrayList<String> actualTags = tags.getLeft();
        if (!actualTags.isEmpty()) {
            return Either.left(new ArrayList<Reminder>());
        }

        LocalDateTime actualStartTime = startTime.getLeft();
        LocalDateTime actualEndTime = endTime.getLeft();

        //filter the reminders by the time parameters
        if (actualStartTime != null && actualEndTime != null) {
            filteredReminders = new ArrayListUtil.ChainableArrayListUtil<>(filteredReminders)
                    .filter(reminder -> reminder.getTimeOfReminder().isEqual(actualStartTime)
                    || (reminder.getTimeOfReminder().isAfter(actualStartTime) && reminder.getTimeOfReminder().isBefore(actualEndTime)))
                    .value();
        } else if (actualStartTime != null) {
            filteredReminders = new ArrayListUtil.ChainableArrayListUtil<>(filteredReminders)
                    .filter(reminder -> reminder.getTimeOfReminder().isAfter(actualStartTime)
                            || reminder.getTimeOfReminder().isEqual(actualStartTime))
                    .value();
        } else if (actualEndTime != null) {
            filteredReminders = new ArrayListUtil.ChainableArrayListUtil<>(filteredReminders)
                    .filter(reminder -> reminder.getTimeOfReminder().isBefore(actualEndTime)
                            || reminder.getTimeOfReminder().isEqual(actualEndTime))
                    .value();
        }

        return Either.left(filteredReminders);
    }

    /**
     * @param tasks An {@code ArrayList} of {@code Task}.
     * @return Tasks that are not marked as done
     */
    public ArrayList<Task> filterUndoneTasks(ArrayList<Task> tasks) {
        ArrayList<Task> undoneTasks = new ArrayListUtil.ChainableArrayListUtil<>(tasks)
                .filter(Task::isNotDone)
                .value();

        return undoneTasks;
    }

    /**
     * Attempts to parse a start time string.
     * @param argument The {@code String} to parse.
     * @return An {@code Either}. Its left slot is a {@code LocalDateTime} if {@code argument} can be parsed. Otherwise,
     * its right slot contains a {@code CommandResult} describing the failure.
     */
    private Either<LocalDateTime, CommandResult> extractStartTime(String argument) {
        Matcher matcher = Pattern.compile("(^|.*? )st/(?<startTime>.*?)(\\s+(#|et)/.*)?").matcher(argument);

        if (matcher.matches() && matcher.group("startTime") != null) {
            return parseDateTime(matcher.group("startTime").trim());
        } else {
            return Either.left(null);
        }
    }

    /**
     * Attempts to parse a end time string.
     * @param argument The {@code String} to parse.
     * @return An {@code Either}. Its left slot is a {@code LocalDateTime} if {@code argument} can be parsed. Otherwise,
     * its right slot contains a {@code CommandResult} describing the failure.
     */
    private Either<LocalDateTime, CommandResult> extractEndTime(String argument) {
        Matcher matcher = Pattern.compile("(^|.*? )et/(?<endTime>.*?)(\\s+(#|st)/.*)?$").matcher(argument);

        if (matcher.matches() && matcher.group("endTime") != null) {
            return parseDateTime(matcher.group("endTime").trim());
        } else {
            return Either.left(null);
        }
    }

    /**
     * Attempts to extract tags from user argument.
     * @param argument A {@code String} representing the argument given by the user.
     * @return An {@code Either}. Its left slot is a {@code ArrayList} of {@code String} representing the tags, if
     * parsing is successful. Otherwise, its right slot will contain a {@CommandResult} describing the failure.
     */
    private Either<ArrayList<String>, CommandResult> extractTags(String argument) {
        Matcher matcher = Pattern.compile("(?=(^|.*? )#/(?<tags>.*?)(\\s+(st|et|#)/.*)?$)").matcher(argument);
        ArrayList<String> tagList = new ArrayList<>();
        String input;

        while (matcher.find() && matcher.group("tags") != null) {
            input = matcher.group("tags").trim();
            if (input.isEmpty()) {
                return Either.right(makeInvalidArgumentResult());
            }
            if (!tagList.contains(input)) {
                tagList.add(input);
            }
        }

        return Either.left(tagList);
    }

    /**
     * A generic helper used to parse a date time string.
     * @param string A {@code string} to parse.
     * @return An {@Either}. Its left slot is a {@code LocalDateTime} if {@code string} can be parsed. Otherwise, its
     * right slot is a {@code CommandResult} describing the failure.
     */
    private Either<LocalDateTime, CommandResult> parseDateTime(String string) {
        if (this.timeParserManager.canParse(string)) {
            return Either.left(this.timeParserManager.delegateTimeParser(string));
        } else {
            return Either.right(makeInvalidDateTimeResult(string));
        }
    }

    /**
     * @return A {@code CommandResult} when the given argument is invalid.
     */
    private CommandResult makeInvalidArgumentResult() {
        return () -> "Invalid arguments.\n\n" + COMMAND_FORMAT + "\n\n" + CALLOUTS;
    }

    /**
     * @param dateTime A {@code String} extracted from user argument that is supposed to represent a date time.
     * @return A {@code CommandResult} when {@code dateTime} is not a valid date time {@code String}.
     */
    private CommandResult makeInvalidDateTimeResult(String dateTime) {
        return () -> "Cannot parse \"" + dateTime + "\".";
    }

    /**
     * @return A {@code CommandResult} when the end time specified by the user comes before the start time.
     */
    private CommandResult makeEndTimeBeforeStartTimeResult() {
        return () -> "End time cannot come before start time.";
    }
}
