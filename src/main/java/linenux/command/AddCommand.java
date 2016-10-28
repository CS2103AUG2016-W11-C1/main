package linenux.command;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.parser.AddArgumentParser;
import linenux.command.result.CommandResult;
import linenux.control.TimeParserManager;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.time.parser.ISODateWithTimeParser;
import linenux.util.ArrayListUtil;
import linenux.util.Either;

/**
 * Adds a task to the schedule.
 */
public class AddCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "add";
    private static final String DESCRIPTION = "Adds a task to the schedule.";
    private static final String COMMAND_FORMAT = "add TASK_NAME [st/START_TIME] [et/END_TIME] [#/TAG...]...";

    private Schedule schedule;
    private TimeParserManager timeParserManager;
    private AddArgumentParser addArgumentParser;

    //@@author A0144915A
    public AddCommand(Schedule schedule) {
        this.schedule = schedule;
        this.timeParserManager = new TimeParserManager(new ISODateWithTimeParser());
        this.addArgumentParser = new AddArgumentParser(this.timeParserManager, COMMAND_FORMAT, CALLOUTS);
        this.TRIGGER_WORDS.add(TRIGGER_WORD);
    }

    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(getPattern());
        assert this.schedule != null;

        String argument = extractArgument(userInput);

        Either<Task, CommandResult> task = this.addArgumentParser.parse(argument);

        if (task.isRight()) {
            return task.getRight();
        }

        Task actualTask = task.getLeft();

        ArrayList<Task> duplicateTaskList = new ArrayListUtil.ChainableArrayListUtil<>(this.schedule.getTaskList())
                .filter(a -> a.equals(actualTask)).value();

        assert duplicateTaskList.size() <= 1;

        if (duplicateTaskList.isEmpty()) {
            this.schedule.addTask(actualTask);
            return makeResult(actualTask);
        } else {
            return makeDuplicateTaskResult(actualTask);
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

    //@@author A0144915A
    private String extractArgument(String userInput) {
        Matcher matcher = Pattern.compile(getPattern()).matcher(userInput);

        if (matcher.matches() && matcher.group("keywords") != null) {
            return matcher.group("keywords");
        } else {
            return "";
        }
    }

    private CommandResult makeResult(Task task) {
        return () -> "Added " + task.toString();
    }

    private CommandResult makeDuplicateTaskResult(Task task) {
        return () -> task.toString() + " already exists in the schedule!";
    }
}
