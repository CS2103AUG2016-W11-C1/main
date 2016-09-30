package linenux.command.result;

public class InvalidCommandResult implements CommandResult {

    public InvalidCommandResult() {
    }

    @Override
    public String feedbackToUser() {
        return "No such command.";
    }
}
