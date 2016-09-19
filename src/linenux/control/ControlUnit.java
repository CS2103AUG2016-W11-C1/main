package linenux.control;

import linenux.model.Schedule;
import linenux.parser.Parser;
import linenux.parser.commands.Command;

/**
 * Controls data flow for the entire application.
 */

public class ControlUnit {
    private Schedule schedule;
    private Parser parser;
    
    public ControlUnit() {
        this.schedule = (hasExistingSchedule()) ? getExistingSchedule() : new Schedule();
        this.parser = new Parser();
    }

    public String[] execute(String userInput) {
        return null;
     }
    
    // TODO: Check if JAXB schedule file exist.
    private boolean hasExistingSchedule() {
        return false;
    }
    
    // TODO: Get existing JAXB schedule.
    private Schedule getExistingSchedule() {
        return null;
    }
}
