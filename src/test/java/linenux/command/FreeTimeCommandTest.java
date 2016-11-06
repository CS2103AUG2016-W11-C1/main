package linenux.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.Before;
import org.junit.Test;

import linenux.command.result.CommandResult;
import linenux.model.Schedule;
import linenux.model.Task;

//@@author A0144915A
public class FreeTimeCommandTest {
    FreeTimeCommand command;

    @Before
    public void setupCommand() {
        Schedule schedule = new Schedule();
        Task event1 = new Task("Event 1", LocalDateTime.of(2016, 1, 1, 17, 0), LocalDateTime.of(2016, 1, 1, 19, 0));
        Task event2 = new Task("Event 2", LocalDateTime.of(2016, 1, 1, 21, 0), LocalDateTime.of(2016, 1, 1, 23, 0));
        schedule.addTask(event1);
        schedule.addTask(event2);

        Clock clock = Clock.fixed(Instant.parse("2016-01-01T07:24:00.00Z"), ZoneId.of("Asia/Singapore"));

        this.command = new FreeTimeCommand(schedule, clock);
    }

    @Test
    public void respondTo_commandThatStartsWithFreetime_trueReturned() {
        assertTrue(this.command.respondTo("freetime"));
        assertTrue(this.command.respondTo("   freetime   "));
        assertTrue(this.command.respondTo("freetime et/2016-01-01 5.00PM"));
        assertTrue(this.command.respondTo("freetime st/2016-01-01 5.00PM et/2016-01-02 5.00PM"));
    }

    @Test
    public void respondTo_upperCase_trueReturned() {
        assertTrue(this.command.respondTo("FrEetime"));
    }

    @Test
    public void respondTo_otherCommands_falseReturned() {
        assertFalse(this.command.respondTo("freetimeee"));
        assertFalse(this.command.respondTo("facetime"));
    }

    @Test
    public void execute_validInput_commandResultReturned() {
        CommandResult result = this.command.execute("freetime st/2016-01-01 3.00PM et/2016-01-01 11.59PM");
        String expectedResult = "You are free at the following time slots:\n" +
                " - 2016-01-01 3.00PM - 2016-01-01 5.00PM\n" +
                " - 2016-01-01 7.00PM - 2016-01-01 9.00PM\n" +
                " - 2016-01-01 11.00PM - 2016-01-01 11.59PM\n";
        assertEquals(expectedResult, result.getFeedback());
    }

    @Test
    public void execute_noStartTime_nowAssumed() {
        CommandResult result = this.command.execute("freetime et/2016-01-01 11.59PM");
        String expectedResult = "You are free at the following time slots:\n" +
                " - 2016-01-01 3.24PM - 2016-01-01 5.00PM\n" +
                " - 2016-01-01 7.00PM - 2016-01-01 9.00PM\n" +
                " - 2016-01-01 11.00PM - 2016-01-01 11.59PM\n";
        assertEquals(expectedResult, result.getFeedback());
    }

    @Test
    public void execute_invalidTime_commandResultReturned() {
        CommandResult result = this.command.execute("freetime et/Jan 1, 16 2359");
        String expectedResult = "Cannot parse \"Jan 1, 16 2359\".";
        assertEquals(expectedResult, result.getFeedback());

        result = this.command.execute("freetime st/tomorrow et/2016-01-01 11.59PM");
        expectedResult = "Cannot parse \"tomorrow\".";
        assertEquals(expectedResult, result.getFeedback());
    }

    @Test
    public void execute_endTimeBeforeStartTime_commandResultReturned() {
        CommandResult result = this.command.execute("freetime st/2016-01-01 5.00PM et/2016-01-01 4.45PM");
        String expectedResult = "End time must be after start time.";
        assertEquals(expectedResult, result.getFeedback());

        result = this.command.execute("freetime et/2016-01-01 3.00PM");
        assertEquals(expectedResult, result.getFeedback());
    }

    @Test
    public void execute_noFreetimeAtTheBeginning_commandResultReturned() {
        CommandResult result = this.command.execute("freetime st/2016-01-01 5.00PM et/2016-01-01 8.00PM");
        String expectedResult = "You are free at the following time slots:\n" +
                " - 2016-01-01 7.00PM - 2016-01-01 8.00PM\n";
        assertEquals(expectedResult, result.getFeedback());
    }

    @Test
    public void execute_noFreetimeAtTheEnd_commandResultReturned() {
        CommandResult result = this.command.execute("freetime st/2016-01-01 3.00PM et/2016-01-01 7.00PM");
        String expectedResult = "You are free at the following time slots:\n" +
                " - 2016-01-01 3.00PM - 2016-01-01 5.00PM\n";
        assertEquals(expectedResult, result.getFeedback());
    }

    @Test
    public void execute_noEndTime_commandResultReturned() {
        CommandResult result = this.command.execute("freetime");
        String expectedResult = "End time must be specified.";
        assertEquals(expectedResult, result.getFeedback());

        result = this.command.execute("freetime st/2016-01-01 5.00PM");
        assertEquals(expectedResult, result.getFeedback());
    }

    @Test
    public void execute_noFreetime_commandResultReturned() {
        CommandResult result = this.command.execute("freetime st/2016-01-01 5.00PM et/2016-01-01 7.00PM");
        String expectedResult = "You don't have any free time in that period.";
        assertEquals(expectedResult, result.getFeedback());
    }

    @Test
    public void execute_queryRangeIntersectsWithEvent_commandResultReturned() {
        CommandResult result = this.command.execute("freetime st/2016-01-01 6.00PM et/2016-01-01 8.00PM");
        String expectedResult = "You are free at the following time slots:\n" +
                " - 2016-01-01 7.00PM - 2016-01-01 8.00PM\n";
        assertEquals(expectedResult, result.getFeedback());

        result = this.command.execute("freetime st/2016-01-01 4.00PM et/2016-01-01 6.00PM");
        expectedResult = "You are free at the following time slots:\n" +
                " - 2016-01-01 4.00PM - 2016-01-01 5.00PM\n";
        assertEquals(expectedResult, result.getFeedback());

        result = this.command.execute("freetime st/2016-01-01 5.30PM et/2016-01-01 6.30PM");
        expectedResult = "You don't have any free time in that period.";
        assertEquals(expectedResult, result.getFeedback());
    }
}
