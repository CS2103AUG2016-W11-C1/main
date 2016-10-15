package linenux.command;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.parser.SearchKeywordParser;
import linenux.command.result.CommandResult;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.util.Either;
import linenux.util.TasksListUtil;

/**
 * Generates a list of tasks based on userInput.
 */
public class ListCommand implements Command {
    private static final String TRIGGER_WORD = "list";
    private static final String DESCRIPTION = "Lists all tasks.";
    private static final String COMMAND_FORMAT = "list KEYWORDS";

    private static final String LIST_PATTERN = "(?i)^list(\\s+(?<keywords>.*))?$";

    private Schedule schedule;
    private SearchKeywordParser searchKeywordParser;

    public ListCommand(Schedule schedule) {
        this.schedule = schedule;
        this.searchKeywordParser = new SearchKeywordParser(this.schedule);
    }

    @Override
    public boolean respondTo(String userInput) {
        return userInput.matches(LIST_PATTERN);
    }

    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(LIST_PATTERN);
        assert this.schedule != null;

        String keywords = extractKeywords(userInput);

        if (keywords.trim().isEmpty()) {
            return makeResult(this.schedule.getTaskList());
        }

        Either<ArrayList<Task>, CommandResult> tasks = this.searchKeywordParser.parse(keywords);

        if (tasks.getLeft() != null) {
            return makeResult(tasks.getLeft());
        } else {
            return tasks.getRight();
        }
    }

    @Override
    public String getTriggerWord() {
        return TRIGGER_WORD;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public String getCommandFormat() {
        return COMMAND_FORMAT;
    }

    private String extractKeywords(String userInput) {
        Matcher matcher = Pattern.compile(LIST_PATTERN).matcher(userInput);

        if (matcher.matches() && matcher.group("keywords") != null) {
            return matcher.group("keywords");
        } else {
            return "";
        }
    }

    private CommandResult makeResult(ArrayList<Task> tasks) {
        return () -> TasksListUtil.display(tasks);
    }
}
