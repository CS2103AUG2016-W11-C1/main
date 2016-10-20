package linenux.command;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.parser.ReminderArgumentParser;
import linenux.command.parser.SearchKeywordParser;
import linenux.command.result.CommandResult;
import linenux.control.TimeParserManager;
import linenux.model.Reminder;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.time.parser.ISODateWithTimeParser;
import linenux.util.Either;
import linenux.util.TasksListUtil;

/**
 * Adds a reminder to a task in the schedule
 */
public class RemindCommand implements Command {
    private static final String TRIGGER_WORD = "remind";
    private static final String DESCRIPTION = "Adds a reminder to a task in the schedule.";
    public static final String COMMAND_FORMAT = "remind KEYWORDS t/TIME [n/NOTE]";

    private static final String REMIND_PATTERN = "(?i)^remind((?<keywords>.*?)(?<arguments>((n|t)/)+?.*)??)";
    private static final String NUMBER_PATTERN = "^\\d+$";
    private static final String CANCEL_PATTERN = "^cancel$";

    private Schedule schedule;
    private boolean requiresUserResponse;
    private String argument;
    private ArrayList<Task> foundTasks;
    private SearchKeywordParser searchKeywordParser;
    private TimeParserManager timeParserManager;
    private ReminderArgumentParser reminderArgumentParser;

    public RemindCommand(Schedule schedule) {
        this.schedule = schedule;
        this.searchKeywordParser = new SearchKeywordParser(this.schedule);
        this.timeParserManager = new TimeParserManager(new ISODateWithTimeParser());
        this.reminderArgumentParser = new ReminderArgumentParser(this.timeParserManager, COMMAND_FORMAT, CALLOUTS);
    }

    @Override
    public boolean respondTo(String userInput) {
        return userInput.matches(REMIND_PATTERN);
    }

    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(REMIND_PATTERN);
        assert this.schedule != null;

        String keywords = extractKeywords(userInput);

        if (keywords.trim().isEmpty()) {
            return makeNoKeywordsResult();
        }

        Either<ArrayList<Task>, CommandResult> tasks = this.searchKeywordParser.parse(keywords);

        if (tasks.isLeft()) {
            String argument = extractArgument(userInput);

            if (tasks.getLeft().size() == 1) {
                return implementRemind(tasks.getLeft().get(0), argument);
            } else {
                setResponse(true, tasks.getLeft(), argument);
                return makePromptResult(this.foundTasks);
            }
        } else {
            return tasks.getRight();
        }

    }

    @Override
    public boolean awaitingUserResponse() {
        return requiresUserResponse;
    }

    @Override
    public CommandResult userResponse(String userInput) {
        assert this.argument != null;
        assert this.foundTasks != null;
        assert this.schedule != null;

        if (userInput.matches(NUMBER_PATTERN)) {
            int index = Integer.parseInt(userInput);

            if (1 <= index && index <= this.foundTasks.size()) {
                Task task = this.foundTasks.get(index - 1);
                CommandResult result = implementRemind(task, this.argument);
                setResponse(false, null, null);
                return result;
            } else {
                return makeInvalidIndexResult();
            }
        } else if (userInput.matches(CANCEL_PATTERN)) {
            setResponse(false, null, null);
            return makeCancelledResult();
        } else {
            return makeInvalidUserResponse(userInput);
        }
    }

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

    private String extractKeywords(String userInput) {
        Matcher matcher = Pattern.compile(REMIND_PATTERN).matcher(userInput);

        if (matcher.matches() && matcher.group("keywords") != null) {
            return matcher.group("keywords").trim(); // TODO
        } else {
            return "";
        }
    }

    private String extractArgument(String userInput) {
        Matcher matcher = Pattern.compile(REMIND_PATTERN).matcher(userInput);

        if (matcher.matches() && matcher.group("arguments") != null) {
            return matcher.group("arguments");
        } else {
            return "";
        }
    }

    private CommandResult implementRemind(Task original, String argument) {
        Either<Reminder, CommandResult> result = reminderArgumentParser.parse(argument);

        if (result.isLeft()) {
            this.schedule.updateTask(original, original.addReminder(result.getLeft()));
            return makeResult(original, result.getLeft());
        } else {
            return result.getRight();
        }
    }

    private void setResponse(boolean requiresUserResponse, ArrayList<Task> foundTasks, String argument) {
        this.requiresUserResponse = requiresUserResponse;
        this.foundTasks = foundTasks;
        this.argument = argument;
    }

    private CommandResult makeNoKeywordsResult() {
        return () -> "Invalid arguments.\n\n" + COMMAND_FORMAT + "\n\n" + CALLOUTS;
    }

    private CommandResult makeResult(Task task, Reminder reminder) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma");

        return () -> "Added reminder on " + reminder.getTimeOfReminder().format(formatter) + " for "
                + task.getTaskName();
    }

    private CommandResult makePromptResult(ArrayList<Task> tasks) {
        return () -> {
            StringBuilder builder = new StringBuilder();
            builder.append("Which one? (1-");
            builder.append(tasks.size());
            builder.append(")\n");

            builder.append(TasksListUtil.display(tasks));

            return builder.toString();
        };
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

    private CommandResult makeInvalidIndexResult() {
        return () -> {
            StringBuilder builder = new StringBuilder();
            builder.append("That's not a valid index. Enter a number between 1 and ");
            builder.append(this.foundTasks.size());
            builder.append(":\n");
            builder.append(TasksListUtil.display(this.foundTasks));
            return builder.toString();
        };
    }

}