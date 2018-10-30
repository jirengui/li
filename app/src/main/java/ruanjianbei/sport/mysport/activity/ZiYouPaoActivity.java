package ruanjianbei.sport.mysport.activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.githang.statusbar.StatusBarCompat;
import com.iflytek.cloud.SpeechSynthesizer;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.fragment.ExampleDialogFragment;
import ruanjianbei.sport.mysport.util.Douglas;
import ruanjianbei.sport.mysport.util.GradientHelper;
import ruanjianbei.sport.mysport.zidingyi_view.Guiji;

//, TraceStatusListener纠偏监听
public class ZiYouPaoActivity extends AppCompatActivity implements GeocodeSearch.OnGeocodeSearchListener, Guiji.OnTrailChangeListener {
    private MapView mMapView = null;
    private AMap aMap;
    private Boolean is = false, iskaishi = false;
    private Button kaishi, jieshu;
    private View view;
    private LatLng oldLatLng;
    private LatLng latLng;
    private GeocodeSearch geocoderSearch;
    private ArrayList<LatLng> mLineInit = new ArrayList<LatLng>();
    private ArrayList<LatLng> guiji = new ArrayList<LatLng>();
    private TextView tv_licheng, tv_shijian;
    private Intent alarmIntent = null;
    private PendingIntent alarmPi = null;
    private AlarmManager alarm = null;
    private Guiji guijia;
    private List<Point> list = new ArrayList<>();
    private String moshi;
    final int[] miao = {0, 0};
    final int[] fen = {0, 0};
    final int[] shi = {0, 0};
    private float a = 0;

    private String s3[] = new String[1];//配速
    private String s4[] = new String[1];//里程
    private String s5[] = new String[1];//时间
    private String s6[] = new String[1];//开始时间
    private Timer timer;
    private ExampleDialogFragment e = new ExampleDialogFragment();
    private String lujing;
    private IntentFilter filter = null;


    //实例化渐变帮助类
    private GradientHelper mGradientHelper = new GradientHelper(50, Color.parseColor("#fffacd"), Color.parseColor("#ff4500"));
    //    private LBSTraceClient lbsTraceClient;
    private SpeechSynthesizer mTts;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;

    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {

            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {//获取经纬度
                    LatLng newLatLng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                    System.out.println("定位： " + newLatLng);
                    if (!is) {
                        //记录第一次的定位信息
                        oldLatLng = newLatLng;
                        is = true;
                        //移动视角
                        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()), 17, 0, 0)));
                    }
                    if (iskaishi && oldLatLng != newLatLng && AMapUtils.calculateLineDistance(oldLatLng, newLatLng) <= 30) {
                        //位置有变化
                        System.out.println("进入位置监控......");
                        Log.e("Amap", amapLocation.getLatitude() + "," + amapLocation.getLongitude());
                        if (AMapUtils.calculateLineDistance(oldLatLng, newLatLng) <= 20) {
                            setUpMap(oldLatLng, newLatLng);
                            mLineInit.add(newLatLng);
                            a += AMapUtils.calculateLineDistance(oldLatLng, newLatLng);
                            tv_licheng.setText(String.valueOf(Math.round(a)) + "米");
                        }
                        oldLatLng = newLatLng;
                    } else {
                        oldLatLng = newLatLng;
                    }
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };

    private void startAnim() {
        if (!mLineInit.isEmpty()) {
            Douglas douglas = new Douglas(mLineInit, 0.5);
            guiji = douglas.compress();
        }
        if (!guiji.isEmpty()) {
            for (int i = 0; i < guiji.size(); i++) {
                list.add(aMap.getProjection().toScreenLocation(guiji.get(i)));
            }
            System.out.println("轨迹：" + list.get(0));
        }
        guijia.drawSportLine(list, R.drawable.qidian, R.drawable.xiaoliangqiu, this);

        // or avi.smoothToShow();
    }


    private static final String NOTIFICATION_CHANNEL_NAME = "BackgroundLocation";
    private NotificationManager notificationManager = null;
    boolean isCreateChannel = false;

    @SuppressLint("NewApi")
    private Notification buildNotification() {

        Notification.Builder builder = null;
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= 26) {
            //Android O上对Notification进行了修改，如果设置的targetSDKVersion>=26建议使用此种方式创建通知栏
            if (null == notificationManager) {
                notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            }
            String channelId = getPackageName();
            if (!isCreateChannel) {
                NotificationChannel notificationChannel = new NotificationChannel(channelId,
                        NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.enableLights(true);//是否在桌面icon右上角展示小圆点
                notificationChannel.setLightColor(Color.BLUE); //小圆点颜色
                notificationChannel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
                notificationManager.createNotificationChannel(notificationChannel);
                isCreateChannel = true;
            }
            builder = new Notification.Builder(getApplicationContext(), channelId);
        } else {
            builder = new Notification.Builder(getApplicationContext());
        }
        builder.setSmallIcon(android.R.drawable.btn_default)
                .setContentTitle("运动")
                .setContentText("正在后台运行")
                .setWhen(System.currentTimeMillis());

        if (Build.VERSION.SDK_INT >= 16) {
            notification = builder.build();

        } else {
            return builder.getNotification();
        }

        return notification;
    }

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        StatusBarCompat.setLightStatusBar(getWindow(), true);
        setContentView(R.layout.ziyoupao);


        // 创建Intent对象，action为LOCATION
        alarmIntent = new Intent();
        alarmIntent.setAction("LOCATION");
        IntentFilter ift = new IntentFilter();


        // 定义一个PendingIntent对象，PendingIntent.getBroadcast包含了sendBroadcast的动作。
        // 也就是发送了action 为"LOCATION"的intent
        alarmPi = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        // AlarmManager对象,注意这里并不是new一个对象，Alarmmanager为系统级服务
        alarm = (AlarmManager) getSystemService(ALARM_SERVICE);


        //动态注册一个广播
        filter = new IntentFilter();
        filter.addAction("LOCATION");
        registerReceiver(alarmReceiver, filter);

