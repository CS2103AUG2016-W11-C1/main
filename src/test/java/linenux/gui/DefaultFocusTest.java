package linenux.gui;

import org.junit.Test;

import static linenux.helpers.GuiMatchers.isFocused;
import static org.testfx.api.FxAssert.verifyThat;

/**
 * Created by yihangho on 10/18/16.
 */
public class DefaultFocusTest extends GuiTest {
    @Test
    public void testCommandBoxDefaultFocus() {
        verifyThat("#textField", isFocused());
    }
}
