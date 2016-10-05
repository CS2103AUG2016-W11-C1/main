package linenux.model;

import java.util.ArrayList;

/**
 * Contains all outstanding tasks.
 */
public class Schedule {
    private final ArrayList<Task> taskList;

    /**
     * Constructs an empty schedule
     */
    public Schedule() {
        this.taskList = new ArrayList<Task>();
    }

    /**
     * Constructs the schedule with the given data.
     */
    public Schedule(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }

    /**
     * Adds a task to the schedule
     */
    public void addTask(Task task) {
        taskList.add(task);
    }

    /**
     * Clears all tasks from the schedule
     */
    public void clear() {
        taskList.clear();
    }

    /**
     * Returns the list of tasks
     */
    public ArrayList<Task> getTaskList() {
        return this.taskList;
    }
}
