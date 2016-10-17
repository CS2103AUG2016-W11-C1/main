package linenux.gui;

import javafx.scene.control.ListCell;
import linenux.Main;
import linenux.model.Task;
import linenux.view.MainWindowController;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import static linenux.helpers.GuiMatchers.hasCellLabelled;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.ListViewMatchers.hasItems;
import static org.testfx.matcher.control.ListViewMatchers.isEmpty;

/**
 * Created by yihangho on 10/17/16.
 */
public class AddTaskTest {
    private FxRobot robot;

    @Before
    public void setup() throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(Main.class);
        FxToolkit.showStage();
        this.robot = new FxRobot();
    }

    @Test
    public void testAddTodo() {
        verifyThat("#todosList", isEmpty());
        verifyThat("#deadlinesList", isEmpty());
        verifyThat("#eventsList", isEmpty());

        robot.clickOn("#textField");
        robot.write("add hello\n");

        verifyThat("#todosList", hasItems(1));
        verifyThat("#todosList", hasCellLabelled("hello"));
        verifyThat("#deadlinesList", isEmpty());
        verifyThat("#eventsList", isEmpty());
    }

    @Test
    public void testMarkTodoAsDone() {
        robot.clickOn("#textField");
        robot.write("add hello\n");
        robot.write("add world\n");

        verifyThat("#todosList", hasItems(2));
        verifyThat("#todosList", hasCellLabelled("hello"));
        verifyThat("#todosList", hasCellLabelled("world"));

        robot.write("done hello\n");

        verifyThat("#todosList", hasItems(1));
        verifyThat("#todosList", hasCellLabelled("world"));
    }

    @Test
    public void testAddDeadline() {
        verifyThat("#todosList", isEmpty());
        verifyThat("#deadlinesList", isEmpty());
        verifyThat("#eventsList", isEmpty());

        robot.clickOn("#textField");
        robot.write("add deadline et/2016-01-01 5:00PM\n");

        verifyThat("#todosList", isEmpty());
        verifyThat("#deadlinesList", hasItems(1));
        verifyThat("#deadlinesList", hasCellLabelled("deadline (Due 2016-01-01 5:00PM)"));
        verifyThat("#eventsList", isEmpty());
    }

    @Test
    public void testMarkDeadlineAsDone() {
        robot.clickOn("#textField");
        robot.write("add deadline et/2016-01-01 5:00PM\n");
        robot.write("add another et/2016-02-01 5:00PM\n");

        verifyThat("#deadlinesList", hasItems(2));

        robot.write("done deadline\n");

        verifyThat("#deadlinesList", hasItems(1));
        verifyThat("#deadlinesList", hasCellLabelled("another (Due 2016-02-01 5:00PM)"));
    }

    @Test
    public void testAddEvent() {
        verifyThat("#todosList", isEmpty());
        verifyThat("#deadlinesList", isEmpty());
        verifyThat("#eventsList", isEmpty());

        robot.clickOn("#textField");
        robot.write("add event st/2016-01-01 5:00PM et/2016-01-01 7:00PM\n");

        verifyThat("#todosList", isEmpty());
        verifyThat("#deadlinesList", isEmpty());
        verifyThat("#eventsList", hasItems(1));
        verifyThat("#eventsList", hasCellLabelled("event (2016-01-01 5:00PM - 2016-01-01 7:00PM)"));
    }
}
