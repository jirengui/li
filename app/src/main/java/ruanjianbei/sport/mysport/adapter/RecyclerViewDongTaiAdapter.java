package ruanjianbei.sport.mysport.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.bean.LiaoTianResultBean;
import ruanjianbei.sport.mysport.bean.Shequ_DongtaiBean;
import ruanjianbei.sport.mysport.fragment.DongtaiFragment;
import ruanjianbei.sport.mysport.fragment.FragmentDongtaiFragment;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.MyApplication;

/**
 * Created by li on 2018/5/7.
 */
public class RecyclerViewDongTaiAdapter extends RecyclerView.Adapter<RecyclerViewDongTaiAdapter.RecyclerViewHolder> implements View.OnClickListener {

    private List<Shequ_DongtaiBean> list;
    private RecyclerViewDongTaiAdapter.OnItemClickListener mItemClickListener;
    private Context context;
    private int is = -1;



    public RecyclerViewDongTaiAdapter(List<Shequ_DongtaiBean> list, Context context, int is) {
        this.list = list;
        this.context = context;
        this.is = is;
    }
    public RecyclerViewDongTaiAdapter(List<Shequ_DongtaiBean> list, Context context) {
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
        }

    }
    @Override
    public void onClick(View v) {
        if (mItemClickListener!=null){
            mItemClickListener.onItemClick(v,(Integer) v.getTag());
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View v,int o);
        void onGvItemClick(int position, int o);
    }
    public void setItemClickListener(RecyclerViewDongTaiAdapter.OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_item_dongtai,null);
        view.setOnClickListener(this);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {


        Picasso.with(context)
                .load(MyApplication.imageUri+ list.get(position).getDongtai_touxiang())
                .into(holder.im_touxiang);

        MyAdapter1 myAdapter1 = new MyAdapter1(context, list.get(position).getDongtai_tupian());
        holder.gvImage.setAdapter(myAdapter1);
        holder.tv_neirong.setText(list.get(position).getDongtai_neirong());
        holder.tv_fengxiang.setText(list.get(position).getDongtai_fenxiang());
        holder.tv_dianzan.setText(list.get(position).getDongtai_dianzan());
        holder.tv_name.setText(list.get(position).getDongtai_name());
        holder.tv_shijian.setText(list.get(position).getDongtai_shijian());
        holder.tv_pinglun.setText(list.get(position).getDongtai_pinlun());


        myAdapter1.setItemClickListener(new MyAdapter1.OnItemClickListener() {
            @Override
            public void onItemClick(int o) {
                mItemClickListener.onGvItemClick(position, o);
            }
        });
        if (is != -1){
            holder.guanzhu.setVisibility(View.GONE);
        }else {
            holder.guanzhu.setVisibility(View.VISIBLE);
            if (list.get(position).getGuanzhu()){
                holder.guanzhu.setText("已关注");
            }else {
                holder.guanzhu.setText("关注");
            }
        }

        if (list.get(position).getDongtai_name().equals("南信官方")){
            holder.tv_name.setTextColor(Color.parseColor("#FF0000"));
        }else {
            holder.tv_name.setTextColor(Color.parseColor("#101010"));
        }

        System.out.println("位置：" + position + "点赞状态：" + list.get(position).getDianzanstatus());
        if (list.get(position).getDianzanstatus() == 1) {
            holder.im_dianzan.setImageResource(R.drawable.dianzanhou);
            holder.im_dianzan.setTag(1);
        }else {
            holder.im_dianzan.setImageResource(R.drawable.dianzan);
            holder.im_dianzan.setTag(null);
        }

        if (list.get(position).getPinlunstatus() == 1){
            holder.im_pinglun.setImageResource(R.drawable.pinglunhou);
            holder.im_pinglun.setTag(1);
        }else {
            holder.im_pinglun.setImageResource(R.drawable.pinglun);
            holder.im_pinglun.setTag(null);
        }
        if (list.get(position).getFenxiangstatus() == 1){
            holder.im_fengxiang.setImageResource(R.drawable.fenxianghou);
            holder.im_fengxiang.setTag(1);
        }else {
            holder.im_fengxiang.setImageResource(R.drawable.fenxiang);
            holder.im_fengxiang.setTag(null);
        }
        holder.itemView.setTag(position);
        holder.im_touxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(v,position);
            }
        });

        holder.guanzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(v,position);
                if (holder.guanzhu.getText().toString().equals("关注")){
                    System.out.println("关注：" + "1");
                    holder.guanzhu.setText("已关注");
                    Map<String, String> map = new HashMap<>();
                    map.put("uid", String.valueOf(MyApplication.user.getId()));
                    map.put("gid", String.valueOf(list.get(position).getId()));
                    map.put("count", "1");
                    guanzhu(map);
                }else {
                    System.out.println("取消关注：" + "1");
                    holder.guanzhu.setText("关注");
                    Map<String, String> map = new HashMap<>();
                    map.put("uid", String.valueOf(MyApplication.user.getId()));
                    map.put("gid", String.valueOf(list.get(position).getId()));
                    map.put("count", "0");
                    guanzhu(map);
                }
            }
        });
        holder.im_pinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(v,position);
                if (holder.im_pinglun.getTag() == null) {
//                    holder.im_pinglun.setImageResource(R.drawable.pinglunhou);
//                    holder.im_pinglun.setTag(1);
                }else if ((Integer)holder.im_pinglun.getTag() == 1) {
//                    holder.im_pinglun.setImageResource(R.drawable.pinglun);
//                    holder.im_pinglun.setTag(null);
                }
            }
        });
        holder.im_fengxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(v,position);
                if (holder.im_fengxiang.getTag() == null) {
                    holder.im_fengxiang.setImageResource(R.drawable.fenxianghou);
                    holder.im_fengxiang.setTag(1);
                }
            }
        });
        holder.im_dianzan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(v,position);
                System.out.println("输出Tag: " + holder.im_dianzan.getTag());
                if (holder.im_dianzan.getTag() == null) {
                    holder.im_dianzan.setImageResource(R.drawable.dianzanhou);
                    holder.im_dianzan.setTag(1);
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            Map<String, String > map_dianzan = new HashMap<>();
                            map_dianzan.put("uid", String.valueOf(MyApplication.user.getId()));
                            map_dianzan.put("gid", String .valueOf(list.get(position).getId()));
                            map_dianzan.put("count" , "1");
                            String uri_dianzan = "http://192.168.43.44:8080/imooo/dianzan";
                            HttpUtils.post(uri_dianzan, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String responseBody = response.body().string();
                                    System.out.println("点赞获取返回：" + responseBody);
                                }
                            },map_dianzan);
                        }
                    }).start();
                    int a =  Integer.valueOf(holder.tv_dianzan.getText().toString());
                    a++;
                    holder.tv_dianzan.setText(String.valueOf(a));
                }else if ((Integer)holder.im_dianzan.getTag() == 1) {
                    holder.im_dianzan.setImageResource(R.drawable.dianzan);
                    holder.im_dianzan.setTag(null);
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            Map<String, String > map_dianzan = new HashMap<>();
                            map_dianzan.put("uid", String.valueOf(MyApplication.user.getId()));
                            map_dianzan.put("gid", String .valueOf(list.get(position).getId()));
                            map_dianzan.put("count" , "2");
                            String uri_dianzan = "http://192.168.43.44:8080/imooo/dianzan";
                            HttpUtils.post(uri_dianzan, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String responseBody = response.body().string();
                                    System.out.println("点赞获取返回：" + responseBody);
                                }
                            },map_dianzan);
                        }
                    }).start();
                    int a =  Integer.valueOf(holder.tv_dianzan.getText().toString());
                    if (a > 0) {
                        a--;
                        holder.tv_dianzan.setText(String.valueOf(a));
                    }
                }
            }
        });
        holder.tv_pinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(v,position);
                if (holder.im_pinglun.getTag() == null) {
//                    holder.im_pinglun.setImageResource(R.drawable.pinglunhou);
//                    holder.im_pinglun.setTag(1);
                }else if ((Integer)holder.im_pinglun.getTag() == 1) {
//                    holder.im_pinglun.setImageResource(R.drawable.pinglun);
//                    holder.im_pinglun.setTag(null);
                }
            }
        });
        holder.tv_shijian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(v,position);
            }
        });
        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(v,position);
            }
        });
        holder.tv_neirong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(v,position);
            }
        });
        holder.tv_fengxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(v,position);
                if (holder.im_fengxiang.getTag() == null) {
                    holder.im_fengxiang.setImageResource(R.drawable.fenxianghou);
                    holder.im_fengxiang.setTag(1);
                    int a =  Integer.valueOf(holder.tv_fengxiang.getText().toString());
                    a++;
                    holder.tv_fengxiang.setText(a);
                }
            }
        });
        holder.tv_dianzan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(v,position);
                if (holder.im_dianzan.getTag() == null) {
                    holder.im_dianzan.setImageResource(R.drawable.dianzanhou);
                    holder.im_dianzan.setTag(1);
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            Map<String, String > map_dianzan = new HashMap<>();
                            map_dianzan.put("uid", String.valueOf(MyApplication.user.getId()));
                            map_dianzan.put("gid", String .valueOf(list.get(position).getId()));
                            map_dianzan.put("count" , "1");
                            String uri_dianzan = "http://192.168.43.44:8080/imooo/dianzan";
                            HttpUtils.post(uri_dianzan, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String responseBody = response.body().string();
                                    System.out.println("点赞获取返回：" + responseBody);
                                }
                            },map_dianzan);
                        }
                    }).start();
                    int a =  Integer.valueOf(holder.tv_dianzan.getText().toString());
                    a++;
                    holder.tv_dianzan.setText(String.valueOf(a));
                }else if ((Integer)holder.im_dianzan.getTag() == 1) {
                    holder.im_dianzan.setImageResource(R.drawable.dianzan);
                    holder.im_dianzan.setTag(null);
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            Map<String, String > map_dianzan = new HashMap<>();
                            map_dianzan.put("uid", String.valueOf(MyApplication.user.getId()));
                            map_dianzan.put("gid", String .valueOf(list.get(position).getId()));
                            map_dianzan.put("count" , "2");
                            String uri_dianzan = "http://192.168.43.44:8080/imooo/dianzan";
                            HttpUtils.post(uri_dianzan, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String responseBody = response.body().string();
                                    System.out.println("点赞获取返回：" + responseBody);
                                }
                            },map_dianzan);
                        }
                    }).start();
                    int a =  Integer.valueOf(holder.tv_dianzan.getText().toString());
                    if (a > 0) {
                        a++;
                        holder.tv_dianzan.setText(String.valueOf(a));
                    }
                }
            }
        });
    }
    public void guanzhu(final Map<String, String> map) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtils.post(MyApplication.addguanzhuurl, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        System.out.println("添加关注" + response.body().string() + "\n" + response.toString());
                        System.out.println("添加关注ID" + map.get("uid") + "\n" + map.get("gid"));
                    }
                }, map);
            }
        }).start();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_neirong, tv_name, tv_shijian, tv_fengxiang, tv_pinglun, tv_dianzan;
        private ImageView im_fengxiang, im_pinglun, im_dianzan;
        private CircleImageView im_touxiang;
        private GridView gvImage;
        private Button guanzhu;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tv_neirong = (TextView) itemView.findViewById(R.id.item_dongtai_neirong);
            tv_dianzan = itemView.findViewById(R.id.item_dongtai_dianzanshu);
            tv_fengxiang = itemView.findViewById(R.id.item_dongtai_fenxiangshu);
            tv_name = itemView.findViewById(R.id.item_dongtai_name);
            tv_pinglun = itemView.findViewById(R.id.item_dongtai_pinglunshu);
            tv_shijian = itemView.findViewById(R.id.item_dongtai_fabiaoshijian);
            im_dianzan = itemView.findViewById(R.id.item_dongtai_dianzan);
            im_fengxiang = itemView.findViewById(R.id.item_dongtai_fenxiang);
            im_pinglun = itemView.findViewById(R.id.item_dongtai_pinglun);
            im_touxiang = itemView.findViewById(R.id.item_dongtai_touxiang);
            gvImage = (GridView) itemView.findViewById(R.id.item_dongtai_tupian);
            guanzhu = (Button) itemView.findViewById(R.id.item_dongtai_guanzhu);

        }
    }

}
