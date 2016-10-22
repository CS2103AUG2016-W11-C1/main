package linenux.control;

import java.nio.file.Paths;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import linenux.command.result.CommandResult;
import linenux.model.Schedule;
import linenux.storage.XmlScheduleStorage;

/**
 * Controls data flow for the entire application.
 */
public class ControlUnit {
    private Schedule schedule;
    private XmlScheduleStorage scheduleStorage;
    private CommandManager commandManager;
    private ObjectProperty<CommandResult> lastCommandResult = new SimpleObjectProperty<>();

    public ControlUnit() {
        this.scheduleStorage = new XmlScheduleStorage(getDefaultFilePath());

        if (this.hasExistingSchedule() && getExistingSchedule() != null) {
            System.out.println("controlunit load");
            this.schedule = getExistingSchedule();
        } else {
            System.out.println("controlunit new");
            this.schedule = new Schedule();
        }

        this.commandManager = new CommandManager(schedule);
    }

    public CommandResult execute(String userInput) {
        CommandResult result = commandManager.delegateCommand(userInput);
        lastCommandResult.setValue(result);
        scheduleStorage.saveScheduleToFile(schedule);
        return result;
    }

    private boolean hasExistingSchedule() {
        return scheduleStorage.getFile().exists() && scheduleStorage.getFile().isFile();
    }

    private Schedule getExistingSchedule() {
        return scheduleStorage.loadScheduleFromFile();
    }

    private String getDefaultFilePath() {
        return Paths.get(".").toAbsolutePath().toString();
    }

    public Schedule getSchedule() {
        return this.schedule;
    }

    public ObjectProperty<CommandResult> getLastCommandResultProperty() {
        return this.lastCommandResult;
    }
}
