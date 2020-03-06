package upem.tasksAnd.start;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import upem.tasksAnd.start.Adapter.CatAdapter;
import upem.tasksAnd.start.Services.CategoryService;
import upem.tasksAnd.start.models.Category;
import upem.tasksAnd.start.models.Task;

public class CategoriesActivity extends AppCompatActivity {

    ListView catlistview;

    TextView txtcategory;
    TextView lblResult;
    TextView lblcategory;
    ImageView btncategory;
    Button btnmove;
    TextView mentionMove;
    CategoryService categoryService;
    List<Category> categories;
    CatAdapter catadapter;
    Task whichtask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);

        initComponents();
        newCategoryListener();
    }

    void initComponents() {

        catlistview = (ListView) findViewById(R.id.catlistView);
        txtcategory = (TextView) findViewById(R.id.txtcategory);
        lblResult = (TextView) findViewById(R.id.lblresult);
        lblcategory=(TextView) findViewById(R.id.lblcategory);
        btncategory = (ImageView) findViewById(R.id.btncat);
        mentionMove=(TextView) findViewById(R.id.mentionMove);
        btnmove=(Button) findViewById(R.id.movehere);
        categoryService = new CategoryService(getApplicationContext());
        categories = new ArrayList<>();
        categories = categoryService.getAllCategories();
        //Log.d("catcount","the category count"+ categories.size());
        catadapter = new CatAdapter(this, categories);
        catlistview.setAdapter(catadapter);
        Task task=null;
        if(getTaskIncaseOfMove()!=null)
            task = getTaskIncaseOfMove();

        if(task!=null){
            mentionMove.setVisibility(View.VISIBLE);
            lblcategory.setVisibility(View.GONE);
            txtcategory.setVisibility(View.GONE);
            btncategory.setVisibility(View.GONE);
        }else{
            mentionMove.setVisibility(View.GONE);
            lblcategory.setVisibility(View.VISIBLE);
            txtcategory.setVisibility(View.VISIBLE);
            btncategory.setVisibility(View.VISIBLE);
        }

    }

    void newCategoryListener() {
        btncategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category c = new Category(txtcategory.getText().toString());
                if (categoryService.newCategory(c)) {
                    categories.add(c);
                    catadapter.notifyDataSetChanged();
                    lblResult.setTextColor(Color.GREEN);
                    lblResult.setText("Category " + c.getCategoryName() + " is added");
                } else {
                    lblResult.setTextColor(Color.RED);
                    lblResult.setText("Failed to add category: " + c.getCategoryName() + "");
                }

            }
        });
    }

    Task getTaskIncaseOfMove(){
        Bundle bundle= getIntent().getExtras();
        if(bundle!=null){
            whichtask = (upem.tasksAnd.start.models.Task) getIntent().getExtras().get("theTaskCategory");
        }
        return whichtask;
    }

}
