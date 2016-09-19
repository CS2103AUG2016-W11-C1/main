package linenux.view;

import linenux.control.ControlUnit;
import javafx.fxml.FXML;
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
    
    public ConsoleController() {}
    
    public void setControlUnit(ControlUnit controlUnit) {
        this.controlUnit = controlUnit;
    }
 
   @FXML
    private void onCommand(ActionEvent event) {
        String userInput = commandInput.getText();
        String[] outputs = controlUnit.execute(userInput);
        displayResult(outputs);
        clearCommandInput();
   }

    
    /** Clears the command input box. */
    private void clearCommandInput() {
        commandInput.setText("");
    }

    /** Clears the output display area. */
    public void clearOutputConsole(){
        outputConsole.clear();
    }
    
    public void displayResult(String[] outputs) {
        clearOutputConsole();
        display(outputs);
    }
    
    public void displayWelcomeMessage(String version) {
        display(version);
    }
    
    /**
     * Displays the given messages on the output display area, after formatting appropriately.
     */
    private void display(String... messages) {
        String result = "";
        for (String message: messages) {
            result += message + "\n";
        }
        outputConsole.setText(result);
    }
}
