package linenux.view.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import linenux.view.MainWindowController;

import java.io.IOException;

//@@author A0144915A
/**
 * A view component to display a {@code CommandResult}.
 */
public class Result {
    private HBox root;

    @FXML
    private Label label;

    private String result;
    private Runnable onClose;

    /**
     * Instantiate {@code Result} with the string to display and a callback.
     * @param result The {@code String} to display.
     * @param onClose A callback that will be executed when the close button is clicked.
     */
    public Result(String result, Runnable onClose) {
        this.result = result;
        this.onClose = onClose;
        this.setupView();
    }

    /**
     * Load and setup the subview components.
     */
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

    /**
     * Display the text when the subviews are ready.
     */
    @FXML
    private void initialize() {
        this.label.setText(this.result);
    }

    /**
     * Callback when the close button is clicked.
     */
    @FXML
    private void onCloseButtonClicked() {
        this.onClose.run();
    }

    /**
     * @return The root container.
     */
    public HBox getRoot() {
        return this.root;
    }
}
