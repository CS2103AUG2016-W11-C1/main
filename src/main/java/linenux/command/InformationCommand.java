package linenux.command;

import java.nio.file.Paths;

import linenux.command.result.CommandResult;
import linenux.config.Config;

//@@author A0127694U
/**
 * Displays current working information about Linenux.
 */
public class InformationCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "information";
    private static final String DESCRIPTION = "Displays current working information about Linenux.";
    private static final String COMMAND_FORMAT = "information";

    private Config config;

    /**
     * Instantiate an {@code InformationCommand}.
     * @param config The application config.
     */
    public InformationCommand(Config config) {
        this.config = config;
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
        return makeConfigDetails();
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
     * @return A {@code String} representing a regular expression for this {@code Command}.
     */
    @Override
    public String getPattern() {
        return "(?i)^\\s*(" + getTriggerWordsPattern() + ")\\s*";
    }

    /**
     * @return A {@code CommandResult} representing the current app config.
     */
    private CommandResult makeConfigDetails() {
        StringBuilder builder = new StringBuilder();

        builder.append("Version: ");
        builder.append(config.getVersionNo() + "\n");
        builder.append("\n");

        builder.append("Current Working Directory: \n");
        builder.append(Paths.get("").toAbsolutePath().toString() + "\n");
        builder.append("\n");

        builder.append("Current Schedule Location: \n");
        builder.append(config.getScheduleFilePath() + "\n");
        builder.append("\n");

        return () -> builder.toString().trim();
    }
}

