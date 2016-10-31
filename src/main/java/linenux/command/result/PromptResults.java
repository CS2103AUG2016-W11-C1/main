package linenux.command.result;

import java.util.ArrayList;

import linenux.command.util.ReminderSearchResult;
import linenux.model.Task;
import linenux.util.RemindersListUtil;
import linenux.util.TasksListUtil;

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

    public static CommandResult makePromptReminderIndexResult(ArrayList<ReminderSearchResult> results) {
        return () -> {
            int totalResults = ReminderSearchResult.totalReminders(results);

            StringBuilder builder = new StringBuilder();
            builder.append("Which one? (1-");
            builder.append(totalResults);
            builder.append(")\n");
            builder.append(RemindersListUtil.displaySearchResults(results));

            return builder.toString().trim();
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

    public static CommandResult makeInvalidReminderIndexResult(ArrayList<ReminderSearchResult> results) {
        return () -> {
            int totalReminders = ReminderSearchResult.totalReminders(results);

            StringBuilder builder = new StringBuilder();
            builder.append("That's not a valid index. Enter a number between 1 and ");
            builder.append(totalReminders);
            builder.append(":\n");
            builder.append(RemindersListUtil.displaySearchResults(results));

            return builder.toString().trim();
        };
    }
}
