package linenux.command;

import java.util.LinkedList;

import linenux.command.result.CommandResult;
import linenux.model.Schedule;
import linenux.model.State;
import linenux.util.Either;

/**
 * Undo the previous command that mutated the state of the schedule.
 */
public class UndoCommand implements Command {
    private static final String TRIGGER_WORD = "undo";
    private static final String DESCRIPTION = "Undo the previous command.";
    private static final String COMMAND_FORMAT = "undo";

    private static final String UNDO_PATTERN = "(?i)^\\s*undo\\s*$";

    private Schedule schedule;

    public UndoCommand(Schedule schedule) {
        this.schedule = schedule;
    }

    @Override
    public boolean respondTo(String userInput) {
        return userInput.matches(UNDO_PATTERN);
    }

    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(UNDO_PATTERN);
        assert this.schedule != null;

        if (this.schedule.popState()) {
            return makeUndoSuccessfulMessage();
        } else {
            return makeUndoUnsuccessfulMessage();
        }
    }

    @Override
    public String getTriggerWord() {
        return TRIGGER_WORD;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public String getCommandFormat() {
        return COMMAND_FORMAT;
    }

    private CommandResult makeUndoSuccessfulMessage() {
        return () -> "Successfully undo last command.";
    }

    private CommandResult makeUndoUnsuccessfulMessage() {
        return () -> "No more commands to undo!";
    }

}
