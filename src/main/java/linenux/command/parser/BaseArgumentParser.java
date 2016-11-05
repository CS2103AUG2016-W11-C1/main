package linenux.command.parser;

import linenux.command.result.CommandResult;
import linenux.control.TimeParserManager;
import linenux.util.Either;

import java.time.LocalDateTime;

/**
 * Provides common methods for various parsers.
 */
public abstract class BaseArgumentParser {
    protected TimeParserManager timeParserManager;

    /**
     * Attempts to parse a date time string.
     * @param string The {@code String} to parse.
     * @return An {@code Either}. Its left slot is a {@code LocalDateTime} if {@code string} can be parsed. Otherwise,
     * its right slot contains a {@code CommandResult} describing the error.
     */
    public Either<LocalDateTime, CommandResult> parseDateTime(String string) {
        if (this.timeParserManager.canParse(string)) {
            return Either.left(this.timeParserManager.delegateTimeParser(string));
        } else {
            return Either.right(makeInvalidDateTimeResult(string));
        }
    }

    public Either<LocalDateTime, CommandResult> parseCancellableDateTime(String string) {
        if (this.timeParserManager.canParse(string)) {
            return Either.left(this.timeParserManager.delegateTimeParser(string));
        } else if (string.matches("\\s*-\\s*")) {
            return Either.left(null);
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
}
