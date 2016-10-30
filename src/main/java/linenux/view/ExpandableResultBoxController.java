package linenux.view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import linenux.control.ControlUnit;

/**
 * Created by yihangho on 10/30/16.
 */
public class ExpandableResultBoxController {
    @FXML
    Button button;
    @FXML
    TextArea textArea;

    private BooleanProperty expanded = new SimpleBooleanProperty(false);
    private ControlUnit controlUnit;

    public void setControlUnit(ControlUnit controlUnit) {
        this.controlUnit = controlUnit;
        this.controlUnit.getLastCommandResultProperty().addListener((change) -> {
            this.setText(this.controlUnit.getLastCommandResultProperty().get().getFeedback());
        });
    }

    @FXML
    private void initialize() {
        this.expanded.addListener(change -> {
            this.render();
        });
    }

    @FXML
    private void toggleExpandedState() {
        this.expanded.set(!this.expanded.get());
    }

    private void render() {
        if (this.expanded.get()) {
            this.button.setText("Hide");
            this.textArea.setPrefHeight(200);
        } else {
            this.button.setText("Show");
            this.textArea.setPrefHeight(0);
        }
    }

    private void setText(String text) {
        if (text.trim().contains("\n")) {
            this.expanded.set(true);
            this.textArea.setText(text.trim());
        } else {
            // TODO show the reminders instead.
            this.textArea.setText("");
        }
    }
}
