package linenux.view;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import linenux.control.ControlUnit;
import linenux.util.TernarySearchTree;

//@@author A0144915A
public class CommandBoxController {
    @FXML
    private TextField textField;

    private ControlUnit controlUnit;
    private TernarySearchTree tree;
    private ArrayList<String> history;
    private ArrayList<String> searchResult;
    private int historyIndex;
    private int searchIndex;

    @FXML
    private void initialize() {
        Platform.runLater(() -> textField.requestFocus());

        this.textField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.TAB)) {
                event.consume();
            }
        });

        this.textField.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.UP) && historyIndex > 0) {
                historyIndex--;
                this.textField.setText(this.history.get(historyIndex));
            }

            if (event.getCode().equals(KeyCode.DOWN)) {
                if (historyIndex < this.history.size() - 1) {
                    historyIndex++;
                    this.textField.setText(this.history.get(historyIndex));
                } else if (historyIndex == this.history.size() - 1) {
                    historyIndex++;
                    this.textField.setText("");
                }
            }

            if (event.getCode().equals(KeyCode.TAB)) {
                if (searchResult.isEmpty()) {
                    String prefix = this.textField.getText();
                    searchResult = tree.getAllStringsWithPrefix(prefix);
                    if (!searchResult.isEmpty()) {
                        searchIndex++;
                        this.textField.setText(this.searchResult.get(searchIndex));
                    }
                } else if (searchIndex + 1 < searchResult.size()) {
                    searchIndex++;
                    this.textField.setText(this.searchResult.get(searchIndex));
                }

            }

            if (!event.getCode().equals(KeyCode.TAB)) {
                searchIndex = -1;
                searchResult.clear();
            }

            this.textField.positionCaret(this.textField.getLength());
        });
    }

    @FXML
    private void onCommand() {
        String command = textField.getText();
        this.history.add(command);
        this.historyIndex = this.history.size();
        this.controlUnit.execute(command);
        textField.setText("");
    }

    public void setControlUnit(ControlUnit controlUnit) {
        this.controlUnit = controlUnit;
        setUpTree();
        this.history = new ArrayList<>();
        this.searchResult = new ArrayList<>();
        this.historyIndex = -1;
        this.searchIndex = -1;
    }

    // TODO: Get from alias file
    private void setUpTree() {
        this.tree = new TernarySearchTree();
        tree.addString("add");
        tree.addString("remind");
        tree.addString("edit");
        tree.addString("editr");
        tree.addString("rename");
        tree.addString("done");
        tree.addString("undone");
        tree.addString("delete");
        tree.addString("deleter");
        tree.addString("clear");
        tree.addString("freetime");
        tree.addString("list");
        tree.addString("today");
        tree.addString("tomorrow");
        tree.addString("view");
        tree.addString("undo");
        tree.addString("help");
        tree.addString("alias");
        tree.addString("unalias");
        tree.addString("path");
        tree.addString("information");
        tree.addString("exit");
    }
}
