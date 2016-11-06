package linenux.util;

import java.util.ArrayList;

//@@author A0140702X
/**
 * Ternary Search Tree data structure.
 */
public class TernarySearchTree {
    private class Node {
        private char character;
        private boolean lastCharacter;
        private Node left;
        private Node center;
        private Node right;

        public Node(char character) {
            this.character = character;
        }
    }

    private Node root;
    private ArrayList<String> searchResult;

    /**
     * Constructs an {@code TernarySearchTree}.
     */
    public TernarySearchTree() {
        searchResult = new ArrayList<>();
    }

    /**
     * Adds a string to the {@code TernarySearchTree} according to TST rules.
     * @param s A {@code String} to add to the {@code TernarySearchTree}.
     */
    public void addString(String s) {
        if (s == null || s.isEmpty()) {
            return;
        }
        root = addStringHelper(root, 0, s.toLowerCase());
    }

    /**
     * @return A {@code ArrayList} of all strings in {@code TernarySearchTree}.
     */
    public ArrayList<String> getAllStrings() {
        return searchNode(root);
    }

    /**
     * Finds all strings with {@code prefix} in tree.
     * This method operates under the assumption that the {@code prefix} is not null or whitespace.
     * @param prefix A {@code String} representing the prefix.
     * @return An {@code ArrayList} representing a list of strings with the {@code prefix}.
     */
    public ArrayList<String> getAllStringsWithPrefix(String prefix) {
        assert prefix != null;
        assert !prefix.trim().isEmpty();

        String prefixLowerCase = prefix.toLowerCase();

        Node prefixLastNode = getPrefixLastNode(prefixLowerCase);

        if (prefixLastNode == null) {
            return ArrayListUtil.fromSingleton(prefixLowerCase);
        }

        ArrayList<String> prefixList = new ArrayListUtil.ChainableArrayListUtil<>(searchNode(prefixLastNode))
                .map(s -> prefixLowerCase + s)
                .value();
        return prefixList;
    }

    /**
     * Recursive method that adds each letter of a string {@code s} to the tree.
     * @param current The {@code current} node in the recursive call.
     * @param position The {@code position} represents the current character in string {@code s} in recursive call.
     * @param string The {@code s} to add to the {@code TernarySearchTree}.
     * @return A {@code Node} representing the character at the {@code position} of the string {@code s}.
     */
    private Node addStringHelper(Node current, int position, String s) {
        char c = s.charAt(position);

        if (current == null) {
            current = new Node(c);
        }

        if (c < current.character) {
            current.left = addStringHelper(current.left, position, s);
        } else if (c > current.character) {
            current.right = addStringHelper(current.right, position, s);
        } else if (position < s.length() - 1) {
            current.center = addStringHelper(current.center, position + 1, s);
        } else {
            current.lastCharacter = true;
        }
        return current;
    }

    /**
     * Traverses the {@code TernarySearchTree} starting from the {@code current} node.
     * @param current The {@code current} node is the starting node from which traversal begins.
     * @return An {@code ArrayList} representing the suffix of the {@code current} node.
     */
    private ArrayList<String> searchNode(Node current) {
        searchResult.clear();
        searchNodeHelper(current, "");
        return searchResult;
    }

    /**
     * Recursive depth-first search starting from {@code current} node.
     * @param current The {@code current} node is the starting node from which traversal begins.
     * @param s The suffix string.
     */
    private void searchNodeHelper(Node current, String s) {
        if (current != null) {
            if (current.lastCharacter) {
                searchResult.add(s + current.character);
            }
            searchNodeHelper(current.left, s);
            searchNodeHelper(current.center, s + current.character);
            searchNodeHelper(current.right, s);
        }
    }

    /**
     * Searches for the {@code Node} of the last character in the {@code prefix}.
     * @param prefix The prefix string.
     * @return The {@code Node} of the last character in the {@code prefix}.
     */
    private Node getPrefixLastNode(String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return null;
        }
        return getPrefixLastNodeHelper(root, 0, prefix);
    }

    /**
     * Recursive method that searches for the last character of the prefix.
     * @param current The {@code current} node in the recursive call.
     * @param position The {@code position} represents the current character in string {@code prefix} in recursive call.
     * @param prefix The {@code prefix} to search for in the {@code TernarySearchTree}.
     * @return A {@code Node} representing the character at the {@code position} of the string {@code s}.
     */
    private Node getPrefixLastNodeHelper(Node current, int position, String prefix) {
        if (current == null) {
            return null;
        }

        char c = prefix.charAt(position);

        if (c < current.character) {
            return getPrefixLastNodeHelper(current.left, position, prefix);
        } else if (c > current.character) {
            return getPrefixLastNodeHelper(current.right, position, prefix);
        } else if (position < prefix.length() - 1) {
            return getPrefixLastNodeHelper(current.center, position + 1, prefix);
        } else {
            return current.center;
        }
    }
}
