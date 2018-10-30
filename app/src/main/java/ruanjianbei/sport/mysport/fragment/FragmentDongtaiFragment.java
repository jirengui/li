package ruanjianbei.sport.mysport.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;


import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.activity.DongtaipinglunActivity;
import ruanjianbei.sport.mysport.activity.ImageDetailsActivity;
import ruanjianbei.sport.mysport.adapter.RecyclerViewDongTaiAdapter;
import ruanjianbei.sport.mysport.bean.PaoBuJieGuoBean;
import ruanjianbei.sport.mysport.bean.Shequ_DongtaiBean;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.MyApplication;


public class FragmentDongtaiFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerViewDongTaiAdapter adapter;
    private AppBarLayout appBarLayout;
    //刷新
    private PullToRefreshLayout pullToRefreshLayout;


    private List<Shequ_DongtaiBean> list = new ArrayList<>();
    private Map<String, String> map = new HashMap<>();
    private Boolean isone = true;
    private int shuaxinshu = 5;
    private int cishu = 2;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 5:
                    if (!list.isEmpty()) {
                        System.out.println("消息中的List的数据：" + list.get(0));
                    }
                    adapter.replaceAll(list);
                    // 结束刷新
                    if (msg.arg1 == 1){
                        pullToRefreshLayout.finishLoadMore();
                    }else {
                        pullToRefreshLayout.finishRefresh();
                    }
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dongtai, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //当ViewPage多的时候，会执行此函数，所以把需要初始化的在此函数中初始化
        pullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.pu_dongtai);
        pullToRefreshLayout.setCanLoadMore(false);
//        pullToRefreshLayout.setCanRefresh(false);
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        //检测折叠的时候的在屏幕的高度
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                System.out.println("标题栏改变大小： " + verticalOffset);
                if(verticalOffset <= -140){
                    //false为白色字体，ture为黑色字体
                    StatusBarCompat.setLightStatusBar(getActivity().getWindow(), true);
                }else {
                    StatusBarCompat.setLightStatusBar(getActivity().getWindow(), false);
                }
                if (verticalOffset == -appBarLayout.getTotalScrollRange()) {
                    System.out.println("完全折叠： " + verticalOffset);
                    pullToRefreshLayout.setCanLoadMore(true);
//                    pullToRefreshLayout.setCanRefresh(true);
                }
            }
        });
        //刷新
        if (isone) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    map.put("shuaxinshu", String.valueOf(shuaxinshu));
                    map.put("uid", String.valueOf(MyApplication.user.getId()));
                    HttpUtils.post(MyApplication.getshuoshuoUri, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseBody = response.body().string();
                            System.out.println("社区信息：" + responseBody + "萨达：" + response.toString());
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<Shequ_DongtaiBean>>() {
                            }.getType();
                            list = gson.fromJson(responseBody, type);
                            if (!list.isEmpty()) {
                                System.out.println("List的数据：" + list.get(0).getDongtai_touxiang());
                            }
                            Message msg = handler.obtainMessage();
                            msg.what = 5;
                            msg.obj = list; //把登录结果也发送过去
                            handler.sendMessage(msg);
                        }
                    }, map);
                    System.out.println("Map数据：" + map.get("shuaxinshu"));
                }
            }).start();
            isone = false;
        }
        //刷新
        pullToRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        map.put("shuaxinshu", String.valueOf(shuaxinshu));
                        map.put("uid", String.valueOf(MyApplication.user.getId()));
                        HttpUtils.post(MyApplication.getshuoshuoUri, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseBody = response.body().string();
                                System.out.println("社区信息：" + responseBody + "萨达：" + response.toString());
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<Shequ_DongtaiBean>>() {
                                }.getType();
                                list = gson.fromJson(responseBody, type);
                                if (!list.isEmpty()) {
                                    System.out.println("List的数据：" + list.get(0).getDongtai_touxiang());
                                }
                                Message msg = handler.obtainMessage();
                                msg.what = 5;
                                msg.obj = list; //把登录结果也发送过去
                                handler.sendMessage(msg);
                            }
                        }, map);
                        System.out.println("Map数据：" + map.get("shuaxinshu"));
                    }
                }).start();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        // 结束刷新
//                        pullToRefreshLayout.finishRefresh();
//                    }
//                }, 2000);
            }

            @Override
            public void loadMore() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int ssss = cishu*5;
                        cishu++;
                        map.put("uid", String.valueOf(MyApplication.user.getId()));
                        map.put("shuaxinshu", String.valueOf(ssss));
                        HttpUtils.post(MyApplication.getshuoshuoUri, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseBody = response.body().string();
                                System.out.println("轨迹信息：" + responseBody + "萨达：" + response.toString());
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<Shequ_DongtaiBean>>() {
                                }.getType();
                                List<Shequ_DongtaiBean> listjiazai = new ArrayList<>();
                                listjiazai = gson.fromJson(responseBody, type);
                                if (!list.isEmpty()) {
                                    list.addAll(listjiazai);
                                    System.out.println("轨迹List的数据：" + list.get(0).getDongtai_name());
                                }
                                Message msg = handler.obtainMessage();
                                msg.what = 5;
                                msg.arg1 = 1;//把登录结果也发送过去
                                handler.sendMessage(msg);
                            }
                        }, map);
                        System.out.println("轨迹Map数据：" + map.get("shuaxinshu"));
                    }
                }).start();
            }
        });
        recyclerView = view.findViewById(R.id.rc_dongtai);
        adapter = new RecyclerViewDongTaiAdapter(list, getContext());


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        pullToRefreshLayout.autoRefresh();
        adapter.setItemClickListener(new RecyclerViewDongTaiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, final int o) {
                switch (v.getId()) {
                    case R.id.item_dongtai_dianzanshu:
                        list.remove(o);
                        adapter.notifyDataSetChanged();
                        break;
                    case R.id.item_dongtai_pinglun:
                    case R.id.item_dongtai_pinglunshu:
                        Intent intent = new Intent();
                        intent.putExtra("bean", list.get(o));
                        intent.setClass(getContext(), DongtaipinglunActivity.class);
                        startActivity(intent);
                        break;
                }
            }

            @Override
            public void onGvItemClick(int position, int o) {
                Intent intent1 = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("image_position", o);
                bundle.putStringArrayList("imags", (ArrayList<String>) list.get(position).getDongtai_tupian());
                intent1.putExtra("bu", bundle);
                intent1.setClass(getContext(), ImageDetailsActivity.class);
                startActivity(intent1);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (pullToRefreshLayout != null) {
            pullToRefreshLayout.autoRefresh();
        }
    }
}
