package ruanjianbei.sport.mysport.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.activity.JifenzhongxinActivity;
import ruanjianbei.sport.mysport.bean.LiWuBean;
import ruanjianbei.sport.mysport.bean.PaoBuJieGuoBean;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.MyApplication;

/**
 * Created by li on 2018/5/7.
 */
public class RcShangPingAdapter extends RecyclerView.Adapter<RcShangPingAdapter.RecyclerViewHolder> implements View.OnClickListener {

    private List<LiWuBean> list;
    private Context context;
    private RcShangPingAdapter.OnItemClickListener mItemClickListener;

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int o);
    }

    public void setItemClickListener(RcShangPingAdapter.OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public RcShangPingAdapter(List<LiWuBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void replaceAll(List<LiWuBean> list) {
        if (list != null && list.size() > 0) {
            this.list.clear();
            this.list.addAll(list);
            System.out.println("适配器的list：" + list.size());
            notifyDataSetChanged();
        }

    }

    public void addAll(List<LiWuBean> list) {
        if (this.list != null && list != null) {
            this.list.addAll(list);
            notifyDataSetChanged();
//            notifyItemRangeChanged(dataList.size(),list.size());
        }

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_item_shangping, null);
        view.setOnClickListener(this);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        System.out.println("礼物适配器： " + list.get(position).getName());
        holder.tv_name.setText(list.get(position).getName());
        holder.tv_jifen.setText(list.get(position).getJifen());
        holder.tv_kucun.setText(String.valueOf(list.get(position).getKucun()));
        holder.itemView.setTag(position);

        holder.tv_duihuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mItemClickListener.onItemClick(v,position);
                if (Integer.parseInt(MyApplication.user.getJifen()) > Integer.parseInt(list.get(position).getJifen())) {
                    updatajifen("-" + list.get(position).getJifen());
                    Map<String, String> map = new HashMap<>();
                    map.put("uid", String.valueOf(MyApplication.user.getId()));
                    map.put("lid", String.valueOf(list.get(position).getId()));
                    map.put("count", "1");
                    HttpUtils.post(MyApplication.duihuan, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                        }
                    }, map);
                    holder.tv_duihuan.setText("已兑换");
                } else {
                    Toast toast = Toast.makeText(context, "积分不足，无法兑换", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

        Picasso.with(context)
                .load(MyApplication.imageUri + list.get(position).getTupian())
                .placeholder(context.getResources().getDrawable(R.drawable.wanou))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name, tv_jifen, tv_kucun, tv_duihuan;
        private ImageView imageView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.item_shangping_name);
            tv_jifen = (TextView) itemView.findViewById(R.id.item_shangping_jifen);
            tv_kucun = (TextView) itemView.findViewById(R.id.item_shangping_kucun);
            tv_duihuan = (TextView) itemView.findViewById(R.id.item_shangping_duihuan);
            imageView = (ImageView) itemView.findViewById(R.id.item_shangping_tupian);
        }
    }

    private void updatajifen(String jifen) {
        int jifen1 = 0;
        if (MyApplication.user.getJifen() != null) {
            jifen1 = Integer.parseInt(MyApplication.user.getJifen());
            jifen1 += Integer.parseInt(jifen);
        }
        MyApplication.user.setJifen(String.valueOf(jifen1));
        Map<String, String> map = new HashMap<>();
        map.put("uid", String.valueOf(MyApplication.user.getId()));
        map.put("count", jifen);
        map.put("shijian", "兑换");
        HttpUtils.post(MyApplication.updatejifenurl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("兑换：" + response.body().string());
            }
        }, map);
    }
}
