package linenux.command;

import static linenux.helpers.Assert.assertChangeBy;
import static linenux.helpers.Assert.assertNoChange;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import linenux.command.result.CommandResult;
import linenux.model.Reminder;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.util.ArrayListUtil;

//@@author A0140702X
/**
 * JUnit test for remind command.
 */
public class RemindCommandTest {
    private Schedule schedule;
    private RemindCommand remindCommand;

    @Before
    public void setupRemindCommand() {
        Task todo = new Task("Todo");
        Task deadline = new Task("Deadline", LocalDateTime.of(2016, 1, 1, 1, 0));
        Task event = new Task("Event", LocalDateTime.of(2016, 1, 1, 1, 0), LocalDateTime.of(2016, 1, 1, 13, 0));

        this.schedule = new Schedule();
        this.schedule.addTask(todo);
        this.schedule.addTask(deadline);
        this.schedule.addTask(event);
        this.remindCommand = new RemindCommand(this.schedule);
    }

    private void setupTaskWithSameNameAndExecuteAmbiguousCommand() {
        this.schedule.addTask(new Task("Todo 2"));
        this.remindCommand.execute("remind Todo t/2016-01-01 05.00PM n/Hey");
    }

    private String expectedInvalidArgumentMessage() {
        return "Invalid arguments.\n\n" + this.remindCommand.getCommandFormat() + "\n\n" + Command.CALLOUTS;
    }

    /**
     * Get search result when done is executed. Assumes userInput is in correct
     * format and schedule is not null.
     */
    private ArrayList<Task> getSearchResult(String keywords) {
        String[] keywordsArr = keywords.split("\\s+");
        return this.schedule.search(keywordsArr);
    }

    /**
     * Test that respondTo detects various versions of the commands. It should
     * return true even if the format of the arguments are invalid.
     */
    @Test
    public void respondTo_inputThatStartsWithRemind_trueReturned() {
        assertTrue(this.remindCommand.respondTo("remind"));
        assertTrue(this.remindCommand.respondTo("remind task"));
        assertTrue(this.remindCommand.respondTo("remind task t/2016-01-01"));
        assertTrue(this.remindCommand.respondTo("remind task t/2016-01-01 n/notes"));
    }

    /**
     * Test respondTo is case-insensitive.
     */
    @Test
    public void repondTo_upperCase_trueReturned() {
        assertTrue(this.remindCommand.respondTo("ReMiNd task t/2016-01-01"));
    }

    /**
     * Test that respondTo will return false for commands not related to adding
     * reminders.
     */
    @Test
    public void respondTo_otherCommands_falseReturned() {
        assertFalse(this.remindCommand.respondTo("notremind"));
        assertFalse(this.remindCommand.respondTo("remindr"));
    }

    /**
     * Test that executing adding reminder to a To-Do should return correct
     * result.
     */
    @Test
    public void execute_remindTodo_reminderAdded() {
        assertChangeBy(() -> getSearchResult("Todo").get(0).getReminders().size(), 1,
                () -> this.remindCommand.execute("remind Todo t/2000-01-01 05.00PM n/Attend Workshop"));
        ArrayList<Reminder> reminders = getSearchResult("Todo").get(0).getReminders();
        Reminder addedReminder = reminders.get(reminders.size() - 1);
        assertEquals(LocalDateTime.of(2000, 1, 1, 17, 0), addedReminder.getTimeOfReminder());
        assertEquals("Attend Workshop", addedReminder.getNote());
    }

    /**
     * Test that executing adding reminder in different order to a To-Do should
     * return correct result.
     */
    @Test
    public void execute_shuffleFlags_reminderAdded() {
        assertChangeBy(() -> getSearchResult("Todo").get(0).getReminders().size(), 1,
                () -> this.remindCommand.execute("remind Todo n/Attend Workshop t/2000-01-01 05.00PM"));
        ArrayList<Reminder> reminders = getSearchResult("Todo").get(0).getReminders();
        Reminder addedReminder = reminders.get(reminders.size() - 1);
        assertEquals(LocalDateTime.of(2000, 1, 1, 17, 0), addedReminder.getTimeOfReminder());
        assertEquals("Attend Workshop", addedReminder.getNote());
    }

