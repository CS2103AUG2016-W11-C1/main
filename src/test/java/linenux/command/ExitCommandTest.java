package linenux.command;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

//@@author A0140702X
/**
 * JUnit test for exit command.
 */
public class ExitCommandTest {
    private ExitCommand exitCommand;

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Before
    public void setupExitCommmand() {
        this.exitCommand = new ExitCommand();
    }

    /**
     * Test that respondTo detects various versions of the commands.
     */
    @Test
    public void respondTo_inputThatStartsWithExit_trueReturned() {
        assertTrue(this.exitCommand.respondTo("exit"));
        assertTrue(this.exitCommand.respondTo("exit    "));
        assertTrue(this.exitCommand.respondTo("exit now"));
    }

    /**
     * Test that respondTo is case-insensitive.
     */
    @Test
    public void respondTo_upperCase_trueReturned() {
        assertTrue(this.exitCommand.respondTo("ExIt"));
    }

    /**
     * Test that respondTo will return false for commands not related to exit.
     */
    @Test
    public void respondTo_otherCommands_falseReturned() {
        assertFalse(this.exitCommand.respondTo("add"));
    }

    /**
     * Test that executing an exit command will quit the applications.
     */
    @Test
    public void execute() {
        exit.expectSystemExitWithStatus(0);
        this.exitCommand.execute("exit");
    }
}
