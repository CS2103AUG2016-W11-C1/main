package linenux.command;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.parser.GenericParser;
import linenux.command.parser.ReminderArgumentParser;
import linenux.command.result.CommandResult;
import linenux.command.result.PromptResults;
import linenux.command.result.SearchResults;
import linenux.control.TimeParserManager;
import linenux.model.Reminder;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.time.parser.ISODateWithTimeParser;
import linenux.time.parser.StandardDateWithTimeParser;
import linenux.time.parser.TodayWithTimeParser;
import linenux.time.parser.TomorrowWithTimeParser;
import linenux.util.Either;
import linenux.util.TasksListUtil;

/**
 * Adds a reminder to a task in the schedule
 */
public class RemindCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "remind";
    private static final String DESCRIPTION = "Adds a reminder to a task in the schedule.";
    public static final String COMMAND_FORMAT = "remind KEYWORDS t/TIME n/NOTE";

    private static final String NUMBER_PATTERN = "^\\d+$";
    private static final String CANCEL_PATTERN = "^cancel$";

    private Schedule schedule;
    private boolean requiresUserResponse;
    private GenericParser.GenericParserResult parseResult;
    private ArrayList<Task> foundTasks;
    private TimeParserManager timeParserManager;
    private ReminderArgumentParser reminderArgumentParser;

    //@@author A0135788M
    public RemindCommand(Schedule schedule) {
        this.schedule = schedule;
        this.timeParserManager = new TimeParserManager(new ISODateWithTimeParser(), new StandardDateWithTimeParser(), new TodayWithTimeParser(), new TomorrowWithTimeParser());
        this.reminderArgumentParser = new ReminderArgumentParser(this.timeParserManager, COMMAND_FORMAT, CALLOUTS);
        this.TRIGGER_WORDS.add(TRIGGER_WORD);
    }

    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(getPattern());
        assert this.schedule != null;

        String argument = extractArgument(userInput);
        GenericParser parser = new GenericParser();
        GenericParser.GenericParserResult result = parser.parse(argument);

        if (result.getKeywords().isEmpty()) {
            return makeNoKeywordsResult();
        }

        ArrayList<Task> tasks = this.schedule.search(result.getKeywords());

        if (tasks.size() == 0) {
            return SearchResults.makeNotFoundResult(result.getKeywords());
        } else if (tasks.size() == 1) {
            Task task = tasks.get(0);
            return implementRemind(task, result);
        } else {
            setResponse(true, tasks, result);
            return PromptResults.makePromptIndexResult(tasks);
        }
    }

    //@@author A0135788M
    @Override
    public boolean awaitingUserResponse() {
        return requiresUserResponse;
    }

    @Override
    public CommandResult getUserResponse(String userInput) {
        assert this.parseResult != null;
        assert this.foundTasks != null;
        assert this.schedule != null;

        if (userInput.matches(NUMBER_PATTERN)) {
            int index = Integer.parseInt(userInput);

            if (1 <= index && index <= this.foundTasks.size()) {
                Task task = this.foundTasks.get(index - 1);
                CommandResult result = implementRemind(task, this.parseResult);
                setResponse(false, null, null);
                return result;
            } else {
                return PromptResults.makeInvalidIndexResult(this.foundTasks);
            }
        } else if (userInput.matches(CANCEL_PATTERN)) {
            setResponse(false, null, null);
            return makeCancelledResult();
        } else {
            return makeInvalidUserResponse(userInput);
        }
    }

    //@@author A0135788M
    @Override
    public String getTriggerWord() {
        return TRIGGER_WORD;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public String getCommandFormat() {
        return COMMAND_FORMAT;
    }

    private String extractArgument(String userInput) {
        Matcher matcher = Pattern.compile(getPattern()).matcher(userInput);

        if (matcher.matches() && matcher.group("keywords") != null) {
            return matcher.group("keywords").trim(); // TODO
        } else {
            return "";
        }
    }

    private CommandResult implementRemind(Task original, GenericParser.GenericParserResult parseResult) {
        Either<Reminder, CommandResult> result = reminderArgumentParser.parse(parseResult);

        if (result.isLeft()) {
            this.schedule.updateTask(original, original.addReminder(result.getLeft()));
            return makeResult(original, result.getLeft());
        } else {
            return result.getRight();
        }
    }

    private void setResponse(boolean requiresUserResponse, ArrayList<Task> foundTasks, GenericParser.GenericParserResult result) {
        this.requiresUserResponse = requiresUserResponse;
        this.foundTasks = foundTasks;
        this.parseResult = result;
    }

    private CommandResult makeNoKeywordsResult() {
        return () -> "Invalid arguments.\n\n" + COMMAND_FORMAT + "\n\n" + CALLOUTS;
    }

    private CommandResult makeResult(Task task, Reminder reminder) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h.mma");

        return () -> "Added reminder on " + reminder.getTimeOfReminder().format(formatter) + " for "
                + task.getTaskName();
    }

    private CommandResult makeCancelledResult() {
        return () -> "OK! Not adding new reminder.";
    }

    private CommandResult makeInvalidUserResponse(String userInput) {
        return () -> {
            StringBuilder builder = new StringBuilder();
            builder.append("I don't understand \"" + userInput + "\".\n");
            builder.append("Enter a number to indicate which task to add reminder to:\n");
            builder.append(TasksListUtil.display(this.foundTasks));
            return builder.toString();
        };
    }
}
