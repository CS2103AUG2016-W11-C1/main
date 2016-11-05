package linenux.command.parser;

import java.time.Clock;
import java.time.LocalDateTime;

import linenux.command.result.CommandResult;
import linenux.control.TimeParserManager;
import linenux.util.Either;
import linenux.util.TimeInterval;

//@@author A0144915A
/**
 * A helper class used to parse the arguments to the free time command.
 */
public class FreeTimeArgumentParser extends BaseArgumentParser {
    private GenericParser genericParser;
    private GenericParser.GenericParserResult parseResult;
    private Clock clock;

    /**
     * The public constructor for {@code FreeTimeArgumentParser}.
     * @param timeParserManager A {@code TimeParserManager} used to parse any date time string.
     * @param clock A {@code Clock}. This is used to determine the current time. Helpful when doing DI in tests.
     */
    public FreeTimeArgumentParser(TimeParserManager timeParserManager, Clock clock) {
        this.timeParserManager = timeParserManager;
        this.genericParser = new GenericParser();
        this.clock = clock;
    }

    /**
     * Attempts to parse an argument given by the user.
     * @param argument A {@code String}, which is part of the user input.
     * @return An {@code Either}. Its left slot is a {@code TimeInterval} representing the query range if
     * {@code argument} is valid. Otherwise, its right slot is a {@code CommandResult} indicating the failure.
     */
    public Either<TimeInterval, CommandResult> parse(String argument) {
        this.parseResult = this.genericParser.parse(argument);

        return Either.<TimeInterval, CommandResult>left(new TimeInterval())
                .bind(this::parseStartTime)
                .bind(this::parseEndTime)
                .bind(this::ensureIntervalValidity);
    }

    /**
     * Attempts to extract the start of the query interval.
     * @param interval An existing {@code TimeInterval}.
     * @return An {@code Either}. If the start time can be extracted, its left slot is {@code interval} with its start
     * time set to the start time specified by the user. Otherwise, its right slot is a {@code CommandResult} describing
     * the failure.
     */
    private Either<TimeInterval, CommandResult> parseStartTime(TimeInterval interval) {
        if (this.parseResult.getArguments("st").size() > 0) {
            return parseDateTime(this.parseResult.getArguments("st").get(0))
                    .bind(t -> Either.left(interval.setFrom(t)));
        } else {
            return Either.left(interval.setFrom(LocalDateTime.now(this.clock)));
        }
    }

    /**
     * Attempts to extract the end the query interval.
     * @param interval An existing {@code TimeInterval}.
     * @return An {@code Either}. If the end time can be extracted, its left slot is {@code interval} with its end time
     * set to the end time specified by the user. Otherwise, its right slot is a {@code CommandResult} describing the
     * failure.
     */
    private Either<TimeInterval, CommandResult> parseEndTime(TimeInterval interval) {
        if (this.parseResult.getArguments("et").size() > 0) {
            return parseDateTime(this.parseResult.getArguments("et").get(0))
                    .bind(t -> Either.left(interval.setTo(t)));
        } else {
            return Either.right(makeEndTimeNotSpecified());
        }
    }

    /**
     * Makes sure that {@code interval} represents a valid time interval.
     * @param interval The {@code TimeInterval} to validate.
     * @return An {@code Either}. If {@code interval} is valid, its left slot is {@code interval}. Otherwise, its right
     * slot is a {@code CommandResult} indicating the error.
     */
    private Either<TimeInterval, CommandResult> ensureIntervalValidity(TimeInterval interval) {
        if (interval.getFrom().compareTo(interval.getTo()) <= 0) {
            return Either.left(interval);
        } else {
            return Either.right(makeEndTimeBeforeStartTimeResult());
        }
    }

    /**
     * @return A {@code CommandResult} indicating that the specified end time comes before the speicifed start time.
     */
    private CommandResult makeEndTimeBeforeStartTimeResult() {
        return () -> "End time must be after start time.";
    }

    /**
     * @return A {@code CommandResult} indicating that end time is not specified.
     */
    private CommandResult makeEndTimeNotSpecified() {
        return () -> "End time must be specified.";
    }
}
