package linenux.command;

import java.util.ArrayList;

import linenux.command.result.CommandResult;
import linenux.command.result.PromptResults;
import linenux.command.result.SearchResults;
import linenux.model.Reminder;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.util.ArrayListUtil;

//@@author A0135788M
/**
 * Generates details of tasks and reminders attached to task based on userInput.
 */
public class ViewCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "view";
    private static final String DESCRIPTION = "Views details of specific task.";
    private static final String COMMAND_FORMAT = "view KEYWORDS";

    private static final String NUMBER_PATTERN = "^\\d+$";
    private static final String CANCEL_PATTERN = "^cancel$";

    private Schedule schedule;
    private boolean requiresUserResponse;
    private ArrayList<Task> foundTasks;

    public ViewCommand(Schedule schedule) {
        this.schedule = schedule;
        this.TRIGGER_WORDS.add(TRIGGER_WORD);
    }

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

        String keywords = extractArgument(userInput);

        if (keywords.trim().isEmpty()) {
            return makeNoKeywordsResult();
        }

        ArrayList<Task> tasks = this.schedule.searchTasks(keywords);

        if (tasks.size() == 0) {
            return SearchResults.makeNotFoundResult(keywords);
        } else if (tasks.size() == 1) {
            Task task = tasks.get(0);
            return makeResult(task);
        } else {
            setResponse(true, tasks);
            return PromptResults.makePromptIndexResult(tasks);
        }
    }

    /**
     * @return {@code true} if and only if this {@code Command} is awaiting for user response.
     */
    @Override
    public boolean isAwaitingUserResponse() {
        return requiresUserResponse;
    }

    /**
     * Process the response given by the user.
     * @param userInput {@code String} representing the user response.
     * @return A {@code CommandResult}, which is the result of processing {@code userInput}.
     */
    @Override
    public CommandResult processUserResponse(String userInput) {
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

    /**
     * @return A {@code String} describing the format that this {@code Command} expects.
     */
    @Override
    public String getCommandFormat() {
        return COMMAND_FORMAT;
    }

    /**
     * Updates the user response status.
     * @param requiresUserResponse Whether or not this {@code Command} is expecting user response.
     * @param foundTasks An {@code ArrayList} of {@code Task} matching some search criteria.
     */
    private void setResponse(boolean requiresUserResponse, ArrayList<Task> foundTasks) {
        this.requiresUserResponse = requiresUserResponse;
        this.foundTasks = foundTasks;
    }

    /**
     * @return A {@code CommandResult} indicating that the user does not specify a keywords.
     */
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
            builder.append(ArrayListUtil.display(reminders));
        }

        return () -> builder.toString().trim();

    }

    /**
     * @return A {@code CommandResult} indicating that the view operation is cancelled.
     */
    private CommandResult makeCancelledResult() {
        return () -> "OK! Not viewing any task.";
    }

    /**
     * @param userInput A {@code String} representing the user response.
     * @return A {@code CommandResult} indicating that {@code userInput} is invalid.
     */
    private CommandResult makeInvalidUserResponse(String userInput) {
        return () -> {
            StringBuilder builder = new StringBuilder();
            builder.append("I don't understand \"" + userInput + "\".\n");
            builder.append("Enter a number to indicate which task to view.\n");
            builder.append(ArrayListUtil.display(this.foundTasks));
            return builder.toString();
        };
    }
}
