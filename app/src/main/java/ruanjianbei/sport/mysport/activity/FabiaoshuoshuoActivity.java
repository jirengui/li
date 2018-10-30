package ruanjianbei.sport.mysport.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.githang.statusbar.StatusBarCompat;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.adapter.FreshImgCallBack;
import ruanjianbei.sport.mysport.adapter.ImgGridAdapter;
import ruanjianbei.sport.mysport.fragment.DongtaiFragment;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.MyApplication;

public class FabiaoshuoshuoActivity extends Activity implements View.OnClickListener, FreshImgCallBack {

    private TextView tvFabiaoquxiao;
    private TextView tvFabiaofasong;
    private ImageView fabiaoPaizhao;
    private ImageView fabiaoTupian;
    private ImageView fabiaojieping;
    private Map<String, String[]> map;
    private String context[] = new String[1];
    private List<String> pathList =  new ArrayList<>();;
    private ImgGridAdapter imgGridAdapter = null;
    private GridView gridView;
    private static final int REQUEST_CODE_GALLERY = 100;//打开相册
    private static final int REQUEST_CODE_PREVIEW = 101;//预览图片
    private List<String> list = new ArrayList<>();
//    private static final String SD_PATH = "/sdcard/dskqxt/pic/";
//    private static final String IN_PATH = "/dskqxt/pic/";
//
//    /**
//     * 随机生产文件名
//     *
//     * @return
//     */
//    private static String generateFileName() {
//        return UUID.randomUUID().toString();
//    }
//    /**
//     * 保存bitmap到本地
//     *
//     * @param context
//     * @param mBitmap
//     * @return
//     */
//    public static String saveBitmap(Context context, Bitmap mBitmap) {
//        String savePath;
//        File filePic;
//        if (Environment.getExternalStorageState().equals(
//                Environment.MEDIA_MOUNTED)) {
//            savePath = SD_PATH;
//        } else {
//            savePath = context.getApplicationContext().getFilesDir()
//                    .getAbsolutePath()
//                    + IN_PATH;
//        }
//        try {
//            filePic = new File(savePath + generateFileName() + ".jpg");
//            if (!filePic.exists()) {
//                filePic.getParentFile().mkdirs();
//                filePic.createNewFile();
//            }
//            FileOutputStream fos = new FileOutputStream(filePic);
//            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//            fos.flush();
//            fos.close();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return null;
//        }
//        System.out.println("文件名：" + filePic.getAbsolutePath());
//        return filePic.getAbsolutePath();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        //false为白色字体，ture为黑色字体
        StatusBarCompat.setLightStatusBar(getWindow(), true);
        initFresco();
        setContentView(R.layout.fabiaoshuoshuo);

        Intent intent = getIntent();

        tvFabiaoquxiao = (TextView) findViewById(R.id.tv_fabiaoquxiao);
        tvFabiaofasong = (TextView) findViewById(R.id.tv_fabiaofasong);
        fabiaoPaizhao = (ImageView) findViewById(R.id.fabiao_paizhao);
        fabiaoTupian = (ImageView) findViewById(R.id.fabiao_tupian);

