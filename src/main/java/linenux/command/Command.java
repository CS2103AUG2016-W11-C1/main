package linenux.command;

import linenux.command.result.CommandResult;

/**
 * All command types must support interface methods.
 */
public interface Command {

    /**
     * Checks if the user input corresponds to the format of the respective
     * command.
     *
     * @param userInput
     * @return true if format matches and false otherwise.
     */
    public boolean respondTo(String userInput);

    /**
     * Carries out the command.
     * Contract: use respondTo to check before calling execute
     * @param userInput
     * @return
     */
    public CommandResult execute(String userInput);

    /**
     * Checks if command is waiting for user response.
     * @return true if command is waiting and false otherwise.
     */
    default public boolean awaitingUserResponse() {
        return false;
    }

    /**
     * Carries out the user response.
     * @param userInput
     * @return
     */
    default public CommandResult userResponse(String userInput) {
        return null;
    }
}
