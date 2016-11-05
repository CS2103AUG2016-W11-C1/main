package linenux.util;

import java.util.ArrayList;

import linenux.command.util.ReminderSearchResult;
import linenux.model.Reminder;

//@@author A0140702X
public class RemindersListUtil {
    /**
     * Display the list of {@code ReminderSearchResult}.
     * @param results An {@code ArrayList} of {@code ReminderSearchResult} to display.
     * @return A {@code String} representing {@code results}.
     */
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