    /**
     * Test that executing adding reminder to a Deadline should return correct
     * result.
     */
    @Test
    public void execute_deadline_reminderAdded() {
        assertChangeBy(() -> getSearchResult("Deadline").get(0).getReminders().size(), 1,
                () -> this.remindCommand.execute("remind deadline t/2000-01-01 05.00PM n/Attend Workshop"));
        ArrayList<Reminder> reminders = getSearchResult("Deadline").get(0).getReminders();
        Reminder addedReminder = reminders.get(reminders.size() - 1);
        assertEquals(LocalDateTime.of(2000, 1, 1, 17, 0), addedReminder.getTimeOfReminder());
        assertEquals("Attend Workshop", addedReminder.getNote());
    }

    /**
     * Test that executing adding reminder in different order to a Deadline
     * should return correct result.
     */
    @Test
    public void execute_shuffleDeadlineFlags_reminderAdded() {
        assertChangeBy(() -> getSearchResult("Deadline").get(0).getReminders().size(), 1,
                () -> this.remindCommand.execute("remind deadline n/Attend Workshop t/2000-01-01 05.00PM"));
        ArrayList<Reminder> reminders = getSearchResult("Deadline").get(0).getReminders();
        Reminder addedReminder = reminders.get(reminders.size() - 1);
        assertEquals(LocalDateTime.of(2000, 1, 1, 17, 0), addedReminder.getTimeOfReminder());
        assertEquals("Attend Workshop", addedReminder.getNote());
    }

    /**
     * Test that executing adding reminder to a Event should return correct
     * result.
     */
    @Test
    public void execute_event_reminderAdded() {
        assertChangeBy(() -> getSearchResult("Event").get(0).getReminders().size(), 1,
                () -> this.remindCommand.execute("remind Event t/2000-01-01 05.00PM n/Attend Workshop"));
        ArrayList<Reminder> reminders = getSearchResult("Event").get(0).getReminders();
        Reminder addedReminder = reminders.get(reminders.size() - 1);
        assertEquals(LocalDateTime.of(2000, 1, 1, 17, 0), addedReminder.getTimeOfReminder());
        assertEquals("Attend Workshop", addedReminder.getNote());
    }

    /**
     * Test that executing adding reminder in different order to a Event should
     * return correct result.
     */
    @Test
    public void execute_eventFlagsShuffled_reminderAdded() {
        assertChangeBy(() -> getSearchResult("Event").get(0).getReminders().size(), 1,
                () -> this.remindCommand.execute("remind Event n/Attend Workshop t/2000-01-01 05.00PM"));
        ArrayList<Reminder> reminders = getSearchResult("Event").get(0).getReminders();
        Reminder addedReminder = reminders.get(reminders.size() - 1);
        assertEquals(LocalDateTime.of(2000, 1, 1, 17, 0), addedReminder.getTimeOfReminder());
        assertEquals("Attend Workshop", addedReminder.getNote());
    }

    /**
     * Test the result when no task name is given to search.
     */
    @Test
    public void execute_noTaskName_commandResultReturned() {
        ArrayList<Task> tasks = this.schedule.getTaskList();
        CommandResult result = assertNoChange(() -> {
            int size = 0;
            for (int i = 0; i < tasks.size(); i++) {
                size += tasks.get(i).getReminders().size();
            }
            return size;
        }, () -> this.remindCommand.execute("remind t/2011-01-01 05.00PM n/hey"));
        assertEquals(expectedInvalidArgumentMessage(), result.getFeedback());
    }

    /**
     * Test the result when no reminder note is given to search.
     */
    @Test
    public void execute_noReminderNote_commandResultReturned() {
        ArrayList<Task> tasks = this.schedule.getTaskList();
        CommandResult result = assertNoChange(() -> {
            int size = 0;
            for (int i = 0; i < tasks.size(); i++) {
                size += tasks.get(i).getReminders().size();
            }
            return size;
        }, () -> this.remindCommand.execute("remind Todo t/2011-01-01 05.00PM"));
        assertEquals("Cannot create reminder without note.", result.getFeedback());
    }

    /**
     * Test the result when no time is given for the reminder.
     */
    @Test
    public void execute_noTime_commandResultReturned() {
        CommandResult result = assertNoChange(() -> getSearchResult("Todo").get(0).getReminders().size(),
                () -> this.remindCommand.execute("remind todo n/hey"));
        assertEquals("Cannot create reminder without date.", result.getFeedback());
    }

