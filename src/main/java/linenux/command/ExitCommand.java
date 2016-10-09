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
    private static final String TASK_PATTERN = "(?i)^exit$";

    @Override
    public String getTriggerWord() {
        return TRIGGER_WORD;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public boolean respondTo(String userInput) {
        return userInput.matches(TASK_PATTERN);
    }

    @Override
    public CommandResult execute(String userInput) {
        Matcher matcher = Pattern.compile(TASK_PATTERN).matcher(userInput);

        if (matcher.matches()) {
            // TODO: Platform.exit();
            System.exit(0);
        }
        return null;
    }
}
