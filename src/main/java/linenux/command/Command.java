package linenux.command;

import java.util.Collection;
import java.util.Set;

import linenux.command.result.CommandResult;

/**
 * All command types must support interface methods.
 */
public interface Command {
    public static final String CALLOUTS = "* Non-compulsory fields are in square brackets.\n* Arguments are case insensitive.";

    //@@author A0144915A
    /**
     * Checks if the user input corresponds to the format of the respective
     * command.
     *
     * @param userInput
     * @return true if format matches and false otherwise.
     */
    public boolean respondTo(String userInput);

    /**
     * Executes the command based on {@code userInput}. This method operates under the assumption that
     * {@code respondTo(userInput)} is {@code true}.
     * @param userInput A {@code String} representing the user input.
     * @return A {@code CommandResult} representing the result of the command.
     */
    public CommandResult execute(String userInput);

    /**
     * @return {@code true} if and only if this {@code Command} is awaiting for user response.
     */
    default public boolean awaitingUserResponse() {
        return false;
    }

    /**
     * Process the response given by the user.
     * @param userInput {@code String} representing the user response.
     * @return A {@code CommandResult}, which is the result of processing {@code userInput}.
     */
    default public CommandResult getUserResponse(String userInput) {
        return null;
    }

    /**
     * @return A {@code String} representing the default command word.
     */
    public String getTriggerWord();

    /**
     * @return A {@code String} describing what this {@code Command} does.
     */
    public String getDescription();

    //@@author A0135788M
    /**
     * @return A {@code String} describing the format that this {@code Command} expects.
     */
    public String getCommandFormat();

    /**
     * @return A {@code String} representing a regular expression for this {@code Command}.
     */
    public String getPattern();

    //@@author A0144915A

    /**
     * Add a new alias.
     * @param alias The new alias.
     */
    public void setAlias(String alias);

    /**
     * Update the list of aliases.
     * @param aliases The new aliases.
     */
    public void setAliases(Collection<String> aliases);

    /**
     * Remove an alias.
     * @param alias The alias to remove.
     */
    public void removeAlias(String alias);

    //@@author A0135788M
    /**
     * Returns the list of trigger words for that command.
     * @return an ArrayList of trigger words for that command.
     */
    public Set<String> getTriggerWords();
}
