package linenux.view.components;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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

    public EventCell() {
        super();

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TodoCell.class.getResource("/view/EventCell.fxml"));
            loader.setController(this);
            VBox result = loader.load();
            this.setGraphic(result);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    public void updateItem(Task task, boolean empty) {
        super.updateItem(task, empty);

        if (empty || task == null) {
            this.title.setText("");
            this.time.setText("");
            this.tags.setText("");
        } else {
            ArrayList<String> tagsWithHash = new ArrayListUtil.ChainableArrayListUtil<>(task.getTags())
                    .map(tag -> "#" + tag).value();

            this.title.setText(task.getTaskName());
            this.time.setText("From " + LocalDateTimeUtil.toString(task.getStartTime()) + " to "
                    + LocalDateTimeUtil.toString(task.getEndTime()));
            this.tags.setText(String.join(", ", tagsWithHash));

            if (isOverdue(task)) {
                this.time.setTextFill(Color.RED);
            } else {
                this.time.setTextFill(Color.MINTCREAM);
            }
            this.title.setTextFill(Color.MINTCREAM);
            this.tags.setTextFill(Color.MINTCREAM);
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
