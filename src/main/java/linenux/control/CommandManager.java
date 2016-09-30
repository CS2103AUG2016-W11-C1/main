package linenux.control;

import linenux.model.Schedule;
import linenux.command.Command;
import linenux.command.AddCommand;
import linenux.command.InvalidCommand;
import linenux.command.result.CommandResult;

import java.util.ArrayList;

public class CommandManager {
    private ArrayList<Command> commandList;
    private Schedule schedule;

    public CommandManager(Schedule schedule) {
        commandList = new ArrayList<Command>();
        initializeCommands();
    }

    /**
     * Adds all supported commands to the commandList.
     */
    private void initializeCommands() {
        commandList.add(new AddCommand());
        commandList.add(new InvalidCommand()); // Must be the last element in
                                               // the list.
    }

    /**
     * Assigns the appropriate command to the user input.
     */
    public CommandResult delegateCommand(String userInput) {
        for (Command command : commandList) {
            if (command.respondTo(schedule, userInput)) {
                return command.execute();
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
