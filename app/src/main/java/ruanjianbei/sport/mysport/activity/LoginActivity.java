package ruanjianbei.sport.mysport.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.bean.UserIndividualInfoBean;
import ruanjianbei.sport.mysport.fragment.PersonFragment;
import ruanjianbei.sport.mysport.fragment.SportFragment;
import ruanjianbei.sport.mysport.util.HttpUtils;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ruanjianbei.sport.mysport.util.MyApplication;

import static com.amap.api.col.n3.rg.i;

public class LoginActivity extends Activity implements View.OnClickListener {

    private UserIndividualInfoBean m_result;
    private Map<String, String> map = new HashMap<String, String>();
    private ImageView imageView;
    //处理登录成功消息
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 123:
                    try {
                        //获取用户登录的结果
                        UserIndividualInfoBean result = (UserIndividualInfoBean) msg.obj;
                        if (result.getError() == 3) {
                            Toast toast;
                            toast = Toast.makeText(LoginActivity.this, "你已成功登录", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            //跳转到登录成功的界面
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
//                            setResult(11, intent);
                            finish();
                        } else if (result.getError() == 1) {
                            Toast toast;
                            toast = Toast.makeText(LoginActivity.this, "没有此账号", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {
                            Toast toast;
                            toast = Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
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
        setContentView(R.layout.login);
        findViewById(R.id.bt_denglu).setOnClickListener(this);
        getTvZhuce().setOnClickListener(this);
        getTvWangjimima().setOnClickListener(this);
        imageView = findViewById(R.id.login_touxiang);
        final int[] zi = {0};
        final int[] zi1 = {0};

        getEdZhanghao().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (start >= 2) {
                    if (start <= zi[0]) {
                        imageView.setImageResource( R.drawable.a3);
                    }
                    if (start >= zi[0]) {
                        if (start >= 5) {
                            imageView.setImageResource(R.drawable.a1);
                        } else {
                            imageView.setImageResource(R.drawable.a2);
                        }
                    }
                }
                zi[0] = start;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        getEdMima().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (start >= 0){
                    imageView.setImageResource(R.drawable.a4);
                }
                if (start <= zi1[0]) {
                    imageView.setImageResource(R.drawable.a5);
                }
                zi1[0] = start;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getEdZhanghao().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    imageView.setImageResource(R.drawable.a2);
                }
            }
        });
        getEdMima().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    imageView.setImageResource(R.drawable.a2);
                }
            }
        });
        getEdZhanghao().setFocusable(true);
        getEdZhanghao().setFocusableInTouchMode(true);
        getEdMima().setFocusable(true);
        getEdMima().setFocusableInTouchMode(true);
        getEdZhanghao().clearFocus();
        getEdMima().clearFocus();
//        imageView.setImageResource(R.drawable.dengludonghua);
//        // 1. 设置动画
//        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
//        // 2. 获取动画对象
//        animationDrawable.start();
//        // 3. 启动动画

    }

    private EditText getEdZhanghao() {
        return (EditText) findViewById(R.id.ed_zhanghao);
    }

    private EditText getEdMima() {
        return (EditText) findViewById(R.id.ed_mima);
    }

    private TextView getTvZhuce() {
        return (TextView) findViewById(R.id.tv_zhuce);
    }

    private TextView getTvWangjimima() {
        return (TextView) findViewById(R.id.tv_wangjimima);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_denglu:

                //TODO implement
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //POST信息中加入用户名和密码
                            map.put("uid", getEdZhanghao().getText().toString().trim());
                            map.put("pwd", getEdMima().getText().toString().trim());
                            System.out.println("登录：" + map.get("uid"));
                            //HttpUtils.httpPostMethod(url, json, handler);
                            HttpUtils.post(MyApplication.url, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.e("DaiDai", "OnFaile:", e);
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    //final String m_result = response.body().string();
                                    String responseBody = response.body().string();
                                    System.out.println("信息：" + responseBody);
                                    Gson gson = new Gson();
                                    m_result = gson.fromJson(responseBody, UserIndividualInfoBean.class);
//                                    if (m_result.getVname().isEmpty() || m_result.getVname().equals("")) {
//                                        if (m_result.getQuanxian() == 1) {
//                                            m_result.setVname("学生");
//                                        } else if (m_result.getQuanxian() == 2) {
//                                            m_result.setVname("辅导员");
//                                        } else if (m_result.getQuanxian() == 3) {
//                                            m_result.setVname("老师");
//                                        } else if (m_result.getQuanxian() == 4) {
//                                            m_result.setVname("学校");
//                                        } else {
//                                            m_result.setVname("学生");
//                                        }
//                                    }
                                    MyApplication.user = m_result;
                                    System.out.println("登陆：" + MyApplication.user.getTname() );
                                    //发送登录成功的消息
                                    Message msg = handler.obtainMessage();
                                    msg.what = 123;
                                    msg.obj = m_result; //把登录结果也发送过去
                                    handler.sendMessage(msg);
                                }
                            }, map);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case R.id.tv_zhuce:
                Intent intent = new Intent(this, ZhuceActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.tv_wangjimima:
                break;
            default:
                break;
        }
    }

}
