package linenux.logic;

import linenux.command.Command;
import linenux.command.CommandResult;
import linenux.data.Schedule;
import linenux.parser.Parser;

/**
 * Represents the main Logic of the application.
 */
public class Logic {
    private Schedule schedule;
    
    public Logic() {
        this.schedule = new Schedule();
    }
    
    /**
     * Parses the user command, executes it, and returns the result.
     */
    public CommandResult execute(String userCommandText) throws Exception {
        Command command = new Parser().parseCommand(userCommandText);
        CommandResult result = execute(command);
        return result;
    }

    /**
     * Executes the command, updates storage, and returns the result.
     */
    private CommandResult execute(Command command) throws Exception {
        command.setSchedule(schedule);
        CommandResult result = command.execute();
        return result;
    }
}
