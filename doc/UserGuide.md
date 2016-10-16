# User Guide

* [About](#about)
* [Getting Started](#getting-started)
	* [Downloading](#downloading)
	* [Launching](#launching)
	* [Walkthrough](#walkthrough)
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

## About

Everyone of us will face, at some point in our lives, the feeling of being overwhelmed by the amount of things to do: meeting project deadlines, attending a friend's wedding ceremony, paying the bills and the list goes on. This hectic pace of life is the new normal, but it doesn't mean we have to suffer in silence. We believe that everyone should have control over their own lives and Linenux is the key to achieving just that.

Linenux is essentially a task organiser. It helps you to prioritise your tasks by urgency and importance, so that you can better make use of your time by focussing on things that matter. What sets Linenux apart from other task managers is its simplicity and efficiency. It requires only a single line of command. No mouse clicks needed and no gimmicks. Linenux works only as fast as you can type, making it a perfect fit for those with an affinity for typing.

Without further ado, let's get started!

## Getting Started

#### Downloading

1. Ensure that you have Java 8 installed on your computer.
2. Download the latest linenux.jar from the [releases](https://github.com/CS2103AUG2016-W11-C1/main/releases) tab.
<img src="images/releases.png">
> Figure 1: Downloading linenux.jar
3. Copy the file to the folder that you want to use as the home folder for Linenux.

#### Launching

Double-click on the file to launch Linenux. Wait for a few seconds for the main window to appear.  

<img src="images/mainWindow.png">
> Figure 2: Linenux's main window

#### Walkthrough

*This section is a scenario driven, step-by-step introduction on how to use Linenux. For a more in-depth coverage of the commands, refer to our [Commands](#commands) section in the user guide.*

**Step 1: Adding a task.**

The date is October 17, 2016. You have to buy groceries from the local supermarket but you are only free to do so on a weekend. You decide to remind youself with the aid of Linenux. Type the following line in the command box:

`> add buy groceries et/2016-10-22`

**Step 2: Setting a reminder.**

You worry that you might not have enough cash to buy groceries and decide to draw money from a nearby ATM machine on the way home from work on Friday. Type the following line in the command box:

`> remind buy groceries n/draw money et/2016-10-21 6:00pm`

**Step 3: Listing tasks for the day.**

Time flies and it is already Friday. You wonder what needs to be done for the day. Type the following line in the command box:

`> list et/2016-10-21` or more simply, `> today`

**Step 4: Marking a task as done.**

You repeat step 3 on Saturday and realised that you needed to buy the groceries. You hastily went out to buy them, thus completing all of your work for the day. Type the following line in the command box:

`> done buy groceries`

This command will prevent the task from showing up in subsequent `list` or `today` commands.

## Commands Summary:
| Description | Command | Format |
|---|---|---|
| Add a task to Linenux | [`add`](#adding-a-task-add) | `add` TASK_NAME [st/START_TIME] [et/END_TIME] [#/TAGS]... |
| Add a reminder to a task | [`remind`](#adding-reminders-to-a-task-remind) | `remind` KEYWORDS... t/TIME [n/NOTE] |
| Edit a task or reminder | [`edit`](#edit-a-taskreminder-edit) | `edit` KEYWORDS... [n/NEW_NAME] [st/START_TIME] [et/END_TIME] |
| Delete a task or reminder | [`delete`](#deleting-tasks-delete) | `delete` KEYWORDS… |
| Mark a task as done | [`done`](#marking-tasks-as-done-done) | `done` KEYWORDS… |
| Undo | [`undo`](#undo-an-action-undo) | `undo` |
| Finding a free timeslot | [`freetime`](#finding-a-free-timeslot---freetime) | `freetime` [st/START_TIME] et/END_TIME |
| Listing all tasks and reminders | [`list`](#listing-all-tasks-list) | `list` [KEYWORDS]... [st/START_TIME] [et/END_TIME] [#/TAG] |
| Viewing details of tasks and reminders | [`view`](#viewing-details-of-task-or-reminder-view) | `view` KEYWORDS… |
| Help for commands | [`help`](#viewing-help--help) | `help` [KEYWORD] |
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
remind KEYWORDS... t/TIME [n/NOTE]
```

##### Examples:
```
remind assignment t/2016-07-18 05:00PM
```
```
remind project deadline t/2016-07-18 05:00PM n/Complete v0.0 document
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
Sometimes, listing down all the tasks and reminders is simply not enough, as we might want to check all the reminders that we have created for a particular task.

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