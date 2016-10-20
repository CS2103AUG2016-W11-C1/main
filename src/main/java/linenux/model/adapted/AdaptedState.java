package linenux.model.adapted;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import linenux.model.State;
import linenux.model.Task;

@XmlRootElement(name = "State")
public class AdaptedState {
    @XmlElement(name = "taskList")
    private ArrayList<AdaptedTask> taskList;

    public AdaptedState() {
    }

    public AdaptedState convertToXml(State s) {
        this.taskList = new ArrayList<>();
        for (Task t : s.getTaskList()) {
            taskList.add(new AdaptedTask().convertToXml(t));
        }
        return this;
    }

    public State convertToModel() {
        ArrayList<Task> newTaskList = new ArrayList<>();
        for (AdaptedTask t : taskList) {
            newTaskList.add(t.convertToModel());
        }
        return new State(newTaskList);
    }
}
