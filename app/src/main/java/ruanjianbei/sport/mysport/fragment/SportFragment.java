package ruanjianbei.sport.mysport.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import pub.devrel.easypermissions.easyPermission.EasyPermission;
import ruanjianbei.sport.mysport.activity.DituActivity;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.activity.DongtaipinglunActivity;
import ruanjianbei.sport.mysport.activity.DuanlianjihuaActivity;
import ruanjianbei.sport.mysport.activity.ShiyanGuijiActivity;
import ruanjianbei.sport.mysport.activity.TianqiActivity;
import ruanjianbei.sport.mysport.activity.ZiYouPaoActivity;
import ruanjianbei.sport.mysport.adapter.RcgonggaoAdapter;
import ruanjianbei.sport.mysport.adapter.RecyclerViewPinLunAdapter;
import ruanjianbei.sport.mysport.bean.GongGaoBean;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.MyApplication;
import ruanjianbei.sport.mysport.zidingyi_view.WaveView;


import static android.content.Context.TELEPHONY_SERVICE;

public class SportFragment extends Fragment implements View.OnClickListener, EasyPermission.PermissionCallback, WeatherSearch.OnWeatherSearchListener {

    private ImageView imPaobu;
    TextView tv_1, tv_2, tv_3;
    private PopupWindow window;
    private WaveView mWaveView;
    private TextView gonggao;
    private ImageView tianqi;
    private TextView wendu;
    //天气
    private WeatherSearchQuery mquery;
    private WeatherSearch mweathersearch;
    private LocalWeatherLive weatherlive = null;
    private RecyclerView recyclerView;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //定位信息，省市区
    private String diLiWeiZhi_sheng = null, diLiWeiZhi_shi = null, diLiWeiZhi_qu = null;


    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    diLiWeiZhi_sheng = aMapLocation.getProvince();//省信息
                    diLiWeiZhi_shi = aMapLocation.getCity();//城市信息
                    diLiWeiZhi_qu = aMapLocation.getDistrict();//城区信息

