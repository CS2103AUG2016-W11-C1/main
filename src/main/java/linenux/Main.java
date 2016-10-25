package linenux;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main program for Linenux.
 */
public class Main extends Application implements Stoppable {

    public static final String APP_NAME = "Linenux";
    public static final String VERSION = "Linenux - Version 0.3";

    public static final int INITIAL_CONSOLE_WIDTH = 800;
    public static final int INITIAL_CONSOLE_HEIGHT = 500;

    public static void main(String args) {
        launch(args);
    }

    // TODO: Handle general exception & make TextArea keyboard insensitive.
    @Override
    public void start(Stage primaryStage) throws Exception {
        setupMainWindow(primaryStage);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Platform.exit();
        System.exit(0);
    }

    private void setupMainWindow(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/view/MainWindow.fxml"));
            primaryStage.setTitle(APP_NAME);
            primaryStage.getIcons().add(new Image("/images/terminal.png"));
            Scene scene = new Scene(loader.load(), INITIAL_CONSOLE_WIDTH, INITIAL_CONSOLE_HEIGHT);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
