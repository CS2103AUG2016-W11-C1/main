package linenux.command;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.parser.SearchKeywordParser;
import linenux.command.result.CommandResult;
import linenux.command.result.PromptResults;
import linenux.model.Reminder;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.util.Either;
import linenux.util.RemindersListUtil;
import linenux.util.TasksListUtil;

/**
 * Generates details of tasks and reminders attached to task based on userInput.
 */
public class ViewCommand implements Command {
    private static final String TRIGGER_WORD = "view";
    private static final String DESCRIPTION = "Views details of specific task.";
    private static final String COMMAND_FORMAT = "view KEYWORDS";

    private static final String VIEW_PATTERN = "(?i)^view(\\s+(?<keywords>.*))?$";
    private static final String NUMBER_PATTERN = "^\\d+$";
    private static final String CANCEL_PATTERN = "^cancel$";

    private Schedule schedule;
    private boolean requiresUserResponse;
    private ArrayList<Task> foundTasks;
    private SearchKeywordParser searchKeywordParser;

    public ViewCommand(Schedule schedule) {
        this.schedule = schedule;
        this.searchKeywordParser = new SearchKeywordParser(this.schedule);
    }

    @Override
    public boolean respondTo(String userInput) {
        return userInput.matches(VIEW_PATTERN);
    }

    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(VIEW_PATTERN);
        assert this.schedule != null;

        String keywords = extractKeywords(userInput);

        if (keywords.trim().isEmpty()) {
            return makeNoKeywordsResult();
        }

        Either<ArrayList<Task>, CommandResult> tasks = this.searchKeywordParser.parse(keywords);

        if (tasks.isLeft()) {
            if (tasks.getLeft().size() == 1) {
                return makeResult(tasks.getLeft().get(0));
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

                setResponse(false, null);
                return makeResult(task);
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
        Matcher matcher = Pattern.compile(VIEW_PATTERN).matcher(userInput);

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

    private CommandResult makeResult(Task task) {
        ArrayList<Reminder> reminders = task.getReminders();
        StringBuilder builder = new StringBuilder();
        builder.append(task.toString());
        builder.append('\n');
        builder.append("Reminders:" + '\n');

        if (reminders.size() == 0) {
            builder.append("You have not set any reminders for this task.");
        } else {
            builder.append(RemindersListUtil.display(reminders));
        }

        return () -> builder.toString().trim();

    }

    private CommandResult makeCancelledResult() {
        return () -> "OK! Not viewing any task.";
    }

    private CommandResult makeInvalidUserResponse(String userInput) {
        return () -> {
            StringBuilder builder = new StringBuilder();
            builder.append("I don't understand \"" + userInput + "\".\n");
            builder.append("Enter a number to indicate which task to view.\n");
            builder.append(TasksListUtil.display(this.foundTasks));
            return builder.toString();
        };
    }
}
