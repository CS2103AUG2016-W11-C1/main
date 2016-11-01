package linenux.control;

import java.util.ArrayList;

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

/**
 * Assigns commands based on user input.
 */
public class CommandManager {
    private ArrayList<Command> commandList;
    private Schedule schedule;
    private Command catchAllCommand;
    private Config config;
    private ControlUnit controlUnit;

    //@@author A0144915A
    public CommandManager(ControlUnit controlUnit) {
        this.controlUnit = controlUnit;
        this.schedule = controlUnit.getSchedule();
        this.config = controlUnit.getConfig();
        commandList = new ArrayList<>();
        initializeCommands();
    }

    /**
     * Adds all supported commands to the commandList.
     */
    private void initializeCommands() {
        commandList.add(new AddCommand(this.schedule));
        commandList.add(new EditReminderCommand(this.schedule));
        commandList.add(new DeleterCommand(this.schedule));
        commandList.add(new EditCommand(this.schedule));
        commandList.add(new RenameCommand(this.schedule));
        commandList.add(new DoneCommand(this.schedule));
        commandList.add(new UndoneCommand(this.schedule));
        commandList.add(new RemindCommand(this.schedule));
        commandList.add(new DeleteCommand(this.schedule));
        commandList.add(new ClearCommand(this.schedule));

        commandList.add(new ListCommand(this.schedule));
        commandList.add(new ViewCommand(this.schedule));
        commandList.add(new TodayCommand(this.schedule));
        commandList.add(new TomorrowCommand(this.schedule));

        commandList.add(new SaveCommand(this.controlUnit));
        commandList.add(new LoadCommand(this.controlUnit));

        commandList.add(new UndoCommand(this.schedule));
        commandList.add(new FreeTimeCommand(this.schedule));
        commandList.add(new HelpCommand(this.commandList));
        commandList.add(new AliasCommand(this.commandList));
        commandList.add(new UnaliasCommand(this.commandList));
        commandList.add(new ExitCommand());

        commandList.add(new InformationCommand(this.config));

        this.catchAllCommand = new InvalidCommand(this.commandList);
    }

    //@@author A0135788M
    /**
     * Assigns the appropriate command to the user input. Contract: only 1
     * command should be awaiting user response at any point in time.
     */
    public CommandResult delegateCommand(String userInput) {
        for (Command command : commandList) {
            if (command.awaitingUserResponse()) {
                return command.getUserResponse(userInput);
            }
        }

        for (Command command : commandList) {
            if (command.respondTo(userInput)) {
                return command.execute(userInput);
            }
        }

        return this.catchAllCommand.execute(userInput);
    }

    /**
     * Getter for commandList.
     */
    //@@author A0135788M
    public ArrayList<Command> getCommandList() {
        return this.commandList;
    }
}
