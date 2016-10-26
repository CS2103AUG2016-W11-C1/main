package linenux.model.adapted;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import linenux.model.Reminder;
import linenux.storage.LocalDateTimeAdapter;

//@@author A0135788M
@XmlRootElement(name = "Reminder")
@XmlType(propOrder = { "note", "timeOfReminder" })
public class AdaptedReminder {
    @XmlElement(name = "note")
    private String note;

    @XmlElement(name = "timeOfReminder")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime timeOfReminder;

    public AdaptedReminder() {
    }

    public AdaptedReminder convertToXml(Reminder r) {
        this.note = r.getNote();
        this.timeOfReminder = r.getTimeOfReminder();
        return this;
    }

    public Reminder convertToModel() {
        return new Reminder(note, timeOfReminder);
    }
}
