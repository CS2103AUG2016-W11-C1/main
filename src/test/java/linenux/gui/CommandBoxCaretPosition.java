package linenux.gui;

import static linenux.helpers.GuiMatchers.textFieldHasText;
import static org.testfx.api.FxAssert.verifyThat;

import org.junit.Test;

import javafx.scene.input.KeyCode;

//@@author A0144915A
public class CommandBoxCaretPosition extends GuiTest {
    @Test
    public void leftAndRightKey_moveCaretAndModifyText() {
        robot.clickOn("#textField");
        robot.write("add hello");

        for (int i = 0; i < 5; i++) {
            robot.pressAndRelease(KeyCode.LEFT);
        }

        robot.write("b");

        verifyThat("#textField", textFieldHasText("add bhello"));

        for (int i = 0; i < 5; i++) {
            robot.pressAndRelease(KeyCode.RIGHT);
        }

        robot.write("c");

        verifyThat("#textField", textFieldHasText("add bhelloc"));
    }

    @Test
    public void upKey_pressUpKeyAndModifyText() {
        robot.clickOn("#textField");
        robot.write("add hello\n");
        robot.pressAndRelease(KeyCode.UP);
        robot.write("ooo");
        verifyThat("#textField", textFieldHasText("add helloooo"));
    }

    @Test
    public void downKey_browseCommandHistoryAndModifyText() {
        robot.clickOn("#textField");
        robot.write("add hello\n");
        robot.write("add world\n");
        robot.pressAndRelease(KeyCode.UP);
        robot.pressAndRelease(KeyCode.UP);
        robot.pressAndRelease(KeyCode.DOWN);
        robot.write("ddd");
        verifyThat("#textField", textFieldHasText("add worldddd"));
    }

    @Test
    public void autoComplete_tabOnCommandWord_commandWordSuggested() {
        robot.clickOn("#textField");
        robot.write("a");
        robot.pressAndRelease(KeyCode.TAB);
        robot.write(" hello");
        verifyThat("#textField", textFieldHasText("add hello"));
    }
}
