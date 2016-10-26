package linenux.command.parser;

import java.time.LocalDateTime;
import java.util.ArrayList;

import linenux.command.result.CommandResult;
import linenux.control.TimeParserManager;
import linenux.model.Task;
import linenux.util.ArrayListUtil;
import linenux.util.Either;

/**
 * Parser for the argument portion of add command.
 **/
//@@author A0135788M
public class AddArgumentParser {
    public static String COMMAND_FORMAT;
    public static String CALLOUTS;

    private TimeParserManager timeParserManager;
    private GenericParser genericParser = new GenericParser();
    private GenericParser.GenericParserResult parseResult;

    //@@author A0135788M
    public AddArgumentParser(TimeParserManager timeParserManager, String commandFormat, String callouts) {
        this.timeParserManager = timeParserManager;
        AddArgumentParser.COMMAND_FORMAT = commandFormat;
        AddArgumentParser.CALLOUTS = callouts;
    }

    //@@author A0144915A
    public Either<Task, CommandResult> parse(String argument) {
        this.parseResult = this.genericParser.parse(argument);

        return Either.<Task, CommandResult>left(new Task(""))
                .bind(this::extractTaskName)
                .bind(this::extractStartTime)
                .bind(this::extractEndTime)
                .bind(this::extractTags)
                .bind(this::ensureValidDateCombination)
                .bind(this::ensureValidEventTimes);
    }

    private Either<Task, CommandResult> extractTaskName(Task task) {
        if (this.parseResult.getKeywords().length() > 0) {
            Task output = task.setTaskName(this.parseResult.getKeywords());
            return Either.left(output);
        } else {
            return Either.right(makeInvalidArgumentResult());
        }
    }

    private Either<Task, CommandResult> extractStartTime(Task task) {
        if (this.parseResult.getArguments("st").size() > 0) {
            return parseDateTime(this.parseResult.getArguments("st").get(0))
                    .bind(t -> Either.left(task.setStartTime(t)));
        } else {
            return Either.left(task);
        }
    }

    private Either<Task, CommandResult> extractEndTime(Task task) {
        if (this.parseResult.getArguments("et").size() > 0) {
            return parseDateTime(this.parseResult.getArguments("et").get(0))
                    .bind(t -> Either.left(task.setEndTime(t)));
        } else {
            return Either.left(task);
        }
    }

    private Either<Task, CommandResult> extractTags(Task task) {
        ArrayList<String> tags = ArrayListUtil.unique(this.parseResult.getArguments("#"));

        if (tags.indexOf("") == -1) {
            return Either.left(task.setTags(tags));
        } else {
            return Either.right(makeInvalidArgumentResult());
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
        } else {
            return Either.right(makeInvalidDateTimeResult(string));
        }
    }

    //@@author A0135788M
    private CommandResult makeInvalidArgumentResult() {
        return () -> "Invalid arguments.\n\n" + COMMAND_FORMAT + "\n\n" + CALLOUTS;
    }

    //@@author A0144915A
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
