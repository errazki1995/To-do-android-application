package upem.tasksAnd.start.Services;

import android.content.Context;

import java.util.List;

import upem.tasksAnd.start.models.Category;

public class CategoryService {
    public DatabaseHelper db;
    TaskService taskService;
Context context;
    public CategoryService(Context context){
         this.context = context;
        this.db = new DatabaseHelper(context);
    }
    public boolean newCategory(/*Category c*/){
        Category c = new Category("Travel","@drawable/ic_menu_send");
        Category c1 = new Category("Work");
        Category c2 = new Category("Shopping");
        Category c3  = new Category("My does");
        db.insertCategory(c);
        db.insertCategory(c1);
        db.insertCategory(c2);
        return db.insertCategory(c3);
    }

    public List<Category> getAllCategories(){
        return db.getAllCategories();
    }

}
