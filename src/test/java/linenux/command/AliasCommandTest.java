package linenux.command;

import static linenux.helpers.Assert.assertChangeBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import linenux.util.ArrayListUtil;
import org.junit.Before;
import org.junit.Test;

import linenux.command.result.CommandResult;
import linenux.model.Schedule;

/**
 * JUnit test for alias command.
 */
public class AliasCommandTest {
    private AliasCommand aliasCommand;
    private AddCommand addCommand;
    private DeleteCommand deleteCommand;
    private Schedule schedule;

    //@@author A0144915A
    @Before
    public void setupAliasCommand() {
        this.schedule = new Schedule();
        this.addCommand = new AddCommand(this.schedule);
        this.deleteCommand = new DeleteCommand(this.schedule);
        this.aliasCommand = new AliasCommand(ArrayListUtil.fromArray(new Command[] {this.addCommand, this.deleteCommand}));
    }

    //@@author A0135788M
    /**
     * Test that respondTo detects various versions of the commands. It should return true even if
     * the format of the arguments are invalid.
     */
    @Test
    public void testRespondToAliasTaskCommand() {
        assertTrue(this.aliasCommand.respondTo("alias"));
        assertTrue(this.aliasCommand.respondTo("alias add"));
        assertTrue(this.aliasCommand.respondTo("alias add ad"));
    }

    /**
     * Test that respondTo is case-insensitive.
     */
    @Test
    public void testCaseInsensitiveAddTaskCommand() {
        assertTrue(this.aliasCommand.respondTo("AliAs add ad"));
    }

    /**
     * Test that respondTo will return false for commands not related to add tasks.
     */
    @Test
    public void testNotRespondToOtherCommands() {
        assertFalse(this.aliasCommand.respondTo("halp"));
    }

    /**
     * Test invalid arguments.
     */
    @Test
    public void testInvalidArguments() {
        CommandResult result = this.aliasCommand.execute("alias add");
        assertEquals("Invalid arguments.\n\n" + this.aliasCommand.getCommandFormat() + "\n\n" + Command.CALLOUTS, result.getFeedback());
    }

    /**
     * Test no such command to alias.
     */
    @Test
    public void testNoSuchCommandToAlias() {
        CommandResult result = this.aliasCommand.execute("alias hi h");
        assertEquals("No such command to make alias for.", result.getFeedback());
    }

    /**
     * Test alias command must be alphanumeric.
     */
    @Test
    public void testAliasCommandIsAlphanumeric() {
        CommandResult result = this.aliasCommand.execute("alias add add1234");
        assertEquals("add1234 is now the alias for the add command.", result.getFeedback());

        result = this.aliasCommand.execute("alias add add!");
        assertEquals("Alias must be alphanumeric.", result.getFeedback());
    }

    //@@author A0144915A
    /**
     * Test alias creates an alias.
     */
    @Test
    public void testAliasFunctionality() {
        this.aliasCommand.execute("alias add addi");
        assertChangeBy(() -> this.schedule.getTaskList().size(), 1,
                () -> this.addCommand.execute("addi CS2103T Tutorial #/tag1 tag2"));
    }

    /**
     * Ensure that aliases can only be used once.
     */
    @Test
    public void testReusedAlias() {
        this.aliasCommand.execute("alias add addi");
        assertTrue(this.addCommand.respondTo("addi"));
        CommandResult result = this.aliasCommand.execute("alias add addi");
        String expectedFeedback = "\"addi\" is used for another command.";
        assertEquals(expectedFeedback, result.getFeedback());
    }

    @Test
    public void testSecondCommandInList() {
        this.aliasCommand.execute("alias delete d");
        assertTrue(this.deleteCommand.respondTo("d"));
    }
}
