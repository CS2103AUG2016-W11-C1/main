package linenux.model.adapted;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import linenux.model.Schedule;

//@@author A0135788M
@XmlRootElement(name = "Schedule")
public class AdaptedSchedule {
    @XmlElement(name = "state")
    private AdaptedState state;

    public AdaptedSchedule() {
    }

    public AdaptedSchedule convertToXml(Schedule sch) {
        this.state = new AdaptedState();
        this.state.convertToXml(sch.getStates().get(sch.getStates().size() - 1));
        return this;
    }

    public Schedule convertToModel() {
        return new Schedule(state.convertToModel());
    }
}
