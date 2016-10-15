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
 * JUnit test for view command.
 */
public class ViewCommandTest {
    private Schedule schedule;
    private ViewCommand viewCommand;
    private Task task1;
    private Task task2;

    @Before
    public void setupListCommand() {
        this.schedule = new Schedule();
        this.viewCommand = new ViewCommand(this.schedule);
    }

    public void setupTaskWithAndWithoutReminders() {
        this.task1 = new Task("Task1");
        this.task2 = new Task("Task2");
        this.schedule.addTask(task1);
        this.schedule.addTask(task2);
        ArrayList<Reminder> reminders = this.task2.getReminders();
        reminders.add(new Reminder("Attend Workshop 1", LocalDateTime.of(2016, 1, 1, 17, 0)));
        reminders.add(new Reminder("Attend Workshop 2", LocalDateTime.of(2016, 2, 1, 17, 0)));
        reminders.add(new Reminder("Attend Workshop 3", LocalDateTime.of(2016, 3, 1, 17, 0)));
    }

    public CommandResult setupMultipleHelloTaskAndExecuteAmbiguousCommand() {
        this.schedule.addTask(new Task("hello it's me"));
        this.schedule.addTask(new Task("hello from the other side"));
        return this.viewCommand.execute("view hello");
    }

    @Test
    public void testRespondToViewWithKeywords() {
        assertTrue(this.viewCommand.respondTo("view keyword"));
    }

    @Test
    public void testCaseInsensitiveRespondToView() {
        assertTrue(this.viewCommand.respondTo("ViEw keyword"));
    }

    @Test
    public void testDoesNotRespondToOtherCommands() {
        assertFalse(this.viewCommand.respondTo("notview"));
    }

    @Test
    public void testCommandResultWhenNoMatchFound() {
        this.schedule.addTask(new Task("asdjkahsdkjhasjdkh"));
        CommandResult result = this.viewCommand.execute("view that nasty todo");
        assertEquals("Cannot find \"that nasty todo\".", result.getFeedback());
    }

    @Test
    public void testCommandResultWhenExactlyOneTaskWithNoRemindersFound() {
        this.setupTaskWithAndWithoutReminders();
        CommandResult result = this.viewCommand.execute("view Task1");
        assertEquals("Task1" + '\n' + "Reminders:" + '\n' + "You have not set any reminders for this task.", result.getFeedback());
    }

    @Test
    public void testCommandResultWhenExactlyOneTaskWithRemindersFound() {
        this.setupTaskWithAndWithoutReminders();
        CommandResult result = this.viewCommand.execute("view Task2");
        assertEquals("Task2\n" + "Reminders:\n"
            + "1. Attend Workshop 1 (On 2016-01-01 5:00PM)\n"
            + "2. Attend Workshop 2 (On 2016-02-01 5:00PM)\n"
            + "3. Attend Workshop 3 (On 2016-03-01 5:00PM)", result.getFeedback());
    }

    @Test
    public void testCommandResultWhenMultipleMatchesFound() {
        CommandResult result = this.setupMultipleHelloTaskAndExecuteAmbiguousCommand();
        assertEquals("Which one? (1-2)\n1. hello it's me\n2. hello from the other side", result.getFeedback());
    }

    @Test
    public void testAwaitingUserResponse() {
        assertFalse(this.viewCommand.awaitingUserResponse());
        this.setupMultipleHelloTaskAndExecuteAmbiguousCommand();
        assertTrue(this.viewCommand.awaitingUserResponse());
    }

    @Test
    public void testUserResponseCancel() {
        this.setupMultipleHelloTaskAndExecuteAmbiguousCommand();
        CommandResult result = this.viewCommand.userResponse("cancel");
        assertEquals("OK! Not viewing any task.", result.getFeedback());
        assertFalse(this.viewCommand.awaitingUserResponse());
    }

    @Test
    public void testUserResponseValidIndex() {
        this.setupMultipleHelloTaskAndExecuteAmbiguousCommand();
        CommandResult result = this.viewCommand.userResponse("1");
        assertEquals("hello it's me\nReminders:\nYou have not set any reminders for this task.", result.getFeedback());
    }

    @Test
    public void testUserResponseInvalidIndex() {
        this.setupMultipleHelloTaskAndExecuteAmbiguousCommand();
        CommandResult result = this.viewCommand.userResponse("0");
        String expectedResponse = "That's not a valid index. Enter a number between 1 and 2:\n" +
                "1. hello it's me\n2. hello from the other side";
        assertEquals(expectedResponse, result.getFeedback());
    }

    @Test
    public void testUserResponseInvalidResponse() {
        this.setupMultipleHelloTaskAndExecuteAmbiguousCommand();
        CommandResult result = this.viewCommand.userResponse("notindex");
        String expectedResponse = "I don't understand \"notindex\".\n" + "Enter a number to indicate which task to delete.\n" +
                "1. hello it's me\n2. hello from the other side";
        assertEquals(expectedResponse, result.getFeedback());
    }
}
