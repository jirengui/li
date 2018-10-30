package ruanjianbei.sport.mysport.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mabeijianxi.stickydotslib.utils.DisplayUtils;
import com.mabeijianxi.stickydotslib.view.StickyViewHelper;

import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.bean.LiaoTianResultBean;
import ruanjianbei.sport.mysport.bean.UserIndividualInfoBean;

public class HaoyouItemAdapter extends RecyclerView.Adapter<HaoyouItemAdapter.ViewHolder> implements View.OnClickListener {

    private List<LiaoTianResultBean> dataList = new ArrayList<>();
    private OnItemClickListener mItemClickListener;
    private Context mContext;
    public  ArrayList<Integer> removeList =new ArrayList<Integer>();
    public HaoyouItemAdapter(Context context){
        this.mContext = context;
    }
    @Override
    public void onClick(View v) {
        if (mItemClickListener!=null){
            mItemClickListener.onItemClick((Integer) v.getTag());
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void replaceAll(List<LiaoTianResultBean> list) {
        dataList.clear();
        if (list != null && list.size() > 0) {
            dataList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addAll(List<LiaoTianResultBean> list) {
        if (dataList != null && list != null) {
            dataList.addAll(list);
            notifyDataSetChanged();
        }

    }

    @Override
    public HaoyouItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.haoyou_item, parent, false);
        HaoyouItemAdapter.ViewHolder holder = new ViewHolder(view);
        holder.mDragView = (TextView) view.findViewById(R.id.mDragView);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.setData(dataList.get(position).getUserIndividualInfoBean());
        if(removeList.contains(position) ||dataList.get(position).getList() == null ){
            holder.mDragView.setVisibility(View.GONE);
        } else {
            holder.mDragView.setVisibility(View.VISIBLE);
            holder.mDragView.setText(String.valueOf(dataList.get(position).getList().size()));
        }
        StickyViewHelper stickyViewHelper = new StickyViewHelper(mContext, holder.mDragView,R.layout.includeview);
        setViewOut2InRangeUp(stickyViewHelper);
        setViewOutRangeUp(position, stickyViewHelper);
        setViewInRangeUp(stickyViewHelper);
        setViewInRangeMove(stickyViewHelper);
        setViewOutRangeMove(stickyViewHelper);
    }
    /**
     * view在范围外移动执行此Runnable
     * @param stickyViewHelper
     */
    private void setViewOutRangeMove(StickyViewHelper stickyViewHelper) {
        stickyViewHelper.setViewOutRangeMoveRun(new Runnable() {
            @Override
            public void run() {
                DisplayUtils.showToast(mContext, "ViewOutRangeMove");
            }
        });
    }

    /**
     * view在范围内移动指此此Runnable
     * @param stickyViewHelper
     */
    private void setViewInRangeMove(StickyViewHelper stickyViewHelper) {
        stickyViewHelper.setViewInRangeMoveRun(new Runnable() {
            @Override
            public void run() {
                DisplayUtils.showToast(mContext, "ViewInRangeMove");
            }
        });
    }

    /**
     * view没有移出过范围，在范围内松手
     * @param stickyViewHelper
     */
    private void setViewInRangeUp(StickyViewHelper stickyViewHelper) {
        stickyViewHelper.setViewInRangeUpRun(new Runnable() {
            @Override
            public void run() {
                DisplayUtils.showToast(mContext, "ViewInRangeUp");
                notifyDataSetChanged();
            }
        });
    }

    /**
     * view移出范围，最后在范围外松手
     * @param position
     * @param stickyViewHelper
     */
    private void setViewOutRangeUp(final int position, StickyViewHelper stickyViewHelper) {
        stickyViewHelper.setViewOutRangeUpRun(new Runnable() {
            @Override
            public void run() {
                DisplayUtils.showToast(mContext, "ViewOutRangeUp");
                removeList.add(position);
                notifyDataSetChanged();
            }
        });
    }

    /**
     * view移出过范围，最后在范围内松手执行次Runnable
     * @param stickyViewHelper
     */
    private void setViewOut2InRangeUp(StickyViewHelper stickyViewHelper) {
        stickyViewHelper.setViewOut2InRangeUpRun(new Runnable() {
            @Override
            public void run() {
                DisplayUtils.showToast(mContext, "ViewOut2InRangeUp");
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_mingcheng;
        private ImageView im_touxiang;
        private TextView mDragView;
        private TextView tv_jianjie;


        public ViewHolder(View itemView) {
            super(itemView);
            tv_mingcheng = (TextView) itemView.findViewById(R.id.haoyou_mingcheng);
            im_touxiang = itemView.findViewById(R.id.haoyou_touxiang);
            tv_jianjie = itemView.findViewById(R.id.tv_jianjie);
        }

        void setData(UserIndividualInfoBean map) {
            tv_mingcheng.setText(String.valueOf(map.getId()));
            if (map.getStautus() == -1){
                tv_jianjie.setText("离线");
            }else {
                tv_jianjie.setText("在线");
            }
        }
    }
}
