package linenux.command;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.result.CommandResult;

/**
 * Creates an alias for commands.
 */
public class AliasCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "alias";
    private static final String DESCRIPTION = "Creates an alias for commands.";
    private static final String COMMAND_FORMAT = "alias COMMAND_NAME NEW_NAME";

    private static final String ALPHANUMERIC = "^[a-zA-Z0-9]*$";

    private ArrayList<Command> commands;

    public AliasCommand(ArrayList<Command> commands) {
        this.commands = commands;
        this.TRIGGER_WORDS.add(TRIGGER_WORD);
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

        for (Command command: this.commands) {
            if (command.respondTo(commandNames[0])) {
                command.setAlias(commandNames[1]);
            }
            break;
        }

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

    private String extractArguments(String userInput) {
        Matcher matcher = Pattern.compile(getPattern()).matcher(userInput);

        if (matcher.matches() && matcher.group("keywords") != null) {
            return matcher.group("keywords").trim();
        } else {
            return "";
        }
    }

    private boolean validCommand(String command) {
        for (Command cmd: this.commands) {
            if (cmd.respondTo(command)) {
                return true;
            }
        }
        return false;
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
