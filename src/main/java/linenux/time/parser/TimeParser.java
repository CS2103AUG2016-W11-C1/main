package linenux.time.parser;

import java.time.LocalDateTime;

/**
 * All time parser types must support interface methods.
 */
//@@author A0135788M
public interface TimeParser {

    /**
     * Checks if the user input corresponds to the format of the respective
     * time parser.
     *
     * @param userInput
     * @return true if format matches and false otherwise.
     */
    public boolean respondTo(String input);

    /**
     * Parses the userInput string to a time instance.
     * Contract: use respondTo to check before calling execute
     * @param userInput
     * @return
     */
    public LocalDateTime parse(String input);
}