    /**
     * Test the result when time is invalid.
     */
    @Test
    public void execute_invalidTime_commandResultReturned() {
        CommandResult result = assertNoChange(() -> getSearchResult("Todo").get(0).getReminders().size(),
                () -> this.remindCommand.execute("remind todo t/tomorrow"));
        assertEquals("Cannot parse \"tomorrow\".", result.getFeedback());
    }

    /**
     * Test the result when no match is found.
     */
    @Test
    public void execute_taskNotFound_commandResultReturned() {
        ArrayList<Task> tasks = this.schedule.getTaskList();
        CommandResult result = assertNoChange(() -> {
            int size = 0;
            for (int i = 0; i < tasks.size(); i++) {
                size += tasks.get(i).getReminders().size();
            }
            return size;
        }, () -> this.remindCommand.execute("remind not task t/2016-01-01 05.00PM"));
        assertEquals("Cannot find task names with \"not task\".", result.getFeedback());
    }

    /**
     * Test the result when multiple matches are found.
     */
    @Test
    public void execute_multipleMatches_commandResultReturned() {
        this.schedule.addTask(new Task("todo 2"));
        CommandResult result = assertNoChange(() -> this.totalNumberOfReminders(),
                () -> this.remindCommand.execute("remind todo t/2016-01-01 05.00PM"));
        assertEquals("Which one? (1-2, \"cancel\" to cancel the current operation)\n1. Todo\n2. todo 2", result.getFeedback());
    }

    /**
     * Test that command is waiting user response.
     */
    @Test
    public void isAwaitingUserResponse_multipleMatches_trueReturned() {
        assertFalse(this.remindCommand.isAwaitingUserResponse());
        this.setupTaskWithSameNameAndExecuteAmbiguousCommand();
        assertTrue(this.remindCommand.isAwaitingUserResponse());
    }

    /**
     * Test the result when user cancel response.
     */
    @Test
    public void processUserResponse_cancel_isNotAwaitingUserResponse() {
        this.setupTaskWithSameNameAndExecuteAmbiguousCommand();
        CommandResult result = assertNoChange(() -> this.totalNumberOfReminders(),
                () -> this.remindCommand.processUserResponse("cancel"));
        assertEquals("OK! Not adding new reminder.", result.getFeedback());
        assertFalse(this.remindCommand.isAwaitingUserResponse());
    }

    /**
     * Test the result when user inputs valid index.
     */
    @Test
    public void processUserResponse_validIndex_reminderAdded() {
        this.setupTaskWithSameNameAndExecuteAmbiguousCommand();
        CommandResult result = assertChangeBy(() -> this.schedule.getTaskList().get(0).getReminders().size(), 1,
                () -> this.remindCommand.processUserResponse("1"));
        assertEquals("Added reminder on 2016-01-01 5.00PM for Todo", result.getFeedback());
        assertFalse(this.remindCommand.isAwaitingUserResponse());
    }

    /**
     * Test the result when user inputs invalid index.
     */
    @Test
    public void processUserResponse_invalidIndex_commandResultReturned() {
        this.setupTaskWithSameNameAndExecuteAmbiguousCommand();
        CommandResult result = assertNoChange(() -> getSearchResult("Todo").get(0).getReminders().size(),
                () -> this.remindCommand.processUserResponse("0"));
        assertEquals("That's not a valid index. Enter a number between 1 and 2, or \"cancel\" to cancel the current operation:\n" + "1. Todo\n2. Todo 2",
                result.getFeedback());
        assertTrue(this.remindCommand.isAwaitingUserResponse());
    }

    /**
     * Test the result when user inputs invalid response.
     */
    @Test
    public void processUserResponse_invalidResponse_commandResultReturned() {
        this.setupTaskWithSameNameAndExecuteAmbiguousCommand();
        CommandResult result = assertNoChange(() -> getSearchResult("Todo").get(0).getReminders().size(),
                () -> this.remindCommand.processUserResponse("One"));
        assertEquals("I don't understand \"One\".\nEnter a number to indicate which task to add reminder to:\n"
                + "1. Todo\n2. Todo 2", result.getFeedback());
        assertTrue(this.remindCommand.isAwaitingUserResponse());
    }

    private int totalNumberOfReminders() {
        return new ArrayListUtil.ChainableArrayListUtil<>(this.schedule.getTaskList())
                .map(Task::getReminders)
                .map(ArrayList::size)
                .foldr(Integer::sum, 0);
    }
}
