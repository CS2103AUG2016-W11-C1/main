package linenux.view;

import linenux.control.ControlUnit;
import linenux.command.result.CommandResult;

import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Responsible for console IO.
 */
public class ConsoleController {
    @FXML
    private TextArea outputConsole;

    @FXML
    private TextField commandInput;

    private ControlUnit controlUnit;

    public void setControlUnit(ControlUnit controlUnit) {
        this.controlUnit = controlUnit;
    }
    
    public void setActiveControl() {
        Platform.runLater(() -> commandInput.requestFocus());
    }

    @FXML
    private void onCommand(ActionEvent event) {
        try {
            String userInput = commandInput.getText();
            CommandResult commandResult = controlUnit.execute(userInput);
            display(commandResult.getFeedback());
            clearCommandInput();
        } catch (NullPointerException e) {
            e.printStackTrace();
            clearCommandInput();
        }
    }

    /**
     * Clears the command input box.
     */
    private void clearCommandInput() {
        commandInput.setText("");
    }

    /**
     * Clears the output display area.
     */
    public void clearOutputConsole() {
        outputConsole.clear();
    }

    public void displayWelcomeMessage(String version) {
        display(version);
    }

    /**
     * Displays the given messages on the output display area, after formatting
     * appropriately.
     */
    private void display(String... messages) {
        clearOutputConsole();
        String result = "";
        for (String message : messages) {
            result += message + "\n";
        }
        outputConsole.setText(result);
    }
}
