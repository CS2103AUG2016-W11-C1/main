package linenux.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.result.CommandResult;

/**
 * Exits the program.
 */
public class ExitCommand implements Command {
    private static final String TRIGGER_WORD = "exit";
    private static final String DESCRIPTION = "Exits the program.";
    public static final String COMMAND_FORMAT = "exit";

    private static final String EXIT_PATTERN = "(?i)^\\s*exit\\s*$";

    @Override
    public boolean respondTo(String userInput) {
        return userInput.matches(EXIT_PATTERN);
    }

    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(EXIT_PATTERN);

        Matcher matcher = Pattern.compile(EXIT_PATTERN).matcher(userInput);

        if (matcher.matches()) {
            // TODO: Platform.exit();
            System.exit(0);
        }
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
