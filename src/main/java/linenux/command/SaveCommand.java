package linenux.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.result.CommandResult;
import linenux.control.ControlUnit;

//@@author A0144915A
public class SaveCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "save";
    private static final String DESCRIPTION = "Save the schedule into the specified file.";
    private static final String COMMAND_FORMAT = "save PATH";

    private ControlUnit controlUnit;

    public SaveCommand(ControlUnit controlUnit) {
        this.controlUnit = controlUnit;
    }

    @Override
    public CommandResult execute(String userInput) {
        String path = this.extractArgument(userInput);

        if (path.isEmpty()) {
            return this.makeInvalidArgumentResult();
        }

        this.controlUnit.saveSchedule();
        this.controlUnit.setScheduleFilePath(path);

        return makeResult(path);
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
        return () -> "Saved to " + path;
    }

    private CommandResult makeInvalidArgumentResult() {
        return () -> "Invalid arguments.\n\n" + COMMAND_FORMAT + "\n\n" + CALLOUTS;
    }
}
