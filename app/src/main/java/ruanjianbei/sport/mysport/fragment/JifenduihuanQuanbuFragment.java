package ruanjianbei.sport.mysport.fragment;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import android.support.v7.widget.RecyclerView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.adapter.RcShangPingAdapter;
import ruanjianbei.sport.mysport.bean.LiWuBean;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.MyApplication;

public class JifenduihuanQuanbuFragment extends Fragment  {

    private PullToRefreshLayout pullToRefreshLayout;
    private RecyclerView rcJifenduihuanquanbu;
    private RcShangPingAdapter adapter;
    private List<LiWuBean> list = new ArrayList<>();
    private List<LiWuBean> list1 = new ArrayList<>();//商品是2
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 11:
                    list1 = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++){
                        if (list.get(i).getType().equals("2")) {
                            list1.add(list.get(i));
                        }
                    }
                    if (adapter == null) {
                        adapter = new RcShangPingAdapter(list1, getContext());
                        rcJifenduihuanquanbu.setLayoutManager(new LinearLayoutManager(getContext()));
                        rcJifenduihuanquanbu.setAdapter(adapter);
                    }else {
                    System.out.println("商品List1的数据：" + list1.get(0).getName());
                    adapter.replaceAll(list1);
                    }

                    // 结束刷新
                    pullToRefreshLayout.finishRefresh();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.jifenduihuan_quanbu, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.pu_quanbu);
        rcJifenduihuanquanbu = (RecyclerView) view.findViewById(R.id.rc_jifenduihuan_quanbu);

        pullToRefreshLayout.autoRefresh();
//        adapter = new RcShangPingAdapter(list1, getContext());
//        rcJifenduihuanquanbu.setLayoutManager(new LinearLayoutManager(getContext()));
//        rcJifenduihuanquanbu.setAdapter(adapter);
        //刷新
        pullToRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtils.post(MyApplication.getLiWuurl, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseBody = response.body().string();
                                System.out.println("礼物信息：" + responseBody + "萨达：" + response.toString());
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<LiWuBean>>() {
                                }.getType();
                                list = gson.fromJson(responseBody, type);
                                if (!list.isEmpty()) {
                                    System.out.println("礼物List的数据：" + list.get(0).getName());
                                }
                                Message msg = handler.obtainMessage();
                                msg.what = 11;
                                msg.obj = list; //把登录结果也发送过去
                                handler.sendMessage(msg);
                            }
                        },null);
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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 结束加载更多
                        pullToRefreshLayout.finishLoadMore();
                    }
                }, 2000);
            }
        });
    }

}


