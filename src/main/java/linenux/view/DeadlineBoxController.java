package linenux.view;

import java.util.ArrayList;
import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import linenux.control.ControlUnit;
import linenux.model.State;
import linenux.model.Task;
import linenux.util.ArrayListUtil;

//@@author A0144915A
public class DeadlineBoxController {
    @FXML
    private ListView<Task> deadlinesList;

    private ControlUnit controlUnit;
    private ObservableList<Task> deadlines = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        setOverdueCell();
        deadlinesList.itemsProperty().setValue(deadlines);
    }

    public void setControlUnit(ControlUnit controlUnit) {
        this.controlUnit = controlUnit;
        updateDeadlines();
        this.controlUnit.getSchedule().getStates().addListener((ListChangeListener<? super State>) c -> {
            updateDeadlines();
        });
        this.controlUnit.getSchedule().getFilteredTaskList().addListener((ListChangeListener<? super ArrayList<Task>>) c -> {
            updateFilteredDeadlines();
        });
    }

    private void updateDeadlines() {
        ArrayList<Task> tasks = this.controlUnit.getSchedule().getTaskList();
        ArrayList<Task> deadlines = filterDeadlines(tasks);
        this.deadlines.setAll(deadlines);
    }

    private void updateFilteredDeadlines() {
        ArrayList<Task> filteredTasks = this.controlUnit.getSchedule().getFilteredTasks();
        ArrayList<Task> deadlines = filterDeadlines(filteredTasks);
        this.deadlines.setAll(deadlines);
    }

    private ArrayList<Task> filterDeadlines(ArrayList<Task> tasks) {
        ArrayList<Task> deadlines = new ArrayListUtil.ChainableArrayListUtil<>(tasks)
                .filter(Task::isDeadline)
                .filter(((Predicate<Task>) Task::isDone).negate())
                .sortBy(Task::getTaskName)
                .sortBy(Task::getEndTime)
                .value();

        return deadlines;
    }

    private void setOverdueCell() {
        deadlinesList.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
            @Override
            public ListCell<Task> call(ListView<Task> list) {
                return new OverdueCell();
            }
        });
    }
}
