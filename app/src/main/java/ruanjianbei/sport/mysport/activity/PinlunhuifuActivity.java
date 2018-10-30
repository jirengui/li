package ruanjianbei.sport.mysport.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.adapter.RecyclerViewPinLunAdapter;
import ruanjianbei.sport.mysport.adapter.RvPinLunHuiFuAdapter;
import ruanjianbei.sport.mysport.bean.PinlunBean;
import ruanjianbei.sport.mysport.bean.RepinlunBean;
import ruanjianbei.sport.mysport.bean.UserIndividualInfoBean;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.MyApplication;

import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.view.View;

import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.squareup.picasso.Picasso;

import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PinlunhuifuActivity extends Activity implements View.OnClickListener {

    private RelativeLayout a1;
    private CircleImageView pinlunhuifuTouxiang;
    private TextView pinlunhuifuName;
    private TextView pinlunhuifuFabiaoshijian;
    private TextView pinlunhuifuNeirong;
    private LinearLayout pinlunhuifuLl;
    private ImageView pinlunhuifuPinglun;
    private TextView pinlunhuifuPinglunshu;
    private ImageView pinlunhuifuDianzan;
    private TextView pinlunhuifuDianzanshu;
    private PullToRefreshLayout pinlunhuifuShuaxin;
    private RecyclerView pinlunhuifuRc;
    private RelativeLayout rlComment1;
    private TextView pinlunhuifuPinglunneirong;
    private RelativeLayout llComment2;
    private String id, context, time;
    private UserIndividualInfoBean pinlun;
    private List<RepinlunBean> huifu;
    private Bundle bu;
    private RvPinLunHuiFuAdapter adapter;


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 6:
                    if (!huifu.isEmpty()) {
                        System.out.println("消息中的List的数据：");
                    }
                     // 结束刷新
                    pinlunhuifuShuaxin.finishRefresh();
                    adapter.replaceAll(huifu);
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        //false为白色字体，ture为黑色字体
        StatusBarCompat.setLightStatusBar(getWindow(), true);
        setContentView(R.layout.pinlunhuifu);

        Intent intent = getIntent();
        bu = intent.getBundleExtra("bu");
        id = bu.getString("id");
        context = bu.getString("context");
        time = bu.getString("time");
        pinlun = (UserIndividualInfoBean) bu.getSerializable("pinlun");
        huifu = (List<RepinlunBean>) bu.getSerializable("huifu");


        pinlunhuifuTouxiang = (CircleImageView) findViewById(R.id.pinlunhuifu_touxiang);
        pinlunhuifuName = (TextView) findViewById(R.id.pinlunhuifu_name);
        pinlunhuifuFabiaoshijian = (TextView) findViewById(R.id.pinlunhuifu_fabiaoshijian);
        pinlunhuifuNeirong = (TextView) findViewById(R.id.pinlunhuifu_neirong);
        pinlunhuifuLl = (LinearLayout) findViewById(R.id.pinlunhuifu_ll);
        pinlunhuifuPinglun = (ImageView) findViewById(R.id.pinlunhuifu_pinglun);
        pinlunhuifuPinglunshu = (TextView) findViewById(R.id.pinlunhuifu_pinglunshu);
        pinlunhuifuDianzan = (ImageView) findViewById(R.id.pinlunhuifu_dianzan);
        pinlunhuifuDianzanshu = (TextView) findViewById(R.id.pinlunhuifu_dianzanshu);
        pinlunhuifuShuaxin = (PullToRefreshLayout) findViewById(R.id.pinlunhuifu_shuaxin);
        pinlunhuifuRc = (RecyclerView) findViewById(R.id.pinlunhuifu_rc);
        rlComment1 = (RelativeLayout) findViewById(R.id.pinlunhuifu_comment1);
        pinlunhuifuPinglunneirong = (TextView) findViewById(R.id.pinlunhuifu_pinglunneirong);
        llComment2 = (RelativeLayout) findViewById(R.id.pinlunhuifu_comment2);
        findViewById(R.id.pinlunhuifu_fasong).setOnClickListener(this);
        adapter = new RvPinLunHuiFuAdapter(huifu, PinlunhuifuActivity.this);
        pinlunhuifuRc.setLayoutManager(new LinearLayoutManager(PinlunhuifuActivity.this));
        pinlunhuifuRc.setAdapter(adapter);
        pinlunhuifuPinglunneirong.setOnClickListener(this);
        Picasso.with(this)
                .load(MyApplication.imageUri + pinlun.getTouxiang())
                .into(pinlunhuifuTouxiang);
        pinlunhuifuName.setText(pinlun.getVname());
        pinlunhuifuFabiaoshijian.setText(time);
        pinlunhuifuNeirong.setText(context);
        //刷新
        pinlunhuifuShuaxin.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                shuaxin();
            }

            @Override
            public void loadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 结束加载更多
                        pinlunhuifuShuaxin.finishLoadMore();
                    }
                }, 2000);
            }
        });
    }

    private void shuaxin() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> map1 = new HashMap<>();
                map1.put("gid", id);
                HttpUtils.post(MyApplication.gethuifuurl, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseBody = response.body().string();
                        System.out.println("回复信息：" + responseBody);
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<RepinlunBean>>() {
                        }.getType();
                        huifu = gson.fromJson(responseBody, type);
                        Message msg = handler.obtainMessage();
                        msg.what = 6;
                        msg.obj = huifu; //把结果也发送过去
                        handler.sendMessage(msg);
                    }
                }, map1);
            }
        }).start();

    }

    private EditText getPinlunhuifuZhenneirong(){
        return (EditText) findViewById(R.id.pinlunhuifu_zhenneirong);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.budong, R.anim.xiaqu);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pinlunhuifu_pinglunneirong:
                // 弹出输入法
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

                // 显示评论框
                llComment2.setVisibility(View.VISIBLE);
                rlComment1.setVisibility(View.GONE);
                break;
            case R.id.pinlunhuifu_fasong:
                //TODO implement
                final Map<String, String> map = new HashMap<>();
                if (getPinlunhuifuZhenneirong().getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"请输入内容。",Toast.LENGTH_LONG).show();
                }else {
                    map.put("uid", String.valueOf(MyApplication.user.getId()));
                    map.put("gid", String.valueOf(id));
                    map.put("context", getPinlunhuifuZhenneirong().getText().toString());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HttpUtils.post(MyApplication.huifupinglunurl, new okhttp3.Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    System.out.println("评论返回：" + map.get("uid") + "gid: " + map.get("gid") + "内容: " + map.get("context"));
                                }
                            }, map);
                        }
                    }).start();
                    // 隐藏评论框
                    llComment2.setVisibility(View.GONE);
                    rlComment1.setVisibility(View.VISIBLE);
                    getPinlunhuifuZhenneirong().setText("");
                    // 隐藏输入法，然后暂存当前输入框的内容，方便下次使用
                    InputMethodManager im = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(getPinlunhuifuZhenneirong().getWindowToken(), 0);
                    pinlunhuifuShuaxin.autoRefresh();
                }
                break;
        }
    }
}
