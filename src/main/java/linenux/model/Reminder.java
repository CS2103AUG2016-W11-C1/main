package linenux.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public Reminder(Reminder other) {
        this.note = other.note;
        this.timeOfReminder = other.timeOfReminder;
    }

    /**
     * Creates a copy of the reminder.
     * @return
     */
    public Reminder copyReminder() {
        return new Reminder(this.getNote(), this.getTimeOfReminder());
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma");

        if (this.note != null) {
            return this.note + " (On " + this.timeOfReminder.format(formatter) + ")";
        } else {
            return "Reminder on " + this.timeOfReminder.format(formatter);
        }
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
