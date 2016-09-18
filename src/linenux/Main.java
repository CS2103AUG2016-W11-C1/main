package linenux;

import linenux.logic.Logic;
import linenux.view.Gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * Main entry point to the application.
 */
public class Main extends Application implements Stoppable {

    /** Version information of the program. */
    public static final String VERSION = "Linenux - Version 0.1";
    
    private Gui gui;

    
    @Override
    public void start(Stage primaryStage) throws Exception {
        gui = new Gui(new Logic(), VERSION);
        gui.start(primaryStage, this);
    }
    
    @Override
    public void stop() throws Exception {
        super.stop();
        Platform.exit();
        System.exit(0);
    }
    
    public static void main(String args) {
        launch(args);
    }
}
