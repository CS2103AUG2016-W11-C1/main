package linenux.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

//@@author A0144915A
public class StringsSimilarityTest {
    @Test
    public void compute_twoStrings_levenshteinDistanceReturned() {
        assertEquals(5, StringsSimilarity.compute("kitten", "sitting"));
        assertEquals(0, StringsSimilarity.compute("hello", "hello"));
    }
}
