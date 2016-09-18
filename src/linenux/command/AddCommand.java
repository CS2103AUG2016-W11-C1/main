package linenux.command;

import linenux.command.CommandResult;
import linenux.data.task.Task;

import java.time.LocalDateTime;

/**
 * Adds a task to the schedule.
 */
public class AddCommand extends Command {
    
    public static final String COMMAND_WORD = "add";
    public static final String MESSAGE_SUCCESS = "New person added: %1$s";
    
    private final Task toAdd;
    
    public AddCommand(String taskName, LocalDateTime deadline) {
        toAdd = new Task(taskName, deadline);
    }

    @Override
    public CommandResult execute() {
        schedule.addTask(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

}
