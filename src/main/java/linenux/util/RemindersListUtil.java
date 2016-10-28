package linenux.util;

import java.util.ArrayList;

import linenux.command.util.ReminderSearchResult;
import linenux.model.Reminder;

//@@author A0140702X
public class RemindersListUtil {
    /**
     * Display the list of reminders as a string.
     * @param reminders The list of reminders to display.
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

    public static String displaySearchResults(ArrayList<ReminderSearchResult> results) {
        StringBuilder builder = new StringBuilder();

        int counter = 1;
        for (ReminderSearchResult result: results) {
            builder.append("Task: ");
            builder.append(result.getTask().getTaskName());
            builder.append("\n");

            for (int i = 0; i < result.getReminders().size(); i++, counter++) {
                builder.append(counter);
                builder.append(". ");
                builder.append(result.getReminders().get(i).toString());
                builder.append("\n");
            };
        }

        return builder.toString().trim();
    }
}
