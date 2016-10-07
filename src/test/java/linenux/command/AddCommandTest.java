package linenux.command;

import linenux.model.Task;
import linenux.model.Schedule;
import linenux.command.result.CommandResult;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static linenux.helpers.Assert.assertChangeBy;
import static linenux.helpers.Assert.assertNoChange;
import static org.junit.Assert.*;

/**
 * Created by yihangho on 10/4/16.
 */
public class AddCommandTest {
    private Schedule schedule;
    private AddCommand addCommand;

    @Before
    public void setupAddCommand() {
        this.schedule = new Schedule();
        this.addCommand = new AddCommand(this.schedule);
    }

    /**
     * Test that respondTo detects various versions of the commands. It should return true even if
     * the format of the arguments are invalid.
     */
    @Test
    public void testRespondToAddTaskCommand() {
        assertTrue(this.addCommand.respondTo("add"));
        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial"));
        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial st/2016-01-01"));
        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial et/2016-01-01"));
        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial st/2016-01-01 et/2016-01-01"));
    }

    /**
     * Test that respondTo is case-insensitive.
     */
    @Test
    public void testCaseInsensitiveAddTaskCommand() {
        assertTrue(this.addCommand.respondTo("AdD CS2103T Tutorial"));
    }

    /**
     * Test that respondTo will return false for commands not related to add tasks.
     */
    @Test
    public void testNotRespondToOtherCommands() {
        assertFalse(this.addCommand.respondTo("halp"));
    }

    /**
     * Test that executing an add task command will correctly add new task to the schedule.
     */
    @Test
    public void testExecuteAddTask() {
        assertChangeBy(() -> this.schedule.getTaskList().size(),
                1,
                () -> this.addCommand.execute("add CS2103T Tutorial"));

        // The new task has correct name
        ArrayList<Task> tasks = this.schedule.getTaskList();
        Task addedTask = tasks.get(tasks.size() - 1);
        assertEquals("CS2103T Tutorial", addedTask.getTaskName());

        // The new task should not have start time
        assertNull(addedTask.getStartTime());

        // The new task should not have end time
        assertNull(addedTask.getEndTime());
    }

    /**
     * Test that executing an add task command should return the correct result
     */
    @Test
    public void testExecuteAddCommandResult() {
        CommandResult result = this.addCommand.execute("add CS2103T Tutorial");
        assertEquals("Added CS2103T Tutorial", result.getFeedback());
    }

    /**
     * Test the result when running without a task name
     */
    @Test
    public void testMissingTaskNameCommandResult() {
        CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
                () -> this.addCommand.execute("add"));
        assertEquals("Invalid arguments.\n\n" + AddCommand.COMMAND_FORMAT, result.getFeedback());
    }

    /**
     * Test the result when the task name consists of only empty spaces
     */
    @Test
    public void testTaskNameIsEmptyCommandResult() {
        CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
                () -> this.addCommand.execute("add             "));
        assertEquals("Invalid arguments.\n\n" + AddCommand.COMMAND_FORMAT, result.getFeedback());
    }
}