                    //检索参数为城市和天气类型，实况天气为WEATHER_TYPE_LIVE、天气预报为WEATHER_TYPE_FORECAST
                    System.out.println("城市：" + diLiWeiZhi_shi);
                    mquery = new WeatherSearchQuery(diLiWeiZhi_shi, WeatherSearchQuery.WEATHER_TYPE_LIVE);
                    mweathersearch = new WeatherSearch(getContext());
                    mweathersearch.setOnWeatherSearchListener(SportFragment.this);
                    mweathersearch.setQuery(mquery);
                    mweathersearch.searchWeatherAsyn(); //异步搜索
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 8:
                    try {
                        Toast.makeText(getContext(), "绑定成功！", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.putExtra("moshi", "1");
                        intent.setClass(getActivity(), DituActivity.class);
                        startActivity(intent);
                        window.dismiss();
                        mWaveView.setVisibility(View.VISIBLE);
                        mWaveView.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 9:
                    String str = (String) msg.obj;
                    Gson gson = new Gson();
                    List<GongGaoBean> list = gson.fromJson(str, new TypeToken<List<GongGaoBean>>() {
                    }.getType());
                    RcgonggaoAdapter adapter = new RcgonggaoAdapter(list, getContext());
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(adapter);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.sport, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

             /*
            申请权限,使用Build模式更清晰
        */
        EasyPermission.with(this)
                .addRequestCode(100)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.RECORD_AUDIO)
                .request();
        ;
        imPaobu = (ImageView) view.findViewById(R.id.im_paobu);
        imPaobu.setOnClickListener(this);
        gonggao = (TextView) view.findViewById(R.id.xiaoyuangonggao);
        gonggao.setOnClickListener(this);

//        imPaobu.setVisibility(View.GONE);
        mWaveView = (WaveView) view.findViewById(R.id.wv);
        mWaveView.setStyle(Paint.Style.STROKE);
        mWaveView.setInitialRadius(200);
        mWaveView.setColor(Color.parseColor("#FF3dceba"));
        mWaveView.setInterpolator(new AccelerateInterpolator(1.0f));
        mWaveView.start();

        //初始化定位
        mLocationClient = new AMapLocationClient(getContext());
//设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

        tianqi = view.findViewById(R.id.tianqi);
        wendu = view.findViewById(R.id.wendu);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_paobu: // 跑步
                mWaveView.setVisibility(View.GONE);
                mWaveView.stop();
                showPopwindow();
//                startActivity(new Intent(getActivity(), DituActivity.class));
                break;
            case R.id.xiaoyuangonggao://公告
                showPopwindow2();
                break;
            case R.id.wendu:
            case R.id.tianqi:
                if (weatherlive != null) {
                    Intent intent = new Intent(getContext(), TianqiActivity.class);
                    intent.putExtra("shijian", weatherlive.getReportTime());
                    intent.putExtra("chengshi", weatherlive.getCity());
                    intent.putExtra("tianqi", weatherlive.getWeather());
                    intent.putExtra("wendu", weatherlive.getTemperature());
                    intent.putExtra("fengxiang", weatherlive.getWindDirection());
                    intent.putExtra("fengli",weatherlive.getWindPower());
                    intent.putExtra("shidu",weatherlive.getHumidity());
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    private String getbiaoshifu() {
//        MAC地址
        WifiManager wm = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String MAC = wm.getConnectionInfo().getMacAddress();
//        IMEI:
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(TELEPHONY_SERVICE); //获取IMEI
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        String IMEI = telephonyManager.getDeviceId();
        System.out.println("MAC: " + MAC + "IMEI: " + IMEI);
        return MAC + IMEI;
    }

    /**
     * 显示popupWindow
     */
    private void showPopwindow() {
        // 利用layoutInflater获得View
        final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop, null);
        RelativeLayout relativeLayout = view.findViewById(R.id.pop_rc);

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        window = new PopupWindow(view,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);
        tv_1 = (TextView) view.findViewById(R.id.tv1);
        tv_2 = (TextView) view.findViewById(R.id.tv2);
        tv_3 = (TextView) view.findViewById(R.id.tv3);
        tv_1.setText("跑步");
        tv_2.setText("自由跑");
        tv_3.setText("训练");
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        window.setBackgroundDrawable(dw);
        //         设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.PopupAnimation);
        backgroundAlpha((float) 0.9);
//         在底部显示
        window.showAtLocation(getActivity().findViewById(R.id.rc_sport),
                Gravity.CENTER, 0, -300);
//        window.showAsDropDown(getActivity().findViewById(layout_id));
        //popWindow消失监听方法
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                backgroundAlpha((float) 1.0);
                mWaveView.setVisibility(View.VISIBLE);
                mWaveView.start();
            }
        });
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        tv_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.user == null) {
                    Toast.makeText(getContext(), "请先登录在随即跑。", Toast.LENGTH_LONG).show();
                } else {
                    System.out.println("设备： " + MyApplication.user.getShebei());
                    if (MyApplication.user.getShebei() == null) {
                        android.app.AlertDialog dialog;
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                        builder.setTitle("请进行设备绑定。");
                        builder.setMessage("绑定设备后你将只能在此设备上进行随即跑。不绑定则无法开始跑步");
                        builder.setCancelable(false);
                        builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Map<String, String> map = new HashMap<>();
                                map.put("id", String.valueOf(MyApplication.user.getId()));
                                map.put("shebei", getbiaoshifu());
                                HttpUtils.post(MyApplication.updateshebeiurl, new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        Message msg = handler.obtainMessage();
                                        msg.what = 8;
                                        handler.sendMessage(msg);
                                    }
                                }, map);

                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dialog = builder.create();
                        dialog.show();
                    } else if (Objects.equals(getbiaoshifu(), MyApplication.user.getShebei())) {
                        Intent intent = new Intent();
                        intent.putExtra("moshi", "1");
                        intent.setClass(getActivity(), DituActivity.class);
                        startActivity(intent);
                        mWaveView.setVisibility(View.VISIBLE);
                        mWaveView.start();
                        window.dismiss();
                    } else {
                        Toast.makeText(getContext(), "不是绑定设备，无法开始随机跑。", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        tv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("moshi", "2");
                intent.setClass(getActivity(), ZiYouPaoActivity.class);
                startActivity(intent);
                mWaveView.setVisibility(View.VISIBLE);
                mWaveView.start();
                window.dismiss();
            }
        });
        tv_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("moshi", 2);
                intent.setClass(getActivity(), DuanlianjihuaActivity.class);
                startActivity(intent);
//                Toast.makeText(getContext(), "敬请期待！", Toast.LENGTH_LONG).show();
                mWaveView.setVisibility(View.VISIBLE);
                mWaveView.start();
                window.dismiss();
            }
        });


    }

    /**
     * 显示popupWindow
     */
    private void showPopwindow2() {
        // 利用layoutInflater获得View
        final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.xiaoyuangonggao, null);
        recyclerView = view.findViewById(R.id.rc_gonggao);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtils.post(MyApplication.getgonggao, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String str = response.body().string();
                        System.out.println("公告：" + str);
                        Message msg = handler.obtainMessage();
                        msg.what = 9;
                        handler.sendMessage(msg);
                        msg.obj = str;
                    }
                }, null);
            }
        }).start();

