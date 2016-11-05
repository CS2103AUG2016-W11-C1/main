package linenux.command;

import java.util.ArrayList;

import linenux.command.parser.EditReminderArgumentParser;
import linenux.command.parser.GenericParser;
import linenux.command.result.CommandResult;
import linenux.command.result.PromptResults;
import linenux.command.result.SearchResults;
import linenux.command.util.ReminderSearchResult;
import linenux.control.TimeParserManager;
import linenux.model.Reminder;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.time.parser.ISODateWithTimeParser;
import linenux.time.parser.StandardDateWithTimeParser;
import linenux.time.parser.TodayWithTimeParser;
import linenux.time.parser.TomorrowWithTimeParser;
import linenux.util.ArrayListUtil;
import linenux.util.Either;
import linenux.util.RemindersListUtil;

//@@author A0140702X
/**
 * Edits a task in the schedule.
 */
public class EditReminderCommand extends AbstractCommand {

    private static final String TRIGGER_WORD = "editr";
    private static final String DESCRIPTION = "Edits a reminder in the schedule.";
    public static final String COMMAND_FORMAT = "editr KEYWORDS [t/TIME] [n/NOTE]";

    private static final String NUMBER_PATTERN = "^\\d+$";
    private static final String CANCEL_PATTERN = "^cancel$";

    private Schedule schedule;
    private boolean requiresUserResponse;
    private GenericParser.GenericParserResult parseResult;
    private TimeParserManager timeParserManager;
    private EditReminderArgumentParser editReminderArgumentParser;
    private ArrayList<ReminderSearchResult> searchResults;

    /**
     * Constructs an {@code EditReminderCommand}.
     * @param schedule The {@code Schedule} to search and edit {@code Reminder} from.
     */
    public EditReminderCommand(Schedule schedule) {
        this.schedule = schedule;
        this.timeParserManager = new TimeParserManager(new ISODateWithTimeParser(), new StandardDateWithTimeParser(), new TodayWithTimeParser(), new TomorrowWithTimeParser());
        this.editReminderArgumentParser = new EditReminderArgumentParser(this.timeParserManager);
        this.TRIGGER_WORDS.add(TRIGGER_WORD);
    }

    /**
     * Executes the command based on {@code userInput}. This method operates under the assumption that
     * {@code respondTo(userInput)} is {@code true}.
     * @param userInput A {@code String} representing the user input.
     * @return A {@code CommandResult} representing the result of the command.
     */
    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(getPattern());
        assert this.schedule != null;

        String argument = extractArgument(userInput);
        GenericParser parser = new GenericParser();
        GenericParser.GenericParserResult result = parser.parse(argument);

        if (result.getKeywords().isEmpty()) {
            return makeNoKeywordsResult();
        }

        ArrayList<ReminderSearchResult> results = new ArrayListUtil.ChainableArrayListUtil<>(this.schedule.getTaskList())
                .map(task -> new ReminderSearchResult(task, task.searchReminder(result.getKeywords())))
                .filter(r -> r.getReminders().size() > 0)
                .value();

        int totalResults = new ArrayListUtil.ChainableArrayListUtil<>(results)
                .map(ReminderSearchResult::getReminders)
                .map(ArrayList::size)
                .foldr((a, b) -> a + b, 0);

        if (totalResults == 0) {
            return SearchResults.makeReminderNotFoundResult(result.getKeywords());
        } else if (totalResults == 1) {
            ReminderSearchResult searchResult = results.get(0);
            return implementEditr(searchResult.getTask(), searchResult.getReminders().get(0), result);
        } else {
            setResponse(true, results, result);
            return PromptResults.makePromptReminderIndexResult(results);
        }
    }

    /**
     * @return {@code true} if and only if this {@code Command} is awaiting for user response.
     */
    @Override
    public boolean isAwaitingUserResponse() {
        return requiresUserResponse;
    }

    /**
     * Process the response given by the user.
     * @param userInput {@code String} representing the user response.
     * @return A {@code CommandResult}, which is the result of processing {@code userInput}.
     */
    @Override
    public CommandResult processUserResponse(String userInput) {
        assert this.parseResult != null;
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

                CommandResult result = implementEditr(task, reminder, this.parseResult);
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

    /**
     * @return A {@code String} representing the default command word.
     */
    @Override
    public String getTriggerWord() {
        return TRIGGER_WORD;
    }

    /**
     * @return A {@code String} describing what this {@code Command} does.
     */
    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    /**
     * @return A {@code String} describing the format that this {@code Command} expects.
     */
    @Override
    public String getCommandFormat() {
        return COMMAND_FORMAT;
    }

    private CommandResult implementEditr(Task task, Reminder original, GenericParser.GenericParserResult parseResult) {
        Either<Reminder, CommandResult> result = editReminderArgumentParser.parse(original, parseResult);

        if (result.isLeft()) {
            Reminder newReminder = result.getLeft();
            Task newTask = task.removeReminder(original).addReminder(newReminder);
            this.schedule.updateTask(task, newTask);
            return makeEditedReminder(original, newReminder);
        } else {
            return result.getRight();
        }
    }

    /**
     * Updates the user response status.
     * @param requiresUserResponse Whether or not this {@code Command} is expecting user response.
     * @param results An {@code ArrayList} of {@code ReminderSearchResult} representing the search results.
     * @param result The result of parsing user argument.
     */
    private void setResponse(boolean requiresUserResponse, ArrayList<ReminderSearchResult> results, GenericParser.GenericParserResult result) {
        this.requiresUserResponse = requiresUserResponse;
        this.searchResults = results;
        this.parseResult = result;
    }

    /**
     * @return A {@code CommandResult} indicating that the user does not specify a keywords.
     */
    private CommandResult makeNoKeywordsResult() {
        return () -> "Invalid arguments.\n\n" + COMMAND_FORMAT + "\n\n" + CALLOUTS;
    }

    /**
     * @param original The {@code Reminder} before being edited.
     * @param reminder The updated {@code Reminder}.
     * @return A {@code CommandResult} informing the user that {@code original} has been updated to {@code reminder}.
     */
    private CommandResult makeEditedReminder(Reminder original, Reminder reminder) {
        return () -> "Edited \"" + original.getNote() + "\".\nNew reminder details: " + reminder.toString();
    }

    /**
     * @return A {@code CommandResult} indicating that the edit reminder operation is cancelled.
     */
    private CommandResult makeCancelledResult() {
        return () -> "OK! Not editing anything.";
    }

    /**
     * @param userInput A {@code String} representing the user response.
     * @return A {@code CommandResult} indicating that {@code userInput} is invalid.
     */
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
