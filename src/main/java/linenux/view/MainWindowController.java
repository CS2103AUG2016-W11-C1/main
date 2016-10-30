package linenux.view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import linenux.config.Config;
import linenux.control.ControlUnit;

//@@author A0144915A
public class MainWindowController {
    @FXML
    private StackPane stackPane;

    @FXML
    private SplitPane splitPane;
    @FXML
    private AnchorPane commandBoxContainer;

    private ControlUnit controlUnit;

    public MainWindowController(Config config) {
        this.controlUnit = new ControlUnit(config);
    }

    @FXML
    private void initialize() {
        setupTodoBox();
        setupDeadlineBox();
        setupEventBox();
        setupResultBox();
        splitPane.setDividerPositions(0.25, 0.50, 0.75);
        setupCommandBox();
        setupResultsOverlay();
    }

    private void setupTodoBox() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainWindowController.class.getResource("/view/TodoBox.fxml"));
            AnchorPane todoBox = loader.load();
            splitPane.getItems().add(todoBox);
            TodoBoxController controller = loader.getController();
            controller.setControlUnit(this.controlUnit);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupDeadlineBox() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainWindowController.class.getResource("/view/DeadlineBox.fxml"));
            AnchorPane deadlineBox = loader.load();
            splitPane.getItems().add(deadlineBox);
            DeadlineBoxController controller = loader.getController();
            controller.setControlUnit(this.controlUnit);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupEventBox() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainWindowController.class.getResource("/view/EventBox.fxml"));
            AnchorPane eventBox = loader.load();
            splitPane.getItems().add(eventBox);
            EventBoxController controller = loader.getController();
            controller.setControlUnit(this.controlUnit);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupResultBox() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainWindowController.class.getResource("/view/ResultBox.fxml"));
            AnchorPane resultBox = loader.load();
            splitPane.getItems().add(resultBox);
            ResultBoxController controller = loader.getController();
            controller.setControlUnit(this.controlUnit);
            controller.displayReminder();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupCommandBox() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainWindowController.class.getResource("/view/CommandBox.fxml"));
            TextField field = loader.load();
            AnchorPane.setTopAnchor(field, 2.0);
            AnchorPane.setRightAnchor(field, 5.0);
            AnchorPane.setBottomAnchor(field, 5.0);
            AnchorPane.setLeftAnchor(field, 5.0);
            commandBoxContainer.getChildren().add(field);

            CommandBoxController controller = loader.getController();
            controller.setControlUnit(this.controlUnit);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupResultsOverlay() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainWindowController.class.getResource("/view/ResultsOverlay.fxml"));
            this.stackPane.getChildren().add(loader.load());
            ResultsOverlayController controller = loader.getController();
            controller.setControlUnit(this.controlUnit);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
