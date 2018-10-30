package ruanjianbei.sport.mysport.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

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

public class BangdingshenfenActivity extends Activity implements View.OnClickListener {

    private ImageView bangdingFanhui;
    private String shoujihao, mima, xuehao, xingming;
    private UserIndividualInfoBean m_result;
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
                            toast = Toast.makeText(BangdingshenfenActivity.this, "你已成功登录", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            //跳转到登录成功的界面
                            Intent intent = new Intent(BangdingshenfenActivity.this, MainActivity.class);
                            startActivity(intent);
//                            setResult(11, intent);
                            finish();
                        } else if (result.getError() == 1) {
                           Toast toast;
                            toast = Toast.makeText(BangdingshenfenActivity.this, "没有此账号", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }else if (result.getError() == 4){
                            Toast toast;
                            toast = Toast.makeText(BangdingshenfenActivity.this, "注册失败，学号姓名不匹配", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else if (result.getError() == 2){
                            Toast toast;
                            toast = Toast.makeText(BangdingshenfenActivity.this, "密码错误", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }else if (result.getError() == 5){
                            Toast toast;
                            toast = Toast.makeText(BangdingshenfenActivity.this, "注册失败,学号已注册", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }else {
                            Toast toast;
                            toast = Toast.makeText(BangdingshenfenActivity.this, "注册失败", Toast.LENGTH_SHORT);
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
        setContentView(R.layout.bangdingshenfen);

        Intent intent = getIntent();
        shoujihao = intent.getStringExtra("shoujihao");
        mima = intent.getStringExtra("mima");
        bangdingFanhui = (ImageView) findViewById(R.id.bangding_fanhui);
        bangdingFanhui.setOnClickListener(this);
        findViewById(R.id.bangding_jiaoyan).setOnClickListener(this);
    }

    private EditText getBangdingXuehao(){
        return (EditText) findViewById(R.id.bangding_xuehao);
    }

    private EditText getBangdingXingming(){
        return (EditText) findViewById(R.id.bangding_xingming);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bangding_jiaoyan:
                //TODO implement
                final String xuehao = getBangdingXuehao().getText().toString();
                final String xingming = getBangdingXingming().getText().toString();
                if (xuehao.isEmpty()){
                    Toast.makeText(this,"学号不能为空。", Toast.LENGTH_SHORT).show();
                }else if (xingming.isEmpty()){
                    Toast.makeText(this,"姓名不能为空。", Toast.LENGTH_SHORT).show();
                }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final Map<String, String> map = new HashMap<>();
                            map.put("username", shoujihao);
                            map.put("password", mima);
                            map.put("xuehao", xuehao);
                            map.put("name", xingming);
                            HttpUtils.post(MyApplication.updatemima, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String responseBody = response.body().string();
                                    System.out.println("注册信息：" + responseBody);
                                    Gson gson = new Gson();
                                    m_result = gson.fromJson(responseBody, UserIndividualInfoBean.class);
                                    if (m_result.getVname() == null || m_result.getVname().isEmpty() || m_result.getVname().equals("")) {
                                        if (m_result.getQuanxian() == 1) {
                                            m_result.setVname("学生");
                                        } else if (m_result.getQuanxian() == 2) {
                                            m_result.setVname("辅导员");
                                        } else if (m_result.getQuanxian() == 3) {
                                            m_result.setVname("老师");
                                        } else if (m_result.getQuanxian() == 4) {
                                            m_result.setVname("学校");
                                        } else {
                                            m_result.setVname("学生");
                                        }
                                    }
                                    MyApplication.user = m_result;
                                    System.out.println("注册信息：" + m_result.getTname() + "My：" + MyApplication.user.getTname());

                                    //发送登录成功的消息
                                    Message msg = handler.obtainMessage();
                                    msg.what = 123;
                                    msg.obj = m_result; //把登录结果也发送过去
                                    handler.sendMessage(msg);
                                }
                            },map);
                        }
                    }).start();

                }
                break;
            case R.id.bangding_fanhui:
                startActivity(new Intent(BangdingshenfenActivity.this, ZhuceActivity.class));
                finish();
                break;
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
        startActivity(new Intent(BangdingshenfenActivity.this, ZhuceActivity.class));
        finish();
    }
}
