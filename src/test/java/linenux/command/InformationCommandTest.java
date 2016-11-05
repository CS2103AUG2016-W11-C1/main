package linenux.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import linenux.command.result.CommandResult;
import linenux.config.JsonConfig;

/**
 * JUnit test for Information Command.
 */
// @@author A0127694U
public class InformationCommandTest {
    public static final String VERSION_NO = "v0.0";
    public static final String DEFAULT_FILE_PATH = Paths.get(".").toAbsolutePath().toString();
    public static final String CONFIG_FILENAME = "ConfigTest.json";
    public static final String SCHEDULE_FILENAME = "ScheduleTest.xml";

    private InformationCommand informationCommand;
    private JsonConfig config;

    @Before
    public void setupInformationCommand() {
        this.config = new JsonConfig(VERSION_NO, DEFAULT_FILE_PATH + CONFIG_FILENAME,
                DEFAULT_FILE_PATH + SCHEDULE_FILENAME);
        this.informationCommand = new InformationCommand(this.config);
    }

    /**
     * Test that respondTo detects various versions of the commands. It should
     * return true even if the format of the arguments are invalid.
     */
    @Test
    public void testRespondToInformationCommand() {
        assertTrue(this.informationCommand.respondTo("information"));
    }

    /**
     * Test that respondTo is case-insensitive.
     */
    @Test
    public void testCaseInsensitiveInformationCommand() {
        assertTrue(this.informationCommand.respondTo("inFORMatIon"));
    }

    /**
     * Test that respondTo will return false for commands not related to add
     * tasks.
     */
    @Test
    public void testNotRespondToOtherCommands() {
        assertFalse(this.informationCommand.respondTo("halp"));
    }

    /**
     * Test information command.
     */
    @Test
    public void testInformationCommand() {
        CommandResult result = this.informationCommand.execute("information");
        String output = "Version: " + VERSION_NO + "\n\nCurrent Working Directory: \n"
                + Paths.get("").toAbsolutePath().toString() + "\n\nCurrent Schedule Location: \n" + DEFAULT_FILE_PATH
                + SCHEDULE_FILENAME + "\n\nSaved Schedule Locations: \n" + DEFAULT_FILE_PATH
                + SCHEDULE_FILENAME;
        assertEquals(output, result.getFeedback());
    }

    @After
    public void removeConfigFile() {
        File configFile = new File(DEFAULT_FILE_PATH + CONFIG_FILENAME);
        configFile.delete();
    }
}
