package linenux.command;

import linenux.command.result.CommandResult;

/**
 * Exits the program.
 */
public class ExitCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "exit";
    private static final String DESCRIPTION = "Exits the program.";
    public static final String COMMAND_FORMAT = "exit";

    //@@author A0144915A
    /**
     * Constructs an {@code ExitCommand}.
     */
    public ExitCommand() {
        this.TRIGGER_WORDS.add(TRIGGER_WORD);
    }

    //@@author A0135788M
    /**
     * Executes the command based on {@code userInput}. This method operates under the assumption that
     * {@code respondTo(userInput)} is {@code true}.
     * @param userInput A {@code String} representing the user input.
     * @return A {@code CommandResult} representing the result of the command.
     */
    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(getPattern());

        System.exit(0);
        return null;
    }

    //@@author A0144915A
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

    //@@author A0135788M
    /**
     * @return A {@code String} describing the format that this {@code Command} expects.
     */
    @Override
    public String getCommandFormat() {
        return COMMAND_FORMAT;
    }
}
