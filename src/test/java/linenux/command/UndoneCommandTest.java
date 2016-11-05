package linenux.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import linenux.command.result.CommandResult;
import linenux.model.Schedule;
import linenux.model.Task;

//@@author A0140702X
public class UndoneCommandTest {
    private Schedule schedule;
    private UndoneCommand undoneCommand;

    @Before
    public void setupUndoneCommand() {
        this.schedule = new Schedule();
        this.undoneCommand = new UndoneCommand(this.schedule);
    }

    /**
     * Get search result when done is executed. Assumes userInput is in correct
     * format and schedule is not null.
     */
    private ArrayList<Task> getSearchResult(String keywords) {
        return this.schedule.search(keywords);
    }

    private void setupMultipleHelloTasksAndExecuteAmbiguousCommand() {
        Task task1 = new Task("hello world");
        Task task2 = new Task("say hello from the other side");

        task1 = task1.markAsDone();
        task2 = task2.markAsDone();

        this.schedule.addTask(task1);
        this.schedule.addTask(task2);
        this.undoneCommand.execute("undone hello");
    }

    /**
     * Test that respondTo detects various versions of the commands. It should
     * return true even if the format of the arguments are invalid.
     */
    @Test
    public void testRespondToUndoneCommand() {
        assertTrue(this.undoneCommand.respondTo("undone"));
        assertTrue(this.undoneCommand.respondTo("undone       "));
        assertTrue(this.undoneCommand.respondTo("undone hey"));
    }

    /**
     * Test that the undone command is case insensitive.
     */
    @Test
    public void testCaseInsensitiveRespondToUndoneCommand() {
        assertTrue(this.undoneCommand.respondTo("uNdOnE hello"));
    }

    /**
     * Test that respondTo will return false for commands not related to undone.
     */
    @Test
    public void testDoesNotRespondToOtherCommands() {
        assertFalse(this.undoneCommand.respondTo("donedone"));
    }

    /**
     * Test the feedback when no match is found.
     */
    @Test
    public void testCommandResultWhenNoMatchFound() {
        this.schedule.addTask(new Task("Shot through the heart"));
        CommandResult result = this.undoneCommand.execute("undone and you are to blame");
        assertEquals("Cannot find task names with \"and you are to blame\".", result.getFeedback());
    }

    /**
     * Test the feedback when only one match is found.
     */
    @Test
    public void testCommandResultWhenOnlyOneMatchFound() {
        Task task1 = new Task("Live like we are dying");
        task1 = task1.markAsDone();
        this.schedule.addTask(task1);
        this.schedule.addTask(new Task("Play on broken strings"));

        ArrayList<Task> taskList = getSearchResult("live");
        assertFalse(taskList.get(0).isNotDone());
        CommandResult result = this.undoneCommand.execute("undone live");
        assertEquals("\"Live like we are dying\" is marked as undone.", result.getFeedback());

        taskList = getSearchResult("live");
        assertTrue(taskList.get(0).isNotDone());
    }

    /**
     * Test the feedback when multiple matches are found.
     */
    @Test
    public void testCommandResultWhenMultipleMatchesFound() {
        Task task1 = new Task("hello world");
        Task task2 = new Task("say hello");

        task1 = task1.markAsDone();
        task2 = task2.markAsDone();

        this.schedule.addTask(task1);
        this.schedule.addTask(task2);
        CommandResult result = this.undoneCommand.execute("undone hello");
        assertEquals("Which one? (1-2, \"cancel\" to cancel the current operation)\n1. hello world\n2. say hello", result.getFeedback());
    }

    /**
     * Test the command is awaiting user response when multiple matches are
     * found.
     */
    @Test
    public void testAwaitingUserResponse() {
        assertFalse(this.undoneCommand.isAwaitingUserResponse());
        this.setupMultipleHelloTasksAndExecuteAmbiguousCommand();
        assertTrue(this.undoneCommand.isAwaitingUserResponse());
    }

    /**
     * Test that cancel works properly.
     */
    @Test
    public void testUserResponseCancel() {
        this.setupMultipleHelloTasksAndExecuteAmbiguousCommand();
        CommandResult result = this.undoneCommand.processUserResponse("cancel");
        assertEquals("OK! Not marking any task as undone.", result.getFeedback());
        assertFalse(this.undoneCommand.isAwaitingUserResponse());
    }

    /**
     * Test that task is marked as done if user selects a valid index.
     */
    @Test
    public void testUserResponseValidIndex() {
        this.setupMultipleHelloTasksAndExecuteAmbiguousCommand();
        ArrayList<Task> taskList = getSearchResult("hello");
        assertFalse(taskList.get(0).isNotDone());

        CommandResult result = this.undoneCommand.processUserResponse("1");
        assertEquals("\"hello world\" is marked as undone.", result.getFeedback());
        taskList = getSearchResult("hello");
        assertTrue(taskList.get(0).isNotDone());

        assertFalse(this.undoneCommand.isAwaitingUserResponse());
    }

    /**
     * Test that task is not marked as done if user selects an invalid index.
     */
    @Test
    public void testUserResponseInvalidIndex() {
        this.setupMultipleHelloTasksAndExecuteAmbiguousCommand();
        ArrayList<Task> taskList = getSearchResult("hello");
        assertFalse(taskList.get(0).isNotDone());

        CommandResult result = this.undoneCommand.processUserResponse("0");
        String expectedResponse = "That's not a valid index. Enter a number between 1 and 2, or \"cancel\" to cancel the current operation:\n"
                + "1. hello world\n2. say hello from the other side";
        assertEquals(expectedResponse, result.getFeedback());
        taskList = getSearchResult("hello");
        assertFalse(taskList.get(0).isNotDone());

        assertTrue(this.undoneCommand.isAwaitingUserResponse());
    }

    /**
     * Test that task is not marked as done if user types an invalid response.
     */
    @Test
    public void testUserResponseInvalidResponse() {
        this.setupMultipleHelloTasksAndExecuteAmbiguousCommand();
        ArrayList<Task> taskList = getSearchResult("hello");
        assertFalse(taskList.get(0).isNotDone());

        CommandResult result = this.undoneCommand.processUserResponse("roses are red");
        String expectedResponse = "I don't understand \"roses are red\".\n"
                + "Enter a number to indicate which task to mark as undone.\n1. hello world\n2. say hello from the other side";
        assertEquals(expectedResponse, result.getFeedback());
        taskList = getSearchResult("hello");
        assertFalse(taskList.get(0).isNotDone());

        assertTrue(this.undoneCommand.isAwaitingUserResponse());
    }

    /**
     * Test searching only searches for done tasks.
     */

    @Test
    public void testSearchOnlyDoneTasks() {
        this.schedule.addTask(new Task("hello"));
        this.schedule.addTask(new Task("hello", LocalDateTime.of(2017, 1, 1, 17, 0)).markAsDone());

        CommandResult result = this.undoneCommand.execute("undone hello");
        String expectedResponse = "\"hello\" is marked as undone.";

        assertEquals(expectedResponse, result.getFeedback());
    }
}
