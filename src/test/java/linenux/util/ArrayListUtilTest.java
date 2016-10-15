package linenux.util;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by yihangho on 10/5/16.
 */
public class ArrayListUtilTest {
    ArrayList<String> list;
    @Before
    public void setupList() {
        this.list = ArrayListUtil.fromArray(new String[] {"1", "2", "3"});
    }

    @Test
    public void testFromArray() {
        assertEquals("1", this.list.get(0));
        assertEquals("2", this.list.get(1));
        assertEquals("3", this.list.get(2));
    }

    @Test
    public void testMap() {
        ArrayList<String> mapped = ArrayListUtil.map(x -> x + " bla", this.list);

        assertEquals("1 bla", mapped.get(0));
        assertEquals("2 bla", mapped.get(1));
        assertEquals("3 bla", mapped.get(2));
    }

    @Test
    public void testFilter() {
        ArrayList<String> filtered = ArrayListUtil.filter(x -> x.equals("2"), this.list);

        assertEquals(1, filtered.size());
        assertEquals("2", filtered.get(0));
    }

    @Test
    public void testReduce() {
        String output = ArrayListUtil.foldr(String::concat, "", this.list);
        assertEquals("123", output);
    }

    @Test
    public void testReverse() {
        ArrayList<String> strings = ArrayListUtil.reverse(this.list);
        assertEquals("3", strings.get(0));
        assertEquals("2", strings.get(1));
        assertEquals("1", strings.get(2));
    }

    public void testFromSingleton() {
        ArrayList<String> strings = ArrayListUtil.fromSingleton("hello");
        assertEquals(1, strings.size());
        assertEquals("hello", strings.get(0));
    }
}
