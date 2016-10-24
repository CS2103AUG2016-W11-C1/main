package linenux.command.parser;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.result.CommandResult;
import linenux.control.TimeParserManager;
import linenux.model.Reminder;
import linenux.model.Task;
import linenux.util.Either;

/**
 * Parses new details of reminder to be edited.
 */
public class EditrArgumentParser {
    public static String COMMAND_FORMAT;
    public static String CALLOUTS;

    private TimeParserManager timeParserManager;

    public EditrArgumentParser(TimeParserManager timeParserManager, String commandFormat, String callouts) {
        this.timeParserManager = timeParserManager;
        EditArgumentParser.COMMAND_FORMAT = commandFormat;
        EditArgumentParser.CALLOUTS = callouts;
    }

    public Either<Reminder, CommandResult> parse(Task task, Reminder original, String argument) {
        if (argument.trim().isEmpty()) {
            return Either.right(makeNoArgumentsResult());
        }

        Either<LocalDateTime, CommandResult> time = extractTime(original, argument);
        if (time.isRight()) {
            return Either.right(time.getRight());
        }

        Either<String, CommandResult> note = extractNote(original, argument);
        if (note.isRight()) {
            return Either.right(note.getRight());
        }

        if (time.equals(original.getTimeOfReminder()) && note.equals(original.getNote())) {
            return Either.right(makeNoChangeResult());
        }

        LocalDateTime actualTime = time.getLeft();
        String actualNote = note.getLeft();

        assert actualTime != null;
        assert actualNote != null;

        Reminder newReminder = new Reminder(actualNote, actualTime);
        return Either.left(newReminder);
    }

    private Either<LocalDateTime, CommandResult> extractTime(Reminder original, String argument) {
        Matcher matcher = Pattern.compile("(^|.*? )t/(?<time>.*?)(\\s+n/.*)?").matcher(argument);

        if (matcher.matches() && matcher.group("time") != null) {
            return parseDateTime(matcher.group("time").trim());
        } else {
            return Either.left(original.getTimeOfReminder());
        }
    }

    private Either<String, CommandResult> extractNote(Reminder original, String argument) {
        Matcher matcher = Pattern.compile("(^|.*? )n/(?<note>.*?)(\\s+t/.*)?").matcher(argument);

        if (matcher.matches() && matcher.group("note") != null) {
            return Either.left(matcher.group("note").trim());
        } else {
            return Either.left(original.getNote());
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

    private CommandResult makeNoArgumentsResult() {
        return () -> "No changes to be made!";
    }

    private CommandResult makeNoChangeResult() {
        return () -> "No changes to be made!";
    }

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
