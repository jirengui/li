package ruanjianbei.sport.mysport.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import ruanjianbei.sport.mysport.adapter.RcDingdanAdapter;
import ruanjianbei.sport.mysport.adapter.RcSaishiAdapter;
import ruanjianbei.sport.mysport.bean.DuihuanjiluBean;
import ruanjianbei.sport.mysport.bean.SaiShiBean;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.MyApplication;

import static java.security.AccessController.getContext;

public class WodedingdanActivity extends Activity implements View.OnClickListener {

    private ImageView wodedingdanFanhui;
    private PullToRefreshLayout pullToRefreshLayout;
    private RecyclerView recyclerView;
    private List<DuihuanjiluBean> list = new ArrayList<>();
    private RcDingdanAdapter adapter ;
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
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //状态栏颜色
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.wodedingdan);

        wodedingdanFanhui = (ImageView) findViewById(R.id.wodedingdan_fanhui);
        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pu_dingdan);
        pullToRefreshLayout.autoRefresh();
        pullToRefreshLayout.setCanLoadMore(false);

        adapter = new RcDingdanAdapter(list,this);
        recyclerView = findViewById(R.id.wodedingdan_rc);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        wodedingdanFanhui.setOnClickListener(this);

        pullToRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, String> map = new HashMap<>();
                        map.put("uid", String.valueOf(MyApplication.user.getId()));
                        HttpUtils.post(MyApplication.duihuanjilu, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseBody = response.body().string();
                                System.out.println("订单列表：" + responseBody);
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<DuihuanjiluBean>>() {
                                }.getType();
                                list = gson.fromJson(responseBody, type);
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

            }
        });
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wodedingdan_fanhui:
                finish();
                break;
        }
    }
}
