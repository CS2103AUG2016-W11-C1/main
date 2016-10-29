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
    public ExitCommand() {
        this.TRIGGER_WORDS.add(TRIGGER_WORD);
    }

    //@@author A0135788M
    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(getPattern());

        // TODO: Platform.exit();
        System.exit(0);
        return null;
    }

    //@@author A0144915A
    @Override
    public String getTriggerWord() {
        return TRIGGER_WORD;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    //@@author A0135788M
    @Override
    public String getCommandFormat() {
        return COMMAND_FORMAT;
    }
}
