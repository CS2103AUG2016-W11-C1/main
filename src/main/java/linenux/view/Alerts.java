package linenux.view;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

//@@author A0144915A
public class Alerts {
    public static void alertAndDie(String title, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(contentText);
        alert.showAndWait();
        System.exit(1);
    }

    public static boolean alertAndConfirm(String title, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR, contentText, ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        return alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES;

    }
}
