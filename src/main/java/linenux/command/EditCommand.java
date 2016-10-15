package linenux.command;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.parser.EditTaskArgumentParser;
import linenux.command.result.CommandResult;
import linenux.control.TimeParserManager;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.time.parser.ISODateWithTimeParser;
import linenux.util.Either;
import linenux.util.TasksListUtil;

/**
 * Adds a task to the schedule.
 */
public class EditCommand implements Command {
	public static final String COMMAND_FORMAT = "add TASK_NAME";
	public static final String EDIT_FORMAT = "edit KEYWORDS... [n/NEW_NAME][st/START_TIME][et/END_TIME]";

	private static final String TRIGGER_WORD = "edit";
	private static final String DESCRIPTION = "Edits a task in the schedule.";

	private static final String EDIT_PATTERN = "(?i)^edit((?<keywords>.*?)(?<arguments>((n|st|et)/)+?.*)??)";
	private static final String NUMBER_PATTERN = "^\\d+$";
	private static final String CANCEL_PATTERN = "^cancel$";

	private Schedule schedule;
	private ArrayList<Task> foundTasks;
	private TimeParserManager timeParserManager;
	private EditTaskArgumentParser editTaskArgumentParser;

	private boolean requiresUserResponse;
	private String commandString;

	public EditCommand(Schedule schedule) {
		this.schedule = schedule;

		this.timeParserManager = new TimeParserManager(new ISODateWithTimeParser());
		this.editTaskArgumentParser = new EditTaskArgumentParser(this.timeParserManager);

		this.requiresUserResponse = false;
		this.commandString = null;
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
	public boolean respondTo(String userInput) {
		return userInput.matches(EDIT_PATTERN);
	}

	public String getCommandString() {
		return this.commandString;
	}

	@Override
	public CommandResult execute(String userInput) {
		Matcher matcher = Pattern.compile(EDIT_PATTERN).matcher(userInput);

		if (matcher.matches()){
			assert (this.schedule != null);

			if (matcher.group("keywords") == null || matcher.group("keywords").trim().isEmpty()) {
				return makeNoKeywordsResult();
			}

			String keywords = matcher.group("keywords").trim();
			String[] keywordsArr = keywords.split("\\s+");
			ArrayList<Task> tasks = this.schedule.search(keywordsArr);

			if (matcher.group("arguments") == null) {
				return makeNoArgumentsResult();
			}

			if (tasks.size() == 0) {
				return makeNotFoundResult(keywords);
			} else if (tasks.size() == 1) {
				this.commandString = userInput;
				return implementEdit(tasks.get(0));
			} else {
				this.commandString = userInput;
				this.requiresUserResponse = true;
				this.foundTasks = tasks;

				return makePromptResult(tasks);
			}
		}
		return null;
	}

	@Override
	public boolean awaitingUserResponse() {
		return requiresUserResponse;
	}

	@Override
	public CommandResult userResponse(String userInput) {
		if (userInput.matches(NUMBER_PATTERN)) {
			int index = Integer.parseInt(userInput);

			if (1 <= index && index <= this.foundTasks.size()) {
				Task task = this.foundTasks.get(index - 1);

				this.requiresUserResponse = false;
				this.foundTasks = null;

				return implementEdit(task);
			} else {
				return makeInvalidIndexResult();
			}
		} else if (userInput.matches(CANCEL_PATTERN)) {
			this.requiresUserResponse = false;
			this.foundTasks = null;
			return makeCancelledResult();
		} else {
			return makeInvalidUserResponse(userInput);
		}
	}

	private String extractArgument(String userInput) {
		Matcher matcher = Pattern.compile(EDIT_PATTERN).matcher(userInput);

		if (matcher.matches() && matcher.group("arguments") != null) {
			return matcher.group("arguments");
		} else {
			return "";
		}
	}

	private CommandResult implementEdit(Task original) {
		String argument = extractArgument(commandString);
		this.commandString = null;
		Either<Task, CommandResult> result = editTaskArgumentParser.parse(original, argument);

		if (result.isLeft()) {
			this.schedule.editTask(original, result.getLeft());
			return makeEditedTask(original, result.getLeft());
		} else {
			return result.getRight();
		}
	}

	private CommandResult makeNoKeywordsResult() {
		return () -> "No keywords entered!";
	}

	private CommandResult makeNoArgumentsResult() {
		return () -> "No changes to be made!";
	}

	private CommandResult makeNotFoundResult(String keywords) {
		return () -> "Cannot find \"" + keywords + "\".";
	}

	private CommandResult makeEditedTask(Task original, Task task) {
		return () -> "Edited \"" + original.getTaskName() + "\".\nNew task details: " + task.toString();
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
		this.commandString = null;
		return () -> "OK! Not editing anything.";
	}

	private CommandResult makeInvalidUserResponse(String userInput) {
		return () -> {
			StringBuilder builder = new StringBuilder();
			builder.append("I don't understand \"" + userInput + "\".\n");
			builder.append("Enter a number to indicate which task to edit.\n");
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
