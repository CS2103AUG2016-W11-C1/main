package linenux.command;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.result.CommandResult;
import linenux.model.Reminder;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.util.RemindersListUtil;
import linenux.util.TasksListUtil;

/**
 * Generates details of tasks and reminders attached to task based on userInput.
 */
public class ViewCommand implements Command {
    private static final String TRIGGER_WORD = "view";
    private static final String DESCRIPTION = "Views details of specific task.";
    private static final String VIEW_PATTERN = "(?i)^view (?<keywords>.*)$";
    private static final String NUMBER_PATTERN = "^\\d+$";
    private static final String CANCEL_PATTERN = "^cancel$";

    private Schedule schedule;
    private boolean requiresUserResponse;
    private ArrayList<Task> foundTasks;

    public ViewCommand(Schedule schedule) {
        this.schedule = schedule;
        this.requiresUserResponse = false;
    }

    @Override
    public String getTriggerWord() {
        return TRIGGER_WORD;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public boolean respondTo(String userInput) {
        return userInput.matches(VIEW_PATTERN);
    }

    @Override
    public CommandResult execute(String userInput) {
        Matcher matcher = Pattern.compile(VIEW_PATTERN).matcher(userInput);

        if (matcher.matches()) {
            assert (this.schedule != null);

            String keywords = matcher.group("keywords");
            String[] keywordsArr = keywords.split("\\s+");
            ArrayList<Task> tasks = this.schedule.search(keywordsArr);

            if (tasks.size() == 0) {
                return makeNotFoundResult(keywords);
            } else if (tasks.size() == 1) {
                return makeResult(tasks.get(0));
            } else {
                this.requiresUserResponse = true;
                this.foundTasks = tasks;
                return makePromptResult(tasks);
            }
        }

        return null;
    }

    @Override
    public boolean awaitingUserResponse() {
        return requiresUserResponse;
    }

    @Override
    public CommandResult userResponse(String userInput) {
        if (userInput.matches(NUMBER_PATTERN)) {
            int index = Integer.parseInt(userInput);

            if (1 <= index && index <= this.foundTasks.size()) {
                Task task = this.foundTasks.get(index - 1);

                this.requiresUserResponse = false;
                this.foundTasks = null;
                return makeResult(task);
            } else {
                return makeInvalidIndexResult();
            }
        } else if (userInput.matches(CANCEL_PATTERN)) {
            this.requiresUserResponse = false;
            this.foundTasks = null;
            return makeCancelledResult();
        } else {
            return makeInvalidUserResponse(userInput);
        }
    }

    private CommandResult makeNotFoundResult(String keywords) {
        return () -> "Cannot find \"" + keywords + "\".";
    }

    private CommandResult makeResult(Task task) {
        ArrayList<Reminder> reminders = task.getReminders();
        StringBuilder builder = new StringBuilder();
        builder.append(task.toString());
        builder.append('\n');
        builder.append("Reminders:" + '\n');

        if (reminders.size() == 0) {
            builder.append("There are no reminders found!");
        } else {
            builder.append(RemindersListUtil.display(reminders));
        }

        return () -> builder.toString().trim();

    }

    private CommandResult makePromptResult(ArrayList<Task> tasks) {
        return () -> {
            StringBuilder builder = new StringBuilder();
            builder.append("Which one? (1-");
            builder.append(tasks.size());
            builder.append(")\n");

            builder.append(TasksListUtil.display(tasks));

            return builder.toString();
        };
    }

    private CommandResult makeCancelledResult() {
        return () -> "OK! Not viewing any task.";
    }

    private CommandResult makeInvalidUserResponse(String userInput) {
        return () -> {
            StringBuilder builder = new StringBuilder();
            builder.append("I don't understand \"" + userInput + "\".\n");
            builder.append("Enter a number to indicate which task to delete.\n");
            builder.append(TasksListUtil.display(this.foundTasks));
            return builder.toString();
        };
    }

    private CommandResult makeInvalidIndexResult() {
        return () -> {
            StringBuilder builder = new StringBuilder();
            builder.append("That's not a valid index. Enter a number between 1 and ");
            builder.append(this.foundTasks.size());
            builder.append(":\n");
            builder.append(TasksListUtil.display(this.foundTasks));
            return builder.toString();
        };
    }
}