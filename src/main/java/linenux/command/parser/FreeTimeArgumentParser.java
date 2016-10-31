package linenux.command.parser;

import java.time.Clock;
import java.time.LocalDateTime;

import linenux.command.result.CommandResult;
import linenux.control.TimeParserManager;
import linenux.util.Either;
import linenux.util.TimeInterval;

//@@author A0144915A
public class FreeTimeArgumentParser {
    private TimeParserManager timeParserManager;
    private GenericParser genericParser;
    private GenericParser.GenericParserResult parseResult;
    private Clock clock;

    public FreeTimeArgumentParser(TimeParserManager timeParserManager, Clock clock) {
        this.timeParserManager = timeParserManager;
        this.genericParser = new GenericParser();
        this.clock = clock;
    }

    public Either<TimeInterval, CommandResult> parse(String argument) {
        this.parseResult = this.genericParser.parse(argument);

        return Either.<TimeInterval, CommandResult>left(new TimeInterval())
                .bind(this::parseStartTime)
                .bind(this::parseEndTime)
                .bind(this::ensureIntervalValidity);
    }

    private Either<TimeInterval, CommandResult> parseStartTime(TimeInterval interval) {
        if (this.parseResult.getArguments("st").size() > 0) {
            return parseDateTime(this.parseResult.getArguments("st").get(0))
                    .bind(t -> Either.left(interval.setFrom(t)));
        } else {
            return Either.left(interval.setFrom(LocalDateTime.now(this.clock)));
        }
    }

    private Either<TimeInterval, CommandResult> parseEndTime(TimeInterval interval) {
        if (this.parseResult.getArguments("et").size() > 0) {
            return parseDateTime(this.parseResult.getArguments("et").get(0))
                    .bind(t -> Either.left(interval.setTo(t)));
        } else {
            return Either.right(makeEndTimeNotSpecified());
        }
    }

    private Either<TimeInterval, CommandResult> ensureIntervalValidity(TimeInterval interval) {
        if (interval.getFrom().compareTo(interval.getTo()) <= 0) {
            return Either.left(interval);
        } else {
            return Either.right(makeEndTimeBeforeStartTimeResult());
        }
    }

    private Either<LocalDateTime, CommandResult> parseDateTime(String string) {
        if (this.timeParserManager.canParse(string)) {
            return Either.left(this.timeParserManager.delegateTimeParser(string));
        } else {
            return Either.right(makeCannotParseTimeResult(string));
        }
    }

    private CommandResult makeCannotParseTimeResult(String string) {
        return () -> "Cannot parse \"" + string + "\"." ;
    }

    private CommandResult makeEndTimeBeforeStartTimeResult() {
        return () -> "End time must be after start time.";
    }

    private CommandResult makeEndTimeNotSpecified() {
        return () -> "End time must be specified.";
    }
}
