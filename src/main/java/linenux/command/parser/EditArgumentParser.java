package linenux.command.parser;

import java.time.LocalDateTime;
import java.util.ArrayList;

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
    private GenericParser.GenericParserResult parseResult;

    //@@author A0135788M
    /**
     * The public constructor for {@code EditArgumentParser}.
     * @param timeParserManager A {@code TimeParserManager} used to parse any date time string.
     * @param commandFormat A {@code String} representing the format of the command using this class.
     * @param callouts A {@code String}, which is an extra message added to the command result when argument is invalid.
     */
    public EditArgumentParser(TimeParserManager timeParserManager, String commandFormat, String callouts) {
        this.timeParserManager = timeParserManager;
        EditArgumentParser.COMMAND_FORMAT = commandFormat;
        EditArgumentParser.CALLOUTS = callouts;
    }

    //@@author A0144915A
    /**
     * Attempts to parse an argument given by the user.
     * @param original A {@code Task}, the original {@code Task} object.
     * @param result A {@code GenericParserResult}, which is the output of {@code GenericParser}.
     * @return An {@code Either}. Its left slot is a {@code Task}, updated from {@code original} based on
     * {@code argument}, if {@code argument} represents a valid instruction to edit a {@code Task}. Otherwise, its
     * right slot contains a {@code CommandResult} indicating the failure.
     */
    public Either<Task, CommandResult> parse(Task original, GenericParser.GenericParserResult result) {
        this.parseResult = result;

        return Either.<Task, CommandResult>left(original)
                .bind(this::ensureNeedsEdit)
                .bind(this::updateTaskName)
                .bind(this::updateStartTime)
                .bind(this::updateEndTime)
                .bind(this::updateTags)
                .bind(this::ensureValidDateCombination)
                .bind(this::ensureValidEventTimes);
    }

    /**
     * Attempts to extract the new task name from the user argument.
     * @param task A {@code Task}, which is the original {@code Task} object.
     * @return An {@code Either}. If the user argument contains a valid task name, the left slot will be {@code task}
     * with its name updated. Otherwise, its right slot is a {@code CommandResult} indicating the failure.
     */
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

    /**
     * Attempts to extract the new start time from the user argument.
     * @param task A {@code Task}, which is the original {@code Task} object.
     * @return An {@code Either}. If the user argument contains a valid start time, the left slot will be {@code task}
     * with its start time updated. Otherwise, its right slot is a {@code CommandResult} indicating the failure.
     */
    private Either<Task, CommandResult> updateStartTime(Task task) {
        if (this.parseResult.getArguments("st").size() > 0) {
            return parseDateTime(this.parseResult.getArguments("st").get(0))
                    .bind(t -> Either.left(task.setStartTime(t)));
        } else {
            return Either.left(task);
        }
    }

    /**
     * Attempts to extract the new end time from the user argument.
     * @param task A {@code Task}, which is the original {@code Task} object.
     * @return An {@code Either}. If the user argument contains a valid end time, the left slot will be {@code task}
     * with its end time updated. Otherwise, its right slot is a {@code CommandResult} indicating the failure.
     */
    private Either<Task, CommandResult> updateEndTime(Task task) {
        if (this.parseResult.getArguments("et").size() > 0) {
            return parseDateTime(this.parseResult.getArguments("et").get(0))
                    .bind(t -> Either.left(task.setEndTime(t)));
        } else {
            return Either.left(task);
        }
    }

    /**
     * Attempts to extract the new tags from the user argument.
     * @param task A {@code Task}, which is the original {@code Task} object.
     * @return An {@code Either}. If the user argument contains valid new tags, the left slot will be {@code task}
     * with its tags updated. Otherwise, its right slot is a {@code CommandResult} indicating the failure.
     */
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

    /**
     * Ensures that the user argument contains some instructions to edit a task.
     * @param task The {@code Task} to edit.
     * @return An {@code Either}. If the user argument contains some edit instructions, its left slot is {@code task}.
     * Otherwise, its right slot is a {@code CommandResult}.
     */
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

    /**
     * Makes sure that {@code task} has a valid start time/end time combination. In particular, we do not allow
     * a {@code Task} to have a start time but not an end time.
     * @param task The {@code Task} to validate.
     * @return An {@code Either}. Its left slot is {@code task} if {@code task} is valid (in context). Otherwise,
     * its right slot contains a {@code CommandResult} describing the error.
     */
    private Either<Task, CommandResult> ensureValidDateCombination(Task task) {
        if (task.getStartTime() == null || task.getEndTime() != null) {
            return Either.left(task);
        } else {
            return Either.right(makeStartTimeWithoutEndTimeResult());
        }
    }

    /**
     * Makes sure that {@code task} has valid start time/end time. In particular, end time cannot come before start
     * time.
     * @param task The {@code Task} to validate.
     * @return An {@code Either}. Its left slot is {@code task} if {@code task} is valid (in context). Otherwise,
     * its right slot contains a {@code CommandResult} describing the error.
     */
    private Either<Task, CommandResult> ensureValidEventTimes(Task task) {
        if (task.getStartTime() == null || task.getEndTime() == null || task.getEndTime().compareTo(task.getStartTime()) >= 0) {
            return Either.left(task);
        } else {
            return Either.right(makeEndTimeBeforeStartTimeResult());
        }
    }

    /**
     * Attempts to parse a date time string.
     * @param string The {@code String} to parse.
     * @return An {@code Either}. Its left slot is a {@code LocalDateTime} if {@code string} can be parsed. Otherwise,
     * its right slot contains a {@code CommandResult} describing the error.
     */
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
    /**
     * @return A {@code CommandResult} indicating that there is no instructions for change.
     */
    private CommandResult makeNoArgumentsResult() {
        return () -> "No changes to be made!";
    }

    //@@author A0135788
    /**
     * @return A {@code CommandResult} when the user argument is invalid.
     */
    private CommandResult makeInvalidArgumentResult() {
        return () -> "Invalid arguments.\n\n" + COMMAND_FORMAT + "\n\n" + CALLOUTS;
    }

    /**
     * @param dateTime A {@code String} given by the user.
     * @return A {@code CommandResult} describing that {@code dateTime} cannot be parsed.
     */
    private CommandResult makeInvalidDateTimeResult(String dateTime) {
        return () -> "Cannot parse \"" + dateTime + "\".";
    }

    /**
     * @return A {@code CommandResult} describing that a {@code Task} cannot have a start time without an end time.
     */
    private CommandResult makeStartTimeWithoutEndTimeResult() {
        return () -> "Cannot create task with start time but without end time.";
    }

    /**
     * @return A {@code CommandResult} describing that the end time of a {@code Task} cannot come before its start time.
     */
    private CommandResult makeEndTimeBeforeStartTimeResult() {
        return () -> "End time cannot come before start time.";
    }
}
