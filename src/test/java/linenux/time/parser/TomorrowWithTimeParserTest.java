package linenux.time.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

//@@author A0127694U
/**
 * JUnit test for TomorrowWithTime time format.
 */
public class TomorrowWithTimeParserTest {
    TimeParser parser;

    @Before
    public void setupParser() {
        this.parser = new TomorrowWithTimeParser();
    }

    /**
     * Test that parser responds to valid format.
     */
    @Test
    public void testRespondToValidFormat() {
        assertTrue(this.parser.respondTo("tomorrow 2.05PM"));
    }

    /**
     * Test that parser responds to lowercase am and pm.
     */
    @Test
    public void testRespondToSmallAmPm() {
        assertTrue(this.parser.respondTo("tomorrow 2.05am"));
    }

    /**
     * Test that parser responds to invalid format.
     */
    @Test
    public void testDoesNotRespondToInValidFormat() {
        assertFalse(this.parser.respondTo("Jan 1, 2016 2:05PM"));
        assertFalse(this.parser.respondTo("yesterday"));
        assertFalse(this.parser.respondTo("2:05PM"));
        assertFalse(this.parser.respondTo("2016-01-01"));
        assertFalse(this.parser.respondTo("2016-01-01 14:00"));
    }

    /**
     * Test that parser responds to uppercase today.
     */
    @Test
    public void testRespondToUppercaseMonth() {
        assertTrue(this.parser.respondTo("TOMORROW 2.05am"));
    }

}
