package linenux.gui;

import org.junit.Test;

import static linenux.helpers.GuiMatchers.isFocused;
import static org.testfx.api.FxAssert.verifyThat;

//@@author A0144915A
public class DefaultFocusTest extends GuiTest {
    @Test
    public void testCommandBoxDefaultFocus() {
        verifyThat("#textField", isFocused());
    }
}
