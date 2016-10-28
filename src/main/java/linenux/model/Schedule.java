package linenux.model;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import linenux.util.ArrayListUtil;

/**
 * Contains all outstanding tasks.
 */
public class Schedule {
    public static final int MAX_STATES = 10;
    private final ObservableList<State> states = FXCollections.observableArrayList();
    private final ObservableList<ArrayList<Task>> filteredTaskList = FXCollections.observableArrayList();

    /**
     * Constructs an empty schedule
     */
    //@@author A0144915A
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

    /**
     * Replace {@code originalTask} with {@code newTask}.
     * @param originalTask The original task.
     * @param newTask The new ask.
     */
    //@@author A0144915A
    public void updateTask(Task originalTask, Task newTask) {
        addState(getMostRecentState().updateTask(originalTask, newTask));
    }

    /**
     * Delete the specified task.
     *
     * @param task The task to delete.
     */
    //@@author A0135788M
    public void deleteTask(Task task) {
        addState(getMostRecentState().deleteTask(task));
    }

    /**
     * Delete the specified list of tasks.
     *
     * @param tasks The tasks to delete.
     */
    //@@author A0140702X
    public void deleteTasks(ArrayList<Task> tasks) {
        State newState = getMostRecentState();

        for (Task task : tasks) {
            newState = newState.deleteTask(task);
        }

        addState(newState);
    }

    /**
     * Clears all tasks from the schedule
     */
    //@@author A0135788M
    public void clear() {
        State newState = new State();
        addState(newState);
    }

    //@@author A0144915A
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
    //@@author A0135788M
    public ArrayList<Task> search(String[] keywords) {
        return getMostRecentState().search(keywords);
    }

    //@@author A0127694U
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
    //@@author A0127694U
    public ArrayList<Reminder> searchReminder(String[] keywords) {
        return getMostRecentState().searchReminder(keywords);
    }

    // @@author A0140702X
    public boolean isUniqueTask(Task task) {
        ArrayList<Task> taskList = getMostRecentState().getTaskList();
        ArrayList<Task> duplicateTaskList = new ArrayListUtil.ChainableArrayListUtil<>(taskList)
                .filter(a -> a.equals(task)).value();

        if (duplicateTaskList.size() >= 1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns the list of states.
     */
    //@@author A0135788M
    public ObservableList<State> getStates() {
        return states;
    }

    //@@author A0140702X
    public ObservableList<ArrayList<Task>> getFilteredTaskList() {
        return filteredTaskList;
    }

    /**
     * Returns the list of tasks.
     */
    //@@author A0135788M
    public ArrayList<Task> getTaskList() {
        return getMostRecentState().getTaskList();
    }

    /**
     * Returns the list of filtered tasks.
     */
    //@@author A0140702X
    public ArrayList<Task> getFilteredTasks() {
        if (filteredTaskList.isEmpty()) {
            return getTaskList();
        }

        return filteredTaskList.get(0);
    }

    /**
     * Returns the list of all task reminders.
     */
    //@@author A0127694U
    public ArrayList<Reminder> getReminderList() {
        ArrayList<Reminder> result = new ArrayList<>();
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
    //@@author A0144915A
    public boolean popState() {
        if (states.size() > 1) {
            states.remove(states.size() - 1);
            return true;
        }
        return false;
    }

    /**
     * Adds a new list of filtered tastes into filteredTaskList
     * @param filteredTasks
     */
    //@@author A0140702X
    public void addFilterTasks(ArrayList<Task> filteredTasks) {
        filteredTaskList.clear();
        filteredTaskList.add(filteredTasks);
    }

    /**
     * Returns the most recent state of schedule
     */
    //@@author A0144915A
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
