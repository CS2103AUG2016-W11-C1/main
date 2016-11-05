package linenux.view.components;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import linenux.model.Task;
import linenux.util.ArrayListUtil;

//@@author A0144915A
/**
 * View component used to display a single todo.
 */
public class TodoCell extends ListCell<Task> {
    @FXML
    private Label title;

    @FXML
    private Label tags;

    @FXML
    private AnchorPane container;

    private ListView<Task> parent;

    /**
     * Instantiate a {@code TodoCell}.
     * @param parent The {@code ListView} that uses this cell.
     */
    public TodoCell(ListView<Task> parent) {
        super();

        this.parent = parent;

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TodoCell.class.getResource("/view/TodoCell.fxml"));
            loader.setController(this);
            AnchorPane result = loader.load();
            this.setGraphic(result);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Callback when the {@code Task} is updated.
     * @param task The new {@code Task}.
     * @param empty Whether the cell is empty.
     */
    @Override
    public void updateItem(Task task, boolean empty) {
        super.updateItem(task, empty);

        this.container.getStyleClass().removeAll("no-tags", "empty", "done");

        if (empty || task == null) {
            this.container.getStyleClass().add("empty");
            this.title.setText("");
            this.tags.setText("");
        } else {
            ArrayList<String> tagsWithHash = new ArrayListUtil.ChainableArrayListUtil<>(task.getTags())
                    .map(tag -> "#" + tag)
                    .value();

            this.title.setText(task.getTaskName());
            this.tags.setText(String.join(", ", tagsWithHash));

            if (tagsWithHash.isEmpty()) {
                this.container.getStyleClass().add("no-tags");
            }

            if (task.isDone()) {
                this.container.getStyleClass().add("done");
            }
        }
    }

    /**
     * Callback to initialize the component when various children are ready.
     * Here we make sure that the container does not grow wider than the list.
     */
    @FXML
    private void initialize() {
        this.container.setMaxWidth(this.parent.getWidth());
        this.parent.widthProperty().addListener(change -> {
            this.container.setMaxWidth(this.parent.getWidth());
        });
    }
}
