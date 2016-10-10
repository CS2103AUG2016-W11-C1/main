package linenux.command;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.result.CommandResult;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.util.ArrayListUtil;
import linenux.util.TasksListUtil;

/**
 * Marks task as done.
 */
public class DoneCommand implements Command {
    private static final String TRIGGER_WORD = "done";
    private static final String DONE_PATTERN = "(?i)^done (?<keywords>.*)$";
    private static final String NUMBER_PATTERN = "^\\d+$";
    private static final String CANCEL_PATTERN = "^cancel$";

    private Schedule schedule;
    private boolean requiresUserResponse;
    private ArrayList<Task> foundTasks;

    public DoneCommand(Schedule schedule) {
        this.schedule = schedule;
    }

    @Override
    public boolean respondTo(String userInput) {
        return userInput.matches(DONE_PATTERN);
    }

    @Override
    public CommandResult execute(String userInput) {
        Matcher matcher = Pattern.compile(DONE_PATTERN).matcher(userInput);

        if (matcher.matches()) {
            assert (this.schedule != null);

            String keywords = matcher.group("keywords");
            String[] keywordsArr = keywords.split("\\s+");
            ArrayList<Task> tasks = new ArrayListUtil.ChainableArrayListUtil<Task>(this.schedule.search(keywordsArr))
                                                     .filter(task -> task.isNotDone())
                                                     .value();

            if (tasks.size() == 0) {
                return makeNotFoundResult(keywords);
            } else if (tasks.size() == 1) {
                tasks.get(0).markAsDone();
                return makeDoneTask(tasks.get(0));
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
                task.markAsDone();

                this.requiresUserResponse = false;
                this.foundTasks = null;

                return makeDoneTask(task);
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

    @Override
    public String getTriggerWord() {
        return TRIGGER_WORD;
    }

    @Override
    public String getDescription() {
        return "Mark a task as done.";
    }

    private CommandResult makeNotFoundResult(String keywords) {
        return () -> "Cannot find \"" + keywords + "\".";
    }

    private CommandResult makeDoneTask(Task task) {
        return () -> "\"" + task.getTaskName() + "\" is marked as done.";
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
        return () -> "OK! Not marking any task as done.";
    }

    private CommandResult makeInvalidUserResponse(String userInput) {
        return () -> {
            StringBuilder builder = new StringBuilder();
            builder.append("I don't understand \"" + userInput + "\".\n");
            builder.append("Enter a number to indicate which task to mark as done.\n");
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
