package linenux.command.result;

import linenux.model.Task;
import linenux.util.TasksListUtil;

import java.util.ArrayList;

/**
 * Created by yihangho on 10/20/16.
 */
//@@author A0144915A
public class PromptResults {
    public static CommandResult makePromptIndexResult(ArrayList<Task> tasks) {
        return () -> {
            StringBuilder builder = new StringBuilder();
            builder.append("Which one? (1-");
            builder.append(tasks.size());
            builder.append(")\n");

            builder.append(TasksListUtil.display(tasks));

            return builder.toString();
        };
    }

    public static CommandResult makeInvalidIndexResult(ArrayList<Task> tasks) {
        return () -> {
            StringBuilder builder = new StringBuilder();
            builder.append("That's not a valid index. Enter a number between 1 and ");
            builder.append(tasks.size());
            builder.append(":\n");
            builder.append(TasksListUtil.display(tasks));
            return builder.toString();
        };
    }
}
