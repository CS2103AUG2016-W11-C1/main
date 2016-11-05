package linenux.gui;

import org.junit.Test;

import static linenux.helpers.GuiMatchers.isFocused;
import static org.testfx.api.FxAssert.verifyThat;

//@@author A0140702X
public class DefaultFocusTest extends GuiTest {
    @Test
    public void testCommandBoxDefaultFocus() {
        verifyThat("#textField", isFocused());
    }
}
