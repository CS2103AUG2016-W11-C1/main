package linenux.command.result;

public class InvalidCommandResult implements CommandResult {

    public InvalidCommandResult() {
    }

    @Override
    public String getFeedback() {
        return "No such command.";
    }
}
