package linenux.command.parser;

import java.time.LocalDateTime;

import linenux.command.result.CommandResult;
import linenux.control.TimeParserManager;
import linenux.model.Reminder;
import linenux.util.Either;

/**
 * Created by yihangho on 10/8/16.
 */
public class ReminderArgumentParser {
    public static String COMMAND_FORMAT;
    public static String CALLOUTS;

    private TimeParserManager timeParserManager;
    private GenericParser genericParser;
    private GenericParser.GenericParserResult parseResult;

    //@@author A0135788M
    public ReminderArgumentParser(TimeParserManager timeParserManager, String commandFormat, String callouts) {
        this.timeParserManager = timeParserManager;
        this.genericParser = new GenericParser();
        ReminderArgumentParser.COMMAND_FORMAT = commandFormat;
        ReminderArgumentParser.CALLOUTS = callouts;
    }

    //@@author A0144915A
    public Either<Reminder, CommandResult> parse(String argument) {
        this.parseResult = this.genericParser.parse(argument);

        return Either.<Reminder, CommandResult>left(new Reminder())
                .bind(this::extractTime)
                .bind(this::extractNote);
    }

    private Either<Reminder, CommandResult> extractTime(Reminder reminder) {
        if (this.parseResult.getArguments("t").size() > 0) {
            return parseDateTime(this.parseResult.getArguments("t").get(0))
                    .bind(t -> Either.left(reminder.setTimeOfReminder(t)));
        } else {
            return Either.right(makeWithoutDateResult());
        }
    }

    private Either<Reminder, CommandResult> extractNote(Reminder reminder) {
        if (this.parseResult.getArguments("n").size() > 0) {
            return Either.left(reminder.setNote(this.parseResult.getArguments("n").get(0)));
        } else {
            return Either.right(makeWithoutNoteResult());
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

    private CommandResult makeWithoutNoteResult() {
        return () -> "Cannot create reminder without note.";
    }
}
