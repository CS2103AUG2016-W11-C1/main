package linenux.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Parses user input. 
 */
public class Parser {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    public static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    public static final Pattern ADD_COMMAND_FORMAT = Pattern.compile("(?<taskName>[^/]+) \bby\b (?<deadline>[^/]+)");

    
    public Parser() {}
    
    public Command parseCommand(String userInput) {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            return null;
        }
        
        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        
        switch (commandWord) {
            case AddCommand.COMMAND_WORD:
                return prepareAdd(arguments);
            default:
                return null;
        }
    }
    
    public Command prepareAdd(String arguments) {
        final Matcher matcher = ADD_COMMAND_FORMAT.matcher(arguments);
        if (!matcher.matches()) {
            return null;
        }
        
        final String taskName = matcher.group("taskName");
        final LocalDateTime deadline = parseTime(matcher.group("deadline"));
        
        return new AddCommand(taskName, deadline);
    }
    
    public LocalDateTime parseTime(String time) {
        try {
            return LocalDateTime.parse(time, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
