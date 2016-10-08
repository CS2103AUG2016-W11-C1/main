package linenux.model;

import java.util.ArrayList;
import java.util.Collections;

import linenux.util.ArrayListUtil;

/**
 * Represents a snapshot in time of a schedule.
 */
 public class State {
     private final ArrayList<Task> taskList;

     public State(State previous) {
         this.taskList = new ArrayList<Task>(previous.getTaskList());
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
      * Clears all tasks from the schedule
      */
     public void clear() {
         taskList.clear();
     }

     /**
      * Returns the list of tasks.
      */
     public ArrayList<Task> getTaskList() {
         return taskList;
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
