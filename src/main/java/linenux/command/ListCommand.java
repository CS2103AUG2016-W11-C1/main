package linenux.command;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.filter.ListArgumentFilter;
import linenux.command.result.CommandResult;
import linenux.control.TimeParserManager;
import linenux.model.Reminder;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.time.parser.ISODateWithTimeParser;
import linenux.time.parser.StandardDateWithTimeParser;
import linenux.time.parser.TodayWithTimeParser;
import linenux.time.parser.TomorrowWithTimeParser;
import linenux.util.ArrayListUtil;
import linenux.util.Either;
import linenux.util.RemindersListUtil;

/**
 * Generates a list of tasks based on userInput.
 */
public class ListCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "list";
    private static final String DESCRIPTION = "Lists tasks and reminders.";
    private static final String COMMAND_FORMAT = "list [KEYWORDS...] [st/START_TIME] [et/END_TIME] [#/TAG]";
    private static final String VIEW_DONE_ONLY = "yes";
    private static final String VIEW_DONE = "all";

    private Schedule schedule;
    private TimeParserManager timeParserManager;
    private ListArgumentFilter listArgumentFilter;

    //@@author A0140702X
    public ListCommand(Schedule schedule) {
        this.schedule = schedule;
        this.timeParserManager = new TimeParserManager(new ISODateWithTimeParser(), new StandardDateWithTimeParser(), new TodayWithTimeParser(), new TomorrowWithTimeParser());
        this.listArgumentFilter = new ListArgumentFilter(this.timeParserManager, COMMAND_FORMAT, CALLOUTS);
        this.TRIGGER_WORDS.add(TRIGGER_WORD);
    }

    //@@author A0140702X
    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(getPattern());
        assert this.schedule != null;

        ArrayList<Task> tasks = this.schedule.getTaskList();
        ArrayList<Task> doneTasks = new ArrayList<>();
        ArrayList<Reminder> reminders = this.schedule.getReminderList();

        if (tasks.isEmpty() && reminders.isEmpty()) {
            return makeEmptyTaskListResult();
        }

        String keywords = extractKeywords(userInput);
        String arguments = extractArgument(userInput);
        Either<String, CommandResult> viewDone = extractViewDone(arguments);

        if (viewDone.isRight()) {
            return viewDone.getRight();
        }

        if (!keywords.trim().isEmpty()) {
            tasks = this.schedule.search(keywords);
            reminders = this.schedule.searchReminder(keywords);
        }

        String actualViewDone = viewDone.getLeft();
        Boolean doneOnly = actualViewDone.equals(VIEW_DONE_ONLY);

        Either<ArrayList<Task>, CommandResult> filterTasks = this.listArgumentFilter.filter(arguments, tasks, doneOnly);
        if (filterTasks.isRight()) {
            return filterTasks.getRight();
        }

        ArrayList<Task> actualFilterTasks = filterTasks.getLeft();
        ArrayList<Reminder> actualFilterReminders = new ArrayList<Reminder>();

        //If users request for done tasks only, we will not show any reminders
        if (doneOnly) {
            //does no search for reminders, leaves it as an empty ArrayList
        } else {
            Either<ArrayList<Reminder>, CommandResult> filterReminders = this.listArgumentFilter.filterReminders(arguments, reminders);
            if (filterReminders.isRight()) {
                return filterReminders.getRight();
            }

            actualFilterReminders = filterReminders.getLeft();
        }

        //Remove all done tasks if field d/ is not yes amd all
        if (actualViewDone.equals(VIEW_DONE) || actualViewDone.equals(VIEW_DONE_ONLY)) {
            //no filtering of done tasks occurs.
        } else {
            actualFilterTasks = this.listArgumentFilter.filterUndoneTasks(actualFilterTasks);
        }


        if (actualFilterTasks.size() == 0 && actualFilterReminders.size() == 0) {
            this.schedule.addFilterTasks(new ArrayList<Task>());
            return makeNoTasksAndRemindersFoundResult();
        } else {
            return makeResult(actualFilterTasks, actualFilterReminders);
        }
    }

    //@@author A0144915A
    @Override
    public String getTriggerWord() {
        return TRIGGER_WORD;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    //@@author A0135788M
    @Override
    public String getCommandFormat() {
        return COMMAND_FORMAT;
    }

    //@@author A0140702X
    @Override
    public String getPattern() {
        return "(?i)^\\s*(" + getTriggerWordsPattern() + ")((?<keywords>.*?)(?<arguments>((st|et|#|d)/)+?.*)??)";
    }

    //@@author A0135788M
    private String extractKeywords(String userInput) {
        Matcher matcher = Pattern.compile(getPattern()).matcher(userInput);

        if (matcher.matches() && matcher.group("keywords") != null) {
            return matcher.group("keywords").trim();
        } else {
            return "";
        }
    }

    //@@author A0140702X
    private String extractArgument(String userInput) {
        Matcher matcher = Pattern.compile(getPattern()).matcher(userInput);

        if (matcher.matches() && matcher.group("arguments") != null) {
            return matcher.group("arguments");
        } else {
            return "";
        }
    }

    private Either<String, CommandResult> extractViewDone(String argument) {
        Matcher matcher = Pattern.compile("(?i)(^|.*? )d/(?<done>.*?)(\\s+(#|st|et)/.*)?$").matcher(argument);

        if (matcher.matches() && matcher.group("done") != null) {
            return parseViewDone(matcher.group("done").trim());
        } else {
            return Either.left("");
        }
    }

    private Either<String, CommandResult> parseViewDone(String string) {
        if (string.toLowerCase().equals(VIEW_DONE)) {
            return Either.left(VIEW_DONE);
        } else if (string.toLowerCase().equals(VIEW_DONE_ONLY)){
            return Either.left(VIEW_DONE_ONLY);
        }

        return Either.right(makeInvalidViewDoneResult(string));
    }

    private CommandResult makeInvalidViewDoneResult(String viewDone) {
        return () -> "Unable to parse \"" + viewDone + "\".\n" + "Did you mean:\n"
                + "d/" + VIEW_DONE + " - View all done and uncompleted tasks.\n"
                + "d/" + VIEW_DONE_ONLY + " - Show only tasks that are marked done.";
    }

    private CommandResult makeEmptyTaskListResult() {
        return () -> "You have no tasks and reminders to list!";
    }

    private CommandResult makeNoTasksAndRemindersFoundResult() {
        return () -> "There are no tasks and reminders found based on your given inputs!";
    }

    private CommandResult makeResult(ArrayList<Task> tasks, ArrayList<Reminder> reminders) {
        this.schedule.addFilterTasks(tasks);

        return () -> {
            if (reminders.isEmpty()) {
                return "";
            } else {
                return "Reminders:\n" + RemindersListUtil.display(reminders);
            }
        };
    }
}
