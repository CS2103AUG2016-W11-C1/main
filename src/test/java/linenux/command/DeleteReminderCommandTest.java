package linenux.command;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static linenux.helpers.Assert.assertChangeBy;
import static linenux.helpers.Assert.assertNoChange;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import linenux.command.result.CommandResult;
import linenux.model.Reminder;
import linenux.model.Schedule;
import linenux.model.Task;

/**
 * JUnit test for deleter command.
 */
// @@author A0127694U
public class DeleteReminderCommandTest {
    private Schedule schedule;
    private DeleteReminderCommand deleteReminderCommand;

    @Before
    public void setupDeleterCommand() {
        this.schedule = new Schedule();
        this.deleteReminderCommand = new DeleteReminderCommand(this.schedule);
    }

    private void setupMultipleRemindersAndExecuteAmbiguousCommand() {
        Task task1 = new Task("hello world");
        task1.getReminders().add(new Reminder("wash up", LocalDateTime.of(2016, 11, 1, 17, 0)));
        this.schedule.addTask(task1);
        Task task2 = new Task("hello");
        task2.getReminders().add(new Reminder("wash laundry", LocalDateTime.of(2016, 12, 1, 3, 0)));
        task2.getReminders().add(new Reminder("wash car", LocalDateTime.of(2016, 12, 2, 3, 0)));
        this.schedule.addTask(task2);
        this.deleteReminderCommand.execute("deleter wash");
    }

    /**
     * Test that respondTo detects various versions of the commands. It should
     * return true even if the format of the arguments are invalid.
     */
    @Test
    public void testRespondToDeleterCommand() {
        assertTrue(this.deleteReminderCommand.respondTo("deleter"));
        assertTrue(this.deleteReminderCommand.respondTo("deleter    "));
        assertTrue(this.deleteReminderCommand.respondTo("deleter hello"));
    }

    /**
     * Test that the deleter command is case insensitive.
     */
    @Test
    public void testCaseInsensitiveRespondToDeleteCommand() {
        assertTrue(this.deleteReminderCommand.respondTo("dElEteR hello"));
    }

    /**
     * Test that respondTo will return false for commands not related to deleter
     * tasks.
     */
    @Test
    public void testDoesNotRespondToOtherCommands() {
        assertFalse(this.deleteReminderCommand.respondTo("walala"));
    }

    /**
     * Test invalid arguments.
     */
    @Test
    public void testInvalidArguments() {
        CommandResult result1 = this.deleteReminderCommand.execute("deleter");
        CommandResult result2 = this.deleteReminderCommand.execute("deleter ");
        CommandResult result3 = this.deleteReminderCommand.execute("deleter      ");

        assertEquals(expectedInvalidArgumentMessage(), result1.getFeedback());
        assertEquals(expectedInvalidArgumentMessage(), result2.getFeedback());
        assertEquals(expectedInvalidArgumentMessage(), result3.getFeedback());
    }

    /**
     * Test the feedback when no match is found.
     */
    @Test
    public void testCommandResultWhenNoMatchFound() {
        this.schedule.clear();
        this.schedule.getTaskList().add(new Task("task!"));

        CommandResult result = assertNoChange(() -> this.schedule.getReminderList().size(),
                () -> this.deleteReminderCommand.execute("deleter that nasty reminder"));
        assertEquals("Cannot find reminders with \"that nasty reminder\".", result.getFeedback());
    }

    /**
     * Test the feedback when only one match is found.
     */
    @Test
    public void testCommandResultWhenOnlyOneMatchFound() {
        this.schedule.clear();
        Task task1 = new Task("hello");
        task1.getReminders().add(new Reminder("world", LocalDateTime.of(2016, 12, 1, 18, 0)));
        this.schedule.addTask(task1);
        Task task2 = new Task("new world");
        task2.getReminders().add(new Reminder("begins", LocalDateTime.of(2016, 12, 3, 2, 0)));

        CommandResult result = assertChangeBy(() -> this.schedule.getReminderList().size(), -1,
                () -> this.deleteReminderCommand.execute("deleter world"));
        assertEquals("Deleted reminder \"world (On 2016-12-01 6.00PM)\" from task \"hello\".", result.getFeedback());
    }

