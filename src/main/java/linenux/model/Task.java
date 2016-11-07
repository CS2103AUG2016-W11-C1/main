package linenux.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

import linenux.util.ArrayListUtil;

//@@author A0135788M
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
        this(taskName, startTime, endTime, new ArrayList<>());
    }

    /**
     * Constructor for To-Dos (tasks with no deadlines or predetermined time
     * slots).
     */
    public Task(String taskName, ArrayList<String> tags) {
        this(taskName, false, null, null, tags, new ArrayList<>());
    }

    /**
     * Constructor for Deadlines (tasks with deadlines only).
     */
    public Task(String taskName, LocalDateTime endTime, ArrayList<String> tags) {
        this(taskName, false, null, endTime, tags, new ArrayList<>());
    }

    /**
     * Constructor for Events (tasks with predetermined time slots).
     */
    public Task(String taskName, LocalDateTime startTime, LocalDateTime endTime, ArrayList<String> tags) {
        this(taskName, false, startTime, endTime, tags, new ArrayList<>());
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
    public Task(Task other) {
        this.taskName = other.taskName;
        this.isDone = other.isDone;
        this.startTime = other.startTime;
        this.endTime = other.endTime;
        this.tags = new ArrayList<>(other.tags);
        this.reminders = new ArrayList<>(other.reminders);
    }

    /**
     * @return The textual representation of the {@code Task}.
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h.mma");

        if (this.isDeadline()) {
            return taskName + " (Due " + this.endTime.format(formatter) + ")" + tagsToString();
        } else if (this.isEvent()) {
            return taskName + " (" + this.startTime.format(formatter) + " - " + this.endTime.format(formatter) + ")"
                    + tagsToString();
        } else {
            return taskName + tagsToString();
        }
    }

    /**
     * @return {@code true} if and only if this {@code Task} is a todo.
     */
    public boolean isTodo() {
        return startTime == null && endTime == null;
    }

    /**
     * @return {@code true} if and only if this {@code Task} is a deadline.
     */
    public boolean isDeadline() {
        return startTime == null && endTime != null;
    }

    /**
     * @return {@code true} if and only if this {@code Task} is an event.
     */
    public boolean isEvent() {
        return startTime != null && endTime != null;
    }

    /**
     * @return {@code true} if and only if this {@code Task} is marked as done.
     */
    public boolean isDone() {
        return isDone == true;
    }

    /**
     * @return {@code true} if and only if this {@code Task} is not marked as done.
     */
    public boolean isNotDone() {
        return isDone == false;
    }

    /**
     * @param other Another {@code Object} to compare with.
     * @return {@code true} if and only if this {@code Task} is equal to {@code other}.
     */
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

        return true;
    }

    /**
     * @return The correct hash code for the current {@code Task}.
     */
    @Override
    public int hashCode() {
        int result = taskName.hashCode() ^ startTime.hashCode() ^ endTime.hashCode();

        return result;
    }

    /**
     * @param tag The tag to check.
     * @return {@code true} if and only if this {@code Task} has {@code tag}.
     */
    public boolean hasTag(String tag) {
        ArrayList<String> lowercaseTags = new ArrayListUtil.ChainableArrayListUtil<>(tags).map(String::toLowerCase)
                .value();
        return lowercaseTags.contains(tag.toLowerCase());
    }

    /**
     * @return The name of the task.
     */
    public String getTaskName() {
        return this.taskName;
    }

    /**
     * @return The start time of the task.
     */
    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    /**
     * @return The end time of the task.
     */
    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    /**
     * @return An {@code ArrayList} of tags.
     */
    public ArrayList<String> getTags() {
        return this.tags;
    }

    /**
     * @return An {@code ArrayList} of {@code Reminder}.
     */
    public ArrayList<Reminder> getReminders() {
        return this.reminders;
    }

    /**
     * @param taskName The new task name.
     * @return A new {@code Task} with the updated name.
     */
    public Task setTaskName(String taskName) {
        Task output = new Task(this);
        output.taskName = taskName;
        return output;
    }

    /**
     * @param startTime The new start time.
     * @return A new {@code Task} with the updated start time.
     */
    public Task setStartTime(LocalDateTime startTime) {
        Task output = new Task(this);
        output.startTime = startTime;
        return output;
    }

    /**
     * @param endTime The new end time.
     * @return A new {@code Task} with the updated end time.
     */
    public Task setEndTime(LocalDateTime endTime) {
        Task output = new Task(this);
        output.endTime = endTime;
        return output;
    }

    /**
     * @return A new {@code Task} by marking the current {@code Task} as done.
     */
    public Task markAsDone() {
        Task output = new Task(this);
        output.isDone = true;
        return output;
    }

    //A0140702X
    /**
     * @return A new {@code Task} by marking the current {@code Task} as undone.
     */
    public Task markAsUndone() {
        Task output = new Task(this);
        output.isDone = false;
        return output;
    }

    /**
     * @param reminder The {@code Reminder} to add.
     * @return A new {@code Task} by adding a new reminder.
     */
    public Task addReminder(Reminder reminder) {
        Task output = new Task(this);
        output.reminders.add(reminder);
        return output;
    }

    /**
     * @param tags The new tag to add.
     * @return A new {@code Task} by adding a new tag.
     */
    public Task setTags(ArrayList<String> tags) {
        Task output = new Task(this);
        output.tags = tags;
        return output;
    }

    /**
     * @param reminder The {@code Reminder} to remove from this {@code Task}.
     * @return A new {@code Task} by removing {@code reminder}.
     */
    public Task removeReminder(Reminder reminder) {
        Task output = new Task(this);
        output.reminders.remove(reminder);
        return output;
    }

    /**
     * @return The textual representation of the tags.
     */
    private String tagsToString() {
        StringBuilder builder = new StringBuilder();
        if (this.tags.isEmpty()) {
            return "";
        }

        builder.append(" [ ");
        builder.append("Tags:");
        for (String s : this.tags) {
            builder.append(" \"");
            builder.append(s);
            builder.append("\"");
        }
        builder.append(" ]");

        return builder.toString();
    }

    /**
     * @param keywords The search keywords.
     * @return An {@code ArrayList} of {@code Reminder} matching the keywords.
     */
    public ArrayList<Reminder> searchReminder(String keywords) {
        return this.searchReminder(keywords.split("\\s+"));
    }

    /**
     * @param keywords The search keywords.
     * @return An {@code ArrayList} of {@code Reminder} matching the keywords.
     */
    public ArrayList<Reminder> searchReminder(String[] keywords) {
        ArrayList<String> keywordsList = new ArrayListUtil.ChainableArrayListUtil<>(keywords)
                .map(String::toLowerCase)
                .value();

        return new ArrayListUtil.ChainableArrayListUtil<>(this.reminders)
                .filter(task -> { ArrayList<String> reminderKeywords =
                        new ArrayListUtil.ChainableArrayListUtil<>(task.getNote().split("\\s+"))
                                .map(String::toLowerCase)
                                .value();
                    return !Collections.disjoint(keywordsList, reminderKeywords);
                })
                .value();
    }
}
