package linenux.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.parser.commands.*;

/**
 * Separates user input into the command word and its arguments.
 */

public class Parser {
   
    public static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
  
    public Parser() {}
}
