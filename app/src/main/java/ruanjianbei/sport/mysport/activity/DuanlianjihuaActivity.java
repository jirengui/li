package ruanjianbei.sport.mysport.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.adapter.JihuaItemAdapter;
import ruanjianbei.sport.mysport.bean.PaoBuJieGuoBean;
import ruanjianbei.sport.mysport.bean.PlanBean;
import ruanjianbei.sport.mysport.bean.PlanBean1;
import ruanjianbei.sport.mysport.fragment.MyDialogFrament;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.MyApplication;
import ruanjianbei.sport.mysport.util.TimeUtil;

public class DuanlianjihuaActivity extends AppCompatActivity {

    private Toolbar zhidingjihua;
    private ImageView jihuaFanhui;
    private TextView jihuaTianjia;
    private PopupWindow window;
    private RecyclerView rcJihua;
    private TextView jihua_biaoti;
    private JihuaItemAdapter jihuaItemAdapter;
    private List<PlanBean1> objects = new ArrayList<>();
    private List<PlanBean> planBeanList = new ArrayList<>();
    private PullToRefreshLayout pullToRefreshLayout;


    private Map<String, String> map = new HashMap<>();
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                //0刷新，1修改,2添加，3删除
                case 0:
                    jihuaItemAdapter.replaceAll(objects);
                    pullToRefreshLayout.finishRefresh();
                    break;
                case 1:
                case 2:
                case 3:
                    pullToRefreshLayout.autoRefresh();
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //状态栏颜色设置和tar颜色一致即可
            getWindow().setStatusBarColor(getResources().getColor(R.color.biaoti));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.duanlianjihua);
        iniv();
        data();

    }

    private void iniv() {
        pullToRefreshLayout = findViewById(R.id.jihua_pu);
        zhidingjihua = (Toolbar) findViewById(R.id.zhidingjihua);
        jihuaFanhui = (ImageView) findViewById(R.id.jihua_fanhui);
        jihuaTianjia = (TextView) findViewById(R.id.jihua_tianjia);
        rcJihua = (RecyclerView) findViewById(R.id.rc_jihua);
        jihua_biaoti = findViewById(R.id.jihua_biaoti);


        jihuaItemAdapter = new JihuaItemAdapter();
        rcJihua.setLayoutManager(new LinearLayoutManager(this));
        jihuaItemAdapter.setHasStableIds(true);
        rcJihua.setAdapter(jihuaItemAdapter);
        jihuaItemAdapter.addAll(objects);

        pullToRefreshLayout.setCanLoadMore(false);
        jihuaItemAdapter.setItemClickListener(new JihuaItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int o) {
                //完成
                Intent intent = new Intent(DuanlianjihuaActivity.this, XunlianActivity.class);
                intent.putExtra("bean", objects.get(o));
                startActivity(intent);
            }
        });
        jihuaFanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        jihuaTianjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopwindow2(objects.size());
            }
        });
        jihuaItemAdapter.setOnDelListener(new JihuaItemAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                //删除
                Map<String, String>map = new HashMap<>();
                map.put("uid", String.valueOf(MyApplication.user.getId()));
                map.put("plan", String.valueOf(objects.get(pos).getPlan()));
                map.put("time", objects.get(pos).getTime());
                map.put("starttime", objects.get(pos).getStarttime());

                HttpUtils.post(MyApplication.deletePlan, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        System.out.println("删除" + response.body().string());
                        Message msg = handler.obtainMessage();
                        msg.what = 3;
                        handler.sendMessage(msg);
                    }
                },map);
            }

