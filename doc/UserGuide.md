# User Guide

* [Introduction](#introduction)
* [Quick Start](#quick-start)
* [Commands Summary](#commands-summary)
* [List of Commands](#list-of-commands)
    * [Notes](#some-notes-before-you-start)
    * [Adding a task: `add`](#adding-a-task-add)
    * [Adding reminders to a task: `remind`](#adding-reminders-to-a-task-remind)
    * [Edit a task/reminder: `edit`](#edit-a-taskreminder-edit)
    * [Deleting tasks: `delete`](#deleting-tasks-delete)
    * [Marking tasks as done: `done`](#marking-tasks-as-done-done)
    * [Undo an action: `undo`](#undo-an-action-undo)
    * [Finding a free timeslot - `freetime`](#finding-a-free-timeslot---freetime)
    * [Listing all tasks: `list`](#listing-all-tasks-list)
    * [Viewing details of task or reminder: `view`](#viewing-details-of-task-or-reminder-view)
    * [Viewing help: `help`](#viewing-help-help)
    * [Exiting the Program: `exit`](#exiting-the-program-exit)
    * [Viewing Help: `help`](#viewing-help-help)
    * [Exiting Linenux: `exit`](#exiting-linenux-exit)



## Introduction
We understand that in everyone’s daily lives, we will be thrown many tasks, for example, to attend meetings, preparing documents by a certain deadline etc. Our application, Linenux, aims to help you effectively organize and manage all your different tasks with a simple type on your keyboard.



## Quick Start

1. Find the project in the `Project Explorer` or `Package Explorer` (usually located at the left side)
2. Right click on the project
3. Click `Run As` > `Java Application` and choose the `Main` class.
4. The GUI should appear in a few seconds.



## Commands Summary:
| Description | Command | Format |
|---|---|---|
| Add a task to Linenux | [`add`](#adding-a-task-add) | `add` TASK_NAME [st/START_TIME] [et/END_TIME] [#/TAGS]... |
| Add a reminder to a task | [`remind`](#adding-reminders-to-a-task-remind) | `remind` KEYWORDS... d/DATE [n/NOTE] |
| Edit a task or reminder | [`edit`](#edit-a-taskreminder-edit) | `edit` KEYWORDS... [n/NEW_NAME] [st/START_TIME] [et/END_TIME] |
| Delete a task or reminder | [`delete`](#deleting-tasks-delete) | `delete` KEYWORDS… |
| Mark a task as done | [`done`](#marking-tasks-as-done-done) | `done` KEYWORDS… |
| Undo | [`undo`](#undo-an-action-undo) | `undo` |
| Finding a free timeslot | [`freetime`](#finding-a-free-timeslot---freetime) | `freetime` [st/START_TIME] et/END_TIME |
| Listing all tasks and reminders | [`list`](#listing-all-tasks-list) | `list` [KEYWORDS]... [st/START_TIME] [et/END_TIME] [#/TAG] |
| Viewing details of tasks and reminders | [`view`](#viewing-details-of-task-or-reminder-view) | `view` KEYWORDS… |
| Help for commands | [`help`](#viewing-help-help) | `help` [KEYWORD] |
| Exiting Linenux | [`exit`](#exiting-linenux-exit) | `exit` |



## List of Commands
#### Some notes before you start:
##### Command format:
  1. Any capitalized words, e.g TASK_NAME, is a field required by the command.
  2. All time fields are in the format of YYYY-MM-DD.
  3. Any field surrounded by square brackets, '[ ]', are optional.
  4. Each field represents one word, and fields are seperated by spaces.
  5. The only exception to (4) is when you see '...' after a field, indicating that you can key in multiple words for that particular field.

\* Some of the commands in Linenux will ask for KEYWORDS of the task/reminder that you are doing the command for. In those cases, Linenux will search your schedule for the tasks/reminders that matches your KEYWORDS. There are 2 things that can happen when the Linenux finds something from your schedule:
  1. Only one task/reminder was found:

      Linenux will do the command on the found task.
  2. More than one task/reminder was found:

      Linenux will prompt you with a list of all the tasks and reminders found, which you will then need to tell Linenux which task/reminder to execute the command on.


#### Adding a task: `add`
Linenux’s goal is to help you organize and manage your tasks, so the most important feature will be to add said tasks into Linenux.
To help organize your tasks, our add command allows you tag your task to help you easily search for them in the future. Furthermore, our team has looked into all possible tasks and discovered that tasks can be generally categorized into the following 3 groups:
  1. Deadlines (Tasks with end times only)
  2. Events (Tasks with start and end times)
  3. To-Dos (Tasks without start and end times)

As such, all tasks will be categorize under these 3 groups by default(no input from you required!) and will tagged under #deadlines, #events, #todos respectively.

Thus, to add any type of task, all you need to do is to type `add`, followed by your task name. Depending on your needs, you may also include the optional fields specified in the format of the add command.

##### Format:
```
add TASK_NAME [st/START_TIME] [et/END_TIME] [#/TAG]...
```

##### Examples:
```
//Adding a deadline
add Submit v0.0 deliverables et/2016-10-05 #quite_important
```
```
//Adding an Event
add Hackathon st/2016-10-01 et/2016-10-02 :D #really_important
```
```
//Adding a To-Do
add CS2103T tutorial :D #very_important
```


#### Adding reminders to a task: `remind`
Often enough, just having a deadline or knowing when an event will happen is not enough. For instance, knowing that you have a submission due on Friday, you might want to remind yourself about the submission on Wednesday. Fear not, for Linenux is here to help.

To add a reminder, type `remind`, along with some keywords for Linenux to search for your task and Linenux will add the reminder onto the task. Also, you can add notes to your reminders, which will show up in addition to the task name when it is the day of the reminder.

##### Format:
```
remind KEYWORDS... d/DATE [n/NOTE]
```

##### Examples:
```
remind assignment d/2016-07-18
```
```
remind project deadline d/2016-07-18 n/Complete v0.0 document
```

##### Callouts:
> Upon starting up the application, all your reminders for the day will be shown.


#### Edit a task/reminder: `edit`
Things change all the time, and that applies to tasks too. To help facilitate these changes, you can edit your tasks and reminders. All you have to do, is to type `edit` and some keywords to search for your task and provide the new details and Linenux will update your tasks and reminders.

##### Format:
```
edit KEYWORDS... [n/NEW_NAME] [st/START_TIME] [et/END_TIME]
```

##### Examples:
```
edit assignment et/2016-08-13
```
```
edit this n/CS2103 st/2016-09-20 et/2016-05-09
```

##### Callouts:
> Linenux will overwrite the fields of the old task with all the details of the new task, so you will be required to provide all the fields for your new task. We, the developers, aim to find a user-friendly way for you to edit each field separately, so stay tuned for more updates!


#### Deleting tasks: `delete`
At times, tasks or reminders might become redundant. For example, a scheduled meeting might be cancelled due to unforeseen circumstances. So, you would need a way to delete these redundant tasks and reminders from your schedule as it is not longer required. Thus, to update your schedule, type `delete`, followed by some keywords of your task/reminder and Linenux will remove the task/reminder from your schedule.

##### Format:
```
delete KEYWORDS...
```

##### Callouts:
> Deleting task is a dangerous thing, especially when you can accidentally delete an important task unintentionally. Thus, Linenux will show you the details of the task that you have just deleted so please check that the correct task is deleted. If the wrong task is deleted, worry not, just type [`undo`](#undo-an-action-undo)!

> Also, when you delete a task, you don't have to manually delete all the remainders attached to it, as Linenux will automatically do so for you!


#### Marking tasks as done: `done`
Yes! You’ve just completed a task. Since it’s completed, you wouldn’t want to see it popping up as a task that is incomplete, would you? Thus, type done, along with some keywords to search to the task to tell Linenux that the task has been completed.

##### Format:
```
done KEYWORDS...
```
```
done CS2103 tutorial
```


#### Undo an action: `undo`
Nobody’s perfect, and we would make mistakes. Thankfully, Linenux is here to help with one of our favourite feature, `undo`. Type `undo` and Linenux will restore your schedule to the last time you made changes to your tasks.

##### Format:
```
undo
```

##### Callouts:
> Undo will only affect commands that have made changes to your schedule, such as add and delete. Commands that don’t make changes like list and view, will not be affected.


#### Finding a free timeslot - `freetime`
With all your events in the schedule, Linenux can help you find all your available time slots. Just type `freetime` and give the time period for Linenux to search and Linenux will list down all your free time slots in the period!

##### Format:
```
freetime [st/START_TIME] et/END_TIME
```

##### Examples:
```
freetime et/2016-07-24
```
```
freetime st/2016-07-18 et/2016-07-24
```

##### Callouts:
> If you do specify a start time, Linenux would assume that the start time will be today, the day you call this command.

#### Listing all tasks: `list`
As you start getting overwhelmed by the overwhelming number of tasks that you have to do, it is understandable that you start to forget some of them. Thankfully, Linenux will never forget. Just type `list` and Linenux will show you the list of tasks.

Just want to view a particular group of tasks and reminders? Linenux will also allow you to filter all the tasks to your own needs! Simply provide Linenux with more details on some of the fields(or all, if you'd like) and let Linenux do all the filtering for you!

##### Format:
```
list [KEYWORDS]... [st/START_TIME] [et/END_TIME] [#/TAG]
```

##### Examples:

```
list
```
```
list st/2016-06-01 et/2016-07-01
```
```
list st/2016-06-01 et/2016-07-01 #Hobbies
```
```
list assignment st/2016-06-01 et/2016-07-01
```
##### Callouts
>
* To help organize and manage your tasks, Linenux will group your tasks under the 3 default categories (Deadlines, Events, To-Dos) and before listing them.
>
* To avoid over cluttering of data, `list` will only show you the name and start/end time(if any) of each task and reminder.
>
* Your deadlines, events and reminders will be listed in a sorted manner as follows:
     * Deadlines - by earliest end time
     * Events, reminders -  by earliest start time
>
* When a time field is provided, Linenux will still show all to-dos task, since they are not bounded by anytime. For deadline, events and reminders, if you provided Linenux with a:
    * Start time:
      All deadlines, events and reminders that ends after your given start time will be shown.
    * End time:
      All events, deadlines and reminders that start before given end time will be shown.


#### Viewing details of task or reminder: `view`
Sometimes, listing down all the tasks is simply not enough. There’s too many details to show, which, Linenux has left out in list. To help you with that, Linenux supports view, which allows you to view all the details of a task in your schedule, inclusive of the reminders that are attached to your task.

Just type `view`, followed by some keywords to search for your task and Linenux will show all the details of the task that you searched for.

##### Format:
```
view KEYWORDS...
```

##### Example:
```
view assignment
```


#### Viewing help : `help`
We understand that there may be too many commands to remember and it might be a hassle to refer to the user guide to refer to the list of commands. Thus, to get a brief overview of the list of commands available immediately, you can type the command, `help`.

Furthermore, are you just searching for how a particular command work and finding it difficult to look through the whole list? Well, include the command as a keyword for the help function and Linenux will show you only what you need.

##### Format:
```
help [KEYWORD]
```

##### Callouts:
> When Linenux is unable to understand your command, it will also show you the `help` section.


#### Exiting Linenux: `exit`
Although we are sad to see you goTo exit the program, apart from clicking the ‘x’ button, we too have catered to your love of typing. Simply type `exit` and you’re done!

Format:
```
exit
```