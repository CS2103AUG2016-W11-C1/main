package linenux.command.util;

import linenux.model.Reminder;
import linenux.model.Task;
import linenux.util.ArrayListUtil;

import java.util.ArrayList;

//@author A0144915A

/**
 * The class of an object used to represent the result of searching for {@code Reminder}.
 */
public class ReminderSearchResult {
    private Task task;
    private ArrayList<Reminder> reminders;

    /**
     * From an {@code ArrayList} of {@code ReminderSearchResult}, return the {@code Task} that {@code reminder} belongs
     * to.
     * @param results An {@code ArrayList} of {@code ReminderSearchResult}.
     * @param reminder The {@code Reminder} that we are interested in.
     * @return A {@code Task} such that {@code reminder} is one of its reminders.
     */
    public static Task getTaskFromReminder(ArrayList<ReminderSearchResult> results, Reminder reminder) {
        for (ReminderSearchResult result : results) {
            if (result.getReminders().contains(reminder)) {
                return result.getTask();
            }
        }
        return null;
    }

    /**
     * Given an {@code ArrayList} of {@code ReminderSearchResult}, count the total number of {@code Reminder}.
     * @param results The {@code ArrayList} of {@code ReminderSearchResult}.
     * @return The total number of {@code Reminder} that {@code results} have.
     */
    public static int totalReminders(ArrayList<ReminderSearchResult> results) {
        return new ArrayListUtil.ChainableArrayListUtil<>(results)
                .map(ReminderSearchResult::getReminders)
                .map(ArrayList::size)
                .foldr((a, b) -> a + b, 0);
    }

    /**
     * Construct a {@code ReminderSearchResult} from a {@code Task} and an {@code ArrayList} of {@code Reminder}.
     * @param task A {@code Task}.
     * @param reminders An {@code ArrayList} of {@code Reminder} such that each element is a reminder of {@code task}
     *                  matching some search criteria.
     */
    public ReminderSearchResult(Task task, ArrayList<Reminder> reminders) {
        this.task = task;
        this.reminders = reminders;
    }

    /**
     * @return The {@code Task} that this {@code ReminderSearchResult} is wrapping.
     */
    public Task getTask() {
        return this.task;
    }

    /**
     * @return An {@code ArrayList} of {@code Reminder} that match some search criteria.
     */
    public ArrayList<Reminder> getReminders() {
        return this.reminders;
    }
}
