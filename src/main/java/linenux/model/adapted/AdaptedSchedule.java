package linenux.model.adapted;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import linenux.model.Schedule;
import linenux.model.State;

@XmlRootElement(name = "Schedule")
public class AdaptedSchedule {
    @XmlElement(name = "states")
    private ArrayList<AdaptedState> states;

    public AdaptedSchedule() {
    }

    public AdaptedSchedule convertToXml(Schedule sch) {
        this.states = new ArrayList<>();
        for (State s : sch.getStates()) {
            states.add(new AdaptedState().convertToXml(s));
        }
        return this;
    }

    public Schedule convertToModel() {
        ArrayList<State> newStates = new ArrayList<>();
        for (AdaptedState s: states) {
            newStates.add(s.convertToModel());
        }
        return new Schedule(newStates);
    }
}
