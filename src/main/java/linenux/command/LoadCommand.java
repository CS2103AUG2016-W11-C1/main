package linenux.command;

import linenux.command.result.CommandResult;
import linenux.control.ControlUnit;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@@author A0144915A
public class LoadCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "load";
    private static final String DESCRIPTION = "Load schedule from the specified path.";
    private static final String COMMAND_FORMAT = "load PATH";

    private ControlUnit controlUnit;
    private Path basePath;

    public LoadCommand(ControlUnit controlUnit) {
        this(controlUnit, Paths.get("").toAbsolutePath());
    }

    public LoadCommand(ControlUnit controlUnit, Path basePath) {
        this.controlUnit = controlUnit;
        this.basePath = basePath;
        this.TRIGGER_WORDS.add(TRIGGER_WORD);
    }

    @Override
    public CommandResult execute(String userInput) {
        String relativePath = this.extractArgument(userInput);

        if (relativePath.isEmpty()) {
            return this.makeInvalidArgumentResult();
        }

        String path = this.basePath.resolve(relativePath).toString();
        File f = new File(path);

        if (!f.exists()) {
            return this.makeFileNotFoundResult(path);
        } else if (!f.isFile()) {
            return this.makeNotAFileResult(path);
        } else if (!f.canRead()) {
            return this.makeFileNotReadableResult(path);
        } else {
            this.controlUnit.setScheduleFilePath(path);
            this.controlUnit.loadSchedule();
        }

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

    private CommandResult makeResult(String path) {
        return () -> "Loaded from " + path;
    }

    private CommandResult makeFileNotFoundResult(String path) {
        return () -> path + " does not exist.";
    }

    private CommandResult makeFileNotReadableResult(String path) {
        return () -> path + " is not readable.";
    }

    private CommandResult makeNotAFileResult(String path) {
        return () -> path + " is not a file.";
    }

    private CommandResult makeInvalidArgumentResult() {
        return () -> "Invalid arguments.\n\n" + COMMAND_FORMAT + "\n\n" + CALLOUTS;
    }
}
