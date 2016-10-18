package linenux.gui;

import linenux.Main;
import linenux.helpers.BetterRobot;
import org.junit.Before;
import org.testfx.api.FxToolkit;

/**
 * Created by yihangho on 10/18/16.
 */
public abstract class GuiTest {
    protected BetterRobot robot;

    @Before
    public void setup() throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(Main.class);
        FxToolkit.showStage();
        this.robot = new BetterRobot();
    }
}
