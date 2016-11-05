package linenux.command;

import linenux.command.result.CommandResult;
import linenux.control.ControlUnit;
import linenux.util.StringsSimilarity;

//@@author A0144915A
/**
 * Act as a fail-safe for invalid or unrecognized commands.
 */
public class InvalidCommand extends AbstractCommand {
    private ControlUnit controlUnit;

    private CommandResult lastCommandResult = null;
    private String lastCorrectedCommand = null;

    public InvalidCommand(ControlUnit controlUnit) {
        this.controlUnit = controlUnit;
        this.controlUnit.addPostExecuteListener((userInput, commandResult) -> {
            if (commandResult != lastCommandResult) {
                lastCorrectedCommand = null;
            }
        });
    }

    /**
     * @return true for all user inputs.
     */
    @Override
    public boolean respondTo(String userInput) {
        return true;
    }

    /**
     * Executes the command based on {@code userInput}. This method operates under the assumption that
     * {@code respondTo(userInput)} is {@code true}.
     * @param userInput A {@code String} representing the user input.
     * @return A {@code CommandResult} representing the result of the command.
     */
    @Override
    public CommandResult execute(String userInput) {
        if (userInput.trim().toLowerCase().equals("yes") && this.lastCorrectedCommand != null) {
            CommandResult output = this.controlUnit.execute(this.lastCorrectedCommand);
            this.lastCorrectedCommand = null;
            this.lastCommandResult = null;
            return output;
        }

        String userCommand = extractCommand(userInput);
        String userArgument = parseArgument(userInput);
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

    /**
     * @return A {@code String} representing the default command word.
     */
    @Override
    public String getTriggerWord() {
        return null;
    }

    /**
     * @return A {@code String} describing what this {@code Command} does.
     */
    @Override
    public String getDescription() {
        return null;
    }

    /**
     * @return A {@code String} describing the format that this {@code Command} expects.
     */
    @Override
    public String getCommandFormat() {
        return null;
    }

    @Override
    public String getPattern() {
        return null;
    }

    /**
     * Extract the command trigger word from {@code userInput}.
     * @param userInput A {@code String} representing the user input.
     * @return A {@code String}, which is the command trigger word.
     */
    private String extractCommand(String userInput) {
        String[] parts = userInput.split(" ");

        if (parts.length > 0) {
            return parts[0];
        } else {
            return "";
        }
    }

    private String parseArgument(String userInput) {
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
