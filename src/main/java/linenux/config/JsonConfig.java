package linenux.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Creates the configuration file.
 */
public class JsonConfig implements Config{
    public static final String VERSION_NO = "v0.5";
    public static final String DEFAULT_FILE_PATH = Paths.get(".").toAbsolutePath().toString();
    public static final String CONFIG_FILENAME = "Config.json";
    public static final String SCHEDULE_FILENAME = "Schedule.xml";

    private static final String VERSION_KEY = "versionNo";
    private static final String SCHEDULE_PATH_KEY = "schedulePath";
    private static final String ALIASES_KEY = "aliases";
    private static final String SCHEDULE_LOCATIONS_KEY = "scheduleLocations";

    private Path configFilePath;
    private Path scheduleFilePath;
    private String verNo;
    private ArrayList<String> scheduleLocations;

    private JSONObject configFile;

    // @@author A0135788M
    public JsonConfig() {
        this(VERSION_NO, DEFAULT_FILE_PATH + CONFIG_FILENAME, DEFAULT_FILE_PATH + SCHEDULE_FILENAME);
    }

    public JsonConfig(String verNo, String configFilePath, String scheduleFilePath) {
        this.verNo = verNo;
        this.configFilePath = Paths.get(configFilePath);
        this.scheduleFilePath = Paths.get(scheduleFilePath);
        initialize();
        scheduleLocations = initializeScheduleLocations();
    }

    /**
     * Initializes configuration file with default values;
     */
    private void initialize() {
        JSONObject configFile;

        if (hasConfigFile()) {
            configFile = this.getConfigFile();
        } else {
            configFile = new JSONObject();
        }

        if (!configFile.has(VERSION_KEY)) {
            configFile.put(VERSION_KEY, verNo);
        }

        if (!configFile.has(SCHEDULE_PATH_KEY)) {
            configFile.put(SCHEDULE_PATH_KEY, scheduleFilePath.toString());
        }

        this.configFile = configFile;
        this.saveConfig();
    }

    // @@author A0127694U
    private ArrayList<String> initializeScheduleLocations() {
        JSONObject configFile = this.getConfigFile();
        JSONArray schedulesArray;

        if (configFile.has(SCHEDULE_LOCATIONS_KEY)) {
            schedulesArray = configFile.getJSONArray(SCHEDULE_LOCATIONS_KEY);
        } else {
            schedulesArray = new JSONArray();
        }

        ArrayList<String> output = new ArrayList<>();
        for (int i = 0; i < schedulesArray.length(); i++) {
            try {
                output.add(schedulesArray.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (!output.contains(this.scheduleFilePath.toString())) {
            output.add(this.scheduleFilePath.toString());
            configFile.put(SCHEDULE_LOCATIONS_KEY, output);
        }

        this.saveConfig();
        return output;
    }

    @Override
    public String getVersionNo() {
        try {
            return this.getConfigFile().getString(VERSION_KEY);
        } catch (JSONException e) {
            return VERSION_NO;
        }
    }

    public void setVersionNo(String version) {
        this.getConfigFile().put(VERSION_KEY, version);
        this.saveConfig();
    }

    // @@author A0135788M
    @Override
    public String getScheduleFilePath() {
        try {
            return this.getConfigFile().getString(SCHEDULE_PATH_KEY);
        } catch (JSONException e) {
            return DEFAULT_FILE_PATH + SCHEDULE_FILENAME;
        }
    }

    @Override
    public void setScheduleFilePath(String path) {
        this.getConfigFile().put(SCHEDULE_PATH_KEY, path);
        this.saveConfig();
        this.addScheduleLocations(path);
    }

    @Override
    public boolean hasConfigFile() {
        return Files.exists(configFilePath);
    }

    // @@author A0144915A
    @Override
    public Collection<String> getAliases(String triggerWord) {
        JSONObject configFile = this.getConfigFile();
        JSONObject aliasesMap;

        if (configFile.has(ALIASES_KEY)) {
            aliasesMap = configFile.getJSONObject(ALIASES_KEY);
        } else {
            aliasesMap = new JSONObject();
        }

        ArrayList<String> output = new ArrayList<>();
        if (aliasesMap.has(triggerWord)) {
            JSONArray triggerWords = aliasesMap.getJSONArray(triggerWord);
            for (int i = 0; i < triggerWords.length(); i++) {
                try {
                    output.add(triggerWords.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return output;
    }

    @Override
    public void setAliases(String triggerWord, Collection<String> aliases) {
        JSONObject aliasesMap;

        if (this.configFile.has(ALIASES_KEY)) {
            aliasesMap = this.configFile.getJSONObject(ALIASES_KEY);
        } else {
            aliasesMap = new JSONObject();
        }

        aliasesMap.put(triggerWord, aliases);
        this.configFile.put(ALIASES_KEY, aliasesMap);

        this.saveConfig();
    }

    // @@author A0127694U
    @Override
    public Collection<String> getScheduleLocations() {
        return this.scheduleLocations;
    }

    private void addScheduleLocations(String newLocation) {
        File temp = new File(newLocation);
        try {
            newLocation = temp.getCanonicalPath();
        } catch (IOException ioe) {
            throwAlert("File Path Error", "Could not access file at: \n" + newLocation);
            return;
        }

        ArrayList<String> scheduleLocations = this.scheduleLocations;

        if (scheduleLocations.contains(newLocation)) {
            return;
        }

        scheduleLocations.add(newLocation);
        this.configFile.put(SCHEDULE_LOCATIONS_KEY, scheduleLocations);

        this.scheduleLocations = scheduleLocations;
        this.saveConfig();
    }

    // @@author A0135788M
    private Alert throwAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        return alert;
    }

    // @@author A0144915A
    private JSONObject getConfigFile() {
        if (this.configFile == null) {
            this.configFile = new JSONObject();

            try {
                JSONTokener tokener = new JSONTokener(new FileReader(configFilePath.toString()));
                this.configFile = new JSONObject(tokener);
            } catch (IOException e) {
                throwAlert("Reading File Error", "Could not read file at: \n" + configFilePath.toString());
            } catch (JSONException e) {
                throwAlert("Parsing Error", "Could not parse file at: \n" + configFilePath.toString());
            }
        }

        return this.configFile;
    }

    private void saveConfig() {
        try {
            FileWriter file = new FileWriter(configFilePath.toString());
            file.write(configFile.toString());
            file.flush();
            file.close();
        } catch (IOException i) {
            throwAlert("Creating File Error", "Could not create file at: \n" + configFilePath.toString());
        }
    }
}
