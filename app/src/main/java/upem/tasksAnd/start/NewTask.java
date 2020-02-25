package upem.tasksAnd.start;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import upem.tasksAnd.start.Services.DatabaseHelper;
import upem.tasksAnd.start.Services.IdAssigner;
import upem.tasksAnd.start.Services.TaskService;
import upem.tasksAnd.start.models.Attachement;
import upem.tasksAnd.start.models.Audio;
import upem.tasksAnd.start.models.Task;

public class NewTask extends AppCompatActivity {
    TaskService taskService;
    Intent fileintent;
    String haveAparent = null;  //Does this task have a parent  ?
    Task toUpdateTask;
    Spinner priorlist;
    Spinner difflist;
    ImageView btnattach;
    ImageView imgcontainer1;
    ImageView imgcontainer2;
    ImageView imgcontainer3;
    VideoView videocontainer;
    ArrayAdapter<String> priorAdapter;
    ArrayAdapter<String> difficultyAdapter;
    Map<String, Integer> toGetPriorityPosition = new HashMap<String, Integer>();
    Map<String, Integer> toGetDifficultyPosition = new HashMap<String, Integer>();
    List<Attachement> attachements = new ArrayList<>();
    Button addBtn;
    Button btnCancel;
    EditText txtTaskname;
    EditText txtDescription;
    EditText txtStartDate;
    EditText txtEndDate;
    List<String> spinnerPriority = new ArrayList<String>();
    List<String> spinnerDifficulty = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task1);
        initTaskService();
        populateHashMap();
        initComponents();
        initSpinners();
        attach();
        //Toast("The Next ID IS: "+taskService.taskIdGiver(),taskService.duration);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            haveAparent = bundle.getString("parentidN");
            //Toast("The redirected parent is :"+haveAparent+" ",taskService.duration);
            toUpdateTask = (Task) getIntent().getExtras().get("tasktoEdit");
            Toast("The update task description is : " + toUpdateTask, taskService.duration);
            if (toUpdateTask != null) {
                addBtn.setText("Edit");
                populateTheForm();
            }
        }

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (toUpdateTask == null && formValidator()) {
                        newTask();
                    } else if (toUpdateTask != null && formValidator()) {
                        updateTheTask();
                    } else Toast("Please verify your inputs", 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    void initComponents() {
        addBtn = (Button) findViewById(R.id.addBtn);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        txtTaskname = (EditText) findViewById(R.id.txtTaskName);
        txtDescription = (EditText) findViewById(R.id.txtTaskDescription);
        txtStartDate = (EditText) findViewById(R.id.txtstartDate);
        txtEndDate = (EditText) findViewById(R.id.txtEndDate);
        btnattach = (ImageView) findViewById(R.id.btnattach);
        imgcontainer1 = (ImageView) findViewById(R.id.img1);
        imgcontainer2 = (ImageView) findViewById(R.id.img2);
        imgcontainer3 = (ImageView) findViewById(R.id.img3);
        videocontainer = (VideoView) findViewById(R.id.vid1);

    }

    void initTaskService() {
        try {
            taskService = new TaskService(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void initSpinners() {
        spinnerPriority.add("Not urgent");
        spinnerPriority.add("Urgent");
        spinnerDifficulty.add("Simple");
        spinnerDifficulty.add("Medium");
        spinnerDifficulty.add("Hard");
        priorAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, spinnerPriority);
        difficultyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, spinnerDifficulty);
        priorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priorlist = (Spinner) findViewById(R.id.priorityList);
        difflist = (Spinner) findViewById(R.id.difficultyList);
        priorlist.setAdapter(priorAdapter);
        difflist.setAdapter(difficultyAdapter);
    }

    void populateTheForm() {
        Task st = toUpdateTask;
        txtTaskname.setText(toUpdateTask.getName());
        txtDescription.setText(toUpdateTask.getDescription());
        txtStartDate.setText(toUpdateTask.getDateStart());
        txtEndDate.setText(toUpdateTask.getDateEnd());
        if (!toUpdateTask.getDifficulty().isEmpty() || !toUpdateTask.getPriorityLevel().isEmpty()) {
            priorlist.setSelection(toGetPriorityPosition.get(toUpdateTask.getPriorityLevel()));
            difflist.setSelection(toGetDifficultyPosition.get(toUpdateTask.getDifficulty()));
        }
        //don't forget to add the other elements [Status..]
    }

    void populateHashMap() {
        //Hashmap to get the position based on the String
        toGetPriorityPosition.put("Not urgent", 0);
        toGetPriorityPosition.put("Urgent", 1);

        toGetDifficultyPosition.put("Simple", 0);
        toGetDifficultyPosition.put("Medium", 1);
        toGetDifficultyPosition.put("Hard", 2);
    }


    void newTask() throws Exception {
        Toast("Clicked", taskService.duration);
        String priorSelect = priorlist.getSelectedItem().toString();
        String diffSelect = difflist.getSelectedItem().toString();

        Task t = new Task(taskService.taskIdGiver(), txtTaskname.getText().toString(), txtDescription.getText().toString()
                , txtStartDate.getText().toString(), txtEndDate.getText().toString(),
                priorSelect, diffSelect, 0, null, 0, null);

        Toast(t.toString(), taskService.duration);
        if (haveAparent == null) taskService.addtask(0, t);
        else taskService.addtask(Integer.parseInt(haveAparent), t);
        getIntent().removeExtra("parentidN");
        Intent i = new Intent(getApplicationContext(), TasksList.class);
        startActivity(i);
    }

    void updateTheTask() {
        if (taskService.updateTask(toUpdateTask.getTaskid(), toUpdateTask))
            Toast("Task With id :" + toUpdateTask.getTaskid()
                    + " has been updated", taskService.duration);
        //remove intent of the object
        getIntent().removeExtra("tasktoEdit");
        Intent i = new Intent(getApplicationContext(), TasksList.class);
        startActivity(i);
    }

    void attach() {
        btnattach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileintent = new Intent(Intent.ACTION_GET_CONTENT);
                fileintent.setType("*/*");
                startActivityForResult(fileintent, 10);
            }
        });
    }

    void displayAttachement(String path) {
        String path1 = path.substring(path.lastIndexOf("/") + 1);
        String filetype = path1.substring(path1.indexOf(".") + 1, path1.length());
        Log.w("PATHPATH", "the path is :" + path);
        Log.w("FILEPATH", "the  path1 is :" + path1);
        Log.w("Filetype", "The file type is " + filetype);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                imgcontainer1.setImageDrawable(Drawable.createFromPath(path));
                imgcontainer1.setVisibility(View.VISIBLE);
            } else {
                String[] permissionRequest = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissionRequest, 1010);
            }
        }

    }

    //test to validate the form
    boolean formValidator() {
        if (txtTaskname.getText().toString().isEmpty() || txtDescription.getText().toString().isEmpty() || txtStartDate.getText().toString().isEmpty() || txtEndDate.getText().toString().isEmpty())
            return false;
        return true;
    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == RESULT_OK) {
                String path = data.getData().getPath();
                displayAttachement(path);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1010: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    attach();

                } else {
                    Toast.makeText(this, "We need a permission huh !", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    public void Toast(String message, int duration) {
        taskService.Toast(message, duration);
    }

    public Date getActualDate() {
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = Calendar.getInstance().getTime();
        return today;
    }

}
/*
BESIDE THE DESTROYING OF THE INTENT INSIDE THE ONCREATE IT SHOULD BE DESTROYED WHEN THERE IS ANY MIUSE OF
THE ACTIVITY , LIKE PRRESSING HOME OR BUTTON BACK
 */