package linenux.command;

import static linenux.helpers.Assert.assertChangeBy;
import static linenux.helpers.Assert.assertNoChange;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import linenux.command.parser.ReminderArgumentParser;
import linenux.command.result.CommandResult;
import linenux.model.Reminder;
import linenux.model.Schedule;
import linenux.model.Task;

/**
 * JUnit test for remind command.
 */
public class RemindCommandTest {
	private Schedule schedule;
	private RemindCommand remindCommand;

	@Before
	public void setupRemindCommand() {
		ArrayList<Task> tasks = new ArrayList<>();
		tasks.add(new Task("Todo1"));
		tasks.add(new Task("Todo2"));
		tasks.add(new Task("Deadline1", LocalDateTime.of(2016, 1, 1, 1, 0)));
		tasks.add(new Task("Deadline2", LocalDateTime.of(2016, 1, 1, 13, 0)));
		tasks.add(new Task("Event1", LocalDateTime.of(2016, 1, 1, 1, 0), LocalDateTime.of(2016, 1, 1, 13, 0)));
		tasks.add(new Task("Event2", LocalDateTime.of(2016, 1, 1, 13, 0), LocalDateTime.of(2016, 1, 1, 23, 0)));

		this.schedule = new Schedule(tasks);
		this.remindCommand = new RemindCommand(this.schedule);
	}

	/**
     * Test that respondTo detects various versions of the commands. It should return true even if
     * the format of the arguments are invalid.
     */
	@Test
	public void testRespondToRemindCommand() {
		assertTrue(this.remindCommand.respondTo("remind"));
		assertTrue(this.remindCommand.respondTo("remind task"));
		assertTrue(this.remindCommand.respondTo("remind task t/2016-01-01"));
		assertTrue(this.remindCommand.respondTo("remind task t/2016-01-01 n/notes"));
	}

	/**
	 * Test respondTo is case-insensitive.
	 */
	@Test
	public void testCaseInsensitiveRemindCommand() {
		assertTrue(this.remindCommand.respondTo("ReMiNd task t/2016-01-01"));
	}

	/**
	 * Test that respondTo will return false for commands not related to adding reminders.
	 */
	@Test
	public void testNotRespondToOtherCommands() {
		assertFalse(this.remindCommand.respondTo("notremind"));
	}

	/**
	 * Test that executing adding reminder without notes to a To-Do should return correct result
	 */
	@Test
	public void testExecuteAddReminderWithoutNotesToToDo() {
		assertChangeBy(() -> this.schedule.getTaskList().get(0).getReminders().size(),
			1,
			() -> this.remindCommand.execute("remind Todo1 t/2000-01-01 05:00PM"));
		ArrayList<Task> tasks = this.schedule.getTaskList();
		ArrayList<Reminder> reminders = tasks.get(0).getReminders();
		Reminder addedReminder = reminders.get(reminders.size() - 1);
		assertEquals(LocalDateTime.of(2000, 1, 1, 17, 0), addedReminder.getTimeOfReminder());
	}

	/**
	 * Test that executing adding reminder with notes to a To-Do should return correct result
	 */
	@Test
	public void testExecuteAddReminderWithNotesToToDo() {
		assertChangeBy(() -> this.schedule.getTaskList().get(0).getReminders().size(),
			1,
			() -> this.remindCommand.execute("remind Todo1 t/2000-01-01 05:00PM n/Attend Workshop"));
		ArrayList<Task> tasks = this.schedule.getTaskList();
		ArrayList<Reminder> reminders = tasks.get(0).getReminders();
		Reminder addedReminder = reminders.get(reminders.size() - 1);
		assertEquals(LocalDateTime.of(2000, 1, 1, 17, 0), addedReminder.getTimeOfReminder());
		assertEquals("Attend Workshop", addedReminder.getNote());
	}

	/**
	 * Test that executing adding reminder with notes in different order to a To-Do should return correct result
	 */
	@Test
	public void testExecuteAddReminderWithDiffParamOrderToToDo() {
		assertChangeBy(() -> this.schedule.getTaskList().get(0).getReminders().size(),
			1,
			() -> this.remindCommand.execute("remind Todo1 n/Attend Workshop t/2000-01-01 05:00PM"));
		ArrayList<Task> tasks = this.schedule.getTaskList();
		ArrayList<Reminder> reminders = tasks.get(0).getReminders();
		Reminder addedReminder = reminders.get(reminders.size() - 1);
		assertEquals(LocalDateTime.of(2000, 1, 1, 17, 0), addedReminder.getTimeOfReminder());
		assertEquals("Attend Workshop", addedReminder.getNote());
	}

	/**
	 * Test that executing adding reminder without notes to a Deadline should return correct result
	 */
	@Test
	public void testExecuteAddReminderWithoutNotesToDeadline() {
		assertChangeBy(() -> this.schedule.getTaskList().get(2).getReminders().size(),
			1,
			() -> this.remindCommand.execute("remind Deadline1 t/2000-01-01 05:00PM"));
		ArrayList<Task> tasks = this.schedule.getTaskList();
		ArrayList<Reminder> reminders = tasks.get(2).getReminders();
		Reminder addedReminder = reminders.get(reminders.size() - 1);
		assertEquals(LocalDateTime.of(2000, 1, 1, 17, 0), addedReminder.getTimeOfReminder());
	}

