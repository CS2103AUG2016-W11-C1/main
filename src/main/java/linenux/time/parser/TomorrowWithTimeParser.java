package linenux.time.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//@@author A0135788M
/**
 * Parse date and time in the form of "tomorrow 5.00pm"
 */
public class TomorrowWithTimeParser implements TimeParser {
    private static final String TOMORROW_TIME_PATTERN = "(?i)^tomorrow (1[012]|[1-9]).[0-5][0-9](\\s)?(am|pm)";

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
        return input.matches(TOMORROW_TIME_PATTERN);
    }

    /**
     * Parses the userInput string to a time instance. This method assumes that {@code respondTo} returns {@code true}.
     * @param input The input to parse.
     * @return The {@code LocalDateTime}, which is the result of parsing {@code input}.
     */
    @Override
    public LocalDateTime parse(String input) {
        assert input.matches(TOMORROW_TIME_PATTERN);

        String tomorrowDate = LocalDate.now().plusDays(1).toString();
        String time = input.split("\\s+")[1];
        String tomorrowDateTime = tomorrowDate + " " + time;

        return LocalDateTime.parse(tomorrowDateTime.toUpperCase(), formatter);
    }

}
