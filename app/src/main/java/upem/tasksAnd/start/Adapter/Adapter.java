package upem.tasksAnd.start.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import upem.tasksAnd.start.NewTask;
import upem.tasksAnd.start.R;
import upem.tasksAnd.start.Services.Display;
import upem.tasksAnd.start.Services.TaskService;
import upem.tasksAnd.start.TasksList;
import upem.tasksAnd.start.ViewTask;
import upem.tasksAnd.start.models.Task;

public class Adapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    List<Task> tasks;
    Context context;
    TaskService taskService;
    static LayoutInflater layoutInflater = null;

    public Adapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try {
            taskService = new TaskService(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int pos = parent.getPositionForView(view);
        Toast.makeText(context.getApplicationContext(), pos + "", Toast.LENGTH_LONG).show();
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.popupmenu, popupMenu.getMenu());
        popupMenu.show();

    }

    public class ViewHolder {
        TextView txtname;
        TextView txtdescription;
        TextView txtstatus;
        TextView txtTaskNumber;
        ImageButton btnNewSubtask;
        ImageButton btnEditSubtask;
        ImageButton btnListSubtasks;
        ImageView imageViewmore;
    }

    @Override
    public int getCount() {
        return tasks.size();

    }

    public Task getItem(int i) {
        return tasks.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getPosition(int i) {
        return i;
    }


    public View getView(final int position, View v, ViewGroup parent) {
        View itemview = v;
        ViewHolder viewHolder = new ViewHolder();
        itemview = (itemview == null) ? layoutInflater.inflate(R.layout.taskitem1, null) : itemview;
        viewHolder.txtname = (TextView) itemview.findViewById(R.id.lbltaskName);
        viewHolder.txtstatus = (TextView) itemview.findViewById(R.id.lblstatus);
        viewHolder.txtTaskNumber = (TextView) itemview.findViewById(R.id.lbltasknumber);
        viewHolder.btnNewSubtask = (ImageButton) itemview.findViewById(R.id.btnNewsubtask);
        viewHolder.txtdescription = (TextView) itemview.findViewById(R.id.lbltaskDescription);

        viewHolder.btnEditSubtask = (ImageButton) itemview.findViewById(R.id.btnEditsubtask);
        viewHolder.btnListSubtasks = (ImageButton) itemview.findViewById(R.id.btnListsubtasks);
        viewHolder.imageViewmore = (ImageView) itemview.findViewById(R.id.imgviewMore);
        Task t = getItem(position);
        viewHolder.txtname.setText(t.getName());
        viewHolder.txtdescription.setText(t.getDescription());
        viewHolder.imageViewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopMenu((Activity) context, v, position);
            }
        });


//        viewHolder.txtstatus.setText("Status: "+t.getStatus()+"%");
        //      viewHolder.txtTaskNumber.setText("Task number :#"+t.getTaskid()+"-"+t.getParentid());


        //redirect add subtask with current task id as it will be parent id using Extra
        /*
        ADD 3 buttons Intent  here
         */

        //New subtask Listener

        /*
        viewHolder.btnNewSubtask.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context,"This task id is:"+getItem(position).getTaskid()+" and the parent id of this task is :"+getItem(position).getParentid(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, NewTask.class);
                intent.putExtra("parentidN",getItem(position).getTaskid()+"");
                context.startActivity(intent);
            }
        });
        //Edit Subtask Listener

        viewHolder.btnEditSubtask.setOnClickListener(new View.OnClickListener() {
/*
STUCK HERE , OBJECT IS NOT WRAPPED INSIDE THE EXTRA

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewTask.class);
                intent.putExtra("tasktoEdit",getItem(position));
                context.startActivity(intent);
            }
        });

        //List Subtasks Listener
        viewHolder.btnListSubtasks.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TasksList.class);
                intent.putExtra("parentidL",getItem(position).getTaskid()+"");
                context.startActivity(intent);
            }
        });

         */
        return itemview;
    }


    private void showPopMenu(final Activity activity, View v, final int position) {
        PopupMenu popupMenu = new PopupMenu(activity, v);
        popupMenu.getMenuInflater().inflate(R.menu.popupmenu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.itemhuhadd:
                        addItemContent(activity, position);
                        break;
                    case R.id.itemhuhdelete:
                        deleteItemContent(activity, position);
                        break;
                    case R.id.itemhuhlistsubtask:
                        listItemContent(activity, position);
                        break;
                    case R.id.itemhuhedit:
                        editItemContent(activity, position);
                        break;
                    case R.id.itemhuhview:
                        viewItemContent(activity,position);
                    default:
                        break;

                }
                return true;
            }
        });
        popupMenu.show();
    }

    private void addItemContent(final Activity activity, int position) {
        Toast.makeText(context, "This task id is:" + getItem(position).getTaskid() + " and the parent id of this task is :" + getItem(position).getParentid(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(activity.getApplicationContext(), NewTask.class);
        intent.putExtra("parentidN", getItem(position).getTaskid() + "");
        context.startActivity(intent);

    }

    private void editItemContent(final Activity activity, int position) {
        Intent intent = new Intent(activity.getApplicationContext(), NewTask.class);
        intent.putExtra("tasktoEdit", getItem(position));
        context.startActivity(intent);
    }

    private void listItemContent(final Activity activity, int position) {
        Intent intent = new Intent(activity.getApplicationContext(), TasksList.class);
        intent.putExtra("parentidL", getItem(position).getTaskid() + "");
        activity.startActivity(intent);
    }

    private void deleteItemContent(final Activity activity, int position) {
        Task t = getItem(position);
        String taskname = t.getName();
        if(taskService.deleteTask(t.getTaskid())) Toast.makeText(activity.getApplicationContext(), "The task " + taskname + " has been successfuly deleted !", Toast.LENGTH_LONG).show();
        else Toast.makeText(activity.getApplicationContext(), "Failed to delete the task", Toast.LENGTH_LONG).show();
        Intent i = new Intent(activity.getApplicationContext(),TasksList.class);
        activity.startActivity(i);
    }

    private void viewItemContent(final Activity activity, int position) {
        Intent intent = new Intent(activity.getApplicationContext(), ViewTask.class);
        intent.putExtra("viewTask", getItem(position));
        context.startActivity(intent);
    }

}