package linenux.command;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.parser.EditrArgumentParser;
import linenux.command.result.CommandResult;
import linenux.command.result.PromptResults;
import linenux.command.result.SearchResults;
import linenux.control.TimeParserManager;
import linenux.model.Reminder;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.time.parser.ISODateWithTimeParser;
import linenux.util.ArrayListUtil;
import linenux.util.Either;
import linenux.util.TasksListUtil;

/**
 * Edits a task in the schedule.
 */
public class EditrCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "editr";
    private static final String DESCRIPTION = "Edits a reminder in the schedule.";
    public static final String COMMAND_FORMAT = "editr KEYWORDS... [t/TIME] [n/NOTE]";

    private static final String NUMBER_PATTERN = "^\\d+$";
    private static final String CANCEL_PATTERN = "^cancel$";

    private Schedule schedule;
    private boolean requiresUserResponse;
    private String argument;
    private ArrayList<Task> foundTasks;
    private ArrayList<Integer> noOfReminders;
    private ArrayList<Reminder> foundReminders;
    private TimeParserManager timeParserManager;
    private EditrArgumentParser editrArgumentParser;

    public EditrCommand(Schedule schedule) {
        this.schedule = schedule;
        this.timeParserManager = new TimeParserManager(new ISODateWithTimeParser());
        this.editrArgumentParser = new EditrArgumentParser(this.timeParserManager, COMMAND_FORMAT, CALLOUTS);
        this.TRIGGER_WORDS.add(TRIGGER_WORD);
    }

    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(getPattern());
        assert this.schedule != null;

        String keywords = extractKeywords(userInput);
        String argument = extractArgument(userInput);

        if (keywords.trim().isEmpty()) {
            return makeNoKeywordsResult();
        }

        ArrayList<Task> tasks = this.schedule.searchByReminder(keywords);

        if (tasks.size() == 0) {
            return SearchResults.makeReminderNotFoundResult(keywords);
        }

        ArrayList<Integer> noOfReminders = new ArrayListUtil.ChainableArrayListUtil<>(tasks)
                                                            .map(task -> this.schedule.searchReminder(keywords, task).size())
                                                            .value();
        ArrayList<Reminder> filteredReminders = this.schedule.searchReminder(keywords, tasks);

        assert filteredReminders.size() > 0;

        if (filteredReminders.size() == 1) {
            assert tasks.size() == 1;
            assert filteredReminders.size() == 1;

            return implementEditr(tasks.get(0), filteredReminders.get(0), argument);
        } else {
            setResponse(true, tasks, noOfReminders, filteredReminders, argument);
            return PromptResults.makePromptIndexResult(tasks, noOfReminders, filteredReminders);
        }
    }

    @Override
    public boolean awaitingUserResponse() {
        return requiresUserResponse;
    }

    @Override
    public CommandResult userResponse(String userInput) {
        assert this.argument != null;
        assert this.foundTasks != null;
        assert this.schedule != null;

        if (userInput.matches(NUMBER_PATTERN)) {
            int index = Integer.parseInt(userInput);
            if (1 <= index && index <= this.foundReminders.size()) {
                Reminder reminder = this.foundReminders.get(index - 1);

                int counter = noOfReminders.get(0);
                int taskIndex = 0;

                for (int i = 0; i < this.foundTasks.size(); i++) {
                    if (index <= counter) {
                        taskIndex = i;
                        break;
                    }
                    counter += noOfReminders.get(i + 1);
                }

                CommandResult result = implementEditr(this.foundTasks.get(taskIndex), reminder, this.argument);
                setResponse(false, null, null, null, null);
                return result;
            } else {
                return PromptResults.makeInvalidIndexResult(this.foundTasks, this.noOfReminders, this.foundReminders);
            }
        } else if (userInput.matches(CANCEL_PATTERN)) {
            setResponse(false, null, null, null, null);
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
        Either<Reminder, CommandResult> result = editrArgumentParser.parse(original, argument);

        if (result.isLeft()) {
            Reminder newReminder = result.getLeft();
            Task newTask = task.removeReminder(original).addReminder(newReminder);
            this.schedule.updateTask(task, newTask);
            return makeEditedReminder(original, newReminder);
        } else {
            return result.getRight();
        }
    }

    private void setResponse(boolean requiresUserResponse, ArrayList<Task> foundTasks, ArrayList<Integer> noOfReminders,
            ArrayList<Reminder> foundReminders, String argument) {
        this.requiresUserResponse = requiresUserResponse;
        this.foundTasks = foundTasks;
        this.noOfReminders = noOfReminders;
        this.foundReminders = foundReminders;
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
            builder.append(TasksListUtil.display(this.foundTasks, this.noOfReminders, this.foundReminders));
            return builder.toString();
        };
    }
}
