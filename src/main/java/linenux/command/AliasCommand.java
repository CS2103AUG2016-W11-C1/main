package linenux.command;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.result.CommandResult;

//@@author A0135788M
/**
 * Creates an alias for commands.
 */
public class AliasCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "alias";
    private static final String DESCRIPTION = "Creates an alias for commands.";
    private static final String COMMAND_FORMAT = "alias COMMAND_NAME NEW_NAME";

    private static final String ALPHANUMERIC = "^[a-zA-Z0-9]*$";

    private ArrayList<Command> commands;

    /**
     * Constructs an {@code AliasCommand}.
     * @param commands An {@code ArrayList} of {@code Command} that can be aliased.
     */
    public AliasCommand(ArrayList<Command> commands) {
        this.commands = commands;
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

        String arguments = extractArgument(userInput);
        String[] commandNames = arguments.trim().split("\\s+");

        if (commandNames.length != 2) {
            return makeInvalidArgumentResult();
        }

        String command = commandNames[0];
        String alias = commandNames[1];

        if (!isValidCommand(command)) {
            return makeNoSuchCommandResult();
        }

        if (!isValidAlias(alias)) {
            return makeInvalidAliasResult();
        }

        if (!isAliasAvailable(alias)) {
            return makeAliasUsedForAnotherCommand(alias);
        }

        for (Command cmd: this.commands) {
            if (cmd.respondTo(command)) {
                cmd.setAlias(alias);
                break;
            }
        }

        return makeSuccessfulAliasResult(command, alias);
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

    /**
     * @param command A {@code String} representing a trigger word for some {@code Command}.
     * @return {@code true} if and only if there is some {@code Command} that respond to {@code command}.
     */
    private boolean isValidCommand(String command) {
        for (Command cmd: this.commands) {
            if (cmd.respondTo(command)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if {@code alias} is a valid alias.
     * @param alias A {@code String} representing the alias.
     * @return {@code true} if and only if {@code alias} is a valid alias.
     */
    private boolean isValidAlias(String alias) {
        Matcher matcher = Pattern.compile(ALPHANUMERIC).matcher(alias);
        return matcher.matches();
    }

    /**
     * Check if {@code alias} can be used as an alias.
     * @param alias A {@code String} representing the alias.
     * @return {@code true} if and only if {@code alias} is not used by some other {@code Command}.
     */
    private boolean isAliasAvailable(String alias) {
        for (Command cmd: this.commands) {
            if (cmd.respondTo(alias)) {
                return false;
            }
        }

        return true;
    }

    /**
     * @return A {@code CommandResult} indicating that the input format is invalid.
     */
    private CommandResult makeInvalidArgumentResult() {
        return () -> "Invalid arguments.\n\n" + COMMAND_FORMAT + "\n\n" + CALLOUTS;
    }

    /**
     * @return A {@code CommandResult} indicating that the {@code Command} that the user is trying to alias does not
     * exist.
     */
    private CommandResult makeNoSuchCommandResult() {
        return () -> "No such command to make alias for.";
    }

    /**
     * @return A {@code CommandResult} indicating that the alias that the user specified is not a valid alias.
     */
    private CommandResult makeInvalidAliasResult() {
        return () -> "Alias must be alphanumeric.";
    }

    /**
     * @param alias A {@code String} representing the alias the that user is trying to create.
     * @return A {@code CommandResult} indicating the {@code alias} is used for some other {@code Command}.
     */
    private CommandResult makeAliasUsedForAnotherCommand(String alias) {
        return () -> "\"" + alias + "\" is used for another command.";
    }

    /**
     * @param command The {@code Command} that {@code alias} is now pointing to.
     * @param alias A {@code String} representing the alias that the user is creating.
     * @return A {@code CommandResult} indicating that the alias is created.
     */
    private CommandResult makeSuccessfulAliasResult(String command, String alias) {
        return () -> alias + " is now the alias for the " + command + " command.";
    }
}
