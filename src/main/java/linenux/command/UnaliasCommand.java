package linenux.command;

import linenux.command.result.CommandResult;

import java.util.ArrayList;

//@@author A0144915A
public class UnaliasCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "unalias";
    private static final String DESCRIPTION = "Removes an alias.";
    private static final String COMMAND_FORMAT = "unalias ALIAS";

    private ArrayList<Command> commands;

    public UnaliasCommand(ArrayList<Command> commands) {
        this.TRIGGER_WORDS.add(TRIGGER_WORD);
        this.commands = commands;
    }

    /**
     * Executes the command based on {@code userInput}. This method operates under the assumption that
     * {@code respondTo(userInput)} is {@code true}.
     * @param userInput A {@code String} representing the user input.
     * @return A {@code CommandResult} representing the result of the command.
     */
    @Override
    public CommandResult execute(String userInput) {
        String argument = extractArgument(userInput);

        for (Command command: this.commands) {
            if (command.getTriggerWord().equals(argument)) {
                return makeUnaliasDefaultResult(argument);
            } else if (command.respondTo(argument)) {
                command.removeAlias(argument);
                return makeUnaliasResult(argument);
            }
        }

        return makeInvalidAliasResult(argument);
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

    private CommandResult makeUnaliasResult(String alias) {
        return () -> "\"" + alias + "\" is removed as an alias.";
    }

    private CommandResult makeInvalidAliasResult(String alias) {
        return () -> "\"" + alias + "\" is not an alias.";
    }

    private CommandResult makeUnaliasDefaultResult(String alias) {
        return () -> "\"" + alias + "\" cannot be removed as an alias.";
    }
}
