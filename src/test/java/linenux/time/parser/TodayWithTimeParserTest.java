package linenux.time.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

//@@author A0140702X
/**
 * JUnit test for TodayWithTime time format.
 */
public class TodayWithTimeParserTest {
    TimeParser parser;

    @Before
    public void setupParser() {
        this.parser = new TodayWithTimeParser();
    }

    /**
     * Test that parser responds to valid format.
     */
    @Test
    public void respondTo_validInputFormat_trueReturned() {
        assertTrue(this.parser.respondTo("today 2.05PM"));
    }

    /**
     * Test that parser responds to lowercase am and pm.
     */
    @Test
    public void respondTo_lowerCaseAmPm_trueReturned() {
        assertTrue(this.parser.respondTo("today 2.05am"));
        assertTrue(this.parser.respondTo("today 2.06pm"));
    }

    /**
     * Test that parser responds to uppercase today.
     */
    @Test
    public void respondTo_upperCaseToday_trueReturned() {
        assertTrue(this.parser.respondTo("TODAY 2.05am"));
    }

    /**
     * Test that parser responds to mixed case today.
     */
    @Test
    public void respondTo_mixedCaseToday_trueReturned() {
        assertTrue(this.parser.respondTo("TodAy 2.05am"));
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
}
