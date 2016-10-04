package linenux.command;

import linenux.model.Schedule;
import linenux.command.result.CommandResult;
import linenux.command.result.AddCommandResult;

/**
 * Adds a task to the schedule.
 */
public class AddCommand implements Command {

    public AddCommand() {
    }

    @Override
    public boolean respondTo(String userInput) {
        return false;
    }

    @Override
    public CommandResult execute() {
        return new AddCommandResult();
    }
}
