package linenux.control;

import java.util.ArrayList;
import java.time.LocalDateTime;

public class TimeParserManager {
    private ArrayList<TimeParser> parserList;
    
    public TimeParserManager(TimeParser... parsers) {
        parserList = new ArrayList<TimeParser>();
        for (TimeParser parser: parsers){
            parserList.add(parser);
        }
    }
    
    public LocalDateTime delegateTimeParser(String userInput) {
        for (TimeParser parser: parserList) {
            if (parser.respondTo(userInput)) {
                return parser.execute(userInput);
            }
        }
        return null;
    }
}
