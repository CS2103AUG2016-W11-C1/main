package linenux.model.adapted;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import linenux.model.State;
import linenux.model.Task;

//@@author A0135788M
@XmlRootElement(name = "State")
public class AdaptedState {
    @XmlElement(name = "tasks")
    private ArrayList<AdaptedTask> tasks;

    /**
     * Empty constructor for XML use.
     */
    public AdaptedState() {
    }

    /**
     * Converts a state to an XML-friendly state object.
     * @param r A {@code state} instance.
     * @return an XML-friendly state object.
     */
    public AdaptedState convertToXml(State s) {
        this.tasks = new ArrayList<>();
        for (Task t : s.getTaskList()) {
            tasks.add(new AdaptedTask().convertToXml(t));
        }
        return this;
    }

    /**
     * Converts an XML-friendly state object to a state.
     * @return a state object.
     */
    public State convertToModel() {
        ArrayList<Task> newTasks = new ArrayList<>();
        for (AdaptedTask t : tasks) {
            newTasks.add(t.convertToModel());
        }
        return new State(newTasks);
    }
}
