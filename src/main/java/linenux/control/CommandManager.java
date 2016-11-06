package linenux.control;

import java.util.ArrayList;

import linenux.command.Command;
import linenux.command.result.CommandResult;

/**
 * A collection class to contain all commands that the program can handle.
 */
public class CommandManager {
    private ArrayList<Command> commands = new ArrayList<>();
    private Command catchAllCommand = null;

    //@@author A0135788M
    /**
     * Assigns the appropriate command to the user input. Contract: only 1
     * command should be awaiting user response at any point in time.
     */
    public CommandResult delegateCommand(String userInput) {
        for (Command command : this.commands) {
            if (command.isAwaitingUserResponse()) {
                return command.processUserResponse(userInput);
            }
        }

        for (Command command : this.commands) {
            if (command.respondTo(userInput)) {
                return command.execute(userInput);
            }
        }

        return this.catchAllCommand.execute(userInput);
    }

    //@@author A0135788M
    /**
     * @return An {@code ArrayList} of {@code Command}.
     */
    public ArrayList<Command> getCommandList() {
        return this.commands;
    }

    //@@author A0144915A
    /**
     * Add a new {@code Command} into the manager.
     * @param command The new {@code Command} to add.
     */
    public void addCommand(Command command) {
        this.commands.add(command);
    }

    /**
     * Set the catch all command which will be used when no other commands can handle some input.
     * @param command A {@code Command} that can handle any user input.
     */
    public void setCatchAllCommand(Command command) {
        this.catchAllCommand = command;
    }
}
