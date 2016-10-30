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

/**
 * Created by yihangho on 10/30/16.
 */
public class ResultsOverlayController {
    @FXML
    private VBox container;

    private Timer timer = new Timer();
    private ControlUnit controlUnit;
    private ObservableList<String> messages = FXCollections.observableArrayList();
    private ArrayList<TimerTask> pendingTasks = new ArrayList<>();

    public void setControlUnit(ControlUnit controlUnit) {
        // TODO remove listeners from old control unit
        this.controlUnit = controlUnit;
        this.controlUnit.getLastCommandResultProperty().addListener((change) -> {
            CommandResult lastResult = this.controlUnit.getLastCommandResultProperty().get();
            this.messages.add(lastResult.getFeedback());

            TimerTask task = this.autoRemovalTask();
            this.timer.schedule(task, 5000);
            this.pendingTasks.add(task);
        });
    }

    @FXML
    private void initialize() {
        this.messages.addListener((ListChangeListener<? super String>) (change) -> {
            this.render();
        });
    }

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
