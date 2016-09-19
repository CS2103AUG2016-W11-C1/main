package linenux.parser.time.formats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents an acceptable time format.
 */

public abstract class Format {
    public abstract LocalDateTime parseTime(String userInput);
}
