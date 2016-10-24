package linenux.view;

import java.util.ArrayList;
import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import linenux.control.ControlUnit;
import linenux.model.State;
import linenux.model.Task;
import linenux.util.ArrayListUtil;

/**
 * Created by yihangho on 10/16/16.
 */
public class EventBoxController {
    @FXML
    private ListView<Task> eventsList;

    private ControlUnit controlUnit;
    private ObservableList<Task> events = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        eventsList.itemsProperty().setValue(events);
    }

    public void setControlUnit(ControlUnit controlUnit) {
        this.controlUnit = controlUnit;
        updateEvents();
        this.controlUnit.getSchedule().getStates().addListener((ListChangeListener<? super State>) c -> {
            updateEvents();
        });
        this.controlUnit.getSchedule().getFilteredTaskList().addListener((ListChangeListener<? super ArrayList<Task>>) c -> {
            updateFilteredEvents();
        });
    }

    private void updateEvents() {
        ArrayList<Task> tasks = this.controlUnit.getSchedule().getTaskList();
        ArrayList<Task> events = filterEvents(tasks);
        this.events.setAll(events);
    }

    private void updateFilteredEvents() {
        ArrayList<Task> filteredTasks = this.controlUnit.getSchedule().getFilteredTasks();
        ArrayList<Task> events = filterEvents(filteredTasks);
        this.events.setAll(events);
    }

    private ArrayList<Task> filterEvents(ArrayList<Task> tasks) {
        ArrayList<Task> events = new ArrayListUtil.ChainableArrayListUtil<>(tasks)
                .filter(Task::isEvent)
                .filter(((Predicate<Task>) Task::isDone).negate())
                .sortBy(Task::getTaskName)
                .sortBy(Task::getStartTime)
                .value();

        return events;
    }
}