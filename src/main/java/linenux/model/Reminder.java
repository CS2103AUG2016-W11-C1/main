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

    //@@author A0144915A
    public Reminder() {
        this(null, null);
    }

    //@@author A0135788M
    public Reminder(String note, LocalDateTime timeOfReminder) {
        this.note = note;
        this.timeOfReminder = timeOfReminder;
    }

    //@@author A0144915A
    public Reminder(Reminder other) {
        this.note = other.note;
        this.timeOfReminder = other.timeOfReminder;
    }

    @Override
    //@@author A0140702X
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma");

        if (this.note != null) {
            return this.note + " (On " + this.timeOfReminder.format(formatter) + ")";
        } else {
            return "Reminder on " + this.timeOfReminder.format(formatter);
        }
    }

    // @@author A0140702X
    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Reminder)) {
            return false;
        }

        Reminder otherReminder = (Reminder) other;

        assert this.getNote() != null;
        assert otherReminder.getNote() != null;

        // testing for equality is not case-sensitive
        if (!this.getNote().toLowerCase().equals(otherReminder.getNote().toLowerCase())) {
            return false;
        }

        assert this.getTimeOfReminder() != null;
        assert otherReminder.getTimeOfReminder() != null;

        if (!this.getTimeOfReminder().equals(otherReminder.getTimeOfReminder())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return note.hashCode() ^ timeOfReminder.hashCode();
    }

    /* Getters */

    //@@author A0135788M
    public String getNote() {
        return this.note;
    }

    public LocalDateTime getTimeOfReminder() {
        return this.timeOfReminder;
    }

    /* Setters */

    //@@author A0144915A
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
