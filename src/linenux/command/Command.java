package linenux.command;

import linenux.command.CommandResult;
import linenux.data.Schedule;

/**
 * Represents an executable command.
 */
public abstract class Command {
    protected Schedule schedule;
    
    public abstract CommandResult execute();
    
    /**
     * Sets a reference to the schedule for the commands to operate on.
     */
    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
