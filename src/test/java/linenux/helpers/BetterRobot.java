package linenux.helpers;

import javafx.scene.input.KeyCode;
import org.testfx.api.FxRobot;

//@@author A0144915A
public class BetterRobot extends FxRobot {
    public void pressAndRelease(KeyCode key) {
        this.press(key);
        this.release(key);
    }
}
