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
import ruanjianbei.sport.mysport.bean.LiWuBean;
import ruanjianbei.sport.mysport.bean.SaiShiBean;
import ruanjianbei.sport.mysport.util.MyApplication;

/**
 * Created by li on 2018/5/7.
 */
public class RcSaishiAdapter extends RecyclerView.Adapter<RcSaishiAdapter.RecyclerViewHolder> implements View.OnClickListener {

    private List<SaiShiBean> list;
    private Context context;
    private RcSaishiAdapter.OnItemClickListener mItemClickListener;

    public RcSaishiAdapter(List<SaiShiBean> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void replaceAll(List<SaiShiBean> list) {
        if (list != null && list.size() > 0) {
            this.list.clear();
            this.list.addAll(list);
            System.out.println("赛事适配器的list：" + list.size());
            notifyDataSetChanged();
        }

    }

    public void addAll(List<SaiShiBean> list) {
        if (this.list != null && list != null) {
            this.list.addAll(list);
            notifyDataSetChanged();
//            notifyItemRangeChanged(dataList.size(),list.size());
        }

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_item_saishi,parent,false);
        view.setOnClickListener(this);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        holder.tv_shijian.setText(list.get(position).getKaishitime());
        holder.tv_neirong.setText(list.get(position).getContext());
        holder.itemView.setTag(position);
        Picasso.with(context)
                .load(MyApplication.imageUri + list.get(position).getTupian())
                .placeholder(context.getResources().getDrawable(R.drawable.wanou))
                .into(holder.imageView);
        if (list.get(position).getStatus() == 2){
            holder.yiguoqi.setVisibility(View.VISIBLE);
        }else {
            holder.yiguoqi.setVisibility(View.GONE);
        }
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
    public void setItemClickListener(RcSaishiAdapter.OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }


    class RecyclerViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_shijian, tv_neirong;
        private ImageView imageView, yiguoqi;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tv_shijian = (TextView) itemView.findViewById(R.id.item_saishi_shijian);
            tv_neirong = (TextView) itemView.findViewById(R.id.item_saishi_neirong);
            imageView = (ImageView) itemView.findViewById(R.id.item_saishi_tupian);
            yiguoqi = itemView.findViewById(R.id.item_saishi_yiguoqi);
        }
    }

}
