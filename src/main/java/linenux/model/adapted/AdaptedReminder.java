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

    /**
     * Empty constructor for XML use.
     */
    public AdaptedReminder() {
    }

    /**
     * Converts a reminder to an XML-friendly reminder object.
     * @param r A {@code reminder} instance.
     * @return an XML-friendly reminder object.
     */
    public AdaptedReminder convertToXml(Reminder r) {
        this.note = r.getNote();
        this.timeOfReminder = r.getTimeOfReminder();
        return this;
    }

    /**
     * Converts an XML-friendly reminder object to a reminder.
     * @return a reminder object.
     */
    public Reminder convertToModel() {
        return new Reminder(note, timeOfReminder);
    }
}