//            @Override
//            public void onFinish(int pos) {
//                //完成
//                objects.get(pos).setStatus(1);
//                jihuaItemAdapter.replaceAll(objects);
//            }

            @Override
            public void onXiugai(int pos) {
                //修改
                showPopwindow2(pos);
            }
        });
    }

    private void data() {
        Intent intent = getIntent();
        int a = intent.getIntExtra("moshi", 1);
        System.out.println("模式：" + a);
        if (a != 1){
            jihua_biaoti.setText("选择计划");
            AlertDialog.Builder builder = new AlertDialog.Builder(DuanlianjihuaActivity.this);
                builder.setTitle("");
                builder.setMessage("请选择一个计划");
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            jihuaTianjia.setVisibility(View.GONE);
        }
        pullToRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        objects = new ArrayList<>();
                        Map<String, String> map1 = new HashMap<>();
                        map1.put("uid", String.valueOf(MyApplication.user.getId()));
                        HttpUtils.post(MyApplication.getplanurl, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String s = response.body().string();
                                System.out.println("锻炼计划：" + s);
                                Gson gson = new Gson();
                                Map<Integer, PlanBean> map = gson.fromJson(s,
                                        new TypeToken<Map<Integer, PlanBean>>() {
                                        }.getType());
                                //map的key：1234567代表一星期七天
                                if (map != null && !map.isEmpty()) {
                                    System.out.println("锻炼计划Bean：" + map.get(1).getTime());
                                    for (Integer in : map.keySet()) {
                                        //map.keySet()返回的是所有key的值
                                        PlanBean planBean = map.get(in);//得到每个key多对用value的值
                                        planBeanList.add(planBean);
                                        if (planBean.getPlan() != null && !planBean.getPlan().isEmpty()) {
                                            for (int i = 0; i < planBean.getPlan().size(); i++) {
                                                PlanBean1 planBean1 = new PlanBean1();
                                                planBean1.setEndtime(TimeUtil.addDateMinut(planBean.getStarttime().get(i),Integer.parseInt(planBean.getSporttime().get(i))));
                                                planBean1.setPlan(planBean.getPlan().get(i));
                                                planBean1.setStatus(planBean.getStatus().get(i));
                                                planBean1.setTime(planBean.getTime());
                                                planBean1.setShijian(planBean.getSporttime().get(i));
                                                planBean1.setStarttime(planBean.getStarttime().get(i));
                                                objects.add(planBean1);
                                            }
                                        }
                                    }
                                    Message msg = handler.obtainMessage();
                                    handler.sendMessage(msg);
                                }
                            }
                        }, map1);
                    }
                }).start();
            }

            @Override
            public void loadMore() {
            }
        });
        pullToRefreshLayout.autoRefresh();
    }



    private void showPopwindow2(final int position) {
//        MyDialogFrament myDialogFrament = new MyDialogFrament();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("objects", (Serializable) objects);
//        bundle.putSerializable("planBeanList", (Serializable) planBeanList);
//        bundle.putInt("position", position);
//        myDialogFrament.setArguments(bundle);
//        myDialogFrament.show(getFragmentManager(), "DialogFragment");
        Intent intent = new Intent(this, XiugaiActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("objects", (Serializable) objects);
        bundle.putSerializable("planBeanList", (Serializable) planBeanList);
        bundle.putInt("position", position);
        intent.putExtra("bu", bundle);
        startActivityForResult(intent,1);
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 2){
            final List<PlanBean1> list = objects;
            objects = (List<PlanBean1>) data.getSerializableExtra("obj");
            final int position = data.getIntExtra("position",-1);
            if (position != -1){
                final Map<String, String> map = new HashMap<>();
                map.put("uid", String.valueOf(MyApplication.user.getId()));
                map.put("plan",list.get(position).getPlan());
                map.put("time", list.get(position).getTime());
                map.put("sporttime", objects.get(position).getShijian());
                map.put("starttime", objects.get(position).getStarttime());
                map.put("restarttime", list.get(position).getStarttime());//修改前
                map.put("updatetime", objects.get(position).getTime());
                map.put("replan", objects.get(position).getPlan());
                HttpUtils.post(MyApplication.updatePlan, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        System.out.println("修改：" + response.body().string() +"修改计划：" + map.get("replan")+ "确定计划：" + list.get(position).getTime());
                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                },map);
            }

        }
        if (requestCode == 1 && resultCode == 3){
            //添加
            objects = (List<PlanBean1>) data.getSerializableExtra("obj");
            int position = data.getIntExtra("position",-1);
            if (position != -1){
                final Map<String, String> map = new HashMap<>();
                map.put("uid", String.valueOf(MyApplication.user.getId()));
                map.put("plan",objects.get(position).getPlan());
                map.put("time", objects.get(position).getTime());
                map.put("sporttime", objects.get(position).getShijian());
                map.put("starttime", objects.get(position).getStarttime());
                HttpUtils.post(MyApplication.insertPlan, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        System.out.println("添加：" + response.body().string() );
                        Message msg = handler.obtainMessage();
                        msg.what = 2;
                        handler.sendMessage(msg);
                    }
                },map);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        pullToRefreshLayout.autoRefresh();
    }
}
