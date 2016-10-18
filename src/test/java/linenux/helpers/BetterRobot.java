package linenux.helpers;

import javafx.scene.input.KeyCode;
import org.testfx.api.FxRobot;

/**
 * Created by yihangho on 10/18/16.
 */
public class BetterRobot extends FxRobot {
    public void pressAndRelease(KeyCode key) {
        this.press(key);
        this.release(key);
    }
}
