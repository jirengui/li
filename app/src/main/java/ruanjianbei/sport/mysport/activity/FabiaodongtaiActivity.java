package ruanjianbei.sport.mysport.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.githang.statusbar.StatusBarCompat;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.album.Album;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.adapter.FreshImgCallBack;
import ruanjianbei.sport.mysport.adapter.ImgGridAdapter;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.MyApplication;

public class FabiaodongtaiActivity extends Activity implements View.OnClickListener{

    private Map<String, String[]> map = new HashMap<>();
    private String a[] = new String[1];
    private String b[] = new String[1];
    private String c[] = new String[1];
    private List<String> pathList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        //false为白色字体，ture为黑色字体
        StatusBarCompat.setLightStatusBar(getWindow(), true);
        setContentView(R.layout.fabiaodongtai);

        iniv();
        getTimeByCalendar();
    }
    public void getTimeByCalendar(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);//获取年份
        int month=cal.get(Calendar.MONTH);//获取月份
        int day=cal.get(Calendar.DATE);//获取日
        int hour=cal.get(Calendar.HOUR);//小时
        int minute=cal.get(Calendar.MINUTE);//分
        int second=cal.get(Calendar.SECOND);//秒
        int weekOfYear = cal.get(Calendar.DAY_OF_WEEK);//一周的第几天
        getTvRi().setText(String.valueOf(day));
        if (month+1 < 10) {
            month = month+1;
            getTvnian().setText("0" + month + "/" + year);
        }else {
            month = month+1;
            getTvnian().setText(month  + "/" + year);
        }
        switch (weekOfYear){
            case 1:
                getTvzhou().setText("周日");
                break;
            case 2:
                getTvzhou().setText("周一");
                break;
            case 3:
                getTvzhou().setText("周二");
                break;
            case 4:
                getTvzhou().setText("周三");
                break;
            case 5:
                getTvzhou().setText("周四");
                break;
            case 6:
                getTvzhou().setText("周五");
                break;
            case 7:
                getTvzhou().setText("周六");
                break;
        }
    }
    private void iniv(){
        findViewById(R.id.im_fabiaowenzi).setOnClickListener(this);
        findViewById(R.id.tv_fabiaowenzi).setOnClickListener(this);
        findViewById(R.id.im_fabiaoxiangce).setOnClickListener(this);
        findViewById(R.id.tv_fabiaoxiangce).setOnClickListener(this);
        findViewById(R.id.im_fabiaopaishe).setOnClickListener(this);
        findViewById(R.id.tv_fabiaopaishe).setOnClickListener(this);
        findViewById(R.id.im_fabiaozhibo).setOnClickListener(this);
        findViewById(R.id.tv_fabiaozhibo).setOnClickListener(this);
        findViewById(R.id.im_fabiaoyinyue).setOnClickListener(this);
        findViewById(R.id.tv_fabiaoyinyue).setOnClickListener(this);
        findViewById(R.id.im_fabiaodaka).setOnClickListener(this);
        findViewById(R.id.tv_fabiaodaika).setOnClickListener(this);
        findViewById(R.id.im_fabiaoguanbi).setOnClickListener(this);
        Picasso.with(FabiaodongtaiActivity.this)
                .load(MyApplication.imageUri + MyApplication.user.getTouxiang())
                .into(getImguanggao());
        TextView tianqi = findViewById(R.id.fabiao_tv_dizhitianqi);
        if (MyApplication.city != null && !MyApplication.city.isEmpty() && MyApplication.tianqi != null && !MyApplication.tianqi.isEmpty()) {
            tianqi.setText(MyApplication.city + "：" + MyApplication.tianqi);
        }


    }

    private TextView getTvRi(){
        return (TextView) findViewById(R.id.fabiao_tv_ri);
    }
    private TextView getTvzhou(){
        return (TextView) findViewById(R.id.fabiao_tv_zhou);
    }
    private TextView getTvnian(){
        return (TextView) findViewById(R.id.fabiao_tv_nianyue);
    }
    private TextView getTvtianqi(){
        return (TextView) findViewById(R.id.fabiao_tv_dizhitianqi);
    }
    private ImageView getImguanggao(){
        return (ImageView) findViewById(R.id.fabiao_im_guanggao);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.im_fabiaoxiangce:
            case R.id.tv_fabiaoxiangce:
            case R.id.tv_fabiaodaika:
            case R.id.im_fabiaodaka:
            case R.id.tv_fabiaopaishe:
            case R.id.im_fabiaopaishe:
            case R.id.tv_fabiaoyinyue:
            case R.id.im_fabiaoyinyue:
            case R.id.tv_fabiaowenzi:
            case R.id.im_fabiaowenzi :
                //TODO implement
                startActivity(new Intent(FabiaodongtaiActivity.this, FabiaoshuoshuoActivity.class));
                finish();
                break;
            case R.id.im_fabiaozhibo:
            case R.id.tv_fabiaozhibo:
                startActivity(new Intent(FabiaodongtaiActivity.this, ZhiboActivity.class));
                finish();
            case R.id.im_fabiaoguanbi:
                finish();
                break;


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) { // 判断是否成功。
                // 拿到用户选择的图片路径List：
//                pathList = Album.parseResult(data);

            } else if (resultCode == RESULT_CANCELED) { // 用户取消选择。
                // 根据需要提示用户取消了选择。
                Toast.makeText(this, "取消", Toast.LENGTH_LONG).show();
            }
        }
    }


}
