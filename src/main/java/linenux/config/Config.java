package linenux.config;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

//@@author A0135788M
/**
 * Creates the configuration file.
 */
public class Config {
    public static final String DEFAULT_FILE_PATH = Paths.get(".").toAbsolutePath().toString();
    public static final String CONFIG_FILENAME = "Config.json";
    public static final String SCHEDULE_FILENAME = "Schedule.xml";

    private Path configFilePath;
    private Path scheduleFilePath;

    public Config() {
        this(DEFAULT_FILE_PATH + CONFIG_FILENAME, DEFAULT_FILE_PATH + SCHEDULE_FILENAME);
    }

    public Config(String configFilePath, String scheduleFilePath) {
        this.configFilePath = Paths.get(configFilePath);
        this.scheduleFilePath = Paths.get(scheduleFilePath);
        initialize();
    }

    /**
     * Initializes configuration file with default values;
     */
    @SuppressWarnings("unchecked")
    private void initialize() {
        if (hasConfigFile()) {
            return;
        }

        JSONObject configFile = new JSONObject();
        configFile.put("Actual Schedule File Path", scheduleFilePath.toString());

        try {
            FileWriter file = new FileWriter(configFilePath.toString());
            file.write(configFile.toJSONString());
            file.flush();
            file.close();
        } catch (IOException i) {
            throwAlert("Creating File Error", "Could not create file at: \n" + configFilePath.toString());
        }
    }

    public String getActualFilePath() {
        JSONParser parser = new JSONParser();
        try {
            JSONObject configFile = (JSONObject) parser.parse(new FileReader(configFilePath.toString()));
            return (String) configFile.get("Actual Schedule File Path");
        } catch (IOException i) {
            throwAlert("Reading File Error", "Could not read file at: \n" + configFilePath.toString());
            return "";
        } catch (ParseException p) {
            throwAlert("Parsing Error", "Could not parse file at: \n" + configFilePath.toString());
            return "";
        }
    }

    public boolean hasConfigFile() {
        return Files.exists(configFilePath);
    }

    private Alert throwAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        return alert;
    }
}
