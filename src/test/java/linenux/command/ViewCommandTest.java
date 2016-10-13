package linenux.command;

import static org.junit.Assert.assertEquals;
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
public class ViewCommandTest {
    private Schedule schedule;
    private ViewCommand viewCommand;

    @Before
    public void setupListCommand() {
        this.schedule = new Schedule();
        this.viewCommand = new ViewCommand(this.schedule);
    }

    public void setupTaskWithAndWithoutReminders() {
        this.schedule.addTask(new Task("Task"));
        this.schedule.addTask(new Task("Found"));
        ArrayList<Task> tasks = this.schedule.getTaskList();
        ArrayList<Reminder> reminders = tasks.get(1).getReminders();
        reminders.add(new Reminder("Attend Workshop 1", LocalDateTime.of(2016, 1, 1, 17, 0)));
        reminders.add(new Reminder("Attend Workshop 2", LocalDateTime.of(2016, 2, 1, 17, 0)));
        reminders.add(new Reminder("Attend Workshop 3", LocalDateTime.of(2016, 3, 1, 17, 0)));
    }

    @Test
    public void testRespondToViewWithKeywords() {
        assertTrue(this.viewCommand.respondTo("view keyword"));
    }

    @Test
    public void testCaseInsensitiveRespondToView() {
        assertTrue(this.viewCommand.respondTo("ViEw keyword"));
    }

    @Test
    public void testDoesNotRespondToOtherCommands() {
        assertFalse(this.viewCommand.respondTo("notview"));
    }

    @Test
    public void testCommandResultWhenNoMatchFound() {
        this.schedule.addTask(new Task("asdjkahsdkjhasjdkh"));
        CommandResult result = this.viewCommand.execute("view that nasty todo");
        assertEquals("Cannot find \"that nasty todo\".", result.getFeedback());
    }

    @Test
    public void testCommandResultWhenExactlyOneTaskWithNoRemindersFound() {
        this.setupTaskWithAndWithoutReminders();
        CommandResult result = this.viewCommand.execute("view Task");
        assertEquals("Task" + '\n' + "Reminders:" + '\n' + "There are no reminders found!", result.getFeedback());
    }

    @Test
    public void testCommandResultWhenExactlyOneTaskWithRemindersFound() {
        this.setupTaskWithAndWithoutReminders();
        CommandResult result = this.viewCommand.execute("view Found");
        assertEquals("Found" + '\n' + "Reminders:" + '\n'
            + "1. Attend Workshop 1 (On 2016-01-01 5:00PM)" + '\n'
            + "2. Attend Workshop 2 (On 2016-02-01 5:00PM)" + '\n'
            + "3. Attend Workshop 3 (On 2016-03-01 5:00PM)" + '\n', result.getFeedback());
    }
}
