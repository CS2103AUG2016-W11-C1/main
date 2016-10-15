package linenux.command;

import java.util.LinkedList;

import linenux.command.result.CommandResult;
import linenux.model.Schedule;
import linenux.model.State;
import linenux.util.Either;

public class UndoCommand implements Command {
    private static final String TRIGGER_WORD = "undo";
    private static final String DESCRIPTION = "Undo the previous command.";

    private static final String UNDO_PATTERN = "(?i)^undo$";

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

        Either<CommandResult, CommandResult> outcome = tryUndo();
        if (outcome.isLeft()) {
            return outcome.getLeft();
        } else {
            return outcome.getRight();
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

    private Either<CommandResult, CommandResult> tryUndo() {
        LinkedList<State> states = schedule.getStates();
        if (states.size() == 1) {
            return Either.right(makeUndoUnsuccessfulMessage());
        } else {
            states.removeLast();
            return Either.left(makeUndoSuccessfulMessage());
        }
    }

    private CommandResult makeUndoSuccessfulMessage() {
        return () -> "Successfully undo last command.";
    }

    private CommandResult makeUndoUnsuccessfulMessage() {
        return () -> "No more commands to undo!";
    }

}
