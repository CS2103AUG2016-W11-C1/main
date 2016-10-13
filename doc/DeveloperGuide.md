# Developer Guide

* [Introduction](#introduction)
* [Setting Up](#setting-up)
    * [Prerequisites](#prerequisites)
    * [Importing project into Eclipse](#importing-project-into-eclipse)
    * [Troubleshooting](#troubleshooting)
* [Design](#design)
    * [Architecture](#architecture)
    * [Model Component](#model-component)
    * [View Component](#view-component)
    * [Controller Component](#controller-component)
* [Testing](#testing)
    * [In Eclipse](#in-eclipse)
    * [Using Gradle](#using-gradle)
* [Dev Ops](#dev-ops)
    * [Build Automation](#build-automation)
    * [Continuous Integration](#continuous-integration)
    * [Making a Release](#making-a-release)
    * [Managing Dependencies](#managing-dependencies)
* [Appendices](#appendices)
    * [Appendix A: User Stories](#appendix-a--user-stories)
    * [Appendix B: Use Cases](#appendix-b--use-cases)
    * [Appendix C: Non Functional Requirements](#appendix-c--non-functional-requirements)
    * [Appendix D: Glossary](#appendix-d--glossary)
    * [Appendix E: Product Survey](#appendix-e--product-survey)

## Introduction
Linenux is a command-line, task manager application designed for consumers who are quick at typing. Being an open-source project, we understand that there are developers (yes, you) who wants to contribute to the project but do not know where to begin. Thus, we have written this guide to inform newcomers to the project on the key design considerations and the overall software architecture. We hope that by the end of this developer guide, you will in a better position to start working on improving Linenux.

## Setting up

#### Prerequisites

1. **JDK 8** or later.
2. **Eclipse** IDE with **e(fx)clipse** plugin. Follow the instructions given on their [website](https://www.eclipse.org/efxclipse/install.html#for-the-ambitious).
3. **Scene Builder 8.0**. Available for download [here](http://gluonhq.com/labs/scene-builder/).
4. **Gradle**. Follow the instuctions on their [website](https://docs.gradle.org/current/userguide/installation.html?_ga=1.32481590.94426092.1475838180).

#### Importing project into Eclipse

1. Fork this repository and clone the fork to your computer.
2. Open the Eclipse application.
3. Click `File` > `Import` > `General` > `Existing Projects into Workspace` > `Next`.
4. Click `Browse`, then locate the project's directory.
5. Click `Finish`.
6. Run `gradle eclipse` in your terminal to set up the folders in Linenx.
7. Run `gradle run` in your terminal to ensure that everything is working properly.

#### Troubleshooting

1. **Eclipse reports that some of the required libraries are missing.**
    * Reason: Required libraries were not downloaded during project import.
    * Solution: Run `gradle test` in your terminal once to refresh libraries.
2. **Eclipse reports compile errors after new commits are pulled from Git.**
    * Reason: Eclipse fails to detect the changes made to your project during `git pull`.
    * Solution: Refresh your project in Eclipse by clicking on it in the package explorer window and pressing `F5`.

## Design

#### Architecture

<img src="images/architectureDiagram.png"/>
> Figure 1: Architecture Diagram

Linenux follows the Model-View-Controller (MVC) pattern which is made up of 3 main components.

1. **Model** is where Linenux's data objects are stored. It is independent from the view and controller.
2. **View** is the window that our user sees and interacts with. It updates whenever there are changes to the model.
3. **Controller** is the decision maker and the glue between model and view.

#### Model Component

<img src="images/modelDiagram.png">
> Figure 2: Model Diagram

The **Schedule** class stores a collection of states. A **State** is an immutable class that is created whenever a task or reminder is added or deleted from the **Schedule**. This design allows users to `undo` their previous command.

The **Task** class is made up of the name of the task, a start time, an end time and a list of reminders. There are 3 types of tasks:
1. **Deadlines** - tasks that have an end time but no start time.
2. **Events** - tasks that have both start and end times.
3. **To-dos** - tasks that have neither start nor end times.

The **Reminder** class allows our users to set one or more reminders for their tasks.

The **Storage** class allows the in-memory data to persist after the application is closed. The state of the **Schedule** is stored as an XML file called **XMLScheduleStorage**.

#### View Component
<img src="images/viewDiagram.png">
> Figure 3: View Diagram

The **View Component** follows the JavaFx UI framework. Each of the classes (**MainWindow**, **CommandBox** etc) has their own respective `.fxml` file stored in `src/main/resources/view`.

#### Controller Component
<img src="images/controllerDiagram.png">
> Figure 4: Controller Diagram

The **ConsoleController** takes the user input and sends it to the "brain" of Linenux, the "ControlUnit" class. The "ControlUnit" class is in charge of retrieving the appropriate schedule from storage and passing it over to the **CommandManager** class. The **CommandManager** class then delegates the right command based on the user input.

``` java
public CommandResult delegateCommand(String userInput) {
    for (Command command : commandList) {
        if (command.awaitingUserResponse()) {
            return command.userResponse(userInput);
        }
    }

    for (Command command : commandList) {
        if (command.respondTo(userInput)) {
            return command.execute(userInput);
        }
    }

    return this.catchAllCommand.execute(userInput);
}
```
The above code shows how the **CommandManager** class delegates the right command based on the user input. Every command class must implement the **Command** interface. At any point in time, only 1 command is awaiting user response. If there are none, then each command will check if the user input corresponds to the command format.

``` java
public boolean canParse(String userInput) {
    for (TimeParser parser: parserList) {
        if (parser.respondTo(userInput)) {
            return true;
        }
    }

    return false;
}
```
Similarly, the same pattern is used if the command has to parse the time. These commands will have their own **TimeParserManager** and they can pick and choose which **TimeParser** format they want to support.

## Testing

Tests can be found in the `./src/test/java` folder.

#### In Eclipse

* To run all tests, right click on the `src/test/java` folder in the package explorer and choose `Run as` > `JUnit Test`.
* To run a subset of tests, you can right click on a test package, test class or a test in the package explorer and choose `Run as` > `JUnit Test`.

#### Using Gradle

* To run all tests, run `gradle test` command in the terminal.

## Dev Ops

#### Build Automation

Gradle is a build automation tool. It can automate build-related tasks such as:
* Running tests
* Managing library dependecies
* Analysing code for style compliance

The gradle configuration for this project is defined in the build script `build.gradle`.

#### Continuous Integration

Travis CI is a Continuous Integration platform for GitHub projects. It runs the projects' tests automaticaally whenever new code is pushed to the repo. This ensures that existing functionality and features  have not been broken by the changes.

The current Travis CI set up performs the following things whenever someone push code to the repo:
* Runs the `./gradlew test` command.

#### Making a Release

Linenux automatically creates a new release by using Travis.

#### Managing Dependecies

A project often depends on third-party libraries. Linenux manages and automates these dependencies using Gradle.

## Appendices

#### Appendix A : User Stories

Priorities: High (must have) - `* * *`, Medium (nice to have)  - `* *`,  Low (unlikely to have) - `*`


Priority | As a ...  | I want to ...                             | So that I can ...
-------- | :-------- | :---------------------------------------  | :---------------
`* * *`  | user      | see usage instructions                    | have a reference on how to use the App in the event that I do not know the commands or have forgotten them.
`* * *`  | user      | add a new task                            |
`* * *`  | user      | edit a task                               | update the deadlines or other details regarding the task.
`* * *`  | user      | delete a task                             | remove tasks that I no longer need.
`* * *`  | user      | search a task                             | check the details of the task.
`* * *`  | user      | list tasks by day or deadlines            | plan ahead.
`* * *`  | user      | undo previous commands                    | correct any erroneous actions.
`* *`    | user      | set reminders for tasks                   | make preperations before their stipulated deadlines.
`* *`    | user      | have multiple language support            | choose my preferred working language.
`* *`    | user      | find free time slots                      | make appointments with others.
`* *`    | user      | have a day/week/month view                | more easily digest the information.
`* *`    | user      | print the schedule for the day/week/month | have a hard copy of my schedule.
`* *`    | user      | sync with Google Calendar                 | have the option to view on any devices with access to the Internet.
`*`      | user      | see syntax highlighting                   | more easily discern special keywords and commands.
`*`      | user      | see notifications                         | be constantly reminded without having to open the App.
`*`      | user      | have a mini-window mode                   | the application does not take up the whole screen.


#### Appendix B : Use Cases

>Use Case: Add task
MSS
1. User requests to add task.
2. TaskManager categorizes task under one of the 3 default tasks.
3. TaskManager adds task into schedule.
4. TaskManager shows message indicating successful add, including details of added task.
5. Use Case ends.

Extensions
1a. User provides start time without end time.
    1a1. TaskManager shows error message to indicate that task is not a valid task.
    1a2. Use Case ends.

1b. User requests to add an event with overlapping timeslot with an existing event.
    1b1. TaskManager shows list of overlapping events.
    b2. TaskManager will show filtered tasks instead.
    1b3. Use Case ends.

>Use Case: List tasks
MSS
1. User requests to list all tasks.
2. TaskManager shows a list of task.
3. Use Case ends.

Extensions
1a. User provides search parameters.
    1a1. TaskManager will perform fuzzy search and filter tasks based on search parameters.
    1a2. TaskManager will show filtered tasks instead.
    1a3. Use Case ends.

>Use Case: Add reminder to tasks
MSS
1. User requests to add reminder to task by providing search parameters for task.
2. TaskManager adds reminder to task.
3. TaskManager shows message indicating successful add, including details of reminder and task that reminder was added to.
4. Use Case ends.

Extensions
1. More than one task found when with the given parameters.
    1a1. TaskManager shows the list of tasks found.
    1a2. User select task from given list.
    1a3. Use Case resumes at step 2.
1b. No task found from given parameters.
    1b1. TaskManager shows error indicating no task found.
    1b2. Use Case ends.
1c. Specified index is invalid
    1c1. TaskManager shows error message indicating invalid index.
    1c2. Use Case resumes at step 1a1.

>Use Case: Delete task
MSS
1. User requests to delete task by giving search parameters.
2. TaskManager deletes specific task from schedule.
3. TaskManager shows message indicating successful delete, including details of task deleted.
4. Use Case ends.

Extensions:
1a. No tasks found.
    1a1. TaskManager shows error message indicating no task found.
    1a2. Use Case ends
1b. More than one task found.
    1b1. TaskManager show list of tasks found.
    1b2. User specify index of task to delete.
    1b3. Use Case resumes at step 2.
1c. Specified index is invalid.
    1c1. TaskManager shows error message indicating invalid index.
    1c2. Use Case resumes at step 1b1.

>Use Case: Mark task as done.
MSS
User requests to mark task as done by giving search parameters.
TaskManager marks specific task as done
TaskManager shows message indicating task is marked as done, including details of task
	Use Case ends

Extensions:
1a. No tasks found
	1a1. TaskManager shows error message indicating no task found
	Use Case ends
1b. More than one task found
	1b1. TaskManager show list of tasks found
	1b2. User specify index of task to mark as done
	Use Case resumes at step 2
1b2a. Specified index is invalid
	1b2a1. TaskManager shows error message indicating invalid index
	Use Case resumes at step 1b1

>Use Case: Undo
MSS
User requests to undo to previous state
TaskManager undos to previous state
	Use case ends

Extensions:
1a. No previous state to undo to
	1a1. TaskManager shows error indicating unable to undo
	Use Case ends

>Use Case: Exit

MSS:
User requests to exit application.
TaskManager prompts user to confirm application exit.
TaskManager closes.
Use case ends

Extensions:
2a. User confirms exit while TaskManager is still processing information (e.g. reading/ saving a file).
	2a1. TaskManager blocks input until process is done.
	2a2. TaskManager closes.
	        Use case ends.
2b. User cancels exit operation.
      Use case ends.

>Use Case: Edit

MSS:
User requests to edit task by giving search parameters and changes to be made.
TaskManager processes changes.
TaskManager shows changes made.

Extensions:
1a. No tasks found.
	1a1. TaskManager shows error message indicating no task found.
	        Use case ends.
1b. More than 1 task found.
	1b1. TaskManager shows list of tasks found.
	1b2. User specifies index of task to edit.
                    Use case resumes at step 2.
1b2a. Specified index is invalid.
	1b2a1. TaskManager shows error message indicating invalid index.
	            Use case resumes at step 1b1.
1c. Specified changes are invalid.
	1c1. TaskManager shows error message indicating invalid changes.
	        Use case ends.

#### Appendix C : Non Functional Requirements

1. **Backup** - Should be easy for user to backup their data
2. **Documentation** - User guides, Developer guides and UML diagrams available
3. **Efficiency & Response time** - All commands run within 3 seconds
4. **Open source** - Adopt a developer friendly license that permits users to modify and improve the program.
5. **Quality** - Code is peer-reviewed before merging the pull requests.
6. **Reliability** - Code is in accordance to the official Java coding style.
7. **Testability** - Use of Travis Continuous Integration.
8. **Backup** - Should be easy for user to backup their data.

#### Appendix D : Glossary

Users are able to create 2 objects, Tasks and Reminders.

Tasks are split into 3 different sub-categories:
1. Deadline: Tasks created with end dates only.
2. Event: Tasks created with both start and end dates.
3. To-do: Tasks created with no start and end dates.

**Tasks cannot be created with start dates only.**

#### Appendix E : Product Survey
