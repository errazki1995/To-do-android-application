package upem.tasksAnd.start;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.InputStream;
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
import upem.tasksAnd.start.Services.UriManager;
import upem.tasksAnd.start.models.Attachement;
import upem.tasksAnd.start.models.Audio;
import upem.tasksAnd.start.models.Task;

public class NewTask extends AppCompatActivity {
    int ThenewTaskid=-1;
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

    //Image Cancel buttons
    ImageView imgcancel1;
    ImageView imgcancel2;
    ImageView imgcancel3;

    VideoView videocontainer;
    ArrayAdapter<String> priorAdapter;
    ArrayAdapter<String> difficultyAdapter;
    List<ImageView> imagesviews;
    Map<String, Integer> toGetPriorityPosition = new HashMap<String, Integer>();
    Map<String, Integer> toGetDifficultyPosition = new HashMap<String, Integer>();
    Map<Integer,String> attachementImgViewLinker = new HashMap<>();
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
        attachListener();
        imageCancelListener();
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

        imgcancel1 = (ImageView) findViewById(R.id.imgcancel1);
        imgcancel2 = (ImageView) findViewById(R.id.imgcancel2);
        imgcancel3 = (ImageView) findViewById(R.id.imgcancel3);


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
        ThenewTaskid= taskService.taskIdGiver();

        Task t = new Task(ThenewTaskid, txtTaskname.getText().toString(), txtDescription.getText().toString()
                , txtStartDate.getText().toString(), txtEndDate.getText().toString(),
                priorSelect, diffSelect, 0, null, 0, null);


        //link the task id to the attachements
        for(Attachement a: attachements){
            a.setTaskid(ThenewTaskid+"");
            taskService.setupAttachemennt(a);
        }

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

    void attachListener() {
        btnattach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileintent = new Intent(Intent.ACTION_GET_CONTENT);
                fileintent.setType("*/*");
                startActivityForResult(fileintent, 10);
            }
        });
    }



    void GetTheAttachement(Uri uri) throws Exception {
        ContentResolver contentResolver= getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = mime.getExtensionFromMimeType(contentResolver.getType(uri));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                if(type.equalsIgnoreCase("png") || type.equalsIgnoreCase("jpeg") || type.equalsIgnoreCase("jpg")  ){
                    if(!HasImage(imgcontainer1)) linkTheImage(imgcontainer1,imgcancel1,uri,type,1);
                    else if(!HasImage(imgcontainer2)) linkTheImage(imgcontainer2,imgcancel2,uri,type,2);
                    else if(!HasImage(imgcontainer3)) linkTheImage(imgcontainer3,imgcancel3,uri,type,3);
                }
            } else {
                String[] permissionRequest = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissionRequest, 1010);

            }
        }
    }

    private boolean HasImage(ImageView imageVieww){
        Drawable drawable= imageVieww.getDrawable();
        boolean imageisthere = drawable !=null;
        if (imageisthere && (drawable instanceof BitmapDrawable)) {
            imageisthere = ((BitmapDrawable)drawable).getBitmap() != null;
        }
        return imageisthere;
    }
    private void linkTheImage(ImageView imgview,ImageView imgcancel,Uri uri,String type,int whichimageview) throws Exception{
        InputStream is =getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        is.close();
        imgview.setImageBitmap(bitmap);
        imgview.setVisibility(View.VISIBLE);
        imgcancel.setVisibility(View.VISIBLE);
        Attachement a= new Attachement(uri.toString(),type);
        attachementImgViewLinker.put(whichimageview,a.getGetAttachmentType());
        attachements.add(a);
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

                try {
                    GetTheAttachement(data.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //String pathFromUri=  uriManager.getUriRealPath(this,data.getData());
                //Log.d("theXpathfromUri","Xthe path from uri is:"+pathFromUri);
            }
        }

    }

    void imageCancelListener(){
        imgcancel1.setOnClickListener(new View.OnClickListener() {
            ;
            @Override
            public void onClick(View v) {
                if(attachementImgViewLinker.get(1)!=null){
                    String uri=attachementImgViewLinker.get(1);
                    removeAttachement(uri,imgcontainer1,imgcancel1);

                }
            }
        });

        imgcancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(attachementImgViewLinker.get(2)!=null){
                    String uri=attachementImgViewLinker.get(2);
                    removeAttachement(uri,imgcontainer2,imgcancel2);
                }
            }
        });

        imgcancel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(attachementImgViewLinker.get(3)!=null){
                    String uri=attachementImgViewLinker.get(3);
                    removeAttachement(uri,imgcontainer3,imgcancel3);

                }
            }
        });
    }


    void removeAttachement(String whichone,ImageView imageView,ImageView cancelImage) {
        for (Attachement a : attachements) {
            if (whichone.equalsIgnoreCase(a.getAttachmentPath())) {
                attachements.remove(a);
            }
            imageView.setVisibility(View.GONE);
            cancelImage.setVisibility(View.GONE);
        }
    }
    /*
        String getPathFromImageUri(Context context, Uri uri){
            Cursor cursor = null;
            try{
                String[] imgcontent = {MediaStore.Images.Media.DATA};
                cursor = context.getContentResolver().query(uri,imgcontent,null,null,null);
                int colind = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(colind);

            }catch(Exception e){
                e.printStackTrace();
                Log.w("ErrorGetImage","Problem occured getting image path from Uri, line 271 in NewTask.java");
                return  e.toString();
            }
            finally {
                if(cursor!=null) cursor.close();
            }
        }

     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1010: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    attachListener();
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