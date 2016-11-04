package linenux;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import linenux.config.Config;
import linenux.config.JsonConfig;
import linenux.view.MainWindowController;

/**
 * Main program for Linenux.
 */
public class Main extends Application implements Stoppable {

    public static final String APP_NAME = "Linenux";

    protected Config config = new JsonConfig();

    public static void main(String args) {
        launch(args);
    }

    // TODO: Handle general exception & make TextArea keyboard insensitive.
    //@@author A0135788M
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

    //@@author A0144915A
    private void setupMainWindow(Stage primaryStage) {
        try {
            FXMLLoader loader = setUpLoader();
            Scene scene = new Scene(loader.load());
            setUpStage(primaryStage);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private FXMLLoader setUpLoader() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/view/MainWindow.fxml"));
        loader.setController(new MainWindowController(this.config));
        return loader;
    }

    private void setUpStage(Stage primaryStage) {
        primaryStage.setTitle(APP_NAME);
        primaryStage.getIcons().add(new Image("/images/terminal.png"));
        primaryStage.setMaximized(true);;
    }
}
