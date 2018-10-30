package ruanjianbei.sport.mysport.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.adapter.TitleFragmentPagerAdapter;
import ruanjianbei.sport.mysport.fragment.FragmentDongtaiFragment;
import ruanjianbei.sport.mysport.fragment.GerenxinxiFragment;
import ruanjianbei.sport.mysport.fragment.WodefabuFragment;
import ruanjianbei.sport.mysport.fragment.WodezhanduiFragment;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.MyApplication;

import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GerenjiemianActivity extends AppCompatActivity {

    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private LinearLayout loginLayout;
    private CircleImageView headImg;
    private TextView head_name;
    private TextView head_jianjie;
    private ImageView fanhui;
    private Toolbar toolbar;
    private TabLayout head_viewpage;
    private ViewPager viewpager;
    private Uri originalUri = null;
    private String uri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.gerenjiemian);

        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        loginLayout = (LinearLayout) findViewById(R.id.login_layout);
        headImg = (CircleImageView) findViewById(R.id.head_img);
        head_name = (TextView) findViewById(R.id.head_name);
        head_jianjie = (TextView) findViewById(R.id.head_jianjie);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fanhui = (ImageView) findViewById(R.id.geren_fanhui);
        head_viewpage = (TabLayout) findViewById(R.id.head_viewpage);
        viewpager = (ViewPager) findViewById(R.id.geren_viewpager);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset <= -loginLayout.getHeight() / 2) {
                    collapsingToolbarLayout.setTitle(MyApplication.user.getVname());
                } else {
                    collapsingToolbarLayout.setTitle(" ");
                }
            }
        });
        WodezhanduiFragment wodezhanduiFragment = new WodezhanduiFragment();
        GerenxinxiFragment gerenxinxiFragment = new GerenxinxiFragment();
        WodefabuFragment wodefabuFragment = new WodefabuFragment();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(wodezhanduiFragment);
        fragments.add(wodefabuFragment);
        fragments.add(gerenxinxiFragment);
        TitleFragmentPagerAdapter adapter1 = new TitleFragmentPagerAdapter(getSupportFragmentManager(), fragments, new String[]{"我的战队",  "我的发布", "个人信息"});
        viewpager.setAdapter(adapter1);
        head_viewpage.setupWithViewPager(viewpager);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        Picasso.with(this)
                .load(MyApplication.imageUri + MyApplication.user.getTouxiang())
                .into(headImg);
        head_name.setText(MyApplication.user.getVname());
        head_jianjie.setText("关注" + MyApplication.user.getGidcount() + " | " + "粉丝" +  MyApplication.user.getFidcount());
        headImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPhoto();
            }
        });
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
    //从相册中取图片
    private void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("返回来了。。" + resultCode + "数据" + data + "返回requestCode" + requestCode);
        if (resultCode != RESULT_OK) {        //此处的 RESULT_OK 是系统自定义得一个常量
            Log.e("TAG->onresult", "ActivityResult resultCode error");
            return;
        }
        // 此处的用于判断接收的Activity是不是你想要的那个
        if (requestCode == 2) {
            System.out.println("返回图片。。" + data.getData());
            originalUri = data.getData();        //获得图片的uri
            uri = getImagePathFromURI(originalUri);//华为屌
            if (uri == null) {
                uri = originalUri.getEncodedPath();
            }
            headImg.setImageURI(originalUri);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File file = new File(uri);

                    HttpUtils.upload(String.valueOf(MyApplication.user.getId()), file, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            System.out.println("回调：" + response.toString());
                        }
                    });
                }
            }).start();

        }
    }



}
