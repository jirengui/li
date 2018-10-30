package ruanjianbei.sport.mysport.activity;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.githang.statusbar.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.adapter.TitleFragmentPagerAdapter;
import ruanjianbei.sport.mysport.fragment.GerenxinxiFragment;
import ruanjianbei.sport.mysport.fragment.JifenduihuanJinpinFragment;
import ruanjianbei.sport.mysport.fragment.JifenduihuanLiwuFragment;
import ruanjianbei.sport.mysport.fragment.JifenduihuanQuanbuFragment;
import ruanjianbei.sport.mysport.fragment.WodefabuFragment;
import ruanjianbei.sport.mysport.fragment.WodezhanduiFragment;

public class JifenduihuanActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView jifenduihuanFanhui;
    private TabLayout headViewpage;
    private ViewPager gerenViewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        //false为白色字体，ture为黑色字体
        StatusBarCompat.setLightStatusBar(getWindow(), true);
        setContentView(R.layout.jifenduihuan);
        jifenduihuanFanhui = (ImageView) findViewById(R.id.jifenduihuan_fanhui);
        headViewpage = (TabLayout) findViewById(R.id.head_jifenviewpage);
        gerenViewpager = (ViewPager) findViewById(R.id.jifen_viewpager);
        jifenduihuanFanhui.setOnClickListener(this);
        JifenduihuanJinpinFragment jifenduihuanJinpinFragment = new JifenduihuanJinpinFragment();
        JifenduihuanLiwuFragment jifenduihuanLiwuFragment = new JifenduihuanLiwuFragment();
        JifenduihuanQuanbuFragment jifenduihuanQuanbuFragment = new JifenduihuanQuanbuFragment();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(jifenduihuanJinpinFragment);
        fragments.add(jifenduihuanQuanbuFragment);
        fragments.add(jifenduihuanLiwuFragment);
        TitleFragmentPagerAdapter adapter1 = new TitleFragmentPagerAdapter(getSupportFragmentManager(), fragments, new String[]{"精品",  "商品", "礼物"});
        gerenViewpager.setAdapter(adapter1);
        headViewpage.setupWithViewPager(gerenViewpager);

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.jifenduihuan_fanhui:
                finish();
                break;
        }
    }
}
