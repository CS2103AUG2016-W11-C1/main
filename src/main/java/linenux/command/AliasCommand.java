package linenux.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.result.CommandResult;
import linenux.util.AliasUtil;

/**
 * Creates an alias for commands.
 */
public class AliasCommand implements Command {
    private static final String TRIGGER_WORD = "alias";
    private static final String DESCRIPTION = "Creates an alias for commands.";
    private static final String COMMAND_FORMAT = "alias COMMAND_NAME NEW_NAME";

    private static final String ALPHANUMERIC = "^[a-zA-Z0-9]*$";

    @Override
    public boolean respondTo(String userInput) {
        return userInput.matches(getPattern());
    }

    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(getPattern());

        String arguments = extractArguments(userInput);
        String[] commandNames = arguments.trim().split("\\s+");

        if (commandNames.length != 2) {
            return makeInvalidArgumentResult();
        }
        if (!validCommand(commandNames[0])) {
            return makeNoSuchCommandResult();
        }
        if (!validAlias(commandNames[1])) {
            return makeInvalidAliasResult();
        }
        AliasUtil.ALIASMAP.put(commandNames[0], commandNames[1]);
        return makeSuccessfulAliasResult(commandNames);
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
        return "(?i)^(" + TRIGGER_WORD + "|" + AliasUtil.ALIASMAP.get(TRIGGER_WORD) + ")(\\s+(?<arguments>.*))?$";
    }

    private String extractArguments(String userInput) {
        Matcher matcher = Pattern.compile(getPattern()).matcher(userInput);

        if (matcher.matches() && matcher.group("arguments") != null) {
            return matcher.group("arguments").trim();
        } else {
            return "";
        }
    }

    private boolean validCommand(String command) {
        return AliasUtil.ALIASMAP.containsKey(command);
    }

    private boolean validAlias(String alias) {
        Matcher matcher = Pattern.compile(ALPHANUMERIC).matcher(alias);
        return matcher.matches();
    }

    private CommandResult makeInvalidArgumentResult() {
        return () -> "Invalid arguments.\n\n" + COMMAND_FORMAT + "\n\n" + CALLOUTS;
    }

    private CommandResult makeNoSuchCommandResult() {
        return () -> "No such command to make alias for.";
    }

    private CommandResult makeInvalidAliasResult() {
        return () -> "Alias must be alphanumeric.";
    }

    private CommandResult makeSuccessfulAliasResult(String[] commands) {
        return () -> commands[1] + " is now the alias for the " + commands[0] + " command.";
    }
}
