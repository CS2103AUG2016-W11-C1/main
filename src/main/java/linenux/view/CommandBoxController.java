package linenux.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import linenux.control.ControlUnit;

/**
 * Created by yihangho on 10/16/16.
 */
public class CommandBoxController {
    @FXML
    private TextField textField;

    private ControlUnit controlUnit;

    @FXML
    private void initialize() {
        Platform.runLater(() -> textField.requestFocus());
    }

    @FXML
    private void onCommand() {
        this.controlUnit.execute(textField.getText());
        textField.setText("");
    }

    public void setControlUnit(ControlUnit controlUnit) {
        this.controlUnit = controlUnit;
    }
}
