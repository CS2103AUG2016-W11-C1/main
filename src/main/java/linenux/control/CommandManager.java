package linenux.control;

import java.util.ArrayList;

import linenux.command.AddCommand;
import linenux.command.Command;
import linenux.command.DeleteCommand;
import linenux.command.DoneCommand;
import linenux.command.ExitCommand;
import linenux.command.InvalidCommand;
import linenux.command.ListCommand;
import linenux.command.result.CommandResult;
import linenux.model.Schedule;

/**
 * Assigns commands based on user input.
 */
public class CommandManager {
    private ArrayList<Command> commandList;
    private Schedule schedule;

    public CommandManager(Schedule schedule) {
        this.schedule = schedule;
        commandList = new ArrayList<Command>();
        initializeCommands();
    }

    /**
     * Adds all supported commands to the commandList.
     */
    private void initializeCommands() {
        commandList.add(new AddCommand(this.schedule));
        commandList.add(new ListCommand(this.schedule));
        commandList.add(new DeleteCommand(this.schedule));
        commandList.add(new DoneCommand(this.schedule));
        commandList.add(new ExitCommand());
        commandList.add(new InvalidCommand()); // Must be the last element in
                                               // the list.
    }

    /**
     * Assigns the appropriate command to the user input.
     * Contract: only 1 command should be awaiting user response at any point in time.
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

        return null;
    }

    /**
     * Sets the reference for the schedule.
     */
    private void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
