package linenux.command.result;

import java.util.ArrayList;

import linenux.command.EditrCommand;
import linenux.model.Reminder;
import linenux.model.Task;
import linenux.util.ArrayListUtil;
import linenux.util.RemindersListUtil;
import linenux.util.TasksListUtil;

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

    public static CommandResult makePromptReminderIndexResult(ArrayList<EditrCommand.ReminderSearchResult> results) {
        return () -> {
            int totalResults = EditrCommand.ReminderSearchResult.totalReminders(results);

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

    public static CommandResult makeInvalidReminderIndexResult(ArrayList<EditrCommand.ReminderSearchResult> results) {
        return () -> {
            int totalReminders = EditrCommand.ReminderSearchResult.totalReminders(results);

            StringBuilder builder = new StringBuilder();
            builder.append("That's not a valid index. Enter a number between 1 and ");
            builder.append(totalReminders);
            builder.append(":\n");
            builder.append(RemindersListUtil.displaySearchResults(results));

            return builder.toString().trim();
        };
    }
}
