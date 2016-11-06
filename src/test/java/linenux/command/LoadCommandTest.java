package linenux.command;

import linenux.command.result.CommandResult;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;

import static org.junit.Assert.assertEquals;

//@@author A0144915A
public class LoadCommandTest extends FileCommandsTest {
    private LoadCommand loadCommand;

    @Before
    @Override
    public void setupTestEnvironment() throws Exception {
        super.setupTestEnvironment();
        this.loadCommand = new LoadCommand(this.controlUnit, this.tempDir);
    }
    @Test
    public void execute_existentAndReadableRelativePath_scheduleLoaded() throws Exception {
        String target = this.tempDir.resolve("hello.xml").toString();
        new FileOutputStream(target).close();

        CommandResult result = this.loadCommand.execute("load hello.xml");
        String expectedFeedback = "Loaded from " + target;
        assertEquals(expectedFeedback, result.getFeedback());
        assertEquals(target, this.config.getScheduleFilePath());
    }

    @Test
    public void execute_existentAndReadableAbsolutePath_scheduleLoaded() throws Exception {
        String target = this.tempDir.resolve("hello.xml").toString();
        new FileOutputStream(target).close();

        CommandResult result = this.loadCommand.execute("load " + target);
        String expectedFeedback = "Loaded from " + target;
        assertEquals(expectedFeedback, result.getFeedback());
        assertEquals(target, this.config.getScheduleFilePath());
    }

    @Test
    public void execute_pathDoesNotExist_commandResultReturned() {
        String target = this.tempDir.resolve("404.xml").toString();

        CommandResult result = this.loadCommand.execute("load 404.xml");
        String expectedFeedback = target + " does not exist.";
        assertEquals(expectedFeedback, result.getFeedback());
        assertEquals("existingPath", this.config.getScheduleFilePath());
    }

    @Test
    public void execute_fileExistsButNotReadable_commandResultReturned() throws Exception {
        String target = this.tempDir.resolve("hello.xml").toString();
        File f = new File(target);
        new FileOutputStream(f).close();
        boolean updated = f.setReadable(false);
        if (!updated) { // Windows does not support this.
            return;
        }

        CommandResult result = this.loadCommand.execute("load hello.xml");
        String expectedFeedback = target + " is not readable.";
        assertEquals(expectedFeedback, result.getFeedback());
        assertEquals("existingPath", this.config.getScheduleFilePath());
    }

    @Test
    public void execute_pathIsNotAFile_commandResultReturned() {
        String target = this.tempDir.resolve("hello.xml").toString();
        File f = new File(target);
        f.mkdirs();

        CommandResult result = this.loadCommand.execute("load hello.xml");
        String expectedFeedback = target + " is not a file.";
        assertEquals(expectedFeedback, result.getFeedback());
        assertEquals("existingPath", this.config.getScheduleFilePath());
    }

    @Test
    public void execute_invalidArgument_commandResultReturned() {
        CommandResult result = this.loadCommand.execute("load");
        String expectedFeedback = "Invalid arguments.\n\n" +
                "load PATH\n\n" +
                "* Non-compulsory fields are in square brackets.\n" +
                "* Arguments are case insensitive.";
        assertEquals(expectedFeedback, result.getFeedback());
    }
}
