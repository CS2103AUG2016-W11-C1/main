package linenux.util;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

//@@author A0144915A
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

    @Test
    public void testSort() {
        ArrayList<Integer> numbers = ArrayListUtil.fromArray(new Integer[] {3, 2, 4, 1});
        ArrayList<Integer> sortedNumbers = new ArrayListUtil.ChainableArrayListUtil<>(numbers)
                .sort((a, b) -> a - b)
                .value();
        assertEquals(4, sortedNumbers.size());
        assertEquals(1, (int)sortedNumbers.get(0));
        assertEquals(2, (int)sortedNumbers.get(1));
        assertEquals(3, (int)sortedNumbers.get(2));
        assertEquals(4, (int)sortedNumbers.get(3));
    }

    @Test
    public void testSortBy() {
        ArrayList<String> strings = ArrayListUtil.fromArray(new String[] {"apple", "bla", "walala"});
        ArrayList<String> sortedStrings = new ArrayListUtil.ChainableArrayListUtil<>(strings)
                .sortBy(String::length)
                .value();
        assertEquals(3, sortedStrings.size());
        assertEquals("bla", sortedStrings.get(0));
        assertEquals("apple", sortedStrings.get(1));
        assertEquals("walala", sortedStrings.get(2));
    }

    @Test
    public void testFoldr() {
        String output = new ArrayListUtil.ChainableArrayListUtil<>(new String[] {"1", "2", "3"})
                .foldr(String::concat, "");
        assertEquals("123", output);
    }

    @Test
    public void testSpecialFoldr() {
        ArrayList<String> strings = new ArrayListUtil.ChainableArrayListUtil<>(new String[] {"1", "2", "3"})
                .foldr((x, xs) -> {
                    xs.add(x);
                    return xs;
                }, new ArrayList<String>())
                .value();

        assertEquals(3, strings.size());
        assertEquals("3", strings.get(0));
        assertEquals("2", strings.get(1));
        assertEquals("1", strings.get(2));
    }

    @Test
    public void testReverse() {
        ArrayList<String> strings = new ArrayListUtil.ChainableArrayListUtil<>(new String[] {"1", "2", "3"})
                .reverse()
                .value();

        assertEquals(3, strings.size());
        assertEquals("3", strings.get(0));
        assertEquals("2", strings.get(1));
        assertEquals("1", strings.get(2));
    }
}
