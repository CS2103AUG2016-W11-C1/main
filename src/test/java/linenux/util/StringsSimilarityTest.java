package linenux.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by yihangho on 10/8/16.
 */
public class StringsSimilarityTest {
    @Test
    public void levenshteinDistanceTest() {
        assertEquals(5, StringsSimilarity.compute("kitten", "sitting"));
        assertEquals(0, StringsSimilarity.compute("hello", "hello"));
    }
}
