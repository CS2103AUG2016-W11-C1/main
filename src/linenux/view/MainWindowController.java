package linenux.view;

import linenux.Stoppable;
import linenux.logic.Logic;
import linenux.command.CommandResult;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Controller for main window.
 */
public class MainWindowController {
    @FXML
    private TextArea outputConsole;
    
    @FXML
    private TextField commandInput;
    
    private Logic logic;
    private Stoppable mainApp;
    
    public MainWindowController() {}
    
    public void setLogic(Logic logic) {
        this.logic = logic;
    }
    
    public void setMainApp(Stoppable mainApp) {
        this.mainApp = mainApp;
    }
    
    @FXML
    private void onCommand(ActionEvent event) {
        try {
            String userInput = commandInput.getText();
            CommandResult result = logic.execute(userInput);
            displayResult(result);
            clearCommandInput();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /** Clears the command input box */
    private void clearCommandInput() {
        commandInput.setText("");
    }

    /** Clears the output display area */
    public void clearOutputConsole(){
        outputConsole.clear();
    }
    
    public void displayResult(CommandResult result) {
        clearOutputConsole();
        display(result.feedbackToUser);
    }
    
    public void displayWelcomeMessage(String version) {
        display(version);
    }
    
    /**
     * Displays the given messages on the output display area, after formatting appropriately.
     */
    private void display(String message) {
        outputConsole.setText(message);
    }
}
