package linenux.command;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.result.CommandResult;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.util.ArrayListUtil;

/**
 * Generates details of tasks and reminders attached to task based on userInput.
 */
//@@author A1234567A
public class ClearCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "clear";
    private static final String DESCRIPTION = "Clears the schedule of all done tasks.";
    private static final String COMMAND_FORMAT = "clear";

    private Schedule schedule;

    public ClearCommand(Schedule schedule) {
        this.schedule = schedule;
        this.TRIGGER_WORDS.add(TRIGGER_WORD);
    }

    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(getPattern());
        assert this.schedule != null;

        ArrayList<Task> doneTasks = this.schedule.getTaskList();

        String tag = extractTag(userInput);

        if (tag != null) {
            doneTasks = new ArrayListUtil.ChainableArrayListUtil<>(doneTasks)
                .filter(task -> task.hasTag(tag)).value();
        } else {
            doneTasks = new ArrayListUtil.ChainableArrayListUtil<>(doneTasks)
                .filter(Task::isDone).value();
        }

        if (doneTasks.isEmpty()) {
            return () -> "There are no done tasks to clear!";
        } else {
            this.schedule.deleteTasks(doneTasks);
            return () -> "Deleting tasks!";
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

    private String extractTag(String argument) {
        Matcher matcher = Pattern.compile("(^|.*? )#/(?<tag>.*?)(\\s)?").matcher(argument);

        if (matcher.matches() && matcher.group("tag") != null) {
            return matcher.group("tag").trim(); // TODO
        } else {
            return null;
        }
    }
}
