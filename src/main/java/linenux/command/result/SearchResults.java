package linenux.command.result;

/**
 * Created by yihangho on 10/20/16.
 */
public class SearchResults {
    //@@author A0144915A
    public static CommandResult makeNotFoundResult(String keywords) {
        return () -> "Cannot find task names with \"" + keywords + "\".";
    }

    //@@author A0127694U
    public static CommandResult makeListNotFoundResult(String keywords) {
        return () -> "Cannot find task or reminder names with \"" + keywords + "\".";
    }
}
