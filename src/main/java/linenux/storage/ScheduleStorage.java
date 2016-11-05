package linenux.storage;

import linenux.model.Schedule;

//@@author A0135788M
/**
 * Reads and writes Schedule to the file format.
 */
public interface ScheduleStorage {
    /**
     * Reads schedule from file.
     * @return a {@code Schedule} instance.
     */
    public Schedule loadScheduleFromFile();

    /**
     * Writes schedule to file type.
     * @param a {@code Schedule} instance to write to file type.
     */
    public void saveScheduleToFile(Schedule schedule);

    /**
     * Checks if file exist.
     * @return {@code Boolean} true if there is a schedule file of that file type.
     */
    public boolean hasScheduleFile();
}
