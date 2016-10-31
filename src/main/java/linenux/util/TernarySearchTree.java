package linenux.util;

import java.util.ArrayList;

/**
 * Ternary Search Tree data structure.
 */
//@@author A0135788M
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

    public TernarySearchTree() {
        searchResult = new ArrayList<>();
    }

    public void addString(String s) {
        if (s == null || s.isEmpty()) {
            return;
        }
        root = addStringHelper(root, 0, s.toLowerCase());
    }

    public ArrayList<String> getAllStrings() {
        return searchNode(root);
    }

    public ArrayList<String> getAllStringsWithPrefix(String prefix) {
        Node prefixLastNode = getPrefixLastNode(prefix);

        if (prefixLastNode == null) {
            return new ArrayList<String>();
        }

        ArrayList<String> prefixList = new ArrayListUtil.ChainableArrayListUtil<>(searchNode(prefixLastNode))
                .map(s -> prefix + s)
                .value();
        return prefixList;
    }

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

    private ArrayList<String> searchNode(Node current) {
        searchResult.clear();
        searchNodeHelper(current, "");
        return searchResult;
    }

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

    private Node getPrefixLastNode(String prefix) {
        if (prefix.isEmpty() || prefix == null) {
            return null;
        }
        return getPrefixLastNodeHelper(root, 0, prefix);
    }

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

    public Node getRoot() {
        return root;
    }

    public static void main(String[] args) {
        TernarySearchTree tree = new TernarySearchTree();
        tree.addString("cats");
        tree.addString("bats");
        tree.addString("dog");
        tree.addString("crab");
        tree.addString("crow");
        tree.addString("crap");
        tree.addString("crabby");

        ArrayList<String> result = tree.getAllStrings();

        for (String s : result) {
            System.out.println(s);
        }

        System.out.println();

        ArrayList<String> prefixResult = tree.getAllStringsWithPrefix("crab");

        for (String s : prefixResult) {
            System.out.println(s);
        }
    }

}
