package linenux.model;

import java.util.ArrayList;
import java.util.Collections;

import linenux.util.ArrayListUtil;
import linenux.util.RemindersListUtil;

/**
 * Contains all outstanding tasks.
 */
public class Schedule {
    private final ArrayList<Task> taskList;

    /**
     * Constructs an empty schedule
     */
    public Schedule() {
        this.taskList = new ArrayList<>();
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
     * Delete the specified task.
     * @param task The task to delete.
     */
    public void deleteTask(Task task) {
        this.taskList.remove(task);
    }

    /**
    * View the specific task.
    * @param task The task to view.
    */
    public String viewTask(Task task) {
        ArrayList<Reminder> reminders = task.getReminders();

        StringBuilder builder = new StringBuilder();
        builder.append(task.toString());
        builder.append('\n');
        builder.append("Reminders:" + '\n');

        if (reminders.size() == 0) {
            builder.append("There are no reminders found!");
        } else {
            builder.append(RemindersListUtil.display(reminders));
        }

        return builder.toString().trim();
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
        ArrayList<String> keywordsList = new ArrayListUtil.ChainableArrayListUtil<>(keywords)
                                                          .map(String::toLowerCase)
                                                          .value();

        return new ArrayListUtil.ChainableArrayListUtil<>(this.taskList)
                .filter(task -> {
                    ArrayList<String> taskKeywords = new ArrayListUtil.ChainableArrayListUtil<>(task.getTaskName().split("\\s+"))
                            .map(String::toLowerCase)
                            .value();

                    return !Collections.disjoint(keywordsList, taskKeywords);
                })
                .value();
    }
}
