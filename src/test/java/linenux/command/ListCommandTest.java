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

    //@@author A0144915A
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

    //@@author A0140702X
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

    //@@author A0127694U
    @Test
    public void testNoMatchingKeywords() {
        this.schedule.addTask(new Task("hi!"));

        CommandResult result = this.listCommand.execute("list hello");

        String expectedFeedback = "There are no tasks and reminders found based on your given inputs!";
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

    //@@author A0140702X
    /**
     * Test that list command filters by start time
     */
    @Test
    public void testFilterTaskAndRemindersByStartTime() {
        Task todo = new Task("todo");
        todo = todo.addReminder(new Reminder("todo before", LocalDateTime.of(2015, 1, 1, 17, 0)));
        todo = todo.addReminder(new Reminder("todo after", LocalDateTime.of(2017, 1, 1, 17, 0)));
        todo = todo.addReminder(new Reminder("todo on", LocalDateTime.of(2016, 1, 1, 17, 0)));

        Task eventBefore = new Task("event before", LocalDateTime.of(2015, 1, 1, 17, 0),
                LocalDateTime.of(2015, 1, 1, 19, 0));
        Task eventOn = new Task("event on", LocalDateTime.of(2015, 1, 1, 17, 0), LocalDateTime.of(2016, 1, 1, 17, 0));
        Task eventAfter = new Task("event after", LocalDateTime.of(2015, 1, 1, 17, 0),
                LocalDateTime.of(2017, 1, 1, 17, 0));

        Task deadlineBefore = new Task("deadline before", LocalDateTime.of(2015, 1, 1, 17, 0));
        Task deadlineOn = new Task("deadline On", LocalDateTime.of(2016, 1, 1, 17, 0));
        Task deadlineAfter = new Task("deadline before", LocalDateTime.of(2017, 1, 1, 17, 0));

        this.schedule.addTask(todo);
        this.schedule.addTask(eventBefore);
        this.schedule.addTask(eventOn);
        this.schedule.addTask(eventAfter);
        this.schedule.addTask(deadlineBefore);
        this.schedule.addTask(deadlineOn);
        this.schedule.addTask(deadlineAfter);

        CommandResult result = this.listCommand.execute("list st/2016-01-01 5:00PM");

        ArrayList<Task> filteredTasks = this.schedule.getFilteredTasks();

        assertTrue(filteredTasks.contains(todo));
        assertTrue(!filteredTasks.contains(eventBefore));
        assertTrue(filteredTasks.contains(eventOn));
        assertTrue(filteredTasks.contains(eventAfter));
        assertTrue(!filteredTasks.contains(deadlineBefore));
        assertTrue(filteredTasks.contains(deadlineOn));
        assertTrue(filteredTasks.contains(deadlineAfter));

        String expectedFeedback = "Reminders:\n" + "1. todo on (On 2016-01-01 5:00PM)\n"
                + "2. todo after (On 2017-01-01 5:00PM)";
        assertEquals(expectedFeedback, result.getFeedback());
    }

    /**
     * Test that list command filters by end time
     */
    @Test
    public void testFilterTaskAndRemindersByEndTime() {
        Task todo = new Task("todo");
        todo = todo.addReminder(new Reminder("todo before", LocalDateTime.of(2015, 1, 1, 17, 0)));
        todo = todo.addReminder(new Reminder("todo after", LocalDateTime.of(2017, 1, 1, 17, 0)));
        todo = todo.addReminder(new Reminder("todo on", LocalDateTime.of(2016, 1, 1, 17, 0)));

        Task eventBefore = new Task("event before", LocalDateTime.of(2015, 1, 1, 17, 0),
                LocalDateTime.of(2015, 1, 1, 19, 0));
        Task eventOn = new Task("event on", LocalDateTime.of(2015, 1, 1, 17, 0), LocalDateTime.of(2016, 1, 1, 17, 0));
        Task eventEndTimeAfter = new Task("event after", LocalDateTime.of(2015, 1, 1, 19, 0),
                LocalDateTime.of(2017, 1, 1, 17, 0));
        Task eventStartTimeAfter = new Task("event after", LocalDateTime.of(2017, 1, 1, 19, 0),
                LocalDateTime.of(2018, 1, 1, 17, 0));

        Task deadlineBefore = new Task("deadline before", LocalDateTime.of(2015, 1, 1, 17, 0));
        Task deadlineOn = new Task("deadline On", LocalDateTime.of(2016, 1, 1, 17, 0));
        Task deadlineAfter = new Task("deadline after", LocalDateTime.of(2017, 1, 1, 17, 0));

        this.schedule.addTask(todo);
        this.schedule.addTask(eventBefore);
        this.schedule.addTask(eventOn);
        this.schedule.addTask(eventEndTimeAfter);
        this.schedule.addTask(eventStartTimeAfter);
        this.schedule.addTask(deadlineBefore);
        this.schedule.addTask(deadlineOn);
        this.schedule.addTask(deadlineAfter);

        CommandResult result = this.listCommand.execute("list et/2016-01-01 5:00PM");

        ArrayList<Task> filteredTasks = this.schedule.getFilteredTasks();

        assertTrue(filteredTasks.contains(todo));
        assertTrue(filteredTasks.contains(eventBefore));
        assertTrue(filteredTasks.contains(eventOn));
        assertTrue(filteredTasks.contains(eventEndTimeAfter));
        assertTrue(!filteredTasks.contains(eventStartTimeAfter));
        assertTrue(filteredTasks.contains(deadlineBefore));
        assertTrue(filteredTasks.contains(deadlineOn));
        assertTrue(!filteredTasks.contains(deadlineAfter));

        String expectedFeedback = "Reminders:\n" + "1. todo before (On 2015-01-01 5:00PM)\n"
                + "2. todo on (On 2016-01-01 5:00PM)";
        assertEquals(expectedFeedback, result.getFeedback());
    }

    /**
     * Test that list command filters by tags
     */
    @Test
    public void testFilterTaskAndRemindersByTags() {
        ArrayList<String> tags1 = new ArrayList<>();
        ArrayList<String> tags2 = new ArrayList<>();
        ArrayList<String> tags3 = new ArrayList<>();

        tags1.add("hello");
        tags2.add("hello");
        tags2.add("world");
        tags3.add("wat");

        Task todo1 = new Task("todo 1", tags1);
        Task todo2 = new Task("todo 2", tags2);
        Task todo3 = new Task("todo 3", tags3);

        this.schedule.addTask(todo1);
        this.schedule.addTask(todo2);
        this.schedule.addTask(todo3);

        this.listCommand.execute("list #/hello");

        ArrayList<Task> filteredTasks = this.schedule.getFilteredTasks();

        assertTrue(filteredTasks.contains(todo1));
        assertTrue(filteredTasks.contains(todo2));
        assertTrue(!filteredTasks.contains(todo3));
    }

    /**
     * Test that list command filters by tags is case-insensitive
     */
    @Test
    public void testFilterTaskAndRemindersByTagsCaseInsensitive() {
        ArrayList<String> tags1 = new ArrayList<>();
        ArrayList<String> tags2 = new ArrayList<>();
        ArrayList<String> tags3 = new ArrayList<>();

        tags1.add("hello");
        tags2.add("hello");
        tags2.add("world");
        tags3.add("wat");

        Task todo1 = new Task("todo 1", tags1);
        Task todo2 = new Task("todo 2", tags2);
        Task todo3 = new Task("todo 3", tags3);

        this.schedule.addTask(todo1);
        this.schedule.addTask(todo2);
        this.schedule.addTask(todo3);

        this.listCommand.execute("list #/hElLo");

        ArrayList<Task> filteredTasks = this.schedule.getFilteredTasks();

        assertTrue(filteredTasks.contains(todo1));
        assertTrue(filteredTasks.contains(todo2));
        assertTrue(!filteredTasks.contains(todo3));
    }

    /**
     * Test that list command field d/yes (view done only)
     */
    @Test
    public void testFilterTaskAndRemindersByDoneOnly() {
        Task todo1 = new Task("todo 1");
        Task todo2 = new Task("todo 2");

        todo1 = todo1.markAsDone();

        this.schedule.addTask(todo1);
        this.schedule.addTask(todo2);

        CommandResult result = this.listCommand.execute("list d/yes");

        ArrayList<Task> filteredTasks = this.schedule.getFilteredTasks();

        assertTrue(filteredTasks.contains(todo1));
        assertTrue(!filteredTasks.contains(todo2));

        String expectedFeedback = "1. todo 1";
        assertEquals(expectedFeedback, result.getFeedback());
    }

    /**
     * Test that list command field d/all (view all including done)
     */
    @Test
    public void testViewTaskAndRemindersIncludingDone() {
        Task todo1 = new Task("todo 1");
        Task todo2 = new Task("todo 2");

        todo1 = todo1.markAsDone();

        this.schedule.addTask(todo1);
        this.schedule.addTask(todo2);

        CommandResult result = this.listCommand.execute("list d/all");

        ArrayList<Task> filteredTasks = this.schedule.getFilteredTasks();

        assertTrue(filteredTasks.contains(todo1));
        assertTrue(filteredTasks.contains(todo2));

        String expectedFeedback = "1. todo 1";
        assertEquals(expectedFeedback, result.getFeedback());
    }

    /**
     * Test that list command field d/ when invalid
     */
    @Test
    public void testInvalidDoneField() {
        Task todo = new Task("todo");

        this.schedule.addTask(todo);

        CommandResult result = this.listCommand.execute("list d/invalid");

        String expectedFeedback = "Unable to parse \"invalid\".\n" + "Did you mean:\n"
                + "d/all - View all done and uncompleted tasks.\n" + "d/yes - Show only tasks that are marked done.";
        assertEquals(expectedFeedback, result.getFeedback());
    }
}
