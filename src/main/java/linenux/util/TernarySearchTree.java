package linenux.util;

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
            this.lastCharacter = lastCharacter;
        }
    }

    private Node root;

    public TernarySearchTree() {
    }

    public void addString(String s) {
        if (s == null || s.isEmpty()) {
            return;
        }
        addStringHelper(s.toLowerCase(), 0, root);
    }

    private void addStringHelper(String s, int position, Node current) {
        if (current == null) {
            current = new Node(s.charAt(position));
        }

        if (s.charAt(position) < current.character) {
            addStringHelper(s, position, current.left);
        } else if (s.charAt(position) > current.character) {
            addStringHelper(s, position, current.right);
        } else {
            if (position + 1 == s.length()) {
                current.lastCharacter = true;
            } else {
                addStringHelper(s, position + 1, current.center);
            }
        }
    }

}
