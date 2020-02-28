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

public class CategoriesActivity extends AppCompatActivity {

    ListView catlistview;
    TextView txtcategory;
    TextView lblResult;
    ImageView btncategory;
    CategoryService categoryService;
    List<Category> categories;
    CatAdapter catadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        initComponents();
        newCategoryListener();
    }

    void initComponents() {
        catlistview = (ListView) findViewById(R.id.catlistView);
        txtcategory = (TextView) findViewById(R.id.txtcategory);
        lblResult = (TextView) findViewById(R.id.lblresult);
        btncategory = (ImageView) findViewById(R.id.btncat);
        categoryService = new CategoryService(getApplicationContext());
        categories = new ArrayList<>();
        categories = categoryService.getAllCategories();
        //Log.d("catcount","the category count"+ categories.size());
        catadapter = new CatAdapter(this, categories);
        catlistview.setAdapter(catadapter);
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

}
