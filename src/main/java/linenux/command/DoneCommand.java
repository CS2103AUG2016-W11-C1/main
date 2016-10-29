package linenux.command;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.result.CommandResult;
import linenux.command.result.PromptResults;
import linenux.command.result.SearchResults;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.util.ArrayListUtil;
import linenux.util.TasksListUtil;

/**
 * Marks task as done.
 */
public class DoneCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "done";
    private static final String DESCRIPTION = "Marks a task as done.";
    private static final String COMMAND_FORMAT = "done KEYWORDS";

    private static final String NUMBER_PATTERN = "^\\d+$";
    private static final String CANCEL_PATTERN = "^cancel$";

    private Schedule schedule;
    private boolean requiresUserResponse;
    private ArrayList<Task> foundTasks;

    //@@author A0135788M
    public DoneCommand(Schedule schedule) {
        this.schedule = schedule;
        this.TRIGGER_WORDS.add(TRIGGER_WORD);
    }

    //@@author A0144915A
    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(getPattern());
        assert this.schedule != null;

        String keywords = extractKeywords(userInput);

        if (keywords.trim().isEmpty()) {
            return makeNoKeywordsResult();
        }

        ArrayList<Task> tasks = new ArrayListUtil.ChainableArrayListUtil<>(this.schedule.search(keywords))
                .filter(Task::isNotDone)
                .value();

        if (tasks.size() == 0) {
            return SearchResults.makeNotFoundResult(keywords);
        } else if (tasks.size() == 1) {
            Task task = tasks.get(0);
            this.schedule.updateTask(task, task.markAsDone());
            return makeDoneTask(task);
        } else {
            setResponse(true, tasks);
            return PromptResults.makePromptIndexResult(tasks);
        }
    }

    //@@author A0135788M
    @Override
    public boolean awaitingUserResponse() {
        return requiresUserResponse;
    }

    @Override
    public CommandResult userResponse(String userInput) {
        assert this.foundTasks != null;
        assert this.schedule != null;

        if (userInput.matches(NUMBER_PATTERN)) {
            int index = Integer.parseInt(userInput);

            if (1 <= index && index <= this.foundTasks.size()) {
                Task task = this.foundTasks.get(index - 1);
                this.schedule.updateTask(task, task.markAsDone());

                setResponse(false, null);
                return makeDoneTask(task);
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

    //@@author A0144915A
    @Override
    public String getTriggerWord() {
        return TRIGGER_WORD;
    }

    //@@author A0135788M
    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public String getCommandFormat() {
        return COMMAND_FORMAT;
    }

    private String extractKeywords(String userInput) {
        Matcher matcher = Pattern.compile(getPattern()).matcher(userInput);

        if (matcher.matches() && matcher.group("keywords") != null) {
            return matcher.group("keywords");
        } else {
            return "";
        }
    }

    private void setResponse(boolean requiresUserResponse, ArrayList<Task> foundTasks) {
        this.requiresUserResponse = requiresUserResponse;
        this.foundTasks = foundTasks;
    }

    private CommandResult makeNoKeywordsResult() {
        return () -> "Invalid arguments.\n\n" + COMMAND_FORMAT + "\n\n" + CALLOUTS;
    }

    private CommandResult makeDoneTask(Task task) {
        return () -> "\"" + task.getTaskName() + "\" is marked as done.";
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
}
