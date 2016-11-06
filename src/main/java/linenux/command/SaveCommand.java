package linenux.command;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import linenux.command.result.CommandResult;
import linenux.control.ControlUnit;

//@@author A0144915A
public class SaveCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "save";
    private static final String DESCRIPTION = "Save the schedule into the specified file.";
    private static final String COMMAND_FORMAT = "save NEW_PATH";

    private ControlUnit controlUnit;
    private Path basePath;
    private boolean requiresUserResponse = false;
    private String pendingSavePath = null;

    /**
     * Instantiate a {@code SaveCommand} using a {@code ControlUnit}.
     * @param controlUnit The application {@code ControlUnit}.
     */
    public SaveCommand(ControlUnit controlUnit) {
        this(controlUnit, Paths.get("").toAbsolutePath());
    }

    /**
     * Instantiate a {@code SaveCommand} using a {@code ControlUnit} and the current directory.
     * @param controlUnit The application {@code ControlUnit}.
     * @param basePath The current working directory.
     */
    public SaveCommand(ControlUnit controlUnit, Path basePath) {
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

        Path targetPath = this.basePath.resolve(relativePath);
        Path parentPath = targetPath.getParent();
        String path = this.basePath.resolve(relativePath).toString();
        File file = new File(path);

        try {
            new File(parentPath.toString()).mkdirs();

            if (file.exists()) {
                this.requiresUserResponse = true;
                this.pendingSavePath = path;
                return this.makeFileExistsResult(path);
            } else {
                new FileOutputStream(file).close();
            }
        } catch (Exception e) {
        }

        return this.validateAndUpdate(file);
    }

    /**
     * @return {@code true} if and only if this {@code Command} is awaiting for user response.
     */
    @Override
    public boolean isAwaitingUserResponse() {
        return this.requiresUserResponse;
    }

    /**
     * Process the response given by the user.
     * @param userInput {@code String} representing the user response.
     * @return A {@code CommandResult}, which is the result of processing {@code userInput}.
     */
    @Override
    public CommandResult processUserResponse(String userInput) {
        if (userInput.trim().toLowerCase().equals("yes")) {
            this.requiresUserResponse = false;
            File file = new File(this.pendingSavePath);
            return this.validateAndUpdate(file);
        } else if (userInput.trim().toLowerCase().equals("no")) {
            this.requiresUserResponse = false;
            return this.makeCancelResult();
        } else {
            return this.makeInvalidUserResponse();
        }
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
     * Ensure that {@code file} can be written. If so, asks the control unit to write to {@code file}.
     * @param file The {@code File} to write to.
     * @return A {@code CommandResult} indicating the result.
     */
    private CommandResult validateAndUpdate(File file) {
        if (file.canWrite()) {
            this.controlUnit.setScheduleFilePath(file.toString());
            this.controlUnit.saveSchedule();
            return makeResult(file.toString());
        } else {
            return makeNotWritableResult(file.toString());
        }
    }

    /**
     * @param path The saved path.
     * @return A {@code CommandResult} indicating that {@code Schedule} is saved.
     */
    private CommandResult makeResult(String path) {
        return () -> "Saved to " + path;
    }

    /**
     * @param path The path that the user wants to save to.
     * @return A {@code CommandResult} indicating that {@code path} is not writable.
     */
    private CommandResult makeNotWritableResult(String path) {
        return () -> "Cannot save to " + path;
    }

    /**
     * @param path The path that the user wants to save to.
     * @return A {@code CommandResult} indicating that {@code path} is an existing file.
     */
    private CommandResult makeFileExistsResult(String path) {
        return () -> path + " already exists.\n" +
                "Do you want to overwrite it? (yes/no)";
    }

    /**
     * @return A {@code CommandResult} indicating that the save operation is cancelled.
     */
    private CommandResult makeCancelResult() {
        return () -> "OK! Not overwriting " + this.pendingSavePath;
    }

    /**
     * @return A {@code CommandResult} indicating that the argument is invalid.
     */
    private CommandResult makeInvalidArgumentResult() {
        return () -> "Invalid arguments.\n\n" + COMMAND_FORMAT + "\n\n" + CALLOUTS;
    }

    /**
     * @return A {@code CommandResult} indicating that the user response is invalid.
     */
    private CommandResult makeInvalidUserResponse() {
        return () -> "I don't understand that. Do you want to overwrite " + this.pendingSavePath + "? (yes/no)";
    }
}
