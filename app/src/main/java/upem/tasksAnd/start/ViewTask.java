package upem.tasksAnd.start;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


import upem.tasksAnd.start.Adapter.ImageAdapter;

public class ViewTask extends AppCompatActivity {
    ViewPager vp;
    TabLayout tbly;
    int currentpic = 0;
    Timer timer;

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
    }

}
