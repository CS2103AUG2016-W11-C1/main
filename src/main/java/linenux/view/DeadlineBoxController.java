package linenux.view;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import linenux.control.ControlUnit;
import linenux.model.State;
import linenux.model.Task;
import linenux.util.ArrayListUtil;

import java.util.ArrayList;
import java.util.function.Predicate;

/**
 * Created by yihangho on 10/16/16.
 */
public class DeadlineBoxController {
    @FXML
    private ListView<Task> deadlinesList;

    private ControlUnit controlUnit;
    private ObservableList<Task> deadlines = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        deadlinesList.itemsProperty().setValue(deadlines);
    }

    public void setControlUnit(ControlUnit controlUnit) {
        this.controlUnit = controlUnit;
        updateDeadlines();
        this.controlUnit.getSchedule().getStates().addListener((ListChangeListener<? super State>) c -> {
            updateDeadlines();
        });
    }

    private void updateDeadlines() {
        ArrayList<Task> tasks = this.controlUnit.getSchedule().getTaskList();
        ArrayList<Task> deadlines = new ArrayListUtil.ChainableArrayListUtil<>(tasks)
                .filter(Task::isDeadline)
                .filter(((Predicate<Task>) Task::isDone).negate())
                .sortBy(Task::getEndTime)
                .value();
        this.deadlines.setAll(deadlines);
    }
}
