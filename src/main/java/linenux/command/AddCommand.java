package linenux.command;

import linenux.model.Schedule;
import linenux.command.result.CommandResult;
import linenux.model.Task;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Adds a task to the schedule.
 */
public class AddCommand implements Command {
    private static final String TASK_PATTERN = "(?i)^add (?<taskName>.*)$";

    private Schedule schedule;

    public AddCommand(Schedule schedule) {
        this.schedule = schedule;
    }

    @Override
    public boolean respondTo(String userInput) {
        return userInput.matches(TASK_PATTERN);
    }

    /**
     * Contract: use respondTo to check before calling execute
     * @param userInput
     * @return
     */
    @Override
    public CommandResult execute(String userInput) {
        Matcher matcher = Pattern.compile(TASK_PATTERN).matcher(userInput);

        if (matcher.matches()) {
            String taskName = matcher.group("taskName");
            Task task = new Task(taskName);

            if (this.schedule != null) {
                this.schedule.addTask(task);
            }

            return makeResult(task);
        }

        return null;
    }

    private CommandResult makeResult(Task task) {
        return () -> "Added " + task.getTaskName();
    }
}
