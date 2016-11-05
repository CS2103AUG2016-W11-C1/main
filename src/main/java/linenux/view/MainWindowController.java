package linenux.view;

import java.io.IOException;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import linenux.config.Config;
import linenux.control.ControlUnit;
import linenux.util.LogsCenter;
import linenux.util.ThrowableUtil;

//@@author A0144915A
public class MainWindowController {
    private static Logger logger = LogsCenter.getLogger(MainWindowController.class);

    @FXML
    private StackPane stackPane;
    @FXML
    private VBox vbox;
    @FXML
    private GridPane gridPane;
    @FXML
    private AnchorPane commandBoxContainer;

    private ControlUnit controlUnit;

    public MainWindowController(Config config) {
        this.controlUnit = new ControlUnit(config);
    }

    @FXML
    private void initialize() {
        logger.info("Initializing MainWindowController");
        setupTodoBox();
        setupDeadlineBox();
        setupEventBox();
        setupExpandableCommandResult();
        setupCommandBox();
        setupResultsOverlay();
        logger.info("Done initializing MainWindowController");
    }

    private void setupTodoBox() {
        logger.info("Setting up todo box");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainWindowController.class.getResource("/view/TodoBox.fxml"));
            AnchorPane todoBox = loader.load();
            GridPane.setRowIndex(todoBox, 0);
            GridPane.setColumnIndex(todoBox, 0);
            gridPane.getChildren().add(todoBox);
            TodoBoxController controller = loader.getController();
            controller.setControlUnit(this.controlUnit);
        } catch (IOException e) {
            logger.severe(ThrowableUtil.getStackTrace(e));
            e.printStackTrace();
        }
        logger.info("Done setting up todo box");
    }

    private void setupDeadlineBox() {
        logger.info("Setting up deadline box");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainWindowController.class.getResource("/view/DeadlineBox.fxml"));
            AnchorPane deadlineBox = loader.load();
            GridPane.setRowIndex(deadlineBox, 0);
            GridPane.setColumnIndex(deadlineBox, 1);
            gridPane.getChildren().add(deadlineBox);
            DeadlineBoxController controller = loader.getController();
            controller.setControlUnit(this.controlUnit);
        } catch (IOException e) {
            logger.severe(ThrowableUtil.getStackTrace(e));
            e.printStackTrace();
        }
        logger.info("Done setting up deadline box");
    }

    private void setupEventBox() {
        logger.info("Setting up event box");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainWindowController.class.getResource("/view/EventBox.fxml"));
            AnchorPane eventBox = loader.load();
            GridPane.setRowIndex(eventBox, 0);
            GridPane.setColumnIndex(eventBox, 2);
            gridPane.getChildren().add(eventBox);
            EventBoxController controller = loader.getController();
            controller.setControlUnit(this.controlUnit);
        } catch (IOException e) {
            logger.severe(ThrowableUtil.getStackTrace(e));
            e.printStackTrace();
        }
        logger.info("Done setting up event box");
    }

    private void setupCommandBox() {
        logger.info("Setting up command box");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainWindowController.class.getResource("/view/CommandBox.fxml"));
            TextField field = loader.load();
            AnchorPane.setTopAnchor(field, 0.0);
            AnchorPane.setRightAnchor(field, 0.0);
            AnchorPane.setBottomAnchor(field, 0.0);
            AnchorPane.setLeftAnchor(field, 0.0);
            commandBoxContainer.getChildren().add(field);
            CommandBoxController controller = loader.getController();
            controller.setControlUnit(this.controlUnit);
        } catch (IOException e) {
            logger.severe(ThrowableUtil.getStackTrace(e));
            e.printStackTrace();
        }
        logger.info("Done setting up command box");
    }

    private void setupExpandableCommandResult() {
        logger.info("Setting up expandable command result");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainWindowController.class.getResource("/view/ExpandableResultBox.fxml"));
            VBox root = loader.load();
            vbox.getChildren().add(1, root);
            ExpandableResultBoxController controller = loader.getController();
            controller.setControlUnit(this.controlUnit);
        } catch (IOException e) {
            logger.severe(ThrowableUtil.getStackTrace(e));
            e.printStackTrace();
        }
        logger.info("Done setting up expandable command result");
    }

    private void setupResultsOverlay() {
        logger.info("Setting up results overlay");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainWindowController.class.getResource("/view/ResultsOverlay.fxml"));
            this.stackPane.getChildren().add(loader.load());
            ResultsOverlayController controller = loader.getController();
            controller.setControlUnit(this.controlUnit);
        } catch (IOException e) {
            logger.severe(ThrowableUtil.getStackTrace(e));
            e.printStackTrace();
        }
        logger.info("Done setting up results overlay");
    }

}
