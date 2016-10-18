package linenux.gui;

import javafx.scene.input.KeyCode;
import org.junit.Test;

import static linenux.helpers.GuiMatchers.textFieldHasText;
import static org.testfx.api.FxAssert.verifyThat;

/**
 * I notice some inconsistencies while running these tests. Not sure what's the problem.
 */
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
