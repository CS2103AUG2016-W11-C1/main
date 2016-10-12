package linenux.command.parser;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.result.CommandResult;
import linenux.control.TimeParserManager;
import linenux.model.Task;
import linenux.util.Either;

/**
 * Parses new details of task to be edited.
 */
public class EditTaskArgumentParser {
	public static final String ARGUMENT_FORMAT = "TASK_NAME [n/NEW_TASK_NAME] [st/START_TIME] [et/END_TIME] [#/CATEGORY]...";

	private TimeParserManager timeParserManager;

	public EditTaskArgumentParser(TimeParserManager timeParserManager) {
		this.timeParserManager = timeParserManager;
	}

	public Either<Task, CommandResult> parse(String argument) {
		Either<String, CommandResult> taskName = extractTaskName(argument);
		if (taskName.isRight()) {
			return Either.right(taskName.getRight());
		}

		Either<String, CommandResult> newTaskName = extractNewTaskName(argument);
		if (newTaskName.isRight()) {
			return Either.right(taskName.getRight());
		}

		Either<LocalDateTime, CommandResult> startTime = extractStartTime(argument);
		if (startTime.isRight()) {
			return Either.right(startTime.getRight());
		}

		Either<LocalDateTime, CommandResult> endTime = extractEndTime(argument);
		if (endTime.isRight()) {
			return Either.right(endTime.getRight());
		}

		String actualTaskName = taskName.getLeft();
		LocalDateTime actualStartTime = startTime.getLeft();
		LocalDateTime actualEndTime = endTime.getLeft();

		if (actualStartTime != null && actualEndTime == null) {
			return Either.right(makeStartTimeWithoutEndTimeResult());
		}

		if (actualStartTime != null && actualEndTime != null && actualEndTime.compareTo(actualStartTime) < 0) {
			return Either.right(makeEndTimeBeforeStartTimeResult());
		}

		Task task = new Task(actualTaskName, actualStartTime, actualEndTime);

		return Either.left(task);
	}

	private Either<String, CommandResult> extractTaskName(String argument) {
		String[] parts = argument.split("(^| )(n|st|et)/");

		if (parts.length > 0 && parts[0].trim().length() > 0) {
			return Either.left(parts[0].trim());
		} else {
			return Either.right(makeInvalidArgumentResult());
		}
	}

	private Either<String, CommandResult> extractNewTaskName(String argument) {
		String[] parts = argument.split("(^| )(n|st|et)/");

		if (parts.length > 0 && parts[1].trim().length() > 0) {
			return Either.left(parts[1].trim());
		} else {
			return Either.right(makeInvalidArgumentResult());
		}
	}

	private Either<LocalDateTime, CommandResult> extractStartTime(String argument) {
		Matcher matcher = Pattern.compile("(^|.*? )st/(?<startTime>.*?)(\\s+et/.*)?").matcher(argument);

		if (matcher.matches() && matcher.group("startTime") != null) {
			return parseDateTime(matcher.group("startTime").trim());
		} else {
			return Either.left(null);
		}
	}

	private Either<LocalDateTime, CommandResult> extractEndTime(String argument) {
		Matcher matcher = Pattern.compile("(^|.*? )et/(?<endTime>.*?)(\\s+st/.*)?$").matcher(argument);

		if (matcher.matches() && matcher.group("endTime") != null) {
			return parseDateTime(matcher.group("endTime").trim());
		} else {
			return Either.left(null);
		}
	}

	private Either<LocalDateTime, CommandResult> parseDateTime(String string) {
		if (this.timeParserManager.canParse(string)) {
			return Either.left(this.timeParserManager.delegateTimeParser(string));
		} else {
			return Either.right(makeInvalidDateTimeResult(string));
		}
	}

	private CommandResult makeInvalidArgumentResult() {
		return () -> "Invalid arguments.\n\n" + ARGUMENT_FORMAT;
	}

	private CommandResult makeInvalidDateTimeResult(String dateTime) {
		return () -> "Cannot parse \"" + dateTime + "\".";
	}

	private CommandResult makeStartTimeWithoutEndTimeResult() {
		return () -> "Cannot create task with start time but without end time.";
	}

	private CommandResult makeEndTimeBeforeStartTimeResult() {
		return () -> "End time cannot come before start time.";
	}
}
