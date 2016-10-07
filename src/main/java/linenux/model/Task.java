package linenux.model;

import java.time.LocalDateTime;

/**
 * Represents a task in the schedule.
 * Only taskName is a required field and cannot be an empty string.
 */
public class Task {
    private String taskName;
    private boolean isDone;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    /**
     * Constructor for tasks with no deadlines or predetermined time slots.
     */
    public Task(String taskName) {
        this.taskName = taskName;
        this.isDone = false;
        this.startTime = null;
        this.endTime = null;
    }

    /**
     * Constructor for tasks with deadlines only.
     */
    public Task(String taskName, LocalDateTime endTime) {
        this.taskName = taskName;
        this.isDone = false;
        this.startTime = null;
        this.endTime = endTime;
    }

    /**
     * Constructor for tasks with predetermined time slots.
     */
    public Task(String taskName, LocalDateTime startTime, LocalDateTime endTime) {
        this.taskName = taskName;
        this.isDone = false;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return taskName;
    }

    /* Getters */

    public String getTaskName() {
        return taskName;
    }

    public boolean isDone() {
        return isDone;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
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
