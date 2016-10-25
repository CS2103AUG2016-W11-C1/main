package linenux.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import linenux.command.result.CommandResult;
import linenux.model.Reminder;
import linenux.model.Schedule;
import linenux.model.Task;

/**
 * JUnit test for add command.
 */
public class EditrCommandTest {
    private Schedule schedule;
    private EditrCommand editrCommand;

    @Before
    public void setupeditrCommand() {
        this.schedule = new Schedule();
        this.editrCommand = new EditrCommand(this.schedule);
    }

    private void setupMultipleHelloRemindersAndExecuteAmbiguousCommand() {
        Task task = new Task("hello");
        Reminder reminder1 = new Reminder("world", LocalDateTime.of(2016, 1, 1, 17, 0));
        Reminder reminder2 = new Reminder("hello world", LocalDateTime.of(2017, 1, 1, 17, 0));

        task = task.addReminder(reminder1);
        task = task.addReminder(reminder2);

        this.schedule.addTask(task);
        this.editrCommand.execute("editr world n/wat t/2018-01-01 5:00PM");
    }

    private String expectedInvalidArgumentMessage() {
        return "Invalid arguments.\n\n" + this.editrCommand.getCommandFormat() + "\n\n" + Command.CALLOUTS;
    }
    /**
     * Test that respondTo detects various versions of the commands. It should
     * return true even if the format of the arguments are invalid.
     */
    @Test
    public void testRespondToEditTaskCommand() {
        assertTrue(this.editrCommand.respondTo("editr"));
        assertTrue(this.editrCommand.respondTo("editr n/note"));
        assertTrue(this.editrCommand.respondTo("editr t/2016-01-01 5:00PM"));
        assertTrue(this.editrCommand.respondTo("editr n/note t/2016-01-01 5:00PM"));

        assertTrue(this.editrCommand.respondTo("editr reminder"));
        assertTrue(this.editrCommand.respondTo("editr reminder n/note"));
        assertTrue(this.editrCommand.respondTo("editr reminder t/2016-01-01 5:00PM"));
        assertTrue(this.editrCommand.respondTo("editr reminder n/note t/2016-01-01 5:00PM"));
    }

    /**
     * Test that respondTo is case-insensitive.
     */
    @Test
    public void testCaseInsensitiveEditTaskCommand() {
        assertTrue(this.editrCommand.respondTo("EdITr reminder n/note t/2016-01-01 5:00PM"));
    }

    /**
     * Test that respondTo will return false for commands not related to add
     * tasks.
     */
    @Test
    public void testNotRespondToOtherCommands() {
        assertFalse(this.editrCommand.respondTo("halp"));
    }

    /**
     * Test that executing the edit reminder command will correctly edit existing
     * reminder in schedule.
     */
    @Test
    public void testEditReminderWhenSingleReminderFound() {
        Task task = new Task("hello");
        Reminder reminder = new Reminder("reminder", LocalDateTime.of(2016, 01, 01, 17, 00));
        task = task.addReminder(reminder);
        this.schedule.addTask(task);

        this.editrCommand.execute("editr reminder n/new reminder t/2017-01-01 5:00PM");

        // The edited reminder has correct note and time
        Task editedTask = this.schedule.getTaskList().get(0);
        Reminder editedReminder = editedTask.getReminders().get(0);
        assertEquals("new reminder", editedReminder.getNote());
        assertEquals(LocalDateTime.of(2017, 01, 01, 17, 0), editedReminder.getTimeOfReminder());
    }

    /**
     * Test that executing the edit reminder command will correctly edit existing
     * reminder in schedule.
     */
    @Test
    public void testEditReminderNotedoWhenSingleReminderFound() {
        Task task = new Task("hello");
        Reminder reminder = new Reminder("reminder", LocalDateTime.of(2016, 01, 01, 17, 00));
        task = task.addReminder(reminder);
        this.schedule.addTask(task);

        this.editrCommand.execute("editr reminder n/new reminder");

        // The edited reminder has correct note and time
        Task editedTask = this.schedule.getTaskList().get(0);
        Reminder editedReminder = editedTask.getReminders().get(0);
        assertEquals("new reminder", editedReminder.getNote());
        assertEquals(LocalDateTime.of(2016, 01, 01, 17, 0), editedReminder.getTimeOfReminder());
    }

    /**
     * Test that executing the edit reminder command will correctly edit existing
     * reminder in schedule.
     */
    @Test
    public void testEditReminderTimedoWhenSingleReminderFound() {
        Task task = new Task("hello");
        Reminder reminder = new Reminder("reminder", LocalDateTime.of(2016, 01, 01, 17, 00));
        task = task.addReminder(reminder);
        this.schedule.addTask(task);

        this.editrCommand.execute("editr reminder t/2017-01-01 5:00PM");

        // The edited reminder has correct note and time
        Task editedTask = this.schedule.getTaskList().get(0);
        Reminder editedReminder = editedTask.getReminders().get(0);
        assertEquals("reminder", editedReminder.getNote());
        assertEquals(LocalDateTime.of(2017, 01, 01, 17, 0), editedReminder.getTimeOfReminder());
    }

