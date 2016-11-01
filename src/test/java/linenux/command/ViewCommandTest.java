package linenux.command;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import linenux.command.result.CommandResult;
import linenux.model.Reminder;
import linenux.model.Schedule;
import linenux.model.Task;

//@@author A0140702X
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

    /**
     * Test that command responds to the correct format.
     */
    @Test
    public void testRespondToViewWithKeywords() {
        assertTrue(this.viewCommand.respondTo("view keyword"));
    }

    /**
     * Test that command is case insensitive.
     */
    @Test
    public void testCaseInsensitiveRespondToView() {
        assertTrue(this.viewCommand.respondTo("ViEw keyword"));
    }

    /**
     * Test that command does not respons to other commands.
     */
    @Test
    public void testDoesNotRespondToOtherCommands() {
        assertFalse(this.viewCommand.respondTo("notview"));
    }

    /**
     * Test the result when no match is found.
     */
    @Test
    public void testCommandResultWhenNoMatchFound() {
        this.schedule.addTask(new Task("asdjkahsdkjhasjdkh"));
        CommandResult result = this.viewCommand.execute("view that nasty todo");
        assertEquals("Cannot find task names with \"that nasty todo\".", result.getFeedback());
    }

    /**
     * Test the result when only one match is found and tasks does not have reminders.
     */
    @Test
    public void testCommandResultWhenExactlyOneTaskWithNoRemindersFound() {
        this.setupTaskWithAndWithoutReminders();
        CommandResult result = this.viewCommand.execute("view Task1");
        assertEquals("Task1" + '\n' + "Reminders:" + '\n' + "You have not set any reminders for this task.", result.getFeedback());
    }

    /**
     * Test the result when only one match is found and task has reminders.
     */
    @Test
    public void testCommandResultWhenExactlyOneTaskWithRemindersFound() {
        this.setupTaskWithAndWithoutReminders();
        CommandResult result = this.viewCommand.execute("view Task2");
        assertEquals("Task2\n" + "Reminders:\n"
            + "1. Attend Workshop 1 (On 2016-01-01 5.00PM)\n"
            + "2. Attend Workshop 2 (On 2016-02-01 5.00PM)\n"
            + "3. Attend Workshop 3 (On 2016-03-01 5.00PM)", result.getFeedback());
    }

    /**
     * Test the result when multiple matches are found.
     */
    @Test
    public void testCommandResultWhenMultipleMatchesFound() {
        CommandResult result = this.setupMultipleHelloTaskAndExecuteAmbiguousCommand();
        assertEquals("Which one? (1-2)\n1. hello it's me\n2. hello from the other side", result.getFeedback());
    }

    /**
     * Test that command is wating user response.
     */
    @Test
    public void testAwaitingUserResponse() {
        assertFalse(this.viewCommand.awaitingUserResponse());
        this.setupMultipleHelloTaskAndExecuteAmbiguousCommand();
        assertTrue(this.viewCommand.awaitingUserResponse());
    }

    /**
     * Test that cancel works properly.
     */
    @Test
    public void testUserResponseCancel() {
        this.setupMultipleHelloTaskAndExecuteAmbiguousCommand();
        CommandResult result = this.viewCommand.getUserResponse("cancel");
        assertEquals("OK! Not viewing any task.", result.getFeedback());
        assertFalse(this.viewCommand.awaitingUserResponse());
    }

    /**
     * Test that reminder is added if user selects a valid index.
     */
    @Test
    public void testUserResponseValidIndex() {
        this.setupMultipleHelloTaskAndExecuteAmbiguousCommand();
        CommandResult result = this.viewCommand.getUserResponse("1");
        assertEquals("hello it's me\nReminders:\nYou have not set any reminders for this task.", result.getFeedback());
        assertFalse(this.viewCommand.awaitingUserResponse());
    }

    /**
     * Test that reminder is not added if user selects an invalid index.
     */
    @Test
    public void testUserResponseInvalidIndex() {
        this.setupMultipleHelloTaskAndExecuteAmbiguousCommand();
        CommandResult result = this.viewCommand.getUserResponse("0");
        String expectedResponse = "That's not a valid index. Enter a number between 1 and 2:\n" +
                "1. hello it's me\n2. hello from the other side";
        assertEquals(expectedResponse, result.getFeedback());
        assertTrue(this.viewCommand.awaitingUserResponse());
    }

    /**
     * Test that reminder is not added if user puts an invalid response.
     */
    @Test
    public void testUserResponseInvalidResponse() {
        this.setupMultipleHelloTaskAndExecuteAmbiguousCommand();
        CommandResult result = this.viewCommand.getUserResponse("notindex");
        String expectedResponse = "I don't understand \"notindex\".\n" + "Enter a number to indicate which task to view.\n" +
                "1. hello it's me\n2. hello from the other side";
        assertEquals(expectedResponse, result.getFeedback());
        assertTrue(this.viewCommand.awaitingUserResponse());
    }
}
