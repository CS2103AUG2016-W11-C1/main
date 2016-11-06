package linenux.command.parser;

import static junit.framework.TestCase.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

//@@author A0127694U
public class GenericParserTest {
    private GenericParser parser;

    @Before
    public void setupParser() {
        this.parser = new GenericParser();
    }

    @Test
    public void parseKeywords_noFlags_keywordsReturned() {
        GenericParser.GenericParserResult result = parser.parse("hello world");
        assertEquals("hello world", result.getKeywords());
    }

    @Test
    public void parseKeywords_spacesWithNoFlags_trimmedKeywordsReturned() {
        GenericParser.GenericParserResult result = parser.parse("   hello world    ");
        assertEquals("hello world", result.getKeywords());
    }

    @Test
    public void parseKeywords_withFlags_keywordsReturned() {
        GenericParser.GenericParserResult result = parser.parse("hello world st/12345");
        assertEquals("hello world", result.getKeywords());
    }

    @Test
    public void parseKeywords_spacesWithFlags_trimmedKeywordsReturned() {
        GenericParser.GenericParserResult result = parser.parse("  hello world      st/12345");
        assertEquals("hello world", result.getKeywords());
    }

    @Test
    public void parseFlags_singleFlag_flagReturned() {
        GenericParser.GenericParserResult result = parser.parse("hello world st/12345");
        ArrayList<String> flagValues = result.getArguments("st");
        assertEquals(1, flagValues.size());
        assertEquals("12345", flagValues.get(0));
    }

    @Test
    public void parseFlags_repeatedFlags_flagsReturned() {
        GenericParser.GenericParserResult result = parser.parse("hello world st/12345 st/67890");
        ArrayList<String> flagValues = result.getArguments("st");
        assertEquals(2, flagValues.size());
        assertEquals("12345", flagValues.get(0));
        assertEquals("67890", flagValues.get(1));
    }

    @Test
    public void parseFlags_repeatedButSeparatedFlags_flagsReturned() {
        GenericParser.GenericParserResult result = parser.parse("hello world st/1 et/2 st/3");
        ArrayList<String> flagValues = result.getArguments("st");
        assertEquals(2, flagValues.size());
        assertEquals("1", flagValues.get(0));
        assertEquals("3", flagValues.get(1));
        flagValues = result.getArguments("et");
        assertEquals(1, flagValues.size());
        assertEquals("2", flagValues.get(0));
    }

    @Test
    public void parseFlags_nonWordsFlags_flagsReturned() {
        GenericParser.GenericParserResult result = parser.parse("hello world #/yo #/foo");
        ArrayList<String> flagValues = result.getArguments("#");
        assertEquals(2, flagValues.size());
        assertEquals("yo", flagValues.get(0));
        assertEquals("foo", flagValues.get(1));
    }

    @Test
    public void parseFlags_multiWordsFlagValue_flagsReturned() {
        GenericParser.GenericParserResult result = parser.parse("hello world st/Jan 1 et/Jan 2");
        ArrayList<String> flagValues = result.getArguments("st");
        assertEquals(1, flagValues.size());
        assertEquals("Jan 1", flagValues.get(0));
        flagValues = result.getArguments("et");
        assertEquals(1, flagValues.size());
        assertEquals("Jan 2", flagValues.get(0));
    }

    @Test
    public void parseFlags_emptyFlagValue_flagReturned() {
        GenericParser.GenericParserResult result = parser.parse("hello st/");
        assertEquals("hello", result.getKeywords());
        ArrayList<String> flagValues = result.getArguments("st");
        assertEquals(1, flagValues.size());
        assertEquals("", flagValues.get(0));
    }

    @Test
    public void parseFlags_emptyFlagValueFollowByAnotherFlag_flagsReturned() {
        GenericParser.GenericParserResult result = parser.parse("hello st/  et/12345");
        assertEquals("hello", result.getKeywords());
        ArrayList<String> flagValues = result.getArguments("st");
        assertEquals(1, flagValues.size());
        assertEquals("", flagValues.get(0));
        flagValues = result.getArguments("et");
        assertEquals(1, flagValues.size());
        assertEquals("12345", flagValues.get(0));
    }

    @Test
    public void parseFlags_flagValueContainsSlash_flagReturned() {
        GenericParser.GenericParserResult result = parser.parse("hello st/2016/01/01");
        ArrayList<String> flagValues = result.getArguments("st");
        assertEquals(1, flagValues.size());
        assertEquals("2016/01/01", flagValues.get(0));
    }

    @Test
    public void parseFlags_flagValueWithTrailingSpaces_flagReturned() {
        GenericParser.GenericParserResult result = parser.parse("hello st/12345       ");
        ArrayList<String> flagValue = result.getArguments("st");
        assertEquals(1, flagValue.size());
        assertEquals("12345", flagValue.get(0));
    }

    @Test
    public void parseFlags_noKeywords_flagReturned() {
        GenericParser.GenericParserResult result = parser.parse("st/12345");
        assertEquals("", result.getKeywords());
        ArrayList<String> flagValues = result.getArguments("st");
        assertEquals(1, flagValues.size());
        assertEquals("12345", flagValues.get(0));
    }
}
