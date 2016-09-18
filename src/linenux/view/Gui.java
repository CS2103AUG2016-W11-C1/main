package linenux.view;

import linenux.Main;
import linenux.Stoppable;
import linenux.logic.Logic;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * GUI of the application.
 */
public class Gui {
    public static final int INITIAL_WINDOW_WIDTH = 800;
    public static final int INITIAL_WINDOW_HEIGHT = 600;
    
    private final Logic logic;
    private MainWindowController mainWindowController;
    private String version;
    
    public Gui(Logic logic, String version) {
        this.logic = logic;
        this.version = version;
    }
    
    public void start(Stage primaryStage, Stoppable mainApp) throws IOException {
        mainWindowController = setUpMainWindow(primaryStage, mainApp);
        mainWindowController.displayWelcomeMessage(version);
    }
    
    private MainWindowController setUpMainWindow(Stage primaryStage, Stoppable mainApp) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view" + File.separator + "MainWindow.fxml"));
        primaryStage.setTitle(version);
        primaryStage.setScene(new Scene(loader.load(), INITIAL_WINDOW_WIDTH, INITIAL_WINDOW_HEIGHT));
        primaryStage.show();
        MainWindowController mainWindowController = loader.getController();
        mainWindowController.setLogic(logic);
        mainWindowController.setMainApp(mainApp);
        return mainWindowController;
    }
}
