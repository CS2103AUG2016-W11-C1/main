package linenux.model;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by yihangho on 10/7/16.
 */
public class TaskTest {
    @Test
    public void testIsTodo() {
        Task task = new Task("bla", null, null);
        assertTrue(task.isTodo());
        assertFalse(task.isDeadline());
        assertFalse(task.isEvent());
    }

    @Test
    public void testIsDeadline() {
        Task task = new Task("bla", null, LocalDateTime.of(2016, 1, 1, 0, 0));
        assertTrue(task.isDeadline());
        assertFalse(task.isTodo());
        assertFalse(task.isEvent());
    }

    @Test
    public void testIsEvent() {
        Task task = new Task("bla", LocalDateTime.of(2016, 1, 1, 0, 0), LocalDateTime.of(2016, 1, 1, 0, 0));
        assertTrue(task.isEvent());
        assertFalse(task.isTodo());
        assertFalse(task.isDeadline());
    }
}
