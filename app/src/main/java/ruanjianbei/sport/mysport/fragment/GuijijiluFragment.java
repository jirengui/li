package ruanjianbei.sport.mysport.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.activity.XianshiguijiActivity;
import ruanjianbei.sport.mysport.adapter.RcGuijijiluAdapter;
import ruanjianbei.sport.mysport.adapter.RczhoubangAdapter;
import ruanjianbei.sport.mysport.bean.PaoBuJieGuoBean;
import ruanjianbei.sport.mysport.bean.Shequ_DongtaiBean;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.MyApplication;

public class GuijijiluFragment extends Fragment  {


    private RecyclerView recyclerView;
    private PullToRefreshLayout pullToRefreshLayout;
    private Map<String, String> map = new HashMap<>();
    private List<PaoBuJieGuoBean> list = new ArrayList<>();
    private RcGuijijiluAdapter adapter;
    private Boolean isone = true;
    private int cishu = 2;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 9:
                    adapter.replaceAll(list);
                    // 结束刷新
                    pullToRefreshLayout.finishRefresh();
                    break;
                case 10:
                    adapter.replaceAll(list);
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
        return inflater.inflate(R.layout.guijijilu, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.pu_guiji);

        recyclerView = view.findViewById(R.id.rc_guiji);
        adapter = new RcGuijijiluAdapter(list, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.setItemClickListener(new RcGuijijiluAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int o) {
                Intent intent = new Intent();
                MyApplication.paoBuJieGuoBean = list.get(o);
                intent.setClass(getContext(), XianshiguijiActivity.class);
                startActivity(intent);
            }
        });

        //刷新
        pullToRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        map.put("uid", String.valueOf(MyApplication.user.getId()));
                        map.put("shuaxinshu", "5");
                        HttpUtils.post(MyApplication.getguijiurl, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseBody = response.body().string();
                                System.out.println("轨迹信息：" + responseBody + "萨达：" + response.toString());
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<PaoBuJieGuoBean>>() {
                                }.getType();
                                list = gson.fromJson(responseBody, type);
                                if (!list.isEmpty()) {
                                    System.out.println("轨迹List的数据：" + list.get(0).getTime());
                                }
                                Message msg = handler.obtainMessage();
                                msg.what = 9;
                                msg.obj = list; //把登录结果也发送过去
                                handler.sendMessage(msg);
                            }
                        }, map);
                        System.out.println("轨迹Map数据：" + map.get("shuaxinshu"));
                    }
                }).start();
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
                        HttpUtils.post(MyApplication.getguijiurl, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseBody = response.body().string();
                                System.out.println("轨迹信息：" + responseBody + "萨达：" + response.toString());
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<PaoBuJieGuoBean>>() {
                                }.getType();
                                List<PaoBuJieGuoBean> listjiazai = new ArrayList<>();
                                listjiazai = gson.fromJson(responseBody, type);
                                if (!list.isEmpty()) {
                                    list.addAll(listjiazai);
                                    System.out.println("轨迹List的数据：" + list.get(0).getTime());
                                }
                                Message msg = handler.obtainMessage();
                                msg.what = 10;
                                msg.obj = list; //把登录结果也发送过去
                                handler.sendMessage(msg);
                            }
                        }, map);
                        System.out.println("轨迹Map数据：" + map.get("shuaxinshu"));
                    }
                }).start();
            }
        });
        //刷新
        pullToRefreshLayout.autoRefresh();
    }

}
