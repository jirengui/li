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
import ruanjianbei.sport.mysport.bean.PinlunBean;
import ruanjianbei.sport.mysport.util.MyApplication;

/**
 * Created by li on 2018/5/7.
 */
public class RecyclerViewPinLunAdapter extends RecyclerView.Adapter<RecyclerViewPinLunAdapter.RecyclerViewHolder> implements View.OnClickListener {

    private List<PinlunBean> list;
    private Context context;
    private RecyclerViewPinLunAdapter.OnItemClickListener mItemClickListener;

    public RecyclerViewPinLunAdapter(List<PinlunBean> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void replaceAll(List<PinlunBean> list) {
        this.list.clear();
        if (list != null && list.size() > 0) {
            this.list.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addAll(List<PinlunBean> list) {
        if (this.list != null && list != null) {
            this.list.addAll(list);
            notifyDataSetChanged();
//            notifyItemRangeChanged(dataList.size(),list.size());
        }

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_item_pinlun,null);
        view.setOnClickListener(this);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.tv_name.setText(list.get(position).getUserIndividualInfoBean().getName());
        holder.tv_shijian.setText(list.get(position).getContext().get(0));
        holder.tv_neirong.setText(list.get(position).getContext().get(1));
        holder.itemView.setTag(position);
        Picasso.with(context)
                .load(MyApplication.imageUri + list.get(position).getUserIndividualInfoBean().getTouxiang())
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
        void onItemClick(View v,int o);
    }
    public void setItemClickListener(RecyclerViewPinLunAdapter.OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }


    class RecyclerViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_name, tv_shijian, tv_neirong;
        private ImageView imageView, im_dianzan, im_pinlunhuifu;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.item_pinlun_name);
            tv_shijian = (TextView) itemView.findViewById(R.id.item_pinlun_fabiaoshijian);
            tv_neirong = (TextView) itemView.findViewById(R.id.item_pinlun_neirong);
            imageView = (ImageView) itemView.findViewById(R.id.item_pinlun_touxiang);
            im_dianzan = itemView.findViewById(R.id.item_pinlun_dianzan);
            im_pinlunhuifu = itemView.findViewById(R.id.item_pinlun_huifu);
        }
    }

}
