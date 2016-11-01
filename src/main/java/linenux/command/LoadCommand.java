package linenux.command;

import linenux.command.result.CommandResult;
import linenux.control.ControlUnit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@@author A0144915A
public class LoadCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "load";
    private static final String DESCRIPTION = "Load schedule from the specified path.";
    private static final String COMMAND_FORMAT = "load PATH";

    private ControlUnit controlUnit;

    public LoadCommand(ControlUnit controlUnit) {
        this.controlUnit = controlUnit;
    }

    @Override
    public CommandResult execute(String userInput) {
        String path = this.extractArgument(userInput);

        if (path.isEmpty()) {
            return this.makeInvalidArgumentResult();
        }

        this.controlUnit.setScheduleFilePath(path);
        this.controlUnit.loadSchedule();

        return this.makeResult(path);
    }

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

    private String extractArgument(String userInput) {
        Matcher matcher = Pattern.compile(getPattern()).matcher(userInput);

        if (matcher.matches() && matcher.group("keywords") != null) {
            return matcher.group("keywords").trim();
        } else {
            return "";
        }
    }

    private CommandResult makeResult(String path) {
        return () -> "Loaded from " + path;
    }

    private CommandResult makeInvalidArgumentResult() {
        return () -> "Invalid arguments.\n\n" + COMMAND_FORMAT + "\n\n" + CALLOUTS;
    }
}
