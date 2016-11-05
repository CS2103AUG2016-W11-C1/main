package linenux.config;

import java.util.Collection;

public interface Config {
    /**
     * @return The version of the program that created the config file.
     */
    public String getVersionNo();

    /**
     * @return The absolute path to the current schedule file.
     */
    public String getScheduleFilePath();

    /**
     * @param path The absolute path to the new schedule file.
     */
    public void setScheduleFilePath(String path);

    /**
     * @return Whether or not the config file is present.
     */
    public boolean hasConfigFile();

    /**
     * @param triggerWord The original trigger word of a command.
     * @return A {@code Collection} of aliases for that command.
     */
    public Collection<String> getAliases(String triggerWord);

    /**
     * @param triggerWord The original trigger word of a command.
     * @param aliases A {@code Collection} of aliases for that command.
     */
    public void setAliases(String triggerWord, Collection<String> aliases);
}
