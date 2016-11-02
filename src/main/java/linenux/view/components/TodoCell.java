package linenux.view.components;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import linenux.model.Task;
import linenux.util.ArrayListUtil;

//@@author A0144915A
public class TodoCell extends ListCell<Task> {
    @FXML
    private Label title;

    @FXML
    private Label tags;

    @FXML
    private AnchorPane container;

    public TodoCell() {
        super();

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

    @Override
    public void updateItem(Task task, boolean empty) {
        super.updateItem(task, empty);

        this.container.getStyleClass().removeAll("no-tags", "empty");

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
        }
    }
}
