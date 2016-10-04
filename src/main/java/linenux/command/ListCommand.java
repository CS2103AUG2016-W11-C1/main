package linenux.command;

import linenux.command.result.CommandResult;
import linenux.command.result.ListCommandResult;
import linenux.model.Schedule;

/**
 * Created by yihangho on 10/4/16.
 */
public class ListCommand implements Command {
    private static final String TASK_PATTERN = "(?i)^list$";

    private Schedule schedule;

    public ListCommand(Schedule schedule) {
        this.schedule = schedule;
    }

    @Override
    public boolean respondTo(String userInput) {
        return userInput.matches(TASK_PATTERN);
    }

    @Override
    public CommandResult execute(String userInput) {
        return ListCommandResult.makeResult(this.schedule);
    }
}
