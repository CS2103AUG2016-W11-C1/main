package linenux.command;

import linenux.command.result.CommandResult;
import linenux.util.StringsSimilarity;

import java.util.ArrayList;

/**
 * Act as a fail-safe for invalid or unrecognized commands.
 */
public class InvalidCommand implements Command {
    private ArrayList<Command> commands;

    public InvalidCommand(ArrayList<Command> commands) {
        this.commands = commands;
    }

    @Override
    public String getTriggerWord() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    /**
     * @return true for all user inputs.
     */
    @Override
    public boolean respondTo(String userInput) {
        return true;
    }

    @Override
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
