package linenux.model;

import java.util.ArrayList;
import java.util.Collections;

import linenux.util.ArrayListUtil;

/**
 * Represents a snapshot in time of a schedule. The State class is immutable.
 */
 public class State {
     private final ArrayList<Task> taskList;

     public State() {
         this.taskList = new ArrayList<Task>();
     }

     public State(ArrayList<Task> taskList) {
         this.taskList = taskList;
     }

     /**
      * Adds a task to the schedule
      * @param task The task to add.
      * @return The new State of the schedule.
      */
     public State addTask(Task task) {
         ArrayList<Task> newTaskList = copyTaskList(taskList);
         newTaskList.add(task);
         return new State(newTaskList);
     }

     /**
      * Delete the specified task.
      * @param task The task to delete.
      * @return The new State of the schedule.
      */
     public State deleteTask(Task task) {
         int taskIndex = taskList.indexOf(task);
         ArrayList<Task> newTaskList = copyTaskList(taskList);
         newTaskList.remove(taskIndex);
         return new State(newTaskList);
     }

     /**
      * Marks the specified task as done.
      * @param task The task to mark as done.
      * @return The new State of the schedule.
      */
     public State doneTask(Task task) {
         int taskIndex = taskList.indexOf(task);
         ArrayList<Task> newTaskList = copyTaskList(taskList);
         newTaskList.get(taskIndex).markAsDone();;
         return new State(newTaskList);
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
                                 .filter(task -> { ArrayList<String> taskKeywords =
                                                     new ArrayListUtil.ChainableArrayListUtil<String>(task.getTaskName().split("\\s+"))
                                                                      .map(String::toLowerCase)
                                                                      .value();
                                                   return !Collections.disjoint(keywordsList, taskKeywords);
                                                   })
                                 .value();
     }

     /**
      * Creates a deep copy of the task list.
      * @param taskList
      * @return
      */
     private ArrayList<Task> copyTaskList(ArrayList<Task> taskList) {
         return new ArrayListUtil.ChainableArrayListUtil<Task>(taskList)
                                 .map(task -> task.copyTask())
                                 .value();
     }
}
