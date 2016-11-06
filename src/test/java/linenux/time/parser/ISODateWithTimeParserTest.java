package linenux.time.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

//@@author A0144915A
/**
 * JUnit test for ISODateWithTime time format.
 */
public class ISODateWithTimeParserTest {
    TimeParser parser;

    @Before
    public void setupParser() {
        this.parser = new ISODateWithTimeParser();
    }

    /**
     * Test that parser responds to valid format.
     */
    @Test
    public void respondTo_validInputFormat_trueReturned() {
        assertTrue(this.parser.respondTo("2016-10-01 2.05PM"));
    }

    /**
     * Test that parser responds to lowercase am and pm.
     */
    @Test
    public void respondTo_lowerCaseAmPm_trueReturned() {
        assertTrue(this.parser.respondTo("2016-10-01 2.05am"));
        assertTrue(this.parser.respondTo("2016-10-01 2.05pm"));
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

    /**
     * Test that parser parses correctly for uppercase AM.
     */
    @Test
    public void parse_upperCaseAm_parsedDateMatchesExpected() {
        LocalDateTime dateTime = this.parser.parse("2016-10-01 9.23AM");
        LocalDateTime expectedDateTime = LocalDateTime.of(2016, 10, 01, 9, 23);
        assertEquals(expectedDateTime, dateTime);
    }

    /**
     * Test that parser parses correctly for uppercase PM.
     */
    @Test
    public void parse_upperCasePm_parsedDateMatchesExpected() {
        LocalDateTime dateTime = this.parser.parse("2016-10-01 2.05PM");
        LocalDateTime expectedDateTime = LocalDateTime.of(2016, 10, 01, 14, 5);
        assertEquals(expectedDateTime, dateTime);
    }

    /**
     * Test that parser parses correctly for lowercase AM.
     */
    @Test
    public void parse_lowerCaseAm_parsedDateMatchesExpected() {
        LocalDateTime dateTime = this.parser.parse("2016-10-01 9.23am");
        LocalDateTime expectedDateTime = LocalDateTime.of(2016, 10, 01, 9, 23);
        assertEquals(expectedDateTime, dateTime);
    }

    /**
     * Test that parser parses correctly for lowercase PM.
     */
    @Test
    public void parse_lowerCasePm_parsedDateMatchesExpected() {
        LocalDateTime dateTime = this.parser.parse("2016-10-01 9.23pm");
        LocalDateTime expectedDateTime = LocalDateTime.of(2016, 10, 01, 21, 23);
        assertEquals(expectedDateTime, dateTime);
    }
}
