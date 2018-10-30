package ruanjianbei.sport.mysport.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.bean.RepinlunBean;
import ruanjianbei.sport.mysport.bean.Shequ_DongtaiBean;
import ruanjianbei.sport.mysport.util.MyApplication;

/**
 * Created by li on 2018/5/7.
 */
public class RvWoDeFaBuAdapter extends RecyclerView.Adapter<RvWoDeFaBuAdapter.RecyclerViewHolder> implements View.OnClickListener {

    private List<Shequ_DongtaiBean> list;
    private Context context;
    private RvWoDeFaBuAdapter.OnItemClickListener mItemClickListener;

    public RvWoDeFaBuAdapter(List<Shequ_DongtaiBean> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void replaceAll(List<Shequ_DongtaiBean> list) {
        this.list.clear();
        if (list != null && list.size() > 0) {
            this.list.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addAll(List<Shequ_DongtaiBean> list) {
        if (this.list != null && list != null) {
            this.list.addAll(list);
            notifyDataSetChanged();
//            notifyItemRangeChanged(dataList.size(),list.size());
        }

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_item_wodefabu,parent,false);
        view.setOnClickListener(this);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
//        holder.tv_ri.setText(list.get(position).getHuifuBean().getName());
//        holder.tv_yue.setText(list.get(position).getContext().get(0));
        holder.tv_neirong.setText(list.get(position).getDongtai_neirong());
        SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm" );
        try {
            Date date = sdf.parse(list.get(position).getDongtai_shijian());
            holder.tv_ri.setText(String.valueOf(date.getDate()));
            holder.tv_yue.setText(String.valueOf(date.getMonth() + 1)+"月");
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
    public void setItemClickListener(RvWoDeFaBuAdapter.OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }


    class RecyclerViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_ri, tv_yue, tv_neirong;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tv_ri = (TextView) itemView.findViewById(R.id.wodefabu_ri);
            tv_yue = (TextView) itemView.findViewById(R.id.wodefabu_yue);
            tv_neirong = (TextView) itemView.findViewById(R.id.wodefabu_neirong);
        }
    }

}
