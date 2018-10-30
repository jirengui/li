package ruanjianbei.sport.mysport.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.bean.PaiMingBean;
import ruanjianbei.sport.mysport.bean.PaoBuJieGuoBean;
import ruanjianbei.sport.mysport.util.MyApplication;

/**
 * Created by li on 2018/5/7.
 */
public class RcGuijijiluAdapter extends RecyclerView.Adapter<RcGuijijiluAdapter.RecyclerViewHolder> implements View.OnClickListener {

    private List<PaoBuJieGuoBean> list;
    private Context context;
    private RcGuijijiluAdapter.OnItemClickListener mItemClickListener;

    public RcGuijijiluAdapter(List<PaoBuJieGuoBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void replaceAll(List<PaoBuJieGuoBean> list) {
        this.list.clear();
        if (list != null && list.size() > 0) {
            this.list.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addAll(List<PaoBuJieGuoBean> list) {
        if (this.list != null && list != null) {
            this.list.addAll(list);
            notifyDataSetChanged();
//            notifyItemRangeChanged(dataList.size(),list.size());
        }

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_item_guijijilu, null);
        view.setOnClickListener(this);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        double zonglicheng = 0;
        System.out.println("排名适配器： " + list.get(position).getTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (position > 0) {
                Date startDate = simpleDateFormat.parse(list.get(position).getTime());
                Date endDate = simpleDateFormat.parse(list.get(position - 1).getTime());
                holder.tv_ri.setText(String.valueOf(startDate.getDate()+ "日"));
                if (startDate.getMonth() == endDate.getMonth()){
                    holder.tv_nian.setVisibility(View.GONE);
                    holder.tv_zonglicheng.setVisibility(View.GONE);
                }else {
                    zonglicheng = list.get(position).getLicheng();
                    for (int i = position + 1; i < list.size(); i++)
                    {
                        Date endDate1 = simpleDateFormat.parse(list.get(i).getTime());
                        if (startDate.getMonth() == endDate1.getMonth()){
                            zonglicheng += list.get(i).getLicheng();
                        }
                    }
                    holder.tv_nian.setVisibility(View.VISIBLE);
                    holder.tv_zonglicheng.setVisibility(View.VISIBLE);
                    double licheng = zonglicheng/1000.0;
                    DecimalFormat df = new DecimalFormat("#.0");
                    String str = df.format(licheng);
                    if (licheng < 1){
                        str = "0" + str;
                    }
                    holder.tv_zonglicheng.setText("共计" + str+"公里");
                }
            }else {
                Date startDate = simpleDateFormat.parse(list.get(position).getTime());
                zonglicheng = list.get(position).getLicheng();
                for (int i = position + 1; i < list.size(); i++)
                {
                    Date endDate1 = simpleDateFormat.parse(list.get(i).getTime());
                    if (startDate.getMonth() == endDate1.getMonth()){
                        zonglicheng += list.get(i).getLicheng();
                    }
                }
                holder.tv_ri.setText(String.valueOf(startDate.getDate() + "日"));
                holder.tv_nian.setVisibility(View.VISIBLE);
                holder.tv_zonglicheng.setVisibility(View.VISIBLE);
                double licheng = zonglicheng/1000.0;
                DecimalFormat df = new DecimalFormat("#.0");
                String str = df.format(licheng);
                if (licheng < 1){
                    str = "0" + str;
                }
                holder.tv_zonglicheng.setText("共计" + str+"公里");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.itemView.setTag(position);
        int licheng = (int) Math.floor(list.get(position).getLicheng());
        holder.tv_licheng.setText(String.valueOf(licheng) + "米");
        System.out.println("跑步配速：" +list.get(position).getPeisu() + "ss: " + list.get(position).getLicheng() );
        String[] ss = list.get(position).getPeisu().split("'");

        int fen = Integer.parseInt(ss[0]);
        int miao = Integer.parseInt(ss[1]);
        int shijian = (int) ((list.get(position).getLicheng()/1000.0) * (fen*60 + miao));
        String shijiana = String.valueOf(shijian/60)+ "分" + String.valueOf(shijian - (shijian/60)+"秒");
        holder.tv_paobuyongshi.setText(shijiana);
//        Picasso.with(context)
//                .load(MyApplication.imageUri + list.get(position).getJietu())
//                .into(holder.imageView);
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
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int o);
    }

    public void setItemClickListener(RcGuijijiluAdapter.OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }


    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_nian, tv_licheng, tv_paobuyongshi, tv_zonglicheng, tv_ri;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tv_nian = (TextView) itemView.findViewById(R.id.item_guiji_nian);
            tv_licheng = (TextView) itemView.findViewById(R.id.item_guiji_paobulicheng);
            tv_paobuyongshi = (TextView) itemView.findViewById(R.id.item_guiji_paobuyongshi);
            tv_ri = itemView.findViewById(R.id.item_guiji_ri);
            tv_zonglicheng = itemView.findViewById(R.id.item_guiji_zongji);
        }
    }

}
