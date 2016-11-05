package linenux.time.parser;

import java.time.LocalDateTime;

//@@author A0135788M
/**
 * All time parser types must support interface methods.
 */
public interface TimeParser {

    /**
     * Checks if the user input corresponds to the format of the respective
     * time parser.
     *
     * @param input
     * @return true if format matches and false otherwise.
     */
    public boolean respondTo(String input);

    /**
     * Parses the userInput string to a time instance.
     * Contract: use respondTo to check before calling execute
     * @param input
     * @return
     */
    public LocalDateTime parse(String input);
}
