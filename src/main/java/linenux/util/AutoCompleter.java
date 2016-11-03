package linenux.util;

import java.util.ArrayList;

import linenux.command.Command;

/**
 * Stateful class that autocompletes.
 */
//@@author A0135788M
public class AutoCompleter {
    private TernarySearchTree tree;
    private ArrayList<Command> commandList;
    private ArrayList<String> searchResult;
    private int searchIndex;

    public AutoCompleter() {
        this.tree = new TernarySearchTree();
        this.searchResult = new ArrayList<>();
        this.searchIndex = -1;
    }

    /**
     * Adds default command string and their aliases to the tree.
     * @param commandList
     */
    public AutoCompleter(ArrayList<Command> commandList) {
        this();
        this.commandList = commandList;
        buildTree();
    }


    public boolean hasNoSearchResult() {
        return searchResult.isEmpty();
    }

    /**
     * Find all strings with prefix.
     * @param prefix
     */
    public void findPrefix(String prefix) {
        int indexOfLastSpace = prefix.lastIndexOf(' ');

        //if no ' ' found
        if (indexOfLastSpace == -1) {
            this.searchResult = tree.getAllStringsWithPrefix(prefix);
        }

        String stringAfterLastSpace = prefix.substring(indexOfLastSpace + 1);

        ArrayList<String> searchResult = tree.getAllStringsWithPrefix(stringAfterLastSpace);
        ArrayList<String> finalResult = new ArrayListUtil.ChainableArrayListUtil<>(searchResult)
                                                     .map(result -> result.substring(stringAfterLastSpace.length()))
                                                     .map(suggestedSuffix -> prefix + suggestedSuffix)
                                                     .value();
        this.searchResult = finalResult;
    }

    /**
     * Returns the next search result of prefix.
     * @return next search result.
     */
    public String next() {
        if (hasNoSearchResult()) {
            return "";
        }
        if (searchIndex + 1 < searchResult.size()) {
            searchIndex++;
        }
        return searchResult.get(searchIndex);
    }

    /**
     * Clears search result of previous prefix search.
     */
    public void clear() {
        searchResult.clear();
        searchIndex = -1;
    }

    /**
     * Adds a given string into the search tree
     */
    public void addStringsToTree(String string) {
        String[] wordArray = string.split("\\s+");

        for (String word : wordArray) {
            tree.addString(word);
        }
    }

    /**
     * Builds a tree of triggerWords of command.
     */
    private void buildTree() {
        for (Command c : commandList) {
            for (String word : c.getTriggerWords()) {
                tree.addString(word);
            }
        }
    }
}
