package linenux.control;

import java.util.ArrayList;
import java.util.function.BiConsumer;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import linenux.command.AddCommand;
import linenux.command.AliasCommand;
import linenux.command.ClearCommand;
import linenux.command.Command;
import linenux.command.DeleteCommand;
import linenux.command.DeleterCommand;
import linenux.command.DoneCommand;
import linenux.command.EditCommand;
import linenux.command.EditReminderCommand;
import linenux.command.ExitCommand;
import linenux.command.FreeTimeCommand;
import linenux.command.HelpCommand;
import linenux.command.InformationCommand;
import linenux.command.InvalidCommand;
import linenux.command.ListCommand;
import linenux.command.LoadCommand;
import linenux.command.RemindCommand;
import linenux.command.RenameCommand;
import linenux.command.SaveCommand;
import linenux.command.TodayCommand;
import linenux.command.TomorrowCommand;
import linenux.command.UnaliasCommand;
import linenux.command.UndoCommand;
import linenux.command.UndoneCommand;
import linenux.command.ViewCommand;
import linenux.command.result.CommandResult;
import linenux.config.Config;
import linenux.model.Schedule;
import linenux.storage.ScheduleStorage;
import linenux.storage.XmlScheduleStorage;
import linenux.util.LogsCenter;

/**
 * Controls data flow for the entire application.
 */
public class ControlUnit {
    private Schedule schedule;
    private ScheduleStorage scheduleStorage;
    private CommandManager commandManager;
    private ObjectProperty<CommandResult> lastCommandResult = new SimpleObjectProperty<>();
    private Config config;
    private ArrayList<BiConsumer<String, CommandResult>> postExecuteListeners = new ArrayList<>();

    //@@author A0135788M
    public ControlUnit(Config config) {
        this.scheduleStorage = new XmlScheduleStorage(config);
        this.schedule = (this.scheduleStorage.hasScheduleFile()) ? this.scheduleStorage.loadScheduleFromFile() : new Schedule();
        this.commandManager = new CommandManager();
        this.config = config;

        this.initializeCommands();
        this.initializeAliases();
    }

    public ControlUnit(ScheduleStorage storage, Config config, CommandManager commandManager) {
        this.scheduleStorage = storage;
        this.config = config;
        this.commandManager = commandManager;
    }

    public CommandResult execute(String userInput) {
        CommandResult result = commandManager.delegateCommand(userInput);
        lastCommandResult.setValue(result);
        this.saveSchedule();
        for (Command command: this.commandManager.getCommandList()) {
            this.config.setAliases(command.getTriggerWord(), command.getTriggerWords());
        }

        for (BiConsumer<String, CommandResult> listener: this.postExecuteListeners) {
            listener.accept(userInput, result);
        }

        return result;
    }

    //@@author A0144915A
    public Schedule getSchedule() {
        return this.schedule;
    }

    public Config getConfig() {
        return (this.config);
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

    public void loadSchedule() {
        Schedule schedule;

        if (this.scheduleStorage.hasScheduleFile()) {
            schedule = this.scheduleStorage.loadScheduleFromFile();
        } else {
            schedule = new Schedule();
        }

        this.schedule.update(schedule);
    }

    public void addPostExecuteListener(BiConsumer<String, CommandResult> listener) {
        this.postExecuteListeners.add(listener);
    }

    private void initializeAliases() {
        for (Command command: this.commandManager.getCommandList()) {
            command.setAliases(this.config.getAliases(command.getTriggerWord()));
        }
    }

    private void initializeCommands() {
        this.commandManager.addCommand(new AddCommand(this.schedule));
        this.commandManager.addCommand(new EditReminderCommand(this.schedule));
        this.commandManager.addCommand(new DeleterCommand(this.schedule));
        this.commandManager.addCommand(new EditCommand(this.schedule));
        this.commandManager.addCommand(new RenameCommand(this.schedule));
        this.commandManager.addCommand(new DoneCommand(this.schedule));
        this.commandManager.addCommand(new UndoneCommand(this.schedule));
        this.commandManager.addCommand(new RemindCommand(this.schedule));
        this.commandManager.addCommand(new DeleteCommand(this.schedule));
        this.commandManager.addCommand(new ClearCommand(this.schedule));

        this.commandManager.addCommand(new ListCommand(this.schedule));
        this.commandManager.addCommand(new ViewCommand(this.schedule));
        this.commandManager.addCommand(new TodayCommand(this.schedule));
        this.commandManager.addCommand(new TomorrowCommand(this.schedule));

        this.commandManager.addCommand(new SaveCommand(this));
        this.commandManager.addCommand(new LoadCommand(this));

        this.commandManager.addCommand(new UndoCommand(this.schedule));
        this.commandManager.addCommand(new FreeTimeCommand(this.schedule));
        this.commandManager.addCommand(new HelpCommand(this.commandManager.getCommandList()));
        this.commandManager.addCommand(new AliasCommand(this.commandManager.getCommandList()));
        this.commandManager.addCommand(new UnaliasCommand(this.commandManager.getCommandList()));
        this.commandManager.addCommand(new ExitCommand());
        this.commandManager.addCommand(new InformationCommand(this.config));

        this.commandManager.setCatchAllCommand(new InvalidCommand(this));
    }
}
