package linenux.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import linenux.model.Schedule;
import linenux.model.Task;
import linenux.util.ArrayListUtil;

//@@author A0127694U
/**
 * JUnit test for rename command.
 */
public class RenameCommandTest {
    private Schedule schedule;
    private RenameCommand renameCommand;

    @Before
    public void setUpRenameCommand() {
        this.schedule = new Schedule();
        this.renameCommand = new RenameCommand(this.schedule);
    }

    private void setUpSetOfTasksWithSameTags() {
        schedule.addTask(new Task("hello", ArrayListUtil.fromArray(new String[]{"adele", "happy"})));
        schedule.addTask(new Task("goodbye", ArrayListUtil.fromArray(new String[]{"adele", "tragedy"})));
        schedule.addTask(new Task("hugs", ArrayListUtil.fromArray(new String[]{"adele"})));
    }

    /**
     * Test that respondTo detects various versions of the commands. It should return true even if
     * the format of the arguments are invalid.
     */
    @Test
    public void respondTo_inputThatBeginsWithRename_trueReturned() {
        assertTrue(this.renameCommand.respondTo("rename"));
        assertTrue(this.renameCommand.respondTo("rename #/"));
        assertTrue(this.renameCommand.respondTo("rename #/hi #/bye"));
    }

    /**
     * Test that respondTo will return false for commands not related to add tasks.
     */
    @Test
    public void respondTo_otherCommands_falseReturned() {
        assertFalse(this.renameCommand.respondTo("halp"));
    }

    /**
     * Test invalid command format.
     */
    @Test
    public void execute_invalidArgument_commandResultReturned() {
        assertEquals(expectedInvalidArgumentMessage(), this.renameCommand.execute("rename hi #/").getFeedback());
        assertEquals(expectedInvalidArgumentMessage(), this.renameCommand.execute("rename hi ").getFeedback());
        assertEquals(expectedInvalidArgumentMessage(), this.renameCommand.execute("rename").getFeedback());
    }

    /**
     * Test rename searches for case insensitive.
     */
    @Test
    public void execute_caseInsensitiveKeywords_correctTagIsChosen() {
        schedule.addTask(new Task("hugs", ArrayListUtil.fromSingleton("aDele")));
        schedule.addTask(new Task("punches", ArrayListUtil.fromSingleton("adele")));
        assertEquals("Edited tag \"adele\".\nNew tag name: swift", this.renameCommand.execute("rename adele #/swift").getFeedback());

        ArrayList<Task> taskList = this.schedule.getTaskList();
        assertTrue(taskList.get(0).getTags().contains("swift"));
        assertTrue(taskList.get(1).getTags().contains("swift"));
    }

    /**
     * Test execute no such tag in schedule.
     */
    @Test
    public void execute_tagNotFound_commandResultReturned() {
        setUpSetOfTasksWithSameTags();
        assertEquals("Cannot find tasks with tag \"hi\".", this.renameCommand.execute("rename hi #/bye").getFeedback());
    }

    /**
     * Test execute successfully renaming tags.
     */
    @Test
    public void execute_validInput_tagsRenamed() {
        setUpSetOfTasksWithSameTags();
        assertEquals("Edited tag \"adele\".\nNew tag name: swift", this.renameCommand.execute("rename adele #/swift").getFeedback());
        ArrayList<Task> taskList = this.schedule.getTaskList();

        assertTrue(taskList.get(0).getTags().contains("swift"));
        assertTrue(taskList.get(1).getTags().contains("swift"));
        assertTrue(taskList.get(2).getTags().contains("swift"));

        assertTrue(taskList.get(0).getTags().contains("happy"));
        assertTrue(taskList.get(1).getTags().contains("tragedy"));
    }

    private String expectedInvalidArgumentMessage() {
        return "Invalid arguments.\n\n" + RenameCommand.COMMAND_FORMAT + "\n\n" + Command.CALLOUTS;
    }
}
