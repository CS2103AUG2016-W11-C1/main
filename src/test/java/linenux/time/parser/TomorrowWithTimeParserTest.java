package linenux.time.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

//@@author A0127694U
/**
 * JUnit test for TomorrowWithTime time format.
 */
public class TomorrowWithTimeParserTest {
    private TimeParser parser;

    @Before
    public void setupParser() {
        Clock clock = Clock.fixed(Instant.parse("2016-01-01T07:24:00.00Z"), ZoneId.of("Asia/Singapore"));
        TomorrowWithTimeParser parser = new TomorrowWithTimeParser();
        parser.setClock(clock);
        this.parser = parser;
    }

    /**
     * Test that parser responds to valid format.
     */
    @Test
    public void respondTo_validInputFormat_trueReturned() {
        assertTrue(this.parser.respondTo("tomorrow 2.05PM"));
    }

    /**
     * Test that parser responds to lowercase am and pm.
     */
    @Test
    public void respondTo_lowerCaseAmPm_trueReturned() {
        assertTrue(this.parser.respondTo("tomorrow 2.05am"));
        assertTrue(this.parser.respondTo("tomorrow 2.05pm"));
    }

    /**
     * Test that parser responds to uppercase tomorrow.
     */
    @Test
    public void respondTo_upperCaseTomorrow_trueReturned() {
        assertTrue(this.parser.respondTo("TOMORROW 2.05am"));
    }

    /**
     * Test that parser responds to mixed case tomorrow.
     */
    @Test
    public void respondTo_mixedCaseTomorrow_trueReturned() {
        assertTrue(this.parser.respondTo("ToMorrOw 2.05am"));
    }

    /**
     * Test that parser responds to invalid format.
     */
    @Test
    public void respondTo_invalidInputFormat_falseReturned() {
        assertFalse(this.parser.respondTo("Jan 1, 2016 2:05PM"));
        assertFalse(this.parser.respondTo("yesterday"));
        assertFalse(this.parser.respondTo("2:05PM"));
        assertFalse(this.parser.respondTo("2016-01-01"));
        assertFalse(this.parser.respondTo("2016-01-01 14:00"));
    }

    @Test
    public void parse_validInput_correctOutputReturned() {
        LocalDateTime output = this.parser.parse("tomorrow 2.00PM");
        assertEquals(LocalDateTime.of(2016, 1, 2, 14, 00), output);
    }
}
