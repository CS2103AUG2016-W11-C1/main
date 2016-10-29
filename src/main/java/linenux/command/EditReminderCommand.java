package linenux.command;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.parser.EditReminderArgumentParser;
import linenux.command.result.CommandResult;
import linenux.command.result.PromptResults;
import linenux.command.result.SearchResults;
import linenux.command.util.ReminderSearchResult;
import linenux.control.TimeParserManager;
import linenux.model.Reminder;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.time.parser.ISODateWithTimeParser;
import linenux.util.ArrayListUtil;
import linenux.util.Either;
import linenux.util.RemindersListUtil;

/**
 * Edits a task in the schedule.
 */
public class EditReminderCommand extends AbstractCommand {

    private static final String TRIGGER_WORD = "editr";
    private static final String DESCRIPTION = "Edits a reminder in the schedule.";
    public static final String COMMAND_FORMAT = "editr KEYWORDS... [t/TIME] [n/NOTE]";

    private static final String NUMBER_PATTERN = "^\\d+$";
    private static final String CANCEL_PATTERN = "^cancel$";

    private Schedule schedule;
    private boolean requiresUserResponse;
    private String argument;
    private TimeParserManager timeParserManager;
    private EditReminderArgumentParser editReminderArgumentParser;
    private ArrayList<ReminderSearchResult> searchResults;

    //@@author A0140702X
    public EditReminderCommand(Schedule schedule) {
        this.schedule = schedule;
        this.timeParserManager = new TimeParserManager(new ISODateWithTimeParser());
        this.editReminderArgumentParser = new EditReminderArgumentParser(this.timeParserManager, COMMAND_FORMAT, CALLOUTS);
        this.TRIGGER_WORDS.add(TRIGGER_WORD);
    }

    //@@author A0144915A
    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(getPattern());
        assert this.schedule != null;

        String keywords = extractKeywords(userInput);
        String argument = extractArgument(userInput);

        if (keywords.trim().isEmpty()) {
            return makeNoKeywordsResult();
        }

        ArrayList<ReminderSearchResult> results = new ArrayListUtil.ChainableArrayListUtil<>(this.schedule.getTaskList())
                .map(task -> new ReminderSearchResult(task, task.searchReminder(keywords)))
                .filter(result -> result.getReminders().size() > 0)
                .value();

        int totalResults = new ArrayListUtil.ChainableArrayListUtil<>(results)
                .map(ReminderSearchResult::getReminders)
                .map(ArrayList::size)
                .foldr((a, b) -> a + b, 0);

        if (totalResults == 0) {
            return SearchResults.makeReminderNotFoundResult(keywords);
        } else if (totalResults == 1) {
            ReminderSearchResult result = results.get(0);
            return implementEditr(result.getTask(), result.getReminders().get(0), argument);
        } else {
            setResponse(true, results, argument);
            return PromptResults.makePromptReminderIndexResult(results);
        }
    }

    //@@author A0140702X
    @Override
    public boolean awaitingUserResponse() {
        return requiresUserResponse;
    }

    @Override
    public CommandResult userResponse(String userInput) {
        assert this.argument != null;
        assert this.schedule != null;
        assert this.searchResults != null;

        ArrayList<Reminder> remindersFound = new ArrayListUtil.ChainableArrayListUtil<>(searchResults)
                .map(ReminderSearchResult::getReminders)
                .foldr((r, l) -> {
                    l.addAll(r);
                    return l;
                }, new ArrayList<Reminder>())
                .value();

        if (userInput.matches(NUMBER_PATTERN)) {
            int index = Integer.parseInt(userInput);
            if (1 <= index && index <= remindersFound.size()) {
                Reminder reminder = remindersFound.get(index - 1);
                Task task = ReminderSearchResult.getTaskFromReminder(this.searchResults, reminder);

                CommandResult result = implementEditr(task, reminder, this.argument);
                setResponse(false, null, null);
                return result;
            } else {
                return PromptResults.makeInvalidReminderIndexResult(this.searchResults);
            }
        } else if (userInput.matches(CANCEL_PATTERN)) {
            setResponse(false, null, null);
            return makeCancelledResult();
        } else {
            return makeInvalidUserResponse(userInput);
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

    @Override
    public String getPattern() {
        return "(?i)^\\s*(" + getTriggerWordsPattern() + ")((?<keywords>.*?)(?<arguments>((n|t)/)+?.*)??)";
    }

    private String extractKeywords(String userInput) {
        Matcher matcher = Pattern.compile(getPattern()).matcher(userInput);

        if (matcher.matches() && matcher.group("keywords") != null) {
            return matcher.group("keywords").trim(); //TODO
        } else {
            return "";
        }
    }

    private String extractArgument(String userInput) {
        Matcher matcher = Pattern.compile(getPattern()).matcher(userInput);

        if (matcher.matches() && matcher.group("arguments") != null) {
            return matcher.group("arguments");
        } else {
            return "";
        }
    }

    private CommandResult implementEditr(Task task, Reminder original, String argument) {
        Either<Reminder, CommandResult> result = editReminderArgumentParser.parse(original, argument);

        if (result.isLeft()) {
            Reminder newReminder = result.getLeft();
            Task newTask = task.removeReminder(original).addReminder(newReminder);
            this.schedule.updateTask(task, newTask);
            return makeEditedReminder(original, newReminder);
        } else {
            return result.getRight();
        }
    }

    private void setResponse(boolean requiresUserResponse, ArrayList<ReminderSearchResult> results, String argument) {
        this.requiresUserResponse = requiresUserResponse;
        this.searchResults = results;
        this.argument = argument;
    }

    private CommandResult makeNoKeywordsResult() {
        return () -> "Invalid arguments.\n\n" + COMMAND_FORMAT + "\n\n" + CALLOUTS;
    }

    private CommandResult makeEditedReminder(Reminder original, Reminder reminder) {
        return () -> "Edited \"" + original.getNote() + "\".\nNew reminder details: " + reminder.toString();
    }

    private CommandResult makeCancelledResult() {
        return () -> "OK! Not editing anything.";
    }

    private CommandResult makeInvalidUserResponse(String userInput) {
        return () -> {
            StringBuilder builder = new StringBuilder();
            builder.append("I don't understand \"" + userInput + "\".\n");
            builder.append("Enter a number to indicate which reminder to edit.\n");
            builder.append(RemindersListUtil.displaySearchResults(this.searchResults));
            return builder.toString();
        };
    }
}
