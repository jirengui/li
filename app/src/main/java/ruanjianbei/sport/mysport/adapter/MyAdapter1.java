package ruanjianbei.sport.mysport.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ruanjianbei.sport.mysport.util.MyApplication;

public class MyAdapter1 extends BaseAdapter {
    //上下文对象
    private Context context;
    //图片数组
    private List<String> pathlist = null;
    private int i = -1;//分类
    private MyAdapter1.OnItemClickListener mItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(int o);
    }

    public void setItemClickListener(MyAdapter1.OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public MyAdapter1(Context context, List<String> imgs) {
        this.context = context;
        this.pathlist = imgs;
    }
    public MyAdapter1(Context context, List<String> imgs, int i) {
        this.context = context;
        this.pathlist = imgs;
        this.i = i;
    }


    public int getCount() {
        if (pathlist != null) {
            return pathlist.size();
        } else {
            return 0;
        }
    }

    public Object getItem(int item) {
        return item;
    }

    public long getItemId(int id) {
        return id;
    }

    //创建View方法
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));//设置ImageView对象布局
            imageView.setAdjustViewBounds(true);//设置边界对齐
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置刻度的类型
            imageView.setPadding(5, 1, 5, 1);//设置间距
        } else {
            imageView = (ImageView) convertView;
        }
        if (pathlist == null) {
//            imageView.setImageResource(imgs[pos);//为ImageView设置图片资源
        } else {
            if (i != -1){
                Picasso.with(context)
                        .load("file://"+ pathlist.get(position))
                        .into(imageView);
                System.out.println("图片适配："+ pathlist.get(position));
            }else {
                Picasso.with(context)
                        .load(MyApplication.imageUri + pathlist.get(position))
                        .into(imageView);
                System.out.println("图片适配：" + MyApplication.imageUri + pathlist.get(position));
            }
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(position);
                }
            }
        });
        return imageView;
    }
}