    @Test
    public void testCommandResultWhenMultipleMatchesFound() {
        Task task = new Task("hello");
        Reminder reminder1 = new Reminder("world", LocalDateTime.of(2016, 1, 1, 17, 0));
        Reminder reminder2 = new Reminder("hello world", LocalDateTime.of(2017, 1, 1, 17, 0));

        task.addReminder(reminder1);
        task.addReminder(reminder2);

        this.schedule.addTask(task);

        CommandResult result = this.editrCommand.execute("editr world n/new world t/2016-01-01 5:00PM");
        assertEquals("Cannot find reminders with \"world\".", result.getFeedback());
    }

    @Test
    public void testAwaitingUserResponse() {
        assertFalse(this.editrCommand.awaitingUserResponse());
        this.setupMultipleHelloRemindersAndExecuteAmbiguousCommand();
        assertTrue(this.editrCommand.awaitingUserResponse());
    }

    @Test
    public void testUserResponseCancel() {
        this.setupMultipleHelloRemindersAndExecuteAmbiguousCommand();
        CommandResult result = this.editrCommand.userResponse("cancel");
        assertEquals("OK! Not editing anything.", result.getFeedback());
        assertFalse(this.editrCommand.awaitingUserResponse());
    }

    @Test
    public void testUserResponseValidIndex() {
        this.setupMultipleHelloRemindersAndExecuteAmbiguousCommand();
        CommandResult result = this.editrCommand.userResponse("1");

        Task task = this.schedule.getTaskList().get(0);
        ArrayList<Reminder> reminders = task.getReminders();
        Reminder editedReminder = reminders.get(reminders.size() - 1);

        assertEquals("wat", editedReminder.getNote());
        assertEquals(LocalDateTime.of(2018, 1, 1, 17, 0), editedReminder.getTimeOfReminder());

        String expectedResult = "Edited \"world\".\n" + "New reminder details: wat (On 2018-01-01 5:00PM)";
        assertEquals(expectedResult, result.getFeedback());
    }

    @Test
    public void testUserResponseInvalidIndex() {
        this.setupMultipleHelloRemindersAndExecuteAmbiguousCommand();
        CommandResult result = this.editrCommand.userResponse("3");

        String expectedResult = "That's not a valid index. Enter a number between 1 and 2:\n" + "Task: hello\n"
                + "1. world (On 2016-01-01 5:00PM)\n" + "2. hello world (On 2017-01-01 5:00PM)";
        assertEquals(expectedResult, result.getFeedback());
    }

    @Test
    public void testUserResponseInvalidResponse() {
        this.setupMultipleHelloRemindersAndExecuteAmbiguousCommand();
        CommandResult result = this.editrCommand.userResponse("asd");

        String expectedResult = "I don't understand \"asd\".\nEnter a number to indicate which reminder to edit.\n"
                + "Task: hello\n"
                + "1. world (On 2016-01-01 5:00PM)\n" + "2. hello world (On 2017-01-01 5:00PM)";
        assertEquals(expectedResult, result.getFeedback());
    }

    /**
     * Test that executing the edit reminder command will correctly edit existing
     * reminder in schedule ignoring order of argument.
     */
    @Test
    public void testEditReminderIgnoringOrderOfArguments() {
        Task task = new Task("hello");
        Reminder reminder = new Reminder("reminder", LocalDateTime.of(2016, 01, 01, 17, 00));
        task = task.addReminder(reminder);
        this.schedule.addTask(task);

        this.editrCommand.execute("editr reminder t/2017-01-01 5:00PM n/new reminder");

        // The edited reminder has correct note and time
        Task editedTask = this.schedule.getTaskList().get(0);
        Reminder editedReminder = editedTask.getReminders().get(0);
        assertEquals("new reminder", editedReminder.getNote());
        assertEquals(LocalDateTime.of(2017, 01, 01, 17, 0), editedReminder.getTimeOfReminder());
    }

    @Test
    public void testEditReminderInvalidTime() {
        Task task = new Task("hello");
        Reminder reminder = new Reminder("reminder", LocalDateTime.of(2016, 01, 01, 17, 00));
        task = task.addReminder(reminder);
        this.schedule.addTask(task);

        CommandResult result = this.editrCommand.execute("editr reminder t/yesterday n/new reminder");

        assertEquals("Cannot parse \"yesterday\".", result.getFeedback());
    }

    @Test
    public void testEditReminderNoKeywords() {
        Task task = new Task("hello");
        Reminder reminder = new Reminder("reminder", LocalDateTime.of(2016, 01, 01, 17, 00));
        task = task.addReminder(reminder);
        this.schedule.addTask(task);

        CommandResult result = this.editrCommand.execute("editr t/2016-01-01 5:00PM n/new reminder");

        assertEquals(expectedInvalidArgumentMessage(), result.getFeedback());
    }

    @Test
    public void testEditReminderNoArguments() {
        Task task = new Task("hello");
        Reminder reminder = new Reminder("reminder", LocalDateTime.of(2016, 01, 01, 17, 00));
        task = task.addReminder(reminder);
        this.schedule.addTask(task);

        CommandResult result = this.editrCommand.execute("editr reminder");

        assertEquals("No changes to be made!", result.getFeedback());
    }

    @Test
    public void testNoMatchFound() {
        Task task = new Task("hello");
        Reminder reminder = new Reminder("reminder", LocalDateTime.of(2016, 01, 01, 17, 00));
        task = task.addReminder(reminder);
        this.schedule.addTask(task);

        CommandResult result = this.editrCommand.execute("editr hello");

        assertEquals("Cannot find reminders with \"hello\".", result.getFeedback());
    }
}
