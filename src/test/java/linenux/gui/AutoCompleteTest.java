package linenux.gui;

import static linenux.helpers.GuiMatchers.textFieldHasText;
import static org.testfx.api.FxAssert.verifyThat;

import org.junit.Test;

import javafx.scene.input.KeyCode;

/**
 * JUnit test for autocomplete.
 */
//@@author A0135788M
public class AutoCompleteTest extends GuiTest {

    /**
     * Test that tab gives correct suggestion.
     */
    @Test
    public void testTab() {
        robot.write("to");
        robot.pressAndRelease(KeyCode.TAB);
        verifyThat("#textField", textFieldHasText("today"));
        robot.pressAndRelease(KeyCode.TAB);
        verifyThat("#textField", textFieldHasText("tomorrow"));
        robot.pressAndRelease(KeyCode.ENTER);

        robot.write("alias tomorrow tom");
        robot.pressAndRelease(KeyCode.ENTER);

        robot.write("to");
        robot.pressAndRelease(KeyCode.TAB);
        verifyThat("#textField", textFieldHasText("today"));
        robot.pressAndRelease(KeyCode.TAB);
        verifyThat("#textField", textFieldHasText("tom"));
        robot.pressAndRelease(KeyCode.TAB);
        verifyThat("#textField", textFieldHasText("tomorrow"));
    }

}
