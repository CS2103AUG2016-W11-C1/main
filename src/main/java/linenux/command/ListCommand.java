package linenux.command;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.filter.ListArgumentFilter;
import linenux.command.result.CommandResult;
import linenux.command.result.SearchResults;
import linenux.control.TimeParserManager;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.time.parser.ISODateWithTimeParser;
import linenux.util.Either;

/**
 * Generates a list of tasks based on userInput.
 */
public class ListCommand implements Command {
    private static final String TRIGGER_WORD = "list";
    private static final String DESCRIPTION = "Lists tasks and reminders.";
    private static final String COMMAND_FORMAT = "list [KEYWORDS...] [st/START_TIME] [et/END_TIME] [#/TAG]";

    private static final String LIST_PATTERN = "(?i)^list((?<keywords>.*?)(?<arguments>((st|et|#)/)+?.*)??)";

    private Schedule schedule;
    private TimeParserManager timeParserManager;
    private ListArgumentFilter listArgumentFilter;

    public ListCommand(Schedule schedule) {
        this.schedule = schedule;
        this.timeParserManager = new TimeParserManager(new ISODateWithTimeParser());
        this.listArgumentFilter = new ListArgumentFilter(this.timeParserManager, COMMAND_FORMAT, CALLOUTS);
    }

    @Override
    public boolean respondTo(String userInput) {
        return userInput.matches(LIST_PATTERN);
    }

    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(LIST_PATTERN);
        assert this.schedule != null;

        ArrayList<Task> tasks;

        if (this.schedule.getTaskList().isEmpty()) {
            return makeEmptyTaskListResult();
        }

        String keywords = extractKeywords(userInput);
        String arguments = extractArgument(userInput);

        if (keywords.trim().isEmpty()) {
            tasks = this.schedule.getTaskList();
        } else {
            tasks = this.schedule.search(keywords);
        }

        Either<ArrayList<Task>, CommandResult> filterTasks = this.listArgumentFilter.filter(arguments, tasks);

        if (filterTasks.isRight()) {
            return filterTasks.getRight();
        }

        ArrayList<Task> actualFilterTasks = filterTasks.getLeft();
        if (actualFilterTasks.size() == 0) {
            return SearchResults.makeNotFoundResult(keywords);
        } else {
            return makeResult(actualFilterTasks);
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

    private String extractArgument(String userInput) {
        Matcher matcher = Pattern.compile(LIST_PATTERN).matcher(userInput);

        if (matcher.matches() && matcher.group("arguments") != null) {
            return matcher.group("arguments");
        } else {
            return "";
        }
    }

    private CommandResult makeEmptyTaskListResult() {
        return () -> "You have no tasks to list!";
    }

    private CommandResult makeResult(ArrayList<Task> tasks) {
        this.schedule.addFilterTasks(tasks);
        return () -> "Listing tasks!";
    }
}
