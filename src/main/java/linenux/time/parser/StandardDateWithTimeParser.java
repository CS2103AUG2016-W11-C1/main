package linenux.time.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;

/**
 * Parse date and time in the form of "16 October 2016 5.00pm"
 */
//@@author A0135788M
public class StandardDateWithTimeParser implements TimeParser {
    DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive()
                                        .appendPattern("dd MMM yyyy h.mma").toFormatter();

    @Override
    public boolean respondTo(String input) {
        try {
            parse(input);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    @Override
    public LocalDateTime parse(String input) {
        return LocalDateTime.parse(input, formatter);
    }

}
