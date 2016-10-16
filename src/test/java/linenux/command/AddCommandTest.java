package linenux.command;

import static linenux.helpers.Assert.assertChangeBy;
import static linenux.helpers.Assert.assertNoChange;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import linenux.command.parser.TaskArgumentParser;
import linenux.command.result.CommandResult;
import linenux.model.Schedule;
import linenux.model.Task;

/**
 * JUnit test for add command.
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
        assertTrue(this.addCommand.respondTo("add #/category"));
        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial"));
        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial #/category"));

        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial st/2016-01-01"));
        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial st/2016-01-01 #/category"));
        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial et/2016-01-01"));
        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial et/2016-01-01 #/category"));

        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial st/2016-01-01 et/2016-01-01"));
        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial st/2016-01-01 et/2016-01-01 #/category"));
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
     * Test that executing the add task command will correctly add new todo to the schedule.
     */
    @Test
    public void testExecuteAddTodo() {
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
     * Test that executing the add task command will correctly add new deadline to the schedule.
     */
    @Test
    public void testExecuteAddDeadline() {
        assertChangeBy(() -> this.schedule.getTaskList().size(),
                1,
                () -> this.addCommand.execute("add CS2103T Tutorial et/2016-01-01 5:00PM"));

        // The new deadline has the correct name, start time, and end time
        ArrayList<Task> tasks = this.schedule.getTaskList();
        Task addedTask = tasks.get(tasks.size() - 1);
        assertEquals("CS2103T Tutorial", addedTask.getTaskName());
        assertNull(addedTask.getStartTime());
        assertEquals(LocalDateTime.of(2016, 1, 1, 17, 0), addedTask.getEndTime());
    }

    /**
     * Test that executing the add task command will correctly add new event to the schedule
     */
    @Test
    public void testExecuteAddEvent() {
        assertChangeBy(() -> this.schedule.getTaskList().size(),
                1,
                () -> this.addCommand.execute("add CS2103T Tutorial st/2016-01-01 5:00PM et/2016-01-02 5:00PM"));

        // The new event has the correct name, start time, and end time
        ArrayList<Task> tasks = this.schedule.getTaskList();
        Task addedTask = tasks.get(tasks.size() - 1);
        assertEquals("CS2103T Tutorial", addedTask.getTaskName());
        assertEquals(LocalDateTime.of(2016, 1, 1, 17, 0), addedTask.getStartTime());
        assertEquals(LocalDateTime.of(2016, 1, 2, 17, 0), addedTask.getEndTime());
    }

    /**
     * Test that executing the add task command will correctly add a tagged Todo
     * to schedule
     */
    @Test
    public void testExecuteAddTaggedTask() {
        this.schedule.clear();
        assertChangeBy(() -> this.schedule.getTaskList().size(), 1,
                () -> this.addCommand.execute("add CS2103T Tutorial #/tag1 tag2"));

        ArrayList<Task> tasks = this.schedule.getTaskList();
        Task addedTask = tasks.get(0);

        assertEquals("CS2103T Tutorial", addedTask.getTaskName());
        assertEquals(2, addedTask.getTags().size());
        assertEquals("tag1", addedTask.getTags().get(0));
        assertEquals("tag2", addedTask.getTags().get(1));
    }

    @Test
    public void testExecuteAddEventIgnoringOrderOfTimes() {
        assertChangeBy(() -> this.schedule.getTaskList().size(),
                1,
                () -> this.addCommand.execute("add CS2103T Tutorial et/2016-01-02 5:00PM st/2016-01-01 5:00PM"));

        // The new event has the correct name, start time, and end time
        ArrayList<Task> tasks = this.schedule.getTaskList();
        Task addedTask = tasks.get(tasks.size() - 1);
        assertEquals("CS2103T Tutorial", addedTask.getTaskName());
        assertEquals(LocalDateTime.of(2016, 1, 1, 17, 0), addedTask.getStartTime());
        assertEquals(LocalDateTime.of(2016, 1, 2, 17, 0), addedTask.getEndTime());
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
     * Test that adding a new deadline should return the correct result
     */
    @Test
    public void testExecuteAddDeadlineResult() {
        CommandResult result = this.addCommand.execute("add CS2103T Tutorial et/2016-01-01 5:00PM");
        assertEquals("Added CS2103T Tutorial (Due 2016-01-01 5:00PM)", result.getFeedback());
    }

    /**
     * Test that adding a new event should return the correct result
     */
    @Test
    public void testExecuteAddEventResult() {
        CommandResult result = this.addCommand.execute("add CS2103T Tutorial st/2016-01-01 5:00PM et/2016-01-02 5:00PM");
        assertEquals("Added CS2103T Tutorial (2016-01-01 5:00PM - 2016-01-02 5:00PM)", result.getFeedback());
    }

    /**
     * Test that adding a new categorized Todo should return the correct result
     *
     */
    @Test
    public void testExecuteAddCategorizedTaskResult() {
        CommandResult result = this.addCommand.execute("add CS2103T Tutorial #/tag1 tag2");
        assertEquals("Added CS2103T Tutorial [Tags: \"tag1\" \"tag2\" ]", result.getFeedback());
    }

    /**
     * Test the result when running without a task name
     */
    @Test
    public void testMissingTaskNameCommandResult() {
        CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
                () -> this.addCommand.execute("add"));
        assertEquals(expectedInvalidArgumentMessage(), result.getFeedback());
    }

    /**
     * Test the result when the task name consists of only empty spaces
     */
    @Test
    public void testTaskNameIsEmptyCommandResult() {
        CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
                () -> this.addCommand.execute("add             "));
        assertEquals(expectedInvalidArgumentMessage(), result.getFeedback());
    }

    @Test
    public void testStartTimeWithoutTaskName() {
        CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
                () -> this.addCommand.execute("add st/2016-01-01 5:00PM"));
        assertEquals(expectedInvalidArgumentMessage(), result.getFeedback());
    }

    @Test
    public void testEndTimeWithoutTaskName() {
        CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
                () -> this.addCommand.execute("add et/2016-01-01 5:00PM"));
        assertEquals(expectedInvalidArgumentMessage(), result.getFeedback());
    }

    @Test
    public void testTagWithoutStartName() {
        CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
                () -> this.addCommand.execute("add #/tag1 tag2"));
        assertEquals(expectedInvalidArgumentMessage(), result.getFeedback());
    }

    @Test
    public void testInvalidStartTime() {
        CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
                () -> this.addCommand.execute("add hello st/yesterday et/2016-12-31 11:59PM"));

        assertEquals("Cannot parse \"yesterday\".", result.getFeedback());
    }

    @Test
    public void testInvalidEndTime() {
        CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
                () -> this.addCommand.execute("add hello et/tomorrow"));

        assertEquals("Cannot parse \"tomorrow\".", result.getFeedback());
    }

    /**
     * Test that edit tag with empty spaces in category will return an error.
     */
    @Test
    public void testEmptyTag() {
        CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
                () -> this.addCommand.execute("add hello #/      "));

        assertEquals(expectedInvalidArgumentMessage(), result.getFeedback());
    }

    @Test
    public void testStartTimeWithoutEndTime() {
        CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
                () -> this.addCommand.execute("add hello st/2016-01-01 5:00PM"));

        assertEquals("Cannot create task with start time but without end time.", result.getFeedback());
    }

    @Test
    public void testEndTimeBeforeStartTime() {
        CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
                () -> this.addCommand.execute("add hello st/2016-01-02 5:00PM et/2016-01-01 5:00PM"));

        assertEquals("End time cannot come before start time.", result.getFeedback());
    }

    private String expectedInvalidArgumentMessage() {
        return "Invalid arguments.\n\n" + TaskArgumentParser.ARGUMENT_FORMAT;
    }
}
