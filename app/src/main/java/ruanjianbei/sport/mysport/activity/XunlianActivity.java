package ruanjianbei.sport.mysport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.bean.PlanBean1;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.MyApplication;

public class XunlianActivity extends Activity  implements View.OnClickListener{

    private ImageView xunlianFanhui;
    private TextView xunlianName;
    private TextView xunlian1;
    private TextView xunlianYongshi;
    private TextView xunlian2;
    private TextView xunlianMubiaoyongshi;
    private TextView xunlianLeixing;
    private TextView xunlianDashijian;
    private TextView xunlianZanting;
    private TextView xunlianKaishi;
    private PlanBean1 planBean1;
    private Timer timer;
    final int[] miao = {0, 0};
    final int[] fen = {0, 0};
    final int[] shi = {0, 0};
    final int[] damiao = {0};
    final int[] dafen = {0};
    private Boolean iskaishi = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xunlian);

       iniv();
       setdata();

    }
    private void iniv(){
        xunlianFanhui = (ImageView) findViewById(R.id.xunlian_fanhui);
        xunlianName = (TextView) findViewById(R.id.xunlian_name);
        xunlian1 = (TextView) findViewById(R.id.xunlian_1);
        xunlianYongshi = (TextView) findViewById(R.id.xunlian_yongshi);
        xunlian2 = (TextView) findViewById(R.id.xunlian_2);
        xunlianMubiaoyongshi = (TextView) findViewById(R.id.xunlian_mubiaoyongshi);
        xunlianLeixing = (TextView) findViewById(R.id.xunlian_leixing);
        xunlianDashijian = (TextView) findViewById(R.id.xunlian_dashijian);
        xunlianZanting = (TextView) findViewById(R.id.xunlian_zanting);
        xunlianKaishi = (TextView) findViewById(R.id.xunlian_kaishi);
        xunlianFanhui.setOnClickListener(this);
    }
    private void setdata(){
        Intent intent = getIntent();
        planBean1 = (PlanBean1) intent.getSerializableExtra("bean");
        if (planBean1 != null) {
            xunlianName.setText(planBean1.getPlan() + planBean1.getShijian() + "分钟");
            xunlianLeixing.setText(planBean1.getPlan());
            xunlianMubiaoyongshi.setText(planBean1.getShijian() + ":00");
            xunlianDashijian.setText("00:" + planBean1.getShijian() + ":00");
            xunlianZanting.setOnClickListener(this);
            xunlianKaishi.setOnClickListener(this);
            dafen[0] = Integer.parseInt(planBean1.getShijian());
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.xunlian_kaishi:
                if (xunlianKaishi.getText().toString().equals("开 始 训 练")) {
                    if (dafen[0] > 0) {
                        xunlianKaishi.setText("正 在 训 练");
                        iskaishi = true;
                        timer1();
                    }
                }
                break;
            case R.id.xunlian_zanting:
                if (iskaishi) {
                    jieshu();
                    iskaishi = false;
                }
                break;
            case R.id.xunlian_fanhui:
                finish();
                break;
        }
    }

    private void timer1() {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                miao[0]++;
                if (miao[0] == 10) {
                    miao[1]++;
                    miao[0] = 0;
                }
                if (fen[0] == 10) {
                    fen[1]++;
                    fen[0] = 0;
                }
                if (shi[0] == 10) {
                    shi[1]++;
                    shi[0] = 0;
                }
                if (miao[1] == 6) {
                    fen[0]++;
                    miao[0] = 0;
                    miao[1] = 0;
                }
                if (fen[1] == 6) {
                    shi[0]++;
                    fen[0] = 0;
                    fen[1] = 0;
                }


                if (damiao[0] == 0){
                    if (dafen[0] > 0) {
                        dafen[0]--;
                        damiao[0] = 59;
                    }
                }else {
                    damiao[0]--;
                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        xunlianYongshi.setText( String.valueOf(fen[1])+ String.valueOf(fen[0])+ ":" + String.valueOf(miao[1])+ String.valueOf(miao[0]));
                        if (dafen[0] >= 10 && damiao[0] >= 10) {
                            xunlianDashijian.setText("00:" + dafen[0] + ":" + damiao[0]);
                        }else if (dafen[0] < 10 && damiao[0] < 10){
                            xunlianDashijian.setText("00:0" + dafen[0] + ":0" + damiao[0]);
                        }else if (dafen[0] <10){
                            xunlianDashijian.setText("00:0" + dafen[0] + ":" + damiao[0]);
                        }else if (damiao[0] <10){
                            xunlianDashijian.setText("00:" + dafen[0] + ":0" + damiao[0]);
                        }
                        if (dafen[0] == 0 && damiao[0] == 0){
                            timer.cancel();
                            xunlianKaishi.setText("已 完 成");
                            Map<String, String> map = new HashMap<>();
                            map.put("uid", String.valueOf(MyApplication.user.getId()));
                            map.put("plan", String.valueOf(planBean1.getPlan()));
                            map.put("time", planBean1.getTime());
                            HttpUtils.post(MyApplication.finishPlan, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    System.out.println("完成了");
                                }
                            },map );
                        }
                    }
                });
            }
        };
        timer.schedule(task, 1000, 1000);
    }
    private void jieshu(){
        timer.cancel();
        xunlianKaishi.setText("开 始 训 练");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        planBean1.setShijian(String.valueOf(dafen[0]));
    }
}
