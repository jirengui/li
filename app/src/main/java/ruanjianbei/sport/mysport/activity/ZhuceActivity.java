package ruanjianbei.sport.mysport.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.bean.UserIndividualInfoBean;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.MyApplication;
import ruanjianbei.sport.mysport.zidingyi_view.Yanzhengma;

import static ruanjianbei.sport.mysport.util.SmsDemo.sendSms;

public class ZhuceActivity extends Activity implements View.OnClickListener {


    private ImageView zhucezhanghaoFanhui;
    private Yanzhengma zhuceHuoquyanzhengma;
    private String shoujihao, mima;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 123:
                    try {
                        //获取用户登录的结果
                        int code = Integer.parseInt((String) msg.obj);
                        if (code == 0) {
                            Intent intent = new Intent();
                            intent.putExtra("shoujihao", shoujihao);
                            intent.putExtra("mima", mima);
                            intent.setClass(ZhuceActivity.this, BangdingshenfenActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ZhuceActivity.this, "手机号已注册。", Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
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
        setContentView(R.layout.zhuce);

        zhucezhanghaoFanhui = (ImageView) findViewById(R.id.zhucezhanghao_fanhui);
        zhuceHuoquyanzhengma = findViewById(R.id.zhuce_huoquyanzhengma);
        findViewById(R.id.bt_zhuce).setOnClickListener(this);
        zhucezhanghaoFanhui.setOnClickListener(this);
        zhuceHuoquyanzhengma.setOnClickListener(this);


    }

    private EditText getZhuceShoujihao() {
        return (EditText) findViewById(R.id.zhuce_shoujihao);
    }

    private EditText getZhuceYanzhengma() {
        return (EditText) findViewById(R.id.zhuce_yanzhengma);
    }

    private EditText getZhuceMima() {
        return (EditText) findViewById(R.id.zhuce_mima);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_zhuce:
                //TODO implement
                String yanzhengma = getZhuceYanzhengma().getText().toString();
                if (MyApplication.yanzhengma == Integer.parseInt(yanzhengma)) {
                    shoujihao = getZhuceShoujihao().getText().toString();
                    mima = getZhuceMima().getText().toString();
                    if (shoujihao.isEmpty()) {
                        Toast.makeText(this, "手机号不能为空。", Toast.LENGTH_SHORT).show();
                    } else if (mima.isEmpty()) {
                        Toast.makeText(this, "密码不能为空。", Toast.LENGTH_SHORT).show();
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                final Map<String, String> map = new HashMap<>();
                                map.put("number", shoujihao);
                                HttpUtils.post(MyApplication.getPhoneNumber, new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String responseBody = response.body().string();
                                        System.out.println("注册手机信息：" + responseBody);
                                        //发送登录成功的消息
                                        Message msg = handler.obtainMessage();
                                        msg.what = 123;
                                        msg.obj = responseBody; //把登录结果也发送过去
                                        handler.sendMessage(msg);
                                    }
                                }, map);
                            }
                        }).start();
                    }
                } else {
                    Toast toast;
                    toast = Toast.makeText(ZhuceActivity.this, "验证码错误", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                break;
            case R.id.zhucezhanghao_fanhui:
                startActivity(new Intent(ZhuceActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.zhuce_huoquyanzhengma:
                //发短信
                shoujihao = getZhuceShoujihao().getText().toString();
                if (shoujihao.isEmpty()) {
                    Toast toast;
                    toast = Toast.makeText(ZhuceActivity.this, "手机号不能为空", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    // 短信验证，因太穷注释掉了
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            SendSmsResponse response = null;
//                            try {
////                    response = sendSms(getZhuceShoujihao().getText().toString());
//                                response = sendSms(shoujihao);
//
//                            } catch (ClientException e) {
//                                e.printStackTrace();
//                            }
//
//                            System.out.println("短信接口返回的数据----------------");
//                            System.out.println("Code=" + response.getCode());
//                            System.out.println("Message=" + response.getMessage());
//                            System.out.println("RequestId=" + response.getRequestId());
//                            System.out.println("BizId=" + response.getBizId());
//                        }
//                    }).start();
                    Toast toast = Toast.makeText(ZhuceActivity.this, "因开发者太穷，所以验证码在这："+ MyApplication.getyanzhengma(), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();


                }
        }
    }

    /**
     * 监听Back键按下事件
     * super.onBackPressed()会自动调用finish()方法,关闭
     * 当前Activity.
     * 若要屏蔽Back键盘,注释该行代码即可
     */
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        System.out.println("按下了back键   onBackPressed()");
        startActivity(new Intent(ZhuceActivity.this, LoginActivity.class));
        finish();
    }
}
