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
	private Task todo;
	private Task deadline;
	private Task event;

	@Before
	public void setupRemindCommand() {
		ArrayList<Task> tasks = new ArrayList<>();
		todo = new Task("Todo");
		deadline = new Task("Deadline", LocalDateTime.of(2016, 1, 1, 1, 0));
		event = new Task("Event", LocalDateTime.of(2016, 1, 1, 1, 0), LocalDateTime.of(2016, 1, 1, 13, 0));

		tasks.add(todo);
        tasks.add(deadline);
        tasks.add(event);

		this.schedule = new Schedule(tasks);
		this.remindCommand = new RemindCommand(this.schedule);
	}

	private void setupTaskWithSameNameAndExecuteAmbiguousCommand() {
	    this.schedule.addTask(new Task("Todo 2"));
        this.remindCommand.execute("remind Todo t/2016-01-01 05:00PM");
    }

    private String expectedInvalidArgumentMessage() {
        return "Invalid arguments.\n\n" + ReminderArgumentParser.ARGUMENT_FORMAT;
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
		assertChangeBy(() -> this.todo.getReminders().size(),
			1,
			() -> this.remindCommand.execute("remind Todo t/2000-01-01 05:00PM"));
		ArrayList<Reminder> reminders = this.todo.getReminders();
		Reminder addedReminder = reminders.get(reminders.size() - 1);
		assertEquals(LocalDateTime.of(2000, 1, 1, 17, 0), addedReminder.getTimeOfReminder());
	}

	/**
	 * Test that executing adding reminder with notes to a To-Do should return correct result
	 */
	@Test
	public void testExecuteAddReminderWithNotesToToDo() {
		assertChangeBy(() -> this.todo.getReminders().size(),
			1,
			() -> this.remindCommand.execute("remind Todo t/2000-01-01 05:00PM n/Attend Workshop"));
		ArrayList<Reminder> reminders = this.todo.getReminders();
		Reminder addedReminder = reminders.get(reminders.size() - 1);
		assertEquals(LocalDateTime.of(2000, 1, 1, 17, 0), addedReminder.getTimeOfReminder());
		assertEquals("Attend Workshop", addedReminder.getNote());
	}

	/**
	 * Test that executing adding reminder with notes in different order to a To-Do should return correct result
	 */
	@Test
	public void testExecuteAddReminderWithDiffParamOrderToToDo() {
		assertChangeBy(() -> this.todo.getReminders().size(),
			1,
			() -> this.remindCommand.execute("remind Todo n/Attend Workshop t/2000-01-01 05:00PM"));
		ArrayList<Reminder> reminders = todo.getReminders();
		Reminder addedReminder = reminders.get(reminders.size() - 1);
		assertEquals(LocalDateTime.of(2000, 1, 1, 17, 0), addedReminder.getTimeOfReminder());
		assertEquals("Attend Workshop", addedReminder.getNote());
	}

	/**
	 * Test that executing adding reminder without notes to a Deadline should return correct result
	 */
	@Test
	public void testExecuteAddReminderWithoutNotesToDeadline() {
		assertChangeBy(() -> this.deadline.getReminders().size(),
			1,
			() -> this.remindCommand.execute("remind Deadline t/2000-01-01 05:00PM"));
		ArrayList<Reminder> reminders = this.deadline.getReminders();
		Reminder addedReminder = reminders.get(reminders.size() - 1);
		assertEquals(LocalDateTime.of(2000, 1, 1, 17, 0), addedReminder.getTimeOfReminder());
	}

	/**
	 * Test that executing adding reminder with notes to a Deadline should return correct result
	 */
	@Test
	public void testExecuteAddReminderWithNotesToDeadline() {
		assertChangeBy(() -> this.deadline.getReminders().size(),
			1,
			() -> this.remindCommand.execute("remind deadline t/2000-01-01 05:00PM n/Attend Workshop"));
		ArrayList<Reminder> reminders = this.deadline.getReminders();
		Reminder addedReminder = reminders.get(reminders.size() - 1);
		assertEquals(LocalDateTime.of(2000, 1, 1, 17, 0), addedReminder.getTimeOfReminder());
		assertEquals("Attend Workshop", addedReminder.getNote());
	}

	/**
	 * Test that executing adding reminder with notes in different order to a Deadline should return correct result
	 */
	@Test
	public void testExecuteAddReminderWithDiffParamOrderToDeadline() {
		assertChangeBy(() -> this.deadline.getReminders().size(),
			1,
			() -> this.remindCommand.execute("remind deadline n/Attend Workshop t/2000-01-01 05:00PM"));
		ArrayList<Reminder> reminders = this.deadline.getReminders();
		Reminder addedReminder = reminders.get(reminders.size() - 1);
		assertEquals(LocalDateTime.of(2000, 1, 1, 17, 0), addedReminder.getTimeOfReminder());
		assertEquals("Attend Workshop", addedReminder.getNote());
	}

	/**
	 * Test that executing adding reminder without notes to a Event should return correct result
	 */
	@Test
	public void testExecuteAddReminderWithoutNotesToEvent() {
		assertChangeBy(() -> this.event.getReminders().size(),
			1,
			() -> this.remindCommand.execute("remind event t/2000-01-01 05:00PM"));
		ArrayList<Reminder> reminders = this.event.getReminders();
		Reminder addedReminder = reminders.get(reminders.size() - 1);
		assertEquals(LocalDateTime.of(2000, 1, 1, 17, 0), addedReminder.getTimeOfReminder());
	}

	/**
	 * Test that executing adding reminder with notes to a Event should return correct result
	 */
	@Test
	public void testExecuteAddReminderWithNotesToEvent() {
		assertChangeBy(() -> this.event.getReminders().size(),
			1,
			() -> this.remindCommand.execute("remind Event t/2000-01-01 05:00PM n/Attend Workshop"));
		ArrayList<Reminder> reminders = this.event.getReminders();
		Reminder addedReminder = reminders.get(reminders.size() - 1);
		assertEquals(LocalDateTime.of(2000, 1, 1, 17, 0), addedReminder.getTimeOfReminder());
		assertEquals("Attend Workshop", addedReminder.getNote());
	}

	/**
	 * Test that executing adding reminder with notes in different order to a Event should return correct result
	 */
	@Test
	public void testExecuteAddReminderWithDiffParamOrderToEvent() {
		assertChangeBy(() -> this.event.getReminders().size(),
			1,
			() -> this.remindCommand.execute("remind Event n/Attend Workshop t/2000-01-01 05:00PM"));
		ArrayList<Reminder> reminders = this.event.getReminders();
		Reminder addedReminder = reminders.get(reminders.size() - 1);
		assertEquals(LocalDateTime.of(2000, 1, 1, 17, 0), addedReminder.getTimeOfReminder());
		assertEquals("Attend Workshop", addedReminder.getNote());
	}

	/**
	 * Test the result when no task name is given to search.
	 */
	@Test
	public void testTimeWithoutTaskNameCommandResult() {
        ArrayList<Task> tasks = this.schedule.getTaskList();
        CommandResult result = assertNoChange(() -> {
            int size = 0;
            for (int i = 0; i < tasks.size(); i++) {
                size += tasks.get(i).getReminders().size();
            }
            return size;
        }, () -> this.remindCommand.execute("remind t/2011-01-01 05:00PM"));
		assertEquals(expectedInvalidArgumentMessage(), result.getFeedback());
	}

	/**
	 * Test the result when no task name is given to search + not affected by optional field notes.
	 */
	@Test
	public void testTimeWithoutTaskNameWithNotesCommandResult() {
        ArrayList<Task> tasks = this.schedule.getTaskList();
        CommandResult result = assertNoChange(() -> {
            int size = 0;
            for (int i = 0; i < tasks.size(); i++) {
                size += tasks.get(i).getReminders().size();
            }
            return size;
        }, () -> this.remindCommand.execute("remind t/2011-01-01 05:00PM n/Attend Workshop"));
		assertEquals(expectedInvalidArgumentMessage(), result.getFeedback());
	}


	/**
	 * Test the result when no time is given for the reminder.
	 */
	@Test
	public void testTaskNameWithoutTimeCommandResult() {
		CommandResult result = assertNoChange(() -> this.todo.getReminders().size(),
				() -> this.remindCommand.execute("remind todo"));
		assertEquals("Cannot create reminder without date.", result.getFeedback());
	}

	/**
	 * Test the result when no time is given for the reminder + not affected by optional field Notes
	 */
	@Test
	public void testTaskNameWithoutTimeWithNotesCommandResult() {
		CommandResult result = assertNoChange(() -> this.todo.getReminders().size(),
				() -> this.remindCommand.execute("remind todo n/Attend Workshop"));
		assertEquals("Cannot create reminder without date.", result.getFeedback());
	}

	/**
	 * Test the result when time is invalid
	 */
	@Test
	public void testInvalidTimeCommandResult() {
		CommandResult result = assertNoChange(() -> this.todo.getReminders().size(),
				() -> this.remindCommand.execute("remind todo t/tomorrow"));
		assertEquals("Cannot parse \"tomorrow\".", result.getFeedback());
	}

    @Test
    public void testCommandResultWhenNoMatchFound() {
        ArrayList<Task> tasks = this.schedule.getTaskList();
        CommandResult result = assertNoChange(() -> {
            int size = 0;
            for (int i = 0; i < tasks.size(); i++) {
                size += tasks.get(i).getReminders().size();
            }
            return size;
        }, () -> this.remindCommand.execute("remind not task t/2016-01-01 05:00PM"));
        assertEquals("Cannot find \"not task\".", result.getFeedback());
    }

    @Test
    public void testCommandResultWhenMultipleMatchesFound() {
        this.schedule.addTask(new Task("todo 2"));
        ArrayList<Task> tasks = this.schedule.getTaskList();
        CommandResult result = assertNoChange(() -> {
            int size = 0;
            for (int i = 0; i < tasks.size(); i++) {
                size += tasks.get(i).getReminders().size();
            }
            return size;
        }, () -> this.remindCommand.execute("remind todo t/2016-01-01 05:00PM"));
        assertEquals("Which one? (1-2)\n1. Todo\n2. todo 2\n", result.getFeedback());
    }

    @Test
    public void testAwaitingUserResponse() {
        assertFalse(this.remindCommand.awaitingUserResponse());
        this.setupTaskWithSameNameAndExecuteAmbiguousCommand();
        assertTrue(this.remindCommand.awaitingUserResponse());
    }

    @Test
    public void testUserResponseCancel() {
        ArrayList<Task> tasks = this.schedule.getTaskList();
        this.setupTaskWithSameNameAndExecuteAmbiguousCommand();
        CommandResult result = assertNoChange(() -> {
            int size = 0;
            for (int i = 0; i < tasks.size(); i++) {
                size += tasks.get(i).getReminders().size();
            }
            return size;
        }, () -> this.remindCommand.userResponse("cancel"));
        assertEquals("OK! Not adding new reminder.", result.getFeedback());
        assertFalse(this.remindCommand.awaitingUserResponse());
    }

    @Test
    public void testUserResponseValidIndex() {
        this.setupTaskWithSameNameAndExecuteAmbiguousCommand();
        CommandResult result = assertChangeBy(() -> this.todo.getReminders().size(), 1,
                () -> this.remindCommand.userResponse("1"));
        assertEquals("Added reminder on 2016-01-01 5:00PM for Todo", result.getFeedback());
    }

    @Test
    public void testUserResponseInvalidIndex() {
        this.setupTaskWithSameNameAndExecuteAmbiguousCommand();
        CommandResult result = assertNoChange(() -> this.todo.getReminders().size(),
                () -> this.remindCommand.userResponse("0"));
        assertEquals("That's not a valid index. Enter a number between 1 and 2:\n" + "1. Todo\n2. Todo 2\n",
                result.getFeedback());
    }

    @Test
    public void testUserResponseInvalidUserResponse() {
        this.setupTaskWithSameNameAndExecuteAmbiguousCommand();
        CommandResult result = assertNoChange(() -> this.todo.getReminders().size(),
                () -> this.remindCommand.userResponse("One"));
        assertEquals("I don't understand \"One\".\nEnter a number to indicate which task to add reminder to:\n"
                + "1. Todo\n2. Todo 2\n",
                result.getFeedback());
    }
}