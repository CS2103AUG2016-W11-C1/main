package linenux.command;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import linenux.command.result.CommandResult;
import linenux.model.Schedule;
import linenux.model.Task;

/**
 * JUnit test for done command.
 */
public class DoneCommandTest {
    private Schedule schedule;
    private DoneCommand doneCommand;

    @Before
    public void setupDoneCommand() {
        this.schedule = new Schedule();
        this.doneCommand = new DoneCommand(this.schedule);
    }

    /**
     * Get search result when done is executed.
     * Assumes userInput is in correct format and schedule is not null.
     */
    private ArrayList<Task> getSearchResult(String keywords) {
            String[] keywordsArr = keywords.split("\\s+");
            return this.schedule.search(keywordsArr);
    }

    private void setupMultipleHelloTasksAndExecuteAmbiguousCommand() {
        this.schedule.addTask(new Task("hello world"));
        this.schedule.addTask(new Task("say hello from the other side"));
        this.doneCommand.execute("done hello");
    }

    /**
     * Test that respondTo detects various versions of the commands. It should return true even if
     * the format of the arguments are invalid.
     */
    @Test
    public void testRespondToDoneCommand() {
        assertTrue(this.doneCommand.respondTo("done"));
        assertTrue(this.doneCommand.respondTo("done   "));
        assertTrue(this.doneCommand.respondTo("done hello"));
    }

    /**
     * Test that the delete command is case insensitive.
     */
    @Test
    public void testCaseInsensitiveRespondToDoneCommand() {
        assertTrue(this.doneCommand.respondTo("dOnE hello"));
    }

    /**
     * Test that respondTo will return false for commands not related to delete tasks.
     */
    @Test
    public void testDoesNotRespondToOtherCommands() {
        assertFalse(this.doneCommand.respondTo("scooby-dooby-doo"));
    }

    /**
     * Test the feedback when no match is found.
     */
    @Test
    public void testCommandResultWhenNoMatchFound() {
        this.schedule.addTask(new Task("Shot through the heart"));
        CommandResult result = this.doneCommand.execute("done and you are to blame");
        assertEquals("Cannot find task names with \"and you are to blame\".", result.getFeedback());
    }

    /**
     * Test the feedback when only one match is found.
     */
    @Test
    public void testCommandResultWhenOnlyOneMatchFound() {
        this.schedule.addTask(new Task("Live like we are dying"));
        this.schedule.addTask(new Task("Play on broken strings"));

        ArrayList<Task> taskList = getSearchResult("live");
        assertFalse(taskList.get(0).isDone());
        CommandResult result = this.doneCommand.execute("done live");
        assertEquals("\"Live like we are dying\" is marked as done.", result.getFeedback());

        taskList = getSearchResult("live");
        assertTrue(taskList.get(0).isDone());
    }

    /**
     * Test the feedback when multiple matches are found.
     */
    @Test
    public void testCommandResultWhenMultipleMatchesFound() {
        this.schedule.addTask(new Task("hello world"));
        this.schedule.addTask(new Task("say hello"));
        CommandResult result = this.doneCommand.execute("done hello");
        assertEquals("Which one? (1-2)\n1. hello world\n2. say hello", result.getFeedback());
    }

    /**
     * Test the command is awaiting user response when multiple matches are found.
     */
    @Test
    public void testAwaitingUserResponse() {
        assertFalse(this.doneCommand.awaitingUserResponse());
        this.setupMultipleHelloTasksAndExecuteAmbiguousCommand();
        assertTrue(this.doneCommand.awaitingUserResponse());
    }

    /**
     * Test that cancel works properly.
     */
    @Test
    public void testUserResponseCancel() {
        this.setupMultipleHelloTasksAndExecuteAmbiguousCommand();
        CommandResult result = this.doneCommand.userResponse("cancel");
        assertEquals("OK! Not marking any task as done.", result.getFeedback());
        assertFalse(this.doneCommand.awaitingUserResponse());
    }

    /**
     * Test that task is marked as done if user selects a valid index.
     */
    @Test
    public void testUserResponseValidIndex() {
        this.setupMultipleHelloTasksAndExecuteAmbiguousCommand();
        ArrayList<Task> taskList = getSearchResult("hello");
        assertFalse(taskList.get(0).isDone());

        CommandResult result = this.doneCommand.userResponse("1");
        assertEquals("\"hello world\" is marked as done.", result.getFeedback());
        taskList = getSearchResult("hello");
        assertTrue(taskList.get(0).isDone());

        assertFalse(this.doneCommand.awaitingUserResponse());
    }

    /**
     * Test that task is not marked as done if user selects an invalid index.
     */
    @Test
    public void testUserResponseInvalidIndex() {
        this.setupMultipleHelloTasksAndExecuteAmbiguousCommand();
        ArrayList<Task> taskList = getSearchResult("hello");
        assertFalse(taskList.get(0).isDone());

        CommandResult result = this.doneCommand.userResponse("0");
        String expectedResponse = "That's not a valid index. Enter a number between 1 and 2:\n" +
                "1. hello world\n2. say hello from the other side";
        assertEquals(expectedResponse, result.getFeedback());
        taskList = getSearchResult("hello");
        assertFalse(taskList.get(0).isDone());

        assertTrue(this.doneCommand.awaitingUserResponse());
    }

    /**
     * Test that task is not marked as done if user types an invalid response.
     */
    @Test
    public void testUserResponseInvalidResponse() {
        this.setupMultipleHelloTasksAndExecuteAmbiguousCommand();
        ArrayList<Task> taskList = getSearchResult("hello");
        assertFalse(taskList.get(0).isDone());

        CommandResult result = this.doneCommand.userResponse("roses are red");
        String expectedResponse = "I don't understand \"roses are red\".\n" +
                "Enter a number to indicate which task to mark as done.\n1. hello world\n2. say hello from the other side";
        assertEquals(expectedResponse, result.getFeedback());
        taskList = getSearchResult("hello");
        assertFalse(taskList.get(0).isDone());

        assertTrue(this.doneCommand.awaitingUserResponse());
    }

    @Test
    public void testSearchOnlyUndoneTasks() {
        this.schedule.addTask(new Task("hello"));
        this.schedule.addTask(new Task("hello").markAsDone());

        CommandResult result = this.doneCommand.execute("done hello");
        String expectedResponse = "\"hello\" is marked as done.";

        assertEquals(expectedResponse, result.getFeedback());
    }
}
