package linenux.command;

import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.result.CommandResult;
import linenux.util.StringsSimilarity;

/**
 * Displays available command and their formats.
 */
public class HelpCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "help";
    private static final String DESCRIPTION = "Shows this help message.";
    private static final String COMMAND_FORMAT = "help";

    private ArrayList<Command> commands;

    //@@author A0144915A
    public HelpCommand(ArrayList<Command> commands) {
        this.commands = commands;
        this.TRIGGER_WORDS.add(TRIGGER_WORD);
    }

    //@@author A0140702X
    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(getPattern());

        String keywords = extractKeywords(userInput);
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

    //@@author A0135788M
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

    //@@author A0140702X
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

    //@@author A0140702X
    private String extractKeywords(String userInput) {
        Matcher matcher = Pattern.compile(getPattern()).matcher(userInput);

        if (matcher.matches() && matcher.group("keywords") != null) {
            return matcher.group("keywords").trim();
        } else {
            return "";
        }
    }
}
