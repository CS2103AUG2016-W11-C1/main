package linenux.command.result;

import java.util.ArrayList;

import linenux.model.Reminder;
import linenux.model.Task;
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

    public static CommandResult makePromptIndexResult(ArrayList<Task> tasks, ArrayList<Integer> noOfReminders, ArrayList<Reminder> reminders) {
        return () -> {
            StringBuilder builder = new StringBuilder();
            builder.append("Which one? (1-");
            builder.append(reminders.size());
            builder.append(")\n");

            int counter = 0;
            for (int i = 0; i < noOfReminders.size(); i++) {
                builder.append("Task: ");
                builder.append(tasks.get(i).getTaskName());
                builder.append("\n");

                for (int j = 0; j < noOfReminders.get(i); j++) {
                    builder.append(counter + 1);
                    builder.append(". ");
                    builder.append(reminders.get(counter).toString());
                    builder.append("\n");
                    counter++;
                };
            };

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

    public static CommandResult makeInvalidIndexResult(ArrayList<Task> tasks, ArrayList<Integer> noOfReminders, ArrayList<Reminder> reminders) {
        return () -> {
            StringBuilder builder = new StringBuilder();
            builder.append("That's not a valid index. Enter a number between 1 and ");
            builder.append(reminders.size());
            builder.append(":\n");

            int counter = 0;
            for (int i = 0; i < noOfReminders.size(); i++) {
                builder.append("Task: ");
                builder.append(tasks.get(i).getTaskName());
                builder.append("\n");

                for (int j = 0; j < noOfReminders.get(i); j++) {
                    builder.append(counter + 1);
                    builder.append(". ");
                    builder.append(reminders.get(counter).toString());
                    builder.append("\n");
                    counter++;
                };
            };

            return builder.toString().trim();
        };
    }
}
