package upem.tasksAnd.start.Services;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

public class IdAssigner {
    DatabaseHelper dbhelp;

    public IdAssigner(DatabaseHelper databaseHelper) {
        this.dbhelp = databaseHelper;
    }

    public int assignTaskId() {
        int threshold = 500;
        int taskid = 0, i = 1;
        while (i <= threshold) {
            if (i == threshold && dbhelp.getTaskById(i).getCount() > 0) threshold += 500;
            else if(dbhelp.getTaskById(i).getCount()>0) i++;
            else{
                dbhelp.getTaskById(i).close();
                taskid=i;
                return taskid;
            }
        }
        dbhelp.getTaskById(i).close();
        return taskid;
    }

}
