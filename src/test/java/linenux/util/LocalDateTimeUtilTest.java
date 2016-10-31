package linenux.util;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

//@@author A0144915A
public class LocalDateTimeUtilTest {
    private LocalDateTime first, second;

    @Before
    public void setupLocalDateTimes() {
        this.first = LocalDateTime.of(2016, 1, 1, 0, 0);
        this.second = LocalDateTime.of(2016, 1, 1, 0, 1);
    }

    @Test
    public void testMin() {
        assertEquals(first, LocalDateTimeUtil.min(first, first));
        assertEquals(first, LocalDateTimeUtil.min(first, second));
        assertEquals(first, LocalDateTimeUtil.min(second, first));
    }

    @Test
    public void testMax() {
        assertEquals(first, LocalDateTimeUtil.max(first, first));
        assertEquals(second, LocalDateTimeUtil.max(first, second));
        assertEquals(second, LocalDateTimeUtil.max(second, first));
    }
}
