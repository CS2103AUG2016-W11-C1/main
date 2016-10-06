package linenux.util;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by yihangho on 10/5/16.
 */
public class ChainableArrayListUtil {
    @Test
    public void testConstructFromArray() {
        ArrayList<String> str = new ArrayListUtil.ChainableArrayListUtil<String>(new String[] {"1", "2", "3"}).value();
        assertEquals(3, str.size());
        assertEquals("1", str.get(0));
        assertEquals("2", str.get(1));
        assertEquals("3", str.get(2));
    }

    @Test
    public void testConstructFromArrayList() {
        ArrayList<String> list = ArrayListUtil.fromArray(new String[] {"1", "2", "3"});
        ArrayList<String> output = new ArrayListUtil.ChainableArrayListUtil<String>(list).value();
        assertEquals(list, output);
    }

    @Test
    public void testMap() {
        ArrayList<String> list = new ArrayListUtil.ChainableArrayListUtil<String>(new String[] {"1", "2", "3"})
                .map(x -> x + " bla")
                .value();
        assertEquals("1 bla", list.get(0));
        assertEquals("2 bla", list.get(1));
        assertEquals("3 bla", list.get(2));
    }

    @Test
    public void testFilter() {
        ArrayList<String> list = new ArrayListUtil.ChainableArrayListUtil<String>(new String[] {"1", "2", "3"})
                .filter(x -> x.equals("2"))
                .value();
        assertEquals(1, list.size());
        assertEquals("2", list.get(0));
    }
}
