package linenux.time.parser;

import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Charlton
 *
 */
public class InvalidTimeParserTest {
    private InvalidTimeParser invalidTimeParser;

    @Before
    public void setUpInvalidTimeParser() {
        this.invalidTimeParser = new InvalidTimeParser();
    }

    @Test
    public void testRespondToInvalidTimeParser() {
        assertTrue(this.invalidTimeParser.respondTo(""));
        assertTrue(this.invalidTimeParser.respondTo("415 on 041117"));
        assertTrue(this.invalidTimeParser.respondTo("the day after tmr at 4 o clock"));
    }

    @Test
    public void testExecuteInvalidTimeParser() {
        LocalDateTime result = this.invalidTimeParser.parse("");
        assertTrue(result == null);
    }
}
