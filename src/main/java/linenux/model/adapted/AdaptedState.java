package linenux.model.adapted;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import linenux.model.State;
import linenux.model.Task;

@XmlRootElement(name = "State")
public class AdaptedState {
    @XmlElement(name = "tasks")
    private ArrayList<AdaptedTask> tasks;

    public AdaptedState() {
    }

    public AdaptedState convertToXml(State s) {
        this.tasks = new ArrayList<>();
        for (Task t : s.getTaskList()) {
            tasks.add(new AdaptedTask().convertToXml(t));
        }
        return this;
    }

    public State convertToModel() {
        ArrayList<Task> newTasks = new ArrayList<>();
        for (AdaptedTask t : tasks) {
            newTasks.add(t.convertToModel());
        }
        return new State(newTasks);
    }
}
