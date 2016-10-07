package linenux.control;

import linenux.command.*;
import linenux.model.Schedule;
import linenux.command.result.CommandResult;

import java.util.ArrayList;

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
        commandList.add(new ListCommand(this.schedule));
        commandList.add(new DeleteCommand(this.schedule));
        commandList.add(new ExitCommand());
        commandList.add(new HelpCommand(this.commandList));

        this.catchAllCommand = new InvalidCommand(this.commandList);
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

        return this.catchAllCommand.execute(userInput);
    }

    /**
     * Sets the reference for the schedule.
     */
    private void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
