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
import ruanjianbei.sport.mysport.bean.DuihuanjiluBean;
import ruanjianbei.sport.mysport.bean.SaiShiBean;
import ruanjianbei.sport.mysport.util.MyApplication;

/**
 * Created by li on 2018/5/7.
 */
public class RcDingdanAdapter extends RecyclerView.Adapter<RcDingdanAdapter.RecyclerViewHolder> implements View.OnClickListener {

    private List<DuihuanjiluBean> list;
    private Context context;
    private RcDingdanAdapter.OnItemClickListener mItemClickListener;

    public RcDingdanAdapter(List<DuihuanjiluBean> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void replaceAll(List<DuihuanjiluBean> list) {
        if (list != null && list.size() > 0) {
            this.list.clear();
            this.list.addAll(list);
            System.out.println("兑换礼物适配器的list：" + list.size());
            notifyDataSetChanged();
        }

    }

    public void addAll(List<DuihuanjiluBean> list) {
        if (this.list != null && list != null) {
            this.list.addAll(list);
            notifyDataSetChanged();
//            notifyItemRangeChanged(dataList.size(),list.size());
        }

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_item_wodedingdan,parent,false);
        view.setOnClickListener(this);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        holder.dingdanShijian.setText(String.valueOf(list.get(position).getTime()));
        holder.dingdanName.setText(list.get(position).getLiWuBean().getName());
        holder.dingdanGeshu.setText("共计：" + String.valueOf(list.get(position).getCount()) + "个" );
        holder.itemView.setTag(position);
        Picasso.with(context)
                .load(MyApplication.imageUri + list.get(position).getLiWuBean().getTupian())
                .placeholder(context.getResources().getDrawable(R.drawable.wanou))
                .into(holder.dingdanTu);
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
    public void setItemClickListener(RcDingdanAdapter.OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }


    class RecyclerViewHolder extends RecyclerView.ViewHolder{

        private ImageView dingdanTu;
        private TextView dingdanName;
        private TextView dingdanShijian;//时间
        private TextView dingdanGeshu;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            dingdanTu = (ImageView) itemView.findViewById(R.id.dingdan_tu);
            dingdanName = (TextView) itemView.findViewById(R.id.dingdan_name);
            dingdanShijian = (TextView) itemView.findViewById(R.id.dingdan_leibie);
            dingdanGeshu = itemView.findViewById(R.id.dingdan_geshu);

        }
    }

}
