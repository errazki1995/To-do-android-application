package upem.tasksAnd.start.Services;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import upem.tasksAnd.start.models.Category;
import upem.tasksAnd.start.models.Task;

public class CategoryService {
    public DatabaseHelper db;
    TaskService taskService;
    Context context;

    public CategoryService(Context context) {
        this.context = context;
        this.db = new DatabaseHelper(context);
    }

    public boolean newCategory(Category c) {
        return db.insertCategory(c);
    }

    public List<Category> getAllCategories() {
        List<Category> cats = new ArrayList<>();
        /*
        Category c1 = new Category("Work");
        Category c2 = new Category("University");
        Category c3 = new Category("Family");
        cats.add(c1);
        cats.add(c2);
        cats.add(c3);

         */
        return db.getAllCategories();
        //return cats;
    }

    public boolean deleteCategory(int id) {
        return db.deleteCategory(id);
    }

    public boolean editCategory(int id, Category c) {
        return db.updateCategory(id, c);
    }

    public boolean moveTasktoList(int categoryid, Task t){
        return db.moveTaskTocategory(categoryid,t);
    }

}