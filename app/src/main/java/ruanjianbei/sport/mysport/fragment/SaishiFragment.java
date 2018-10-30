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
import android.widget.RelativeLayout;

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
import ruanjianbei.sport.mysport.activity.SaishixiangqinActivity;
import ruanjianbei.sport.mysport.adapter.RcSaishiAdapter;
import ruanjianbei.sport.mysport.adapter.RcShangPingAdapter;
import ruanjianbei.sport.mysport.bean.LiWuBean;
import ruanjianbei.sport.mysport.bean.SaiShiBean;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.MyApplication;

public class SaishiFragment extends Fragment {

    private RecyclerView saishiRc;
    private RcSaishiAdapter adapter;
    private List<SaiShiBean> list = new ArrayList<>();
    private PullToRefreshLayout pullToRefreshLayout;
    int i = 2;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 12:
                    adapter.replaceAll(list);
                    // 结束刷新
                    pullToRefreshLayout.finishRefresh();
                    break;
                case 13:
                    adapter.replaceAll(list);
                    // 结束刷新
                    pullToRefreshLayout.finishLoadMore();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.saishi, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        saishiRc = (RecyclerView) view.findViewById(R.id.saishi_rc);

        //当ViewPage多的时候，会执行此函数，所以把需要初始化的在此函数中初始化
        pullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.pu_saishi);
        pullToRefreshLayout.autoRefresh();
//        pullToRefreshLayout.setCanRefresh(false);
        adapter = new RcSaishiAdapter(list, getContext());
        saishiRc.setLayoutManager(new LinearLayoutManager(getContext()));
        saishiRc.setAdapter(adapter);
        adapter.setItemClickListener(new RcSaishiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int o) {
                Intent intent = new Intent();
                intent.putExtra("saishi", list.get(o));
                intent.setClass(getContext(), SaishixiangqinActivity.class);
                startActivity(intent);
            }
        });

        pullToRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, String> map = new HashMap<>();
                        map.put("uid", String.valueOf(MyApplication.user.getId()));
                        map.put("m", "5");
                        HttpUtils.post(MyApplication.getSaiShiurl, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseBody = response.body().string();
                                System.out.println("赛事信息：" + responseBody + "萨达：" + response.toString());
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<SaiShiBean>>() {
                                }.getType();
                                list = gson.fromJson(responseBody, type);
                                if (!list.isEmpty()) {
                                    System.out.println("赛事List的数据：" + list.get(0).getJiangli());
                                }
                                Message msg = handler.obtainMessage();
                                msg.what = 12;
                                msg.obj = list; //把登录结果也发送过去
                                handler.sendMessage(msg);
                            }
                        }, map);
                    }
                }).start();
            }

            /**
             * 加载更多
             */
            @Override
            public void loadMore() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, String> map = new HashMap<>();
                        map.put("uid", String.valueOf(MyApplication.user.getId()));
                        map.put("m", String.valueOf(5*i));
                        i++;
                        HttpUtils.post(MyApplication.getSaiShiurl, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseBody = response.body().string();
                                System.out.println("赛事信息：" + responseBody + "萨达：" + response.toString());
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<SaiShiBean>>() {
                                }.getType();
                                List<SaiShiBean> list1 = new ArrayList<>();
                                list1 = gson.fromJson(responseBody, type);
                                if (!list1.isEmpty()) {
                                    list.addAll(list1);
                                    System.out.println("赛事List的数据：" + list.get(0).getJiangli());
                                }
                                Message msg = handler.obtainMessage();
                                msg.what = 13;
                                msg.obj = list; //把登录结果也发送过去
                                handler.sendMessage(msg);
                            }
                        }, map);
                    }
                }).start();
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
