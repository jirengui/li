package ruanjianbei.sport.mysport.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.bean.PlanBean;
import ruanjianbei.sport.mysport.bean.PlanBean1;
import ruanjianbei.sport.mysport.util.TimeUtil;

public class JihuaItemAdapter extends RecyclerView.Adapter<JihuaItemAdapter.ViewHolder> implements View.OnClickListener {

    private List<PlanBean1> dataList = new ArrayList<>();
    private JihuaItemAdapter.OnItemClickListener mItemClickListener;

    public void replaceAll(List<PlanBean1> list) {
        dataList.clear();
        if (list != null && list.size() > 0) {
            dataList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addAll(List<PlanBean1> list) {
        if (dataList != null && list != null) {
            dataList.addAll(list);
            notifyDataSetChanged();
        }

    }

    @Override
    public JihuaItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.jihua_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final JihuaItemAdapter.ViewHolder holder, final int position) {
        holder.setData(dataList.get(position));
        if (position > 0) {
            if (!dataList.get(position).getTime().equals(dataList.get(position - 1).getTime())) {
                holder.tv_riqi.setVisibility(View.VISIBLE);
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf2 = new SimpleDateFormat("MM月dd日");
                try {
                    String xingqi = TimeUtil.dateToWeek(dataList.get(position).getTime());
                    Date date = sdf1.parse(dataList.get(position).getTime());
                    String riqi = sdf2.format(date) + "  " + xingqi;
                    holder.tv_riqi.setText(riqi);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else {
                holder.tv_riqi.setVisibility(View.GONE);
            }
        } else {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MM月dd日");
            try {
                String xingqi = TimeUtil.dateToWeek(dataList.get(position).getTime());
                Date date = sdf1.parse(dataList.get(position).getTime());
                String riqi = sdf2.format(date) + "  " + xingqi;
                holder.tv_riqi.setText(riqi);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        holder.itemView.setTag(position);
        holder.relativeLayout.setTag(position);
        holder.relativeLayout.setOnClickListener(this);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //((CstSwipeDelMenu) holder.itemView).quickClose();
                    mOnSwipeListener.onDel(holder.getAdapterPosition());
                    holder.swipeMenuLayout.quickClose();
                }
            }
        });
//        //完成
//        holder.btnfinish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mOnSwipeListener) {
//                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
//                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
//                    //((CstSwipeDelMenu) holder.itemView).quickClose();
//                    mOnSwipeListener.onFinish(holder.getAdapterPosition());
//                    holder.swipeMenuLayout.quickClose();
//                    notifyDataSetChanged();
//                }
//            }
//        });
        //修改：
        holder.btnxiugai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    mOnSwipeListener.onXiugai(holder.getAdapterPosition());
                    holder.swipeMenuLayout.quickClose();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int o);
    }

    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onDel(int pos);

        void onXiugai(int pos);
    }

    private onSwipeListener mOnSwipeListener;

    public onSwipeListener getOnDelListener() {
        return mOnSwipeListener;
    }

    public void setOnDelListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }

    public void setItemClickListener(JihuaItemAdapter.OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_starttime, tv_endtime, tv_yundong, tv_riqi;
        private Button btnxiugai;
        private Button btnDelete;
        private SwipeMenuLayout swipeMenuLayout;
        private ImageView yiwancheng, tubiao;
        private boolean aBoolean = false;
        private RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.lll);
            tv_riqi = itemView.findViewById(R.id.jihua_item_riqi);
            tv_starttime = (TextView) itemView.findViewById(R.id.jihua_item_starttime);
            tv_endtime = (TextView) itemView.findViewById(R.id.jihua_item_endtime);
            tv_yundong = itemView.findViewById(R.id.jihua_item_yundong);
            tubiao = itemView.findViewById(R.id.tv_item_yundongtubiao);
            btnDelete = itemView.findViewById(R.id.btnshanchu);
            btnxiugai = (Button) itemView.findViewById(R.id.btnTop);//修改
            swipeMenuLayout = itemView.findViewById(R.id.swipe);
            yiwancheng = itemView.findViewById(R.id.jihua_yiwancheng);
        }

        void setData(PlanBean1 planBean) {
            try {
                aBoolean = TimeUtil.getDayBetweenDate(planBean.getTime());//之后true,之前false
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (aBoolean) {
                if (planBean.getStatus() == 1) {
                    yiwancheng.setVisibility(View.VISIBLE);
                } else {
                    yiwancheng.setVisibility(View.GONE);
                }

                tv_starttime.setTextColor(Color.parseColor("#3cc6b3"));
                tv_endtime.setTextColor(Color.parseColor("#3cc6b3"));
                tv_yundong.setTextColor(Color.parseColor("#101010"));

                tv_starttime.setText(planBean.getStarttime());
                tv_endtime.setText(planBean.getEndtime());
                tv_yundong.setText(planBean.getPlan() + planBean.getShijian() + "分钟");
                switch (planBean.getPlan()) {
                    case "游泳":
                        tubiao.setImageResource(R.drawable.youyonglv);
                        break;
                    case "跑步":
                        tubiao.setImageResource(R.drawable.paobulv);
                        break;
                    case "骑行":
                        tubiao.setImageResource(R.drawable.qixinglv);
                        break;
                    case "跳绳":
                        tubiao.setImageResource(R.drawable.tiaoshenglv);
                        break;
                    case "爬山":
                        tubiao.setImageResource(R.drawable.pashanlv);
                        break;
                    case "步行":
                        tubiao.setImageResource(R.drawable.buxinglv);
                        break;
                }
            }else {
                if (planBean.getStatus() == 1) {
                    yiwancheng.setVisibility(View.VISIBLE);
                    yiwancheng.setImageResource(R.drawable.yiwancheng);
                } else {
                    yiwancheng.setVisibility(View.VISIBLE);
                    yiwancheng.setImageResource(R.drawable.weiwancheng);
                }

                tv_starttime.setTextColor(Color.parseColor("#CACACA"));
                tv_endtime.setTextColor(Color.parseColor("#CACACA"));
                tv_yundong.setTextColor(Color.parseColor("#CACACA"));

                tv_starttime.setText(planBean.getStarttime());
                tv_endtime.setText(planBean.getEndtime());
                tv_yundong.setText(planBean.getPlan() + planBean.getShijian() + "分钟");
                switch (planBean.getPlan()) {
                    case "游泳":
                        tubiao.setImageResource(R.drawable.youyonghui);
                        break;
                    case "跑步":
                        tubiao.setImageResource(R.drawable.paobuhui);
                        break;
                    case "骑行":
                        tubiao.setImageResource(R.drawable.qixinghui);
                        break;
                    case "跳绳":
                        tubiao.setImageResource(R.drawable.tiaoshenghui);
                        break;
                    case "爬山":
                        tubiao.setImageResource(R.drawable.pashanhui);
                        break;
                    case "步行":
                        tubiao.setImageResource(R.drawable.buxinghui);
                        break;
                }
            }
        }
    }


}
