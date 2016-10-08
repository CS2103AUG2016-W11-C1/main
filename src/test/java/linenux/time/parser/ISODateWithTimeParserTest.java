package linenux.time.parser;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by yihangho on 10/7/16.
 */
public class ISODateWithTimeParserTest {
    TimeParser parser;

    @Before
    public void setupParser() {
        this.parser = new ISODateWithTimeParser();
    }

    @Test
    public void testRespondToValidFormat() {
        assertTrue(this.parser.respondTo("2016-10-01 2:05PM"));
    }

    @Test
    public void testRespondToSmallAmPm() {
        assertTrue(this.parser.respondTo("2016-10-01 2:05am"));
    }

    @Test
    public void testDoesNotRespondToInValidFormat() {
        assertFalse(this.parser.respondTo("Jan 1, 2016 2:05PM"));
        assertFalse(this.parser.respondTo("yesterday"));
        assertFalse(this.parser.respondTo("2:05PM"));
        assertFalse(this.parser.respondTo("2016-01-01"));
        assertFalse(this.parser.respondTo("2016-01-01 14:00"));
    }

    @Test
    public void testParseAM() {
        LocalDateTime dateTime = this.parser.parse("2016-10-01 9:23AM");
        LocalDateTime expectedDateTime = LocalDateTime.of(2016, 10, 01, 9, 23);
        assertEquals(expectedDateTime, dateTime);
    }

    @Test
    public void testParsePM() {
        LocalDateTime dateTime = this.parser.parse("2016-10-01 2:05PM");
        LocalDateTime expectedDateTime = LocalDateTime.of(2016, 10, 01, 14, 5);
        assertEquals(expectedDateTime, dateTime);
    }

    @Test
    public void testParseSmallAm() {
        LocalDateTime dateTime = this.parser.parse("2016-10-01 9:23am");
        LocalDateTime expectedDateTime = LocalDateTime.of(2016, 10, 01, 9, 23);
        assertEquals(expectedDateTime, dateTime);
    }
}
