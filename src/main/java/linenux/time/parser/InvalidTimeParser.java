package linenux.time.parser;

import java.time.LocalDateTime;

/**
 * Act as a fail-safe for invalid or unrecognized time input formats.
 */
public class InvalidTimeParser implements TimeParser {
    
    /**
     * @return true for all user inputs.
     */
    @Override
    public boolean respondTo(String userInput) {
        return true;
    }

    @Override
    public LocalDateTime parse(String userInput) {
        return null;
    }

}
