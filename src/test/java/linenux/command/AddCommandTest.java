package linenux.command;

import static linenux.helpers.Assert.assertChangeBy;
import static linenux.helpers.Assert.assertNoChange;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import linenux.command.parser.AddArgumentParser;
import linenux.command.result.CommandResult;
import linenux.model.Schedule;
import linenux.model.Task;

//@@author A0144915A
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
        assertTrue(this.addCommand.respondTo("add #/"));
        assertTrue(this.addCommand.respondTo("add #/category"));
        assertTrue(this.addCommand.respondTo("add #/category #/"));
        assertTrue(this.addCommand.respondTo("add #/category #/tag"));

        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial"));
        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial #/"));
        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial #/category"));
        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial #/category #/"));
        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial #/category #/tag"));

        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial st/2016-01-01"));
        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial st/2016-01-01 #/"));
        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial st/2016-01-01 #/category"));
        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial st/2016-01-01 #/category #/"));
        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial st/2016-01-01 #/category #/tag"));

        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial et/2016-01-01"));
        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial et/2016-01-01 #/"));
        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial et/2016-01-01 #/category"));
        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial et/2016-01-01 #/category #/"));
        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial et/2016-01-01 #/category #/tag"));

        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial st/2016-01-01 et/2016-01-01"));
        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial st/2016-01-01 et/2016-01-01 #/"));
        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial st/2016-01-01 et/2016-01-01 #/category"));
        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial st/2016-01-01 et/2016-01-01 #/category #/"));
        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial st/2016-01-01 et/2016-01-01 #/category #/tag"));

        assertTrue(this.addCommand.respondTo("add CS2103T Tutorial st2016-01-01 et2016-01-01 #category #tag"));
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
     * Test that executing the add task command will correctly add new to-do to the schedule.
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
                () -> this.addCommand.execute("add CS2103T Tutorial et/2016-01-01 5.00PM"));

        // The new deadline has the correct name, start time, and end time
        ArrayList<Task> tasks = this.schedule.getTaskList();
        Task addedTask = tasks.get(tasks.size() - 1);
        assertEquals("CS2103T Tutorial", addedTask.getTaskName());
        assertNull(addedTask.getStartTime());
        assertEquals(LocalDateTime.of(2016, 1, 1, 17, 0), addedTask.getEndTime());
    }

    /**
     * Test that executing the add task command will correctly add new event to the schedule.
     */
    @Test
    public void testExecuteAddEvent() {
        assertChangeBy(() -> this.schedule.getTaskList().size(),
                1,
                () -> this.addCommand.execute("add CS2103T Tutorial st/2016-01-01 5.00PM et/2016-01-02 5.00PM"));

        // The new event has the correct name, start time, and end time
        ArrayList<Task> tasks = this.schedule.getTaskList();
        Task addedTask = tasks.get(tasks.size() - 1);
        assertEquals("CS2103T Tutorial", addedTask.getTaskName());
        assertEquals(LocalDateTime.of(2016, 1, 1, 17, 0), addedTask.getStartTime());
        assertEquals(LocalDateTime.of(2016, 1, 2, 17, 0), addedTask.getEndTime());
    }

    /**
     * Test that executing the add task command will correctly add a tagged Todo
     * with a single tag to schedule.
     */
    @Test
    public void testExecuteAddTaskSingleTag() {
        this.schedule.clear();
        assertChangeBy(() -> this.schedule.getTaskList().size(), 1,
                () -> this.addCommand.execute("add CS2103T Tutorial #/tag1 tag2"));

        ArrayList<Task> tasks = this.schedule.getTaskList();
        Task addedTask = tasks.get(0);

        assertEquals("CS2103T Tutorial", addedTask.getTaskName());
        assertEquals(1, addedTask.getTags().size());
        assertEquals("tag1 tag2", addedTask.getTags().get(0));
    }

    /**
     * Test that executing the add task command will correctly add a tagged Todo
     * with multiple tags to schedule.
     */
    @Test
    public void testExecuteAddTaskMultipleTag() {
        this.schedule.clear();
        assertChangeBy(() -> this.schedule.getTaskList().size(), 1,
                () -> this.addCommand.execute("add CS2103T Tutorial #/tag1 tag2 #/tag3"));

        ArrayList<Task> tasks = this.schedule.getTaskList();
        Task addedTask = tasks.get(0);

        assertEquals("CS2103T Tutorial", addedTask.getTaskName());
        assertEquals(2, addedTask.getTags().size());
        assertEquals("tag1 tag2", addedTask.getTags().get(0));
        assertEquals("tag3", addedTask.getTags().get(1));
    }

    /**
     * Test that executing the add task command will ignore repeating tags.
     *
     */
    @Test
    public void testExecuteAddTaskRepeatedTag() {
        this.schedule.clear();
        assertChangeBy(() -> this.schedule.getTaskList().size(), 1,
                () -> this.addCommand.execute("add CS2103T Tutorial #/tag #/tag"));

        ArrayList<Task> tasks = this.schedule.getTaskList();
        Task addedTask = tasks.get(0);

        assertEquals("CS2103T Tutorial", addedTask.getTaskName());
        assertEquals(1, addedTask.getTags().size());
        assertEquals("tag", addedTask.getTags().get(0));
    }

    /**
     * Test that order of times do not matter.
     */
    @Test
    public void testExecuteAddTaggedEventIgnoringOrderOfTimes() {
        assertChangeBy(() -> this.schedule.getTaskList().size(),
                1,
                () -> this.addCommand
                        .execute("add CS2103T Tutorial #/tag 1 et/2016-01-02 5.00PM #/tag 2 st/2016-01-01 5.00PM"));

        // The new event has the correct name, start time, and end time
        ArrayList<Task> tasks = this.schedule.getTaskList();
        Task addedTask = tasks.get(tasks.size() - 1);
        ArrayList<String> tagList = addedTask.getTags();

        assertEquals("CS2103T Tutorial", addedTask.getTaskName());
        assertEquals(LocalDateTime.of(2016, 1, 1, 17, 0), addedTask.getStartTime());
        assertEquals(LocalDateTime.of(2016, 1, 2, 17, 0), addedTask.getEndTime());

        assertEquals(2, tagList.size());
        assertEquals("tag 1", tagList.get(0));
        assertEquals("tag 2", tagList.get(1));
    }

    /**
     * Test that executing an add task command should return the correct result.
     */
    @Test
    public void testExecuteAddCommandResult() {
        CommandResult result = this.addCommand.execute("add CS2103T Tutorial");
        assertEquals("Added CS2103T Tutorial", result.getFeedback());
    }

    /**
     * Test that adding a new deadline should return the correct result.
     */
    @Test
    public void testExecuteAddDeadlineResult() {
        CommandResult result = this.addCommand.execute("add CS2103T Tutorial et/2016-01-01 5.00PM");
        assertEquals("Added CS2103T Tutorial (Due 2016-01-01 5.00PM)", result.getFeedback());
    }

    /**
     * Test that adding a new event should return the correct result.
     */
    @Test
    public void testExecuteAddEventResult() {
        CommandResult result = this.addCommand.execute("add CS2103T Tutorial st/2016-01-01 5.00PM et/2016-01-02 5.00PM");
        assertEquals("Added CS2103T Tutorial (2016-01-01 5.00PM - 2016-01-02 5.00PM)", result.getFeedback());
    }

    /**
     * Test that adding a new Todo with a single tag returns the correct result
     * message.
     *
     */
    @Test
    public void testExecuteAddTaskTagResult() {
        CommandResult result = this.addCommand.execute("add CS2103T Tutorial #/tag1 tag2");
        assertEquals("Added CS2103T Tutorial [Tags: \"tag1 tag2\" ]", result.getFeedback());
    }

    /**
     * Test the result when running without a task name
     *
     */
    @Test
    public void testMissingTaskNameCommandResult() {
        CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
                () -> this.addCommand.execute("add"));
        assertEquals(expectedInvalidArgumentMessage(), result.getFeedback());
    }

    /**
     * Test the result when the task name consists of only empty spaces.
     */
    @Test
    public void testTaskNameIsEmptyCommandResult() {
        CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
                () -> this.addCommand.execute("add             "));
        assertEquals(expectedInvalidArgumentMessage(), result.getFeedback());
    }

    /**
     * Test that task name cannot be empty.
     */
    @Test
    public void testStartTimeWithoutTaskName() {
        CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
                () -> this.addCommand.execute("add st/2016-01-01 5:00PM"));
        assertEquals(expectedInvalidArgumentMessage(), result.getFeedback());
    }

    /**
     * Test that task name cannot be empty.
     */
    @Test
    public void testEndTimeWithoutTaskName() {
        CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
                () -> this.addCommand.execute("add et/2016-01-01 5:00PM"));
        assertEquals(expectedInvalidArgumentMessage(), result.getFeedback());
    }

    /**
     * Test that invalid time formats are not accepted.
     */
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

    /**
     * Test that invalid time formats are not accepted.
     */
    @Test
    public void testInvalidEndTime() {
        CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
                () -> this.addCommand.execute("add hello et/tomorrow"));

        assertEquals("Cannot parse \"tomorrow\".", result.getFeedback());
    }

    /**
     * Test that adding tag with empty spaces in category will return an error.
     */
    @Test
    public void testEmptyTag() {
        CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
                () -> this.addCommand.execute("add hello #/      "));

        assertEquals(expectedInvalidArgumentMessage(), result.getFeedback());
    }

    /**
     * Test that no tasks are created with start time only.
     *
     */
    @Test
    public void testStartTimeWithoutEndTime() {
        CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
                () -> this.addCommand.execute("add hello st/2016-01-01 5.00PM"));

        assertEquals("Cannot create task with start time but without end time.", result.getFeedback());
    }

    /**
     * Test that end time cannot be before start time.
     */
    @Test
    public void testEndTimeBeforeStartTime() {
        CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
                () -> this.addCommand.execute("add hello st/2016-01-02 5.00PM et/2016-01-01 5.00PM"));

        assertEquals("End time cannot come before start time.", result.getFeedback());
    }

    /**
     * Test that duplicate to-do is not added
     */
    @Test
    public void testAddDuplicateToDo() {
        this.schedule.addTask(new Task("todo"));
        CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
                () -> this.addCommand.execute("add todo"));
        assertEquals("todo already exists in the schedule!", result.getFeedback());
    }

    /**
     * Test that duplicate deadline is not added
     */
    @Test
    public void testAddDuplicateDeadline() {
        this.schedule.addTask(new Task("deadline", LocalDateTime.of(2016, 1, 1, 17, 0)));
        CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
                () -> this.addCommand.execute("add deadline et/2016-01-01 5.00PM"));
        assertEquals("deadline (Due 2016-01-01 5.00PM) already exists in the schedule!", result.getFeedback());
    }

    /**
     * Test that duplicate event is not added
     */
    @Test
    public void testAddDuplicateEvent() {
        this.schedule
                .addTask(new Task("event", LocalDateTime.of(2016, 1, 1, 17, 0), LocalDateTime.of(2017, 1, 1, 17, 0)));
        CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
                () -> this.addCommand.execute("add event st/2016-01-01 5.00PM et/2017-01-01 5.00PM"));
        assertEquals("event (2016-01-01 5.00PM - 2017-01-01 5.00PM) already exists in the schedule!",
                result.getFeedback());
    }

    /**
     * Test that similar to-do is added
     */
    @Test
    public void testAddSimilarToDo() {
        this.schedule.addTask(new Task("todo", LocalDateTime.of(2016, 1, 1, 17, 0)));
        this.schedule
                .addTask(new Task("todo", LocalDateTime.of(2016, 1, 1, 17, 0), LocalDateTime.of(2017, 1, 1, 17, 0)));
        CommandResult result = assertChangeBy(() -> this.schedule.getTaskList().size(), 1,
                () -> this.addCommand.execute("add todo"));
        assertEquals("Added todo", result.getFeedback());
    }

    /**
     * Test that similar deadline is added
     */
    @Test
    public void testAddSimilarDeadline() {
        this.schedule.addTask(new Task("deadline"));
        this.schedule.addTask(new Task("deadline", LocalDateTime.of(2019, 1, 1, 17, 0)));
        this.schedule.addTask(
                new Task("deadline", LocalDateTime.of(2016, 1, 1, 17, 0), LocalDateTime.of(2017, 1, 1, 17, 0)));

        CommandResult result = assertChangeBy(() -> this.schedule.getTaskList().size(), 1,
                () -> this.addCommand.execute("add deadline et/2017-01-01 5.00PM"));
        assertEquals("Added deadline (Due 2017-01-01 5.00PM)", result.getFeedback());

        CommandResult result2 = assertChangeBy(() -> this.schedule.getTaskList().size(), 1,
                () -> this.addCommand.execute("add deadline et/2016-01-01 5.00PM"));
        assertEquals("Added deadline (Due 2016-01-01 5.00PM)", result2.getFeedback());
    }

    /**
     * Test that similar event is added
     */
    @Test
    public void testAddSimilarEvent() {
        this.schedule.addTask(new Task("event"));
        this.schedule.addTask(
                new Task("event", LocalDateTime.of(2016, 1, 1, 17, 0)));

        CommandResult result = assertChangeBy(() -> this.schedule.getTaskList().size(), 1,
                () -> this.addCommand.execute("add event st/2016-01-01 5.00PM et/2017-01-01 5.00PM"));
        assertEquals("Added event (2016-01-01 5.00PM - 2017-01-01 5.00PM)", result.getFeedback());
    }


    private String expectedInvalidArgumentMessage() {
        return "Invalid arguments.\n\n" + this.addCommand.getCommandFormat() + "\n\n" + Command.CALLOUTS;
    }
}
