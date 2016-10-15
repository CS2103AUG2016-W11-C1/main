package linenux.command.parser;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public Either<Task, CommandResult> parse(Task original, String argument) {
        Either<String, CommandResult> newTaskName = extractNewTaskName(original, argument);
        if (newTaskName.isRight()) {
            return Either.right(newTaskName.getRight());
        }

        Either<LocalDateTime, CommandResult> startTime = extractStartTime(original, argument);
        if (startTime.isRight()) {
            return Either.right(startTime.getRight());
        }

        Either<LocalDateTime, CommandResult> endTime = extractEndTime(original, argument);
        if (endTime.isRight()) {
            return Either.right(endTime.getRight());
        }

        Either<ArrayList<String>, CommandResult> categories = extractCategories(original, argument);
        if (categories.isRight()) {
            return Either.right(categories.getRight());
        }

        String actualTaskName = newTaskName.getLeft();
        LocalDateTime actualStartTime = startTime.getLeft();
        LocalDateTime actualEndTime = endTime.getLeft();
        ArrayList<String> actualCategories = categories.getLeft();

        if (actualStartTime != null && actualEndTime == null) {
            return Either.right(makeStartTimeWithoutEndTimeResult());
        }
        if (actualStartTime != null && actualEndTime != null && actualEndTime.compareTo(actualStartTime) < 0) {
            return Either.right(makeEndTimeBeforeStartTimeResult());
        }

        Task task = new Task(actualTaskName, actualStartTime, actualEndTime, actualCategories);

        return Either.left(task);
    }

    private Either<String, CommandResult> extractNewTaskName(Task original, String argument) {
        Matcher matcher = Pattern.compile("(^|.*?)n/(?<name>.*?)((n|st|et)/.*)?$").matcher(argument);

        if (matcher.matches() && matcher.group("name") != null) {
            if (matcher.group("name").trim().length() > 0) {
                return Either.left(matcher.group("name").trim());
            } else {
                return Either.right(makeInvalidArgumentResult());
            }
        } else {
            return Either.left(original.getTaskName());
        }
    }

    private Either<LocalDateTime, CommandResult> extractStartTime(Task original, String argument) {
        Matcher matcher = Pattern.compile("(^|.*? )st/(?<startTime>.*?)(\\s+(n|et)/.*)?").matcher(argument);

        if (matcher.matches() && matcher.group("startTime") != null) {
            return parseDateTime(matcher.group("startTime").trim());
        } else {
            return Either.left(original.getStartTime());
        }
    }

    private Either<LocalDateTime, CommandResult> extractEndTime(Task original, String argument) {
        Matcher matcher = Pattern.compile("(^|.*? )et/(?<endTime>.*?)(\\s+(n|st)/.*)?$").matcher(argument);

        if (matcher.matches() && matcher.group("endTime") != null) {
            return parseDateTime(matcher.group("endTime").trim());
        } else {
            return Either.left(original.getEndTime());
        }
    }

    private Either<ArrayList<String>, CommandResult> extractCategories(Task original, String argument) {
        Matcher matcher = Pattern.compile("(^|.*? )#/(?<categories>.*?)(\\s+(n|st|et)/.*)?$").matcher(argument);

        if (matcher.matches() && matcher.group("categories") != null) {
            String[] categories = matcher.group("categories").split(" ");
            return getCategoryArray(categories);
        } else {
            return Either.left(original.getCategories());
        }
    }

    private Either<LocalDateTime, CommandResult> parseDateTime(String string) {
        if (this.timeParserManager.canParse(string)) {
            return Either.left(this.timeParserManager.delegateTimeParser(string));
        } else if (string.matches("rm")) {
            return Either.left(null);
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

    private Either<ArrayList<String>, CommandResult> getCategoryArray(String[] categories) {
        if (categories.length == 0) {
            return Either.right(makeInvalidArgumentResult());
        }

        ArrayList<String> categoryList = new ArrayList<String>();

        for (String s : categories) {
            if (s.trim().isEmpty()) {
                return Either.right(makeInvalidArgumentResult());
            }
            categoryList.add(s.trim());
        }

        return Either.left(categoryList);
    }
}
