package linenux.gui;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.testfx.api.FxToolkit;

import linenux.Main;
import linenux.config.JsonConfig;
import linenux.helpers.BetterRobot;

//@@author A0144915A
public abstract class GuiTest {
    protected BetterRobot robot;
    private Path tempDir;

    @Before
    public void setupGuiTest() throws Exception {
        tempDir = Files.createTempDirectory("tmp");

        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(() -> {
            String version = "v0.0";
            String configPath = tempDir.resolve("config.json").toString();
            String schedulePath = tempDir.resolve("schedule.xml").toString();
            return new TestMain(version, configPath, schedulePath);
        });
        FxToolkit.showStage();
        this.robot = new BetterRobot();
    }

    @After
    public void teardownSetup() {
        tempDir.resolve("config.json").toFile().delete();
        tempDir.resolve("schedule.xml").toFile().delete();
        tempDir.toFile().delete();
    }

    public static class TestMain extends Main {
        public static void main(String args) {
            launch(args);
        }

        public TestMain(String ver, String configFilePath, String scheduleFilePath) {
            super();
            this.config = new JsonConfig(ver, configFilePath, scheduleFilePath);
        }
    }
}
