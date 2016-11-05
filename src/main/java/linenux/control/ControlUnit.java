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
    /**
     * Constructs a {@code ControlUnit} from a {@code Config}.
     * @param config A {@code Config} representing the application configuration.
     */
    public ControlUnit(Config config) {
        this.scheduleStorage = new XmlScheduleStorage(config);
        this.schedule = (this.scheduleStorage.hasScheduleFile()) ? this.scheduleStorage.loadScheduleFromFile() : new Schedule();
        this.commandManager = new CommandManager();
        this.config = config;

        this.initializeCommands();
        this.initializeAliases();
    }

    /**
     * Constructs a {@code ControlUnit} from {@code ScheduleStorage}, {@code Config}, and {@code CommandManager}.
     * @param storage The {@code ScheduleStorage} class that will be used to store {@code Schedule}.
     * @param config The application configuration.
     * @param commandManager The {@code CommandManager} that should be used.
     */
    public ControlUnit(ScheduleStorage storage, Config config, CommandManager commandManager) {
        this.scheduleStorage = storage;
        this.schedule = this.scheduleStorage.loadScheduleFromFile();
        this.config = config;
        this.commandManager = commandManager;
    }

    /**
     * Executes a user command.
     * @param userInput A {@code String}, which is the user input.
     * @return A {@code CommandResult} containing feedback for the user.
     */
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
    /**
     * @return The current {@code Schedule}.
     */
    public Schedule getSchedule() {
        return this.schedule;
    }

    /**
     * @return The application configuration.
     */
    public Config getConfig() {
        return (this.config);
    }

    /**
     * @return A reactive object which encapsulate the latest {@code CommandResult} shown to the user.
     */
    public ObjectProperty<CommandResult> getLastCommandResultProperty() {
        return this.lastCommandResult;
    }

    /**
     * @return An {@code ArrayList} of supported {@code Command}.
     */
    public ArrayList<Command> getCommandList() {
        return commandManager.getCommandList();
    }

    /**
     * Update the path to the schedule file.
     * @param path The absolute path to the new file.
     */
    public void setScheduleFilePath(String path) {
        this.config.setScheduleFilePath(path);
    }

    /**
     * Save the current {@code Schedule} into the file.
     */
    public void saveSchedule() {
        this.scheduleStorage.saveScheduleToFile(schedule);
    }

    /**
     * Load the current {@code Schedule} from file.
     */
    public void loadSchedule() {
        Schedule schedule;

        if (this.scheduleStorage.hasScheduleFile()) {
            schedule = this.scheduleStorage.loadScheduleFromFile();
        } else {
            schedule = new Schedule();
        }

        this.schedule.update(schedule);
    }

    /**
     * Add a listener that will be executed every time a command is processed.
     * @param listener A function that takes in the user input and the {@code CommandResult}.
     */
    public void addPostExecuteListener(BiConsumer<String, CommandResult> listener) {
        this.postExecuteListeners.add(listener);
    }

    /**
     * Setup aliases for the commands based on what's in the config file.
     */
    private void initializeAliases() {
        for (Command command: this.commandManager.getCommandList()) {
            command.setAliases(this.config.getAliases(command.getTriggerWord()));
        }
    }

    /**
     * Setup the default set of commands.
     */
    private void initializeCommands() {
        this.commandManager.addCommand(new AddCommand(this.schedule));
        this.commandManager.addCommand(new RemindCommand(this.schedule));
        this.commandManager.addCommand(new EditCommand(this.schedule));
        this.commandManager.addCommand(new EditReminderCommand(this.schedule));

        this.commandManager.addCommand(new RenameCommand(this.schedule));
        this.commandManager.addCommand(new DoneCommand(this.schedule));
        this.commandManager.addCommand(new UndoneCommand(this.schedule));
        this.commandManager.addCommand(new DeleteCommand(this.schedule));

        this.commandManager.addCommand(new DeleterCommand(this.schedule));
        this.commandManager.addCommand(new ClearCommand(this.schedule));
        this.commandManager.addCommand(new ListCommand(this.schedule));
        this.commandManager.addCommand(new TodayCommand(this.schedule));

        this.commandManager.addCommand(new TomorrowCommand(this.schedule));
        this.commandManager.addCommand(new ViewCommand(this.schedule));
        this.commandManager.addCommand(new FreeTimeCommand(this.schedule));
        this.commandManager.addCommand(new UndoCommand(this.schedule));

        this.commandManager.addCommand(new AliasCommand(this.commandManager.getCommandList()));
        this.commandManager.addCommand(new UnaliasCommand(this.commandManager.getCommandList()));
        this.commandManager.addCommand(new SaveCommand(this));
        this.commandManager.addCommand(new LoadCommand(this));

        this.commandManager.addCommand(new InformationCommand(this.config));
        this.commandManager.addCommand(new HelpCommand(this.commandManager.getCommandList()));
        this.commandManager.addCommand(new ExitCommand());

        this.commandManager.setCatchAllCommand(new InvalidCommand(this));
    }
}
