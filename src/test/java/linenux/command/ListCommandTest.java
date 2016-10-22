package linenux.command;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import linenux.command.result.CommandResult;
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
     * Test that list command does not respons to other commands.
     */
    @Test
    public void testDoesNotRespondToOtherCommands() {
        assertFalse(this.listCommand.respondTo("whaddup"));
    }

    /**
     * Test that list without params should display all tasks
     */
    @Test
    public void testDisplayTheEntireList() {
        this.schedule.addTask(new Task("First Task"));
        this.schedule.addTask(new Task("Second Task"));
        this.schedule.addTask(new Task("Deadline", null, LocalDateTime.of(2016, 1, 1, 17, 0)));
        this.schedule.addTask(new Task("Event", LocalDateTime.of(2016, 1, 1, 17, 0), LocalDateTime.of(2016, 1, 1, 18, 0)));

        CommandResult result = this.listCommand.execute("list");

        String expectedFeedback = "Tasks:\n1. First Task\n" +
                "2. Second Task\n" +
                "3. Deadline (Due 2016-01-01 5:00PM)\n" +
                "4. Event (2016-01-01 5:00PM - 2016-01-01 6:00PM)";
        assertEquals(expectedFeedback, result.getFeedback());
    }

    /**
     * Test that list command displays multiple tasks correctly.
     */
    @Test
    public void testDisplayTasksMatchingKeywords() {
        this.schedule.addTask(new Task("hello"));
        this.schedule.addTask(new Task("world"));
        this.schedule.addTask(new Task("hello world"));

        CommandResult result = this.listCommand.execute("list world");

        String expectedFeedback = "Tasks:\n1. world\n2. hello world";
        assertEquals(expectedFeedback, result.getFeedback());
    }
}
