package linenux.command;

import static linenux.helpers.Assert.assertChangeBy;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

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
		this.editCommand.execute("edit hello");
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
		        .execute(
		                "edit hello n/CS2103T Tutorial st/2016-01-01 5:00PM et/2016-01-01 7:00PM"));

		// The edited task has correct name
		ArrayList<Task> tasks = this.schedule.getTaskList();
		Task editedTask = tasks.get(0);
		assertEquals("CS2103T Tutorial", editedTask.getTaskName());

		// The edited task should have the new specified start time
		assertEquals(LocalDateTime.of(2016, 1, 1, 17, 0), editedTask.getStartTime());

		// The edited task should have the new specified end time
		assertEquals(LocalDateTime.of(2016, 1, 1, 19, 0), editedTask.getEndTime());
	}
}