//        tv_kaluli = findViewById(R.id.tv_kaluli);
        tv_licheng = findViewById(R.id.ziyoupao_licheng);
        tv_shijian = findViewById(R.id.ziyoupao_shijian);
        guijia = findViewById(R.id.ziyoupao_ditu);
        Intent i = getIntent();
        moshi = i.getStringExtra("moshi");

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.ziyoupao_map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);


        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        mLocationClient.enableBackgroundLocation(2001, buildNotification());

        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
        myLocationStyle.strokeColor(0x000000);//设置定位蓝点精度圆圈的边框颜色的方法。
        myLocationStyle.radiusFillColor(0x000000);//设置定位蓝点精度圆圈的填充颜色的方法。
        //设置为高精度定位模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        myLocationStyle.interval(1000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
//设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(true);
        //3S内最精准的一个
        mLocationOption.setOnceLocationLatest(true);
//关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
//启动定位
        mLocationClient.startLocation();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.showIndoorMap(true);//显示室内地图
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。


        //使用 aMap.setMapTextZIndex(2) 可以将地图底图文字设置在添加的覆盖物之上
        aMap.setMapTextZIndex(2);
        aMap.showIndoorMap(true);
        kaishi = findViewById(R.id.ziyoupao_kaishipaobu);
        jieshu = findViewById(R.id.ziyoupao_jieshupaobu);

        kaishi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miao[0] = 0;
                miao[1] = 0;
                fen[0] = 0;
                fen[1] = 0;
                shi[0] = 0;
                shi[1] = 0;
                 a = 0;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
//获取当前时间
                Date date = new Date(System.currentTimeMillis());
                s6[0] = simpleDateFormat.format(date);
                iskaishi = true;
                mLocationClient.startLocation();
                kaishi.setVisibility(View.GONE);
                jieshu.setVisibility(View.VISIBLE);
                Timer1();
                if (null != alarm) {
                    //设置一个闹钟，2秒之后每隔一段时间执行启动一次定位程序
                    System.out.println("锁屏唤醒。");
                    alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000,
                            1000, alarmPi);
                }

            }
        });
        jieshu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnim();
            }
        });

    }

    /**
     * 绘制两个坐标点之间的线段,从以前位置到现在位置
     */
    private void setUpMap(LatLng oldData, LatLng newData) {

        // 绘制一个大地曲线
        aMap.addPolyline((new PolylineOptions())
                .add(oldData, newData)
                .geodesic(true).color(mGradientHelper.getGradient()));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
        mLocationClient.disableBackgroundLocation(true);
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
        if (null != alarm) {
            //停止定位的时候取消闹钟
            alarm.cancel(alarmPi);
        }
        if (null != alarmReceiver) {
            unregisterReceiver(alarmReceiver);
        }
        mMapView.onDestroy();
        System.out.println("结束地图");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.deleteNotificationChannel(getPackageName());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    //搜索距离回调
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    public void showToast(String name) {
        Toast.makeText(ZiYouPaoActivity.this, name, Toast.LENGTH_SHORT).show();
    }

    private void Timer1() {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                miao[0]++;
                if (miao[0] == 10) {
                    miao[1]++;
                    miao[0] = 0;
                }
                if (fen[0] == 10) {
                    fen[1]++;
                    fen[0] = 0;
                }
                if (shi[0] == 10) {
                    shi[1]++;
                    shi[0] = 0;
                }
                if (miao[1] == 6) {
                    fen[0]++;
                    miao[0] = 0;
                    miao[1] = 0;
                }
                if (fen[1] == 6) {
                    shi[0]++;
                    fen[0] = 0;
                    fen[1] = 0;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_shijian.setText(String.format(getResources().getString(R.string.current_time), shi[1], shi[0], fen[1], fen[0], miao[1], miao[0]));
                    }
                });
            }
        };
        timer.schedule(task, 1000, 1000);
    }

    private BroadcastReceiver alarmReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("LOCATION")) {
                if (null != mLocationClient) {
                    System.out.println("开始唤醒了。。。");
                    mLocationClient.startLocation();
                }
            }
        }
    };

    @Override
    public void onFinish() {
        mLocationClient.stopLocation();

        //轨迹绘制完成返回
        guijia.setVisibility(View.GONE);
        // 绘制一个大地曲线
        aMap.addPolyline((new PolylineOptions())
                .addAll(guiji)
                .width(20)
                .geodesic(true).color(Color.parseColor("#22c1c3")));
        Marker marker = aMap.addMarker(new MarkerOptions().position(guiji.get(0)));
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.drawable.qidian)));
        Marker marker1 = aMap.
                addMarker(new MarkerOptions().position(guiji.get(guiji.size() - 1)));
        marker1.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.drawable.zhongdian)));
        jieshu.setVisibility(View.GONE);
        kaishi.setVisibility(View.VISIBLE);
        mLineInit = new ArrayList<LatLng>();
        guiji = new ArrayList<LatLng>();
        is = false;
        iskaishi = false;

    }

}