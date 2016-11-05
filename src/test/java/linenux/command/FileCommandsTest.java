package linenux.command;

import linenux.config.Config;
import linenux.control.ControlUnit;
import linenux.model.Schedule;
import linenux.storage.ScheduleStorage;
import org.junit.Before;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

//@@author A0144915A
abstract public class FileCommandsTest {
    protected Path tempDir;
    protected ScheduleStorage storage;
    protected Config config;
    protected ControlUnit controlUnit;
    protected SaveCommand saveCommand;

    @Before
    public void setupTestEnvironment() throws Exception {
        this.tempDir = Files.createTempDirectory("tmp").toAbsolutePath();
        this.storage = new MockStorage();
        this.config = new MockConfig();
        this.controlUnit = new ControlUnit(this.storage, this.config, null);
        this.saveCommand = new SaveCommand(this.controlUnit, this.tempDir);
    }

    protected static class MockStorage implements ScheduleStorage {
        @Override
        public Schedule loadScheduleFromFile() {
            return null;
        }

        @Override
        public void saveScheduleToFile(Schedule schedule) {
        }

        @Override
        public boolean hasScheduleFile() {
            return true;
        }
    }

    protected static class MockConfig implements Config {
        private String scheduleFilePath = "existingPath";

        @Override
        public String getVersionNo() {
            return "test";
        }

        @Override
        public String getScheduleFilePath() {
            return this.scheduleFilePath;
        }

        @Override
        public void setScheduleFilePath(String path) {
            this.scheduleFilePath = path;
        }

        @Override
        public boolean hasConfigFile() {
            return true;
        }

        @Override
        public Collection<String> getAliases(String triggerWords) {
            return null;
        }

        @Override
        public void setAliases(String triggerWord, Collection<String> aliases) {

        }
    }
}
