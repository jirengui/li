package ruanjianbei.sport.mysport.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.bean.LiWuBean;
import ruanjianbei.sport.mysport.bean.LiWuBean;
import ruanjianbei.sport.mysport.util.MyApplication;

/**
 * Created by li on 2018/5/7.
 */
public class RcShangChengXuanGouAdapter extends RecyclerView.Adapter<RcShangChengXuanGouAdapter.RecyclerViewHolder> implements View.OnClickListener {

    private List<LiWuBean> list;
    private Context context;
    private RcShangChengXuanGouAdapter.OnItemClickListener mItemClickListener;

    public RcShangChengXuanGouAdapter(List<LiWuBean> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void replaceAll(List<LiWuBean> list) {
        if (list != null && list.size() > 0) {
            this.list.clear();
            this.list.addAll(list);
            System.out.println("适配器的list：" + list.size());
            notifyDataSetChanged();
        }

    }

    public void addAll(List<LiWuBean> list) {
        if (this.list != null && list != null) {
            this.list.addAll(list);
            notifyDataSetChanged();
//            notifyItemRangeChanged(dataList.size(),list.size());
        }

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_item_shangchengxuangou,null);
        view.setOnClickListener(this);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        System.out.println("商品："  + position + " "+ list.get(position).getName());
        holder.tv_name.setText(list.get(position).getName());
        holder.tv_qian.setText(list.get(position).getJifen());
        holder.itemView.setTag(position);
        Picasso.with(context)
                .load(MyApplication.imageUri + list.get(position).getTupian())
                .placeholder(context.getResources().getDrawable(R.drawable.wanou))
                .into(holder.tupian);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (mItemClickListener!=null){
            mItemClickListener.onItemClick(v,(Integer) v.getTag());
        }
    }
    public interface OnItemClickListener{
        void onItemClick(View v, int o);
    }
    public void setItemClickListener(RcShangChengXuanGouAdapter.OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }


    class RecyclerViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_name,tv_qian;
        private ImageView tupian;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.item_shangcheng_name);
            tv_qian = (TextView) itemView.findViewById(R.id.item_shangcheng_qian);
            tupian = itemView.findViewById(R.id.item_shangcheng_tupian);
        }
    }

}
