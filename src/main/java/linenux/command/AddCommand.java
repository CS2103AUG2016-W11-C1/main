package linenux.command;

import linenux.model.Task;
import linenux.model.Schedule;
import linenux.command.result.CommandResult;
import linenux.command.parser.TaskArgumentParser;
import linenux.control.TimeParserManager;
import linenux.time.parser.ISODateWithTimeParser;
import linenux.util.Either;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Adds a task to the schedule.
 */
public class AddCommand implements Command {
    private static final String TASK_PATTERN = "(?i)^add(\\s+(?<arguments>.*))?$";

    private Schedule schedule;
    private TimeParserManager timeParserManager;
    private TaskArgumentParser taskArgumentParser;

    public AddCommand(Schedule schedule) {
        this.schedule = schedule;
        this.timeParserManager = new TimeParserManager(new ISODateWithTimeParser());
        this.taskArgumentParser = new TaskArgumentParser(this.timeParserManager);
    }

    @Override
    public boolean respondTo(String userInput) {
        return userInput.matches(TASK_PATTERN);
    }

    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(TASK_PATTERN);
        assert this.schedule != null;

        String argument = extractArgument(userInput);

        Either<Task, CommandResult> task = this.taskArgumentParser.parse(argument);

        if (task.isLeft()) {
            this.schedule.addTask(task.getLeft());
            return makeResult(task.getLeft());
        } else {
            return task.getRight();
        }
    }

    private String extractArgument(String userInput) {
        Matcher matcher = Pattern.compile(TASK_PATTERN).matcher(userInput);

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
