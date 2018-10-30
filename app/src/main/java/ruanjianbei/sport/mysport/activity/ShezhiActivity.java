package ruanjianbei.sport.mysport.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.util.MyApplication;

import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ShezhiActivity extends Activity implements View.OnClickListener{

    private ImageView shezhiFanhui;
    private LinearLayout shezhiZhanghao;
    private CircleImageView shezhiTouxiang;
    private LinearLayout shezhiShoujihao;
    private LinearLayout shezhiYinsi;
    private LinearLayout shezhiBangzhu;
    private LinearLayout shezhiGuanyu;
    private TextView shezhiTuichudenglu, shoujihao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.shezhi);

        shezhiFanhui = (ImageView) findViewById(R.id.shezhi_fanhui);
        shezhiZhanghao = (LinearLayout) findViewById(R.id.shezhi_zhanghao);
        shezhiTouxiang = (CircleImageView) findViewById(R.id.shezhi_touxiang);
        shezhiShoujihao = (LinearLayout) findViewById(R.id.shezhi_shoujihao);
        shezhiYinsi = (LinearLayout) findViewById(R.id.shezhi_yinsi);
        shezhiBangzhu = (LinearLayout) findViewById(R.id.shezhi_bangzhu);
        shezhiGuanyu = (LinearLayout) findViewById(R.id.shezhi_guanyu);
        shezhiTuichudenglu = (TextView) findViewById(R.id.shezhi_tuichudenglu);
        shoujihao = findViewById(R.id.shezhi_tvshoujihao);

        shezhiBangzhu.setOnClickListener(this);
        shezhiFanhui.setOnClickListener(this);
        shezhiGuanyu.setOnClickListener(this);
        shezhiShoujihao.setOnClickListener(this);
        shezhiTuichudenglu.setOnClickListener(this);
        shezhiYinsi.setOnClickListener(this);
        shezhiZhanghao.setOnClickListener(this);


        Picasso.with(this)
                .load(MyApplication.imageUri + MyApplication.user.getTouxiang())
                .into(shezhiTouxiang);
        if ( MyApplication.user.getUsername() != null && !MyApplication.user.getUsername().isEmpty() ) {
            if (MyApplication.user.getUsername().length() > 7) {
                shoujihao.setText(MyApplication.user.getUsername().substring(0, 3) + "****" + MyApplication.user.getUsername().substring(7, MyApplication.user.getUsername().length()));
            }
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
            case R.id.shezhi_bangzhu:
                toast("帮助");
                break;
            case R.id.shezhi_fanhui:
                finish();
                break;
            case R.id.shezhi_guanyu:
                toast("关于");
                break;
            case R.id.shezhi_shoujihao:
                toast("手机号");
                break;
            case R.id.shezhi_tuichudenglu:
                SharedPreferences.Editor editor = getSharedPreferences("name", MODE_PRIVATE).edit();
                editor.clear();
                editor.commit();
                MyApplication.chushihua(-1);
                MainActivity.main.finish();
                startActivity(new Intent(ShezhiActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.shezhi_yinsi:
                toast("隐私");
                break;
            case R.id.shezhi_zhanghao:
                toast("账号");
                break;
        }
    }
    private void toast(String string){
        Toast toast;
        toast = Toast.makeText(ShezhiActivity.this, string, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
