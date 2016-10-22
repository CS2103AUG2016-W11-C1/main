package linenux.command;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.result.CommandResult;
import linenux.command.result.SearchResults;
import linenux.model.Reminder;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.util.TasksListUtil;

/**
 * Generates a list of tasks based on userInput.
 */
public class ListCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "list";
    private static final String DESCRIPTION = "Lists tasks and reminders.";
    private static final String COMMAND_FORMAT = "list [KEYWORDS...] [st/START_TIME] [et/END_TIME]";

    private Schedule schedule;

    public ListCommand(Schedule schedule) {
        this.schedule = schedule;
        this.TRIGGER_WORDS.add(TRIGGER_WORD);
    }

    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(getPattern());
        assert this.schedule != null;

        String keywords = extractKeywords(userInput);

        if (keywords.trim().isEmpty()) {
            return makeResult(this.schedule.getTaskList(), this.schedule.getReminderList());
        }

/**
        Either<ArrayList<Task>, CommandResult> tasks = this.searchKeywordParser.parse(keywords);
        Either<ArrayList<Reminder>, CommandResult> reminders = this.searchKeywordParser.parseReminder(keywords);

        if (tasks.isLeft()) {
            if (reminders.isLeft()) {
                return makeResult(tasks.getLeft(), reminders.getLeft());
            } else {
                return makeResult(tasks.getLeft(), null);
            }
        } else {
            if (reminders.isLeft()) {
                return makeResult(null, reminders.getLeft());
            } else {
                return tasks.getRight();
            }
**/
        ArrayList<Task> tasks = this.schedule.search(keywords);
        ArrayList<Reminder> reminders = this.schedule.searchReminder(keywords);

        if (tasks.size() == 0 && reminders.size() == 0) {
            return SearchResults.makeNotFoundResult(keywords);
        } else {
            return makeResult(tasks, reminders);
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
        Matcher matcher = Pattern.compile(getPattern()).matcher(userInput);

        if (matcher.matches() && matcher.group("keywords") != null) {
            return matcher.group("keywords");
        } else {
            return "";
        }
    }

    private CommandResult makeResult(ArrayList<Task> tasks, ArrayList<Reminder> reminders) {
        return () -> TasksListUtil.display(tasks, reminders);
    }
}
