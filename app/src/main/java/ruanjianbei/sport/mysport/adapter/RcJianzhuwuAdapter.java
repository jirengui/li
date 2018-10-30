package ruanjianbei.sport.mysport.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.bean.PaoBuJieGuoBean;

/**
 * Created by li on 2018/5/7.
 */
public class RcJianzhuwuAdapter extends RecyclerView.Adapter<RcJianzhuwuAdapter.RecyclerViewHolder> implements View.OnClickListener {

    private List<String> list;
    private Context context;
    private RcJianzhuwuAdapter.OnItemClickListener mItemClickListener;

    public RcJianzhuwuAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void replaceAll(List<String> list) {
        this.list.clear();
        if (list != null && list.size() > 0) {
            this.list.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addAll(List<String> list) {
        if (this.list != null && list != null) {
            this.list.addAll(list);
            notifyDataSetChanged();
//            notifyItemRangeChanged(dataList.size(),list.size());
        }

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_item_jianzhuwu,null);
        view.setOnClickListener(this);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        System.out.println("命名适配器： " + list.get(position));
        holder.tv_jianzhuwu.setText(list.get(position));
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
    public void setItemClickListener(RcJianzhuwuAdapter.OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }


    class RecyclerViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_jianzhuwu;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tv_jianzhuwu = (TextView) itemView.findViewById(R.id.item_jianzhuwu_tv);
        }
    }

}
