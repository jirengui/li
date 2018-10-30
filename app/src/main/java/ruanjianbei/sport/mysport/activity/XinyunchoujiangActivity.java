package ruanjianbei.sport.mysport.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.MyApplication;
import ruanjianbei.sport.mysport.zidingyi_view.DaZhuanPan;

import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class XinyunchoujiangActivity extends Activity implements DaZhuanPan.OnFinishListener {

    private DaZhuanPan choujiangDazhuangpan;
    private ImageView choujiangKaishi, xinyunchoujiang;
    /**
     * 抽奖的文字
     */
    private String[] mStrs = new String[]{"谢谢惠顾", "积分10", "积分30", "积分50",
            "积分80", "积分100"};
    //积分
    private String[] jifenshu = new String[]{"0", "10", "30", "50",
            "80", "100"};
    private int a = 0;

    private Timer timer;
    private TimerTask task;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (choujiangDazhuangpan.isStart) {
                    choujiangDazhuangpan.luckyEnd();
                }
                timer.cancel();
            } else if (msg.what == 2) {
                int jifen = 0;
                if (MyApplication.user.getJifen() != null) {
                    jifen = Integer.parseInt(MyApplication.user.getJifen());
                }
                switch (a) {
                    case 1:
                        MyApplication.user.setJifen(String.valueOf(jifen + 10));
                        break;
                    case 2:
                        MyApplication.user.setJifen(String.valueOf(jifen + 30));
                        break;
                    case 3:
                        MyApplication.user.setJifen(String.valueOf(jifen + 50));
                        break;
                    case 4:
                        MyApplication.user.setJifen(String.valueOf(jifen + 80));
                        break;
                    case 5:
                        MyApplication.user.setJifen(String.valueOf(jifen + 100));
                        break;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(XinyunchoujiangActivity.this);
                if (a != 0) {
                    builder.setTitle("恭喜你抽中");
                    builder.setMessage(mStrs[a]);
                    updatajifen(jifenshu[a]);
                } else {
                    builder.setTitle("很遗憾");
                    builder.setMessage("什么也没有抽中");
                }
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
//                Toast toast = Toast.makeText(XinyunchoujiangActivity.this,"恭喜你抽中" + mStrs[a],Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();
            }
        }
    };
    private void updatajifen(String jifen){
        Map<String, String> map = new HashMap<>();
        map.put("uid", String.valueOf(MyApplication.user.getId()));
        map.put("count", jifen);
        map.put("shijian", "抽奖");
        HttpUtils.post(MyApplication.updatejifenurl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("抽奖：" + response.body().string());
            }
        },map);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xinyunchoujiang);

        choujiangDazhuangpan = (DaZhuanPan) findViewById(R.id.choujiang_dazhuangpan);
        choujiangKaishi = (ImageView) findViewById(R.id.choujiang_kaishi);
        xinyunchoujiang = findViewById(R.id.choujiang_fanhui);


        xinyunchoujiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        choujiangKaishi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!choujiangDazhuangpan.isStart) {
                    a = (int) (Math.random() * 50) + 1;
                    if (a >= 1 && a <= 20) {
                        a = 0;
                    } else if (a <= 30) {
                        a = 1;
                    } else if (a <= 38) {
                        a = 2;
                    } else if (a <= 44) {
                        a = 3;
                    } else if (a <= 48) {
                        a = 4;
                    } else if (a <= 50) {
                        a = 5;
                    }
                    choujiangDazhuangpan.luckyStart(a, XinyunchoujiangActivity.this);
                    System.out.println("开始旋转：" + a);
                    timer = new Timer();
                    task = new TimerTask() {
                        @Override
                        public void run() {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    };
                    timer.schedule(task, 2000);
                }
            }
        });


    }

    @Override
    public void onFinish() {
        //转盘结束接口
        Message message = new Message();
        message.what = 2;
        handler.sendMessage(message);

    }
}
