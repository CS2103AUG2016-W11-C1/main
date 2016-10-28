package linenux.command.util;

import linenux.model.Reminder;
import linenux.model.Task;
import linenux.util.ArrayListUtil;

import java.util.ArrayList;

/**
 * Created by yihangho on 10/28/16.
 */
//@author A0144915A
public class ReminderSearchResult {
    private Task task;
    private ArrayList<Reminder> reminders;

    public static Task getTaskFromReminder(ArrayList<ReminderSearchResult> results, Reminder reminder) {
        for (ReminderSearchResult result : results) {
            if (result.getReminders().contains(reminder)) {
                return result.getTask();
            }
        }
        return null;
    }

    public static int totalReminders(ArrayList<ReminderSearchResult> results) {
        return new ArrayListUtil.ChainableArrayListUtil<>(results)
                .map(ReminderSearchResult::getReminders)
                .map(ArrayList::size)
                .foldr((a, b) -> a + b, 0);
    }

    public ReminderSearchResult(Task task, ArrayList<Reminder> reminders) {
        this.task = task;
        this.reminders = reminders;
    }

    public Task getTask() {
        return this.task;
    }

    public ArrayList<Reminder> getReminders() {
        return this.reminders;
    }
}
