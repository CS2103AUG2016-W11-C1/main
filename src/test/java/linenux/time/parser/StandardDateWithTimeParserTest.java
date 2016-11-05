package linenux.time.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

//@@author A0135788M
/**
 * JUnit test for StandardDateWithTime time format.
 */
public class StandardDateWithTimeParserTest {
    TimeParser parser;

    @Before
    public void setupParser() {
        this.parser = new StandardDateWithTimeParser();
    }

    /**
     * Test that parser responds to valid format.
     */
    @Test
    public void respondTo_validInputFormat_trueReturned() {
        assertTrue(this.parser.respondTo("16 Oct 2016 2.05PM"));
    }

    /**
     * Test that parser responds to lowercase am and pm.
     */
    @Test
    public void respondTo_lowerCaseAmPm_trueReturned() {
        assertTrue(this.parser.respondTo("16 Oct 2016 2.05am"));
        assertTrue(this.parser.respondTo("16 Oct 2016 2.05pm"));
    }

    /**
     * Test that parser responds to uppercase month.
     */
    @Test
    public void respondTo_upperCaseMonth_trueReturned() {
        assertTrue(this.parser.respondTo("16 OCT 2016 2.05am"));
    }

    /**
     * Test that parser responds to lowercase month.
     */
    @Test
    public void respondTo_lowerCaseMonth_trueReturned() {
        assertTrue(this.parser.respondTo("16 oct 2016 2.05am"));
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
        LocalDateTime dateTime = this.parser.parse("01 Oct 2016 9.23AM");
        LocalDateTime expectedDateTime = LocalDateTime.of(2016, 10, 01, 9, 23);
        assertEquals(expectedDateTime, dateTime);
    }

    /**
     * Test that parser parses correctly for uppercase PM.
     */
    @Test
    public void parse_upperCasePm_parsedDateMatchesExpected() {
        LocalDateTime dateTime = this.parser.parse("01 Oct 2016 2.05PM");
        LocalDateTime expectedDateTime = LocalDateTime.of(2016, 10, 01, 14, 5);
        assertEquals(expectedDateTime, dateTime);
    }

    /**
     * Test that parser parses correctly for lowercase AM.
     */
    @Test
    public void parse_lowerCaseAm_parsedDateMatchesExpected() {
        LocalDateTime dateTime = this.parser.parse("01 Oct 2016 9.23am");
        LocalDateTime expectedDateTime = LocalDateTime.of(2016, 10, 01, 9, 23);
        assertEquals(expectedDateTime, dateTime);
    }

    /**
     * Test that parser parses correctly for lowercase PM.
     */
    @Test
    public void parse_lowerCasePm_parsedDateMatchesExpected() {
        LocalDateTime dateTime = this.parser.parse("01 Oct 2016 9.23pm");
        LocalDateTime expectedDateTime = LocalDateTime.of(2016, 10, 01, 21, 23);
        assertEquals(expectedDateTime, dateTime);
    }

    /**
     * Test that parser parses correctly for uppercase month.
     */
    @Test
    public void parse_upperCaseMonth_parsedDateMatchesExpected() {
        LocalDateTime dateTime = this.parser.parse("01 OCT 2016 9.23AM");
        LocalDateTime expectedDateTime = LocalDateTime.of(2016, 10, 01, 9, 23);
        assertEquals(expectedDateTime, dateTime);
    }

    /**
     * Test that parser parses correctly for lowercase month.
     */
    @Test
    public void parse_lowerCaseMonth_parsedDateMatchesExpected() {
        LocalDateTime dateTime = this.parser.parse("01 oct 2016 2.05AM");
        LocalDateTime expectedDateTime = LocalDateTime.of(2016, 10, 01, 2, 5);
        assertEquals(expectedDateTime, dateTime);
    }

    /**
     * Test that parser parses correctly for mixed case month.
     */
    @Test
    public void parse_mixedCaseMonth_parsedDateMatchesExpected() {
        LocalDateTime dateTime = this.parser.parse("01 OcT 2016 2.05AM");
        LocalDateTime expectedDateTime = LocalDateTime.of(2016, 10, 01, 2, 5);
        assertEquals(expectedDateTime, dateTime);
    }
}
