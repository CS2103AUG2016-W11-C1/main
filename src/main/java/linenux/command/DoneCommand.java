package linenux.command;

import java.util.ArrayList;

import linenux.command.result.CommandResult;
import linenux.command.result.PromptResults;
import linenux.command.result.SearchResults;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.util.ArrayListUtil;

/**
 * Marks task as done.
 */
public class DoneCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "done";
    private static final String DESCRIPTION = "Marks a task as done.";
    private static final String COMMAND_FORMAT = "done KEYWORDS";

    private static final String NUMBER_PATTERN = "^\\d+$";
    private static final String CANCEL_PATTERN = "^cancel$";

    private Schedule schedule;
    private boolean requiresUserResponse;
    private ArrayList<Task> foundTasks;

    //@@author A0135788M
    /**
     * Constructs an {@code AliasCommand}.
     * @param schedule The {@code Schedule} to search and update {@code Task} from.
     */
    public DoneCommand(Schedule schedule) {
        this.schedule = schedule;
        this.TRIGGER_WORDS.add(TRIGGER_WORD);
    }

    //@@author A0144915
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

        ArrayList<Task> tasks = new ArrayListUtil.ChainableArrayListUtil<>(this.schedule.searchTasks(keywords))
                .filter(Task::isNotDone)
                .value();

        if (tasks.size() == 0) {
            return SearchResults.makeNotFoundResult(keywords);
        } else if (tasks.size() == 1) {
            Task task = tasks.get(0);
            this.schedule.updateTask(task, task.markAsDone());
            return makeDoneTask(task);
        } else {
            setResponse(true, tasks);
            return PromptResults.makePromptIndexResult(tasks);
        }
    }

    //@@author A0135788M
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
                this.schedule.updateTask(task, task.markAsDone());

                setResponse(false, null);
                return makeDoneTask(task);
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

    //@@author A0144915A
    /**
     * @return A {@code String} representing the default command word.
     */
    @Override
    public String getTriggerWord() {
        return TRIGGER_WORD;
    }

    //@@author A0135788M
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

    /**
     * @param task The {@code Task} that is marked as done.
     * @return A {@code CommandResult} informing the user that {@code task} has been marked as done.
     */
    private CommandResult makeDoneTask(Task task) {
        return () -> "\"" + task.getTaskName() + "\" is marked as done.";
    }

    /**
     * @return A {@code CommandResult} indicating that the done operation is cancelled.
     */
    private CommandResult makeCancelledResult() {
        return () -> "OK! Not marking any task as done.";
    }

    /**
     * @param userInput A {@code String} representing the user response.
     * @return A {@code CommandResult} indicating that {@code userInput} is invalid.
     */
    private CommandResult makeInvalidUserResponse(String userInput) {
        return () -> {
            StringBuilder builder = new StringBuilder();
            builder.append("I don't understand \"" + userInput + "\".\n");
            builder.append("Enter a number to indicate which task to mark as done.\n");
            builder.append(ArrayListUtil.display(this.foundTasks));
            return builder.toString();
        };
    }
}
