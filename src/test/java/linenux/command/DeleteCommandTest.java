package linenux.command;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static linenux.helpers.Assert.assertChangeBy;
import static linenux.helpers.Assert.assertNoChange;

import org.junit.Before;
import org.junit.Test;

import linenux.command.result.CommandResult;
import linenux.model.Schedule;
import linenux.model.Task;

/**
 * JUnit test for delete command.
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
        CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
                () -> this.deleteCommand.execute("delete that nasty todo"));
        assertEquals("Cannot find \"that nasty todo\".", result.getFeedback());
    }

    @Test
    public void testCommandResultWhenOnlyOneMatchFound() {
        this.schedule.addTask(new Task("hello"));
        this.schedule.addTask(new Task("i can' type"));
        CommandResult result = assertChangeBy(() -> this.schedule.getTaskList().size(),
                -1,
                () -> this.deleteCommand.execute("delete hello"));
        assertEquals("Deleted \"hello\".", result.getFeedback());
    }

    @Test
    public void testCommandResultWhenMultipleMatchesFound() {
        this.schedule.addTask(new Task("hello world"));
        this.schedule.addTask(new Task("say hello"));
        CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
                () -> this.deleteCommand.execute("delete hello"));
        assertEquals("Which one? (1-2)\n1. hello world\n2. say hello", result.getFeedback());
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
        CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
                () -> this.deleteCommand.userResponse("cancel"));
        assertEquals("OK! Not deleting anything.", result.getFeedback());
        assertFalse(this.deleteCommand.awaitingUserResponse());
    }

    @Test
    public void testUserResponseValidIndex() {
        this.setupMultipleHelloTasksAndExecuteAmbiguousCommand();
        CommandResult result = assertChangeBy(() -> this.schedule.getTaskList().size(),
                -1,
                () -> this.deleteCommand.userResponse("1"));
        assertEquals("Deleted \"hello world\".", result.getFeedback());
    }

    @Test
    public void testUserResponseInvalidIndex() {
        this.setupMultipleHelloTasksAndExecuteAmbiguousCommand();
        CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
                () -> this.deleteCommand.userResponse("0"));
        String expectedResponse = "That's not a valid index. Enter a number between 1 and 2:\n" +
                "1. hello world\n2. say hello";
        assertEquals(expectedResponse, result.getFeedback());
    }

    @Test
    public void testUserResponseInvalidResponse() {
        this.setupMultipleHelloTasksAndExecuteAmbiguousCommand();
        CommandResult result = assertNoChange(() -> this.schedule.getTaskList().size(),
                () -> this.deleteCommand.userResponse("roses are red"));
        String expectedResponse = "I don't understand \"roses are red\".\n" +
                "Enter a number to indicate which task to delete.\n1. hello world\n2. say hello";
        assertEquals(expectedResponse, result.getFeedback());
    }
}
