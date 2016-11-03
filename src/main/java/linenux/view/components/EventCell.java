package linenux.view.components;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import linenux.model.Task;
import linenux.util.ArrayListUtil;
import linenux.util.LocalDateTimeUtil;

//@@author A0135788M
public class EventCell extends ListCell<Task> {
    @FXML
    private Label title;

    @FXML
    private Label time;

    @FXML
    private Label tags;

    @FXML
    private AnchorPane container;

    public EventCell() {
        super();

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

    private boolean isOverdue(Task task) {
        if (task.isDeadline() || task.isEvent()) {
            LocalDateTime now = LocalDateTime.now();
            return now.isAfter(task.getEndTime());
        }
        return false;
    }
}
