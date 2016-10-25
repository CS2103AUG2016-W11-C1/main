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

    public Reminder() {
        this(null, null);
    }

    public Reminder(String note, LocalDateTime timeOfReminder) {
        this.note = note;
        this.timeOfReminder = timeOfReminder;
    }

    public Reminder(Reminder other) {
        this.note = other.note;
        this.timeOfReminder = other.timeOfReminder;
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

    public Reminder setNote(String newNote) {
        Reminder output = new Reminder(this);
        output.note = newNote;
        return output;
    }

    public Reminder setTimeOfReminder(LocalDateTime newTimeOfReminder) {
        Reminder output = new Reminder(this);
        output.timeOfReminder = newTimeOfReminder;
        return output;
    }
}
