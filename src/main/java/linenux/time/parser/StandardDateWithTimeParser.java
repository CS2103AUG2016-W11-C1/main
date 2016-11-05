package linenux.time.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;

//@@author A0135788M
/**
 * Parse date and time in the form of "16 October 2016 5.00pm"
 */
public class StandardDateWithTimeParser implements TimeParser {
    DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive()
                                        .appendPattern("dd MMM yyyy h.mma").toFormatter();

    /**
     * Checks if the user input corresponds to the format of the respective
     * time parser.
     *
     * @param input
     * @return true if format matches and false otherwise.
     */
    @Override
    public boolean respondTo(String input) {
        try {
            parse(input);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Parses the userInput string to a time instance. This method assumes that {@code respondTo} returns {@code true}.
     * @param input The input to parse.
     * @return The {@code LocalDateTime}, which is the result of parsing {@code input}.
     */
    @Override
    public LocalDateTime parse(String input) {
        return LocalDateTime.parse(input, formatter);
    }

}
