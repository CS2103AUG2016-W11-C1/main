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
    public void testRelativePathFileExistAndReadable() throws Exception {
        String target = this.tempDir.resolve("hello.xml").toString();
        new FileOutputStream(target).close();

        CommandResult result = this.loadCommand.execute("load hello.xml");
        String expectedFeedback = "Loaded from " + target;
        assertEquals(expectedFeedback, result.getFeedback());
        assertEquals(target, this.config.getScheduleFilePath());
    }

    @Test
    public void testAbsolutePathFileExistAndReadable() throws Exception {
        String target = this.tempDir.resolve("hello.xml").toString();
        new FileOutputStream(target).close();

        CommandResult result = this.loadCommand.execute("load " + target);
        String expectedFeedback = "Loaded from " + target;
        assertEquals(expectedFeedback, result.getFeedback());
        assertEquals(target, this.config.getScheduleFilePath());
    }

    @Test
    public void testFileDoesNotExist() {
        String target = this.tempDir.resolve("404.xml").toString();

        CommandResult result = this.loadCommand.execute("load 404.xml");
        String expectedFeedback = target + " does not exist.";
        assertEquals(expectedFeedback, result.getFeedback());
        assertEquals("existingPath", this.config.getScheduleFilePath());
    }

    @Test
    public void testFileExistsButNotReadable() throws Exception {
        String target = this.tempDir.resolve("hello.xml").toString();
        File f = new File(target);
        new FileOutputStream(f).close();
        f.setReadable(false);

        CommandResult result = this.loadCommand.execute("load hello.xml");
        String expectedFeedback = target + " is not readable.";
        assertEquals(expectedFeedback, result.getFeedback());
        assertEquals("existingPath", this.config.getScheduleFilePath());
    }

    @Test
    public void testFileIsNotAFile() {
        String target = this.tempDir.resolve("hello.xml").toString();
        File f = new File(target);
        f.mkdirs();

        CommandResult result = this.loadCommand.execute("load hello.xml");
        String expectedFeedback = target + " is not a file.";
        assertEquals(expectedFeedback, result.getFeedback());
        assertEquals("existingPath", this.config.getScheduleFilePath());
    }

    @Test
    public void testInvalidCommand() {
        CommandResult result = this.loadCommand.execute("load");
        String expectedFeedback = "Invalid arguments.\n\n" +
                "load PATH\n\n" +
                "* Non-compulsory fields are in square brackets.\n" +
                "* Arguments are case insensitive.";
        assertEquals(expectedFeedback, result.getFeedback());
    }
}
