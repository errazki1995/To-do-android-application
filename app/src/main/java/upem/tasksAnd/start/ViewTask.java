package upem.tasksAnd.start;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import upem.tasksAnd.start.Adapter.ImageAdapter;
import upem.tasksAnd.start.Services.TaskService;
import upem.tasksAnd.start.models.Attachement;
import upem.tasksAnd.start.models.Task;

public class ViewTask extends AppCompatActivity {
    ViewPager vp;
    TabLayout tbly;
    Timer timer;
    TextView taskname;
    TextView description;
    TextView startDate;
    TextView endDate;
    TextView difficulty;
    TextView priority;
    TaskService taskService;
    ArrayList<String> images;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        tbly = (TabLayout) findViewById(R.id.tabdts);
        vp = (ViewPager) findViewById(R.id.view_pager);
        images = new ArrayList<>();
        initComponents();
        populateTheLayout();
    }


    void initComponents(){
        try { taskService= new TaskService(this); }catch (Exception e){e.printStackTrace();}
        taskname=(TextView) findViewById(R.id.detailtaskname);
        description = (TextView) findViewById(R.id.detaildesc);
        startDate=(TextView) findViewById(R.id.detailstartDate);
        difficulty=(TextView) findViewById(R.id.detaildifficulty);
        endDate = (TextView)findViewById(R.id.detailendDate);
        priority = (TextView)findViewById(R.id.detailpriority);
        visualiseImage();

    }
    public void populateTheLayout(){
        Task task = (Task) getIntent().getExtras().get("viewTask");
        taskname.setText(task.getName());
        description.setText(task.getDescription());
        startDate.setText(task.getDateStart());
        endDate.setText(task.getDateEnd());
        difficulty.setText(task.getDifficulty());
        priority.setText(task.getPriorityLevel());
        //remove the viewTask extra
    }
    void visualiseImage(){
        //get task here

        List<Attachement> attachements= taskService.getTaskAttachement(4);
        int integers = attachements.size();
        Log.d("attachCount","attach counts are :"+integers);
        ImageAdapter slider = new ImageAdapter(getApplicationContext(), images);
        vp.setAdapter(slider);
        vp.setCurrentItem(0);
        tbly.setupWithViewPager(vp, true);
    }
}
