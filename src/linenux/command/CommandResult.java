package linenux.command;

/**
 * Represents the result of the command execution.
 */
public class CommandResult {
    /** The feedback message to be shown to the user. Contains a description of the execution result */
    public final String feedbackToUser;
    
    public CommandResult(String feedbackToUser) {
        this.feedbackToUser = feedbackToUser;
    }
}
