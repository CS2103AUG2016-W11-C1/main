package linenux.command;

import linenux.model.Task;
import linenux.model.Schedule;
import linenux.command.result.CommandResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Adds a task to the schedule.
 */
public class AddCommand implements Command {
    public static final String COMMAND_FORMAT = "add TASK_NAME";

    private static final String TASK_PATTERN = "(?i)^add(\\s+(?<arguments>.*))?$";
    private static final String ARGUMENT_PATTERN = "(?i)^(?<taskName>.*?)?" +
            "(\\s+(st\\/(?<startTime>.*?)))?" +
            "(\\s+(et\\/(?<endTime>.*?)))?$";

    private Schedule schedule;

    public AddCommand(Schedule schedule) {
        this.schedule = schedule;
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
        Matcher matcher = Pattern.compile(ARGUMENT_PATTERN).matcher(argument);

        if (!matcher.matches()) {
            return makeInvalidArgumentResult();
        }

        String taskName = matcher.group("taskName");

        if (!isTaskNameValid(taskName)) {
            return makeInvalidArgumentResult();
        }

        Task task = new Task(taskName);
        this.schedule.addTask(task);

        return makeResult(task);
    }

    private String extractArgument(String userInput) {
        Matcher matcher = Pattern.compile(TASK_PATTERN).matcher(userInput);

        if (matcher.matches() && matcher.group("arguments") != null) {
            return matcher.group("arguments");
        } else {
            return "";
        }
    }

    private boolean isTaskNameValid(String taskName) {
        if (taskName == null) {
            return false;
        }

        return taskName.matches("^.*?\\S+.*?$");
    }

    private CommandResult makeResult(Task task) {
        return () -> "Added " + task.getTaskName();
    }

    private CommandResult makeInvalidArgumentResult() {
        return () -> "Invalid arguments.\n\n" + COMMAND_FORMAT;
    }
}
