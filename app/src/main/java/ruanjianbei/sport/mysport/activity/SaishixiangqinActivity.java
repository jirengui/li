package ruanjianbei.sport.mysport.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.bean.SaiShiBean;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.MyApplication;

import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SaishixiangqinActivity extends Activity  {

    private ImageView saishiFanhui;
    private CircleImageView saishiTouxiang;
    private TextView saishiName;
    private ImageView saishiTupian;
    private TextView saishiNeirong;
    private TextView saishiCanjia;
    private SaiShiBean saiShiBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setContentView(R.layout.saishixiangqin);

        Intent intent = getIntent();
        saiShiBean = (SaiShiBean) intent.getSerializableExtra("saishi");
        saishiFanhui = (ImageView) findViewById(R.id.saishi_fanhui);
        saishiTouxiang = (CircleImageView) findViewById(R.id.saishi_touxiang);
        saishiName = (TextView) findViewById(R.id.saishi_name);
        saishiTupian = (ImageView) findViewById(R.id.saishi_tupian);
        saishiNeirong = (TextView) findViewById(R.id.saishi_neirong);
        saishiCanjia = (TextView) findViewById(R.id.saishi_canjia);

        Picasso.with(this)
                .load(MyApplication.imageUri + saiShiBean.getUserIndividualInfoBean().getTouxiang())
                .into(saishiTouxiang);
        saishiName.setText(saiShiBean.getUserIndividualInfoBean().getName());
        saishiNeirong.setText(saiShiBean.getContext() + "\n\n" + "奖励积分：" + saiShiBean.getJiangli()
        + "\n\n"+ "开始时间：" + saiShiBean.getKaishitime() + "\n"+ "结束时间：" + saiShiBean.getJieshushijian());
        Picasso.with(this)
                .load(MyApplication.imageUri + saiShiBean.getTupian())
                .placeholder(getDrawable(R.drawable.caochang))
                .into(saishiTupian);
        if (saiShiBean.getPerStatus() != -1){
            saishiCanjia.setText("已参加");
        }
        if (saiShiBean.getStatus() == 2){
            saishiCanjia.setText("已过期");
        }
        saishiCanjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saishiCanjia.getText().toString().equals("参加赛事")) {
                    Map<String, String> map = new HashMap<>();
                    map.put("saishiId", String.valueOf(saiShiBean.getId()));
                    map.put("uid", String.valueOf(saiShiBean.getUserIndividualInfoBean().getId()));
                    map.put("jid", String.valueOf(MyApplication.user.getId()));
                    HttpUtils.post(MyApplication.baoming, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SaishixiangqinActivity.this, "恭喜你已成功参加此赛事。", Toast.LENGTH_LONG).show();
                                    saishiCanjia.setText("已参加");
                                }
                            });
                        }
                    }, map);
                }
            }
        });
        saishiFanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
