package linenux.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Represents a task in the schedule.
 * Only taskName is a required field and cannot be an empty string.
 */
public class Task {
    private String taskName;
    private boolean isDone;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ArrayList<Reminder> reminders;

    /**
     * Constructor for tasks with no deadlines or predetermined time slots.
     */
    public Task(String taskName) {
        this.taskName = taskName;
        this.isDone = false;
        this.startTime = null;
        this.endTime = null;
        this.reminders = new ArrayList<Reminder>();
    }

    /**
     * Constructor for tasks with deadlines only.
     */
    public Task(String taskName, LocalDateTime endTime) {
        this.taskName = taskName;
        this.isDone = false;
        this.startTime = null;
        this.endTime = endTime;
        this.reminders = new ArrayList<Reminder>();
    }

    /**
     * Constructor for tasks with predetermined time slots.
     */
    public Task(String taskName, LocalDateTime startTime, LocalDateTime endTime) {
        this.taskName = taskName;
        this.isDone = false;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reminders = new ArrayList<Reminder>();
    }

    @Override
    public String toString() {
        return this.taskName;
    }

    /* Getters */

    public String getTaskName() {
        return this.taskName;
    }

    public boolean isDone() {
        return this.isDone;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    public ArrayList<Reminder> getReminders() {
        return this.reminders;
    }

    /* Setters */

     public void setTaskName(String taskName) {
         this.taskName = taskName;
     }

     public void markAsDone() {
         this.isDone = true;
     }

     public void setStartTime(LocalDateTime startTime) {
         this.startTime = startTime;
     }

     public void setEndTime(LocalDateTime endTime) {
         this.endTime = endTime;
     }
}
