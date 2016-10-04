package linenux.command.result;

import linenux.model.Task;

public class AddCommandResult implements CommandResult {

    public static AddCommandResult makeResult(Task addedTask) {
        return new AddCommandResult("Added " + addedTask.toString());
    }

    private String feedback;

    public AddCommandResult(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public String getFeedback() {
        return this.feedback;
    }

}
