package linenux.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Test;

/**
 * JUnit test for task model.
 */
public class TaskTest {
    //@@author A0144915A
    /**
     * Test that todo task is correctly labeled.
     */
    @Test
    public void testIsTodo() {
        Task task = new Task("bla", null, null, new ArrayList<String>());
        assertTrue(task.isTodo());
        assertFalse(task.isDeadline());
        assertFalse(task.isEvent());
    }

    //@@author A0144915A
    /**
     * Test that deadline task is correctly labeled.
     */
    @Test
    public void testIsDeadline() {
        Task task = new Task("bla", null, LocalDateTime.of(2016, 1, 1, 0, 0));
        assertTrue(task.isDeadline());
        assertFalse(task.isTodo());
        assertFalse(task.isEvent());
    }

    /**
     * Test that event task is correctly labeled.
     */
    @Test
    public void testIsEvent() {
        Task task = new Task("bla", LocalDateTime.of(2016, 1, 1, 0, 0), LocalDateTime.of(2016, 1, 1, 0, 0));
        assertTrue(task.isEvent());
        assertFalse(task.isTodo());
        assertFalse(task.isDeadline());
    }

    /**
     * Test that todo task is correctly converted to string.
     */
    @Test
    public void testTodoToString() {
        Task task = new Task("hello", null, null, new ArrayList<String>());
        assertEquals("hello", task.toString());
    }

    /**
     * Test that deadline task is correctly converted to string.
     */
    @Test
    public void testDeadlineToString() {
        Task task = new Task("hello", null, LocalDateTime.of(2016, 1, 1, 17, 0));
        assertEquals("hello (Due 2016-01-01 5:00PM)", task.toString());
    }

    /**
     * Test that event task is correctly converted to string.
     */
    @Test
    public void testEventToString() {
        Task task = new Task("hello", LocalDateTime.of(2016, 1, 1, 17, 0), LocalDateTime.of(2016, 1, 2, 17, 0));
        assertEquals("hello (2016-01-01 5:00PM - 2016-01-02 5:00PM)", task.toString());
    }

    //@@author A0127694U
    @Test
    public void testCategoryToString() {
        ArrayList<String> tags = new ArrayList<>();
        tags.add("tag");
        Task task = new Task("hello", null, null, tags);
        assertEquals("hello [Tags: \"tag\" ]", task.toString());
    }
}
