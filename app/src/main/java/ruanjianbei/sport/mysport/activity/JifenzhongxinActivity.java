package ruanjianbei.sport.mysport.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.adapter.RcShangPingAdapter;
import ruanjianbei.sport.mysport.bean.LiWuBean;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.MyApplication;

import android.widget.LinearLayout;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JifenzhongxinActivity extends Activity implements View.OnClickListener {

    private ImageView jifenzhongxinFanhui;
    private CircleImageView jifenzhongxinTouxiang;
    private TextView jifenzhongxinJifen;
    private LinearLayout jifenzhongxinJifenduihuan;
    private LinearLayout jifenzhongxinShangchengxuangou;
    private LinearLayout jifenzhongxinXinyunchoujiang;
    private ImageView jfzxImRemenduihuan1;
    private TextView jfzxTvRemenduihuan1;
    private ImageView jfzxImRemenduihuan2;
    private TextView jfzxTvRemenduihuan2;
    private ImageView jfzxImRemenduihuan3;
    private TextView jfzxTvRemenduihuan3;
    private ImageView jfzxImRemenduihuan4;
    private TextView jfzxTvRemenduihuan4;
    private TextView jfzxRenwuQiandao;
    private TextView jfzxRenwuQiandao1;
    private TextView jfzxRenwuCanyupinlun;
    private TextView jfzxRenwuQiandao2;
    private TextView jfzxRenwuCanjiabisai;
    private TextView jfzxRenwuQiandao3;
    private TextView jfzxRenwuBangdinshouji;
    private TextView jfzxRenwuQiandao4;

    private List<LiWuBean> list = new ArrayList<>();
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Picasso.with(JifenzhongxinActivity.this)
                    .load(MyApplication.imageUri + list.get(0).getTupian())
                    .into(jfzxImRemenduihuan1);
            Picasso.with(JifenzhongxinActivity.this)
                    .load(MyApplication.imageUri + list.get(1).getTupian())
                    .into(jfzxImRemenduihuan2);
            Picasso.with(JifenzhongxinActivity.this)
                    .load(MyApplication.imageUri + list.get(2).getTupian())
                    .into(jfzxImRemenduihuan3);
            Picasso.with(JifenzhongxinActivity.this)
                    .load(MyApplication.imageUri + list.get(3).getTupian())
                    .into(jfzxImRemenduihuan4);

            jfzxTvRemenduihuan1.setText(String.valueOf(list.get(0).getJifen()) + "积分");
            jfzxTvRemenduihuan2.setText(String.valueOf(list.get(1).getJifen()) + "积分");
            jfzxTvRemenduihuan3.setText(String.valueOf(list.get(2).getJifen()) + "积分");
            jfzxTvRemenduihuan4.setText(String.valueOf(list.get(3).getJifen()) + "积分");

        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        StatusBarCompat.setLightStatusBar(getWindow(), false);
        if (jifenzhongxinJifen != null) {
            jifenzhongxinJifen.setText(MyApplication.user.getJifen());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //false为白色字体，ture为黑色字体
        StatusBarCompat.setLightStatusBar(getWindow(), false);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.jifenzhongxin);
        iniv();
        date();
    }
    private void iniv(){
        jifenzhongxinFanhui = (ImageView) findViewById(R.id.jifenzhongxin_fanhui);
        jifenzhongxinTouxiang = (CircleImageView) findViewById(R.id.jifenzhongxin_touxiang);
        jifenzhongxinJifen = (TextView) findViewById(R.id.jifenzhongxin_jifen);
        jifenzhongxinJifenduihuan = (LinearLayout) findViewById(R.id.jifenzhongxin_jifenduihuan);
        jifenzhongxinShangchengxuangou = (LinearLayout) findViewById(R.id.jifenzhongxin_shangchengxuangou);
        jifenzhongxinXinyunchoujiang = (LinearLayout) findViewById(R.id.jifenzhongxin_xinyunchoujiang);
        jfzxImRemenduihuan1 = (ImageView) findViewById(R.id.jfzx_im_remenduihuan1);
        jfzxTvRemenduihuan1 = (TextView) findViewById(R.id.jfzx_tv_remenduihuan1);
        jfzxImRemenduihuan2 = (ImageView) findViewById(R.id.jfzx_im_remenduihuan2);
        jfzxTvRemenduihuan2 = (TextView) findViewById(R.id.jfzx_tv_remenduihuan2);
        jfzxImRemenduihuan3 = (ImageView) findViewById(R.id.jfzx_im_remenduihuan3);
        jfzxTvRemenduihuan3 = (TextView) findViewById(R.id.jfzx_tv_remenduihuan3);
        jfzxImRemenduihuan4 = (ImageView) findViewById(R.id.jfzx_im_remenduihuan4);
        jfzxTvRemenduihuan4 = (TextView) findViewById(R.id.jfzx_tv_remenduihuan4);
        jfzxRenwuQiandao = (TextView) findViewById(R.id.jfzx_renwu_qiandao);
        jfzxRenwuQiandao1 = (TextView) findViewById(R.id.jfzx_renwu_qiandao1);
        jfzxRenwuCanyupinlun = (TextView) findViewById(R.id.jfzx_renwu_canyupinlun);
        jfzxRenwuQiandao2 = (TextView) findViewById(R.id.jfzx_renwu_qiandao2);
        jfzxRenwuCanjiabisai = (TextView) findViewById(R.id.jfzx_renwu_canjiabisai);
        jfzxRenwuQiandao3 = (TextView) findViewById(R.id.jfzx_renwu_qiandao3);
        jfzxRenwuBangdinshouji = (TextView) findViewById(R.id.jfzx_renwu_bangdinshouji);
        jfzxRenwuQiandao4 = (TextView) findViewById(R.id.jfzx_renwu_qiandao4);
    }
    //初始化数据
    private void date(){
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

        jifenzhongxinJifen.setText(String.valueOf(MyApplication.user.getJifen()));
        Picasso.with(this)
                .load(MyApplication.imageUri+MyApplication.user.getTouxiang())
                .placeholder(this.getDrawable(R.drawable.wanou))
                .into(jifenzhongxinTouxiang);
        jifenzhongxinFanhui.setOnClickListener(this);
        jifenzhongxinJifenduihuan.setOnClickListener(this);
        jifenzhongxinShangchengxuangou.setOnClickListener(this);
        jifenzhongxinXinyunchoujiang.setOnClickListener(this);
        jfzxRenwuQiandao2.setOnClickListener(this);
        jfzxRenwuQiandao3.setOnClickListener(this);
        jfzxRenwuQiandao4.setOnClickListener(this);

        jfzxImRemenduihuan1.setOnClickListener(this);
        jfzxImRemenduihuan2.setOnClickListener(this);
        jfzxImRemenduihuan3.setOnClickListener(this);
        jfzxImRemenduihuan4.setOnClickListener(this);



        if (MyApplication.qiandaozhuangtai == 1){
            jfzxRenwuQiandao1.setText("已完成");
        }else {
            jfzxRenwuQiandao1.setOnClickListener(this);
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
            case R.id.jifenzhongxin_fanhui:
                finish();
                break;
            case R.id.jifenzhongxin_jifenduihuan:
                startActivity(new Intent(JifenzhongxinActivity.this, JifenduihuanActivity.class));
                break;
            case R.id.jifenzhongxin_shangchengxuangou:
                startActivity(new Intent(JifenzhongxinActivity.this, ShangchengxuangouActivity.class));
                break;
            case R.id.jifenzhongxin_xinyunchoujiang:
                startActivity(new Intent(JifenzhongxinActivity.this, XinyunchoujiangActivity.class));
                break;
            case R.id.jfzx_renwu_qiandao1:
            case R.id.jfzx_renwu_qiandao2:
            case R.id.jfzx_renwu_qiandao3:
            case R.id.jfzx_renwu_qiandao4:
                finish();
                break;
            case R.id.jfzx_im_remenduihuan1:
                if (Integer.parseInt(MyApplication.user.getJifen()) > Integer.parseInt(list.get(0).getJifen()))
                {
                    updatajifen("-" + list.get(0).getJifen());
                    Map<String, String> map = new HashMap<>();
                    map.put("uid", String.valueOf(MyApplication.user.getId()));
                    map.put("lid", String.valueOf(list.get(0).getId()));
                    map.put("count","1");
                    HttpUtils.post(MyApplication.duihuan, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast toast = Toast.makeText(JifenzhongxinActivity.this,"兑换成功",Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER,0,0);
                                    toast.show();
                                }
                            });
                        }
                    }, map);
                }else {
                    Toast toast = Toast.makeText(JifenzhongxinActivity.this,"积分不足，无法兑换",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                break;
            case R.id.jfzx_im_remenduihuan2:
                if (Integer.parseInt(MyApplication.user.getJifen()) > Integer.parseInt(list.get(1).getJifen()))
                {
                    updatajifen("-" + list.get(1).getJifen());
                    Map<String, String> map = new HashMap<>();
                    map.put("uid", String.valueOf(MyApplication.user.getId()));
                    map.put("lid", String.valueOf(list.get(1).getId()));
                    map.put("count","1");
                    HttpUtils.post(MyApplication.duihuan, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast toast = Toast.makeText(JifenzhongxinActivity.this,"兑换成功",Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER,0,0);
                                    toast.show();
                                }
                            });
                        }
                    }, map);
                }else {
                    Toast toast = Toast.makeText(JifenzhongxinActivity.this,"积分不足，无法兑换",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                break;
            case R.id.jfzx_im_remenduihuan3:
                if (Integer.parseInt(MyApplication.user.getJifen()) > Integer.parseInt(list.get(2).getJifen()))
                {
                    updatajifen("-" + list.get(2).getJifen());
                    Map<String, String> map = new HashMap<>();
                    map.put("uid", String.valueOf(MyApplication.user.getId()));
                    map.put("lid", String.valueOf(list.get(2).getId()));
                    map.put("count","1");
                    HttpUtils.post(MyApplication.duihuan, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast toast = Toast.makeText(JifenzhongxinActivity.this,"兑换成功",Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER,0,0);
                                    toast.show();
                                }
                            });
                        }
                    }, map);
                }else {
                    Toast toast = Toast.makeText(JifenzhongxinActivity.this,"积分不足，无法兑换",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                break;
            case R.id.jfzx_im_remenduihuan4:
                if (Integer.parseInt(MyApplication.user.getJifen()) > Integer.parseInt(list.get(3).getJifen()))
                {
                    updatajifen("-" + list.get(3).getJifen());
                    Map<String, String> map = new HashMap<>();
                    map.put("uid", String.valueOf(MyApplication.user.getId()));
                    map.put("lid", String.valueOf(list.get(3).getId()));
                    map.put("count","1");
                    HttpUtils.post(MyApplication.duihuan, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast toast = Toast.makeText(JifenzhongxinActivity.this,"兑换成功",Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER,0,0);
                                    toast.show();
                                }
                            });
                        }
                    }, map);
                }else {
                    Toast toast = Toast.makeText(JifenzhongxinActivity.this,"积分不足，无法兑换",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                break;

        }

    }

    private void updatajifen(String jifen){
        int jifen1 = 0;
        if (MyApplication.user.getJifen() != null) {
            jifen1 = Integer.parseInt(MyApplication.user.getJifen());
            jifen1 += Integer.parseInt(jifen);
        }
        MyApplication.user.setJifen(String.valueOf(jifen1));
        jifenzhongxinJifen.setText(MyApplication.user.getJifen());
        Map<String, String> map = new HashMap<>();
        map.put("uid", String.valueOf(MyApplication.user.getId()));
        map.put("count", jifen);
        map.put("shijian", "兑换");
        HttpUtils.post(MyApplication.updatejifenurl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("兑换：" + response.body().string());
            }
        },map);
    }
}
