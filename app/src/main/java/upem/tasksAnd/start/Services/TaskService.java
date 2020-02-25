package upem.tasksAnd.start.Services;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import upem.tasksAnd.start.models.Task;
import upem.tasksAnd.start.models.TasksTree;

public class TaskService {
    public static final int duration =0;
    TasksTree task;
    IdAssigner idAssigner;
    DatabaseHelper db;
    Context context;
    Json json;
    Map<String, String> map;

    void loadFormat() {
        map = new HashMap<String, String>();
        map.put("mp3", "audio");
        map.put("mp4", "video");
        map.put("wav", "audio");
        map.put("png", "image");
        map.put("jpg", "image");
        map.put("jpeg", "image");
        map.put("ogg", "audio");
        map.put("mov", "video");
        map.put("mov", "video");
        map.put("3gp", "video");
        map.put("3gpp", "video");


    }

    public TaskService(Context ctx) throws JSONException {
        task = new TasksTree(new Task());
        this.context = ctx;
        loadFormat();
        db = new DatabaseHelper(ctx);
        idAssigner = new IdAssigner(db);
        //test();

    }

    public int taskIdGiver() {
        return idAssigner.assignTaskId();
    }

    public boolean addtask(int parentid, Task t) throws JSONException {
        t.setParentid(parentid);
        if (db.insertTask(t)) {
            if(parentid==0) Toast("This is a Main Task, added successfully" + t.getTaskid(),duration);
            else  Toast("This is a subtask with parentid :"+parentid+ " added successfully" + t.getTaskid(),duration);
            return true;
        }
        Toast("Failure adding task",duration);
        return false;
    }

    public  void Toast(String message,int duration) {
        int i=0;
        while(i<duration){
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            i++;
        }
    }

    /*

        void test()throws JSONException {
            Task t = new Task();
          //  JSONObject jsondata = new JSONObject(getJsonFile());
            //JSONArray array = jsondata.getJSONArray("tasks");
    JSONArray array = new JSONArray();
            JSONObject newtask =new JSONObject();
            JSONObject newattachement = new JSONObject();
            JSONObject multimedia = new JSONObject();

    //        newattachement.put("attachementid",t.getAttachement().getAttachementid());
      //      newattachement.put("insidevideo",t.getAttachement().isVideoinside());
        //    newattachement.put("insideaudio",t.getAttachement().isVideoinside());
          //  newattachement.put("insideimage",t.getAttachement().isVideoinside());
            //newattachement.put("insidegeo",t.getAttachement().isVideoinside());


            newtask.put("taskid",t.getTaskid());
            newtask.put("datestart",t.getDateStart());
            newtask.put("dateend",t.getDateEnd());
            newtask.put("prioritylevel",t.getPriorityLevel());
            newtask.put("difficulty",t.getStatus());
            //newtask.put("attachement",t.getA)
           // newtask.put("attachement",newattachement);
            array.put(newtask);
            JSONObject  finaljson= new JSONObject();
            finaljson.put("tasks",array.toString());
            writeToFile(finaljson.toString());

        }

    */
    public String getJsonFile() {
        return new Json(context).getTheJson();
    }

    public void writeToFile(String jsondata) {
        try {
            FileWriter file = new FileWriter(getJsonFile());
            BufferedWriter bufferedwriter = new BufferedWriter(file);
            bufferedwriter.write(jsondata);
            bufferedwriter.close();

        } catch (IOException e) {
            Log.i("ErrorOpenFileOrWrite", "Error openning file or write to it");
            e.printStackTrace();
        }
    }

    public void addSubtaskChild(int parentTaskid, Task t) {
        TasksTree<Task> newsubtask = new TasksTree<>(t);
        //search in json file the object with that parent id and place it as a parent
        addChild(parentTaskid, newsubtask);

    }

    public void addChild(int parentTaskid, TasksTree child) {
        //search for parentTaskid
        //get the parent object
        TasksTree parent = getParentObject(parentTaskid);
        child.setParent(task);
        task.getSubtasks().add(child);
        //put the tree with the new object
    }

    public TasksTree getParentObject(int parentTasksid) {
        //search for the object based on the parentTasksid, populate object from json and return the sub-tree
        return null;
    }

    public List<TasksTree> listTasks() {
        //get from Json file
        //return all tasks
        return null;
    }

    public List<TasksTree> listsubtasks(int taskparentid) {
        //get subtasks from task
        //search in json
        //return the tasks
        return null;
    }

    public void modifyTask(int taskid, Task t) {
        //search id of task and replace the whole object with the new object

    }

    public boolean deleteTask(int taskid) {
       return db.deleteTask(taskid);

    }

    public List<Task> listAllTasks(int parentid){
        return db.getAllTasksByParent(parentid);
    }
    public boolean updateTask(int taskid,Task t){
        return db.updateTask(taskid,t);
    }

}
