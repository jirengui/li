package ruanjianbei.sport.mysport.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.bean.UserIndividualInfoBean;
import ruanjianbei.sport.mysport.util.MyApplication;

/**
 * Created by li on 2018/5/7.
 */
public class RcChengYuanAdapter extends RecyclerView.Adapter<RcChengYuanAdapter.RecyclerViewHolder> implements View.OnClickListener {

    private List<UserIndividualInfoBean> list;
    private Context context;
    private RcChengYuanAdapter.OnItemClickListener mItemClickListener;

    public RcChengYuanAdapter(List<UserIndividualInfoBean> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void replaceAll(List<UserIndividualInfoBean> list) {
        this.list.clear();
        if (list != null && list.size() > 0) {
            this.list.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addAll(List<UserIndividualInfoBean> list) {
        if (this.list != null && list != null) {
            this.list.addAll(list);
            notifyDataSetChanged();
//            notifyItemRangeChanged(dataList.size(),list.size());
        }

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_item_chengyuan,parent,false);
        view.setOnClickListener(this);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        System.out.println("战队成员适配器： " + list.get(position));
        holder.tv_zhiwu.setText(list.get(position).getTeamPermission());
        holder.tv_mingcheng.setText(list.get(position).getVname());
        Picasso.with(context)
                .load(MyApplication.imageUri+list.get(position).getTouxiang())
                .into(holder.touxiang);
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
    public void setItemClickListener(RcChengYuanAdapter.OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }


    class RecyclerViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_mingcheng, tv_zhiwu;
        private CircleImageView touxiang;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tv_mingcheng = (TextView) itemView.findViewById(R.id.item_chengyuan_mingcheng);
            tv_zhiwu = (TextView) itemView.findViewById(R.id.item_chengyuan_zhiwu);
            touxiang = itemView.findViewById(R.id.item_chengyuan_touxiang);
        }
    }

}
