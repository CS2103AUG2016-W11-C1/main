package linenux.view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import linenux.control.ControlUnit;
import linenux.model.Reminder;
import linenux.model.State;
import linenux.model.Task;
import linenux.util.ArrayListUtil;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;

//@@author A0135788M
/**
 * Controller for expandable result box, which is used to show longer command results.
 */
public class ExpandableResultBoxController {
    public enum UserAction {
        SHOW, HIDE
    }

    @FXML
    TextArea textArea;

    private BooleanProperty expanded = new SimpleBooleanProperty(false);
    private ControlUnit controlUnit;
    private boolean isShowingReminders = true;
    private UserAction lastUserAction = UserAction.HIDE;
    private Clock clock = Clock.systemDefaultZone();
    private boolean isShowingFilteredTasks = false;

    /**
     * Update the application {@code ControlUnit}.
     * @param controlUnit The new {@code ControlUnit}.
     */
    public void setControlUnit(ControlUnit controlUnit) {
        this.controlUnit = controlUnit;
        this.controlUnit.getLastCommandResultProperty().addListener((change) -> {
            this.setText(this.controlUnit.getLastCommandResultProperty().get().getFeedback());
        });
        this.renderInitialReminders();
        this.controlUnit.getSchedule().getStates().addListener((ListChangeListener<? super State>) change -> {
            this.onTaskListChange();
        });
        this.controlUnit.getSchedule().getFilteredTaskList().addListener((ListChangeListener<? super ArrayList<Task>>) (change) -> {
            this.onFilteredTaskListChange();
        });
    }

    /**
     * Initializes subviews.
     */
    @FXML
    private void initialize() {
        this.expanded.addListener(change -> {
            this.render();
        });
    }

    /**
     * Show the result box if it's currently hidden, hide otherwise.
     */
    @FXML
    private void toggleExpandedState() {
        if (this.expanded.get()) {
            this.lastUserAction = UserAction.HIDE;
            this.expanded.set(false);
        } else {
            this.lastUserAction = UserAction.SHOW;
            this.expanded.set(true);
        }
    }

    /**
     * Ensure that the UI is in the correct state.
     */
    private void render() {
        if (this.expanded.get()) {
            this.textArea.setPrefHeight(200);
        } else {
            this.textArea.setPrefHeight(0);
        }
    }

    /**
     * Render {@code text} on screen.
     * @param text The {@code String} to show.
     */
    private void setText(String text) {
        if (text.trim().contains("\n")) {
            this.isShowingReminders = false;
            this.expanded.set(true);
            this.textArea.setText(text.trim());
        } else {
            if (this.lastUserAction == UserAction.HIDE) {
                this.expanded.set(false);
            }
            this.isShowingReminders = true;
            this.renderReminders();
        }
    }

    private void onTaskListChange() {
        ArrayList<Task> tasks = this.controlUnit.getSchedule().getTaskList();
        ArrayList<Task> undoneTasks = new ArrayListUtil.ChainableArrayListUtil<>(tasks)
                .filter(Task::isNotDone)
                .value();

        this.isShowingFilteredTasks = false;

        if (this.isShowingReminders) {
            this.renderReminders(undoneTasks);
        }
    }

    /**
     * Callback when filtered task changes.
     */
    private void onFilteredTaskListChange() {
        this.isShowingFilteredTasks = true;
        if (this.isShowingReminders) {
            this.renderReminders(this.controlUnit.getSchedule().getFilteredTasks());
        }
    }

    /**
     * Render the initial set of reminders.
     */
    private void renderInitialReminders() {
        String formattedReminders = this.formatReminders(this.controlUnit.getSchedule().getTaskList());

        if (!formattedReminders.isEmpty()) {
            this.lastUserAction = UserAction.SHOW;
            this.isShowingReminders = true;
            this.expanded.set(true);
            this.renderReminders(this.controlUnit.getSchedule().getTaskList());
        }
    }

    /**
     * @return Format the reminders that will be shown.
     */
    private String formatReminders(ArrayList<Task> tasks) {
        LocalDateTime now = LocalDateTime.now(this.clock);
        LocalDateTime today = now.withHour(0).withMinute(0).withSecond(0);

        ArrayList<Reminder> reminders = new ArrayListUtil.ChainableArrayListUtil<>(tasks)
                .map(Task::getReminders)
                .foldr((x, xs) -> {
                    xs.addAll(x);
                    return xs;
                }, new ArrayList<Reminder>())
                .filter(reminder -> today.compareTo(reminder.getTimeOfReminder()) <= 0)
                .sortBy(Reminder::getTimeOfReminder)
                .value();

        return ArrayListUtil.display(reminders);
    }

    /**
     * Display the reminders on screen.
     */
    private void renderReminders() {
        if (this.isShowingFilteredTasks) {
            this.onFilteredTaskListChange();
        } else {
            this.onTaskListChange();
        }
    }

    private void renderReminders(ArrayList<Task> tasks) {
        this.textArea.setText(formatReminders(tasks));
    }
}
