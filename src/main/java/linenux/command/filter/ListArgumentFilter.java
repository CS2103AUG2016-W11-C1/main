package linenux.command.filter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.result.CommandResult;
import linenux.control.TimeParserManager;
import linenux.model.Task;
import linenux.util.ArrayListUtil;
import linenux.util.Either;

/**
 * Parser for the argument portion of add command.
 **/
public class ListArgumentFilter {
    public static String COMMAND_FORMAT;
    public static String CALLOUTS;

    private TimeParserManager timeParserManager;

    public ListArgumentFilter(TimeParserManager timeParserManager, String commandFormat, String callouts) {
        this.timeParserManager = timeParserManager;
        ListArgumentFilter.COMMAND_FORMAT = commandFormat;
        ListArgumentFilter.CALLOUTS = callouts;
    }

    public Either<ArrayList<Task>, CommandResult> filter(String argument, ArrayList<Task> tasks) {
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

        if (actualStartTime != null && actualEndTime != null) {
            filteredTasks = new ArrayListUtil.ChainableArrayListUtil<>(filteredTasks).filter(task -> task.isTodo()
                    || task.getEndTime().isEqual(actualStartTime)
                    || (task.getEndTime().isAfter(actualStartTime) && task.getEndTime().isBefore(actualEndTime)))
                    .value();
        } else if (actualStartTime != null) {
            filteredTasks = new ArrayListUtil.ChainableArrayListUtil<>(filteredTasks)
                    .filter(task -> task.isTodo() || task.getEndTime().isAfter(actualStartTime)
                            || task.getEndTime().isEqual(actualStartTime))
                    .value();
        } else if (actualEndTime != null) {
            filteredTasks = new ArrayListUtil.ChainableArrayListUtil<>(filteredTasks)
                    .filter(task -> task.isTodo() || task.getEndTime().isBefore(actualEndTime)
                            || task.getEndTime().isEqual(actualEndTime))
                .value();
        }

        if (!actualTags.isEmpty()) {

        }

        return Either.left(filteredTasks);
    }

    private Either<LocalDateTime, CommandResult> extractStartTime(String argument) {
        Matcher matcher = Pattern.compile("(^|.*? )st/(?<startTime>.*?)(\\s+(#|et)/.*)?").matcher(argument);

        if (matcher.matches() && matcher.group("startTime") != null) {
            return parseDateTime(matcher.group("startTime").trim());
        } else {
            return Either.left(null);
        }
    }

    private Either<LocalDateTime, CommandResult> extractEndTime(String argument) {
        Matcher matcher = Pattern.compile("(^|.*? )et/(?<endTime>.*?)(\\s+(#|st)/.*)?$").matcher(argument);

        if (matcher.matches() && matcher.group("endTime") != null) {
            return parseDateTime(matcher.group("endTime").trim());
        } else {
            return Either.left(null);
        }
    }

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

    private Either<LocalDateTime, CommandResult> parseDateTime(String string) {
        if (this.timeParserManager.canParse(string)) {
            return Either.left(this.timeParserManager.delegateTimeParser(string));
        } else {
            return Either.right(makeInvalidDateTimeResult(string));
        }
    }

    private CommandResult makeInvalidArgumentResult() {
        return () -> "Invalid arguments.\n\n" + COMMAND_FORMAT + "\n\n" + CALLOUTS;
    }

    private CommandResult makeInvalidDateTimeResult(String dateTime) {
        return () -> "Cannot parse \"" + dateTime + "\".";
    }

    private CommandResult makeEndTimeBeforeStartTimeResult() {
        return () -> "End time cannot come before start time.";
    }
}
