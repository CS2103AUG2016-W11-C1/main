package linenux.control;

import java.util.ArrayList;

import linenux.command.AddCommand;
import linenux.command.AliasCommand;
import linenux.command.ClearCommand;
import linenux.command.Command;
import linenux.command.DeleteCommand;
import linenux.command.DoneCommand;
import linenux.command.EditCommand;
import linenux.command.ExitCommand;
import linenux.command.FreeTimeCommand;
import linenux.command.HelpCommand;
import linenux.command.InvalidCommand;
import linenux.command.ListCommand;
import linenux.command.RemindCommand;
import linenux.command.UnaliasCommand;
import linenux.command.UndoCommand;
import linenux.command.ViewCommand;
import linenux.command.result.CommandResult;
import linenux.model.Schedule;

/**
 * Assigns commands based on user input.
 */
public class CommandManager {
    private ArrayList<Command> commandList;
    private Schedule schedule;
    private Command catchAllCommand;

    public CommandManager(Schedule schedule) {
        this.schedule = schedule;
        commandList = new ArrayList<>();
        initializeCommands();
    }

    /**
     * Adds all supported commands to the commandList.
     */
    private void initializeCommands() {
        commandList.add(new AddCommand(this.schedule));
        commandList.add(new EditCommand(this.schedule));
        commandList.add(new DoneCommand(this.schedule));
        commandList.add(new RemindCommand(this.schedule));
        commandList.add(new DeleteCommand(this.schedule));
        commandList.add(new ClearCommand(this.schedule));

        commandList.add(new ListCommand(this.schedule));
        commandList.add(new ViewCommand(this.schedule));

        commandList.add(new UndoCommand(this.schedule));
        commandList.add(new FreeTimeCommand(this.schedule));
        commandList.add(new HelpCommand(this.commandList));
        commandList.add(new AliasCommand(this.commandList));
        commandList.add(new UnaliasCommand(this.commandList));
        commandList.add(new ExitCommand());

        this.catchAllCommand = new InvalidCommand(this.commandList);
    }

    /**
     * Assigns the appropriate command to the user input. Contract: only 1
     * command should be awaiting user response at any point in time.
     */
    public CommandResult delegateCommand(String userInput) {
        for (Command command : commandList) {
            if (command.awaitingUserResponse()) {
                return command.userResponse(userInput);
            }
        }

        for (Command command : commandList) {
            if (command.respondTo(userInput)) {
                return command.execute(userInput);
            }
        }

        return this.catchAllCommand.execute(userInput);
    }
}
