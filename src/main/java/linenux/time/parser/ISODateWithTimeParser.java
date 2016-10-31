package linenux.time.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

//@@author A0144915A
/**
 * Parse date and time in the form of "2016-10-01 2:00pm"
 */
public class ISODateWithTimeParser implements TimeParser {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d h.mma");

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
        return LocalDateTime.parse(input.toUpperCase(), formatter);
    }
}
