package linenux.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for Ternary Search Tree
 */
//@@author A0135788M
public class TernarySearchTreeTest {
    private TernarySearchTree tree;

    @Before
    public void setUpTree() {
        tree = new TernarySearchTree();
    }

    private void setUpData() {
        tree.addString("cats");
        tree.addString("bats");
        tree.addString("dog");
        tree.addString("crab");
        tree.addString("crow");
        tree.addString("crap");
        tree.addString("crabby");
    }

    /**
     * Test that getAllString() is never null.
     */
    @Test
    public void testGetAllStringsNotNull() {
        assertNotNull(tree.getAllStrings());
    }

    /**
     * Test that getAllStringWithPrefix() for empty string and null is empty.
     */
    @Test
    public void testInvalidString() {
        assertTrue(tree.getAllStringsWithPrefix("").isEmpty());
        assertTrue(tree.getAllStringsWithPrefix(null).isEmpty());
    }

    /**
     * Test that getAllStringWithPrefix() for invalid prefix is empty.
     */
    @Test
    public void testInvalidPrefix() {
        setUpData();
        assertTrue(tree.getAllStringsWithPrefix("z").isEmpty());
    }
}