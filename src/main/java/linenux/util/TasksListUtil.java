package linenux.util;

import linenux.model.Task;

import java.util.ArrayList;

/**
 * Created by yihangho on 10/5/16.
 */
public class TasksListUtil {
    /**
     * Display the list of tasks as a string.
     * @param tasks The list of tasks to display.
     * @return A string representing the tasks.
     */
    public static String display(ArrayList<Task> tasks) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < tasks.size(); i++) {
            builder.append(i + 1);
            builder.append(". ");
            builder.append(tasks.get(i).toString());
            builder.append('\n');
        }

        return builder.toString().trim();
    }
}
