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
    }

    private void updateEvents() {
        ArrayList<Task> tasks = this.controlUnit.getSchedule().getTaskList();
        ArrayList<Task> events = new ArrayListUtil.ChainableArrayListUtil<>(tasks)
                .filter(Task::isEvent)
                .sortBy(Task::getStartTime)
                .value();
        this.events.setAll(events);
    }
}
