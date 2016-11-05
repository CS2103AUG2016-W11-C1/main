package linenux.command;

import linenux.command.result.CommandResult;
import linenux.control.ControlUnit;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

//@@author A0144915A
public class SaveCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "save";
    private static final String DESCRIPTION = "Save the schedule into the specified file.";
    private static final String COMMAND_FORMAT = "save PATH";

    private ControlUnit controlUnit;
    private Path basePath;
    private boolean requiresUserResponse = false;
    private String pendingSavePath = null;

    public SaveCommand(ControlUnit controlUnit) {
        this(controlUnit, Paths.get("").toAbsolutePath());
    }

    public SaveCommand(ControlUnit controlUnit, Path basePath) {
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

    @Override
    public boolean awaitingUserResponse() {
        return this.requiresUserResponse;
    }

    @Override
    public CommandResult getUserResponse(String userInput) {
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

    private CommandResult validateAndUpdate(File file) {
        if (file.canWrite()) {
            this.controlUnit.setScheduleFilePath(file.toString());
            this.controlUnit.saveSchedule();
            return makeResult(file.toString());
        } else {
            return makeNotWritableResult(file.toString());
        }
    }

    private CommandResult makeResult(String path) {
        return () -> "Saved to " + path;
    }

    private CommandResult makeNotWritableResult(String path) {
        return () -> "Cannot save to " + path;
    }

    private CommandResult makeFileExistsResult(String path) {
        return () -> path + " already exists.\n" +
                "Do you want to overwrite it? (yes/no)";
    }

    private CommandResult makeCancelResult() {
        return () -> "OK! Not overwriting " + this.pendingSavePath;
    }

    private CommandResult makeInvalidArgumentResult() {
        return () -> "Invalid arguments.\n\n" + COMMAND_FORMAT + "\n\n" + CALLOUTS;
    }

    private CommandResult makeInvalidUserResponse() {
        return () -> "I don't understand that. Do you want to overwrite " + this.pendingSavePath + "? (yes/no)";
    }
}
