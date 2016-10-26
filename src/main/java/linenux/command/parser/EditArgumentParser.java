package linenux.command.parser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

import linenux.command.result.CommandResult;
import linenux.control.TimeParserManager;
import linenux.model.Task;
import linenux.util.ArrayListUtil;
import linenux.util.Either;

/**
 * Parses new details of task to be edited.
 */
public class EditArgumentParser {
    public static String COMMAND_FORMAT;
    public static String CALLOUTS;

    private TimeParserManager timeParserManager;
    private GenericParser genericParser;
    private GenericParser.GenericParserResult parseResult;

    //@@author A0135788M
    public EditArgumentParser(TimeParserManager timeParserManager, String commandFormat, String callouts) {
        this.timeParserManager = timeParserManager;
        this.genericParser = new GenericParser();
        EditArgumentParser.COMMAND_FORMAT = commandFormat;
        EditArgumentParser.CALLOUTS = callouts;
    }

    //@@author A0144915A
    public Either<Task, CommandResult> parse(Task original, String argument) {
        this.parseResult = this.genericParser.parse(argument);

        return Either.<Task, CommandResult>left(original)
                .bind(this::ensureNeedsEdit)
                .bind(this::updateTaskName)
                .bind(this::updateStartTime)
                .bind(this::updateEndTime)
                .bind(this::updateTags)
                .bind(this::ensureValidDateCombination)
                .bind(this::ensureValidEventTimes);
    }

    private Either<Task, CommandResult> updateTaskName(Task task) {
        if (this.parseResult.getArguments("n").size() > 0) {
            String taskName = this.parseResult.getArguments("n").get(0);

            if (taskName.length() > 0) {
                return Either.left(task.setTaskName(taskName));
            } else {
                return Either.right(makeInvalidArgumentResult());
            }
        } else {
            return Either.left(task);
        }
    }

    private Either<Task, CommandResult> updateStartTime(Task task) {
        if (this.parseResult.getArguments("st").size() > 0) {
            return parseDateTime(this.parseResult.getArguments("st").get(0))
                    .bind(t -> Either.left(task.setStartTime(t)));
        } else {
            return Either.left(task);
        }
    }

    private Either<Task, CommandResult> updateEndTime(Task task) {
        if (this.parseResult.getArguments("et").size() > 0) {
            return parseDateTime(this.parseResult.getArguments("et").get(0))
                    .bind(t -> Either.left(task.setEndTime(t)));
        } else {
            return Either.left(task);
        }
    }

    private Either<Task, CommandResult> updateTags(Task task) {
        ArrayList<String> tags = ArrayListUtil.unique(this.parseResult.getArguments("#"));

        if (tags.size() == 0) {
            return Either.left(task);
        } else if (tags.indexOf("") != -1) {
            return Either.right(makeInvalidArgumentResult());
        } else if (tags.indexOf("-") != -1) {
            return Either.left(task.setTags(new ArrayList<>()));
        } else {
            return Either.left(task.setTags(tags));
        }
    }

    private Either<Task, CommandResult> ensureNeedsEdit(Task task) {
        boolean needsEdit = new ArrayListUtil.ChainableArrayListUtil<>(new String[]{"n", "st", "et", "#"})
                .map(this.parseResult::getArguments)
                .map(ArrayList::size)
                .map(s -> s > 0)
                .foldr(Boolean::logicalOr, false);

        if (needsEdit) {
            return Either.left(task);
        } else {
            return Either.right(makeNoArgumentsResult());
        }
    }

    private Either<Task, CommandResult> ensureValidDateCombination(Task task) {
        if (task.getStartTime() == null || task.getEndTime() != null) {
            return Either.left(task);
        } else {
            return Either.right(makeStartTimeWithoutEndTimeResult());
        }
    }

    private Either<Task, CommandResult> ensureValidEventTimes(Task task) {
        if (task.getStartTime() == null || task.getEndTime() == null || task.getEndTime().compareTo(task.getStartTime()) >= 0) {
            return Either.left(task);
        } else {
            return Either.right(makeEndTimeBeforeStartTimeResult());
        }
    }

    private Either<LocalDateTime, CommandResult> parseDateTime(String string) {
        if (this.timeParserManager.canParse(string)) {
            return Either.left(this.timeParserManager.delegateTimeParser(string));
        } else if (string.matches("\\s*-\\s*")) {
            return Either.left(null);
        } else {
            return Either.right(makeInvalidDateTimeResult(string));
        }
    }

    //@@author A0135788M
    private CommandResult makeNoArgumentsResult() {
        return () -> "No changes to be made!";
    }

    //@@author A0135788M
    private CommandResult makeInvalidArgumentResult() {
        return () -> "Invalid arguments.\n\n" + COMMAND_FORMAT + "\n\n" + CALLOUTS;
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
