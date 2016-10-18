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
public class TodoBoxController {
    @FXML
    private ListView<Task> todosList;

    private ControlUnit controlUnit;
    private ObservableList<Task> todos = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        todosList.itemsProperty().setValue(todos);
    }

    public void setControlUnit(ControlUnit controlUnit) {
        this.controlUnit = controlUnit;
        updateTodos();
        this.controlUnit.getSchedule().getStates().addListener((ListChangeListener<? super State>) c -> {
            updateTodos();
        });
    }

    private void updateTodos() {
        ArrayList<Task> tasks = this.controlUnit.getSchedule().getTaskList();
        ArrayList<Task> todos = new ArrayListUtil.ChainableArrayListUtil<>(tasks)
                .filter(Task::isTodo)
                .filter(((Predicate<Task>) Task::isDone).negate())
                .sortBy(Task::getTaskName)
                .value();
        this.todos.setAll(todos);
    }
}
