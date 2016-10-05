package linenux.command;

import linenux.command.result.CommandResult;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.util.TasksListUtil;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yihangho on 10/4/16.
 */
public class ListCommand implements Command {
    private static final String TASK_PATTERN = "(?i)^list( (?<keywords>.*))?$";

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
        Matcher matcher = Pattern.compile(TASK_PATTERN).matcher(userInput);

        if (matcher.matches()) {
            String keywords = matcher.group("keywords");

            if (keywords != null) {
                return makeResult(this.schedule.search(keywords.split("//s+")));
            } else {
                return makeResult(this.schedule.getTaskList());
            }
        }

        return null;
    }

    private CommandResult makeResult(ArrayList<Task> tasks) {
        return () -> TasksListUtil.display(tasks);
    }
}
