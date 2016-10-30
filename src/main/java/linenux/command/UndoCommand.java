package linenux.command;

import linenux.command.result.CommandResult;
import linenux.model.Schedule;

//@@author A0135788M
/**
 * Undo the previous command that mutated the state of the schedule.
 */
public class UndoCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "undo";
    private static final String DESCRIPTION = "Undo the previous command.";
    private static final String COMMAND_FORMAT = "undo";

    private Schedule schedule;

    public UndoCommand(Schedule schedule) {
        this.schedule = schedule;
        this.TRIGGER_WORDS.add(TRIGGER_WORD);
    }

    /**
     * Executes the command based on {@code userInput}. This method operates under the assumption that
     * {@code respondTo(userInput)} is {@code true}.
     * @param userInput A {@code String} representing the user input.
     * @return A {@code CommandResult} representing the result of the command.
     */
    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(getPattern());
        assert this.schedule != null;

        if (this.schedule.popState()) {
            return makeUndoSuccessfulMessage();
        } else {
            return makeUndoUnsuccessfulMessage();
        }
    }

    /**
     * @return A {@code String} representing the default command word.
     */
    @Override
    public String getTriggerWord() {
        return TRIGGER_WORD;
    }

    /**
     * @return A {@code String} describing what this {@code Command} does.
     */
    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    /**
     * @return A {@code String} describing the format that this {@code Command} expects.
     */
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
