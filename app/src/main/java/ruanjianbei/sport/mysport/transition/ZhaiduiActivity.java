package ruanjianbei.sport.mysport.transition;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.activity.ChuangjianzhanduiActivity;
import ruanjianbei.sport.mysport.activity.ZhanduijianjieActivity;
import ruanjianbei.sport.mysport.activity.ZhiboActivity;
import ruanjianbei.sport.mysport.bean.TeamInfoBean;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.MyApplication;


public class ZhaiduiActivity extends Fragment {

    private TextView indicatorTv;
    private View positionView;
    private Button button;
    private ViewPager viewPager;
    private List<CommonFragment> fragments = new ArrayList<>(); // 供ViewPager使用
    private List<TeamInfoBean> list = new ArrayList<>();
    private View view1;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 121:
                    // 3. 填充ViewPager
                    fillViewPager(view1);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_mainaa, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view1 = view;

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("进入战队");
                HttpUtils.post(MyApplication.getTeam, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String s = response.body().string();
                        System.out.println("战队列表：" + s);
                        Gson gson = new Gson();
                        list = gson.fromJson(s,new TypeToken<List<TeamInfoBean>>(){}.getType());
                        Message message = handler.obtainMessage();
                        message.what = 121;
                        handler.sendMessage(message);
                    }
                },null);
            }
        }).start();
        positionView = view.findViewById(R.id.position_view);
        button = view.findViewById(R.id.zhandui_chuangjianzhandui);

        if (MyApplication.user.getTeam() != null){
            button.setText("管理战队");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), ZhanduijianjieActivity.class));
                }
            });
        }else {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), ChuangjianzhanduiActivity.class));
                }
            });
        }

    }


    /**
     * 填充ViewPager
     */
    private void fillViewPager(View view) {
        indicatorTv = (TextView) view.findViewById(R.id.indicator_tv);
        viewPager = (ViewPager) view.findViewById(R.id.viewpagera);

        // 1. viewPager添加parallax效果，使用PageTransformer就足够了
        viewPager.setPageTransformer(false, new CustPagerTransformer(getContext()));

        // 2. viewPager添加adapter
        for (int i = 0; i < list.size(); i++) {
            // 预先准备10个fragment
            fragments.add(new CommonFragment());
        }

        viewPager.setAdapter(new FragmentStatePagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                CommonFragment fragment = fragments.get(position);
                fragment.bindData(list.get(position));
                return fragment;
            }

            @Override
            public int getCount() {
                return list.size();
            }
        });


        // 3. viewPager滑动时，调整指示器
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                updateIndicatorTv();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                final Picasso picasso = Picasso.with(getContext());
                if (state == 1) {
                    picasso.pauseTag("PhotoTag");
                } else {
                    picasso.resumeTag("PhotoTag");
                }


            }
        });

        updateIndicatorTv();
    }

    /**
     * 更新指示器
     */
    private void updateIndicatorTv() {
        int totalNum = viewPager.getAdapter().getCount();
        int currentItem = viewPager.getCurrentItem() + 1;
        indicatorTv.setText(Html.fromHtml("<font color='#12edf0'>" + currentItem + "</font>  /  " + totalNum));
    }
}
