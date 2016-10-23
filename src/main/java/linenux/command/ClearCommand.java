package linenux.command;

import java.util.ArrayList;

import linenux.command.result.CommandResult;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.util.ArrayListUtil;

/**
 * Generates details of tasks and reminders attached to task based on userInput.
 */
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

        ArrayList<Task> tasks = this.schedule.getTaskList();
        ArrayList<Task> doneTasks = new ArrayListUtil.ChainableArrayListUtil<>(tasks).filter(Task::isDone).value();

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
}
