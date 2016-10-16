package linenux.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * Created by yihangho on 10/16/16.
 */
public class CommandBoxController {
    @FXML
    private TextField textField;

    @FXML
    private void onCommand() {
        System.out.println(textField.getText());
    }
}
