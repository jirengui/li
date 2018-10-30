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
import ruanjianbei.sport.mysport.bean.GongGaoBean;
import ruanjianbei.sport.mysport.bean.LiWuBean;
import ruanjianbei.sport.mysport.util.MyApplication;

/**
 * Created by li on 2018/5/7.
 */
public class RcgonggaoAdapter extends RecyclerView.Adapter<RcgonggaoAdapter.RecyclerViewHolder> implements View.OnClickListener {

    private List<GongGaoBean> list;
    private Context context;
    private RcgonggaoAdapter.OnItemClickListener mItemClickListener;

    public RcgonggaoAdapter(List<GongGaoBean> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void replaceAll(List<GongGaoBean> list) {
        if (list != null && list.size() > 0) {
            this.list.clear();
            this.list.addAll(list);
            System.out.println("适配器的list：" + list.size());
            notifyDataSetChanged();
        }

    }

    public void addAll(List<GongGaoBean> list) {
        if (this.list != null && list != null) {
            this.list.addAll(list);
            notifyDataSetChanged();
//            notifyItemRangeChanged(dataList.size(),list.size());
        }

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_gonggao,null);
        view.setOnClickListener(this);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        holder.tv_shijan.setText(list.get(position).getTime());
        holder.tv_neirong.setText(list.get(position).getContext());
        holder.itemView.setTag(position);
        if (position == list.size()-1){
            holder.view.setVisibility(View.GONE);
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
    public void setItemClickListener(RcgonggaoAdapter.OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }


    class RecyclerViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_neirong,tv_shijan;
        private View view;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tv_neirong = (TextView) itemView.findViewById(R.id.item_gonggao_neirong);
            tv_shijan = (TextView) itemView.findViewById(R.id.item_gonggao_shijian);
            view = itemView.findViewById(R.id.item_gonggao_xian);
        }
    }

}
