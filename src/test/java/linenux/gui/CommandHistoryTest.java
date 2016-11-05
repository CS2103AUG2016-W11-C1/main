package linenux.gui;

import static linenux.helpers.GuiMatchers.textFieldHasText;
import static org.testfx.api.FxAssert.verifyThat;

import org.junit.Test;

import javafx.scene.input.KeyCode;

//@@author A0144915A
public class CommandHistoryTest extends GuiTest {
    @Test
    public void commandHistory_browseCommandHistory_commandHistoryShown() {
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
