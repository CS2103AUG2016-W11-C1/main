package linenux.command;

import linenux.command.result.CommandResult;
import linenux.control.ControlUnit;
import linenux.util.StringsSimilarity;

/**
 * Act as a fail-safe for invalid or unrecognized commands.
 */
public class InvalidCommand extends AbstractCommand {
    private ControlUnit controlUnit;

    private CommandResult lastCommandResult = null;
    private String lastCorrectedCommand = null;

    //@@author A0144915A
    public InvalidCommand(ControlUnit controlUnit) {
        this.controlUnit = controlUnit;
        this.controlUnit.addPostExecuteListener((userInput, commandResult) -> {
            if (commandResult != lastCommandResult) {
                lastCorrectedCommand = null;
            }
        });
    }

    //@@author A0135788M
    /**
     * @return true for all user inputs.
     */
    @Override
    public boolean respondTo(String userInput) {
        return true;
    }

    //@@author A0144915A
    @Override
    public CommandResult execute(String userInput) {
        if (userInput.trim().toLowerCase().equals("yes") && this.lastCorrectedCommand != null) {
            CommandResult output = this.controlUnit.execute(this.lastCorrectedCommand);
            this.lastCorrectedCommand = null;
            this.lastCommandResult = null;
            return output;
        }

        String userCommand = extractCommand(userInput);
        String userArgument = extractArgument(userInput);
        String suggestion = null;
        int bestScore = Integer.MAX_VALUE;

        for (Command command: this.controlUnit.getCommandList()) {
            for (String triggerWord: command.getTriggerWords()) {
                int similarity = StringsSimilarity.compute(userCommand, command.getTriggerWord());
                if (similarity < bestScore) {
                    suggestion = triggerWord;
                    bestScore = similarity;
                }
            }
        }

        if (suggestion == null) {
            this.lastCommandResult = this.makeResponse();
        } else {
            this.lastCorrectedCommand = suggestion + " " + userArgument;
            this.lastCommandResult = this.makeResponseWithSuggestion(suggestion);
        }

        return this.lastCommandResult;
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

    private String extractArgument(String userInput) {
        String[] parts = userInput.split("\\s+", 2);

        if (parts.length == 2) {
            return parts[1];
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
