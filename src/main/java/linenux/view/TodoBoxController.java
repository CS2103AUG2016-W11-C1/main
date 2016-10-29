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

//@@author A0144915A
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
        this.controlUnit.getSchedule().getFilteredTaskList().addListener((ListChangeListener<? super ArrayList<Task>>) c -> {
            updateFilteredTodos();
        });
    }

    private void updateTodos() {
        ArrayList<Task> tasks = this.controlUnit.getSchedule().getTaskList();
        ArrayList<Task> todos = filterToDos(tasks);
        this.todos.setAll(todos);
    }

    private void updateFilteredTodos() {
        ArrayList<Task> filteredTasks = this.controlUnit.getSchedule().getFilteredTasks();
        ArrayList<Task> todos = filterToDos(filteredTasks);
        this.todos.setAll(todos);
    }

    private ArrayList<Task> filterToDos(ArrayList<Task> tasks) {
        ArrayList<Task> todos = new ArrayListUtil.ChainableArrayListUtil<>(tasks)
                .filter(Task::isTodo)
                .filter(((Predicate<Task>) Task::isDone).negate())
                .sortBy(Task::getTaskName)
                .value();

        return todos;
    }
}
