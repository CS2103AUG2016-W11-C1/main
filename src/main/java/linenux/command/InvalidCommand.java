package linenux.command;

import linenux.model.Schedule;
import linenux.command.result.CommandResult;
import linenux.command.result.InvalidCommandResult;

/**
 * Act as a fail-safe for invalid or unrecognized commands.
 */
public class InvalidCommand implements Command {

    public InvalidCommand() {
    }

    /**
     * @return true for all user input.
     */
    @Override
    public boolean respondTo(String userInput) {
        return true;
    }

    @Override
    public CommandResult execute() {
        return new InvalidCommandResult();
    }
}
