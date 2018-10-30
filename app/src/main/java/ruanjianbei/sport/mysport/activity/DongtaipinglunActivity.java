package ruanjianbei.sport.mysport.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.adapter.MyAdapter1;
import ruanjianbei.sport.mysport.adapter.RecyclerViewPinLunAdapter;
import ruanjianbei.sport.mysport.bean.PinlunBean;
import ruanjianbei.sport.mysport.bean.Shequ_DongtaiBean;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.MyApplication;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.GridView;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.view.View;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DongtaipinglunActivity extends Activity implements View.OnClickListener {

    private CircleImageView pinlunTouxiang;
    private TextView pinlunName;
    private TextView pinlunFabiaoshijian;
    private TextView pinlunWenzhangNeirong;
    private GridView itemPinlunTupian;
    private RecyclerView pinlunRc;
    private ImageView pinlunFenxiang;
    private TextView pinlunFenxiangshu;
    private ImageView pinlunPinglun;
    private TextView pinlunPinglunshu;
    private ImageView pinlunDianzan;
    private TextView pinlunDianzanshu;
    private Shequ_DongtaiBean shequ_dongtaiBean;
    private TextView pinlunNeirong;
    private RelativeLayout relativeLayout1;
    private RelativeLayout relativeLayout2;
    private EditText pinlunZhenneirong;
    private Button fasong;
    private List<PinlunBean> list_pinlun = new ArrayList<>();
    private PullToRefreshLayout pullToRefreshLayout;
    private RecyclerViewPinLunAdapter adapter;
    private Boolean isone = true;
    private ImageView fanhui;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 6:
                    if (!list_pinlun.isEmpty()) {
                        System.out.println("消息中的List的数据：");
                    }
                    // 结束刷新
                    pullToRefreshLayout.finishRefresh();
                    if (isone) {
                        adapter = new RecyclerViewPinLunAdapter(list_pinlun, DongtaipinglunActivity.this);
                        pinlunRc.setLayoutManager(new LinearLayoutManager(DongtaipinglunActivity.this));
                        pinlunRc.setAdapter(adapter);
                        isone = false;
                        adapter.setItemClickListener(new RecyclerViewPinLunAdapter.OnItemClickListener() {

                            @Override
                            public void onItemClick(View v, int o) {
                                System.out.println("点击：");
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putString("id", String.valueOf(list_pinlun.get(o).getId()));
                                bundle.putSerializable("pinlun", (Serializable) list_pinlun.get(o).getUserIndividualInfoBean());                                bundle.putString("context", list_pinlun.get(o).getContext().get(1));
                                bundle.putString("time", list_pinlun.get(o).getContext().get(0));
                                bundle.putSerializable("huifu", (Serializable) list_pinlun.get(o).getRepinlunBeans());
                                intent.putExtra("bu", bundle);
                                intent.setClass(DongtaipinglunActivity.this, PinlunhuifuActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.shangqu, R.anim.budong);
                            }
                        });
                    } else {
                        adapter.replaceAll(list_pinlun);
                    }
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
        setContentView(R.layout.dongtaipinglun);
        init();
    }

    private void init() {
        pullToRefreshLayout = findViewById(R.id.pinlun_shuaxin);
        relativeLayout1 = findViewById(R.id.rl_comment1);
        relativeLayout2 = findViewById(R.id.ll_comment2);
        pinlunZhenneirong = findViewById(R.id.pinlun_zhenneirong);
        fasong = findViewById(R.id.pinlun_fasong);
        pinlunTouxiang = (CircleImageView) findViewById(R.id.pinlun_touxiang);
        pinlunName = (TextView) findViewById(R.id.pinlun_name);
        pinlunFabiaoshijian = (TextView) findViewById(R.id.pinlun_fabiaoshijian);
        pinlunWenzhangNeirong = (TextView) findViewById(R.id.pinlun_neirong);
        itemPinlunTupian = (GridView) findViewById(R.id.pinlun_tupian);
        pinlunRc = (RecyclerView) findViewById(R.id.pinlun_rc);
        pinlunFenxiang = (ImageView) findViewById(R.id.pinlun_fenxiang);
        pinlunFenxiangshu = (TextView) findViewById(R.id.pinlun_fenxiangshu);
        pinlunPinglun = (ImageView) findViewById(R.id.pinlun_pinglun);
        pinlunPinglunshu = (TextView) findViewById(R.id.pinlun_pinglunshu);
        pinlunDianzan = (ImageView) findViewById(R.id.pinlun_dianzan);
        pinlunDianzanshu = (TextView) findViewById(R.id.pinlun_dianzanshu);
        pinlunNeirong = (TextView) findViewById(R.id.pinlun_pinglunneirong);
        fanhui = findViewById(R.id.dongtaipinglun_fanhui);

        Intent intent = getIntent();
        shequ_dongtaiBean = (Shequ_DongtaiBean) intent.getSerializableExtra("bean");
        Picasso.with(this)
                .load(MyApplication.imageUri + shequ_dongtaiBean.getDongtai_touxiang())
                .into(pinlunTouxiang);
        pinlunName.setText(shequ_dongtaiBean.getDongtai_name());
        pinlunFabiaoshijian.setText(shequ_dongtaiBean.getDongtai_shijian());
        pinlunDianzanshu.setText(shequ_dongtaiBean.getDongtai_dianzan());
        pinlunFenxiangshu.setText(shequ_dongtaiBean.getDongtai_fenxiang());
        pinlunPinglunshu.setText(shequ_dongtaiBean.getDongtai_pinlun());
        pinlunWenzhangNeirong.setText(shequ_dongtaiBean.getDongtai_neirong());
        if (shequ_dongtaiBean.getDianzanstatus() == 1) {
            pinlunDianzan.setImageResource(R.drawable.dianzanhou);
        }
        if (shequ_dongtaiBean.getPinlunstatus() == 1) {
            pinlunPinglun.setImageResource(R.drawable.pinglunhou);
        }
        if (shequ_dongtaiBean.getFenxiangstatus() == 1) {
            pinlunFenxiang.setImageResource(R.drawable.fenxianghou);
        }


        itemPinlunTupian.setAdapter(new MyAdapter1(this, shequ_dongtaiBean.getDongtai_tupian()));
        pinlunNeirong.setOnClickListener(this);
        fasong.setOnClickListener(this);
        fanhui.setOnClickListener(this);
        pinlun();

    }

    private void pinlun() {


        if (isone) {
            shuaxin();
        }

        //刷新
        pullToRefreshLayout.setRefreshListener(new BaseRefreshListener() {
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
                        pullToRefreshLayout.finishLoadMore();
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
                map1.put("gid", String.valueOf(shequ_dongtaiBean.getId()));
                HttpUtils.post(MyApplication.getpinlunUri, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseBody = response.body().string();
                        System.out.println("评论信息：" + responseBody);
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<PinlunBean>>() {
                        }.getType();
                        list_pinlun = gson.fromJson(responseBody, type);
                        Message msg = handler.obtainMessage();
                        msg.what = 6;
                        msg.obj = list_pinlun; //把登录结果也发送过去
                        handler.sendMessage(msg);
                    }
                }, map1);
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
        switch (v.getId()) {
            case R.id.dongtaipinglun_fanhui:
                finish();
                break;
            case R.id.pinlun_pinglunneirong:
                // 弹出输入法
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

                // 显示评论框
                relativeLayout2.setVisibility(View.VISIBLE);
                relativeLayout1.setVisibility(View.GONE);
                break;
            case R.id.pinlun_fasong:
                final Map<String, String> map = new HashMap<>();
                if (pinlunZhenneirong.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "请输入内容。", Toast.LENGTH_LONG).show();
                }else {
                    map.put("uid", String.valueOf(MyApplication.user.getId()));
                    map.put("gid", String.valueOf(shequ_dongtaiBean.getId()));
                    map.put("context", pinlunZhenneirong.getText().toString());
                    System.out.println("评论内容：" + map.get("context"));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HttpUtils.post(MyApplication.setpinlunUri, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    System.out.println("评论返回：" + response.body().string());
                                }
                            }, map);
                        }
                    }).start();
                    // 隐藏评论框
                    relativeLayout2.setVisibility(View.GONE);
                    relativeLayout1.setVisibility(View.VISIBLE);
                    pinlunZhenneirong.setText("");
                    // 隐藏输入法，然后暂存当前输入框的内容，方便下次使用
                    InputMethodManager im = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(pinlunZhenneirong.getWindowToken(), 0);
                    pullToRefreshLayout.autoRefresh();
                }
                break;

        }
    }

//
}
