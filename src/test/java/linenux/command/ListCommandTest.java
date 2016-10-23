package linenux.command;

import static junit.framework.TestCase.assertEquals;
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
 * JUnit test for list command.
 */
public class ListCommandTest {
    private Schedule schedule;
    private ListCommand listCommand;

    @Before
    public void setupListCommand() {
        this.schedule = new Schedule();
        this.listCommand = new ListCommand(this.schedule);
    }

    /**
     * Test list all.
     */
    @Test
    public void testRespondToListWithoutParams() {
        assertTrue(this.listCommand.respondTo("list"));
    }

    /**
     * Test search function in list.
     */
    @Test
    public void testRespondToListWithKeywords() {
        assertTrue(this.listCommand.respondTo("list bla"));
    }

    /**
     * Test that list command is case insenstive.
     */
    @Test
    public void testCaseInsensitiveRespondToList() {
        assertTrue(this.listCommand.respondTo("LiSt"));
    }

    /**
     * Test that list command does not respond to other commands.
     */
    @Test
    public void testDoesNotRespondToOtherCommands() {
        assertFalse(this.listCommand.respondTo("whaddup"));
    }

    /**
     * Test that list without params should display all tasks and reminders
     */
    @Test
    public void testDisplayTheEntireList() {
        this.schedule.addTask(new Task("First Task"));
        this.schedule.addTask(new Task("Second Task"));
        this.schedule.addTask(new Task("Deadline", null, LocalDateTime.of(2016, 1, 1, 17, 0)));
        this.schedule.addTask(new Task("Event", LocalDateTime.of(2016, 1, 1, 17, 0), LocalDateTime.of(2016, 1, 1, 18, 0)));

        Task taskWithReminder = new Task("Task with Reminder");
        taskWithReminder = taskWithReminder.addReminder(new Reminder("Reminder", LocalDateTime.of(2016, 2, 1, 17, 0)));
        this.schedule.addTask(taskWithReminder);

        CommandResult result = this.listCommand.execute("list");

        String expectedFeedback = "Reminders:\n"
                + "1. Reminder (On 2016-02-01 5:00PM)";
        assertEquals(expectedFeedback, result.getFeedback());
    }

    /**
     * Test that list command displays multiple tasks correctly.
     */
    @Test
    public void testDisplayTasksMatchingKeywords() {
        Task task1 = new Task("hello");
        Task task2 = new Task("world");
        Task task3 = new Task("hello world");
        this.schedule.addTask(task1);
        this.schedule.addTask(task2);
        this.schedule.addTask(task3);

        this.listCommand.execute("list world");
        assertTrue(
                this.schedule.getFilteredTasks().contains(task2) && this.schedule.getFilteredTasks().contains(task3));
    }

    @Test
    public void testNoMatchingKeywords() {
        this.schedule.addTask(new Task("hi!"));

        CommandResult result = this.listCommand.execute("list hello");

        String expectedFeedback = "Cannot find task or reminder names with \"hello\".";
        assertEquals(expectedFeedback, result.getFeedback());
    }

    /**
     * Test that list command displays multiple reminders correctly.
     */
    @Test
    public void testDisplayRemindersMatchingKeywords() {
        Task hello = new Task("hello");
        hello = hello.addReminder(new Reminder("world domination", LocalDateTime.of(2016, 2, 1, 17, 0)));
        hello = hello.addReminder(new Reminder("is my occupation", LocalDateTime.of(2016, 1, 1, 17, 0)));
        hello = hello.addReminder(new Reminder("hello world", LocalDateTime.of(2016, 3, 1, 17, 0)));
        this.schedule.addTask(hello);

        CommandResult result = this.listCommand.execute("list world");

        ArrayList<Task> filteredTasks = this.schedule.getFilteredTasks();
        assertTrue(!filteredTasks.contains(hello));

        String expectedFeedback = "Reminders:\n1. world domination (On 2016-02-01 5:00PM)\n2. hello world (On 2016-03-01 5:00PM)";
        assertEquals(expectedFeedback, result.getFeedback());
    }

    /**
     * Test that list command displays multiple reminders and tasks correctly.
     */
    @Test
    public void testDisplayTaskAndRemindersMatchingKeywords() {
        Task hello = new Task("hello");
        hello = hello.addReminder(new Reminder("world domination", LocalDateTime.of(2016, 2, 1, 17, 0)));
        hello = hello.addReminder(new Reminder("is my occupation", LocalDateTime.of(2016, 1, 1, 17, 0)));
        hello = hello.addReminder(new Reminder("hello world", LocalDateTime.of(2016, 3, 1, 17, 0)));
        hello = hello.addReminder(new Reminder("hello darkness", LocalDateTime.of(2016, 4, 1, 17, 0)));
        this.schedule.addTask(hello);

        Task helloWorld = new Task("Hello World");
        helloWorld = helloWorld.addReminder(new Reminder("hello hello", LocalDateTime.of(2016, 1, 1, 17, 0)));
        this.schedule.addTask(helloWorld);

        CommandResult result = this.listCommand.execute("list hello");

        String expectedFeedback = "Reminders:\n" + "1. hello hello (On 2016-01-01 5:00PM)\n"
                + "2. hello world (On 2016-03-01 5:00PM)\n" + "3. hello darkness (On 2016-04-01 5:00PM)";
        assertEquals(expectedFeedback, result.getFeedback());
    }
}
