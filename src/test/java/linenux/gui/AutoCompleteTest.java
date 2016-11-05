package linenux.gui;

import static linenux.helpers.GuiMatchers.textFieldHasText;
import static org.testfx.api.FxAssert.verifyThat;

import org.junit.Test;

import javafx.scene.input.KeyCode;

//@@author A0135788M
/**
 * JUnit test for autocomplete.
 */
public class AutoCompleteTest extends GuiTest {

    /**
     * Test that tab gives correct suggestion.
     */
    @Test
    public void tab_tabOnCommandWords_commandWordsSuggested() {
        robot.write("to");
        robot.pressAndRelease(KeyCode.TAB);
        verifyThat("#textField", textFieldHasText("today"));
        robot.pressAndRelease(KeyCode.TAB);
        verifyThat("#textField", textFieldHasText("tomorrow"));
        robot.pressAndRelease(KeyCode.ENTER);
    }

    /**
     * Test that tab gives suggestion for words added into the schedule
     */
    @Test
    public void tab_tabOnUnknownWord_wordLearnedAndSuggested() {
        robot.write("add zebra wat\n");
        robot.pressAndRelease(KeyCode.ENTER);

        robot.write("z");
        robot.pressAndRelease(KeyCode.TAB);
        verifyThat("#textField", textFieldHasText("zebra"));

        // to clear textField
        robot.pressAndRelease(KeyCode.ENTER);

        robot.write("w");
        robot.pressAndRelease(KeyCode.TAB);
        verifyThat("#textField", textFieldHasText("wat"));
    }

}
