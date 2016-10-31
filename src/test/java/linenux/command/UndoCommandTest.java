package linenux.command;

import static linenux.helpers.Assert.assertChangeBy;
import static linenux.helpers.Assert.assertNoChange;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import linenux.model.Schedule;
import linenux.model.Task;

//@@author A0135788M
/**
 * JUnit test for undo command.
 */
public class UndoCommandTest {
    private Schedule schedule;
    private UndoCommand undoCommand;

    @Before
    public void setUpUndoCommand() {
        this.schedule = new Schedule();
        this.undoCommand = new UndoCommand(this.schedule);
    }

    /**
     * Should only return true if the user input is "undo" without any other spaces or characters.
     */
    @Test
    public void testRespondToUndoCommand() {
        assertTrue(undoCommand.respondTo("undo"));
        assertTrue(undoCommand.respondTo("undo "));
        assertTrue(undoCommand.respondTo(" undo"));
    }

    /**
     * Test that respondTo is case-insensitive.
     */
    @Test
    public void testCaseInsensitiveUndoCommand() {
        assertTrue(undoCommand.respondTo("UnDo"));
    }

    /**
     * Test that respondTo will return false for commands not related to add tasks.
     */
    @Test
    public void testNotRespondToOtherCommands() {
        assertFalse(this.undoCommand.respondTo("halp"));
    }

    /**
     * Test that undo successfully removes a state if number of states > 1.
     */
    @Test
    public void testExecuteUndo() {
        this.schedule.addTask(new Task("task1"));
        assertChangeBy(() -> this.schedule.getStates().size(),
                -1,
                () -> this.undoCommand.execute("undo"));
    }

    /**
     * Test that undo does not remove the last state if number of states = 1.
     */
    @Test
    public void testExecuteUndoWhenStateIsOne() {
        assertNoChange(() -> this.schedule.getStates().size(),
                () -> this.undoCommand.execute("undo"));
    }

    /**
     * Test successful undo message.
     */
    @Test
    public void testSuccessfulUndo() {
        this.schedule.addTask(new Task("task1"));
        assertEquals("Successfully undo last command.", this.undoCommand.execute("undo").getFeedback());
    }

    /**
     * Test no more states to undo message.
     */
    @Test
    public void testUnsuccessfulUndo() {
        assertEquals("No more commands to undo!", this.undoCommand.execute("undo").getFeedback());
    }
}
