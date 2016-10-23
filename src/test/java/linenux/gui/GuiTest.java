package linenux.gui;

import org.junit.Before;
import org.testfx.api.FxToolkit;

import linenux.Main;
import linenux.helpers.BetterRobot;

/**
 * Created by yihangho on 10/18/16.
 */
public abstract class GuiTest {
    protected BetterRobot robot;

    @Before
    public void setup() throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(Main.class, "test");
        FxToolkit.showStage();
        this.robot = new BetterRobot();
    }
}
