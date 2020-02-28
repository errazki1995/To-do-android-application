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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import upem.tasksAnd.start.CategoriesActivity;
import upem.tasksAnd.start.NewTask;
import upem.tasksAnd.start.R;
import upem.tasksAnd.start.Services.CategoryService;
import upem.tasksAnd.start.Services.Display;
import upem.tasksAnd.start.models.Category;

public class CatAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    List<Category> categories;
    Context context;
    CategoryService categoryService;
    static LayoutInflater layoutInflater = null;

    public CatAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try {
            categoryService = new CategoryService(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int pos = parent.getPositionForView(view);
        Toast.makeText(context.getApplicationContext(), pos + "", Toast.LENGTH_LONG).show();
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.categorymenu, popupMenu.getMenu());
        popupMenu.show();

    }

    public class ViewHolder {
        TextView catName;
        ImageView imgmorecat;

    }

    @Override
    public int getCount() {
        return categories.size();

    }

    public Category getItem(int i) {
        return categories.get(i);
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
        itemview = (itemview == null) ? layoutInflater.inflate(R.layout.category_item, null) : itemview;
        viewHolder.catName = (TextView) itemview.findViewById(R.id.catName);
        viewHolder.imgmorecat = (ImageView) itemview.findViewById(R.id.morecat);
        Category c = getItem(position);

        viewHolder.catName.setText(c.getCategoryName());
        viewHolder.imgmorecat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopMenu((Activity) context, v, position);
            }
        });


        return itemview;
    }


    private void showPopMenu(final Activity activity, View v, final int position) {
        PopupMenu popupMenu = new PopupMenu(activity, v);
        popupMenu.getMenuInflater().inflate(R.menu.categorymenu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Display.Toast(activity.getApplicationContext(), "Item n" + item.getItemId() + " is clicked", 0);
                Log.w("menudebug", "item.getItemId() :" + item.getItemId());
                switch (item.getItemId()) {
                    case R.id.catdelete:
                        deleteCategory(activity, position);
                        break;
                    case R.id.catedit:
                        editItemContent(activity, position);
                        break;

                    default:
                        break;

                }
                return true;
            }
        });
        popupMenu.show();
    }


    private void editItemContent(final Activity activity, int position) {
        Intent intent = new Intent(activity.getApplicationContext(), NewTask.class);
        //intent.putExtra("catedit", getItem(position));
        context.startActivity(intent);
    }


    private void deleteCategory(final Activity activity, int position) {
        Category c = getItem(position);
        String category = c.getCategoryName();
        if(categoryService.deleteCategory(c.getCategoryid())) Toast.makeText(activity.getApplicationContext(), "The category" + category + " has been successfuly deleted !", Toast.LENGTH_LONG).show();
        else Toast.makeText(activity.getApplicationContext(), "Failed to delete the Category", Toast.LENGTH_LONG).show();
       Intent i = new Intent(activity.getApplicationContext(), CategoriesActivity.class);
       activity.startActivity(i);
    }




}