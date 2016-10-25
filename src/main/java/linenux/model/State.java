package linenux.model;

import java.util.ArrayList;
import java.util.Collections;

import linenux.util.ArrayListUtil;

/**
 * Represents a snapshot in time of a schedule. The State class is immutable.
 */
 public class State {
     private final ArrayList<Task> tasks;

     public State() {
         this.tasks = new ArrayList<Task>();
     }

     public State(State other) {
         this.tasks = new ArrayList<>(other.tasks);
     }

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

     /**
      * Delete the specified task.
      * @param task The task to delete.
      * @return The new State of the schedule.
      */
     public State deleteTask(Task task) {
         int taskIndex = tasks.indexOf(task);
         State output = new State(this);
         output.tasks.remove(taskIndex);
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
         ArrayList<String> keywordsList = new ArrayListUtil.ChainableArrayListUtil<String>(keywords)
                                                           .map(String::toLowerCase)
                                                           .value();

         return new ArrayListUtil.ChainableArrayListUtil<Task>(this.tasks)
                                 .filter(task -> { ArrayList<String> taskKeywords =
                                                     new ArrayListUtil.ChainableArrayListUtil<String>(task.getTaskName().split("\\s+"))
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
        ArrayList<String> keywordsList = new ArrayListUtil.ChainableArrayListUtil<String>(keywords)
                .map(String::toLowerCase).value();

        ArrayList<Reminder> result = new ArrayList<Reminder>();

        for (Task t : this.tasks) {
            result.addAll(new ArrayListUtil.ChainableArrayListUtil<Reminder>(t.getReminders()).filter(reminder -> {
                ArrayList<String> reminderKeywords = new ArrayListUtil.ChainableArrayListUtil<String>(
                        reminder.getNote().split("\\s+")).map(String::toLowerCase).value();
                return !Collections.disjoint(keywordsList, reminderKeywords);
            }).value());
        }

        return result;
    }
}
