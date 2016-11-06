package linenux.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Before;
import org.junit.Test;

import linenux.command.result.CommandResult;

//@@author A0144915A
public class SaveCommandTest extends FileCommandsTest {
    private SaveCommand saveCommand;

    @Before
    @Override
    public void setupTestEnvironment() throws Exception {
        super.setupTestEnvironment();
        this.saveCommand = new SaveCommand(this.controlUnit, this.tempDir);
    }

    @Test
    public void execute_writableAbsolutePath_scheduleSaved() throws Exception {
        Path anotherTempDir = Files.createTempDirectory("tmp").toAbsolutePath();
        Path targetPath = anotherTempDir.resolve("schedule.xml");
        String target = targetPath.toString();

        CommandResult result = this.saveCommand.execute("save " + target);
        String expectedResult = "Saved to " + target;
        assertEquals(expectedResult, result.getFeedback());
        assertEquals(target, this.config.getScheduleFilePath());
    }

    @Test
    public void execute_writableRelativePath_scheduleSaved() {
        CommandResult result = this.saveCommand.execute("save hello.xml");
        String expectedPath = this.tempDir.resolve("hello.xml").toString();
        String expectedResult = "Saved to " + expectedPath;
        assertEquals(expectedResult, result.getFeedback());
        assertEquals(expectedPath, this.config.getScheduleFilePath());
    }

    @Test
    public void execute_existingFile() throws Exception {
        String target = this.tempDir.resolve("hello.xml").toString();
        new FileOutputStream(target).close();
        CommandResult result = this.saveCommand.execute("save hello.xml");
        String expectedResult = target + " already exists.\n" +
                "Do you want to overwrite it? (yes/no)";
        assertEquals(expectedResult, result.getFeedback());
        assertEquals("existingPath", this.config.getScheduleFilePath());
        assertTrue(this.saveCommand.isAwaitingUserResponse());

        result = this.saveCommand.processUserResponse("bla");
        expectedResult = "I don't understand that. Do you want to overwrite " + target + "? (yes/no)";
        assertEquals(expectedResult, result.getFeedback());
        assertEquals("existingPath", this.config.getScheduleFilePath());
        assertTrue(this.saveCommand.isAwaitingUserResponse());

        result = this.saveCommand.processUserResponse("no");
        expectedResult = "OK! Not overwriting " + target;
        assertEquals(expectedResult, result.getFeedback());
        assertEquals("existingPath", this.config.getScheduleFilePath());
        assertFalse(this.saveCommand.isAwaitingUserResponse());

        this.saveCommand.execute("save hello.xml");
        assertTrue(this.saveCommand.isAwaitingUserResponse());
        result = this.saveCommand.processUserResponse("yes");
        expectedResult = "Saved to " + target;
        assertEquals(expectedResult, result.getFeedback());
        assertEquals(target, this.config.getScheduleFilePath());
        assertFalse(this.saveCommand.isAwaitingUserResponse());
    }

    @Test
    public void execute_nonWritableFile_commandResultReturn() throws Exception {
        String target = this.tempDir.resolve("notWritable.xml").toString();
        File f = new File(target);
        new FileOutputStream(f).close();
        f.setWritable(false);

        CommandResult result = this.saveCommand.execute("save notWritable.xml");
        String expectedFeedback = target + " already exists.\n" +
                "Do you want to overwrite it? (yes/no)";
        assertEquals(expectedFeedback, result.getFeedback());
        assertEquals("existingPath", this.config.getScheduleFilePath());

        result = this.saveCommand.processUserResponse("yes");
        expectedFeedback = "Cannot save to " + target;
        assertEquals(expectedFeedback, result.getFeedback());
        assertEquals("existingPath", this.config.getScheduleFilePath());
    }

    @Test
    public void execute_parentIsNotWritable_commandResultReturned() {
        File f = new File(this.tempDir.toString());
        boolean updated = f.setWritable(false); // Windows does not support this.
        if (!updated) {
            return;
        }

        CommandResult result = this.saveCommand.execute("save notWritable.xml");
        String expectedPath = this.tempDir.resolve("notWritable.xml").toString();
        String expectedFeedback = "Cannot save to " + expectedPath;
        assertEquals(expectedFeedback, result.getFeedback());
        assertEquals("existingPath", this.config.getScheduleFilePath());
    }

    @Test
    public void execute_parentDirectoryNonExistent_missingDirectoryCreated() {
        CommandResult result = this.saveCommand.execute("save subdir/schedule.xml");

        Path parent = this.tempDir.resolve("subdir");
        File parentFile = new File(parent.toString());
        assertTrue(parentFile.exists());
        assertTrue(parentFile.isDirectory());

        Path target = parent.resolve("schedule.xml");
        File targetFile = new File(target.toString());
        assertTrue(targetFile.exists());
        assertTrue(targetFile.isFile());

        String expectedPath = target.toString();
        String expectedFeedback = "Saved to " + expectedPath;
        assertEquals(expectedFeedback, result.getFeedback());
        assertEquals(expectedPath, this.config.getScheduleFilePath());
    }

    @Test
    public void execute_invalidArgument_commandResultReturned() {
        CommandResult result = this.saveCommand.execute("save");
        String expectedFeedback = "Invalid arguments.\n\n" +
                "save NEW_PATH\n\n" +
                "* Non-compulsory fields are in square brackets.\n" +
                "* Arguments are case insensitive.";
        assertEquals(expectedFeedback, result.getFeedback());
    }

}
