package linenux.control;

import linenux.time.parser.TimeParser;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by yihangho on 10/7/16.
 */
public class TimeParserManagerTest {
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
