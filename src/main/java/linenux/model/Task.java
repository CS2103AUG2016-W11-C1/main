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
    //@@author A0127694U
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
    //@@author A0135788M
    public Task(String taskName, ArrayList<String> tags) {
        this(taskName, false, null, null, tags, new ArrayList<Reminder>());
    }

    /**
     * Constructor for Deadlines (tasks with deadlines only).
     */
    public Task(String taskName, LocalDateTime endTime, ArrayList<String> tags) {
        this(taskName, false, null, endTime, tags, new ArrayList<Reminder>());
    }

    /**
     * Constructor for Events (tasks with predetermined time slots).
     */
    //@@author A0135788M
    public Task(String taskName, LocalDateTime startTime, LocalDateTime endTime, ArrayList<String> tags) {
        this(taskName, false, startTime, endTime, tags, new ArrayList<Reminder>());
    }

    /**
     * Constructor for all Tasks
     */
    public Task(String taskName, boolean isDone, LocalDateTime startTime, LocalDateTime endTime, ArrayList<String> tags, ArrayList<Reminder> reminders) {
        this.taskName = taskName;
        this.isDone = isDone;
        this.startTime = startTime;
        this.endTime = endTime;
        this.tags = tags;
        this.reminders = reminders;
    }

    /**
     * Copy constructor.
     * @param other The other {@code Task} to copy from.
     */
    //@@author A0144915A
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

    //@@author A0144915A
    public boolean isTodo() {
        return startTime == null && endTime == null;
    }

    public boolean isDeadline() {
        return startTime == null && endTime != null;
    }

    public boolean isEvent() {
        return startTime != null && endTime != null;
    }

    //@@author A0135788M
    public boolean isDone() {
        return isDone == true;
    }

    public boolean isNotDone() {
        return isDone == false;
    }

    //@@author A0140702X
    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Task)) {
            return false;
        }

        Task otherTask = (Task) other;

        // equality of task name should be case insensitive
        if (!this.getTaskName().toLowerCase().equals(otherTask.getTaskName().toLowerCase())) {
            return false;
        }

        LocalDateTime thisStartTime = this.getStartTime();
        LocalDateTime otherStartTime = otherTask.getStartTime();

        if (thisStartTime == null && otherStartTime == null) {
            // do nothing and proceed to check other fields.
        } else {
            if (thisStartTime == null || otherStartTime == null) {
                return false;
            }

            if (!thisStartTime.equals(otherStartTime)) {
                return false;
            }
        }

        LocalDateTime thisEndTime = this.getEndTime();
        LocalDateTime otherEndTime = otherTask.getEndTime();

        if (thisEndTime == null && otherEndTime == null) {
            // do nothing and proceed to check other fields.
        } else {
            if (thisEndTime == null || otherEndTime == null) {
                return false;
            }

            if (!thisEndTime.equals(otherEndTime)) {
                return false;
            }
        }

        //checking tags
        if (this.tags.size() != otherTask.getTags().size()) {
            return false;
        } else {
            ArrayList<String> otherTags = otherTask.getTags();
            for (String tag : this.tags) {
                if (!otherTags.contains(tag)) {
                    return false;
                }
            }
        }

        // checking reminders
        if (this.reminders.size() != otherTask.getReminders().size()) {
            return false;
        } else {
            ArrayList<Reminder> otherReminders = otherTask.getReminders();
            for (Reminder reminder : this.reminders) {
                if (!otherReminders.contains(reminder)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = taskName.hashCode() ^ startTime.hashCode() ^ endTime.hashCode();

        if (!tags.isEmpty()) {
            for (String tag : tags) {
                result = result ^ tag.hashCode();
            }
        }

        if (!reminders.isEmpty()) {
            for (Reminder reminder : reminders) {
                result = result ^ reminder.hashCode();
            }
        }

        return result;
    }

    //@@author A1234567A
    public boolean hasTag(String tag) {
        return tags.contains(tag);
    }

    /* Getters */

    //@@author A0135788M
    public String getTaskName() {
        return this.taskName;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    //@@author A0127694U
    public ArrayList<String> getTags() {
        return this.tags;
    }

    //@@author A0135788M
    public ArrayList<Reminder> getReminders() {
        return this.reminders;
    }

    /* Setters */

    //@@author A0144915A
    public Task setTaskName(String taskName) {
        Task output = new Task(this);
        output.taskName = taskName;
        return output;
    }

    public Task setStartTime(LocalDateTime startTime) {
        Task output = new Task(this);
        output.startTime = startTime;
        return output;
    }

    public Task setEndTime(LocalDateTime endTime) {
        Task output = new Task(this);
        output.endTime = endTime;
        return output;
    }

    public Task markAsDone() {
        Task output = new Task(this);
        output.isDone = true;
        return output;
    }

    //@@author A0144915A
    public Task addReminder(Reminder reminder) {
        Task output = new Task(this);
        output.reminders.add(reminder);
        return output;
    }

    //@@author A0144915A
    public Task setTags(ArrayList<String> tags) {
        Task output = new Task(this);
        output.tags = tags;
        return output;
    }

    //@@author A0127694U
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
