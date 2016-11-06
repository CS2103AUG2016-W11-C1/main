package linenux.model;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

//@@author A0127694U
public class ReminderTest {
    @Test
    public void toString_reminderWithNote() {
        Reminder reminder = new Reminder("note", LocalDateTime.of(2016, 1, 1, 17, 0));
        String expectedValue = "note (On 2016-01-01 5.00PM)";
        assertEquals(expectedValue, reminder.toString());
    }

    @Test
    public void toString_reminderWithoutNote() {
        Reminder reminder = new Reminder().setTimeOfReminder(LocalDateTime.of(2016, 1, 1, 17, 0));
        String expectedValue = "Reminder on 2016-01-01 5.00PM";
        assertEquals(expectedValue, reminder.toString());
    }

    @Test
    public void equals_otherObject_falseReturned() {
        Reminder reminder = new Reminder();
        assertFalse(reminder.equals(new Object()));
    }

    @Test
    public void equals_differentNote_falseReturned() {
        Reminder r1 = new Reminder("reminder 1", LocalDateTime.of(2016, 1, 1, 17, 0));
        Reminder r2 = new Reminder("reminder 2", LocalDateTime.of(2016, 1, 1, 17, 0));
        assertFalse(r1.equals(r2));
    }

    @Test
    public void equals_differentTime_falseReturned() {
        Reminder r1 = new Reminder("note", LocalDateTime.of(2016, 1, 1, 17, 0));
        Reminder r2 = new Reminder("note", LocalDateTime.of(2016, 1, 1, 17, 1));
        assertFalse(r1.equals(r2));
    }

    @Test
    public void equals_sameNoteAndTime_trueReturned() {
        Reminder r1 = new Reminder("note", LocalDateTime.of(2016, 1, 1, 17, 0));
        Reminder r2 = new Reminder("note", LocalDateTime.of(2016, 1, 1, 17, 0));
        assertTrue(r1.equals(r2));
    }

    @Test
    public void equals_noteUpperCase_trueReturned() {
        Reminder r1 = new Reminder("note", LocalDateTime.of(2016, 1, 1, 17, 0));
        Reminder r2 = new Reminder("NOTE", LocalDateTime.of(2016, 1, 1, 17, 0));
        assertTrue(r1.equals(r2));
    }

    @Test
    public void hashCode_sameNoteAndTime_sameHashCode() {
        Reminder r1 = new Reminder("note", LocalDateTime.of(2016, 1, 1, 17, 0));
        Reminder r2 = new Reminder("note", LocalDateTime.of(2016, 1, 1, 17, 0));
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    public void hashCode_noteUpperCase_sameHashCode() {
        Reminder r1 = new Reminder("note", LocalDateTime.of(2016, 1, 1, 17, 0));
        Reminder r2 = new Reminder("NOTE", LocalDateTime.of(2016, 1, 1, 17, 0));
        assertEquals(r1.hashCode(), r2.hashCode());
    }
}
