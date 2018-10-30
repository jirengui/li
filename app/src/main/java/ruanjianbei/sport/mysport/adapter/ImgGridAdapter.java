package ruanjianbei.sport.mysport.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.List;

import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.bean.PinlunBean;

/**
 * Created by li on 2017/12/13.
 */

public class ImgGridAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> imgList = new ArrayList<>();
    private int maxImgCount;
    private LayoutInflater inflater;
    private FreshImgCallBack freshImgCallBack;//针对三种操作逻辑所自定义的回调

    public ImgGridAdapter(Context context, ArrayList<String> imgList, int maxImgCount, FreshImgCallBack freshImgCallBack) {
        this.context = context;
        this.imgList = imgList;
        this.maxImgCount = maxImgCount;
        this.freshImgCallBack = freshImgCallBack;
        inflater = LayoutInflater.from(context);
    }

    /**
     * 设置回调
     *
     * @param callBack freshImgCallBack
     */
    public void setImgShowFresh(FreshImgCallBack callBack) {
        freshImgCallBack = callBack;
    }

    public void replaceAll(List<String> list) {
        imgList.clear();
        if (list != null && list.size() > 0) {
            imgList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addAll(List<String> list) {
        if (imgList != null && list != null) {
            imgList.addAll(list);
            notifyDataSetChanged();
//            notifyItemRangeChanged(dataList.size(),list.size());
        }

    }

    @Override
    public int getCount() {
            if (imgList.size() < maxImgCount) {
                return imgList.size();
            } else {
                return maxImgCount;
            }
    }

    @Override
    public Object getItem(int position) {
            return imgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (holder != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.itme_gridview, null);
            holder.sdvItemShowImg = (SimpleDraweeView) convertView.findViewById(R.id.sdvItemShowImg);
            holder.ivDeleteImg =  convertView.findViewById(R.id.ivDeleteImg);
            holder.rlItemShow = (RelativeLayout) convertView.findViewById(R.id.rlItemShow);
        }
        //——————————————————————————————设置图片逻辑——————————————————————————————
        holder.rlItemShow.setVisibility(View.GONE);
            if (imgList.size() < 5) {
                    if (getCount() > 0) {
                        showImg(position, holder);
                    }
            } else {
                showImg(position, holder);
            }

        //放在外面用于更新position
        holder.ivDeleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                freshImgCallBack.updateGvImgShow(position);//更新数据
            }
        });
        return convertView;
    }

    //显示图片
    private void showImg(int position, ViewHolder holder) {
        holder.rlItemShow.setVisibility(View.VISIBLE);
        //设置图片
        System.out.println("imgList: " + imgList);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse("file://" + imgList.get(position)))
                .setProgressiveRenderingEnabled(true)
                .setResizeOptions(new ResizeOptions(200, 200))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .setTapToRetryEnabled(true)
                .setOldController(holder.sdvItemShowImg.getController())
                .build();
        holder.sdvItemShowImg.setController(controller);
    }

    class ViewHolder {
        SimpleDraweeView sdvItemShowImg;
        ImageView ivDeleteImg;
        RelativeLayout rlItemShow;
    }

}