package linenux.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import linenux.command.result.CommandResult;
import linenux.config.Config;
import linenux.model.Schedule;
import linenux.storage.ScheduleStorage;
import linenux.storage.XmlScheduleStorage;

/**
 * Controls data flow for the entire application.
 */
public class ControlUnit {
    public static enum Mode {ACTUAL, TEST}

    private Config config;
    private Schedule schedule;
    private ScheduleStorage scheduleStorage;
    private CommandManager commandManager;
    private ObjectProperty<CommandResult> lastCommandResult = new SimpleObjectProperty<>();

    public ControlUnit() {
        this.scheduleStorage = new XmlScheduleStorage();

        if (this.scheduleStorage.hasScheduleFile()) {
            this.schedule = this.scheduleStorage.loadScheduleFromFile();
        } else {
            this.schedule = new Schedule();
        }
    }

    public ControlUnit(Mode mode) {
        this.config = new Config();
        this.scheduleStorage = (mode == Mode.ACTUAL) ? new XmlScheduleStorage(config.getActualFilePath()) : new XmlScheduleStorage(config.getTestFilePath());
        this.schedule = (this.scheduleStorage.hasScheduleFile()) ? this.scheduleStorage.loadScheduleFromFile() : new Schedule();
        this.commandManager = new CommandManager(schedule);
    }

    public CommandResult execute(String userInput) {
        CommandResult result = commandManager.delegateCommand(userInput);
        lastCommandResult.setValue(result);
        scheduleStorage.saveScheduleToFile(schedule);
        return result;
    }

    public Schedule getSchedule() {
        return this.schedule;
    }

    public ObjectProperty<CommandResult> getLastCommandResultProperty() {
        return this.lastCommandResult;
    }
}
