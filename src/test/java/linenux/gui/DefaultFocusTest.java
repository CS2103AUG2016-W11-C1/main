package linenux.gui;

import linenux.Main;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import static linenux.helpers.GuiMatchers.isFocused;
import static org.testfx.api.FxAssert.verifyThat;

/**
 * Created by yihangho on 10/18/16.
 */
public class DefaultFocusTest {
    private FxRobot robot;

    @Before
    public void setup() throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(Main.class);
        FxToolkit.showStage();
        this.robot = new FxRobot();
    }

    @Test
    public void testCommandBoxDefaultFocus() {
        verifyThat("#textField", isFocused());
    }
}
