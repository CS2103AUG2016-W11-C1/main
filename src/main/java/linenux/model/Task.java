package linenux.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;

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
     * Constructor for To-Dos (tasks with no deadlines or predetermined time slots).
     */
    public Task(String taskName) {
        this.taskName = taskName;
        this.isDone = false;
        this.startTime = null;
        this.endTime = null;
        this.reminders = new ArrayList<Reminder>();
    }

    /**
     * Constructor for Deadlines (tasks with deadlines only).
     */
    public Task(String taskName, LocalDateTime endTime) {
        this.taskName = taskName;
        this.isDone = false;
        this.startTime = null;
        this.endTime = endTime;
        this.reminders = new ArrayList<Reminder>();
    }

    /**
     * Constructor for Events (tasks with predetermined time slots).
     */
    public Task(String taskName, LocalDateTime startTime, LocalDateTime endTime) {
        this.taskName = taskName;
        this.isDone = false;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reminders = new ArrayList<Reminder>();
    }

    public boolean isDone() {
        return (isDone == true);
    }

    public boolean isNotDone() {
        return (isDone == false);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma");

        if (this.isDeadline()) {
            return taskName + " (Due " + this.endTime.format(formatter) + ")";
        } else if (this.isEvent()) {
            return taskName + " (" + this.startTime.format(formatter) + " - " + this.endTime.format(formatter) + ")";
        } else {
            return taskName;
        }
    }

    public boolean isTodo() {
        return startTime == null && endTime == null;
    }

    public boolean isDeadline() {
        return startTime == null && endTime != null;
    }

    public boolean isEvent() {
        return startTime != null && endTime != null;
    }

    /* Getters */

    public String getTaskName() {
        return this.taskName;
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
