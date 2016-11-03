package linenux.control;

import java.util.ArrayList;

import linenux.command.Command;
import linenux.command.result.CommandResult;

/**
 * Assigns commands based on user input.
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
            if (command.awaitingUserResponse()) {
                return command.getUserResponse(userInput);
            }
        }

        for (Command command : this.commands) {
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
        return this.commands;
    }

    //@@author A0144915A
    public void addCommand(Command command) {
        this.commands.add(command);
    }

    public void setCatchAllCommand(Command command) {
        this.catchAllCommand = command;
    }
}
