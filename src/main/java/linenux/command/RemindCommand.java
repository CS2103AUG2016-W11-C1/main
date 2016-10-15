package linenux.command;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.parser.ReminderArgumentParser;
import linenux.command.result.CommandResult;
import linenux.control.TimeParserManager;
import linenux.model.Reminder;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.time.parser.ISODateWithTimeParser;
import linenux.util.Either;
import linenux.util.TasksListUtil;

/**
 * Adds a reminder to a task in the schedule
 */
public class RemindCommand implements Command {
	public static final String ARGUMENT_FORMAT = "KEYWORDS... t/TIME [n/NOTE]";
	public static final String COMMAND_FORMAT = "remind " + ARGUMENT_FORMAT;

	private static final String TRIGGER_WORD = "remind";
	private static final String DESCRIPTION = "Adds a reminder to a task in the schedule.";

	private static final String REMIND_PATTERN = "(?i)^remind(\\s+(?<arguments>.*))?$";
    private static final String NUMBER_PATTERN = "^\\d+$";
    private static final String CANCEL_PATTERN = "^cancel$";

	private Schedule schedule;
	private boolean requiresUserResponse;
	private ArrayList<Task> foundTasks;
	private Reminder reminderForPrompt;
	private TimeParserManager timeParserManager;
	private ReminderArgumentParser reminderArgumentParser;

	public RemindCommand(Schedule schedule) {
		this.schedule = schedule;
		this.requiresUserResponse = false;
		this.timeParserManager = new TimeParserManager(new ISODateWithTimeParser());
		this.reminderArgumentParser = new ReminderArgumentParser(this.timeParserManager);
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
	public boolean respondTo(String userInput) {
		return userInput.matches(REMIND_PATTERN);
	}

	@Override
	public CommandResult execute(String userInput) {
		assert userInput.matches(REMIND_PATTERN);
		assert this.schedule != null;

		String argument = extractArgument(userInput);

		Either<String, CommandResult> keywords = extractKeywords(argument);

        if (keywords.isRight()) {
            return keywords.getRight();
        }

        Either<Reminder, CommandResult> reminder = this.reminderArgumentParser.parse(argument);

        if (reminder.isRight()) {
        	return reminder.getRight();
        }

        String actualKeywords = keywords.getLeft();
        String[] keywordsArr = actualKeywords.split("\\s+");
        ArrayList<Task> tasks = this.schedule.search(keywordsArr);
        Reminder actualReminder = reminder.getLeft();

        if (tasks.size() == 0) {
        	return makeNotFoundResult(actualKeywords);
        } else if (tasks.size() == 1) {
        	Task task = tasks.get(0);
        	task.addReminder(actualReminder);
        	return makeResult(task, actualReminder);
        } else {
        	this.requiresUserResponse = true;
        	this.foundTasks = tasks;
        	this.reminderForPrompt = actualReminder;
        	return makePromptResult(tasks);
        }
	}

	private String extractArgument(String userInput) {
        Matcher matcher = Pattern.compile(REMIND_PATTERN).matcher(userInput);

        if (matcher.matches() && matcher.group("arguments") != null) {
            return matcher.group("arguments");
        } else {
            return "";
        }
    }

    private Either<String, CommandResult> extractKeywords(String argument) {
 	    String[] parts = argument.split("(^| )(t|n)/");

 	    if (parts.length > 0 && parts[0].trim().length() > 0) {
            return Either.left(parts[0].trim());
        } else {
            return Either.right(makeInvalidArgumentResult());
        }
    }

    @Override
	public boolean awaitingUserResponse() {
		return requiresUserResponse;
	}

	@Override
	public CommandResult userResponse(String userInput) {
		assert this.foundTasks != null;

		Reminder reminder = this.reminderForPrompt;

		if (userInput.matches(NUMBER_PATTERN)) {
			int index = Integer.parseInt(userInput);

			if (1 <= index && index <= this.foundTasks.size()) {
				Task task = this.foundTasks.get(index - 1);
				task.addReminder(reminder);

				this.requiresUserResponse = false;
				this.foundTasks = null;
				this.reminderForPrompt = null;

				return makeResult(task, reminder);
			} else {
				return makeInvalidIndexResult();
			}
		} else if (userInput.matches(CANCEL_PATTERN)) {
			this.requiresUserResponse = false;
			this.foundTasks = null;
			this.reminderForPrompt = null;
			return makeCancelledResult();
		} else {
			return makeInvalidUserResponse(userInput);
		}
	}

	private CommandResult makeInvalidArgumentResult() {
        return () -> "Invalid arguments.\n\n" + ARGUMENT_FORMAT;
    }

    private CommandResult makeNotFoundResult(String keywords) {
    	return () -> "Cannot find \"" + keywords + "\".";
    }

    private CommandResult makeResult(Task task, Reminder reminder) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma");

    	return () -> "Added reminder on " + reminder.getTimeOfReminder().format(formatter) + " for " + task.getTaskName();
    }

    private CommandResult makePromptResult(ArrayList<Task> tasks) {
    	return () -> {
    		StringBuilder builder = new StringBuilder();
            builder.append("Which one? (1-");
            builder.append(tasks.size());
            builder.append(")\n");

            builder.append(TasksListUtil.display(tasks));

            return builder.toString();
    	};
    }

	private CommandResult makeCancelledResult() {
		return () -> "OK! Not adding new reminder.";
	}

    private CommandResult makeInvalidUserResponse(String userInput) {
        return () -> {
            StringBuilder builder = new StringBuilder();
            builder.append("I don't understand \"" + userInput + "\".\n");
            builder.append("Enter a number to indicate which task to add reminder to:\n");
            builder.append(TasksListUtil.display(this.foundTasks));
            return builder.toString();
        };
    }

    private CommandResult makeInvalidIndexResult() {
        return () -> {
            StringBuilder builder = new StringBuilder();
            builder.append("That's not a valid index. Enter a number between 1 and ");
            builder.append(this.foundTasks.size());
            builder.append(":\n");
            builder.append(TasksListUtil.display(this.foundTasks));
            return builder.toString();
        };
    }

}