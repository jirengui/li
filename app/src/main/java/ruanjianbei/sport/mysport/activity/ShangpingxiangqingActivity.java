package ruanjianbei.sport.mysport.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.bean.LiWuBean;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.MyApplication;

public class ShangpingxiangqingActivity extends Activity {

    private ImageView shangpingFanhui;
    private ImageView shangpingTupian;
    private TextView shangpingName;
    private TextView shangpingJiage;
    private TextView shangpingDuihuan;
    private LiWuBean liWuBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shangpingxiangqing);

        shangpingFanhui = (ImageView) findViewById(R.id.shangping_fanhui);
        shangpingTupian = (ImageView) findViewById(R.id.shangping_tupian);
        shangpingName = (TextView) findViewById(R.id.shangping_name);
        shangpingJiage = (TextView) findViewById(R.id.shangping_jiage);
        shangpingDuihuan = (TextView) findViewById(R.id.shangping_duihuan);

        Intent intent = getIntent();
        liWuBean = (LiWuBean) intent.getSerializableExtra("bean");
        shangpingName.setText(liWuBean.getName());
        shangpingJiage.setText(liWuBean.getJifen());
        Picasso.with(this)
                .load(MyApplication.imageUri + liWuBean.getTupian())
                .placeholder(getResources().getDrawable(R.drawable.wanou))
                .into(shangpingTupian);

        shangpingFanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        shangpingDuihuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shangpingDuihuan.getText().toString().equals("确 认 兑 换")) {
                    if (Integer.parseInt(liWuBean.getJifen()) > Integer.parseInt(MyApplication.user.getJifen())){
                        Toast toast = Toast.makeText(ShangpingxiangqingActivity.this, "积分不足请充值。", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ShangpingxiangqingActivity.this);
                        builder.setTitle("");
                        builder.setMessage("是否兑换该商品？");
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                shangpingDuihuan.setText("已 兑 换");
                                updatajifen("-" + liWuBean.getJifen());
                                Toast toast = Toast.makeText(ShangpingxiangqingActivity.this, "兑换成功。", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }else {
                    Toast toast = Toast.makeText(ShangpingxiangqingActivity.this,"你已兑换该商品。",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            }
        });
    }

    private void updatajifen(String jifen){
        int jifen1 = 0;
        if (MyApplication.user.getJifen() != null) {
            jifen1 = Integer.parseInt(MyApplication.user.getJifen());
            jifen1 += Integer.parseInt(jifen);
        }
        MyApplication.user.setJifen(String.valueOf(jifen1));
        Map<String, String> map = new HashMap<>();
        map.put("uid", String.valueOf(MyApplication.user.getId()));
        map.put("count", jifen);
        map.put("shijian", "兑换");
        HttpUtils.post(MyApplication.updatejifenurl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("积分：" + response.body().string());
            }
        },map);
    }
}
