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
    private final ObservableList<ArrayList<Task>> filteredTaskList = FXCollections.observableArrayList();

    //@@author A0144915A
    /**
     * Constructs an empty schedule
     */
    public Schedule() {
        this.states.add(new State());
    }

    //@@author A0135788M
    public Schedule(State s) {
        this.states.add(s);
    }

    /**
     * Adds a task to the schedule
     */
    public void addTask(Task task) {
        addState(getMostRecentState().addTask(task));
    }

    //@@author A0144915A
    /**
     * Replace {@code originalTask} with {@code newTask}.
     * @param originalTask The original task.
     * @param newTask The new ask.
     */
    public void updateTask(Task originalTask, Task newTask) {
        addState(getMostRecentState().updateTask(originalTask, newTask));
    }

    //@@author A0135788M
    /**
     * Delete the specified task.
     *
     * @param task The task to delete.
     */
    public void deleteTask(Task task) {
        addState(getMostRecentState().deleteTask(task));
    }

    //@@author A0140702X
    /**
     * Delete the specified list of tasks.
     *
     * @param tasks The tasks to delete.
     */
    public void deleteTasks(ArrayList<Task> tasks) {
        State newState = getMostRecentState();

        for (Task task : tasks) {
            newState = newState.deleteTask(task);
        }

        addState(newState);
    }

    //@@author A0135788M
    /**
     * Clears all tasks from the schedule
     */
    public void clear() {
        State newState = new State();
        addState(newState);
    }

    //@@author A0144915A
    public ArrayList<Task> search(String keywords) {
        return search(keywords.split("\\s+"));
    }

    //@@author A0135788M
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

    //@@author A0127694U
    public ArrayList<Reminder> searchReminder(String keywords) {
        return searchReminder(keywords.split("\\s+"));
    }

    //@@author A0127694U
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

    //@@author A0135788M
    /**
     * Returns the list of states.
     */
    public ObservableList<State> getStates() {
        return states;
    }

    //@@author A0140702X
    public ObservableList<ArrayList<Task>> getFilteredTaskList() {
        return filteredTaskList;
    }

    //@@author A0135788M
    /**
     * Returns the list of tasks.
     */
    public ArrayList<Task> getTaskList() {
        return getMostRecentState().getTaskList();
    }

    //@@author A0140702X
    /**
     * Returns the list of filtered tasks.
     */
    public ArrayList<Task> getFilteredTasks() {
        if (filteredTaskList.isEmpty()) {
            return getTaskList();
        }

        return filteredTaskList.get(0);
    }

    //@@author A0127694U
    /**
     * Returns the list of all task reminders.
     */
    public ArrayList<Reminder> getReminderList() {
        ArrayList<Reminder> result = new ArrayList<>();
        for (Task t : getMostRecentState().getTaskList()) {
            result.addAll(t.getReminders());
        }

        return result;
    }

    //@@author A0144915A
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

    //@@author A0140702X
    /**
     * Adds a new list of filtered tastes into filteredTaskList
     * @param filteredTasks
     */
    public void addFilterTasks(ArrayList<Task> filteredTasks) {
        filteredTaskList.clear();
        filteredTaskList.add(filteredTasks);
    }

    //@@author A0144915A
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
