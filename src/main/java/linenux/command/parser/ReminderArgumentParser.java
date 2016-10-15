package linenux.command.parser;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.result.CommandResult;
import linenux.control.TimeParserManager;
import linenux.model.Reminder;
import linenux.util.Either;

/**
 * Created by yihangho on 10/8/16.
 */
public class ReminderArgumentParser {
    public static final String ARGUMENT_FORMAT = "KEYWORDS... t/TIME [n/NOTE]";

    private TimeParserManager timeParserManager;

    public ReminderArgumentParser(TimeParserManager timeParserManager) {
        this.timeParserManager = timeParserManager;
    }

    public Either<Reminder, CommandResult> parse(String argument) {
        Either<LocalDateTime, CommandResult> time = extractTime(argument);
        if (time.isRight()) {
            return Either.right(time.getRight());
        }

        Either<String, CommandResult> note = extractNote(argument);
        if (note.isRight()) {
            return Either.right(note.getRight());
        }

        LocalDateTime actualTime = time.getLeft();
        String actualNote = note.getLeft();

        Reminder reminder = new Reminder(actualNote, actualTime);

        return Either.left(reminder);
    }

    private Either<LocalDateTime, CommandResult> extractTime(String argument) {
        Matcher matcher = Pattern.compile("(^|.*? )t/(?<time>.*?)(\\s+n/.*)?").matcher(argument);

        if (matcher.matches() && matcher.group("time") != null) {
            return parseDateTime(matcher.group("time").trim());
        } else {
            return Either.right(makeWithoutDateResult());
        }
    }

    private Either<String, CommandResult> extractNote(String argument) {
        Matcher matcher = Pattern.compile("(^|.*? )n/(?<note>.*?)(\\s+t/.*)?").matcher(argument);

        if (matcher.matches() && matcher.group("note") != null) {
            return Either.left(matcher.group("note").trim());
        } else {
            return Either.left(null);
        }
    }

    private Either<LocalDateTime, CommandResult> parseDateTime(String string) {
        if (this.timeParserManager.canParse(string)) {
            return Either.left(this.timeParserManager.delegateTimeParser(string));
        } else {
            return Either.right(makeInvalidDateTimeResult(string));
        }
    }

    private CommandResult makeInvalidDateTimeResult(String dateTime) {
        return () -> "Cannot parse \"" + dateTime + "\".";
    }

    private CommandResult makeWithoutDateResult() {
        return () -> "Cannot create reminder without date.";
    }
}
