package linenux.command.parser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.result.CommandResult;
import linenux.control.TimeParserManager;
import linenux.model.Reminder;
import linenux.model.Task;
import linenux.util.ArrayListUtil;
import linenux.util.Either;

/**
 * Parses new details of reminder to be edited.
 */
public class EditReminderArgumentParser {
    public static String COMMAND_FORMAT;
    public static String CALLOUTS;

    private TimeParserManager timeParserManager;
    private GenericParser genericParser;
    private GenericParser.GenericParserResult parseResult;

    //@@author A0140702X
    public EditReminderArgumentParser(TimeParserManager timeParserManager, String commandFormat, String callouts) {
        this.timeParserManager = timeParserManager;
        this.genericParser = new GenericParser();
        EditArgumentParser.COMMAND_FORMAT = commandFormat;
        EditArgumentParser.CALLOUTS = callouts;
    }

    //@@author A0144915A
    public Either<Reminder, CommandResult> parse(Reminder original, String argument) {
        this.parseResult = this.genericParser.parse(argument);

        return Either.<Reminder, CommandResult>left(original)
                .bind(this::ensureNeedsEdit)
                .bind(this::extractTime)
                .bind(this::extractNote);
    }

    private Either<Reminder, CommandResult> extractTime(Reminder original) {
        if (this.parseResult.getArguments("t").size() > 0) {
            return parseDateTime(this.parseResult.getArguments("t").get(0))
                    .bind(t -> Either.left(original.setTimeOfReminder(t)));
        } else {
            return Either.left(original);
        }
    }

    private Either<Reminder, CommandResult> extractNote(Reminder original) {
        if (this.parseResult.getArguments("n").size() > 0) {
            return Either.left(original.setNote(this.parseResult.getArguments("n").get(0)));
        } else {
            return Either.left(original);
        }
    }

    private Either<Reminder, CommandResult> ensureNeedsEdit(Reminder reminder) {
        boolean needsEdit = new ArrayListUtil.ChainableArrayListUtil<>(new String[] {"n", "t"})
                .map(this.parseResult::getArguments)
                .map(ArrayList::size)
                .map(s -> s > 0)
                .foldr(Boolean::logicalOr, false);

        if (needsEdit) {
            return Either.left(reminder);
        } else {
            return Either.right(makeNoArgumentsResult());
        }
    }

    //@@author A0140702X
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

    private CommandResult makeInvalidDateTimeResult(String dateTime) {
        return () -> "Cannot parse \"" + dateTime + "\".";
    }
}
