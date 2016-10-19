package linenux.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Represents a task in the schedule. Only taskName is a required field and
 * cannot be an empty string.
 */
public class Task {
    private String taskName;
    private boolean isDone;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ArrayList<String> tags;
    private ArrayList<Reminder> reminders;

    /**
     * Constructor for tagged To-Dos (tasks with no deadlines or predetermined
     * time slots).
     *
     * @param taskName
     */
    public Task(String taskName) {
        this(taskName, new ArrayList<String>());
    }

    /**
     * Constructor for tagged Deadlines (tasks with deadlines only).
     *
     * @param taskName
     * @param endTime
     */
    public Task(String taskName, LocalDateTime endTime) {
        this(taskName, endTime, new ArrayList<String>());
    }

    /**
     * Constructor for tagged Events (tasks with predetermined time slots).
     *
     * @param taskName
     * @param startTime
     * @param endTime
     */
    public Task(String taskName, LocalDateTime startTime, LocalDateTime endTime) {
        this(taskName, startTime, endTime, new ArrayList<String>());
    }

    /**
     * Constructor for To-Dos (tasks with no deadlines or predetermined time
     * slots).
     */
    public Task(String taskName, ArrayList<String> tags) {
        this.taskName = taskName;
        this.isDone = false;
        this.startTime = null;
        this.endTime = null;
        this.tags = tags;
        this.reminders = new ArrayList<Reminder>();
    }

    /**
     * Constructor for Deadlines (tasks with deadlines only).
     */
    public Task(String taskName, LocalDateTime endTime, ArrayList<String> tags) {
        this.taskName = taskName;
        this.isDone = false;
        this.startTime = null;
        this.endTime = endTime;
        this.tags = tags;
        this.reminders = new ArrayList<Reminder>();
    }

    /**
     * Constructor for Events (tasks with predetermined time slots).
     */
    public Task(String taskName, LocalDateTime startTime, LocalDateTime endTime, ArrayList<String> tags) {
        this.taskName = taskName;
        this.isDone = false;
        this.startTime = startTime;
        this.endTime = endTime;
        this.tags = tags;
        this.reminders = new ArrayList<Reminder>();
    }

    /**
     * Copy constructor.
     * @param other The other {@code Task} to copy from.
     */
    public Task(Task other) {
        this.taskName = other.taskName;
        this.isDone = other.isDone;
        this.startTime = other.startTime;
        this.endTime = other.endTime;
        this.tags = new ArrayList<>(other.tags);
        this.reminders = new ArrayList<>(other.reminders);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma");

        if (this.isDeadline()) {
            return taskName + " (Due " + this.endTime.format(formatter) + ")" + tagsToString();
        } else if (this.isEvent()) {
            return taskName + " (" + this.startTime.format(formatter) + " - " + this.endTime.format(formatter) + ")"
                    + tagsToString();
        } else {
            return taskName + tagsToString();
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

    public boolean isDone() {
        return isDone == true;
    }

    public boolean isNotDone() {
        return isDone == false;
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

    public ArrayList<String> getTags() {
        return this.tags;
    }

    public ArrayList<Reminder> getReminders() {
        return this.reminders;
    }

    /* Setters */

    public Task markAsDone() {
        Task output = new Task(this);
        output.isDone = true;
        return output;
    }

    public Task addReminder(Reminder reminder) {
        Task output = new Task(this);
        output.reminders.add(reminder);
        return output;
    }

    private String tagsToString() {
        StringBuilder builder = new StringBuilder();
        if (this.tags.isEmpty()) {
            return "";
        }

        builder.append(" [");
        builder.append("Tags:");
        for (String s : this.tags) {
            builder.append(" \"");
            builder.append(s);
            builder.append("\"");
        }
        builder.append(" ]");

        return builder.toString();
    }
}
