package linenux.command;

import linenux.command.result.CommandResult;
import linenux.util.AliasUtil;

/**
 * Exits the program.
 */
public class ExitCommand implements Command {
    private static final String TRIGGER_WORD = "exit";
    private static final String DESCRIPTION = "Exits the program.";
    public static final String COMMAND_FORMAT = "exit";

    @Override
    public boolean respondTo(String userInput) {
        return userInput.matches(getPattern());
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

    @Override
    public String getPattern() {
        return "(?i)^\\s*(" + TRIGGER_WORD + "|" + AliasUtil.ALIASMAP.get(TRIGGER_WORD) + ")\\s*$";
    }
}
