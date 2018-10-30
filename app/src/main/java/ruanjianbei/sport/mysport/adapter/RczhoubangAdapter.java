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
import ruanjianbei.sport.mysport.bean.PaiMingBean;
import ruanjianbei.sport.mysport.bean.RepinlunBean;
import ruanjianbei.sport.mysport.util.MyApplication;

/**
 * Created by li on 2018/5/7.
 */
public class RczhoubangAdapter extends RecyclerView.Adapter<RczhoubangAdapter.RecyclerViewHolder> implements View.OnClickListener {

    private List<PaiMingBean> list;
    private Context context;
    private RczhoubangAdapter.OnItemClickListener mItemClickListener;

    public RczhoubangAdapter(List<PaiMingBean> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void replaceAll(List<PaiMingBean> list) {
        this.list.clear();
        if (list != null && list.size() > 0) {
            this.list.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addAll(List<PaiMingBean> list) {
        if (this.list != null && list != null) {
            this.list.addAll(list);
            notifyDataSetChanged();
//            notifyItemRangeChanged(dataList.size(),list.size());
        }

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_item_paiming,null);
        view.setOnClickListener(this);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        System.out.println("排名适配器： " + list.get(position).getName());
        holder.tv_name.setText(list.get(position).getName());
        holder.tv_licheng.setText(String.valueOf(list.get(position).getLicheng()));
        holder.itemView.setTag(position);
        Picasso.with(context)
                .load(MyApplication.imageUri + list.get(position).getTouxiang())
                .into(holder.imageView);
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
    public void setItemClickListener(RczhoubangAdapter.OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }


    class RecyclerViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_name, tv_licheng;
        private CircleImageView imageView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.paiming_item_name);
            tv_licheng = (TextView) itemView.findViewById(R.id.paiming_item_gongli);
            imageView = (CircleImageView) itemView.findViewById(R.id.paiming_item_touxiang);
        }
    }

}
