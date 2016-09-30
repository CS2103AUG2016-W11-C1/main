package linenux.command.result;

public class AddCommandResult implements CommandResult {

    public AddCommandResult() {
    }

    @Override
    public String feedbackToUser() {
        return "Added Trump";
    }

}
