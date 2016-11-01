package linenux.view;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import linenux.control.ControlUnit;
import linenux.util.AutoCompleter;

//@@author A0144915A
public class CommandBoxController {
    @FXML
    private TextField textField;

    private ControlUnit controlUnit;
    private AutoCompleter autoCompleter;
    private ArrayList<String> history;
    private int historyIndex;

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
                if (autoCompleter.hasNoSearchResult()) {
                    autoCompleter.findPrefix(this.textField.getText());
                }
                this.textField.setText(autoCompleter.next());
            }

            if (!event.getCode().equals(KeyCode.TAB)) {
                autoCompleter.clear();
            }

            this.textField.positionCaret(this.textField.getLength());
        });
    }

    @FXML
    private void onCommand() {
        String command = textField.getText();
        this.history.add(command);
        this.historyIndex = this.history.size();
        this.autoCompleter.addStringsToTree(command);
        this.controlUnit.execute(command);
        textField.setText("");
    }

    public void setControlUnit(ControlUnit controlUnit) {
        this.controlUnit = controlUnit;
        this.autoCompleter = new AutoCompleter(this.controlUnit.getCommandList());
        this.history = new ArrayList<>();
        this.historyIndex = -1;
    }
}