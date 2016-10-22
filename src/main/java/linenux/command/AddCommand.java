package linenux.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.parser.AddArgumentParser;
import linenux.command.result.CommandResult;
import linenux.control.TimeParserManager;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.time.parser.ISODateWithTimeParser;
import linenux.util.AliasUtil;
import linenux.util.Either;

/**
 * Adds a task to the schedule.
 */
public class AddCommand implements Command {
    private static final String TRIGGER_WORD = "add";
    private static final String DESCRIPTION = "Adds a task to the schedule.";
    private static final String COMMAND_FORMAT = "add TASK_NAME [st/START_TIME] [et/END_TIME] [#/TAGS]";

    private Schedule schedule;
    private TimeParserManager timeParserManager;
    private AddArgumentParser addArgumentParser;

    public AddCommand(Schedule schedule) {
        this.schedule = schedule;
        this.timeParserManager = new TimeParserManager(new ISODateWithTimeParser());
        this.addArgumentParser = new AddArgumentParser(this.timeParserManager, COMMAND_FORMAT, CALLOUTS);
    }

    @Override
    public boolean respondTo(String userInput) {
        return userInput.matches(getPattern());
    }

    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(getPattern());
        assert this.schedule != null;

        String argument = extractArgument(userInput);

        Either<Task, CommandResult> task = this.addArgumentParser.parse(argument);

        if (task.isLeft()) {
            this.schedule.addTask(task.getLeft());
            return makeResult(task.getLeft());
        } else {
            return task.getRight();
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

    @Override
    public String getPattern() {
        return "(?i)^(" + TRIGGER_WORD + "|" + AliasUtil.ALIASMAP.get(TRIGGER_WORD) + ")(\\s+(?<arguments>.*))?$";
    }

    private String extractArgument(String userInput) {
        Matcher matcher = Pattern.compile(getPattern()).matcher(userInput);

        if (matcher.matches() && matcher.group("arguments") != null) {
            return matcher.group("arguments");
        } else {
            return "";
        }
    }

    private CommandResult makeResult(Task task) {
        return () -> "Added " + task.toString();
    }
}
