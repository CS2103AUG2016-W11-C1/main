package linenux.gui;

import static linenux.helpers.GuiMatchers.isFocused;
import static org.testfx.api.FxAssert.verifyThat;

import org.junit.Test;

//@@author A0140702X
public class DefaultFocusTest extends GuiTest {
    @Test
    public void commandBox_checkDefaultFocus_commandBoxFocused() {
        verifyThat("#textField", isFocused());
    }
}
