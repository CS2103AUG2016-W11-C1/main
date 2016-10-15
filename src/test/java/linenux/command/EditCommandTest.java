package linenux.command;

import static linenux.helpers.Assert.assertChangeBy;
import static linenux.helpers.Assert.assertNoChange;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import linenux.command.parser.EditTaskArgumentParser;
import linenux.command.result.CommandResult;
import linenux.model.Schedule;
import linenux.model.Task;

/**
 * JUnit test for add command.
 */
public class EditCommandTest {
	private Schedule schedule;
	private EditCommand editCommand;

	@Before
	public void setupEditCommand() {
		this.schedule = new Schedule();
		this.editCommand = new EditCommand(this.schedule);
	}

	private void setupMultipleHelloTasksAndExecuteAmbiguousCommand() {
		this.schedule.addTask(new Task("hello world"));
		this.schedule.addTask(new Task("say hello"));
		this.editCommand.execute("edit hello n/CS2103T Tutorial");
	}

	/**
	 * Test that respondTo detects various versions of the commands. It should
	 * return true even if the format of the arguments are invalid.
	 */
	@Test
	public void testRespondToEditTaskCommand() {
		assertTrue(this.editCommand.respondTo("edit"));
		assertTrue(this.editCommand.respondTo("edit CS2103T Tutorial"));
		assertTrue(this.editCommand.respondTo("edit CS2103T Tutorial n/CS2103T Project"));
		assertTrue(this.editCommand.respondTo("edit CS2103T Tutorial st/2016-01-01"));
		assertTrue(this.editCommand.respondTo("edit CS2103T Tutorial et/2016-01-01"));
		assertTrue(this.editCommand.respondTo("edit CS2103T Tutorial n/CS2103T Project st/2016-01-01"));
		assertTrue(this.editCommand.respondTo("edit CS2103T Tutorial n/CS2103T Project et/2016-01-01"));
		assertTrue(this.editCommand.respondTo("edit CS2103T Tutorial n/CS2103T Project st/2016-01-01 et/2016-01-01"));
	}

	/**
	 * Test that respondTo is case-insensitive.
	 */
	@Test
	public void testCaseInsensitiveEditTaskCommand() {
		assertTrue(this.editCommand.respondTo("EdIT CS2103T Tutorial N/hello"));
	}

	/**
	 * Test that respondTo will return false for commands not related to add
	 * tasks.
	 */
	@Test
	public void testNotRespondToOtherCommands() {
		assertFalse(this.editCommand.respondTo("halp"));
	}

	/**
	 * Test that executing the edit task command will correctly edit existing
	 * todo in schedule.
	 *
	 */
	@Test
	public void testEditTodoToTodoWhenSingleTodoFound() {
		this.schedule.clear();
		this.schedule.addTask(new Task("hello"));
		assertChangeBy(() -> this.schedule.getTaskList().size(), 0,
		        () -> this.editCommand.execute("edit hello n/CS2103T Tutorial"));

		// The edited task has correct name
		ArrayList<Task> tasks = this.schedule.getTaskList();
		Task editedTask = tasks.get(0);
		assertEquals("CS2103T Tutorial", editedTask.getTaskName());

		// The edited task should not have start time
		assertNull(editedTask.getStartTime());

		// The edited task should not have end time
		assertNull(editedTask.getEndTime());
	}

	/**
	 * Test that executing the edit task command will correctly modify existing
	 * Todo in schedule into a Deadline.
	 *
	 */
	@Test
	public void testEditTodoToDeadlineWhenSingleTodoFound() {
		this.schedule.clear();
		this.schedule.addTask(new Task("hello"));
		assertChangeBy(() -> this.schedule.getTaskList().size(), 0,
		        () -> this.editCommand.execute("edit hello n/CS2103T Tutorial et/2016-01-01 5:00PM"));

		// The edited task has correct name
		ArrayList<Task> tasks = this.schedule.getTaskList();
		Task editedTask = tasks.get(0);
		assertEquals("CS2103T Tutorial", editedTask.getTaskName());

		// The edited task should not have start time
		assertNull(editedTask.getStartTime());

		// The edited task should have the new specified end time.
		assertEquals(LocalDateTime.of(2016, 1, 1, 17, 0), editedTask.getEndTime());
	}

