package linenux.command;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public EditCommand(Schedule schedule) {
        this.schedule = schedule;
        this.timeParserManager = new TimeParserManager(new ISODateWithTimeParser(), new StandardDateWithTimeParser(), new TodayWithTimeParser(), new TomorrowWithTimeParser());
        this.editArgumentParser = new EditArgumentParser(this.timeParserManager, COMMAND_FORMAT, CALLOUTS);
        this.TRIGGER_WORDS.add(TRIGGER_WORD);
    }

    //@@author A0135788M
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

    //@@author A0135788M
    private String extractArgument(String userInput) {
        Matcher matcher = Pattern.compile(getPattern()).matcher(userInput);

        if (matcher.matches() && matcher.group("keywords") != null) {
            return matcher.group("keywords").trim(); //TODO
        } else {
            return "";
        }
    }

    //@@author A0127694U
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
    private void setResponse(boolean requiresUserResponse, ArrayList<Task> foundTasks, GenericParser.GenericParserResult result) {
        this.requiresUserResponse = requiresUserResponse;
        this.foundTasks = foundTasks;
        this.parseResult = result;
    }

    private CommandResult makeNoKeywordsResult() {
        return () -> "Invalid arguments.\n\n" + COMMAND_FORMAT + "\n\n" + CALLOUTS;
    }

    //@@author A0127694U
    private CommandResult makeEditedTask(Task original, Task task) {
        return () -> "Edited \"" + original.getTaskName() + "\".\nNew task details: " + task.toString();
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

    // A0140702X
    private CommandResult makeDuplicateTaskResult(Task task) {
        return () -> task.toString() + " already exists in the schedule!";
    }
}
