package linenux.gui;

import javafx.scene.input.KeyCode;
import org.junit.Test;

import static linenux.helpers.GuiMatchers.textFieldHasText;
import static org.testfx.api.FxAssert.verifyThat;

//@@author A0127694U
public class CommandBoxCaretPosition extends GuiTest {
    @Test
    public void testLeftAndRight() {
        robot.clickOn("#textField");
        robot.write("add hello");

        for (int i = 0; i < 5; i++) {
            robot.pressAndRelease(KeyCode.LEFT);
        }

        robot.write("b");

        verifyThat("#textField", textFieldHasText("add bhello"));
    }

    @Test
    public void testUpHistory() {
        robot.clickOn("#textField");
        robot.write("add hello\n");
        robot.pressAndRelease(KeyCode.UP);
        robot.write("ooo");
        verifyThat("#textField", textFieldHasText("add helloooo"));
    }

    @Test
    public void testDownHistory() {
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
    public void testAutoComplete() {
        robot.clickOn("#textField");
        robot.write("a");
        robot.pressAndRelease(KeyCode.TAB);
        robot.write(" hello");
        verifyThat("#textField", textFieldHasText("add hello"));
    }
}
