package linenux.command;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static linenux.helpers.Assert.assertChangeBy;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import linenux.command.result.CommandResult;
import linenux.model.Schedule;
import linenux.model.Task;

//@@author A0140702X
/**
 * JUnit test for clear command.
 */
public class ClearCommandTest {
    private Schedule schedule;
    private ClearCommand clearCommand;
    private Task task1;
    private Task task2;
    private Task task3;
    private ArrayList<String> tags;

    @Before
    public void setupListCommand() {
        this.schedule = new Schedule();
        this.clearCommand = new ClearCommand(this.schedule);

        this.tags = new ArrayList<>();
        tags.add("hello");

        this.task1 = new Task("Task 1");
        this.task2 = new Task("Task 2");
        this.task3 = new Task("Task 3", tags);

        this.task2 = this.task2.markAsDone();

        this.schedule.addTask(task1);
        this.schedule.addTask(task2);
        this.schedule.addTask(task3);
    }

    /**
     * Test that command responds to the correct format.
     */
    @Test
    public void testRespondToViewWithKeywords() {
        assertTrue(this.clearCommand.respondTo("clear #/hashtag"));
    }

    /**
     * Test that command is case insensitive.
     */
    @Test
    public void testCaseInsensitiveRespondToView() {
        assertTrue(this.clearCommand.respondTo("ClEaR #/hashtag"));
    }

    /**
     * Test that command does not respond to other commands.
     */
    @Test
    public void testDoesNotRespondToOtherCommands() {
        assertFalse(this.clearCommand.respondTo("notclear"));
    }

    /**
     * Test the result when there are no tasks to clear.
     */
    @Test
    public void testCommandResultWhenNoTaskToClear() {
        this.schedule.clear();
        CommandResult result = this.clearCommand.execute("clear");
        assertEquals("There are no done tasks to clear!", result.getFeedback());
    }

    /**
     * Test the result when there are tasks to clear.
     */
    @Test
    public void testCommandResultWhenWithTaskToClear() {
        assertChangeBy(() -> this.schedule.getTaskList().size(),
            -1,
            () -> this.clearCommand.execute("clear"));

        //Ensure that the correct task is cleared
        ArrayList<Task> tasks = this.schedule.getTaskList();
        assertTrue(!tasks.contains(this.task2));
    }

    /**
     * Test that command clears the result with hashtag.
     */
    @Test
    public void testClearWithHashtag() {
        assertChangeBy(() -> this.schedule.getTaskList().size(), -1, () -> this.clearCommand.execute("clear #/hello"));

        // Ensure that the correct task is cleared
        ArrayList<Task> tasks = this.schedule.getTaskList();
        assertTrue(!tasks.contains(this.task3));
    }
}
