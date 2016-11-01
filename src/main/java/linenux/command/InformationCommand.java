package linenux.command;

import java.nio.file.Paths;

import linenux.command.result.CommandResult;
import linenux.config.Config;

/**
 * Displays current working information about Linenux.
 */
// @@author A0127694U
public class InformationCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "information";
    private static final String DESCRIPTION = "Displays current working information about Linenux.";
    private static final String COMMAND_FORMAT = "information";

    private Config config;

    public InformationCommand(Config config) {
        this.config = config;
    }

    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(getPattern());
        return makeConfigDetails();
    }

    // @@author A0135788M
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

    private CommandResult makeConfigDetails() {
        StringBuilder builder = new StringBuilder();

        builder.append("Version: ");
        builder.append(config.getVersionNo() + "\n");
        builder.append("\n");

        builder.append("Schedule Location: \n");
        builder.append(config.getScheduleFilePath() + "\n");
        builder.append("\n");

        builder.append("Current Working Directiory: \n");
        builder.append(Paths.get("").toAbsolutePath().toString());

        return () -> builder.toString().trim();
    }
}

