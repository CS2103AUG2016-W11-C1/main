package linenux.model;

import linenux.util.ArrayListUtil;

import java.util.ArrayList;
import java.util.Collections;

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

    /**
     * Performs case-insensitive search using keywords.
     * @param keywords Search keywords
     * @return List of {@code Task} matching the keywords.
     */
    public ArrayList<Task> search(String[] keywords) {
        ArrayList<String> keywordsList = new ArrayListUtil.ChainableArrayListUtil<String>(keywords)
                                                          .map(String::toLowerCase)
                                                          .value();

        return new ArrayListUtil.ChainableArrayListUtil<Task>(this.taskList)
                .filter(task -> {
                    ArrayList<String> taskKeywords = new ArrayListUtil.ChainableArrayListUtil<String>(task.getTaskName().split("\\s+"))
                            .map(String::toLowerCase)
                            .value();

                    return !Collections.disjoint(keywordsList, taskKeywords);
                })
                .value();
    }
}
