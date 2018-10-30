package ruanjianbei.sport.mysport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.adapter.TitleFragmentPagerAdapter;
import ruanjianbei.sport.mysport.fragment.GerenxinxiFragment;
import ruanjianbei.sport.mysport.fragment.RcPaimingFragment;
import ruanjianbei.sport.mysport.fragment.WodefabuFragment;
import ruanjianbei.sport.mysport.fragment.WodezhanduiFragment;
import ruanjianbei.sport.mysport.util.MyApplication;

import android.support.v4.view.ViewPager;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PaimingActivity extends AppCompatActivity implements View.OnClickListener {

    private TabLayout paimingTablayout;
    private TextView paimingTv, mingmin;
    private ImageView fanhui;
    private CircleImageView paimingTouxiang;
    private TextView paimingName;
    private TextView paimingGongli, paiming_mingmin;
    private ViewPager paimingViewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setContentView(R.layout.paiming);
        paimingTablayout = (TabLayout) findViewById(R.id.paiming_tablayout);
        paimingTv = (TextView) findViewById(R.id.paiming_tv);
        paimingTouxiang = (CircleImageView) findViewById(R.id.paiming_touxiang);
        paimingName = (TextView) findViewById(R.id.paiming_name);
        paimingGongli = (TextView) findViewById(R.id.paiming_gongli);
        paimingViewpager = (ViewPager) findViewById(R.id.paiming_viewpager);
        mingmin = findViewById(R.id.paiming_mingmin);
        fanhui = findViewById(R.id.paiming_fanhui);

        fanhui.setOnClickListener(this);
        mingmin.setOnClickListener(this);
        paimingName.setText(MyApplication.user.getVname());
        paimingGongli.setText(String.valueOf(MyApplication.user.getLicheng()));
        Picasso.with(this)
                .load(MyApplication.imageUri + MyApplication.user.getTouxiang())
                .into(paimingTouxiang);

        RcPaimingFragment rcPaimingFragment = new RcPaimingFragment();
        RcPaimingFragment rcPaimingFragment1 = new RcPaimingFragment();
        RcPaimingFragment rcPaimingFragment2 = new RcPaimingFragment();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(rcPaimingFragment);
        fragments.add(rcPaimingFragment1);
        fragments.add(rcPaimingFragment2);
        TitleFragmentPagerAdapter adapter1 = new TitleFragmentPagerAdapter(getSupportFragmentManager(), fragments, new String[]{"周榜",  "月榜", "年榜"});
        paimingViewpager.setAdapter(adapter1);
        paimingTablayout.setupWithViewPager(paimingViewpager);

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.paiming_fanhui:
                finish();
                break;
            case R.id.paiming_mingmin:
                startActivity(new Intent(PaimingActivity.this, JiazhuwumingminActivity.class));
                break;
        }
    }
}
