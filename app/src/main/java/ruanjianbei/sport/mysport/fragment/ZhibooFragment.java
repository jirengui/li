package ruanjianbei.sport.mysport.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.jwenfeng.library.pulltorefresh.ViewStatus;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.activity.GuankanActivity;
import ruanjianbei.sport.mysport.activity.MainActivity;
import ruanjianbei.sport.mysport.activity.ZhiboActivity;
import ruanjianbei.sport.mysport.adapter.RecyclerViewAdapter;
import ruanjianbei.sport.mysport.bean.SaiShiBean;
import ruanjianbei.sport.mysport.bean.Shequ_DongtaiBean;
import ruanjianbei.sport.mysport.bean.ZhiBoBean;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.MyApplication;


public class ZhibooFragment extends Fragment implements OnBannerListener{

    private String mBaseUrl1 = "http://"+ MyApplication.ip+"/image/touxiang/zhang.jpg" ;
    private String mBaseUrl2= "http://"+ MyApplication.ip+"/image/touxiang/wangaodi.jpg" ;
    private String mBaseUrl3 = "http://"+ MyApplication.ip+"/image/touxiang/luchunyan.jpg" ;
    private String mBaseUrl4 = "http://"+ MyApplication.ip+"/image/touxiang/shijianguo.jpg" ;
    private String mBaseUrl5 = "http://"+ MyApplication.ip+"/image/touxiang/20180529124248.jpg" ;
    private Banner banner;//轮播台
    private ArrayList<String> list_path;
    private ArrayList<String> list_title;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private PullToRefreshLayout pullToRefreshLayout;
    private List<ZhiBoBean> list;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 13:
                    adapter.replaceAll(list);
                    // 结束刷新
                    pullToRefreshLayout.finishRefresh();
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.zhiboo, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iniv(view);
    }
    private void iniv(View view){
        banner = (Banner) view.findViewById(R.id.banner);
        pullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.pu_zhibo);
        //放图片地址的集合
        list_path = new ArrayList<>();
        //放标题的集合
        list_title = new ArrayList<>();
        list_path.add(mBaseUrl1);
        list_title.add("a");
        list_path.add(mBaseUrl2);
        list_title.add("b");
        list_path.add(mBaseUrl3);
        list_title.add("c");
        list_path.add(mBaseUrl4);
        list_title.add("d");
        list_path.add(mBaseUrl5);
        list_title.add("e");
        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new MyLoader());
        //设置图片网址或地址的集合
        banner.setImages(list_path);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
        banner.setBannerTitles(list_title);
        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER)
                //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                .setOnBannerListener(this)
                //必须最后调用的方法，启动轮播图。
                .start();

        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                startActivity(new Intent(getContext(), GuankanActivity.class));
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
//        recyclerView.setNestedScrollingEnabled(false);
//        recyclerView.setHasFixedSize(true);
//        pullToRefreshLayout.setNestedScrollingEnabled(false);
        list = new ArrayList<>();
        pullToRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtils.post(MyApplication.getZhiBourl, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseBody = response.body().string();
                                System.out.println("直播信息：" + responseBody + "萨达：" + response.toString());
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<ZhiBoBean>>() {
                                }.getType();
                                list = gson.fromJson(responseBody, type);
                                if (!list.isEmpty()) {
                                    System.out.println("赛事List的数据：" + list.get(0).getBiaoti());
                                }
                                Message msg = handler.obtainMessage();
                                msg.what = 13;
                                msg.obj = list; //把登录结果也发送过去
                                handler.sendMessage(msg);
                            }
                        },null);
                    }
                }).start();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        // 结束刷新
//                        pullToRefreshLayout.finishRefresh();
//                    }
//                }, 2000);
            }

            @Override
            public void loadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 结束加载更多
                        pullToRefreshLayout.finishLoadMore();
                    }
                }, 2000);
            }
        });

        adapter = new RecyclerViewAdapter(list);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        pullToRefreshLayout.autoRefresh();
        adapter.setItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int o) {
                startActivity(new Intent(getContext(), GuankanActivity.class));
            }
        });

    }

    @Override
    public void OnBannerClick(int position) {

    }
    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }



    //自定义的图片加载器
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Picasso.with(context)
                    .load((String) path)
                    .placeholder(context.getResources().getDrawable(R.drawable.wanou))
                    .centerCrop()
                    .fit()
                    .into(imageView);
        }
    }

}
