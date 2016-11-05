package linenux.command;

import linenux.command.parser.AddArgumentParser;
import linenux.command.result.CommandResult;
import linenux.control.TimeParserManager;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.time.parser.ISODateWithTimeParser;
import linenux.time.parser.StandardDateWithTimeParser;
import linenux.time.parser.TodayWithTimeParser;
import linenux.time.parser.TomorrowWithTimeParser;
import linenux.util.Either;

/**
 * Adds a task to the schedule.
 */
public class AddCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "add";
    private static final String DESCRIPTION = "Adds a task to the schedule.";
    private static final String COMMAND_FORMAT = "add TASK [st/START_TIME] [et/END_TIME] [#/TAG]...";

    private Schedule schedule;
    private TimeParserManager timeParserManager;
    private AddArgumentParser addArgumentParser;

    //@@author A0144915A
    /**
     * Constructs an {@code AddCommand}.
     * @param schedule The {@code Schedule} to add new {@code Task} to.
     */
    public AddCommand(Schedule schedule) {
        this.schedule = schedule;
        this.timeParserManager = new TimeParserManager(new ISODateWithTimeParser(), new StandardDateWithTimeParser(), new TodayWithTimeParser(), new TomorrowWithTimeParser());
        this.addArgumentParser = new AddArgumentParser(this.timeParserManager, COMMAND_FORMAT, CALLOUTS);
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

        String argument = extractArgument(userInput);

        Either<Task, CommandResult> task = this.addArgumentParser.parse(argument);

        if (task.isRight()) {
            return task.getRight();
        }

        Task actualTask = task.getLeft();

        if (this.schedule.isUniqueTask(actualTask)) {
            this.schedule.addTask(actualTask);
            return makeResult(actualTask);
        } else {
            return makeDuplicateTaskResult(actualTask);
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

    //@@author A0144915A
    /**
     * @param task The newly created {@code Task}.
     * @return A {@code CommandResult} indicating that a new {@code Task} is created.
     */
    private CommandResult makeResult(Task task) {
        return () -> "Added " + task.toString();
    }

    /**
     * @param task The {@code Task} that the user wants to create.
     * @return A {@code CommandResult} indicating that {@code task} is duplicated.
     */
    private CommandResult makeDuplicateTaskResult(Task task) {
        return () -> task.toString() + " already exists in the schedule!";
    }
}
