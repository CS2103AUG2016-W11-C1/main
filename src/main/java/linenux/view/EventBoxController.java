package linenux.view;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import linenux.control.ControlUnit;
import linenux.model.State;
import linenux.model.Task;
import linenux.util.ArrayListUtil;
import linenux.view.components.EventCell;

//@@author A0144915A
/**
 * Controller for the event box, which displays all the events
 */
public class EventBoxController {
    @FXML
    private ListView<Task> eventsList;

    private ControlUnit controlUnit;
    private ObservableList<Task> events = FXCollections.observableArrayList();

    /**
     * Initializes subviews.
     */
    @FXML
    private void initialize() {
        eventsList.itemsProperty().setValue(events);
        eventsList.setCellFactory(EventCell::new);
    }

    /**
     * Update the application {@code ControlUnit}.
     * @param controlUnit The new {@code ControlUnit}.
     */
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

    /**
     * Render new events.
     */
    private void updateEvents() {
        ArrayList<Task> tasks = this.controlUnit.getSchedule().getTaskList();
        ArrayList<Task> undoneTasks = new ArrayListUtil.ChainableArrayListUtil<>(tasks)
                .filter(Task::isNotDone)
                .value();
        ArrayList<Task> events = filterEvents(undoneTasks);
        this.events.setAll(events);
    }

    /**
     * Render filtered events.
     */
    private void updateFilteredEvents() {
        ArrayList<Task> filteredTasks = this.controlUnit.getSchedule().getFilteredTasks();
        ArrayList<Task> events = filterEvents(filteredTasks);
        this.events.setAll(events);
    }

    /**
     * Sieve out only events.
     * @param tasks The original {@code ArrayList} of {@code Task}.
     * @return An {@code ArrayList} of {@code Task} which are events.
     */
    private ArrayList<Task> filterEvents(ArrayList<Task> tasks) {
        ArrayList<Task> events = new ArrayListUtil.ChainableArrayListUtil<>(tasks)
                .filter(Task::isEvent)
                .sortBy(Task::getTaskName)
                .sortBy(Task::getStartTime)
                .value();

        return events;
    }
}
