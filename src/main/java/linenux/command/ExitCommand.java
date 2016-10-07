package linenux.command;

import linenux.command.result.CommandResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExitCommand implements Command {
    private static final String TASK_PATTERN = "(?i)^exit$";

    @Override
    public boolean respondTo(String userInput) {
        return userInput.matches(TASK_PATTERN);
    }

    @Override
    public CommandResult execute(String userInput) {
        Matcher matcher = Pattern.compile(TASK_PATTERN).matcher(userInput);
        
        if (matcher.matches()) {
            System.exit(0);
        }
        return null;
    }

}
