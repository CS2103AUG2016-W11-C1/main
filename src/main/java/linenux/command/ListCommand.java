package linenux.command;

import java.util.ArrayList;

import linenux.command.filter.ListArgumentFilter;
import linenux.command.parser.GenericParser;
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

/**
 * Generates a list of tasks based on userInput.
 */
public class ListCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "list";
    private static final String DESCRIPTION = "Lists tasks and reminders.";
    private static final String COMMAND_FORMAT = "list [KEYWORDS] [st/START_TIME] [et/END_TIME] [#/TAG] [d/DONE]";
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
    /**
     * Executes the command based on {@code userInput}. This method operates under the assumption that
     * {@code respondTo(userInput)} is {@code true}.
     * @param userInput A {@code String} representing the user input.
     * @return A {@code CommandResult} representing the result of the command.
     */
    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(getPattern());
        assert this.schedule != null;

        ArrayList<Task> tasks = this.schedule.getTaskList();
        ArrayList<Reminder> reminders = this.schedule.getReminderList();

        if (tasks.isEmpty() && reminders.isEmpty()) {
            return makeEmptyTaskListResult();
        }

        String arguments = extractArgument(userInput);
        GenericParser parser = new GenericParser();
        GenericParser.GenericParserResult result = parser.parse(arguments);
        Either<String, CommandResult> viewDone = extractViewDone(result);

        if (viewDone.isRight()) {
            return viewDone.getRight();
        }

        if (!result.getKeywords().isEmpty()) {
            tasks = this.schedule.searchTasks(result.getKeywords());
            reminders = this.schedule.searchReminders(result.getKeywords());
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
        if (!doneOnly) {
            Either<ArrayList<Reminder>, CommandResult> filterReminders = this.listArgumentFilter.filterReminders(arguments, reminders);
            if (filterReminders.isRight()) {
                return filterReminders.getRight();
            }

            actualFilterReminders = filterReminders.getLeft();
        }

        //Remove all done tasks if field d/ is not yes amd all
        if (!actualViewDone.equals(VIEW_DONE) && !actualViewDone.equals(VIEW_DONE_ONLY)) {
            actualFilterTasks = this.listArgumentFilter.filterUndoneTasks(actualFilterTasks);
        }


        if (actualFilterTasks.size() == 0 && actualFilterReminders.size() == 0) {
            this.schedule.addFilterTasks(new ArrayList<>());
            return makeNoTasksAndRemindersFoundResult();
        } else {
            return makeResult(actualFilterTasks, actualFilterReminders);
        }
    }

    //@@author A0144915A
    /**
     * @return A {@code String} representing the default command word.
     */
    @Override
    public String getTriggerWord() {
        return TRIGGER_WORD;
    }

    /**
     * @return A {@code String} describing what this {@code Command} does.
     */
    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    //@@author A0135788M
    /**
     * @return A {@code String} describing the format that this {@code Command} expects.
     */
    @Override
    public String getCommandFormat() {
        return COMMAND_FORMAT;
    }

    //@@author A0140702X
    private Either<String, CommandResult> extractViewDone(GenericParser.GenericParserResult result) {
        ArrayList<String> flags = result.getArguments("d");

        if (flags.size() == 0) {
            return Either.left("");
        } else {
            return parseViewDone(flags.get(0));
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
                return "Reminders:\n" + ArrayListUtil.display(reminders);
            }
        };
    }
}
