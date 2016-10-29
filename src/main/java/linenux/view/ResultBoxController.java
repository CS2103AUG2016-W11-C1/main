package linenux.view;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.function.Predicate;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import linenux.command.result.CommandResult;
import linenux.control.ControlUnit;
import linenux.model.Task;
import linenux.util.ArrayListUtil;

/**
 * Created by yihangho on 10/16/16.
 */
//@@author A0144915A
public class ResultBoxController {
    @FXML
    private TextArea commandResultTextArea;

    private ControlUnit controlUnit;
    private Clock clock = Clock.systemDefaultZone();

    public void setControlUnit(ControlUnit controlUnit) {
        this.controlUnit = controlUnit;

        this.controlUnit.getLastCommandResultProperty().addListener((change) -> {
            CommandResult lastResult = this.controlUnit.getLastCommandResultProperty().getValue();
            commandResultTextArea.setText(lastResult.getFeedback());
        });
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    // TODO Write tests for this when we can finally save the tasks.
    public void displayReminder() {
        ArrayList<String> reminders = new ArrayListUtil.ChainableArrayListUtil<>(this.controlUnit.getSchedule().getTaskList())
                .filter(((Predicate<Task>) Task::isDone).negate())
                .map(task -> remindersForTodayAsStrings(task))
                .foldr((x, xs) -> {
                    xs.addAll(x);
                    return xs;
                }, new ArrayList<String>())
                .reverse()
                .value();

        for (int i = 0; i < reminders.size(); i++) {
            String str = Integer.toString(i + 1) + ". " + reminders.get(i);
            reminders.set(i, str);
        }

        String displayString = new ArrayListUtil.ChainableArrayListUtil<>(reminders)
            .foldr(String::concat, "");

        commandResultTextArea.setText(displayString);
    }

    private boolean isToday(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now(this.clock);
        return now.getYear() == dateTime.getYear() && now.getDayOfYear() == dateTime.getDayOfYear();
    }

    private ArrayList<String> remindersForTodayAsStrings(Task task) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d h:mma");

        return new ArrayListUtil.ChainableArrayListUtil<>(task.getReminders())
                .filter(reminder -> isToday(reminder.getTimeOfReminder()))
                .map(reminder -> {
                    String output = task.getTaskName() + " - " + reminder.getTimeOfReminder().format(formatter) + "\n";

                    if (reminder.getNote() != null && reminder.getNote().length() > 0) {
                        output += "    " + reminder.getNote() + "\n";
                    }

                    return output;
                })
                .value();
    }
}
