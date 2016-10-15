package linenux.command;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.parser.SearchKeywordParser;
import linenux.command.result.CommandResult;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.util.Either;
import linenux.util.TasksListUtil;

/**
 * Marks task as done.
 */
public class DoneCommand implements Command {
    private static final String TRIGGER_WORD = "done";
    private static final String DESCRIPTION = "Marks a task as done.";
    private static final String COMMAND_FORMAT = "done KEYWORDS";

    private static final String DONE_PATTERN = "(?i)^done(\\s+(?<keywords>.*))?$";
    private static final String NUMBER_PATTERN = "^\\d+$";
    private static final String CANCEL_PATTERN = "^cancel$";

    private Schedule schedule;
    private boolean requiresUserResponse;
    private ArrayList<Task> foundTasks;
    private SearchKeywordParser searchKeywordParser;

    public DoneCommand(Schedule schedule) {
        this.schedule = schedule;
        this.searchKeywordParser = new SearchKeywordParser(this.schedule);
    }

    @Override
    public boolean respondTo(String userInput) {
        return userInput.matches(DONE_PATTERN);
    }

    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(DONE_PATTERN);
        assert this.schedule != null;

        String keywords = extractKeywords(userInput);

        if (keywords.trim().isEmpty()) {
            return makeNoKeywordsResult();
        }

        Either<ArrayList<Task>, CommandResult> tasks = this.searchKeywordParser.parse(keywords);

        if (tasks.getLeft() != null) {
            if (tasks.getLeft().size() == 1) {
                this.schedule.doneTask(tasks.getLeft().get(0));
                return makeDoneTask(tasks.getLeft().get(0));
            } else {
                setResponse(true, tasks.getLeft());
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
                task.markAsDone();

                setResponse(false, null);
                return makeDoneTask(task);
            } else {
                return makeInvalidIndexResult();
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
        Matcher matcher = Pattern.compile(DONE_PATTERN).matcher(userInput);

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

    private CommandResult makeDoneTask(Task task) {
        return () -> "\"" + task.getTaskName() + "\" is marked as done.";
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
        return () -> "OK! Not marking any task as done.";
    }

    private CommandResult makeInvalidUserResponse(String userInput) {
        return () -> {
            StringBuilder builder = new StringBuilder();
            builder.append("I don't understand \"" + userInput + "\".\n");
            builder.append("Enter a number to indicate which task to mark as done.\n");
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
