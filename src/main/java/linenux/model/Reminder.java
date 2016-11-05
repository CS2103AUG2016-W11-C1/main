package linenux.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//@@author A0127694U
/**
 * Represents a reminder for a task.
 * Both note and timeOfReminder are required fields.
 */
public class Reminder {
    private String note;
    private LocalDateTime timeOfReminder;

    /**
     * Instantiates a {@code Reminder} with no note and time.
     */
    public Reminder() {
        this(null, null);
    }

    /**
     * Instantiates a {@code Reminder} with the specified note and time.
     * @param note The note of the reminder.
     * @param timeOfReminder The time of the reminder.
     */
    public Reminder(String note, LocalDateTime timeOfReminder) {
        this.note = note;
        this.timeOfReminder = timeOfReminder;
    }

    /**
     * The copy constructor of {@code Reminder}.
     * @param other The other instance of {@code Reminder} to copy from.
     */
    public Reminder(Reminder other) {
        this.note = other.note;
        this.timeOfReminder = other.timeOfReminder;
    }

    /**
     * @return The textual representation of the {@code Reminder}.
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h.mma");

        if (this.note != null) {
            return this.note + " (On " + this.timeOfReminder.format(formatter) + ")";
        } else {
            return "Reminder on " + this.timeOfReminder.format(formatter);
        }
    }

    /**
     * Check equality.
     * @param other Another {@code Object} to compare with.
     * @return {@code true} if and only if they are equal.
     */
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

    /**
     * @return The correct hash code for {@code Reminder}.
     */
    @Override
    public int hashCode() {
        return note.hashCode() ^ timeOfReminder.hashCode();
    }

    /* Getters */

    /**
     * @return The note of the reminder.
     */
    public String getNote() {
        return this.note;
    }

    /**
     * @return The time of the reminder.
     */
    public LocalDateTime getTimeOfReminder() {
        return this.timeOfReminder;
    }

    /* Setters */

    /**
     * Update the note of the reminder.
     * @param newNote The new note.
     * @return A new {@code Reminder} with the note updated.
     */
    public Reminder setNote(String newNote) {
        Reminder output = new Reminder(this);
        output.note = newNote;
        return output;
    }

    /**
     * Update the time of the reminder.
     * @param newTimeOfReminder The new time
     * @return A new {@code Reminder} with the time updated.
     */
    public Reminder setTimeOfReminder(LocalDateTime newTimeOfReminder) {
        Reminder output = new Reminder(this);
        output.timeOfReminder = newTimeOfReminder;
        return output;
    }
}
