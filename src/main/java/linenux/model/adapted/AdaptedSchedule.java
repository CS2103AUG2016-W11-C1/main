package linenux.model.adapted;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import linenux.model.Schedule;

//@@author A0135788M
@XmlRootElement(name = "Schedule")
public class AdaptedSchedule {
    @XmlElement(name = "state")
    private AdaptedState state;

    /**
     * Empty constructor for XML use.
     */
    public AdaptedSchedule() {
    }

    /**
     * Converts a schedule to an XML-friendly schedule object.
     * @param r A {@code schedule} instance.
     * @return an XML-friendly schedule object.
     */
    public AdaptedSchedule convertToXml(Schedule sch) {
        this.state = new AdaptedState();
        this.state.convertToXml(sch.getStates().get(sch.getStates().size() - 1));
        return this;
    }

    /**
     * Converts an XML-friendly schedule object to a schedule.
     * @return a schedule object.
     */
    public Schedule convertToModel() {
        return new Schedule(state.convertToModel());
    }
}
