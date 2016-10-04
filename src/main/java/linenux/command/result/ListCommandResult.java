package linenux.command.result;

import linenux.model.Schedule;
import linenux.model.Task;

import java.util.ArrayList;

/**
 * Created by yihangho on 10/4/16.
 */
public class ListCommandResult implements CommandResult {
    public static ListCommandResult makeResult(Schedule schedule) {
        StringBuilder builder = new StringBuilder();

        ArrayList<Task> tasks = schedule.getTaskList();

        for (int i = 0; i < tasks.size(); i++) {
            builder.append(i+1);
            builder.append(". ");
            builder.append(tasks.get(i).toString());
            builder.append("\n");
        }

        return new ListCommandResult(builder.toString());
    }

    private String feedback;

    public ListCommandResult(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public String getFeedback() {
        return this.feedback;
    }
}
