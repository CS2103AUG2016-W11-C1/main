package linenux.command.result;

/**
 * Created by yihangho on 10/20/16.
 */
public class SearchResults {
    public static CommandResult makeNotFoundResult(String keywords) {
        return () -> "Cannot find task names with \"" + keywords + "\".";
    }
}
