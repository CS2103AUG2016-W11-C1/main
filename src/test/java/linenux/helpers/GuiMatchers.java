package linenux.helpers;

import javafx.scene.Node;
import javafx.scene.control.Cell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import linenux.model.Task;
import org.hamcrest.Matcher;
import org.testfx.api.FxAssert;
import org.testfx.service.finder.NodeFinder;
import org.testfx.service.query.NodeQuery;

import static org.testfx.matcher.base.GeneralMatchers.baseMatcher;
import static org.testfx.matcher.base.GeneralMatchers.typeSafeMatcher;

//@@author A0144915A
public class GuiMatchers {
    public static Matcher<Node> hasCellLabelled(String label) {
        return typeSafeMatcher(ListView.class, label, node -> listHasCellLabelled(node, label));
    }

    public static Matcher<Node> hasTodoCell(String taskName) {
        return typeSafeMatcher(ListView.class, taskName, node -> listHasTodoCell(node, taskName));
    }

    private static boolean listHasCellLabelled(ListView node, String label) {
        NodeFinder finder = FxAssert.assertContext().getNodeFinder();
        NodeQuery query = finder.from(node);
        return query.lookup(".list-cell")
                .<Cell>match(cell -> !cell.isEmpty() && cell.getText().equals(label))
                .tryQuery()
                .isPresent();
    }

    private static boolean listHasTodoCell(ListView node, String name) {
        NodeFinder finder = FxAssert.assertContext().getNodeFinder();
        NodeQuery query = finder.from(node);
        return query.lookup(".list-cell")
                .<Cell>match(cell -> {
                    if (cell.isEmpty() || cell.getItem() == null) {
                        return false;
                    } else {
                        Task todo = (Task) cell.getItem();
                        return todo.getTaskName().equals(name);
                    }
                })
                .tryQuery()
                .isPresent();
    }

    public static Matcher<Node> isFocused() {
        return baseMatcher("is focused", node -> node.focusedProperty().get());
    }

    public static Matcher<Node> textFieldHasText(String text) {
        return typeSafeMatcher(TextField.class, text, node -> node.getText().equals(text));
    }
}