	/**
	 * Test that executing adding reminder with notes to a Deadline should return correct result
	 */
	@Test
	public void testExecuteAddReminderWithNotesToDeadline() {
		assertChangeBy(() -> this.schedule.getTaskList().get(2).getReminders().size(),
			1,
			() -> this.remindCommand.execute("remind Deadline1 t/2000-01-01 05:00PM n/Attend Workshop"));
		ArrayList<Task> tasks = this.schedule.getTaskList();
		ArrayList<Reminder> reminders = tasks.get(2).getReminders();
		Reminder addedReminder = reminders.get(reminders.size() - 1);
		assertEquals(LocalDateTime.of(2000, 1, 1, 17, 0), addedReminder.getTimeOfReminder());
		assertEquals("Attend Workshop", addedReminder.getNote());
	}

	/**
	 * Test that executing adding reminder with notes in different order to a Deadline should return correct result
	 */
	@Test
	public void testExecuteAddReminderWithDiffParamOrderToDeadline() {
		assertChangeBy(() -> this.schedule.getTaskList().get(2).getReminders().size(),
			1,
			() -> this.remindCommand.execute("remind Deadline1 n/Attend Workshop t/2000-01-01 05:00PM"));
		ArrayList<Task> tasks = this.schedule.getTaskList();
		ArrayList<Reminder> reminders = tasks.get(2).getReminders();
		Reminder addedReminder = reminders.get(reminders.size() - 1);
		assertEquals(LocalDateTime.of(2000, 1, 1, 17, 0), addedReminder.getTimeOfReminder());
		assertEquals("Attend Workshop", addedReminder.getNote());
	}

	/**
	 * Test that executing adding reminder without notes to a Event should return correct result
	 */
	@Test
	public void testExecuteAddReminderWithoutNotesToEvent() {
		assertChangeBy(() -> this.schedule.getTaskList().get(4).getReminders().size(),
			1,
			() -> this.remindCommand.execute("remind Event1 t/2000-01-01 05:00PM"));
		ArrayList<Task> tasks = this.schedule.getTaskList();
		ArrayList<Reminder> reminders = tasks.get(4).getReminders();
		Reminder addedReminder = reminders.get(reminders.size() - 1);
		assertEquals(LocalDateTime.of(2000, 1, 1, 17, 0), addedReminder.getTimeOfReminder());
	}

	/**
	 * Test that executing adding reminder with notes to a Event should return correct result
	 */
	@Test
	public void testExecuteAddReminderWithNotesToEvent() {
		assertChangeBy(() -> this.schedule.getTaskList().get(4).getReminders().size(),
			1,
			() -> this.remindCommand.execute("remind Event1 t/2000-01-01 05:00PM n/Attend Workshop"));
		ArrayList<Task> tasks = this.schedule.getTaskList();
		ArrayList<Reminder> reminders = tasks.get(4).getReminders();
		Reminder addedReminder = reminders.get(reminders.size() - 1);
		assertEquals(LocalDateTime.of(2000, 1, 1, 17, 0), addedReminder.getTimeOfReminder());
		assertEquals("Attend Workshop", addedReminder.getNote());
	}

	/**
	 * Test that executing adding reminder with notes in different order to a Event should return correct result
	 */
	@Test
	public void testExecuteAddReminderWithDiffParamOrderToEvent() {
		assertChangeBy(() -> this.schedule.getTaskList().get(4).getReminders().size(),
			1,
			() -> this.remindCommand.execute("remind Event1 n/Attend Workshop t/2000-01-01 05:00PM"));
		ArrayList<Task> tasks = this.schedule.getTaskList();
		ArrayList<Reminder> reminders = tasks.get(4).getReminders();
		Reminder addedReminder = reminders.get(reminders.size() - 1);
		assertEquals(LocalDateTime.of(2000, 1, 1, 17, 0), addedReminder.getTimeOfReminder());
		assertEquals("Attend Workshop", addedReminder.getNote());
	}

	/**
	 * Test the result when no task name is given to search.
	 */
	@Test
	public void testTimeWithoutTaskNameCommandResult() {
		CommandResult result = assertNoChange(() -> this.schedule.getTaskList().get(0).getReminders().size(),
				() -> this.remindCommand.execute("remind t/2011-01-01 05:00PM"));
		assertEquals(expectedInvalidArgumentMessage(), result.getFeedback());
	}

	/**
	 * Test the result when no task name is given to search + not affected by optional field notes.
	 */
	@Test
	public void testTimeWithoutTaskNameWithNotesCommandResult() {
		CommandResult result = assertNoChange(() -> this.schedule.getTaskList().get(0).getReminders().size(),
				() -> this.remindCommand.execute("remind t/2011-01-01 05:00PM n/Attend Workshop"));
		assertEquals(expectedInvalidArgumentMessage(), result.getFeedback());
	}


	/**
	 * Test the result when no time is given for the reminder.
	 */
	@Test
	public void testTaskNameWithoutTimeCommandResult() {
		CommandResult result = assertNoChange(() -> this.schedule.getTaskList().get(0).getReminders().size(),
				() -> this.remindCommand.execute("remind Todo1"));
		assertEquals("Cannot create reminder without date.", result.getFeedback());
	}

	/**
	 * Test the result when no time is given for the reminder + not affected by optional field Notes
	 */
	@Test
	public void testTaskNameWithoutTimeWithNotesCommandResult() {
		CommandResult result = assertNoChange(() -> this.schedule.getTaskList().get(0).getReminders().size(),
				() -> this.remindCommand.execute("remind Todo1 n/Attend Workshop"));
		assertEquals("Cannot create reminder without date.", result.getFeedback());
	}


    private String expectedInvalidArgumentMessage() {
        return "Invalid arguments.\n\n" + ReminderArgumentParser.ARGUMENT_FORMAT;
    }
}