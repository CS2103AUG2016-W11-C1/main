package linenux.model.adapted;

import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import linenux.model.Reminder;
import linenux.model.Task;
import linenux.storage.LocalDateTimeAdapter;

@XmlRootElement(name = "Task")
@XmlType(propOrder = { "taskName", "isDone", "startTime", "endTime", "tags", "reminders" })
public class AdaptedTask {
    @XmlElement(name = "taskName")
    private String taskName;

    @XmlElement(name = "isDone")
    private boolean isDone;

    @XmlElement(name = "startTime", nillable = true)
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime startTime;

    @XmlElement(name = "endTime", nillable = true)
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime endTime;

    @XmlElement(name = "tags")
    @XmlElementWrapper
    private ArrayList<String> tags;

    @XmlElement(name = "reminders")
    @XmlElementWrapper
    private ArrayList<AdaptedReminder> reminders;

    public AdaptedTask() {
    }

    public AdaptedTask convertToXml(Task t) {
        this.taskName = t.getTaskName();
        this.isDone = t.isDone();
        this.startTime = t.getStartTime();
        this.endTime = t.getEndTime();
        this.tags = t.getTags();
        this.reminders = new ArrayList<>();
        for (Reminder r : t.getReminders()) {
            reminders.add(new AdaptedReminder().convertToXml(r));
        }
        return this;
    }

    public Task convertToModel() {
        ArrayList<Reminder> newReminders = new ArrayList<>();
        for (AdaptedReminder r : reminders) {
            newReminders.add(r.convertToModel());
        }
        return new Task(taskName, isDone, startTime, endTime, tags, newReminders);
    }
}
