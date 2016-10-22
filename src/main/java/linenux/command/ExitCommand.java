package linenux.command;

import linenux.command.result.CommandResult;

/**
 * Exits the program.
 */
public class ExitCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "exit";
    private static final String DESCRIPTION = "Exits the program.";
    public static final String COMMAND_FORMAT = "exit";

    public ExitCommand() {
        this.TRIGGER_WORDS.add(TRIGGER_WORD);
    }

    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(getPattern());

        // TODO: Platform.exit();
        System.exit(0);
        return null;
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
}
