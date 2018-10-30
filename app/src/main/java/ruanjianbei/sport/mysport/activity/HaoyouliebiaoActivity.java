package ruanjianbei.sport.mysport.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.adapter.HaoyouItemAdapter;
import ruanjianbei.sport.mysport.bean.LiaoTianResultBean;
import ruanjianbei.sport.mysport.bean.UserIndividualInfoBean;
import ruanjianbei.sport.mysport.bean.MessageEvent;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.MyApplication;
import ruanjianbei.sport.mysport.util.Xiaoxilei;

public class HaoyouliebiaoActivity extends Activity  {

    private RecyclerView haoyouliebiaoRc;
    private List<UserIndividualInfoBean> objects = new ArrayList<>();
    private List<LiaoTianResultBean> objects1 = new ArrayList<>();
    private LiaoTianResultBean liaoTianResultBean;
    private HaoyouItemAdapter haoyouItemAdapter;

    private Map<String, String> map = new HashMap<>();
    private HashMap map_shuju = null;
    private String userId;
    private Map<String, List<String>> map_xiaoxi = new HashMap<>();
    private List<String> list = new ArrayList<>();
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 456:
                    System.out.println("列表：" +  objects.size());
                    list = new ArrayList<>();
                    for (int i = 0; i < objects.size(); i++){
                        liaoTianResultBean = new LiaoTianResultBean();
                        liaoTianResultBean.setUserIndividualInfoBean(objects.get(i));
                        objects1.add(liaoTianResultBean);
                    }
                    haoyouItemAdapter.addAll(objects1);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (map_shuju != null){
                                for (Object obj : map_shuju.keySet()) {
                                    System.out.println("key=" + obj + " value=" + map_shuju.get(obj));
                                    String a[] = map_shuju.get(obj).toString().split("pIN1j0fd");
                                    list = new ArrayList<>();
                                    Collections.addAll(list, a);
                                    map_xiaoxi.put((String) obj,list);
                                }
                            }
                            for (int i = 0; i < objects1.size(); i++){
                                System.out.println("好友列表：" + map_xiaoxi.get(String.valueOf(objects.get(i).getId())));
                                if (map_xiaoxi.containsKey(String.valueOf(objects1.get(i).getUserIndividualInfoBean().getId()))) {
                                    list = map_xiaoxi.get(String.valueOf(objects1.get(i).getUserIndividualInfoBean().getId()));
                                    objects1.get(i).setList(list);
                                }
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    haoyouItemAdapter.replaceAll(objects1);
                                }
                            });

                        }
                    }).start();

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.haoyouliebiao);

        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        final Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        map_shuju = (HashMap) intent.getSerializableExtra("shuju");
        map.put("userId", userId);
        System.out.println("UserId: " + userId);

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                map22.put("uid", new String[]{String.valueOf(userId)});
//                map22.put("gid", new String[]{editText.getText().toString()});
//                HttpUtils.postshuzu(MyApplication.addfriendurl, new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        System.out.println("添加好友：" + response.body().string());
//                    }
//                },map22);
//            }
//        });
        haoyouliebiaoRc = (RecyclerView) findViewById(R.id.haoyouliebiao_rc);
        haoyouItemAdapter = new HaoyouItemAdapter(HaoyouliebiaoActivity.this);
        haoyouliebiaoRc.setLayoutManager(new LinearLayoutManager(this));
        haoyouliebiaoRc.setAdapter(haoyouItemAdapter);
        ImageView fanhui = findViewById(R.id.haoyouliebiao_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        HttpUtils.post(MyApplication.postFriendurl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                    String responseBody = response.body().string();
                    JSONObject json = null;
                    try {
                        json = new JSONObject(responseBody);
                        System.out.println("Json数据长度为：    " + json.length());
                        Iterator key = json.keys();
                        while (key.hasNext()) {
                            String jsonkey = key.next().toString();
                            System.out.println("JSON的key值为：     " + jsonkey);
                            String jsonlist = null;
                            jsonlist = json.get(jsonkey).toString();
                            Gson gson = new Gson();
                            UserIndividualInfoBean m_result = gson.fromJson(jsonlist, UserIndividualInfoBean.class);
                            System.out.println("Json数据为：    " + jsonlist + "名称：" + m_result.getVname());
                            objects.add(m_result);
                        }
                        Message msg = handler.obtainMessage();
                        msg.what = 456;
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        },map);


        haoyouItemAdapter.setItemClickListener(new HaoyouItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                haoyouItemAdapter.removeList.add(position);
                haoyouItemAdapter.notifyDataSetChanged();
                Intent intent1 = new Intent();
                intent1.putExtra("otherId" , (Serializable)objects1.get(position).getUserIndividualInfoBean());
                intent1.putExtra("xiaoxi", (Serializable) objects1.get(position).getList());
                intent1.setClass(HaoyouliebiaoActivity.this, XiaoxiActivity.class);
                Xiaoxilei.otherId = objects.get(position).getId();
                startActivity(intent1);
            }
        });

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        System.out.println("好友列表界面获取的消息为： " + messageEvent.getMessage());
        HashMap map;
        Gson gson = new Gson();
        map = gson.fromJson(messageEvent.getMessage(), HashMap.class);
        System.out.println("获取的消息16为： " + map.get("16"));
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
