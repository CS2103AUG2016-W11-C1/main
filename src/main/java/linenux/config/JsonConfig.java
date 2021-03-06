package linenux.config;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import linenux.util.LogsCenter;
import linenux.util.ThrowableUtil;
import linenux.view.Alerts;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

//@@author A0135788M
/**
 * A JSON-backed config file.
 */
public class JsonConfig implements Config{
    public static final String VERSION_NO = "v0.5";
    public static final String DEFAULT_FILE_PATH = Paths.get(".").toAbsolutePath().toString();
    public static final String CONFIG_FILENAME = "Config.json";
    public static final String SCHEDULE_FILENAME = "Schedule.xml";

    private static final String VERSION_KEY = "versionNo";
    private static final String SCHEDULE_PATH_KEY = "schedulePath";
    private static final String ALIASES_KEY = "aliases";
    private static final Logger logger = LogsCenter.getLogger(JsonConfig.class);

    private Path configFilePath;
    private Path scheduleFilePath;
    private String verNo;

    private JSONObject configFile;

    public JsonConfig() {
        this(VERSION_NO, DEFAULT_FILE_PATH + CONFIG_FILENAME, DEFAULT_FILE_PATH + SCHEDULE_FILENAME);
    }

    public JsonConfig(String verNo, String configFilePath, String scheduleFilePath) {
        this.verNo = verNo;
        this.configFilePath = Paths.get(configFilePath);
        this.scheduleFilePath = Paths.get(scheduleFilePath);
        initialize();
    }

    /**
     * Initializes configuration file with default values;
     */
    private void initialize() {
        logger.info("Initializing config");
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
            logger.info("Initializing default schedule path");
            configFile.put(SCHEDULE_PATH_KEY, scheduleFilePath.toString());
        }

        this.configFile = configFile;
        this.saveConfig();
        logger.info("Done initializing config");
    }

    /**
     * @return The version of the program that created the config file.
     */
    @Override
    public String getVersionNo() {
        try {
            return this.getConfigFile().getString(VERSION_KEY);
        } catch (JSONException e) {
            return VERSION_NO;
        }
    }

    /**
     * @return The absolute path to the current schedule file.
     */
    @Override
    public String getScheduleFilePath() {
        try {
            return this.getConfigFile().getString(SCHEDULE_PATH_KEY);
        } catch (JSONException e) {
            return DEFAULT_FILE_PATH + SCHEDULE_FILENAME;
        }
    }

    /**
     * @param path The absolute path to the new schedule file.
     */
    @Override
    public void setScheduleFilePath(String path) {
        this.getConfigFile().put(SCHEDULE_PATH_KEY, path);
        this.saveConfig();
    }

    /**
     * @return Whether or not the config file is present.
     */
    @Override
    public boolean hasConfigFile() {
        return Files.exists(configFilePath);
    }

    /**
     * @param triggerWord The original trigger word of a command.
     * @return A {@code Collection} of aliases for that command.
     */
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

    /**
     * @param triggerWord The original trigger word of a command.
     * @param aliases A {@code Collection} of aliases for that command.
     */
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

    /**
     * Lazily instantiate the {@code configFile} object.
     * @return A {@code JSONObject} representing the config.
     */
    private JSONObject getConfigFile() {
        if (this.configFile == null) {
            this.configFile = new JSONObject();

            try {
                JSONTokener tokener = new JSONTokener(new FileReader(configFilePath.toString()));
                this.configFile = new JSONObject(tokener);
            } catch (IOException e) {
                Alerts.alertAndDie("Error Reading Config File", "Config file is not readable at\n" + configFilePath.toString());
            } catch (JSONException e) {
                boolean recover = Alerts.alertAndConfirm("Error Parsing Config File", "Config file cannot be parsed at\n" + configFilePath + "\nOverwrite with defaults?");
                if (!recover) {
                    System.exit(1);
                }
            }
        }

        return this.configFile;
    }

    /**
     * Save the config into a JSON file.
     */
    private void saveConfig() {
        logger.info("Saving config");
        try {
            FileWriter file = new FileWriter(configFilePath.toString());
            file.write(configFile.toString());
            file.flush();
            file.close();
        } catch (IOException i) {
            logger.severe(ThrowableUtil.getStackTrace(i));
            Alerts.alertAndDie("Error Saving Config File", "Config file cannot be saved at " + configFilePath.toString());
        }
        logger.info("Done saving config");
    }
}
