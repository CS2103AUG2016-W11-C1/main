package linenux.view;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

//@@author A0144915A
/**
 * Helper functions to show alert.
 */
public class Alerts {
    /**
     * Show an alert.
     * @param title The title of the alert.
     * @param contentText The content of the alert.
     */
    public static void alert(String title, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    /**
     * Show an alert and end the program.
     * @param title The title of the alert.
     * @param contentText The content of the alert.
     */
    public static void alertAndDie(String title, String contentText) {
        alert(title, contentText);
        System.exit(1);
    }

    /**
     * Asks the user a yes/no question.
     * @param title The title of the alert.
     * @param contentText the content of the alert.
     * @return {@code true} if and only if the user chose yes.
     */
    public static boolean alertAndConfirm(String title, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR, contentText, ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        return alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES;

    }
}
