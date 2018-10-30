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

import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.bean.SaiShiBean;
import ruanjianbei.sport.mysport.bean.ZhiBoBean;
import ruanjianbei.sport.mysport.util.MyApplication;

/**
 * Created by li on 2018/5/7.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> implements View.OnClickListener {

    private List<ZhiBoBean> list;
    private Context  context;
    private RecyclerViewAdapter.OnItemClickListener mItemClickListener;


    public RecyclerViewAdapter(List<ZhiBoBean> list) {
        this.list = list;
    }
    public RecyclerViewAdapter(List<ZhiBoBean> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void replaceAll(List<ZhiBoBean> list) {
        if (list != null && list.size() > 0) {
            this.list.clear();
            this.list.addAll(list);
            System.out.println("直播适配器的list：" + list.size());
            notifyDataSetChanged();
        }

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_item,parent, false);
        view.setOnClickListener(this);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.tv.setText(list.get(position).getBiaoti());
        holder.tv_renshu.setText(String.valueOf(list.get(position).getCount()));
        holder.tv_leixin.setText(list.get(position).getType());
        Picasso.with(context)
                .load(MyApplication.imageUri+list.get(position).getTupian())
                .centerCrop()
                .fit()
                .into(holder.imageView);
        holder.itemView.setTag(position);
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
    public void setItemClickListener(RecyclerViewAdapter.OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder{

        private TextView tv, tv_renshu, tv_leixin;
        private ImageView imageView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.item_tv);
            tv_leixin = itemView.findViewById(R.id.item_zhibo_leixin);
            tv_renshu = itemView.findViewById(R.id.item_zhibo_renshu);
            imageView = itemView.findViewById(R.id.item_zhibo_tupian);
        }
    }

}
