package linenux.model;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import linenux.command.util.ReminderSearchResult;
import linenux.util.ArrayListUtil;

//@@author A0135788M
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
    public Schedule() {
        this.states.add(new State());
    }

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
    public void updateTask(Task originalTask, Task newTask) {
        addState(getMostRecentState().updateTask(originalTask, newTask));
    }

    public void updateTask(ArrayList<Task> originalTasks, ArrayList<Task> newTasks) {
        addState(getMostRecentState().updateTasks(originalTasks, newTasks));
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
     * Deletes the specified reminder.
     *
     * @param reminder
     *            The reminder to delete.
     */
    public void deleteReminder(ReminderSearchResult reminder) {
        addState(getMostRecentState().deleteReminder(reminder));
    }

    /**
     * Delete the specified list of tasks.
     *
     * @param tasks
     *            The tasks to delete.
     */
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

    /**
     * Performs case-insensitive tag search using keywords.
     *
     * @param tagName Tag name to look for.
     * @return List of {@code Task} with tags matching the keywords.
     */
    public ArrayList<Task> searchTag(String tagName) {
        return getMostRecentState().searchTag(tagName);
    }

    /**
     * Search reminders based on keywords.
     * @param keywords The keywords to search for.
     * @return An {@code ArrayList} of {@code Reminder} matching the keywords.
     */
    public ArrayList<Reminder> searchReminder(String keywords) {
        return searchReminder(keywords.split("\\s+"));
    }

    /**
     * Performs case-insensitive reminder search using keywords.
     *
     * @param keywords Search keywords
     * @return List of {@code Reminder} matching the keywords.
     */
    public ArrayList<Reminder> searchReminder(String[] keywords) {
        return getMostRecentState().searchReminder(keywords);
    }

    /**
     * Checks if the task given is a unique task.
     */
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
    public ObservableList<State> getStates() {
        return states;
    }

    public ObservableList<ArrayList<Task>> getFilteredTaskList() {
        return filteredTaskList;
    }

    /**
     * Returns the list of tasks.
     */
    public ArrayList<Task> getTaskList() {
        return getMostRecentState().getTaskList();
    }

    /**
     * Returns the list of filtered tasks.
     */
    public ArrayList<Task> getFilteredTasks() {
        if (filteredTaskList.isEmpty()) {
            return getTaskList();
        }

        return filteredTaskList.get(0);
    }

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
     * Replace the content of the schedule with another schedule.
     */
    public void update(Schedule other) {
        this.states.setAll(other.states);
        this.filteredTaskList.setAll(other.filteredTaskList);
    }

    /**
     * Adds a new list of filtered tastes into filteredTaskList
     * @param filteredTasks
     */
    public void addFilterTasks(ArrayList<Task> filteredTasks) {
        filteredTaskList.clear();
        filteredTaskList.add(filteredTasks);
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
