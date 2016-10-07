package linenux.model;

import java.time.LocalDateTime;

/**
 * Represents a reminder for a task.
 * Both note and timeOfReminder are required fields.
 */
public class Reminder {
    private String note;
    private LocalDateTime timeOfReminder;

    public Reminder(String note, LocalDateTime timeOfReminder) {
        this.note = note;
        this.timeOfReminder = timeOfReminder;
    }

    @Override
    public String toString() {
        return this.note;
    }

    /* Getters */

    public String getNote() {
        return this.note;
    }

    public LocalDateTime getTimeOfReminder() {
        return this.timeOfReminder;
    }

    /* Setters */

    public void setNote(String newNote) {
        this.note = newNote;
    }

    public void setTimeOfReminder(LocalDateTime newTimeOfReminder) {
        this.timeOfReminder = newTimeOfReminder;
    }
}
