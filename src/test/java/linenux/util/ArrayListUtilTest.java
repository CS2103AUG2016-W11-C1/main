package linenux.util;

import static junit.framework.TestCase.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

//@@author A0144915A
public class ArrayListUtilTest {
    ArrayList<String> list;
    @Before
    public void setupList() {
        this.list = ArrayListUtil.fromArray(new String[] {"1", "2", "3"});
    }

    @Test
    public void fromArray_arrayAsInitialValue_returnedArrayListMatchesInputArray() {
        assertEquals("1", this.list.get(0));
        assertEquals("2", this.list.get(1));
        assertEquals("3", this.list.get(2));
    }

    @Test
    public void map_modifyValues_expectedValuesInReturnedList() {
        ArrayList<String> mapped = ArrayListUtil.map(x -> x + " bla", this.list);

        assertEquals("1 bla", mapped.get(0));
        assertEquals("2 bla", mapped.get(1));
        assertEquals("3 bla", mapped.get(2));
    }

    @Test
    public void mapWithIndex_modifyValuesAndAttachIndex_expectedIndexedValuesInReturnedList() {
        ArrayList<Integer> numbers = ArrayListUtil.fromArray(new Integer[] {0, 0, 0});
        ArrayList<Integer> mapped = ArrayListUtil.mapWithIndex((x, i) -> x + i, numbers);

        assertEquals(3, mapped.size());
        assertEquals(0, (int)mapped.get(0));
        assertEquals(1, (int)mapped.get(1));
        assertEquals(2, (int)mapped.get(2));
    }

    @Test
    public void filter_filterListByValue_filteredListReturned() {
        ArrayList<String> filtered = ArrayListUtil.filter(x -> x.equals("2"), this.list);

        assertEquals(1, filtered.size());
        assertEquals("2", filtered.get(0));
    }

    @Test
    public void foldr_compressListToSingleString_concatenatedListValuesReturnedAsString() {
        String output = ArrayListUtil.foldr(String::concat, "", this.list);
        assertEquals("123", output);
    }

    @Test
    public void reverse_reverseOrderOfListElements_reversedListReturned() {
        ArrayList<String> strings = ArrayListUtil.reverse(this.list);
        assertEquals("3", strings.get(0));
        assertEquals("2", strings.get(1));
        assertEquals("1", strings.get(2));
    }

    @Test
    public void unique_repeatedValuesInList_listWithUniqueValuesReturned() {
        ArrayList<Integer> numbers = ArrayListUtil.fromArray(new Integer[]{1, 2, 2, 1, 3, 1, 3, 2});
        ArrayList<Integer> uniqueNumbers = ArrayListUtil.unique(numbers);
        assertEquals(3, uniqueNumbers.size());
    }

    public void fromSingleton_listCreatedFromSingleValue() {
        ArrayList<String> strings = ArrayListUtil.fromSingleton("hello");
        assertEquals(1, strings.size());
        assertEquals("hello", strings.get(0));
    }
}
