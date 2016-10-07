package linenux.control;

import linenux.time.parser.TimeParser;

import java.util.ArrayList;
import java.time.LocalDateTime;

/**
 * Assigns time parsers based on user input.
 */
public class TimeParserManager {
    private ArrayList<TimeParser> parserList;

    public TimeParserManager(TimeParser... parsers) {
        parserList = new ArrayList<TimeParser>();
        for (TimeParser parser: parsers){
            parserList.add(parser);
        }
    }

    /**
     * Check if the manager can parse some string
     * @param userInput The string to check
     * @return {@code true} if and only if at least one of the parsers can parse {@code userInput}
     */
    public boolean canParse(String userInput) {
        for (TimeParser parser: parserList) {
            if (parser.respondTo(userInput)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Assigns the appropriate time parser to the user input.
     */
    public LocalDateTime delegateTimeParser(String userInput) {
        for (TimeParser parser: parserList) {
            if (parser.respondTo(userInput)) {
                return parser.parse(userInput);
            }
        }
        return null;
    }
}
