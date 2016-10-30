package linenux.command.result;

//@@author A0135788M
/**
 * An interface representing the result of executing a {@code Command}.
 */
public interface CommandResult {
    /**
     * @return A {@code String} that will be shown to the user.
     */
    public String getFeedback();
}