	/**
	 * Test that executing the edit task command will correctly modify existing
	 * Todo in schedule into an Event.
	 *
	 */
	@Test
	public void testEditTodoToEventWhenSingleTodoFound() {
		this.schedule.clear();
		this.schedule.addTask(new Task("hello"));
		assertChangeBy(() -> this.schedule.getTaskList().size(), 0,
		        () -> this.editCommand
		                .execute("edit hello n/CS2103T Tutorial st/2016-01-01 5:00PM " + "et/2016-01-01 7:00PM"));

		// The edited task has correct name
		ArrayList<Task> tasks = this.schedule.getTaskList();
		Task editedTask = tasks.get(0);
		assertEquals("CS2103T Tutorial", editedTask.getTaskName());

		// The edited task should have the new specified start time
		assertEquals(LocalDateTime.of(2016, 1, 1, 17, 0), editedTask.getStartTime());

		// The edited task should have the new specified end time
		assertEquals(LocalDateTime.of(2016, 1, 1, 19, 0), editedTask.getEndTime());
	}

	/**
	 * Test that executing the edit task command will correctly modify existing
	 * Deadline in schedule to Todo.
	 *
	 */
	@Test
	public void testEditDeadlineToTodoWhenSingleDeadlineFound() {
		this.schedule.clear();
		this.schedule.addTask(new Task("hello et/2016-01-01 5:00PM"));
		assertChangeBy(() -> this.schedule.getTaskList().size(), 0,
		        () -> this.editCommand.execute("edit hello n/CS2103T Tutorial et/rm"));

		// The edited task has correct name
		ArrayList<Task> tasks = this.schedule.getTaskList();
		Task editedTask = tasks.get(0);
		assertEquals("CS2103T Tutorial", editedTask.getTaskName());

		// The edited task should not have start time
		assertNull(editedTask.getStartTime());

		// The edited task should not have end time
		assertNull(editedTask.getEndTime());
	}

	/**
	 * Test that executing the edit task command will correctly edit existing
	 * Deadline in schedule.
	 *
	 */
	@Test
	public void testEditDeadlineToDeadlineWhenSingleDeadlineFound() {
		this.schedule.clear();
		this.schedule.addTask(new Task("hello et/2016-01-01 5:00PM"));
		assertChangeBy(() -> this.schedule.getTaskList().size(), 0,
		        () -> this.editCommand.execute("edit hello n/CS2103T Tutorial et/2016-01-01 7:00PM"));

		// The edited task has correct name
		ArrayList<Task> tasks = this.schedule.getTaskList();
		Task editedTask = tasks.get(0);
		assertEquals("CS2103T Tutorial", editedTask.getTaskName());

		// The edited task should not have start time
		assertNull(editedTask.getStartTime());

		// The edited task should have the new specified end time
		assertEquals(LocalDateTime.of(2016, 1, 1, 19, 0), editedTask.getEndTime());
	}

	/**
	 * Test that executing the edit task command will correctly modify existing
	 * Deadline in schedule to Event.
	 *
	 */
	@Test
	public void testEditDeadlineToEventWhenSingleDeadlineFound() {
		this.schedule.clear();
		this.schedule.addTask(new Task("hello et/2016-01-01 5:00PM"));
		assertChangeBy(() -> this.schedule.getTaskList().size(), 0,
		        () -> this.editCommand
		                .execute("edit hello n/CS2103T Tutorial st/2016-01-01 5:00PM et/2016-01-01 7:00PM"));

		// The edited task has correct name
		ArrayList<Task> tasks = this.schedule.getTaskList();
		Task editedTask = tasks.get(0);
		assertEquals("CS2103T Tutorial", editedTask.getTaskName());

		// The edited task should have the new specified end time
		assertEquals(LocalDateTime.of(2016, 1, 1, 17, 0), editedTask.getStartTime());

		// The edited task should have the new specified end time
		assertEquals(LocalDateTime.of(2016, 1, 1, 19, 0), editedTask.getEndTime());
	}

	/**
	 * Test that executing the edit task command will correctly modify existing
	 * Event in schedule to Todo.
	 *
	 */
	@Test
	public void testEditEventToTodoWhenSingleEventFound() {
		this.schedule.clear();
		this.schedule.addTask(new Task("hello st/2016-01-01 3:00PM et/2016-01-01 5:00PM"));
		assertChangeBy(() -> this.schedule.getTaskList().size(), 0, () -> this.editCommand
		        .execute("edit hello n/CS2103T Tutorial st/rm et/rm"));

		// The edited task has correct name
		ArrayList<Task> tasks = this.schedule.getTaskList();
		Task editedTask = tasks.get(0);
		assertEquals("CS2103T Tutorial", editedTask.getTaskName());

		// The edited task should not have start time
		assertNull(editedTask.getStartTime());

		// The edited task should not have end time
		assertNull(editedTask.getEndTime());
	}

