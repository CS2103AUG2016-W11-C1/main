package linenux.time.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for StandardDateWithTime time format.
 */
//@@author A0135788M
public class StandardDateTimeParserTest {
    TimeParser parser;

    @Before
    public void setupParser() {
        this.parser = new StandardDateTimeParser();
    }

    /**
     * Test that parser responds to valid format.
     */
    @Test
    public void testRespondToValidFormat() {
        assertTrue(this.parser.respondTo("16 Oct 2016 2.05PM"));
    }

    /**
     * Test that parser responds to lowercase am and pm.
     */
    @Test
    public void testRespondToSmallAmPm() {
        assertTrue(this.parser.respondTo("16 Oct 2016 2.05am"));
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
     * Test that parser responds to uppercase month.
     */
    @Test
    public void testRespondToUppercaseMonth() {
        assertTrue(this.parser.respondTo("16 OCT 2016 2.05am"));
    }

}
