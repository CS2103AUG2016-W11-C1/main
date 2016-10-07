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
     * Assigns the appropriate time parser to the user input.
     */
    public LocalDateTime delegateTimeParser(String userInput) {
        for (TimeParser parser: parserList) {
            if (parser.respondTo(userInput)) {
                return parser.execute(userInput);
            }
        }
        return null;
    }
}
