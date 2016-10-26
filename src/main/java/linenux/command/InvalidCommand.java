package linenux.command;

import java.util.ArrayList;

import linenux.command.result.CommandResult;
import linenux.util.StringsSimilarity;

/**
 * Act as a fail-safe for invalid or unrecognized commands.
 */
public class InvalidCommand extends AbstractCommand {
    private ArrayList<Command> commands;

    //@@author A0144915A
    public InvalidCommand(ArrayList<Command> commands) {
        this.commands = commands;
    }

    /**
     * @return true for all user inputs.
     */
    @Override
    //@@author A0135788M
    public boolean respondTo(String userInput) {
        return true;
    }

    @Override
    //@@author A0144915A
    public CommandResult execute(String userInput) {
        String userCommand = extractCommand(userInput);
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
            return this.makeResponse();
        } else {
            return this.makeResponseWithSuggestion(closestCommand.getTriggerWord());
        }
    }

    //@@author A0135788M
    @Override
    public String getTriggerWord() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getCommandFormat() {
        return null;
    }

    @Override
    public String getPattern() {
        return null;
    }

    //@@author A0144915A
    private String extractCommand(String userInput) {
        String[] parts = userInput.split(" ");

        if (parts.length > 0) {
            return parts[0];
        } else {
            return "";
        }
    }

    private CommandResult makeResponse() {
        return () -> "Invalid command.";
    }

    private CommandResult makeResponseWithSuggestion(String suggestion) {
        return () -> "Invalid command. Did you mean " + suggestion + "?";
    }
}
