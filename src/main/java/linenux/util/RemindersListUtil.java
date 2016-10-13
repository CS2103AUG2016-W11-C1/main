package linenux.util;

import java.util.ArrayList;

import linenux.model.Reminder;

public class RemindersListUtil {
    /**
     * Display the list of tasks as a string.
     * @param tasks The list of tasks to display.
     * @return A string representing the tasks.
     */
    public static String display(ArrayList<Reminder> reminders) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < reminders.size(); i++) {
            builder.append(i + 1);
            builder.append(". ");
            builder.append(reminders.get(i).toString());
            builder.append('\n');
        }

        return builder.toString().trim();
    }
}
