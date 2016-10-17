package linenux.helpers;

import javafx.scene.Node;
import javafx.scene.control.Cell;
import javafx.scene.control.ListView;
import org.hamcrest.Matcher;
import org.testfx.api.FxAssert;
import org.testfx.service.finder.NodeFinder;
import org.testfx.service.query.NodeQuery;

import static org.testfx.matcher.base.GeneralMatchers.typeSafeMatcher;

/**
 * Created by yihangho on 10/17/16.
 */
public class GuiMatchers {
    public static Matcher<Node> hasCellLabelled(String label) {
        return typeSafeMatcher(ListView.class, label, node -> listHasCellLabelled(node, label));
    }

    private static boolean listHasCellLabelled(ListView node, String label) {
        NodeFinder finder = FxAssert.assertContext().getNodeFinder();
        NodeQuery query = finder.from(node);
        return query.lookup(".list-cell")
                .<Cell>match(cell -> !cell.isEmpty() && cell.getText().equals(label))
                .tryQuery()
                .isPresent();
    }
}
