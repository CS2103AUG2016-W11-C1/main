package linenux.command.parser;

import linenux.command.Command;
import linenux.command.result.CommandResult;
import linenux.control.TimeParserManager;
import linenux.util.Either;
import linenux.util.TimeInterval;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yihangho on 10/15/16.
 */
public class FreeTimeArgumentParser {
    private TimeParserManager timeParserManager;
    private Clock clock;

    public FreeTimeArgumentParser(TimeParserManager timeParserManager, Clock clock) {
        this.timeParserManager = timeParserManager;
        this.clock = clock;
    }

    public Either<TimeInterval, CommandResult> parse(String argument) {
        Either<LocalDateTime, CommandResult> startTime = parseStartTime(argument);
        if (startTime.isRight()) {
            return Either.right(startTime.getRight());
        }

        Either<LocalDateTime, CommandResult> endTime = parseEndTime(argument);
        if (endTime.isRight()) {
            return Either.right(endTime.getRight());
        }

        if (startTime.getLeft().compareTo(endTime.getLeft()) > 0) {
            return Either.right(makeEndTimeBeforeStartTimeResult());
        }

        return Either.left(new TimeInterval(startTime.getLeft(), endTime.getLeft()));
    }

    private Either<LocalDateTime, CommandResult> parseStartTime(String argument) {
        Matcher matcher = Pattern.compile("(^|.*? )st/(?<startTime>.*?)(\\s+et/.*)?$").matcher(argument);

        if (matcher.matches() && matcher.group("startTime") != null) {
            return parseDateTime(matcher.group("startTime"));
        } else {
            return Either.left(LocalDateTime.now(this.clock));
        }
    }

    private Either<LocalDateTime, CommandResult> parseEndTime(String argument) {
        Matcher matcher = Pattern.compile("(^|.*? )et/(?<endTime>.*?)(\\s+st/.*)?$").matcher(argument);

        if (matcher.matches() && matcher.group("endTime") != null) {
            return parseDateTime(matcher.group("endTime").trim());
        } else {
            return Either.right(makeEndTimeNotSpecified());
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
