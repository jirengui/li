package ruanjianbei.sport.mysport.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

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
import ruanjianbei.sport.mysport.adapter.RvWoDeFaBuAdapter;
import ruanjianbei.sport.mysport.bean.RepinlunBean;
import ruanjianbei.sport.mysport.bean.Shequ_DongtaiBean;
import ruanjianbei.sport.mysport.bean.UserIndividualInfoBean;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.MyApplication;

public class WodefabuFragment extends Fragment  {

    private RecyclerView wodefabuRc;
    private RvWoDeFaBuAdapter woDeFaBuAdapter;
    private List<Shequ_DongtaiBean> list = new ArrayList<>();
    private List<String> list1 = new ArrayList<>();
    private int shuaxinshu = 10;
    private int cishu = 2;
    private AppBarLayout appBarLayout;


    //刷新
    private PullToRefreshLayout pullToRefreshLayout;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 5:
                    if (!list.isEmpty()) {
                        System.out.println("我的发布中的List的数据：" + list.get(0));
                    }
                    woDeFaBuAdapter.replaceAll(list);
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
        return inflater.inflate(R.layout.wodefabu, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.wodefabu_pu);
        pullToRefreshLayout.setCanLoadMore(false);
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.app_bar_layout);
        //检测折叠的时候的在屏幕的高度
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                System.out.println("标题栏改变大小： " + verticalOffset);
                if (verticalOffset == -appBarLayout.getTotalScrollRange()) {
                    System.out.println("完全折叠： " + verticalOffset);
                    pullToRefreshLayout.setCanLoadMore(true);
//                    pullToRefreshLayout.setCanRefresh(true);
                }
            }
        });
        //刷新
        pullToRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String,String> map = new HashMap<>();
                        map.put("count", String.valueOf(shuaxinshu));
                        map.put("uid", String.valueOf(MyApplication.user.getId()));
                        HttpUtils.post(MyApplication.getShuoshuoById, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseBody = response.body().string();
                                System.out.println("我的发布信息：" + responseBody + "萨达：" + response.toString());
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<Shequ_DongtaiBean>>() {
                                }.getType();
                                list = gson.fromJson(responseBody, type);
                                if (!list.isEmpty()) {
                                    System.out.println("我的发布List的数据：" + list.get(0).getDongtai_touxiang());
                                }
                                Message msg = handler.obtainMessage();
                                msg.what = 5;
                                msg.obj = list; //把登录结果也发送过去
                                handler.sendMessage(msg);
                            }
                        }, map);
                        System.out.println("我的发布Map数据：" + map.get("count"));
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
                        Map<String,String> map = new HashMap<>();
                        int ssss = cishu*5;
                        cishu++;
                        map.put("uid", String.valueOf(MyApplication.user.getId()));
                        map.put("count", String.valueOf(ssss));
                        HttpUtils.post(MyApplication.getShuoshuoById, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseBody = response.body().string();
                                System.out.println("我的发布下拉信息：" + responseBody + "萨达：" + response.toString());
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<Shequ_DongtaiBean>>() {
                                }.getType();
                                List<Shequ_DongtaiBean> listjiazai = new ArrayList<>();
                                listjiazai = gson.fromJson(responseBody, type);
                                if (!list.isEmpty()) {
                                    list.addAll(listjiazai);
                                    System.out.println("我的发布下拉List的数据：" + list.get(0).getDongtai_name());
                                }
                                Message msg = handler.obtainMessage();
                                msg.what = 5;
                                msg.arg1 = 1;//把登录结果也发送过去
                                handler.sendMessage(msg);
                            }
                        }, map);
                        System.out.println("我的发布下拉Map数据：" + map.get("count"));
                    }
                }).start();
            }
        });
        pullToRefreshLayout.autoRefresh();
        wodefabuRc = (RecyclerView) view.findViewById(R.id.wodefabu_rc);
        woDeFaBuAdapter = new RvWoDeFaBuAdapter(list, getContext());
        wodefabuRc.setLayoutManager(new LinearLayoutManager(getActivity()));
        wodefabuRc.setAdapter(woDeFaBuAdapter);


        woDeFaBuAdapter.setItemClickListener(new RvWoDeFaBuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int o) {
                Intent intent = new Intent();
                intent.putExtra("bean", list.get(o));
                intent.setClass(getContext(), DongtaipinglunActivity.class);
                startActivity(intent);
            }
        });
    }

}
