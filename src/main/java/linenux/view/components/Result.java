package linenux.view.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import linenux.view.MainWindowController;

import java.io.IOException;

//@@author A0144915A
public class Result {
    private HBox root;

    @FXML
    private Label label;

    private String result;
    private Runnable onClose;

    public Result(String result, Runnable onClose) {
        this.result = result;
        this.onClose = onClose;
        this.setupView();
    }

    private void setupView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainWindowController.class.getResource("/view/Result.fxml"));
            loader.setController(this);
            this.root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        this.label.setText(this.result);
    }

    @FXML
    private void onCloseButtonClicked() {
        this.onClose.run();
    }

    public HBox getRoot() {
        return this.root;
    }
}
