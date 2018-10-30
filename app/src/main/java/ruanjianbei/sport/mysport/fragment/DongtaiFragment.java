package ruanjianbei.sport.mysport.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.activity.ChuangjiansaishiActivity;
import ruanjianbei.sport.mysport.activity.FabiaodongtaiActivity;
import ruanjianbei.sport.mysport.activity.HaoyouliebiaoActivity;
import ruanjianbei.sport.mysport.activity.JifenzhongxinActivity;
import ruanjianbei.sport.mysport.activity.ZhiboActivity;
import ruanjianbei.sport.mysport.adapter.TitleFragmentPagerAdapter;
import ruanjianbei.sport.mysport.bean.MessageEvent;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.Listener;
import ruanjianbei.sport.mysport.util.MyApplication;
import ruanjianbei.sport.mysport.util.Xiaoxilei;

import static android.content.Context.MODE_PRIVATE;


public class DongtaiFragment extends Fragment {


    private TabLayout tabLayout;
    private ViewPager viewpager;
    private FloatingActionButton floatingActionButton;
    private CircleImageView circleImageView;
    private TextView tv_gerenxingming, tv_gerendengji, tv_gerenjifen;
    private Button qiandao_bt;
    private ImageView wodexiaoxi;
    private HashMap<String, String> map = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.dongtai, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        circleImageView = view.findViewById(R.id.shequ_gerentouxiang);
        tv_gerendengji = view.findViewById(R.id.shequ_gerendengji);
        tv_gerenxingming = view.findViewById(R.id.shequ_gerenxingming);
        tv_gerenjifen = view.findViewById(R.id.shequ_tvjifen);
        qiandao_bt = view.findViewById(R.id.shequ_qiandao);
        wodexiaoxi = view.findViewById(R.id.wodexiaoxi);
        setliaotian();

        wodexiaoxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("userId", String.valueOf(MyApplication.user.getId()));
                intent.putExtra("shuju", map);
                intent.setClass(getContext(), HaoyouliebiaoActivity.class);
                startActivity(intent);
            }
        });
        if (MyApplication.qiandaozhuangtai == -1) {
            qiandao_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("name", MODE_PRIVATE).edit();
                    editor.putInt(MyApplication.user.getId() + "qiandaozhuangtai", 1);
                    editor.commit();
                    MyApplication.qiandaozhuangtai = 1;
                    int jifen = 0;
                    if (MyApplication.user.getJifen() != null) {
                        jifen = Integer.parseInt(MyApplication.user.getJifen());
                        jifen += 50;
                    }
                    MyApplication.user.setJifen(String.valueOf(jifen));
                    tv_gerenjifen.setText(MyApplication.user.getJifen());
                    qiandao_bt.setText("已签到");
                    updatajifen("50");

                }
            });
        } else {
            qiandao_bt.setText("已签到");
        }
        if (MyApplication.user != null) {
            Picasso.with(getContext())
                    .load(MyApplication.imageUri + MyApplication.user.getTouxiang())
                    .into(circleImageView);
            tv_gerenxingming.setText(MyApplication.user.getVname());
            tv_gerenjifen.setText(String.valueOf(MyApplication.user.getJifen()));
        }
        tv_gerenjifen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), JifenzhongxinActivity.class));
            }
        });
        tabLayout = view.findViewById(R.id.tablayout);
        viewpager = view.findViewById(R.id.viewpager);

        floatingActionButton = view.findViewById(R.id.pink_icon);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewpager.getCurrentItem() == 3) {
                    startActivity(new Intent(getContext(), ChuangjiansaishiActivity.class));
                } else if (viewpager.getCurrentItem() == 2) {
                    startActivity(new Intent(getContext(), ZhiboActivity.class));
                } else {
                    startActivity(new Intent(getContext(), FabiaodongtaiActivity.class));
                }
            }
        });


        FragmentDongtaiFragment fragmentDongtaiFragment = new FragmentDongtaiFragment();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(fragmentDongtaiFragment);
        FragmentGuanZhuDongtai guanZhuDongtai = new FragmentGuanZhuDongtai();
        fragments.add(guanZhuDongtai);
        ZhibooFragment zhiBoFragment = new ZhibooFragment();
        fragments.add(zhiBoFragment);
        SaishiFragment fragmentDongtaiFragment3 = new SaishiFragment();
        fragments.add(fragmentDongtaiFragment3);
        TitleFragmentPagerAdapter adapter1 = new TitleFragmentPagerAdapter(getActivity().getSupportFragmentManager(), fragments, new String[]{"动态", "关注", "直播", "赛事"});
        viewpager.setAdapter(adapter1);
        tabLayout.setupWithViewPager(viewpager);

    }

    private void setliaotian() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("ws://" + MyApplication.ip + "/w/websocket/{" + String.valueOf(MyApplication.user.getId()) + "}")
                .build();
        Xiaoxilei.webSocket = okHttpClient.newWebSocket(request, new Listener());
        okHttpClient.dispatcher().executorService().shutdown();
    }

    //接收消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        System.out.println("社区界面获取的消息为： " + messageEvent.getMessage());
        Gson gson = new Gson();
        map = gson.fromJson(messageEvent.getMessage(), HashMap.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (tv_gerenjifen != null) {
            tv_gerenjifen.setText(MyApplication.user.getJifen());
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void updatajifen(String jifen) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", String.valueOf(MyApplication.user.getId()));
        map.put("count", jifen);
        map.put("shijian", "签到");
        HttpUtils.post(MyApplication.updatejifenurl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("签到：" + response.body().string());
            }
        }, map);
    }
}
