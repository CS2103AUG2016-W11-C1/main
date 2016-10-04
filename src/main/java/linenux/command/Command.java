package linenux.command;

import linenux.model.Schedule;
import linenux.command.result.CommandResult;

/**
 * All command types must support interface methods.
 */
public interface Command {

    /**
     * Checks if the user input corresponds to the format of the respective
     * command.
     * 
     * @return true if format matches and false otherwise.
     */
    public boolean respondTo(String userInput);

    /**
     * Carries out the command.
     */
    public CommandResult execute();
}
