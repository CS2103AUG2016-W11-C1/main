package linenux.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

import linenux.util.ArrayListUtil;

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

    //@@author A0127694U
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

    //@@author A0135788M
    /**
     * Constructor for To-Dos (tasks with no deadlines or predetermined time
     * slots).
     */
    public Task(String taskName, ArrayList<String> tags) {
        this(taskName, false, null, null, tags, new ArrayList<Reminder>());
    }

    /**
     * Constructor for Deadlines (tasks with deadlines only).
     */
    public Task(String taskName, LocalDateTime endTime, ArrayList<String> tags) {
        this(taskName, false, null, endTime, tags, new ArrayList<Reminder>());
    }

    //@@author A0135788M
    /**
     * Constructor for Events (tasks with predetermined time slots).
     */
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

    //@@author A0144915A
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
    public boolean hasTag(String tag) {
        for (String s : tags) {
            if (s.equalsIgnoreCase(tag)) {
                return true;
            }
        }
        return false;
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
    public Task removeReminder(Reminder reminder) {
        Task output = new Task(this);
        output.reminders.remove(reminder);
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

    //@author A0144915A
    public ArrayList<Reminder> searchReminder(String keywords) {
        return this.searchReminder(keywords.split("\\s+"));
    }

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
