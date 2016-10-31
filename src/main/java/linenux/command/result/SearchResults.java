package linenux.command.result;

public class SearchResults {
    //@@author A0144915A
    public static CommandResult makeNotFoundResult(String keywords) {
        return () -> "Cannot find task names with \"" + keywords + "\".";
    }

    //@@author A0127694U
    public static CommandResult makeReminderNotFoundResult(String keywords) {
        return () -> "Cannot find reminders with \"" + keywords + "\".";
    }

    public static CommandResult makeListNotFoundResult(String keywords) {
        return () -> "Cannot find task or reminder names with \"" + keywords + "\".";
    }

    public static CommandResult makeTagNotFoundResult(String keywords) {
        return () -> "Cannot find tasks with tag \"" + keywords + "\".";
    }
}
