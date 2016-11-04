package linenux;

import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import linenux.config.Config;
import linenux.config.JsonConfig;
import linenux.util.LogsCenter;
import linenux.util.ThrowableUtil;
import linenux.view.Alerts;
import linenux.view.MainWindowController;

/**
 * Main program for Linenux.
 */
public class Main extends Application implements Stoppable {
    public static final String APP_NAME = "Linenux";

    private static Logger logger = LogsCenter.getLogger(Main.class);

    protected Config config = new JsonConfig();

    public static void main(String args) {
        launch(args);
    }

    // TODO: Handle general exception & make TextArea keyboard insensitive.
    //@@author A0135788M
    @Override
    public void start(Stage primaryStage) {
        logger.info("Setting up main window");
        setupMainWindow(primaryStage);
    }

    @Override
    public void stop() throws Exception {
        logger.info("Stopping the application");
        super.stop();
    }

    //@@author A0144915A
    private void setupMainWindow(Stage primaryStage) {
        try {
            FXMLLoader loader = setUpLoader();
            Scene scene = new Scene(loader.load());
            setUpStage(primaryStage);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            logger.severe(ThrowableUtil.getStackTrace(e));
            Alerts.alertAndDie("Fatal Error", "Unable to initialize main window.");
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
