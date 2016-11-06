package linenux.helpers;

import static org.testfx.matcher.base.GeneralMatchers.baseMatcher;
import static org.testfx.matcher.base.GeneralMatchers.typeSafeMatcher;

import org.hamcrest.Matcher;
import org.testfx.api.FxAssert;
import org.testfx.service.finder.NodeFinder;
import org.testfx.service.query.NodeQuery;

import javafx.scene.Node;
import javafx.scene.control.Cell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import linenux.model.Task;
import linenux.util.LocalDateTimeUtil;

//@@author A0144915A
/**
 * Class to check if information is displayed correctly.
 */
public class GuiMatchers {

    /**
     * Checks if list contains task with correct title.
     * @param title
     * @return
     */
    public static Matcher<Node> hasCellTitle(String title) {
        return typeSafeMatcher(ListView.class, title, node -> listHasCellTitle(node, title));
    }

    /**
     * Checks if list contains task with correct time.
     * @param time
     * @return
     */
    //@@A0135788M
    public static Matcher<Node> hasCellTime(String time) {
        return typeSafeMatcher(ListView.class, time, node -> listHasCellTime(node, time));
    }

    /**
     * Check if ListView node has the specified title.
     *
     * @param node
     *            The node to check.
     * @param title
     *            The specified title.
     * @return True if the node has the specified title, false otherwise.
     */
    private static boolean listHasCellTitle(ListView node, String title) {
        NodeFinder finder = FxAssert.assertContext().getNodeFinder();
        NodeQuery query = finder.from(node);
        return query.lookup(".list-cell")
                .<Cell>match(cell -> {
                    if (cell.isEmpty() || cell.getItem() == null) {
                        return false;
                    } else {
                        Task todo = (Task) cell.getItem();
                        return todo.getTaskName().equals(title);
                    }
                })
                .tryQuery()
                .isPresent();
    }

    /**
     * Check if the ListView node has the specified time.
     *
     * @param node
     *            The node to check.
     * @param time
     *            The specified time.
     * @return True if the node has the specified time, false otherwise.
     */
    private static boolean listHasCellTime(ListView node, String time) {
        NodeFinder finder = FxAssert.assertContext().getNodeFinder();
        NodeQuery query = finder.from(node);
        return query.lookup(".list-cell")
                .<Cell>match(cell -> {
                    if (cell.isEmpty() || cell.getItem() == null) {
                        return false;
                    } else {
                        Task task = (Task) cell.getItem();
                        if (task.isDeadline()) {
                            String result = "Due " + LocalDateTimeUtil.toString(task.getEndTime());
                            return result.equals(time);
                        } else if (task.isEvent()) {
                            String result = "From " + LocalDateTimeUtil.toString(task.getStartTime()) + " to "
                                    + LocalDateTimeUtil.toString(task.getEndTime());
                            return result.equals(time);
                        }
                        return false;
                    }
                })
                .tryQuery()
                .isPresent();
    }

    /**
     * Returns the current selected {@code Node}.
     *
     * @return
     */
    public static Matcher<Node> isFocused() {
        return baseMatcher("is focused", node -> node.focusedProperty().get());
    }

    /**
     * Returns the node with the specified text.
     *
     * @param text
     *            The specified text.
     * @return
     */
    public static Matcher<Node> textFieldHasText(String text) {
        return typeSafeMatcher(TextField.class, text, node -> node.getText().equals(text));
    }
}
