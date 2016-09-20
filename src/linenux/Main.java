package linenux;

import linenux.control.ControlUnit;
import linenux.view.ConsoleController;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.fxml.FXMLLoader;

/**
 * Main program for Linenux.
 */

public class Main extends Application implements Stoppable {

    public static final String APP_NAME = "Linenux";
    public static final String VERSION = "Linenux - Version 0.1";

    public static final int INITIAL_CONSOLE_WIDTH = 800;
    public static final int INITIAL_CONSOLE_HEIGHT = 500;
    
    public static void main(String args) {
        launch(args);
    }

    // TODO: Handle general exception & make TextArea keyboard insensitive.
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            ConsoleController consoleController = setUpConsole(primaryStage);
            consoleController.setControlUnit(new ControlUnit());
            consoleController.displayWelcomeMessage(VERSION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Platform.exit();
        System.exit(0);
    }

    private ConsoleController setUpConsole(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/Console.fxml"));
        primaryStage.setTitle(APP_NAME);
        // TODO: Fix icon
        primaryStage.getIcons().add(new Image("file:resources/images/terminal.png"));
        primaryStage.setScene(new Scene(loader.load(), INITIAL_CONSOLE_WIDTH, INITIAL_CONSOLE_HEIGHT));
        primaryStage.show();
        return loader.getController();
    }
}
