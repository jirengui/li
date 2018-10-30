package ruanjianbei.sport.mysport.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.MyApplication;

import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class ChuangjianzhanduiActivity extends Activity implements View.OnClickListener {

    private ImageView chuangjianzhanduiFanhui;
    private CircleImageView chuangjianTouxiang;
    private String uri = null;
    private Uri originalUri = null;
    private Boolean tupian = false, mingzi = false;
    private Button chuangjian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.chuangjianzhandui);

        chuangjianzhanduiFanhui = (ImageView) findViewById(R.id.chuangjianzhandui_fanhui);
        chuangjianTouxiang = (CircleImageView) findViewById(R.id.chuangjian_touxiang);
        chuangjian =  findViewById(R.id.chuangjian_chuangjianzhandui);
        chuangjian.setOnClickListener(this);
        chuangjianTouxiang.setOnClickListener(this);
        chuangjianzhanduiFanhui.setOnClickListener(this);
    }

    private EditText getChuangjianZhanduiming(){
        return (EditText) findViewById(R.id.chuangjian_zhanduiming);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chuangjianzhandui_fanhui:
                finish();
                break;
            case R.id.chuangjian_chuangjianzhandui:
                //TODO implement
                if (uri == null){
                    toast("请选择战队头像。");
                    break;
                }
                if (getChuangjianZhanduiming().getText().toString().isEmpty()){
                    toast("请输入战队名。");
                    break;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        File file = new File(uri);
                        HttpUtils.upzhanduitouxiang(String.valueOf(MyApplication.user.getId()),getChuangjianZhanduiming().getText().toString(), file, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String string = response.body().string();
                                System.out.println("创建战队：" + string);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        toast("创建成功");
                                    }
                                });
                                finish();

                            }
                        });
                    }
                }).start();
                break;
            case R.id.chuangjian_touxiang:
                pickPhoto();
                break;
        }
    }
    private void toast(String string){
        Toast toast;
        toast = Toast.makeText(ChuangjianzhanduiActivity.this, string, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    //从相册中取图片
    private void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("返回来了。。" + resultCode + "数据" + data + "返回requestCode" + requestCode);
        if (resultCode != RESULT_OK) {        //此处的 RESULT_OK 是系统自定义得一个常量
            Log.e("TAG->onresult", "ActivityResult resultCode error");
            return;
        }
        // 此处的用于判断接收的Activity是不是你想要的那个
        if (requestCode == 2) {
            tupian = true;
            System.out.println("返回图片。。" + data.getData());
            originalUri = data.getData();        //获得图片的uri
            uri = getImagePathFromURI(originalUri);//华为屌
            if (uri == null) {
                uri = originalUri.getEncodedPath();
            }
            chuangjianTouxiang.setImageURI(originalUri);
        }
    }
    //正确的图片地址
    public String getImagePathFromURI(Uri uri) {
        Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
        String path = null;
        if (cursor != null) {
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
            cursor.close();

            cursor = this.getContentResolver().query(
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
}
