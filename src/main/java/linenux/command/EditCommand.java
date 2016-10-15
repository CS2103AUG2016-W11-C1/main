package linenux.command;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.parser.EditTaskArgumentParser;
import linenux.command.parser.SearchKeywordParser;
import linenux.command.result.CommandResult;
import linenux.control.TimeParserManager;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.time.parser.ISODateWithTimeParser;
import linenux.util.Either;
import linenux.util.TasksListUtil;

/**
 * Edits a task in the schedule.
 */
public class EditCommand implements Command {
    private static final String TRIGGER_WORD = "edit";
    private static final String DESCRIPTION = "Edits a task in the schedule.";
    public static final String COMMAND_FORMAT = "edit KEYWORDS... [n/NEW_NAME][st/START_TIME][et/END_TIME]";

    private static final String EDIT_PATTERN = "(?i)^edit((?<keywords>.*?)(?<arguments>((n|st|et)/)+?.*)??)";
    private static final String NUMBER_PATTERN = "^\\d+$";
    private static final String CANCEL_PATTERN = "^cancel$";

    private Schedule schedule;
    private boolean requiresUserResponse;
    private String argument;
    private ArrayList<Task> foundTasks;
    private SearchKeywordParser searchKeywordParser;
    private TimeParserManager timeParserManager;
    private EditTaskArgumentParser editTaskArgumentParser;

    public EditCommand(Schedule schedule) {
        this.schedule = schedule;
        this.searchKeywordParser = new SearchKeywordParser(this.schedule);
        this.timeParserManager = new TimeParserManager(new ISODateWithTimeParser());
        this.editTaskArgumentParser = new EditTaskArgumentParser(this.timeParserManager, COMMAND_FORMAT, CALLOUTS);
    }

    @Override
    public boolean respondTo(String userInput) {
        return userInput.matches(EDIT_PATTERN);
    }

    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(EDIT_PATTERN);
        assert this.schedule != null;

        String keywords = extractKeywords(userInput);

        if (keywords.trim().isEmpty()) {
            return makeNoKeywordsResult();
        }

        Either<ArrayList<Task>, CommandResult> tasks = this.searchKeywordParser.parse(keywords);

        if (tasks.getLeft() != null) {
            String argument = extractArgument(userInput);

            if (tasks.getLeft().size() == 1) {
                return implementEdit(tasks.getLeft().get(0), argument);
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
        if (userInput.matches(NUMBER_PATTERN)) {
            int index = Integer.parseInt(userInput);

            if (1 <= index && index <= this.foundTasks.size()) {
                Task task = this.foundTasks.get(index - 1);
                CommandResult result = implementEdit(task, this.argument);
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
        Matcher matcher = Pattern.compile(EDIT_PATTERN).matcher(userInput);

        if (matcher.matches() && matcher.group("keywords") != null) {
            return matcher.group("keywords").trim(); //TODO
        } else {
            return "";
        }
    }

    private String extractArgument(String userInput) {
        Matcher matcher = Pattern.compile(EDIT_PATTERN).matcher(userInput);

        if (matcher.matches() && matcher.group("arguments") != null) {
            return matcher.group("arguments");
        } else {
            return "";
        }
    }

    private CommandResult implementEdit(Task original, String argument) {
        Either<Task, CommandResult> result = editTaskArgumentParser.parse(original, argument);

        if (result.isLeft()) {
            this.schedule.editTask(original, result.getLeft());
            return makeEditedTask(original, result.getLeft());
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

    private CommandResult makeEditedTask(Task original, Task task) {
        return () -> "Edited \"" + original.getTaskName() + "\".\nNew task details: " + task.toString();
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
        return () -> "OK! Not editing anything.";
    }

    private CommandResult makeInvalidUserResponse(String userInput) {
        return () -> {
            StringBuilder builder = new StringBuilder();
            builder.append("I don't understand \"" + userInput + "\".\n");
            builder.append("Enter a number to indicate which task to edit.\n");
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
