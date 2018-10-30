package ruanjianbei.sport.mysport.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import de.hdodenhof.circleimageview.CircleImageView;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.util.MyApplication;

public class ShentisuzhiActivity extends Activity  {

    private ImageView shentisuzhiFanhui;
    private TextView touxiang_tizhong, touxiang_shengao, tizhong, shengao,  bmi, bianji;
    private CircleImageView touxiang;
    private EditText et_shengao, et_tizhong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.shentisuzhi);

        shentisuzhiFanhui = (ImageView) findViewById(R.id.shentisuzhi_fanhui);
        shentisuzhiFanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        touxiang_tizhong = findViewById(R.id.shentisuzhi_touxiangtizhong);
        touxiang_shengao = findViewById(R.id.shentisuzhi_touxiangshengao);
        tizhong = findViewById(R.id.shentisuzhi_tizhong);
        shengao = findViewById(R.id.shentisuzhi_shengao);
        bmi = findViewById(R.id.shentisuzhi_bmi);
        et_shengao = findViewById(R.id.shentisuzhi_etshengao);
        et_tizhong = findViewById(R.id.shentisuzhi_ettizhong);
        bianji = findViewById(R.id.shentisuzhi_bianji);
        touxiang = findViewById(R.id.shentisuzhi_touxiang);

        SharedPreferences pref = getSharedPreferences("name", MODE_PRIVATE);
        shengao.setText(pref.getString(String.valueOf(MyApplication.user.getId()) + "shengao", "178"));
        tizhong.setText(pref.getString(String.valueOf(MyApplication.user.getId()) + "tizhong", "60"));
        touxiang_shengao.setText(shengao.getText() + "cm");
        touxiang_tizhong.setText(tizhong.getText() + "kg");

        Picasso.with(this)
                .load(MyApplication.imageUri + MyApplication.user.getTouxiang())
                .into(touxiang);
        double bmia = Double.parseDouble(tizhong.getText().toString()) / ((Double.parseDouble(shengao.getText().toString())/100) * (Double.parseDouble(shengao.getText().toString())/100));
        DecimalFormat df = new DecimalFormat("#.0");
        String str = df.format(bmia);
        bmi.setText(str);
        bianji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bianji.getText().toString().equals("编辑")){
                    et_tizhong.setText(tizhong.getText());
                    et_shengao.setText(shengao.getText());
                    et_shengao.setVisibility(View.VISIBLE);
                    et_tizhong.setVisibility(View.VISIBLE);
                    shengao.setVisibility(View.GONE);
                    tizhong.setVisibility(View.GONE);
                    bianji.setText("完成");
                }else {
                    tizhong.setText(et_tizhong.getText());
                    shengao.setText(et_shengao.getText());
                    touxiang_shengao.setText(et_shengao.getText()+"cm");
                    touxiang_tizhong.setText(et_tizhong.getText()+"kg");
                    et_shengao.setVisibility(View.GONE);
                    et_tizhong.setVisibility(View.GONE);
                    shengao.setVisibility(View.VISIBLE);
                    tizhong.setVisibility(View.VISIBLE);
                    double bmia = Double.parseDouble(tizhong.getText().toString()) / ((Double.parseDouble(shengao.getText().toString())/100) * (Double.parseDouble(shengao.getText().toString())/100));
                    DecimalFormat df = new DecimalFormat("#.0");
                    String str = df.format(bmia);
                    bmi.setText(str);
                    bianji.setText("编辑");
                    SharedPreferences.Editor editor = getSharedPreferences("name", MODE_PRIVATE).edit();
                    editor.putString(String.valueOf(MyApplication.user.getId()) + "shengao", shengao.getText().toString());
                    editor.putString(String.valueOf(MyApplication.user.getId()) + "tizhong", tizhong.getText().toString());
                    editor.commit();

                }
            }
        });
    }

}
