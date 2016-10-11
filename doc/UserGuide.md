# User Guide

* [Introduction](#introduction)
* [Quick Start](#quick-start)
* [Commands Summary](#commands-summary)
* [List of Commands](#list-of-commands)
    *[Notes](#some-notes-before-you-start)
    *[Adding a task: `add`](#adding-a-task-add)

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
| Add a task to Linenux | `add` | add TASK_NAME [st/START_TIME] [et/END_TIME] [#/TAGS]... |
| Add a reminder to a task | `remind` | remind KEYWORDS... d/DATE [n/NOTE] |
| Edit a task or reminder | `edit` | edit KEYWORDS... [n/NEW_NAME] [st/START_TIME] [et/END_TIME] |
| Delete a task or reminder | `delete` | delete KEYWORDS… |
| Mark a task as done | `done` | done KEYWORDS… |
| Undo | `undo` | undo |
| Finding a free timeslot | `freetime` | freetime [st/START_TIME] et/END_TIME |
| Listing all tasks and reminders | `list` | list [KEYWORDS]... [st/START_TIME] [et/END_TIME] [#/CATEGORY] |
| Viewing details of tasks and reminders | `view` | view KEYWORDS… |
| Help for commands | `help` | help [KEYWORD] |
| Exiting Linenux | `exit` | exit |

## List of Commands

#### Some notes before you start:
Command format:
  1. Any capitalized words, e.g TASK_NAME, is a field required by the command.
  2. All time fields are in the format of YYYY-MM-DD.
  3. Any field surrounded by square brackets, '[ ]', are optional.
  4. Each field represents one word, and fields are seperated by spaces.
  5. The only exception to (3) is when you see '...' after a field, indicating that you can key in multiple words for that particular field.

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

Thus, to add any different type of task, all you need to do is to type add, followed by your task name. Depending on your needs, you may also include the optional fields specified in the format of the add command.

Format:
```
add TASK_NAME [st/START_TIME] [et/END_TIME] [#/TAG]...
```

Example:
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

#### Viewing help : `help`
We understand that there may be too many commands to remember and it might be a hassle to refer to the user guide to refer to the list of commands. Thus, this command allows you to get the list of commands available immediately, along with how each command works.

Format:
```
help [KEYWORD]
```

> Help is also shown if you enter an incorrect command e.g. `abcd`
> Only show help section for keyword.

#### Listing all tasks: `list`
Shows a list of all tasks.
Format:
```
list [KEYWORDS]... [st/START_TIME] [et/END_TIME] [#/CATEGORY]
```

When list is called without any other parameters, all the tasks will be listed.

Calling list with:

- KEYWORDS:
  - Lists all tasks with the given KEYWORDS in the task’s name
  - Lists all remainders with the given KEYWORDS in the reminder’s note
- CATEGORY:
  - Lists all tasks in the given CATEGORY
- START_TIME:
  - Lists all events, deadlines, reminders that ends after given START_TIME
  - Lists all to-dos
- END_TIME:
  - Lists all events, deadlines, reminders that start before given END_TIME
  - Lists all to-dos

Example:

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

#### Viewing details of task: `view`
Format:
```
view KEYWORDS...
```

Example:
```
view assignment
```

#### Setting reminders to a task: `remind`
Adds a reminder to a task
Format:
```
remind KEYWORDS... d/DATE [n/NOTE]
```

> KEYWORDS refer to keywords in the task name that the reminder will be attached to
> If there are more than one task found with the same KEYWORDS, the user will be shown the list of tasks found, and will require the user to indicate which task to add the reminder to.
> Upon starting up the application, the reminders for the day will be shown.
> Reminders will also be shown along with tasks during listing.

Example:
```
remind assignment d/2016-07-18
```
```
remind project deadline d/2016-09-20 n/Complete v0.0 document
```


#### Deleting tasks: `delete`
Deletes a task.
Format:
```
delete KEYWORDS...
```

> Also deletes all reminders attached to a task.
> After deletion, shows details of task deleted.
> KEYWORDS refer to keywords in the task name.
> If there are more than one task found with the same KEYWORDS, the user will be shown the list of tasks found, and will require the user to indicate which task to delete.

Example:
```
delete assignment
```
```
delete CS2103 project
```

#### Marking tasks as done: `done`
Marks a single task as done.

Format:
```
done KEYWORDS...
```

>​ ​KEYWORDS​ ​refer​ ​to​ ​keywords​ ​in​ ​the​ ​task​ ​name​.
>​ ​If​ ​there​ ​are​ ​more​ ​than​ ​one​ ​task​ ​found​ ​with​ ​the​ ​same​ ​KEYWORDS,​ ​the​ ​user​ ​will​ ​be​ ​shown​ ​the
list​ ​of​ ​tasks​ ​found,​ ​and​ ​will​ ​require​ ​the​ ​user​ ​to​ ​indicate​ ​which​ ​task​ ​to​ mark as done.

Example:
```
done assignment
```
```
done CS2103 tutorial
```

#### Undo a task: `undo`
Undo last command executed.

Format:
```
undo
```

#### Exiting the Program: `exit`
Exits the program.

Format:
```
exit
```

#### Edit a task/reminder: `edit`
Allows editing of the details of a task or reminder.

Format:
```
edit KEYWORDS... [n/NEW_NAME][st/START_TIME] [et/END_TIME]
```

>​​ KEYWORDS​​ ​​refer​​ ​​to​​ ​​keywords​​ ​​in​​ ​​the​​ ​​task​​ ​​name​.
>​​ ​​If​​ ​​there​​ ​​are​​ ​​more​​ ​​than​​ ​​one​​ ​​task​​ ​​found​​ ​​with​​ ​​the​​ ​​same​​ ​​KEYWORDS,​​ ​​the​​ ​​user​​ ​​will​​ ​​be​​ ​​shown​​ ​​the
list​​ ​​of​​ ​​tasks​​ ​​found,​​ ​​and​​ ​​will​​ ​​require​​ ​​the​​ ​​user​​ ​​to​​ ​​indicate​​ ​​which​​ ​​task​​ ​​to​​ ​mark​ ​as​ ​done.

Example:
```
edit assignment et/2016-08-13
```
```
edit this n/CS2103 st/2016-09-20 et/2016-05-09
```

## FREETIME - `freetime`
Format:
```
freetime [st/START_TIME] et/END_TIME
```

Example:

```
freetime et/2016-07-24
```
```
freetime st/2016-07-18 et/2016-07-24
```