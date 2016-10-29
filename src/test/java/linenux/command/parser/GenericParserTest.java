package linenux.command.parser;

import linenux.util.ArrayListUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

//@@author A0144915A
public class GenericParserTest {
    private GenericParser parser;

    @Before
    public void setupParser() {
        this.parser = new GenericParser();
    }

    @Test
    public void testKeywordsWithoutFlags() {
        GenericParser.GenericParserResult result = parser.parse("hello world");
        assertEquals("hello world", result.getKeywords());
    }

    @Test
    public void testTrimKeywordsWithoutFlags() {
        GenericParser.GenericParserResult result = parser.parse("   hello world    ");
        assertEquals("hello world", result.getKeywords());
    }

    @Test
    public void testKeywordsWithFlags() {
        GenericParser.GenericParserResult result = parser.parse("hello world st/12345");
        assertEquals("hello world", result.getKeywords());
    }

    @Test
    public void testTrimKeywordsWithFlags() {
        GenericParser.GenericParserResult result = parser.parse("  hello world      st/12345");
        assertEquals("hello world", result.getKeywords());
    }

    @Test
    public void testExtractFlags() {
        GenericParser.GenericParserResult result = parser.parse("hello world st/12345");
        ArrayList<String> flagValues = result.getArguments("st");
        assertEquals(1, flagValues.size());
        assertEquals("12345", flagValues.get(0));
    }

    @Test
    public void testExtractRepeatedFlags() {
        GenericParser.GenericParserResult result = parser.parse("hello world st/12345 st/67890");
        ArrayList<String> flagValues = result.getArguments("st");
        assertEquals(2, flagValues.size());
        assertEquals("12345", flagValues.get(0));
        assertEquals("67890", flagValues.get(1));
    }

    @Test
    public void testRepeatedButSeparatedFlags() {
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
    public void testNonWordFlags() {
        GenericParser.GenericParserResult result = parser.parse("hello world #/yo #/foo");
        ArrayList<String> flagValues = result.getArguments("#");
        assertEquals(2, flagValues.size());
        assertEquals("yo", flagValues.get(0));
        assertEquals("foo", flagValues.get(1));
    }

    @Test
    public void testMultiwordsFlags() {
        GenericParser.GenericParserResult result = parser.parse("hello world st/Jan 1 et/Jan 2");
        ArrayList<String> flagValues = result.getArguments("st");
        assertEquals(1, flagValues.size());
        assertEquals("Jan 1", flagValues.get(0));
        flagValues = result.getArguments("et");
        assertEquals(1, flagValues.size());
        assertEquals("Jan 2", flagValues.get(0));
    }

    @Test
    public void testEmptyFlags() {
        GenericParser.GenericParserResult result = parser.parse("hello st/");
        assertEquals("hello", result.getKeywords());
        ArrayList<String> flagValues = result.getArguments("st");
        assertEquals(1, flagValues.size());
        assertEquals("", flagValues.get(0));
    }

    @Test
    public void testEmptyFlagFollowBySomething() {
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
    public void testFlagValueContainsSlash() {
        GenericParser.GenericParserResult result = parser.parse("hello st/2016/01/01");
        ArrayList<String> flagValues = result.getArguments("st");
        assertEquals(1, flagValues.size());
        assertEquals("2016/01/01", flagValues.get(0));
    }

    @Test
    public void testLastFlagValueWithTrailingSpaces() {
        GenericParser.GenericParserResult result = parser.parse("hello st/12345       ");
        ArrayList<String> flagValue = result.getArguments("st");
        assertEquals(1, flagValue.size());
        assertEquals("12345", flagValue.get(0));
    }

    @Test
    public void testEmptyKeywords() {
        GenericParser.GenericParserResult result = parser.parse("st/12345");
        assertEquals("", result.getKeywords());
        ArrayList<String> flagValues = result.getArguments("st");
        assertEquals(1, flagValues.size());
        assertEquals("12345", flagValues.get(0));
    }
}