//        List<GongGaoBean> list = new ArrayList<>();
//        list.add(new GongGaoBean(5, "学校", "恭喜几人归东城荣获一等奖","shijianguo.jpg","2018-06-25 11:32:50"));
//        list.add(new GongGaoBean(4, "体育部", "战队规则变更","shijianguo.jpg","2018-06-25 11:32:29"));
//        list.add(new GongGaoBean(3, "体育部", "今明下雨","shijianguo.jpg","2018-06-25 11:32:02"));
//        list.add(new GongGaoBean(2, "学校", "今晚8点体育赛跑","shijianguo.jpg","2018-06-25 11:31:59"));
//        list.add(new GongGaoBean(1, "体育部", "恭喜几人归东城荣获一等奖","shijianguo.jpg","2018-06-25 11:30:46"));
//        RcgonggaoAdapter adapter = new RcgonggaoAdapter(list, getContext());
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(adapter);

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        window = new PopupWindow(view,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        window.setBackgroundDrawable(dw);
        //         设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.PopupAnimation);
        backgroundAlpha((float) 0.9);
//         在底部显示
        window.showAtLocation(getActivity().findViewById(R.id.rc_sport),
                Gravity.CENTER, 0, -300);
//        window.showAsDropDown(getActivity().findViewById(layout_id));
        //popWindow消失监听方法
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                backgroundAlpha((float) 1.0);
            }
        });
//        relativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                window.dismiss();
//            }
//        });
    }

    @Override
    public void onEasyPermissionGranted(int requestCode, String... perms) {
         /*
            用户授予所有权限,继续你的业务逻辑
        */

    }

    @Override
    public void onEasyPermissionDenied(int requestCode, String... perms) {
 /*
            用户拒绝至少一个权限
        */
        Toast.makeText(getContext(), "大哥，不给权限没法用啊", Toast.LENGTH_SHORT).show();

        // 弹出对话框,让用户去设置界面授予权限.
        // 根据业务需求可以不添加此代码
        EasyPermission.checkDeniedPermissionsNeverAskAgain(this, "请大哥赋予我权限吧！", perms);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        /*
            传递到EasyPermission中
        */
        EasyPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
            从Settings界面返回
        */
        System.out.println("主页面requestCode返回：" + requestCode + "主页面resultCode返回：" + resultCode);
        if (requestCode == EasyPermission.SETTINGS_REQ_CODE) {
            //判断是否授予权限
            if (EasyPermission.hasPermissions(getContext(), Manifest.permission.READ_SMS)) {
                // 授予了权限,继续你的业务逻辑

            } else {
                // 还是没有权限,弹出提示信息
                Toast.makeText(getContext(), "大哥，没这些权限真不能用啊！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //天气返回
    @Override
    public void onWeatherLiveSearched(LocalWeatherLiveResult localWeatherLiveResult, int i) {
        if (i == 1000) {
            if (localWeatherLiveResult != null && localWeatherLiveResult.getLiveResult() != null) {
                weatherlive = localWeatherLiveResult.getLiveResult();
                if (weatherlive.getWeather().equals("多云")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_duoyun);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("阴")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_yin);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("阵雨")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_zhengyu);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("雷阵雨")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_leizhengyu);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("晴")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_qing);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("雷阵雨并伴有冰雹")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_leizhengyubingbao);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("雨夹雪")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_yujiaxue);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("小雨")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_xiaoyu);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("中雨")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_zhongyu);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("大雨")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_dayu);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("暴雨")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_baoyu);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("大暴雨")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_dabaoyu);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("特大暴雨")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_tedabaoyu);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("阵雪")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_zhenxue);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("小雪")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_xiaoxue);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("中雪")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_zhongxue);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("大雪")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_daxue);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("暴雪")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_baoxue);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("雾")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_wu);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("冻雨")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_dongyu);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("沙尘暴")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_shachenbao);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("小雨-中雨")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_xiaoyu);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("中雨-大雨")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_zhongyu);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("大雨-暴雨")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_dayu);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("暴雨-大暴雨")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_baoyu);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("大暴雨-特大暴雨")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_dabaoyu);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("小雪-中雪")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_xiaoxue);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("中雪-大雪")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_zhongxue);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("大雪-暴雪")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_daxue);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("浮尘")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_fuchen);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("扬沙")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_yangsha);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("强沙尘暴")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_qiangshachenbao);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("飑")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_bao);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("龙卷风")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_longjuanfen);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("弱高吹雪")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_ruogaochuixue);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("轻霾")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_mai);
                    tianqi.setImageDrawable(drawable);
                } else if (weatherlive.getWeather().equals("霾")) {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_zhongmai);
                    tianqi.setImageDrawable(drawable);
                } else {
                    Drawable drawable = getContext().getDrawable(R.drawable.tianqi_qing);
                    tianqi.setImageDrawable(drawable);
                }
                wendu.setText(weatherlive.getTemperature() + "°");
                System.out.println("天气：" + weatherlive.getWeather());
                MyApplication.tianqi = weatherlive.getWeather()+" "+ weatherlive.getTemperature() + "℃";
                tianqi.setOnClickListener(this);
                wendu.setOnClickListener(this);
//                reporttime1.setText(weatherlive.getReportTime()+"发布");
//                weather.setText(weatherlive.getWeather());
//                Temperature.setText(weatherlive.getTemperature()+"°");
//                wind.setText(weatherlive.getWindDirection()+"风     "+weatherlive.getWindPower()+"级");
//                humidity.setText("湿度         "+weatherlive.getHumidity()+"%");
            }
        }
    }

    @Override
    public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int i) {

    }
}
