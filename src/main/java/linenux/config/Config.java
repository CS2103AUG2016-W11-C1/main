package linenux.config;

import java.util.Collection;

public interface Config {
    public String getVersionNo();
    public String getScheduleFilePath();
    public void setScheduleFilePath(String path);
    public boolean hasConfigFile();
    public Collection<String> getAliases(String triggerWord);
    public void setAliases(String triggerWord, Collection<String> aliases);
}
