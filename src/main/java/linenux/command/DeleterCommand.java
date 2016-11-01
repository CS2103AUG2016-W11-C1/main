package linenux.command;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.result.CommandResult;
import linenux.command.result.PromptResults;
import linenux.command.result.SearchResults;
import linenux.command.util.ReminderSearchResult;
import linenux.model.Schedule;
import linenux.util.ArrayListUtil;
import linenux.util.RemindersListUtil;

/**
 * Handles deletion of reminders from schedule.
 */
// @@author A0127694U
public class DeleterCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "deleter";
    private static final String DESCRIPTION = "Deletes a task reminder from the schedule.";
    private static final String COMMAND_FORMAT = "deleter KEYWORDS";

    private static final String NUMBER_PATTERN = "^\\d+$";
    private static final String CANCEL_PATTERN = "^cancel$";

    private Schedule schedule;
    private boolean requiresUserResponse;
    private ArrayList<ReminderSearchResult> foundReminders;

    public DeleterCommand(Schedule schedule) {
        this.schedule = schedule;
        this.TRIGGER_WORDS.add(TRIGGER_WORD);
    }

    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(getPattern());
        assert this.schedule != null;

        String keywords = extractKeywords(userInput);

        if (keywords.trim().isEmpty()) {
            return makeNoKeywordsResult();
        }

        ArrayList<ReminderSearchResult> results = new ArrayListUtil.ChainableArrayListUtil<>(
                this.schedule.getTaskList()).map(task -> new ReminderSearchResult(task, task.searchReminder(keywords)))
                        .filter(result -> result.getReminders().size() > 0).value();

        int totalResults = new ArrayListUtil.ChainableArrayListUtil<>(results).map(ReminderSearchResult::getReminders)
                .map(ArrayList::size).foldr((a, b) -> a + b, 0);

        if (totalResults == 0) {
            return SearchResults.makeReminderNotFoundResult(keywords);
        } else if (totalResults == 1) {
            ReminderSearchResult result = results.get(0);
            this.schedule.deleteReminder(result);
            return makeDeletedReminder(result);
        } else {
            setResponse(true, results);
            return PromptResults.makePromptReminderIndexResult(results);
        }
    }

    @Override
    public boolean awaitingUserResponse() {
        return requiresUserResponse;
    }

    @Override
    public CommandResult getUserResponse(String userInput) {
        assert this.foundReminders != null;
        assert this.schedule != null;

        if (userInput.matches(NUMBER_PATTERN)) {
            int index = Integer.parseInt(userInput);

            if (1 <= index && index <= this.foundReminders.size()) {
                ReminderSearchResult reminder = this.foundReminders.get(index - 1);
                this.schedule.deleteReminder(reminder);

                setResponse(false, null);
                return makeDeletedReminder(reminder);
            } else {
                return PromptResults.makeInvalidReminderIndexResult(this.foundReminders);
            }
        } else if (userInput.matches(CANCEL_PATTERN)) {
            setResponse(false, null);
            return makeCancelledResult();
        } else {
            return makeInvalidUserResponse(userInput);
        }
    }

    // @@author A0135788M
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

    private void setResponse(boolean requiresUserResponse, ArrayList<ReminderSearchResult> reminders) {
        this.requiresUserResponse = requiresUserResponse;
        this.foundReminders = reminders;
    }

    private CommandResult makeNoKeywordsResult() {
        return () -> "Invalid arguments.\n\n" + COMMAND_FORMAT + "\n\n" + CALLOUTS;
    }

    // @@author A0144915A
    private CommandResult makeDeletedReminder(ReminderSearchResult remind) {
        return () -> "Deleted reminder \"" + remind.getReminders().get(0).toString() + "\" from task \""
                + remind.getTask().getTaskName() + "\".";
    }

    private CommandResult makeCancelledResult() {
        return () -> "OK! Not deleting anything.";
    }

    private CommandResult makeInvalidUserResponse(String userInput) {
        return () -> {
            StringBuilder builder = new StringBuilder();
            builder.append("I don't understand \"" + userInput + "\".\n");
            builder.append("Enter a number to indicate which reminder to delete.\n");
            builder.append(RemindersListUtil.displaySearchResults(this.foundReminders));
            return builder.toString();
        };
    }
}
