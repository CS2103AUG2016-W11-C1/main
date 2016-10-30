package linenux.command;

import linenux.command.result.CommandResult;
import linenux.control.ControlUnit;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

//@@author A0144915A
public class LoadCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "load";
    private static final String DESCRIPTION = "Load schedule from the specified path.";
    private static final String COMMAND_FORMAT = "load PATH";

    private ControlUnit controlUnit;
    private Path basePath;

    /**
     * Instantiate a {@code LoadCommand} using a {@code ControlUnit}.
     * @param controlUnit The application {@code ControlUnit}.
     */
    public LoadCommand(ControlUnit controlUnit) {
        this(controlUnit, Paths.get("").toAbsolutePath());
    }

    /**
     * Instantiate a {@code LoadCommand} using a {@code ControlUnit} and the current directory.
     * @param controlUnit The application {@code ControlUnit}.
     * @param basePath The current working directory.
     */
    public LoadCommand(ControlUnit controlUnit, Path basePath) {
        this.controlUnit = controlUnit;
        this.basePath = basePath;
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
     * @param path The loaded path.
     * @return A {@code CommandResult} indicating that {@code Schedule} is loaded.
     */
    private CommandResult makeResult(String path) {
        return () -> "Loaded from " + path;
    }

    /**
     * @param path The path that the user wants to load.
     * @return A {@code CommandResult} indicating the {@code path} cannot be found.
     */
    private CommandResult makeFileNotFoundResult(String path) {
        return () -> path + " does not exist.";
    }

    /**
     * @param path The path that the user wants to load.
     * @return A {@code CommandResult} indicating the {@code path} cannot be read.
     */
    private CommandResult makeFileNotReadableResult(String path) {
        return () -> path + " is not readable.";
    }

    /**
     * @param path The path that the user wants to load.
     * @return A {@code CommandResult} indicating the {@code path} is not a file.
     */
    private CommandResult makeNotAFileResult(String path) {
        return () -> path + " is not a file.";
    }

    /**
     * @return A {@code CommandResult} indicating that the argument is invalid.
     */
    private CommandResult makeInvalidArgumentResult() {
        return () -> "Invalid arguments.\n\n" + COMMAND_FORMAT + "\n\n" + CALLOUTS;
    }
}
