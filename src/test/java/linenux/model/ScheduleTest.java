package linenux.model;

import static junit.framework.TestCase.assertEquals;
import static linenux.helpers.Assert.assertNoChange;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import linenux.command.util.ReminderSearchResult;

//@@author A0135788M
/**
 * JUnit test for schedule.
 */
public class ScheduleTest {
    private Schedule schedule;

    @Before
    public void setupSchedule() {
        this.schedule = new Schedule();
    }

    @Test
    public void testAddTask() {
        int beforeSize = this.schedule.getTaskList().size();
        this.schedule.addTask(new Task("bla"));
        int afterSize = this.schedule.getTaskList().size();

        assertEquals(beforeSize + 1, afterSize);
    }

    @Test
    public void testClear() {
        Task task1 = new Task("hello");
        Task task2 = new Task("blah");
        this.schedule.addTask(task1);
        this.schedule.addTask(task2);

        int originalSize = this.schedule.getTaskList().size();
        this.schedule.clear();
        int endSize = this.schedule.getTaskList().size();

        assertEquals(originalSize - 2, endSize);
    }

    @Test
    public void testSearch() {
        String[] keywords = {"hello", "WoRlD"};
        Task match1 = new Task("Say Hello");
        Task match2 = new Task("Around the world");
        Task mismatch = new Task("meh");

        this.schedule.addTask(match1);
        this.schedule.addTask(mismatch);
        this.schedule.addTask(match2);

        ArrayList<Task> tasks = this.schedule.search(keywords);

        assertEquals(2, tasks.size());
    }

    @Test
    public void testEdit() {
        this.schedule.clear();
        Task originalTask = new Task("hello");
        this.schedule.addTask(originalTask);
        Task editedTask = new Task("new task");
        this.schedule.updateTask(originalTask, editedTask);

        assertEquals(this.schedule.getTaskList().get(0), editedTask);
    }

    @Test
    public void testDelete() {
        Task task = new Task("bla");
        this.schedule.addTask(task);
        int beforeSize = this.schedule.getTaskList().size();
        this.schedule.deleteTask(task);
        int afterSize = this.schedule.getTaskList().size();

        assertEquals(beforeSize - 1, afterSize);
        assertTrue(this.schedule.getTaskList().indexOf(task) == -1);
    }

    @Test
    public void testDeleteReminder() {
        this.schedule.clear();
        Task task = new Task("blah");
        Reminder r = new Reminder("reminder", LocalDateTime.of(2016, 1, 1, 1, 0));
        task.getReminders().add(r);
        this.schedule.addTask(task);

        assertEquals("blah", this.schedule.getTaskList().get(0).getTaskName());
        assertEquals(1, this.schedule.getTaskList().get(0).getReminders().size());

        ArrayList<Reminder> list = new ArrayList<Reminder>();
        list.add(r);
        ReminderSearchResult res = new ReminderSearchResult(task, list);
        this.schedule.deleteReminder(res);
        assertEquals(0, this.schedule.getTaskList().get(0).getReminders().size());
    }

    @Test
    public void testMaxStates() {
        for (int i = 0; i < Schedule.MAX_STATES; i++) {
            this.schedule.addTask(new Task("task" + Integer.toString(i)));
        }
        assertEquals(Schedule.MAX_STATES, this.schedule.getStates().size());
        assertNoChange(() -> this.schedule.getStates().size(), () -> { this.schedule.addTask(new Task("Hi")); return 0; });
    }
}
