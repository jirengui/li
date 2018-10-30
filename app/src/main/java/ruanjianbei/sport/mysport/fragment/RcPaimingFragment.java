package ruanjianbei.sport.mysport.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.activity.DongtaipinglunActivity;
import ruanjianbei.sport.mysport.activity.PinlunhuifuActivity;
import ruanjianbei.sport.mysport.adapter.RczhoubangAdapter;
import ruanjianbei.sport.mysport.adapter.RecyclerViewPinLunAdapter;
import ruanjianbei.sport.mysport.bean.PaiMingBean;
import ruanjianbei.sport.mysport.bean.PinlunBean;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.MyApplication;

public class RcPaimingFragment extends Fragment  {

    private RecyclerView paimingRc;
    private RczhoubangAdapter adapter;
    private List<PaiMingBean> list = new ArrayList<>();
    private boolean isone = true;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 7:
                    if (!list.isEmpty()) {
                        System.out.println("消息中的List的数据：" + list.get(0).getName());
                    }
                    // 结束刷新
//                    pullToRefreshLayout.finishRefresh();
                    if (isone) {
                        isone = false;
                        adapter = new RczhoubangAdapter(list, getContext());
                        paimingRc.setLayoutManager(new LinearLayoutManager(getContext()));
                        paimingRc.setAdapter(adapter);
                    } else {
                        adapter.replaceAll(list);
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
        return inflater.inflate(R.layout.rc_paiming, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        paimingRc = (RecyclerView) view.findViewById(R.id.paiming_rc);

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtils.postJson(MyApplication.getpaimingurl, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String ss = response.body().string();
                        System.out.println("排名返回： " + ss);
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<PaiMingBean>>() {
                        }.getType();
                        list = gson.fromJson(ss, type);
                        Message msg = handler.obtainMessage();
                        msg.what = 7;
                        msg.obj = list; //把登录结果也发送过去
                        handler.sendMessage(msg);
                    }
                },"aaa");
            }
        }).start();

    }

}
