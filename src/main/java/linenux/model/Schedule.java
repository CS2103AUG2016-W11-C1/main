package linenux.model;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Contains all outstanding tasks.
 */
public class Schedule {
    public static final int MAX_STATES = 10;
    private final ObservableList<State> states = FXCollections.observableArrayList();

    /**
     * Constructs an empty schedule
     */
    public Schedule() {
        this.states.add(new State());
    }

    /**
     * Adds a task to the schedule
     */
    public void addTask(Task task) {
        addState(getMostRecentState().addTask(task));
    }

    /**
     * Replace {@code originalTask} with {@code newTask}.
     * @param originalTask The original task.
     * @param newTask The new ask.
     */
    public void updateTask(Task originalTask, Task newTask) {
        addState(getMostRecentState().updateTask(originalTask, newTask));
    }

    /**
     * Delete the specified task.
     *
     * @param task The task to delete.
     */
    public void deleteTask(Task task) {
        addState(getMostRecentState().deleteTask(task));
    }

    /**
     * Clears all tasks from the schedule
     */
    public void clear() {
        State newState = new State();
        addState(newState);
    }

    public ArrayList<Task> search(String keywords) {
        return search(keywords.split("\\s+"));
    }

    /**
     * Performs case-insensitive task search using keywords.
     *
     * @param keywords
     *            Search keywords
     * @return List of {@code Task} matching the keywords.
     */
    public ArrayList<Task> search(String[] keywords) {
        return getMostRecentState().search(keywords);
    }

    public ArrayList<Reminder> searchReminder(String keywords) {
        return searchReminder(keywords.split("\\s+"));
    }

    /**
     * Performs case-insensitive reminder search using keywords.
     *
     * @param keywords
     *            Search keywords
     * @return List of {@code Task} matching the keywords.
     */
    public ArrayList<Reminder> searchReminder(String[] keywords) {
        return getMostRecentState().searchReminder(keywords);
    }

    /**
     * Returns the list of states.
     */
    public ObservableList<State> getStates() {
        return states;
    }

    /**
     * Returns the list of tasks.
     */
    public ArrayList<Task> getTaskList() {
        return getMostRecentState().getTaskList();
    }

    /**
     * Returns the list of all task reminders.
     */
    public ArrayList<Reminder> getReminderList() {
        ArrayList<Reminder> result = new ArrayList<Reminder>();
        for (Task t : getMostRecentState().getTaskList()) {
            result.addAll(t.getReminders());
        }

        return result;
    }

    /**
     * Remove the last state if there are more than one.
     *
     * @return {@code true} if and only if a state is removed.
     */
    public boolean popState() {
        if (states.size() > 1) {
            states.remove(states.size() - 1);
            return true;
        }
        return false;
    }

    /**
     * Returns the most recent state of schedule
     */
    private State getMostRecentState() {
        return states.get(states.size() - 1);
    }

    /**
     * Adds a new state to states.
     * @param state
     */
    private void addState(State state) {
        while (states.size() + 1 > MAX_STATES && states.size() > 1) {
            states.remove(0);
        }
        states.add(state);
    }
}
