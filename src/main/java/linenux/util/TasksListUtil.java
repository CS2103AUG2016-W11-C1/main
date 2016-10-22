package linenux.util;

import java.util.ArrayList;

import linenux.model.Reminder;
import linenux.model.Task;

/**
 * Created by yihangho on 10/5/16.
 */
public class TasksListUtil {
    /**
     * Display the list of tasks and reminders as a string.
     *
     * @param tasks
     *            The list of tasks to display.
     * @param remidners
     *            The list of reminders to display.
     * @return A string representing the tasks and reminders.
     */
    public static String display(ArrayList<Task> tasks) {
        StringBuilder builder = new StringBuilder();

        builder.append("Tasks: ");
        for (int i = 0; i < tasks.size(); i++) {
            builder.append(i + 1);
            builder.append(". ");
            builder.append(tasks.get(i).toString());
            builder.append('\n');
        }

        return builder.toString().trim();
    }

    /**
     * Display the list of tasks and reminders as a string.
     *
     * @param tasks
     *            The list of tasks to display.
     * @param reminders
     *            The list of reminders to display.
     * @return A string representing the tasks and reminders.
     */
    public static String display(ArrayList<Task> tasks, ArrayList<Reminder> reminders) {
        StringBuilder builder = new StringBuilder();

        if (tasks != null) {
            builder.append("Tasks:\n");
            for (int i = 0; i < tasks.size(); i++) {
                builder.append(i + 1);
                builder.append(". ");
                builder.append(tasks.get(i).toString());
                builder.append('\n');
            }
            builder.append('\n');
        }

        if (reminders != null) {
            builder.append("Reminders:\n");
            for (int i = 0; i < reminders.size(); i++) {
                builder.append(i + 1);
                builder.append(". ");
                builder.append(reminders.get(i).toString());
                builder.append('\n');
            }
        }

        return builder.toString().trim();
    }
}
