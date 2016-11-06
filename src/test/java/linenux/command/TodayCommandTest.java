package linenux.command;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.Before;
import org.junit.Test;

import linenux.model.Schedule;
import linenux.model.Task;

//@@author A0127694U
public class TodayCommandTest {
    private TodayCommand todayCommand;

    private Schedule schedule;
    private Task todo;
    private Task deadlineToday;
    private Task deadlineTomorrow;
    private Task eventYesterday;
    private Task eventTomorrow;
    private Task eventYesterdayToday;
    private Task eventTodayTomorrow;
    private Task eventYesterdayTomorrow;

    @Before
    public void setupCommand() {
        this.schedule = new Schedule();
        Clock clock = Clock.fixed(Instant.parse("2016-01-01T07:24:00.00Z"), ZoneId.of("Asia/Singapore"));
        this.todayCommand = new TodayCommand(this.schedule, clock);

        this.todo = new Task("hello");
        this.deadlineToday = new Task("deadline 1", LocalDateTime.of(2016, 1, 1, 21, 0));
        this.deadlineTomorrow = new Task("deadline 2", LocalDateTime.of(2016, 1, 2, 21, 0));
        this.eventYesterday = new Task("event 1", LocalDateTime.of(2015, 12, 31, 10, 0), LocalDateTime.of(2015, 12, 31, 12, 0));
        this.eventTomorrow = new Task("event 2", LocalDateTime.of(2016, 1, 2, 10, 0), LocalDateTime.of(2016, 1, 2, 12, 0));
        this.eventYesterdayToday = new Task("event 3", LocalDateTime.of(2015, 12, 31, 10, 0), LocalDateTime.of(2016, 1, 1, 10, 0));
        this.eventTodayTomorrow = new Task("event 4", LocalDateTime.of(2016, 1, 1, 10, 0), LocalDateTime.of(2016, 1, 2, 10, 0));
        this.eventYesterdayTomorrow = new Task("event 5", LocalDateTime.of(2015, 12, 31, 10, 0), LocalDateTime.of(2016, 1, 2, 10, 0));

        this.schedule.addTask(this.todo);
        this.schedule.addTask(this.deadlineToday);
        this.schedule.addTask(this.deadlineTomorrow);
        this.schedule.addTask(this.eventYesterday);
        this.schedule.addTask(this.eventTomorrow);
        this.schedule.addTask(this.eventYesterdayToday);
        this.schedule.addTask(this.eventTodayTomorrow);
        this.schedule.addTask(this.eventYesterdayTomorrow);
    }

    @Test
    public void respondTo_inputThatBeginsWithToday_trueReturned() {
        assertTrue(this.todayCommand.respondTo("today"));
        assertTrue(this.todayCommand.respondTo("today hello"));
        assertTrue(this.todayCommand.respondTo("toDay"));
    }

    @Test
    public void respondTo_otherCommands_falseReturned() {
        assertFalse(this.todayCommand.respondTo("atoday"));
        assertFalse(this.todayCommand.respondTo("todaya"));
        assertFalse(this.todayCommand.respondTo("toda"));
    }

    @Test
    public void execute_commandResultReturned() {
        this.todayCommand.execute("today");
        assertTrue(this.schedule.getFilteredTasks().contains(this.todo));
        assertTrue(this.schedule.getFilteredTasks().contains(this.deadlineToday));
        assertFalse(this.schedule.getFilteredTasks().contains(this.deadlineTomorrow));
        assertTrue(this.schedule.getFilteredTasks().contains(this.eventYesterday));
        assertFalse(this.schedule.getFilteredTasks().contains(this.eventTomorrow));
        assertTrue(this.schedule.getFilteredTasks().contains(this.eventYesterdayToday));
        assertTrue(this.schedule.getFilteredTasks().contains(this.eventTodayTomorrow));
        assertTrue(this.schedule.getFilteredTasks().contains(this.eventYesterdayTomorrow));
    }
}
