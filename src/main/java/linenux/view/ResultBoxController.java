package linenux.view;

import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import linenux.command.result.CommandResult;
import linenux.control.ControlUnit;

/**
 * Created by yihangho on 10/16/16.
 */
public class ResultBoxController {
    @FXML
    private Label commandResultLabel;

    private ControlUnit controlUnit;

    public void setControlUnit(ControlUnit controlUnit) {
        this.controlUnit = controlUnit;

        this.controlUnit.getLastCommandResultProperty().addListener((change) -> {
            CommandResult lastResult = this.controlUnit.getLastCommandResultProperty().getValue();
            commandResultLabel.setText(lastResult.getFeedback());
        });
    }
}
