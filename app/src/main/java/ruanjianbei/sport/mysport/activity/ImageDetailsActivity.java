package ruanjianbei.sport.mysport.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.adapter.ViewPagerAdapter;
import ruanjianbei.sport.mysport.zidingyi_view.ZoomImageView;

public class ImageDetailsActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ZoomImageView zoomImageView;
    /**
     * 用于管理图片的滑动
     */
    private ViewPager viewPager;
    private List<String> paths = new ArrayList<String>();
    int imagePosition;
    private ViewPagerAdapter adapter;
    private Boolean isNo = false;

    /**
     * 显示当前图片的页数
     */
    private TextView pageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_details);
        Bundle bundle = getIntent().getBundleExtra("bu");
        imagePosition = bundle.getInt("image_position", 1);
        paths = bundle.getStringArrayList("imags");
        adapter = new ViewPagerAdapter(paths, ImageDetailsActivity.this);
        pageText = (TextView) findViewById(R.id.page_text);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(imagePosition);
        viewPager.addOnPageChangeListener(ImageDetailsActivity.this);
        // 设定当前的页数和总页数
        pageText.setText((imagePosition + 1) + "/" + paths.size());
        pageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        Toast.makeText(this, paths.get(imagePosition).get("name"),
//                Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int currentPage) {
        // 每当页数发生改变时重新设定一遍当前的页数和总页数
            pageText.setText((currentPage + 1) + "/" + paths.size());

//            Toast.makeText(this, paths.get(currentPage).get("name"),
//                    Toast.LENGTH_SHORT).show();
    }

}
