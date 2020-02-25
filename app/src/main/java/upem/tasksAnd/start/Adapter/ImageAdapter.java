package upem.tasksAnd.start.Adapter;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

import upem.tasksAnd.start.R;

public class ImageAdapter extends PagerAdapter {
    private ArrayList<Integer> images;
    private LayoutInflater outflat;
    private Context context;

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = outflat.inflate(R.layout.img_layout, view, false);
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.imgok);
        imageView.setImageResource(images.get(position));
        view.addView(imageLayout, 0);
        return imageLayout;
    }

    public ImageAdapter(Context context, ArrayList<Integer> images) {
        this.context = context;
        this.images = images;
        outflat = LayoutInflater.from(context);
    }

    public void destroyItem(ViewGroup container, int position, Object ok) {
        container.removeView((View) ok);
    }

    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }




}

