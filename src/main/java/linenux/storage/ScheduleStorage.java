package linenux.storage;

import linenux.model.Schedule;

//@@author A0135788M
/**
 * Reads and writes Schedule to the file format.
 */
public interface ScheduleStorage {
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
}
