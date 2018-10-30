package ruanjianbei.sport.mysport.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.util.MyApplication;
import ruanjianbei.sport.mysport.zidingyi_view.ZoomImageView;

/**
 * Created by li on 2018/5/17.
 */

public class ViewPagerAdapter extends PagerAdapter {
    private List<String> paths = new ArrayList<String>();
    private Activity activity;

    public ViewPagerAdapter(List<String> paths, Activity activity){
        this.paths = paths;
        this.activity = activity;
    }
    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
//            String imagePath = paths.get(position);
//            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            View view = LayoutInflater.from(container.getContext()).inflate(
                    R.layout.zoom_image_layout, null);
            final ZoomImageView zoomImageView = (ZoomImageView) view
                    .findViewById(R.id.zoom_image_view);
            zoomImageView.fin(activity);
        Picasso.with(activity)
                .load(MyApplication.imageUri + paths.get(position))
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        zoomImageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
