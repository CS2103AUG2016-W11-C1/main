package linenux.control;

import java.util.ArrayList;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import linenux.command.Command;
import linenux.command.result.CommandResult;
import linenux.config.Config;
import linenux.model.Schedule;
import linenux.storage.ScheduleStorage;
import linenux.storage.XmlScheduleStorage;

/**
 * Controls data flow for the entire application.
 */
public class ControlUnit {
    private Schedule schedule;
    private ScheduleStorage scheduleStorage;
    private CommandManager commandManager;
    private ObjectProperty<CommandResult> lastCommandResult = new SimpleObjectProperty<>();
    private Config config;

    //@@author A0135788M
    public ControlUnit(Config config) {
        this.scheduleStorage = new XmlScheduleStorage(config.getScheduleFilePath());
        this.schedule = (this.scheduleStorage.hasScheduleFile()) ? this.scheduleStorage.loadScheduleFromFile() : new Schedule();
        this.commandManager = new CommandManager(schedule);
        this.config = config;

        this.initializeAliases();
    }

    public CommandResult execute(String userInput) {
        CommandResult result = commandManager.delegateCommand(userInput);
        lastCommandResult.setValue(result);
        scheduleStorage.saveScheduleToFile(schedule);
        for (Command command: this.commandManager.getCommandList()) {
            this.config.setAliases(command.getTriggerWord(), command.getTriggerWords());
        }
        return result;
    }

    //@@author A0144915A
    public Schedule getSchedule() {
        return this.schedule;
    }

    public ObjectProperty<CommandResult> getLastCommandResultProperty() {
        return this.lastCommandResult;
    }

    public ArrayList<Command> getCommandList() {
        return commandManager.getCommandList();
    }

    private void initializeAliases() {
        for (Command command: this.commandManager.getCommandList()) {
            command.setAliases(this.config.getAliases(command.getTriggerWord()));
        }
    }
}
