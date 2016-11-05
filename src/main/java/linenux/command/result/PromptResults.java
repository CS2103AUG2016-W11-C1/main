package linenux.command.result;

import java.util.ArrayList;

import linenux.command.util.ReminderSearchResult;
import linenux.model.Task;
import linenux.util.ArrayListUtil;
import linenux.util.RemindersListUtil;

//@@author A0144915A
/**
 * A collection of helper functions related to prompting user for more information.
 */
public class PromptResults {
    /**
     * Prompts the user to choose from an {@code ArrayList} of {@code Task}.
     * @param tasks The {@code ArrayList} of {@code Task} that the user can choose from.
     * @return A {@code CommandResult} prompting the user to choose a {@code Task} from {@code tasks}.
     */
    public static CommandResult makePromptIndexResult(ArrayList<Task> tasks) {
        return () -> {
            StringBuilder builder = new StringBuilder();
            builder.append("Which one? (1-");
            builder.append(tasks.size());
            builder.append(", \"cancel\" to cancel the current operation)\n");

            builder.append(ArrayListUtil.display(tasks));

            return builder.toString();
        };
    }

    /**
     * Prompts the user to choose from an {@code ArrayList} of {@code Reminder}.
     * @param results The {@code ArrayList} of {@code Reminder} that the user can choose from.
     * @return A {@code CommandResult} prompting the user to choose a {@code Reminder} from {@code results}.
     */
    public static CommandResult makePromptReminderIndexResult(ArrayList<ReminderSearchResult> results) {
        return () -> {
            int totalResults = ReminderSearchResult.totalReminders(results);

            StringBuilder builder = new StringBuilder();
            builder.append("Which one? (1-");
            builder.append(totalResults);
            builder.append(", \"cancel\" to cancel the current operation)\n");
            builder.append(RemindersListUtil.displaySearchResults(results));

            return builder.toString().trim();
        };
    }

    /**
     * Make a {@code CommandResult} indicating that the index the user chose is invalid.
     * @param tasks The {@code ArrayList} of {@code Task} that the user can choose from.
     * @return A {@code CommandResult} prompting the user to choose again.
     */
    public static CommandResult makeInvalidIndexResult(ArrayList<Task> tasks) {
        return () -> {
            StringBuilder builder = new StringBuilder();
            builder.append("That's not a valid index. Enter a number between 1 and ");
            builder.append(tasks.size());
            builder.append(", or \"cancel\" to cancel the current operation:\n");
            builder.append(ArrayListUtil.display(tasks));
            return builder.toString();
        };
    }

    /**
     * Make a {@code CommandResult} indicating that the index the user chose is invalid.
     * @param results The {@code ArrayList} of {@code Reminder} that the user can choose from.
     * @return A {@code CommandResult} prompting the user to choose again.
     */
    public static CommandResult makeInvalidReminderIndexResult(ArrayList<ReminderSearchResult> results) {
        return () -> {
            int totalReminders = ReminderSearchResult.totalReminders(results);

            StringBuilder builder = new StringBuilder();
            builder.append("That's not a valid index. Enter a number between 1 and ");
            builder.append(totalReminders);
            builder.append(", or \"cancel\" to cancel the current operation:\n");
            builder.append(RemindersListUtil.displaySearchResults(results));

            return builder.toString().trim();
        };
    }
}