	/**
	 * Test that executing the edit task command will correctly modify existing
	 * Event in schedule to Deadline.
	 *
	 */
	@Test
	public void testEditEventToDeadlineWhenSingleEventFound() {
		this.schedule.clear();
		this.schedule.addTask(new Task("hello st/2016-01-01 3:00PM et/2016-01-01 5:00PM"));
		assertChangeBy(() -> this.schedule.getTaskList().size(), 0, () -> this.editCommand
		        .execute("edit hello n/CS2103T Tutorial st/rm et/2016-01-01 7:00PM"));

		// The edited task has correct name
		ArrayList<Task> tasks = this.schedule.getTaskList();
		Task editedTask = tasks.get(0);
		assertEquals("CS2103T Tutorial", editedTask.getTaskName());

		// The edited task should have the new specified end time
		assertNull(editedTask.getStartTime());

		// The edited task should have the new specified end time
		assertEquals(LocalDateTime.of(2016, 1, 1, 19, 0), editedTask.getEndTime());
	}

	/**
	 * Test that executing the edit task command will correctly edit existing
	 * Event in schedule.
	 *
	 */
	@Test
	public void testEditEventToEventWhenSingleEventFound() {
		this.schedule.clear();
		this.schedule.addTask(new Task("hello st/2016-01-01 3:00PM et/2016-01-01 5:00PM"));
		assertChangeBy(() -> this.schedule.getTaskList().size(), 0, () -> this.editCommand
		        .execute("edit hello n/CS2103T Tutorial st/2016-01-01 5:00PM et/2016-01-01 7:00PM"));

		// The edited task has correct name
		ArrayList<Task> tasks = this.schedule.getTaskList();
		Task editedTask = tasks.get(0);
		assertEquals("CS2103T Tutorial", editedTask.getTaskName());

		// The edited task should have the new specified start time
		assertEquals(LocalDateTime.of(2016, 1, 1, 17, 0), editedTask.getStartTime());

		// The edited task should have the new specified end time
		assertEquals(LocalDateTime.of(2016, 1, 1, 19, 0), editedTask.getEndTime());
	}

	@Test
	public void testCommandResultWhenMultipleMatchesFound() {
		this.schedule.addTask(new Task("hello world"));
		this.schedule.addTask(new Task("say hello"));

		CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
		        () -> this.editCommand.execute("edit hello n/CS2103T Tutorial"));

