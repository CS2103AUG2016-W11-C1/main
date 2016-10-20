package linenux.model.adapted;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import linenux.model.Reminder;
import linenux.storage.LocalDateTimeAdapter;

@XmlRootElement( name = "Reminder" )
@XmlType( propOrder = {"note", "timeOfReminder"} )
public class AdaptedReminder {
    @XmlElement( name = "note", required = true )
    private String note;

    @XmlElement( name = "timeOfReminder", required = true )
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime timeOfReminder;

    public AdaptedReminder() {}

    public void convertToXml(Reminder r) {
        this.note = r.getNote();
        this.timeOfReminder = r.getTimeOfReminder();
    }

    public Reminder convertToModel() {
        return new Reminder(this.note, this.timeOfReminder);
    }
}
