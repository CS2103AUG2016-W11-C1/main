package linenux.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import linenux.command.result.CommandResult;
import linenux.control.ControlUnit;
import linenux.util.ArrayListUtil;
import linenux.view.components.Result;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

//@@author A0144915A
/**
 * Controller for the overlay results, which is the set of shorter {@code CommandResult}.
 */
public class ResultsOverlayController {
    @FXML
    private VBox container;

    private Timer timer = new Timer();
    private ControlUnit controlUnit;
    private ObservableList<String> messages = FXCollections.observableArrayList();
    private ArrayList<TimerTask> pendingTasks = new ArrayList<>();

    /**
     * Update the application {@code ControlUnit}.
     * @param controlUnit The new {@code ControlUnit}.
     */
    public void setControlUnit(ControlUnit controlUnit) {
        this.controlUnit = controlUnit;
        this.controlUnit.getLastCommandResultProperty().addListener((change) -> {
            CommandResult lastResult = this.controlUnit.getLastCommandResultProperty().get();
            String feedback = lastResult.getFeedback().trim();

            if (!feedback.contains("\n") && feedback.length() > 0) {
                this.messages.add(lastResult.getFeedback());

                TimerTask task = this.autoRemovalTask();
                this.timer.schedule(task, 5000);
                this.pendingTasks.add(task);
            }
        });
    }

    /**
     * Initializes subviews.
     */
    @FXML
    private void initialize() {
        this.messages.addListener((ListChangeListener<? super String>) (change) -> {
            this.render();
        });
    }

    /**
     * Ensure that the UI is in the correct state.
     */
    private void render() {
        ArrayList<HBox> children = new ArrayListUtil.ChainableArrayListUtil<>(new ArrayList<>(messages))
                .mapWithIndex((message, i) -> {
                    Runnable runnable = () -> {
                        ResultsOverlayController.this.pendingTasks.get(i).run();
                        ResultsOverlayController.this.pendingTasks.get(i).cancel();
                    };
                    return new Result(message, runnable);
                })
                .map(Result::getRoot)
                .value();

        this.container.getChildren().setAll(children);
    }

    /**
     * @return A {@code TimerTask} that will remove the latest {@code CommandResult} when executed.
     */
    private TimerTask autoRemovalTask() {
        return new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    ResultsOverlayController.this.messages.remove(0);
                    ResultsOverlayController.this.pendingTasks.remove(0);
                });
            }
        };
    }
}
