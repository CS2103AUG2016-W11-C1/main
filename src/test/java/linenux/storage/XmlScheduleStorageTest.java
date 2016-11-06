package linenux.storage;

import linenux.config.Config;
import linenux.model.Reminder;
import linenux.model.Schedule;
import linenux.model.Task;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

//@@author A0135788M
public class XmlScheduleStorageTest {
    private Path tempDir;
    private Path xmlPath;
    private XmlScheduleStorage storage;

    @Before
    public void setupTestEnvironment() throws Exception {
        this.tempDir = Files.createTempDirectory("tmp");
        this.xmlPath = this.tempDir.resolve("hello.xml");

        Config config = new MockConfig(this.xmlPath.toString());
        this.storage = new XmlScheduleStorage(config);
    }

    @After
    public void teardown() {
        new File(this.xmlPath.toString()).delete();
        new File(this.tempDir.toString()).delete();
    }

    @Test
    public void saveAndLoad_normalTasks() {
        Schedule schedule = new Schedule();

        schedule.addTask(new Task("todo").addReminder(new Reminder("note", LocalDateTime.of(2016, 1, 1, 12, 00))));
        schedule.addTask(new Task("deadline", LocalDateTime.of(2016, 1, 1, 17, 00)));
        schedule.addTask(new Task("event", LocalDateTime.of(2016, 1, 1, 17,00), LocalDateTime.of(2016, 1, 2, 17, 00)));

        this.storage.saveScheduleToFile(schedule);

        File f = new File(this.xmlPath.toString());
        assertTrue(f.exists());

        Schedule loadedSchedule = this.storage.loadScheduleFromFile();
        ArrayList<Task> loadedTasks = loadedSchedule.getTaskList();
        assertEquals(3, loadedTasks.size());

        Task todo = loadedTasks.get(0);
        assertTrue(todo.isTodo());
        assertEquals("todo", todo.getTaskName());
        assertEquals(1, todo.getReminders().size());
        assertEquals("note", todo.getReminders().get(0).getNote());
        assertEquals(LocalDateTime.of(2016, 1, 1, 12, 00), todo.getReminders().get(0).getTimeOfReminder());

        Task deadline = loadedTasks.get(1);
        assertTrue(deadline.isDeadline());
        assertEquals("deadline", deadline.getTaskName());
        assertEquals(LocalDateTime.of(2016, 1, 1, 17, 00), deadline.getEndTime());

        Task event = loadedTasks.get(2);
        assertTrue(event.isEvent());
        assertEquals("event", event.getTaskName());
        assertEquals(LocalDateTime.of(2016, 1, 1, 17, 00), event.getStartTime());
        assertEquals(LocalDateTime.of(2016, 1, 2, 17, 00), event.getEndTime());
    }

    private static class MockConfig implements Config {
        private String scheduleFilePath;

        public MockConfig(String scheduleFilePath) {
            this.scheduleFilePath = scheduleFilePath;
        }

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
        public Collection<String> getAliases(String triggerWord) {
            return null;
        }

        @Override
        public void setAliases(String triggerWord, Collection<String> aliases) {
        }
    }
}
