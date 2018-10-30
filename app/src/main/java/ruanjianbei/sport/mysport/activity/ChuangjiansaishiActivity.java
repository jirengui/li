package ruanjianbei.sport.mysport.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.bean.JuBanSaiShiBean;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.MyApplication;

public class ChuangjiansaishiActivity extends Activity implements View.OnClickListener {

    private ImageView chuangjiansaishiFanhui;
    private ImageView chuangjiansaishiTu;
    private Uri originalUri = null;
    private String uri = null;
    private Boolean aBoolean = false;
    private JuBanSaiShiBean juBanSaiShiBean = new JuBanSaiShiBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chuangjiansaishi);

        chuangjiansaishiFanhui = (ImageView) findViewById(R.id.chuangjiansaishi_fanhui);
        chuangjiansaishiTu = (ImageView) findViewById(R.id.chuangjiansaishi_tu);
        chuangjiansaishiTu.setOnClickListener(this);
        findViewById(R.id.chuangjiansaishi_chuangjian).setOnClickListener(this);
        chuangjiansaishiFanhui.setOnClickListener(this);
    }

    //从相册中取图片
    private void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 2);
    }

    //正确的图片地址
    public String getImagePathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        String path = null;
        if (cursor != null) {
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
            cursor.close();

            cursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
            if (cursor != null) {
                cursor.moveToFirst();
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();
            }
        }
        return path;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            originalUri = data.getData();        //获得图片的uri
            uri = getImagePathFromURI(originalUri);//华为屌
            if (uri == null) {
                uri = originalUri.getEncodedPath();
            }
            juBanSaiShiBean.setTupian(uri);
            aBoolean = true;
            chuangjiansaishiTu.setImageURI(originalUri);
        }
    }

    private EditText getChuangjiansaishiStartYue() {
        return (EditText) findViewById(R.id.chuangjiansaishi_start_yue);
    }

    private EditText getChuangjiansaishiStartRi() {
        return (EditText) findViewById(R.id.chuangjiansaishi_start_ri);
    }

    private EditText getChuangjiansaishiStartShi() {
        return (EditText) findViewById(R.id.chuangjiansaishi_start_shi);
    }

    private EditText getChuangjiansaishiStartMiao() {
        return (EditText) findViewById(R.id.chuangjiansaishi_start_miao);
    }

    private EditText getChuangjiansaishiEndYue() {
        return (EditText) findViewById(R.id.chuangjiansaishi_end_yue);
    }

    private EditText getChuangjiansaishiEndRi() {
        return (EditText) findViewById(R.id.chuangjiansaishi_end_ri);
    }

    private EditText getChuangjiansaishiEndShi() {
        return (EditText) findViewById(R.id.chuangjiansaishi_end_shi);
    }

    private EditText getChuangjiansaishiEndMiao() {
        return (EditText) findViewById(R.id.chuangjiansaishi_end_miao);
    }

    private EditText getChuangjiansaishiDidian() {
        return (EditText) findViewById(R.id.chuangjiansaishi_didian);
    }

    private EditText getChuangjiansaishiShuoming() {
        return (EditText) findViewById(R.id.chuangjiansaishi_shuoming);
    }

    private EditText getChuangjiansaishiJiangli() {
        return (EditText) findViewById(R.id.chuangjiansaishi_jiangli);
    }

    private Boolean panduan() {
        if (!aBoolean && juBanSaiShiBean.getTupian() == null) {
            Toast toast = Toast.makeText(this, "图片不能为空。", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return false;
        }
        if (getChuangjiansaishiDidian().getText() == null || getChuangjiansaishiDidian().getText().toString().equals("")) {
            Toast toast = Toast.makeText(this, "地点不能为空。", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return false;
        } else {
            juBanSaiShiBean.setPlace(getChuangjiansaishiDidian().getText().toString());
        }

        if (getChuangjiansaishiEndMiao().getText() == null || getChuangjiansaishiEndMiao().getText().toString().equals("")
                || getChuangjiansaishiEndRi().getText() == null || getChuangjiansaishiEndRi().getText().toString().equals("")
                || getChuangjiansaishiEndShi().getText() == null || getChuangjiansaishiEndShi().getText().toString().equals("")
                || getChuangjiansaishiEndYue().getText() == null || getChuangjiansaishiEndYue().getText().toString().equals("")) {
            Toast toast = Toast.makeText(this, "结束时间不能为空。", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return false;
        } else {
            juBanSaiShiBean.setJieshutime("2018-" + getChuangjiansaishiEndYue().getText().toString() + "-"
                    + getChuangjiansaishiEndRi().getText().toString() + " "
            + getChuangjiansaishiEndShi().getText().toString() + ":"
            + getChuangjiansaishiEndMiao().getText().toString());
        }

        if (getChuangjiansaishiStartMiao().getText() == null || getChuangjiansaishiStartMiao().getText().toString().equals("")
                || getChuangjiansaishiStartRi().getText() == null || getChuangjiansaishiStartRi().getText().toString().equals("")
                || getChuangjiansaishiStartShi().getText() == null || getChuangjiansaishiStartShi().getText().toString().equals("")
                || getChuangjiansaishiStartYue().getText() == null || getChuangjiansaishiStartYue().getText().toString().equals("")) {
            Toast toast = Toast.makeText(this, "开始时间不能为空。", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return false;
        }else {
            juBanSaiShiBean.setKaishitime("2018-" + getChuangjiansaishiStartYue().getText().toString() + "-"
                    + getChuangjiansaishiStartRi().getText().toString() + " "
                    + getChuangjiansaishiStartShi().getText().toString() + ":"
                    + getChuangjiansaishiStartMiao().getText().toString());
        }

        if (getChuangjiansaishiShuoming().getText() == null || getChuangjiansaishiShuoming().getText().toString().equals("")) {
            Toast toast = Toast.makeText(this, "赛事说明不能为空。", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return false;
        }else {
            juBanSaiShiBean.setContext(getChuangjiansaishiShuoming().getText().toString());
        }
        if (getChuangjiansaishiJiangli().getText() == null || getChuangjiansaishiJiangli().getText().toString().equals("")) {
            Toast toast = Toast.makeText(this, "赛事奖励不能为空。", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return false;
        }else {
            juBanSaiShiBean.setLiwu(getChuangjiansaishiJiangli().getText().toString());
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chuangjiansaishi_chuangjian:
                //TODO implement
                if (panduan()) {
                    juBanSaiShiBean.setUid(MyApplication.user.getId());
                    HttpUtils.chuangjiansaishi(juBanSaiShiBean, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            System.out.println("举办赛事：" + response.body().string());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast toast = Toast.makeText(ChuangjiansaishiActivity.this, "赛事创建成功", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                    finish();
                                }
                            });
                        }
                    });
                }
                break;
            case R.id.chuangjiansaishi_tu:
                if (!aBoolean){
                    pickPhoto();
                }
                break;
            case R.id.chuangjiansaishi_fanhui:
                finish();
                break;
        }
    }
}
