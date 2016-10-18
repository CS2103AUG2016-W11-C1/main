package linenux.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import linenux.control.ControlUnit;

import java.util.ArrayList;

/**
 * Created by yihangho on 10/16/16.
 */
public class CommandBoxController {
    @FXML
    private TextField textField;

    private ControlUnit controlUnit;
    private ArrayList<String> history = new ArrayList<>();
    int historyIndex = -1;

    @FXML
    private void initialize() {
        Platform.runLater(() -> textField.requestFocus());

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
    }
}
