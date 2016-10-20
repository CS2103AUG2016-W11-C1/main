package linenux.command;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.parser.SearchKeywordParser;
import linenux.command.result.CommandResult;
import linenux.command.result.PromptResults;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.util.Either;
import linenux.util.TasksListUtil;

/**
 * Deletes a task from the schedule.
 */
public class DeleteCommand implements Command {
    private static final String TRIGGER_WORD = "delete";
    private static final String DESCRIPTION = "Deletes a task from the schedule.";
    private static final String COMMAND_FORMAT = "delete KEYWORDS";

    private static final String DELETE_PATTERN = "(?i)^delete(\\s+(?<keywords>.*))?$";
    private static final String NUMBER_PATTERN = "^\\d+$";
    private static final String CANCEL_PATTERN = "^cancel$";

    private Schedule schedule;
    private boolean requiresUserResponse;
    private ArrayList<Task> foundTasks;
    private SearchKeywordParser searchKeywordParser;

    public DeleteCommand(Schedule schedule) {
        this.schedule = schedule;
        this.searchKeywordParser = new SearchKeywordParser(this.schedule);
    }

    @Override
    public boolean respondTo(String userInput) {
        return userInput.matches(DELETE_PATTERN);
    }

    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(DELETE_PATTERN);
        assert this.schedule != null;

        String keywords = extractKeywords(userInput);

        if (keywords.trim().isEmpty()) {
            return makeNoKeywordsResult();
        }

        Either<ArrayList<Task>, CommandResult> tasks = this.searchKeywordParser.parse(keywords);

        if (tasks.isLeft()) {
            if (tasks.getLeft().size() == 1) {
                this.schedule.deleteTask(tasks.getLeft().get(0));
                return makeDeletedTask(tasks.getLeft().get(0));
            } else {
                setResponse(true, tasks.getLeft());
                return PromptResults.makePromptIndexResult(tasks.getLeft());
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
        assert this.foundTasks != null;
        assert this.schedule != null;

        if (userInput.matches(NUMBER_PATTERN)) {
            int index = Integer.parseInt(userInput);

            if (1 <= index && index <= this.foundTasks.size()) {
                Task task = this.foundTasks.get(index - 1);
                this.schedule.deleteTask(task);

                setResponse(false, null);
                return makeDeletedTask(task);
            } else {
                return PromptResults.makeInvalidIndexResult(this.foundTasks);
            }
        } else if (userInput.matches(CANCEL_PATTERN)) {
            setResponse(false, null);
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
        Matcher matcher = Pattern.compile(DELETE_PATTERN).matcher(userInput);

        if (matcher.matches() && matcher.group("keywords") != null) {
            return matcher.group("keywords");
        } else {
            return "";
        }
    }

    private void setResponse(boolean requiresUserResponse, ArrayList<Task> foundTasks) {
        this.requiresUserResponse = requiresUserResponse;
        this.foundTasks = foundTasks;
    }

    private CommandResult makeNoKeywordsResult() {
        return () -> "Invalid arguments.\n\n" + COMMAND_FORMAT + "\n\n" + CALLOUTS;
    }

    private CommandResult makeDeletedTask(Task task) {
        return () -> "Deleted \"" + task.getTaskName() + "\".";
    }

    private CommandResult makeCancelledResult() {
        return () -> "OK! Not deleting anything.";
    }

    private CommandResult makeInvalidUserResponse(String userInput) {
        return () -> {
            StringBuilder builder = new StringBuilder();
            builder.append("I don't understand \"" + userInput + "\".\n");
            builder.append("Enter a number to indicate which task to delete.\n");
            builder.append(TasksListUtil.display(this.foundTasks));
            return builder.toString();
        };
    }
}
