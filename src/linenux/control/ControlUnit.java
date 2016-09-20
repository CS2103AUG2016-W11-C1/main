package linenux.control;

import linenux.command.result.CommandResult;
import linenux.model.Schedule;

/**
 * Controls data flow for the entire application.
 */

public class ControlUnit {
    private Schedule schedule;
    private CommandManager commandManager;

    public ControlUnit() {
        this.schedule = (hasExistingSchedule()) ? getExistingSchedule() : new Schedule();
        this.commandManager = new CommandManager(schedule);
    }

    public CommandResult execute(String userInput) {
        return commandManager.delegateCommand(userInput);
    }

    // TODO: Check if JAXB schedule file exist.
    private boolean hasExistingSchedule() {
        return false;
    }

    // TODO: Get existing JAXB schedule.
    private Schedule getExistingSchedule() {
        return null;
    }
}
