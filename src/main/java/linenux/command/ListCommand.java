package linenux.command;

import linenux.command.result.CommandResult;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.util.TasksListUtil;

import java.util.ArrayList;

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
        return makeResult(this.schedule.getTaskList());
    }

    private CommandResult makeResult(ArrayList<Task> tasks) {
        return () -> TasksListUtil.display(tasks);
    }
}
