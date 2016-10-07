package linenux.command;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static linenux.helpers.Assert.assertNoChange;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import linenux.command.result.CommandResult;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.util.ArrayListUtil;

/**
 * JUnit test for done command.
 */
public class DoneCommandTest {
    private static final String DONE_PATTERN = "(?i)^done (?<keywords>.*)$";

    private Schedule schedule;
    private DoneCommand doneCommand;

    @Before
    public void setupDeleteCommand() {
        this.schedule = new Schedule();
        this.doneCommand = new DoneCommand(this.schedule);
    }

    /**
     * Get search result when done is executed.
     * Assumes userInput is in correct format and schedule is not null.
     */
    private ArrayList<Task> getSearchResult(String userInput) {
        Matcher matcher = Pattern.compile(DONE_PATTERN).matcher(userInput);

        if (matcher.matches()) {
            String keywords = matcher.group("keywords");
            String[] keywordsArr = keywords.split("\\s+");
            ArrayList<Task> tasks = new ArrayListUtil.ChainableArrayListUtil<Task>(this.schedule.search(keywordsArr))
                                                     .filter(task -> task.isNotDone())
                                                     .value();
            return tasks;
        }

        return null;
    }

    private void setupMultipleHelloTasksAndExecuteAmbiguousCommand() {
        this.schedule.addTask(new Task("hello "));
        this.schedule.addTask(new Task("say hello from the other side"));
        this.doneCommand.execute("done hello");
    }

    @Test
    public void testRespondToDoneCommand() {
        assertTrue(this.doneCommand.respondTo("done hello"));
    }

    @Test
    public void testCaseInsensitiveRespondToDeleteCommand() {
        assertTrue(this.doneCommand.respondTo("dOnE hello"));
    }

    @Test
    public void testDoesNotRespondToOtherCommands() {
        assertFalse(this.doneCommand.respondTo("scooby-dooby-doo"));
    }

    @Test
    public void testCommandResultWhenNoMatchFound() {
        this.schedule.addTask(new Task("Shot through the heart"));
        CommandResult result = assertNoChange(() -> getSearchResult("done and you are to blame").size(),
                () -> this.doneCommand.execute("done and you are to blame"));
        assertEquals("Cannot find \"and you are to blame\".", result.getFeedback());
    }

}
