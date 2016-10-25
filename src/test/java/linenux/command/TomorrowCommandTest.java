package linenux.command;

import linenux.model.Schedule;
import linenux.model.Task;
import org.junit.Before;
import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by yihangho on 10/25/16.
 */
public class TomorrowCommandTest {
    private TomorrowCommand tomorrowCommand;

    private Schedule schedule;
    private Task todo;
    private Task deadlineToday;
    private Task deadlineTomorrow;
    private Task eventYesterday;
    private Task eventTomorrow;
    private Task eventYesterdayToday;
    private Task eventTodayTomorrow;

    @Before
    public void setupCommand() {
        this.schedule = new Schedule();
        Clock clock = Clock.fixed(Instant.parse("2016-01-01T07:24:00.00Z"), ZoneId.of("Asia/Singapore"));
        this.tomorrowCommand = new TomorrowCommand(this.schedule, clock);

        this.todo = new Task("hello");
        this.deadlineToday = new Task("deadline 1", LocalDateTime.of(2016, 1, 1, 21, 0));
        this.deadlineTomorrow = new Task("deadline 2", LocalDateTime.of(2016, 1, 2, 21, 0));
        this.eventYesterday = new Task("event 1", LocalDateTime.of(2015, 12, 31, 10, 0), LocalDateTime.of(2015, 12, 31, 12, 0));
        this.eventTomorrow = new Task("event 2", LocalDateTime.of(2016, 1, 2, 10, 0), LocalDateTime.of(2016, 1, 2, 12, 0));
        this.eventYesterdayToday = new Task("event 3", LocalDateTime.of(2015, 12, 31, 10, 0), LocalDateTime.of(2016, 1, 1, 10, 0));
        this.eventTodayTomorrow = new Task("event 4", LocalDateTime.of(2016, 1, 1, 10, 0), LocalDateTime.of(2016, 1, 2, 10, 0));

        this.schedule.addTask(this.todo);
        this.schedule.addTask(this.deadlineToday);
        this.schedule.addTask(this.deadlineTomorrow);
        this.schedule.addTask(this.eventYesterday);
        this.schedule.addTask(this.eventTomorrow);
        this.schedule.addTask(this.eventYesterdayToday);
        this.schedule.addTask(this.eventTodayTomorrow);
    }

    @Test
    public void testRespondTo() {
        assertTrue(this.tomorrowCommand.respondTo("tomorrow"));
        assertTrue(this.tomorrowCommand.respondTo("tomorrow bla"));
        assertTrue(this.tomorrowCommand.respondTo("toMorrow"));
    }

    @Test
    public void testNotRespondTo() {
        assertFalse(this.tomorrowCommand.respondTo("tomorrowa"));
        assertFalse(this.tomorrowCommand.respondTo("atomorrow"));
        assertFalse(this.tomorrowCommand.respondTo("tomorro"));
    }

    @Test
    public void testExecute() {
        this.tomorrowCommand.execute("tomorrow");

        assertTrue(this.schedule.getFilteredTasks().contains(this.todo));
        assertFalse(this.schedule.getFilteredTasks().contains(this.deadlineToday));
        assertTrue(this.schedule.getFilteredTasks().contains(this.deadlineTomorrow));
        assertFalse(this.schedule.getFilteredTasks().contains(this.eventYesterday));
        assertTrue(this.schedule.getFilteredTasks().contains(this.eventTomorrow));
        assertFalse(this.schedule.getFilteredTasks().contains(this.eventYesterdayToday));
        assertTrue(this.schedule.getFilteredTasks().contains(this.eventTodayTomorrow));
    }
}
