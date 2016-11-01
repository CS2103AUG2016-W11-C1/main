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
        this.scheduleStorage = new XmlScheduleStorage(config);
        this.schedule = (this.scheduleStorage.hasScheduleFile()) ? this.scheduleStorage.loadScheduleFromFile() : new Schedule();
        this.commandManager = new CommandManager(this);
        this.config = config;

        this.initializeAliases();
    }

    public CommandResult execute(String userInput) {
        CommandResult result = commandManager.delegateCommand(userInput);
        lastCommandResult.setValue(result);
        this.saveSchedule();
        for (Command command: this.commandManager.getCommandList()) {
            this.config.setAliases(command.getTriggerWord(), command.getTriggerWords());
        }
        return result;
    }

    //@@author A0144915A
    public Schedule getSchedule() {
        return this.schedule;
    }

    public Config getConfig() {
        return this.config;
    }

    public ObjectProperty<CommandResult> getLastCommandResultProperty() {
        return this.lastCommandResult;
    }

    public ArrayList<Command> getCommandList() {
        return commandManager.getCommandList();
    }

    public void setScheduleFilePath(String path) {
        this.config.setScheduleFilePath(path);
    }

    public void saveSchedule() {
        this.scheduleStorage.saveScheduleToFile(schedule);
    }

    private void initializeAliases() {
        for (Command command: this.commandManager.getCommandList()) {
            command.setAliases(this.config.getAliases(command.getTriggerWord()));
        }
    }
}
