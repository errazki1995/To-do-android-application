package upem.tasksAnd.start;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


import upem.tasksAnd.start.Adapter.ImageAdapter;
import upem.tasksAnd.start.models.Task;

public class ViewTask extends AppCompatActivity {
    ViewPager vp;
    TabLayout tbly;
    int currentpic = 0;
    Timer timer;
    TextView taskname;
    TextView description;
    TextView startDate;
    TextView endDate;
    TextView difficulty;
    TextView priority;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        tbly = (TabLayout) findViewById(R.id.tabdts);
        vp = (ViewPager) findViewById(R.id.view_pager);
        ArrayList<Integer> images = new ArrayList<>();
        images.add(R.drawable.emptystar);
        images.add(R.drawable.camera);
        images.add(R.drawable.fullstar);
        images.add(R.drawable.emptystar);
        ImageAdapter slider = new ImageAdapter(getApplicationContext(), images);
        vp.setAdapter(slider);
        vp.setCurrentItem(0);
        tbly.setupWithViewPager(vp, true);
        initComponents();
        populateTheLayout();
    }


    void initComponents(){
        taskname=(TextView) findViewById(R.id.detailtaskname);
        description = (TextView) findViewById(R.id.detaildesc);
        startDate=(TextView) findViewById(R.id.detailstartDate);
        difficulty=(TextView) findViewById(R.id.detaildifficulty);
        endDate = (TextView)findViewById(R.id.detailendDate);
        priority = (TextView)findViewById(R.id.detailpriority);

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



}
