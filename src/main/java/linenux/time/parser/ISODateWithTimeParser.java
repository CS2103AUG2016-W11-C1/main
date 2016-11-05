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
        return LocalDateTime.parse(input.toUpperCase(), formatter);
    }
}
