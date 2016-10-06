package linenux.command;

import linenux.command.result.CommandResult;
import linenux.model.Schedule;
import linenux.model.Task;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by yihangho on 10/5/16.
 */
public class DeleteCommandTest {
    private Schedule schedule;
    private DeleteCommand deleteCommand;

    @Before
    public void setupDeleteCommand() {
        this.schedule = new Schedule();
        this.deleteCommand = new DeleteCommand(this.schedule);
    }

    private void setupMultipleHelloTasksAndExecuteAmbiguousCommand() {
        this.schedule.addTask(new Task("hello world"));
        this.schedule.addTask(new Task("say hello"));
        this.deleteCommand.execute("delete hello");
    }

    @Test
    public void testRespondToDeleteCommand() {
        assertTrue(this.deleteCommand.respondTo("delete hello"));
    }

    @Test
    public void testCaseInsensitiveRespondToDeleteCommand() {
        assertTrue(this.deleteCommand.respondTo("dElEte hello"));
    }

    @Test
    public void testDoesNotRespondToOtherCommands() {
        assertFalse(this.deleteCommand.respondTo("walala"));
    }

    @Test
    public void testCommandResultWhenNoMatchFound() {
        this.schedule.addTask(new Task("flkasdjfaklsdfjaldf"));
        int beforeSize = this.schedule.getTaskList().size();
        CommandResult result = this.deleteCommand.execute("delete that nasty todo");
        int afterSize = this.schedule.getTaskList().size();
        assertEquals("Cannot find \"that nasty todo\".", result.getFeedback());
        assertEquals(beforeSize, afterSize);
    }

    @Test
    public void testCommandResultWhenOnlyOneMatchFound() {
        this.schedule.addTask(new Task("hello"));
        this.schedule.addTask(new Task("i can' type"));
        int beforeSize = this.schedule.getTaskList().size();
        CommandResult result = this.deleteCommand.execute("delete hello");
        int afterSize = this.schedule.getTaskList().size();
        assertEquals("Deleted \"hello\".", result.getFeedback());
        assertEquals(beforeSize - 1, afterSize);
    }

    @Test
    public void testCommandResultWhenMultipleMatchesFound() {
        this.schedule.addTask(new Task("hello world"));
        this.schedule.addTask(new Task("say hello"));
        int beforeSize = this.schedule.getTaskList().size();
        CommandResult result = this.deleteCommand.execute("delete hello");
        int afterSize = this.schedule.getTaskList().size();
        assertEquals("Which one? (1-2)\n1. hello world\n2. say hello\n", result.getFeedback());
        assertEquals(beforeSize, afterSize);
    }

    @Test
    public void testAwaitingUserResponse() {
        assertFalse(this.deleteCommand.awaitingUserResponse());
        this.setupMultipleHelloTasksAndExecuteAmbiguousCommand();
        assertTrue(this.deleteCommand.awaitingUserResponse());
    }

    @Test
    public void testUserResponseCancel() {
        this.setupMultipleHelloTasksAndExecuteAmbiguousCommand();
        int beforeSize = this.schedule.getTaskList().size();
        CommandResult result = this.deleteCommand.userResponse("cancel");
        int afterSize = this.schedule.getTaskList().size();
        assertEquals("OK! Not deleting anything.", result.getFeedback());
        assertEquals(beforeSize, afterSize);
    }

    @Test
    public void testUserResponseValidIndex() {
        this.setupMultipleHelloTasksAndExecuteAmbiguousCommand();
        int beforeSize = this.schedule.getTaskList().size();
        CommandResult result = this.deleteCommand.userResponse("1");
        int afterSize = this.schedule.getTaskList().size();
        assertEquals("Deleted \"hello world\".", result.getFeedback());
        assertEquals(beforeSize - 1, afterSize);
    }

    @Test
    public void testUserResponseInvalidIndex() {
        this.setupMultipleHelloTasksAndExecuteAmbiguousCommand();
        int beforeSize = this.schedule.getTaskList().size();
        CommandResult result = this.deleteCommand.userResponse("0");
        int afterSize = this.schedule.getTaskList().size();
        String expectedResponse = "That's not a valid index. Enter a number between 1 and 2:\n" +
                "1. hello world\n2. say hello\n";
        assertEquals(expectedResponse, result.getFeedback());
        assertEquals(beforeSize, afterSize);
    }

    @Test
    public void testUserResponseInvalidResponse() {
        this.setupMultipleHelloTasksAndExecuteAmbiguousCommand();
        int beforeSize = this.schedule.getTaskList().size();
        CommandResult result = this.deleteCommand.userResponse("roses are red");
        int afterSize = this.schedule.getTaskList().size();
        String expectedResponse = "I don't understand \"roses are red\".\n" +
                "Enter a number to indicate which task to delete.\n1. hello world\n2. say hello\n";
        assertEquals(expectedResponse, result.getFeedback());
        assertEquals(beforeSize, afterSize);
    }
}
