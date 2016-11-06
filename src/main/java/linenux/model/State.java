package linenux.model;

import java.util.ArrayList;
import java.util.Collections;

import linenux.command.util.ReminderSearchResult;
import linenux.util.ArrayListUtil;

/**
 * Represents a snapshot in time of a schedule. The State class is immutable.
 */
public class State {
    private final ArrayList<Task> tasks;

    /**
     * Construct an empty {@code State}.
     */
    public State(){
        this.tasks = new ArrayList<>();
    }

    /**
     * Copy constructor.
     * @param other The {@code State} to copy from.
     */
    public State(State other) {
        this.tasks = new ArrayList<>();

        for (Task t : other.tasks) {
            this.tasks.add(new Task(t.getTaskName(), t.isDone(), t.getStartTime(), t.getEndTime(), t.getTags(),
                    t.getReminders()));
        }
    }

    /**
     * Construct a {@code State} with the given list of tasks.
     * @param taskList An {@code ArrayList} of {@code Task}.
     */
    public State(ArrayList<Task> taskList) {
        this.tasks = taskList;
    }

    /**
     * Adds a task to the schedule
     * @param task The task to add.
     * @return The new State of the schedule.
     */
    public State addTask(Task task) {
        State output = new State(this);
        output.tasks.add(task);
        return output;
    }

    /**
     * Updates the specified task.
     *
     * @param originalTask The original version of the specified task.
     * @param newTask The edited version of the specified task.
     */
    public State updateTask(Task originalTask, Task newTask) {
        int taskIndex = tasks.indexOf(originalTask);
        State output = new State(this);
        output.tasks.set(taskIndex, newTask);
        return output;
    }

    // @@author A0127694U
    /**
     * Updates the specified task.
     *
     * @param originalTasks
     *            The original version of the specified task.
     * @param newTasks
     *            The edited version of the specified task.
     */
    public State updateTasks(ArrayList<Task> originalTasks, ArrayList<Task> newTasks) {
        State output = new State(this);
        for (int i = 0; i < originalTasks.size(); i++) {
            int taskIndex = tasks.indexOf(originalTasks.get(i));
            output.tasks.set(taskIndex, newTasks.get(i));
        }
        return output;
    }

    // @@author A0135788M
    /**
     * Delete the specified task.
     *
     * @param task
     *            The task to delete.
     * @return The new State of the schedule.
     */
    public State deleteTask(Task task) {
        int taskIndex = tasks.indexOf(task);
        State output = new State(this);
        output.tasks.remove(taskIndex);
        return output;
    }

    // @@author A0127694U
    /**
     * Delete the specified reminder.
     *
     * @param reminder
     *            The reminder to delete.
     * @return The new State of the schedule.
     */
    public State deleteReminder(ReminderSearchResult reminder) {
        assert (reminder.getReminders().size() == 1);

        int taskIndex = this.tasks.indexOf(reminder.getTask());
        State output = new State(this);
        output.tasks.set(taskIndex, output.tasks.get(taskIndex).removeReminder(reminder.getReminders().get(0)));
        return output;
    }

    /**
     * Returns the list of tasks.
     */
    public ArrayList<Task> getTaskList() {
        return tasks;
    }

    /**
     * Performs case-insensitive task search using keywords.
     *
     * @param keywords
     *            Search keywords
     * @return List of {@code Task} matching the keywords.
     */
    public ArrayList<Task> search(String[] keywords) {
        ArrayList<String> keywordsList = new ArrayListUtil.ChainableArrayListUtil<>(keywords)
                .map(String::toLowerCase)
                .value();

        return new ArrayListUtil.ChainableArrayListUtil<>(this.tasks)
                .filter(task -> { ArrayList<String> taskKeywords =
                        new ArrayListUtil.ChainableArrayListUtil<>(task.getTaskName().split("\\s+"))
                                .map(String::toLowerCase)
                                .value();
                    return !Collections.disjoint(keywordsList, taskKeywords);
                })
                .value();
    }

    /**
     * Performs case-insensitive reminder search using keywords.
     *
     * @param keywords
     *            Search keywords
     * @return List of {@code Task} matching the keywords.
     */
    public ArrayList<Reminder> searchReminder(String[] keywords) {
        ArrayList<Reminder> result = new ArrayList<>();

        for (Task t : this.tasks) {
            result.addAll(searchReminder(keywords, t));
        }

        return result;
    }

    /**
     * Search reminders based on keywords.
     * @param keywords The search keywords.
     * @param task The task whose reminders will form the search space.
     * @return An {@code ArrayList} of {@code Reminder} matching the keywords.
     */
    public ArrayList<Reminder> searchReminder(String[] keywords, Task task) {
        ArrayList<String> keywordsList = new ArrayListUtil.ChainableArrayListUtil<>(keywords)
                .map(String::toLowerCase).value();

        return new ArrayListUtil.ChainableArrayListUtil<>(task.getReminders()).filter(reminder -> {
            ArrayList<String> reminderKeywords = new ArrayListUtil.ChainableArrayListUtil<>(
                    reminder.getNote().split("\\s+")).map(String::toLowerCase).value();
            return !Collections.disjoint(keywordsList, reminderKeywords);
        }).value();
    }

    // @@author A0127694U
    /**
     * Performs case-insensitive tag search using keywords.
     *
     * @param tagName
     *            Search keywords
     * @return List of {@code Task} matching the keywords.
     */
    public ArrayList<Task> searchTag(String tagName) {
        ArrayList<Task> result = this.tasks;

        result = new ArrayListUtil.ChainableArrayListUtil<>(result).filter(task -> task.hasTag(tagName)).value();

        return result;
    }
}
