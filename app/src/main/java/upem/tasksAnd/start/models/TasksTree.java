package upem.tasksAnd.start.models;

import java.util.ArrayList;
import java.util.List;

public class TasksTree<Task> {
 private Task t= null;
private List<TasksTree>  subtasks = new ArrayList<>();
private TasksTree parent=null;


    public TasksTree(Task t){
    this.t = t;
     }


    public Task getT() {
        return t;
    }

    public void setT(Task t) {
        this.t = t;
    }

    public List<TasksTree> getTask() {
        return subtasks;
    }

    public void setTask(List<TasksTree> task) {
        this.subtasks = task;
    }

    public TasksTree getParent() {
        return parent;
    }

    public void setParent(TasksTree parent) {
        this.parent = parent;
    }

    public void addChildrenTasks(TasksTree t){
        t.setParent(this);
        this.subtasks.add(t);
    }

    public List<TasksTree> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<TasksTree> subtasks) {
        this.subtasks = subtasks;
    }
}
