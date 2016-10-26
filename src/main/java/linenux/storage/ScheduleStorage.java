package linenux.storage;

import java.nio.file.Paths;

import linenux.model.Schedule;

/**
 * Reads and writes Schedule to the file format.
 */
//@@author A0135788M
public interface ScheduleStorage {
    public static final String DEFAULT_FILE_PATH = Paths.get(".").toAbsolutePath().toString();
    public static final String FILENAME = "Schedule.xml";

    /**
     * Reads schedule from file.
     */
    public Schedule loadScheduleFromFile();

    /**
     * Writes schedule to file.
     */
    public void saveScheduleToFile(Schedule schedule);

    /**
     * Checks if file exist.
     */
    public boolean hasScheduleFile();

    /**
     * Set file path.
     */
    public void setFilePath(String filePath);
}
