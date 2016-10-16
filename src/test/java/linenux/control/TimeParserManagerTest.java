package linenux.control;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Test;

import linenux.time.parser.TimeParser;

/**
 * JUnit test for time parser manager.
 */
public class TimeParserManagerTest {

    /**
     * Test that parses can correctly parse.
     */
    @Test
    public void testCanParse() {
        TimeParser trueParser = new TimeParser() {
            @Override
            public boolean respondTo(String u) {
                return true;
            }

            @Override
            public LocalDateTime parse(String u) {
                return null;
            }
        };

        TimeParser falseParser = new TimeParser() {
            @Override
            public boolean respondTo(String u) {
                return false;
            }

            @Override
            public LocalDateTime parse(String u) {
                return null;
            }
        };

        TimeParserManager manager = new TimeParserManager(falseParser);
        assertFalse(manager.canParse("123123"));

        manager = new TimeParserManager(falseParser, trueParser);
        assertTrue(manager.canParse("1231231"));
    }
}
