package linenux.command.result;

/**
 * A collection of helper functions related to searches.
 */
public class SearchResults {
    //@@author A0144915A
    /**
     * @param keywords The keywords that the user is searching with.
     * @return A {@code CommandResult} informing that there is no tasks that match {@code keywords}.
     */
    public static CommandResult makeNotFoundResult(String keywords) {
        return () -> "Cannot find task names with \"" + keywords + "\".";
    }

    //@@author A0127694U
    /**
     * @param keywords The keywords that the user is searching with.
     * @return A {@code CommandResult} informing that there is no reminders that match {@code keywords}.
     */
    public static CommandResult makeReminderNotFoundResult(String keywords) {
        return () -> "Cannot find reminders with \"" + keywords + "\".";
    }

    public static CommandResult makeTagNotFoundResult(String keywords) {
        return () -> "Cannot find tasks with tag \"" + keywords + "\".";
    }
}
