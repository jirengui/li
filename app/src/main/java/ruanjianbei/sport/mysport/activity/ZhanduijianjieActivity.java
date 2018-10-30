package ruanjianbei.sport.mysport.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.adapter.RcChengYuanAdapter;
import ruanjianbei.sport.mysport.adapter.RcHuoDongAdapter;
import ruanjianbei.sport.mysport.bean.GongGaoBean;
import ruanjianbei.sport.mysport.bean.TeamAboutBean;
import ruanjianbei.sport.mysport.bean.UserIndividualInfoBean;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.MyApplication;

public class ZhanduijianjieActivity extends Activity implements View.OnClickListener {

    private ImageView zhanduijianjieTouxiang;
    private ImageView zhanduijianjieFanhui;
    private ImageView zhanduijianjieGenduochengyuan;
    private RecyclerView zhanduijianjieChengyuan;
    private RecyclerView zhanduijianjieJinqitiaozhan;
    private RcChengYuanAdapter rcChengYuanAdapter;
    private RcHuoDongAdapter rcHuoDongAdapter;
    private List<UserIndividualInfoBean> uselist = new ArrayList<>();
    private List<GongGaoBean> gonggaolist = new ArrayList<>();
    private TextView duiming;
    private TeamAboutBean teamAboutBean;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Picasso.with(ZhanduijianjieActivity.this)
                    .load(MyApplication.imageUri+teamAboutBean.getTeamInfoBean().getTouxiang())
                    .into(zhanduijianjieTouxiang);
            duiming.setText(teamAboutBean.getTeamInfoBean().getTeamname());
            uselist = teamAboutBean.getList();
            gonggaolist = teamAboutBean.getList1();
            rcChengYuanAdapter.replaceAll(uselist);
            rcHuoDongAdapter.replaceAll(gonggaolist);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        StatusBarCompat.setLightStatusBar(getWindow(),true);
        setContentView(R.layout.zhanduijianjie);

        zhanduijianjieTouxiang = (ImageView) findViewById(R.id.zhanduijianjie_touxiang);
        zhanduijianjieFanhui = (ImageView) findViewById(R.id.zhanduijianjie_fanhui);
        zhanduijianjieGenduochengyuan = (ImageView) findViewById(R.id.zhanduijianjie_genduochengyuan);
        zhanduijianjieChengyuan = (RecyclerView) findViewById(R.id.zhanduijianjie_chengyuan);
        zhanduijianjieJinqitiaozhan = (RecyclerView) findViewById(R.id.zhanduijianjie_jinqitiaozhan);
        duiming = (TextView) findViewById(R.id.zhanduijianmian_duiming);

        rcChengYuanAdapter = new RcChengYuanAdapter(uselist, this);
        rcHuoDongAdapter = new RcHuoDongAdapter(gonggaolist,this);
        zhanduijianjieChengyuan.setLayoutManager(new LinearLayoutManager(this));
        zhanduijianjieJinqitiaozhan.setLayoutManager(new LinearLayoutManager(this));
        zhanduijianjieChengyuan.setAdapter(rcChengYuanAdapter);
        zhanduijianjieJinqitiaozhan.setAdapter(rcHuoDongAdapter);
        zhanduijianjieFanhui.setOnClickListener(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("uid", String.valueOf(MyApplication.user.getId()));
                map.put("TeamName", MyApplication.user.getTeam());
                HttpUtils.post(MyApplication.getAboutTeam, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String ss = response.body().string();
                        System.out.println("战队简介：" + ss);
                        Gson gson= new Gson();
                        teamAboutBean = gson.fromJson(ss, TeamAboutBean.class);
                        Message msg = handler.obtainMessage();
                        handler.sendMessage(msg);
                    }
                },map);
            }
        }).start();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.zhanduijianjie_fanhui:
                finish();
                break;
            case R.id.zhanduijianjie_genduochengyuan:
                break;
            case R.id.zhanduijianjie_touxiang:
                break;
        }
    }
}
