package linenux.view.components;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import linenux.model.Task;
import linenux.util.ArrayListUtil;
import linenux.util.LocalDateTimeUtil;

//@@author A0135788M
/**
 * View component used to display a single event.
 */
public class EventCell extends ListCell<Task> {
    @FXML
    private Label title;

    @FXML
    private Label time;

    @FXML
    private Label tags;

    @FXML
    private AnchorPane container;

    private ListView<Task> parent;

    /**
     * Instantiate a {@code EventCell}.
     * @param parent The {@code ListView} that uses this cell.
     */
    public EventCell(ListView<Task> parent) {
        super();

        this.parent = parent;

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TodoCell.class.getResource("/view/EventCell.fxml"));
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

        this.container.getStyleClass().removeAll("no-tags", "overdue", "empty", "done");

        if (empty || task == null) {
            this.title.setText("");
            this.time.setText("");
            this.tags.setText("");
            this.container.getStyleClass().add("empty");
        } else {
            ArrayList<String> tagsWithHash = new ArrayListUtil.ChainableArrayListUtil<>(task.getTags())
                    .map(tag -> "#" + tag).value();

            if (tagsWithHash.isEmpty()) {
                this.container.getStyleClass().add("no-tags");
            }

            if (this.isOverdue(task)) {
                this.container.getStyleClass().add("overdue");
            }

            if (task.isDone()) {
                this.container.getStyleClass().add("done");
            }

            this.title.setText(task.getTaskName());
            this.time.setText(LocalDateTimeUtil.toString(task.getStartTime()) + " - "
                    + LocalDateTimeUtil.toString(task.getEndTime()));
            this.tags.setText(String.join(", ", tagsWithHash));
        }
    }

    /**
     * Check if a {@code Task} is overdue.
     * @param task The {@code Task} to check.
     * @return {@code true} if and only if {@code task} is overdue.
     */
    private boolean isOverdue(Task task) {
        if (task.isDeadline() || task.isEvent()) {
            LocalDateTime now = LocalDateTime.now();
            return now.isAfter(task.getEndTime());
        }
        return false;
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