        fabiaojieping = (ImageView) findViewById(R.id.item_fabiao_jieping);
        tvFabiaofasong.setOnClickListener(this);
        fabiaoPaizhao.setOnClickListener(this);
        fabiaoTupian.setOnClickListener(this);
        tvFabiaoquxiao.setOnClickListener(this);
        gridView = findViewById(R.id.item_fabiao_tupian);
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra("bu");
            if (MyApplication.lujing != null) {
                fabiaojieping.setVisibility(View.VISIBLE);
                String lujing = MyApplication.lujing;
                System.out.println("路径：" + lujing);
                Picasso.with(this)
                        .load("file://"+lujing)
                        .into(fabiaojieping);

                pathList.add(lujing);
                MyApplication.lujing = null;
            }
            if (bundle != null) {
                getEtFabiaoneirong().setText("无敌的我！");
                if (bundle.getStringArrayList("tupian") != null) {
                    list = bundle.getStringArrayList("tupian");
                    if (imgGridAdapter == null) {
                        System.out.println("初始化适配器：");
                        imgGridAdapter = new ImgGridAdapter(FabiaoshuoshuoActivity.this, (ArrayList<String>) list, 5, FabiaoshuoshuoActivity.this);
                        gridView.setAdapter(imgGridAdapter);
                    } else {
                        if (list != null) {
                            System.out.println("适配器刷新" + list.get(0));
                        }
                        imgGridAdapter.replaceAll(list);
                    }
                }
            }
        }

    }

    private EditText getEtFabiaoneirong() {
        return (EditText) findViewById(R.id.et_fabiaoneirong);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_fabiaofasong:
                if (tvFabiaofasong.getText().toString().equals("发送")) {
                    System.out.println("发表说说：");
                    map = new HashMap<>();
                    context[0] = getEtFabiaoneirong().getText().toString();
                    if (context[0].isEmpty()) {
                        Toast.makeText(getApplicationContext(), "请输入内容。", Toast.LENGTH_LONG).show();
                    } else {
                        if (list != null) {
                            pathList.addAll(list);
                        }
                        map.put("context", context);
                        HttpUtils.fabiaoshuoshuo(String.valueOf(MyApplication.user.getId()), map, pathList, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
//                            Intent intent = new Intent();
//                            intent.putExtra("ss", 2);
//                            intent.setClass(FabiaoshuoshuoActivity.this, MainActivity.class);
//                            startActivity(intent);
                                finish();
                            }
                        });
                    }
                }
                tvFabiaofasong.setText("发送中");
                break;
            case R.id.fabiao_paizhao:
            case R.id.fabiao_tupian:
                openGallery();
                break;
            case R.id.tv_fabiaoquxiao:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 6) {
            if (resultCode == RESULT_OK) { // 判断是否成功。
                // 拿到用户选择的图片路径List：
//                imgGridAdapter.notifyDataSetChanged();

            } else if (resultCode == RESULT_CANCELED) { // 用户取消选择。
                // 根据需要提示用户取消了选择。
                Toast.makeText(this, "取消", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void openGallery() {
        list = new ArrayList<>();
        Album.album(this)
                .multipleChoice()
                .selectCount(3)
                .camera(true)
                .columnCount(2)
                .requestCode(6)
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(int requestCode, @NonNull ArrayList<AlbumFile> result) {
                        for (int i = 0; i < result.size(); i++) {
                            list.add(result.get(i).getPath());
                            System.out.println("获取图片：" + result.get(i).getPath());
                        }
                        if (imgGridAdapter == null) {
                            System.out.println("初始化适配器：");
                            imgGridAdapter = new ImgGridAdapter(FabiaoshuoshuoActivity.this, (ArrayList<String>) list, 5, FabiaoshuoshuoActivity.this);
                            gridView.setAdapter(imgGridAdapter);
                        } else {
                            System.out.println("适配器刷新" + list.get(0));
                            imgGridAdapter.replaceAll(list);
                        }
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(int requestCode, @NonNull String result) {
                        Toast.makeText(FabiaoshuoshuoActivity.this, "取消", Toast.LENGTH_LONG).show();
                    }
                })
                .start();
    }

    //将图片添加进手机相册
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.parse("file://" + list.get(0)));
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    public void previewImg(int position) {
        Album.gallery(this)//预览图片
                .requestCode(6)
                .currentPosition(position)
                .checkable(false)
                .start();
    }

    //更新图片：当前用于删除
    @Override
    public void updateGvImgShow(int position) {
        if (position < list.size()) {
            list.remove(position);
        }
        imgGridAdapter.notifyDataSetChanged();
    }

    private void initFresco() {
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(this, config);
    }
}
