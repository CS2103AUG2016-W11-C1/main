package linenux.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import linenux.command.result.CommandResult;
import linenux.model.Schedule;

/**
 * Controls data flow for the entire application.
 */

public class ControlUnit {
    private Schedule schedule;
    private CommandManager commandManager;
    private ObjectProperty<CommandResult> lastCommandResult = new SimpleObjectProperty<>();

    public ControlUnit() {
        this.schedule = (hasExistingSchedule()) ? getExistingSchedule() : new Schedule();
        this.commandManager = new CommandManager(schedule);
    }

    public CommandResult execute(String userInput) {
        CommandResult result = commandManager.delegateCommand(userInput);
        this.lastCommandResult.setValue(result);
        return result;
    }

    // TODO: Check if JAXB schedule file exist.
    private boolean hasExistingSchedule() {
        return false;
    }

    // TODO: Get existing JAXB schedule.
    private Schedule getExistingSchedule() {
        return null;
    }

    public Schedule getSchedule() {
        return this.schedule;
    }

    public ObjectProperty<CommandResult> getLastCommandResultProperty() {
        return this.lastCommandResult;
    }
}
