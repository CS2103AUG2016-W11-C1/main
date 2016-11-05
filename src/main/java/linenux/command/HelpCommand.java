package linenux.command;

import java.util.ArrayList;
import java.util.Set;

import linenux.command.result.CommandResult;
import linenux.util.StringsSimilarity;

//@@author A0140702X
/**
 * Displays available command and their formats.
 */
public class HelpCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "help";
    private static final String DESCRIPTION = "Shows this help message.";
    private static final String COMMAND_FORMAT = "help [COMMAND_NAME]";

    private ArrayList<Command> commands;

    public HelpCommand(ArrayList<Command> commands) {
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

        String keywords = extractArgument(userInput);
        Command commandRequested = null;

        if (keywords.trim().isEmpty()) {
            return () -> displayAllHelp();
        }

        String[] parts = keywords.split(" ");

        if (parts.length > 1) {
            return makeInvalidKeywordResponse();
        }

        for (Command command : this.commands) {
            if (command.getTriggerWord().equals(keywords)) {
                commandRequested = command;
                break;
            }
        }

        if (commandRequested == null) {
            return makeInvalidCommandResponse(keywords);
        }

        return displaySpecificHelp(commandRequested);
    }

    private CommandResult displaySpecificHelp(Command commandRequested) {
        return () -> makeHelpDescriptionForCommand(commandRequested);
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

    private String displayAllHelp() {
        StringBuilder builder = new StringBuilder();
        for (Command command: this.commands) {
            builder.append(makeHelpDescriptionForCommand(command));
        }

        builder.append(CALLOUTS);

        return builder.toString();
    }

    private String makeHelpDescriptionForCommand(Command command) {
        StringBuilder builder = new StringBuilder();
        Set<String> aliasList = command.getTriggerWords();


        builder.append(command.getTriggerWord());
        for (String alias : aliasList) {
            if (alias.equals(command.getTriggerWord())) {
                continue;
            }

            builder.append(", " + alias);
        }
        builder.append(" - ");
        builder.append("\n");

        builder.append("Description: ");
        builder.append(command.getDescription());
        builder.append('\n');

        builder.append("Format: ");
        builder.append(command.getCommandFormat());
        builder.append("\n\n");

        return builder.toString();
    }

    private CommandResult makeInvalidKeywordResponse() {
        return () -> "Too many arguments given. Please only search for one command at a time.";
    }

    private CommandResult makeInvalidCommandResponse(String userInput) {
        assert (userInput.split(" ").length == 1);

        String userCommand = userInput;
        Command closestCommand = null;
        int bestScore = Integer.MAX_VALUE;

        for (Command command: this.commands) {
            int similarity = StringsSimilarity.compute(userCommand, command.getTriggerWord());
            if (similarity < bestScore) {
                closestCommand = command;
                bestScore = similarity;
            }
        }

        if (closestCommand == null) {
            return makeInvalidCommandResponse();
        } else {
            return makeResponseWithSuggestion(closestCommand.getTriggerWord());
        }
    }

    private CommandResult makeInvalidCommandResponse() {
        return () -> "Invalid command.";
    }

    private CommandResult makeResponseWithSuggestion(String suggestion) {
        return () -> "Invalid command given for help. Did you mean \'" + suggestion + "\'?";
    }
}