		assertEquals("Which one? (1-2)\n1. hello world\n2. say hello\n", result.getFeedback());
	}

	@Test
	public void testAwaitingUserResponse() {
		assertFalse(this.editCommand.awaitingUserResponse());
		this.setupMultipleHelloTasksAndExecuteAmbiguousCommand();
		assertTrue(this.editCommand.awaitingUserResponse());
	}

	@Test
	public void testCommandString() {
		this.schedule.addTask(new Task("hello"));
		assertNull(this.editCommand.getCommandString());

		CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
		        () -> this.editCommand.execute("edit hello n/new name"));

		assertNull(this.editCommand.getCommandString());
	}

	@Test
	public void testCommandStringAmbiguous() {
		assertNull(this.editCommand.getCommandString());
		this.setupMultipleHelloTasksAndExecuteAmbiguousCommand();
		assertEquals("edit hello n/CS2103T Tutorial", this.editCommand.getCommandString());

		CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
		        () -> this.editCommand.userResponse("1"));
		assertNull(this.editCommand.getCommandString());
	}

	@Test
	public void testUserResponseCancel() {
		this.setupMultipleHelloTasksAndExecuteAmbiguousCommand();
		assertEquals("edit hello n/CS2103T Tutorial", this.editCommand.getCommandString());

		CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
		        () -> this.editCommand.userResponse("cancel"));
		assertEquals("OK! Not editing anything.", result.getFeedback());

		assertNull(this.editCommand.getCommandString());
		assertFalse(this.editCommand.awaitingUserResponse());
	}

	@Test
	public void testUserResponseValidIndex() {
		this.schedule.clear();
		this.setupMultipleHelloTasksAndExecuteAmbiguousCommand();
		CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
		        () -> this.editCommand.userResponse("1"));
		assertEquals("Edited \"hello world\".\nNew task details: CS2103T Tutorial", result.getFeedback());
	}

	@Test
	public void testUserResponseInvalidIndex() {
		this.setupMultipleHelloTasksAndExecuteAmbiguousCommand();
		CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
		        () -> this.editCommand.userResponse("0"));
		String expectedResponse = "That's not a valid index. Enter a number between 1 and 2:\n"
		        + "1. hello world\n2. say hello\n";
		assertEquals(expectedResponse, result.getFeedback());
	}

	@Test
	public void testUserResponseInvalidResponse() {
		this.setupMultipleHelloTasksAndExecuteAmbiguousCommand();
		CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
		        () -> this.editCommand.userResponse("roses are red"));
		String expectedResponse = "I don't understand \"roses are red\".\n"
		        + "Enter a number to indicate which task to edit.\n1. hello world\n2. say hello\n";
		assertEquals(expectedResponse, result.getFeedback());
	}

	/**
	 * Test that arguments entered in wrong order will be stored in the correct
	 * order.
	 *
	 */
	@Test
	public void testEditIgnoringOrderOfArguments() {
		this.schedule.clear();
		this.schedule.addTask(new Task("hello"));
		assertChangeBy(() -> this.schedule.getTaskList().size(), 0,
		        () -> this.editCommand
		                .execute("edit hello et/2016-01-02 5:00PM n/CS2103T Tutorial st/2016-01-01 5:00PM"));

		// The new event has the correct name, start time, and end time
		ArrayList<Task> tasks = this.schedule.getTaskList();
		Task editedTask = tasks.get(0);
		assertEquals("CS2103T Tutorial", editedTask.getTaskName());
		assertEquals(LocalDateTime.of(2016, 1, 1, 17, 0), editedTask.getStartTime());
		assertEquals(LocalDateTime.of(2016, 1, 2, 17, 0), editedTask.getEndTime());
	}

	@Test
	public void testEditInvalidStartTime() {
		this.schedule.clear();
		this.schedule.addTask(new Task("hello"));
		CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
		        () -> this.editCommand.execute("edit hello st/yesterday et/2016-12-31 11:59PM"));

		assertEquals("Cannot parse \"yesterday\".", result.getFeedback());
	}

	@Test
	public void testEditInvalidEndTime() {
		this.schedule.clear();
		this.schedule.addTask(new Task("hello"));
		CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
		        () -> this.editCommand.execute("edit hello et/tomorrow"));

		assertEquals("Cannot parse \"tomorrow\".", result.getFeedback());
	}

	@Test
	public void testEditStartTimeWithoutEndTime() {
		this.schedule.addTask(new Task("hello"));
		CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
		        () -> this.editCommand.execute("edit hello st/2016-01-01 5:00PM"));

		assertEquals("Cannot create task with start time but without end time.", result.getFeedback());
	}

	@Test
    public void testEndTimeBeforeStartTime() {
		this.schedule.addTask(new Task("hello"));
        CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
                () -> this.editCommand.execute("edit hello st/2016-01-02 5:00PM et/2016-01-01 5:00PM"));

        assertEquals("End time cannot come before start time.", result.getFeedback());
    }

	/**
	 * Test that edit command will correctly reject input when there are no
	 * keywords and arguments.
	 *
	 */
	@Test
	public void testCommandResultWhenNoKeywords() {
		CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
		        () -> this.editCommand.execute("edit "));
		assertEquals("No keywords entered!", result.getFeedback());
	}

	/**
	 * Test that edit command will correctly reject input when there are no
	 * arguments.
	 *
	 */
	@Test
	public void testCommandResultWhenNoArguments() {
		this.schedule.addTask(new Task("hello"));
		CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
		        () -> this.editCommand.execute("edit hello "));
		assertEquals("No changes to be made!", result.getFeedback());
	}

	/**
	 * Test the result when the task name consists of only empty spaces
	 */
	@Test
	public void testTaskNameIsEmptyCommandResult() {
		this.schedule.addTask(new Task("hello"));
		CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
		        () -> this.editCommand.execute("edit hello n/            "));
		assertEquals(expectedInvalidArgumentMessage(), result.getFeedback());
	}

	/**
	 * Test that edit command will correctly reject input with no matching
	 * keywords.
	 */
	@Test
	public void testCommandResultWhenNoMatchFound() {
		this.schedule.addTask(new Task("flkasdjfaklsdfjaldf"));
		CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
		        () -> this.editCommand.execute("edit that nasty todo n/new name"));
		assertEquals("Cannot find \"that nasty todo\".", result.getFeedback());
	}

	private String expectedInvalidArgumentMessage() {
		return "Invalid arguments.\n\n" + EditTaskArgumentParser.ARGUMENT_FORMAT;
	}
}
