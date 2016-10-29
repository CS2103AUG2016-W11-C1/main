package linenux.view;

import java.time.LocalDateTime;

import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import linenux.model.Task;

public class OverdueCell extends ListCell<Task> {
    @Override
    protected void updateItem(Task task, boolean empty) {
        super.updateItem(task, empty);

        if (empty || task == null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(task.toString());
            setTextFill(isOverdue(task) ? Color.RED : Color.BLACK);
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
