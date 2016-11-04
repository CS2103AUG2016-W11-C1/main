package linenux.view;

import javafx.application.Platform;
import javafx.scene.control.Alert;

//@@author A0144915A
public class Alerts {
    public static void alertAndDie(String title, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(contentText);
        alert.showAndWait();
        Platform.exit();
    }
}
