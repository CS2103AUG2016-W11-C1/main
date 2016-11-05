package linenux.command.parser;

import java.time.LocalDateTime;

import linenux.command.result.CommandResult;
import linenux.control.TimeParserManager;
import linenux.model.Reminder;
import linenux.util.Either;

/**
 * A parser used to parse the arguments for the remind command.
 */
public class ReminderArgumentParser {
    public static String COMMAND_FORMAT;
    public static String CALLOUTS;

    private TimeParserManager timeParserManager;
    private GenericParser.GenericParserResult parseResult;

    //@@author A0135788M
    /**
     * The public constructor for {@code ReminderArgumentParser}.
     * @param timeParserManager A {@code TimeParserManager} used to parse any date time string.
     * @param commandFormat A {@code String} representing the format of the command using this class.
     * @param callouts A {@code String}, which is an extra message added to the command result when argument is invalid.
     */
    public ReminderArgumentParser(TimeParserManager timeParserManager, String commandFormat, String callouts) {
        this.timeParserManager = timeParserManager;
        ReminderArgumentParser.COMMAND_FORMAT = commandFormat;
        ReminderArgumentParser.CALLOUTS = callouts;
    }

    //@@author A0144915A
    /**
     * Attempts to parse an argument given by the user.
     * @param result A {@code GenericParserResult}, which is the output of {@code GenericParser}.
     * @return An {@code Either}. Its left slot is a {@code Reminder}, constructed based on {@code argument}, if
     * {@code argument} represents a valid {@code Reminder}. Otherwise, its right slot contains a {@code CommandResult}
     * indicating the failure.
     */
    public Either<Reminder, CommandResult> parse(GenericParser.GenericParserResult result) {
        this.parseResult = result;

        return Either.<Reminder, CommandResult>left(new Reminder())
                .bind(this::extractTime)
                .bind(this::extractNote);
    }

    /**
     * Attempts to extract the reminder time from the user argument.
     * @param reminder A {@code Reminder} to store the extracted time.
     * @return An {@code Either}. If the user argument contains a valid date time string, the left slot will be
     * {@code reminder} with its time updated. Otherwise, its right slot is a {@code CommandResult} indicating the
     * failure.
     */
    private Either<Reminder, CommandResult> extractTime(Reminder reminder) {
        if (this.parseResult.getArguments("t").size() > 0) {
            return parseDateTime(this.parseResult.getArguments("t").get(0))
                    .bind(t -> Either.left(reminder.setTimeOfReminder(t)));
        } else {
            return Either.right(makeWithoutDateResult());
        }
    }

    /**
     * Attempts to extract the reminder note from the user argument.
     * @param reminder A {@code Reminder} to store the extracted time.
     * @return An {@code Either}. If the user argument contains a valid note, the left slot will be {@code original}
     * with its note updated. Otherwise, its right slot is a {@code CommandResult} indicating the failure.
     */
    private Either<Reminder, CommandResult> extractNote(Reminder reminder) {
        if (this.parseResult.getArguments("n").size() > 0) {
            return Either.left(reminder.setNote(this.parseResult.getArguments("n").get(0)));
        } else {
            return Either.right(makeWithoutNoteResult());
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
        } else {
            return Either.right(makeInvalidDateTimeResult(string));
        }
    }

    /**
     * @param dateTime A {@code String} given by the user.
     * @return A {@code CommandResult} describing that {@code dateTime} cannot be parsed.
     */
    private CommandResult makeInvalidDateTimeResult(String dateTime) {
        return () -> "Cannot parse \"" + dateTime + "\".";
    }

    /**
     * @return A {@code CommandResult} indicating that the user has not specified a date.
     */
    private CommandResult makeWithoutDateResult() {
        return () -> "Cannot create reminder without date.";
    }

    /**
     * @return A {@code CommandResult} indicating that the user has not specified a note.
     */
    private CommandResult makeWithoutNoteResult() {
        return () -> "Cannot create reminder without note.";
    }
}