    /**
     * Test the feedback when multiple matches are found.
     */
    @Test
    public void testCommandResultWhenMultipleMatchesFound() {
        Task task1 = new Task("hello world");
        task1.getReminders().add(new Reminder("hello", LocalDateTime.of(2017, 1, 1, 0, 0)));
        this.schedule.addTask(task1);
        Task task2 = new Task("say hello");
        task2.getReminders().add(new Reminder("hello again", LocalDateTime.of(2017, 1, 5, 3, 0)));
        this.schedule.addTask(task2);
        CommandResult result = assertNoChange(() -> this.schedule.getReminderList().size(),
                () -> this.deleteReminderCommand.execute("deleter hello"));
        assertEquals(
                "Which one? (1-2, \"cancel\" to cancel the current operation)\nTask: hello world\n1. hello (On 2017-01-01 12.00AM)\nTask: say hello\n2. hello again (On 2017-01-05 3.00AM)",
                result.getFeedback());
    }

    private String expectedInvalidArgumentMessage() {
        return "Invalid arguments.\n\n" + this.deleteReminderCommand.getCommandFormat() + "\n\n" + Command.CALLOUTS;
    }

    /**
     * Test the command is awaiting user response when multiple matches are
     * found.
     */
    @Test
    public void testAwaitingUserResponse() {
        assertFalse(this.deleteReminderCommand.isAwaitingUserResponse());
        this.setupMultipleRemindersAndExecuteAmbiguousCommand();
        assertTrue(this.deleteReminderCommand.isAwaitingUserResponse());
    }

    /**
     * Test that cancel works properly.
     */
    @Test
    public void testUserResponseCancel() {
        this.setupMultipleRemindersAndExecuteAmbiguousCommand();
        CommandResult result = assertNoChange(() -> this.schedule.getReminderList().size(),
                () -> this.deleteReminderCommand.processUserResponse("cancel"));
        assertEquals("OK! Not deleting anything.", result.getFeedback());
        assertFalse(this.deleteReminderCommand.isAwaitingUserResponse());
    }

    /**
     * Test that reminder is deleted if user selects a valid index.
     */
    @Test
    public void testUserResponseValidIndex() {
        this.setupMultipleRemindersAndExecuteAmbiguousCommand();
        CommandResult result = assertChangeBy(() -> this.schedule.getReminderList().size(), -1,
                () -> this.deleteReminderCommand.processUserResponse("1"));
        assertEquals("Deleted reminder \"wash up (On 2016-11-01 5.00PM)\" from task \"hello world\".",
                result.getFeedback());
        assertFalse(this.deleteReminderCommand.isAwaitingUserResponse());
    }

    /**
     * Test that reminder is not deleted if user selects an invalid index.
     */
    @Test
    public void testUserResponseInvalidIndex() {
        this.setupMultipleRemindersAndExecuteAmbiguousCommand();
        CommandResult result = assertNoChange(() -> this.schedule.getReminderList().size(),
                () -> this.deleteReminderCommand.processUserResponse("0"));
        String expectedResponse = "That's not a valid index. Enter a number between 1 and 3, or \"cancel\" to cancel the current operation:\n"
                + "Task: hello world\n1. wash up (On 2016-11-01 5.00PM)\nTask: hello\n2. wash laundry (On 2016-12-01 3.00AM)\n3. wash car (On 2016-12-02 3.00AM)";
        assertEquals(expectedResponse, result.getFeedback());
        assertTrue(this.deleteReminderCommand.isAwaitingUserResponse());
    }

    /**
     * Test that task is not deleted if user types an invalid response.
     */
    @Test
    public void testUserResponseInvalidResponse() {
        this.setupMultipleRemindersAndExecuteAmbiguousCommand();
        CommandResult result = assertNoChange(() -> this.schedule.getReminderList().size(),
                () -> this.deleteReminderCommand.processUserResponse("roses are red"));
        String expectedResponse = "I don't understand \"roses are red\".\n"
                + "Enter a number to indicate which reminder to delete.\nTask: hello world\n1. wash up (On 2016-11-01 5.00PM)\nTask: hello\n2. wash laundry (On 2016-12-01 3.00AM)\n3. wash car (On 2016-12-02 3.00AM)";
        assertEquals(expectedResponse, result.getFeedback());
        assertTrue(this.deleteReminderCommand.isAwaitingUserResponse());
    }
}
