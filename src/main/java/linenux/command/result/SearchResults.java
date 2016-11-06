package linenux.command.result;

//@@author A0127694U
/**
 * A collection of helper functions related to searches.
 */
public class SearchResults {
    /**
     * @param keywords The keywords that the user is searching with.
     * @return A {@code CommandResult} informing that there is no tasks that match {@code keywords}.
     */
    public static CommandResult makeNotFoundResult(String keywords) {
        return () -> "Cannot find task names with \"" + keywords + "\".";
    }

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
