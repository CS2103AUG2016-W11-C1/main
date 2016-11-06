package linenux.config;

import linenux.util.ArrayListUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

//@@author A0135788M
public class JsonConfigTest {
    private Path tempDir;
    private Path jsonPath;
    private Path xmlPath;
    private Config config;

    @Before
    public void setupTestEnvironment() throws Exception {
        this.tempDir = Files.createTempDirectory("tmp");
        this.jsonPath = this.tempDir.resolve("config.json");
        this.xmlPath = this.tempDir.resolve("schedule.xml");
        this.reinitializeConfig();
    }

    private void reinitializeConfig() {
        this.config = new JsonConfig("test", this.jsonPath.toString(), this.xmlPath.toString());
    }

    @After
    public void teardown() {
        new File(this.jsonPath.toString()).delete();
        new File(this.tempDir.toString()).delete();
    }

    @Test
    public void saveAndLoadScheduleFilePath() {
        this.config.setScheduleFilePath("hello.xml");
        this.reinitializeConfig();
        assertEquals("hello.xml", this.config.getScheduleFilePath());
    }

    @Test
    public void saveAndLoadAliases() {
        this.config.setAliases("hello", ArrayListUtil.fromArray(new String[] {"hello", "foo", "bar"}));
        this.reinitializeConfig();
        Collection<String> aliases = this.config.getAliases("hello");
        assertTrue(aliases.contains("hello"));
        assertTrue(aliases.contains("foo"));
        assertTrue(aliases.contains("bar"));
    }
}
