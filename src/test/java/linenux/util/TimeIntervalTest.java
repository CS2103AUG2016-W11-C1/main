package linenux.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

//@@author A0127694U
public class TimeIntervalTest {
    TimeInterval interval;

    @Before
    public void setupInterval() {
        this.interval = new TimeInterval(LocalDateTime.of(2016, 1, 1, 0, 0), LocalDateTime.of(2016, 12, 31, 23, 59));
    }

    @Test
    public void inInterval_timesInInterval_trueReturned() {
        assertTrue(this.interval.inInterval(LocalDateTime.of(2016, 1, 1, 0, 0)));
        assertTrue(this.interval.inInterval(LocalDateTime.of(2016, 1, 2, 0, 0)));
        assertTrue(this.interval.inInterval(LocalDateTime.of(2016, 12, 31, 23, 59)));
    }

    @Test
    public void inInterval_timesNotInInterval_falseReturned() {
        assertFalse(this.interval.inInterval(LocalDateTime.of(2015, 12, 31, 23, 59)));
        assertFalse(this.interval.inInterval(LocalDateTime.of(2017, 1, 1, 0, 0)));
    }

    @Test
    public void isTrivial_timeIntervalIsTrivial_trueReturned() {
        LocalDateTime time1 = LocalDateTime.of(2016, 1, 1, 0, 0);
        LocalDateTime time2 = LocalDateTime.of(2016, 1, 1, 0, 0);
        this.interval = new TimeInterval(time1, time2);
        assertTrue(this.interval.isTrivial());
    }

    @Test
    public void isTrivial_timeIntervalIsNotTrivial_falseReturned() {
        assertFalse(this.interval.isTrivial());
    }
}
