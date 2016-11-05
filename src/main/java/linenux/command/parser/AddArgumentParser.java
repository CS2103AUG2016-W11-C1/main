package linenux.command.parser;

import java.util.ArrayList;

import linenux.command.result.CommandResult;
import linenux.control.TimeParserManager;
import linenux.model.Task;
import linenux.util.ArrayListUtil;
import linenux.util.Either;

//@@author A0144915A
/**
 * Parser for the argument portion of add command.
 **/
public class AddArgumentParser extends BaseArgumentParser {
    private GenericParser genericParser = new GenericParser();
    private GenericParser.GenericParserResult parseResult;
    public String commandFormat;
    public String callouts;

    /**
     * The public constructor for {@code AddArgumentParser}.
     * @param timeParserManager A {@code TimeParserManager} used to parse any date time string.
     * @param commandFormat A {@code String} representing the format of the command using this class.
     * @param callouts A {@code String}, which is an extra message added to the command result when argument is invalid.
     */
    public AddArgumentParser(TimeParserManager timeParserManager, String commandFormat, String callouts) {
        this.timeParserManager = timeParserManager;
        this.commandFormat = commandFormat;
        this.callouts = callouts;
    }

    /**
     * Attempts to parse an argument given by the user.
     * @param argument A {@code String}, which is part of the user input.
     * @return An {@code Either}. Its left slot is a {@code Task} if {@code argument} represents a valid {@code Task}.
     */
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

    /**
     * Attempts to extract the name of task from user argument.
     * @param task An existing {@code Task}.
     * @return An {@code Either}. If the name of the task can be extracted, its left slot is {@code task} with its name
     * set to the name specified by the user. Otherwise, its right slot is a {@code CommandResult} describing the
     * failure.
     */
    private Either<Task, CommandResult> extractTaskName(Task task) {
        if (this.parseResult.getKeywords().length() > 0) {
            Task output = task.setTaskName(this.parseResult.getKeywords());
            return Either.left(output);
        } else {
            return Either.right(makeInvalidArgumentResult());
        }
    }

    /**
     * Attempts to extract the start time of task from user argument.
     * @param task An existing {@code Task}.
     * @return An {@code Either}. If the name of the task can be extracted, its left slot is {@code task} with its
     * start time set to the start time specified by the user. Otherwise, its right slot is a {@code CommandResult}
     * describing the failure.
     */
    private Either<Task, CommandResult> extractStartTime(Task task) {
        if (this.parseResult.getArguments("st").size() > 0) {
            return parseDateTime(this.parseResult.getArguments("st").get(0))
                    .bind(t -> Either.left(task.setStartTime(t)));
        } else {
            return Either.left(task);
        }
    }

    /**
     * Attempts to extract the end time of task from user argument.
     * @param task An existing {@code Task}.
     * @return An {@code Either}. If the name of the task can be extracted, its left slot is {@code task} with its
     * end time set to the end time specified by the user. Otherwise, its right slot is a {@code CommandResult}
     * describing the failure.
     */
    private Either<Task, CommandResult> extractEndTime(Task task) {
        if (this.parseResult.getArguments("et").size() > 0) {
            return parseDateTime(this.parseResult.getArguments("et").get(0))
                    .bind(t -> Either.left(task.setEndTime(t)));
        } else {
            return Either.left(task);
        }
    }

    /**
     * Attempts to extract the tags of task from user argument.
     * @param task An existing {@code Task}.
     * @return An {@code Either}. If the name of the task can be extracted, its left slot is {@code task} with its
     * tags set to the tags specified by the user. Otherwise, its right slot is a {@code CommandResult} describing
     * the failure.
     */
    private Either<Task, CommandResult> extractTags(Task task) {
        ArrayList<String> tags = ArrayListUtil.unique(this.parseResult.getArguments("#"));

        if (tags.indexOf("") == -1) {
            return Either.left(task.setTags(tags));
        } else {
            return Either.right(makeInvalidArgumentResult());
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
     * @return A {@code CommandResult} when the user argument is invalid.
     */
    private CommandResult makeInvalidArgumentResult() {
        return () -> "Invalid arguments.\n\n" + commandFormat + "\n\n" + callouts;
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
