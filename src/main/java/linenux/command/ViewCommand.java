package linenux.command;

import java.util.ArrayList;

import linenux.command.result.CommandResult;
import linenux.command.result.PromptResults;
import linenux.command.result.SearchResults;
import linenux.model.Reminder;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.util.RemindersListUtil;
import linenux.util.TasksListUtil;

//@@author A0135788M
/**
 * Generates details of tasks and reminders attached to task based on userInput.
 */
public class ViewCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "view";
    private static final String DESCRIPTION = "Views details of specific task.";
    private static final String COMMAND_FORMAT = "view KEYWORDS";

    private static final String NUMBER_PATTERN = "^\\d+$";
    private static final String CANCEL_PATTERN = "^cancel$";

    private Schedule schedule;
    private boolean requiresUserResponse;
    private ArrayList<Task> foundTasks;

    public ViewCommand(Schedule schedule) {
        this.schedule = schedule;
        this.TRIGGER_WORDS.add(TRIGGER_WORD);
    }

    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(getPattern());
        assert this.schedule != null;

        String keywords = extractArgument(userInput);

        if (keywords.trim().isEmpty()) {
            return makeNoKeywordsResult();
        }

        ArrayList<Task> tasks = this.schedule.search(keywords);

        if (tasks.size() == 0) {
            return SearchResults.makeNotFoundResult(keywords);
        } else if (tasks.size() == 1) {
            Task task = tasks.get(0);
            return makeResult(task);
        } else {
            setResponse(true, tasks);
            return PromptResults.makePromptIndexResult(tasks);
        }
    }

    @Override
    public boolean awaitingUserResponse() {
        return requiresUserResponse;
    }

    @Override
    public CommandResult getUserResponse(String userInput) {
        assert this.foundTasks != null;
        assert this.schedule != null;

        if (userInput.matches(NUMBER_PATTERN)) {
            int index = Integer.parseInt(userInput);

            if (1 <= index && index <= this.foundTasks.size()) {
                Task task = this.foundTasks.get(index - 1);

                setResponse(false, null);
                return makeResult(task);
            } else {
                return PromptResults.makeInvalidIndexResult(this.foundTasks);
            }
        } else if (userInput.matches(CANCEL_PATTERN)) {
            setResponse(false, null);
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
        return DESCRIPTION;
    }

    @Override
    public String getCommandFormat() {
        return COMMAND_FORMAT;
    }

    private void setResponse(boolean requiresUserResponse, ArrayList<Task> foundTasks) {
        this.requiresUserResponse = requiresUserResponse;
        this.foundTasks = foundTasks;
    }

    private CommandResult makeNoKeywordsResult() {
        return () -> "Invalid arguments.\n\n" + COMMAND_FORMAT + "\n\n" + CALLOUTS;
    }

    private CommandResult makeResult(Task task) {
        ArrayList<Reminder> reminders = task.getReminders();
        StringBuilder builder = new StringBuilder();
        builder.append(task.toString());
        builder.append('\n');
        builder.append("Reminders:" + '\n');

        if (reminders.size() == 0) {
            builder.append("You have not set any reminders for this task.");
        } else {
            builder.append(RemindersListUtil.display(reminders));
        }

        return () -> builder.toString().trim();

    }

    private CommandResult makeCancelledResult() {
        return () -> "OK! Not viewing any task.";
    }

    private CommandResult makeInvalidUserResponse(String userInput) {
        return () -> {
            StringBuilder builder = new StringBuilder();
            builder.append("I don't understand \"" + userInput + "\".\n");
            builder.append("Enter a number to indicate which task to view.\n");
            builder.append(TasksListUtil.display(this.foundTasks));
            return builder.toString();
        };
    }
}
