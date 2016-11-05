package linenux.command;

import java.util.ArrayList;

import linenux.command.parser.EditArgumentParser;
import linenux.command.parser.GenericParser;
import linenux.command.result.CommandResult;
import linenux.command.result.PromptResults;
import linenux.command.result.SearchResults;
import linenux.control.TimeParserManager;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.time.parser.ISODateWithTimeParser;
import linenux.time.parser.StandardDateWithTimeParser;
import linenux.time.parser.TodayWithTimeParser;
import linenux.time.parser.TomorrowWithTimeParser;
import linenux.util.Either;
import linenux.util.TasksListUtil;

/**
 * Edits a task in the schedule.
 */
public class EditCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "edit";
    private static final String DESCRIPTION = "Edits a task in the schedule.";
    public static final String COMMAND_FORMAT = "edit KEYWORDS... [n/NEW_NAME][st/START_TIME][et/END_TIME][#/TAG...]...";

    private static final String NUMBER_PATTERN = "^\\d+$";
    private static final String CANCEL_PATTERN = "^cancel$";

    private Schedule schedule;
    private boolean requiresUserResponse;
    private GenericParser.GenericParserResult parseResult;
    private ArrayList<Task> foundTasks;
    private TimeParserManager timeParserManager;
    private EditArgumentParser editArgumentParser;

    //@@author A0127694U
    /**
     * Constructs an {@code EditCommand}.
     * @param schedule The {@code Schedule} to search and edit {@code Task} from.
     */
    public EditCommand(Schedule schedule) {
        this.schedule = schedule;
        this.timeParserManager = new TimeParserManager(new ISODateWithTimeParser(), new StandardDateWithTimeParser(), new TodayWithTimeParser(), new TomorrowWithTimeParser());
        this.editArgumentParser = new EditArgumentParser(this.timeParserManager, COMMAND_FORMAT, CALLOUTS);
        this.TRIGGER_WORDS.add(TRIGGER_WORD);
    }

    //@@author A0135788M
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
            return implementEdit(task, result);
        } else {
            setResponse(true, tasks, result);
            return PromptResults.makePromptIndexResult(tasks);
        }
    }

    //@@author A0127694U
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
        assert this.parseResult != null;
        assert this.foundTasks != null;
        assert this.schedule != null;

        if (userInput.matches(NUMBER_PATTERN)) {
            int index = Integer.parseInt(userInput);

            if (1 <= index && index <= this.foundTasks.size()) {
                Task task = this.foundTasks.get(index - 1);
                CommandResult result = implementEdit(task, this.parseResult);
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
     * Performs the actual editing.
     * @param original The original {@code Task} to edit.
     * @param parseResult The result of parsing user argument.
     * @return A {@code CommandResult} for the operation.
     */
    private CommandResult implementEdit(Task original, GenericParser.GenericParserResult parseResult) {
        Either<Task, CommandResult> result = editArgumentParser.parse(original, parseResult);

        if (result.isRight()) {
            return result.getRight();
        }

        Task editedTask = result.getLeft();

        // Will still allow edit if the edited task is equals to the original
        // task.
        if (this.schedule.isUniqueTask(editedTask) || editedTask.equals(original)) {
            this.schedule.updateTask(original, result.getLeft());
            return makeEditedTask(original, result.getLeft());
        } else {
            return makeDuplicateTaskResult(editedTask);
        }
    }

    //@@author A0135788M
    /**
     * Updates the user response status.
     * @param requiresUserResponse Whether or not this {@code Command} is expecting user response.
     * @param foundTasks An {@code ArrayList} of {@code Task} matching some search criteria.
     * @param result The result of parsing the user argument.
     */
    private void setResponse(boolean requiresUserResponse, ArrayList<Task> foundTasks, GenericParser.GenericParserResult result) {
        this.requiresUserResponse = requiresUserResponse;
        this.foundTasks = foundTasks;
        this.parseResult = result;
    }

    /**
     * @return A {@code CommandResult} indicating that the user does not specify a keywords.
     */
    private CommandResult makeNoKeywordsResult() {
        return () -> "Invalid arguments.\n\n" + COMMAND_FORMAT + "\n\n" + CALLOUTS;
    }

    //@@author A0127694U
    /**
     * @param original The {@code Task} before being edited.
     * @param task The updated {@code Task}.
     * @return A {@code CommandResult} informing the user that {@code original} has been updated to {@code task}.
     */
    private CommandResult makeEditedTask(Task original, Task task) {
        return () -> "Edited \"" + original.getTaskName() + "\".\nNew task details: " + task.toString();
    }

    /**
     * @return A {@code CommandResult} indicating that the edit operation is cancelled.
     */
    private CommandResult makeCancelledResult() {
        return () -> "OK! Not editing anything.";
    }

    /**
     * @param userInput A {@code String} representing the user response.
     * @return A {@code CommandResult} indicating that {@code userInput} is invalid.
     */
    private CommandResult makeInvalidUserResponse(String userInput) {
        return () -> {
            StringBuilder builder = new StringBuilder();
            builder.append("I don't understand \"" + userInput + "\".\n");
            builder.append("Enter a number to indicate which task to edit.\n");
            builder.append(TasksListUtil.display(this.foundTasks));
            return builder.toString();
        };
    }

    // A0140702X
    /**
     * @param task The updated {@code Task}.
     * @return A {@code CommandResult} indicating that {@code task} is duplicated.
     */
    private CommandResult makeDuplicateTaskResult(Task task) {
        return () -> task.toString() + " already exists in the schedule!";
    }
}
