package linenux.gui;

import javafx.scene.input.KeyCode;
import org.junit.Test;

import static linenux.helpers.GuiMatchers.textFieldHasText;
import static org.testfx.api.FxAssert.verifyThat;

//@@author A0127694U
public class CommandHistoryTest extends GuiTest {
    @Test
    public void testUpKeyPreviousCommand() {
        robot.write("add hello\n");
        robot.write("add world\n");

        robot.pressAndRelease(KeyCode.UP);
        verifyThat("#textField", textFieldHasText("add world"));

        robot.pressAndRelease(KeyCode.UP);
        verifyThat("#textField", textFieldHasText("add hello"));

        robot.pressAndRelease(KeyCode.DOWN);
        robot.sleep(1);
        verifyThat("#textField", textFieldHasText("add world"));

        robot.pressAndRelease(KeyCode.DOWN);
        robot.sleep(1);
        verifyThat("#textField", textFieldHasText(""));
    }
}
