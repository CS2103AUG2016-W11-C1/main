package linenux.command.parser;

import java.util.ArrayList;

import linenux.command.result.CommandResult;
import linenux.control.TimeParserManager;
import linenux.model.Reminder;
import linenux.util.ArrayListUtil;
import linenux.util.Either;

//@@author A0140702X
/**
 * Parses new details of reminder to be edited.
 */
public class EditReminderArgumentParser extends BaseArgumentParser {
    private GenericParser.GenericParserResult parseResult;

    /**
     * The public constructor for {@code EditReminderArgumentParser}.
     * @param timeParserManager A {@code TimeParserManager} used to parse any date time string.
     */
    public EditReminderArgumentParser(TimeParserManager timeParserManager) {
        this.timeParserManager = timeParserManager;
    }

    /**
     * Attempts to parse an argument given by the user.
     * @param original A {@code Reminder}, the original {@code Reminder} object.
     * @param result A {@code GenericParserResult}, which is the output {@code GenericParser}.
     * @return An {@code Either}. Its left slot is a {@code Reminder}, updated from {@code original} based on
     * {@code argument}, if {@code argument} represents a valid instruction to edit a {@code Reminder}. Otherwise, its
     * right slot contains a {@code CommandResult} indicating the failure.
     */
    public Either<Reminder, CommandResult> parse(Reminder original, GenericParser.GenericParserResult result) {
        this.parseResult = result;

        return Either.<Reminder, CommandResult>left(original)
                .bind(this::ensureNeedsEdit)
                .bind(this::extractTime)
                .bind(this::extractNote);
    }

    /**
     * Attempts to extract the reminder time from the user argument.
     * @param original A {@code Reminder}, which is the original {@code Reminder} object.
     * @return An {@code Either}. If the user argument contains a valid date time string, the left slot will be
     * {@code original} with its time updated. Otherwise, its right slot is a {@code CommandResult} indicating the
     * failure.
     */
    private Either<Reminder, CommandResult> extractTime(Reminder original) {
        if (this.parseResult.getArguments("t").size() > 0) {
            return parseCancellableDateTime(this.parseResult.getArguments("t").get(0))
                    .bind(t -> Either.left(original.setTimeOfReminder(t)));
        } else {
            return Either.left(original);
        }
    }

    /**
     * Attempts to extract the reminder note from the user argument.
     * @param original A {@code Reminder}, which is the original {@code Reminder} object.
     * @return An {@code Either}. If the user argument contains a valid note, the left slot will be {@code original}
     * with its note updated. Otherwise, its right slot is a {@code CommandResult} indicating the failure.
     */
    private Either<Reminder, CommandResult> extractNote(Reminder original) {
        if (this.parseResult.getArguments("n").size() > 0) {
            return Either.left(original.setNote(this.parseResult.getArguments("n").get(0)));
        } else {
            return Either.left(original);
        }
    }

    /**
     * Ensures that the user argument contains some instructions to edit a reminder.
     * @param reminder The {@code Reminder} to edit.
     * @return An {@code Either}. If the user argument contains some edit instructions, its left slot is
     * {@code reminder}. Otherwise, its right slot is a {@code CommandResult}.
     */
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

    /**
     * @return A {@code CommandResult} indicating that there is no instructions for change.
     */
    private CommandResult makeNoArgumentsResult() {
        return () -> "No changes to be made!";
    }
}
