package linenux.util;

import java.util.ArrayList;

import linenux.model.Reminder;
import linenux.model.Task;

//@@author A0144915A
public class TasksListUtil {
    /**
     * Display the list of tasks and reminders as a string.
     * @param tasks The list of tasks to display.
     * @param reminders The list of reminders to display.
     * @return A string representing the tasks and reminders.
     */
    public static String display(ArrayList<Task> tasks, ArrayList<Reminder> reminders) {
        StringBuilder builder = new StringBuilder();

        builder.append(ArrayListUtil.display(tasks));

        builder.append("\n\n");

        if (reminders.size() > 0) {
            builder.append("Reminders:\n");
            builder.append(ArrayListUtil.display(reminders));
        }

        return builder.toString().trim();
    }
}
