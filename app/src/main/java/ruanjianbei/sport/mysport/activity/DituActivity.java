package ruanjianbei.sport.mysport.activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
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
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.wang.avi.AVLoadingIndicatorView;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.bean.PaoBuJieGuoBean;
import ruanjianbei.sport.mysport.bean.ZuobiaoBean;
import ruanjianbei.sport.mysport.fragment.ExampleDialogFragment;
import ruanjianbei.sport.mysport.util.Douglas;
import ruanjianbei.sport.mysport.util.FingerprintUtil;
import ruanjianbei.sport.mysport.util.GradientHelper;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.MyApplication;
import ruanjianbei.sport.mysport.zidingyi_view.Guiji;

//, TraceStatusListener纠偏监听
public class DituActivity extends AppCompatActivity implements GeocodeSearch.OnGeocodeSearchListener, Guiji.OnTrailChangeListener {
    private MapView mMapView = null;
    private AMap aMap;
    private Boolean is = false, iskaishi = false, iszhiwen = false,
            isjieshu = false, isjiazai = false, ishuizhi = false, isjieping = false;
    private Button kaishi, shuaxin, jieshu, zanting;
    private View view;
    private LatLng oldLatLng;
    private LatLng latLng;
    private GeocodeSearch geocoderSearch;
    private Marker bijin_marker;
    private List<Marker> tujindian = new ArrayList<Marker>();
    private ArrayList<LatLng> mLineInit = new ArrayList<LatLng>();
    private ArrayList<LatLng> guiji = new ArrayList<LatLng>();
    private TextView tv_licheng, tv_shijian;
    private float a = 0;
    private Intent alarmIntent = null;
    private PendingIntent alarmPi = null;
    private AlarmManager alarm = null;
    private ArrayList<String> tupianlist = new ArrayList<>();
    private ArrayList<String> tupianlist1 = new ArrayList<>();
    private ArrayList<String> paizhaodidian = new ArrayList<String>();
    private IntentFilter filter = null;
    private AVLoadingIndicatorView avi;
    private Guiji guijia;
    private List<Point> list = new ArrayList<>();
    private byte[] bitmapByte;
    private String moshi;
    final int[] miao = {0, 0};
    final int[] fen = {0, 0};
    final int[] shi = {0, 0};
    private String s3[] = new String[1];//配速
    private String s4[] = new String[1];//里程
    private String s5[] = new String[1];//时间
    private String s6[] = new String[1];//开始时间
    private  Timer timer;
    private ExampleDialogFragment e = new ExampleDialogFragment();
    private String lujing;
    private ZuobiaoBean zuobiaoBean;

    private static final String SD_PATH = "/sdcard/dskqxt/pic/";
    private static final String IN_PATH = "/dskqxt/pic/";

    /**
     * 随机生产文件名
     *
     * @return
     */
    private static String generateFileName() {
        return UUID.randomUUID().toString();
    }
    /**
     * 保存bitmap到本地
     *
     * @param context
     * @param mBitmap
     * @return
     */
    public static String saveBitmap(Context context, Bitmap mBitmap) {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = SD_PATH;
        } else {
            savePath = context.getApplicationContext().getFilesDir()
                    .getAbsolutePath()
                    + IN_PATH;
        }
        try {
            filePic = new File(savePath + generateFileName() + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        System.out.println("文件名：" + filePic.getAbsolutePath());
        return filePic.getAbsolutePath();
    }



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
                        mLocationClient.stopLocation();
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
                        System.out.println("距离的多少marker: " + bijin_marker.getPosition());
                        if (!isjieshu && !MyApplication.isBijindian && AMapUtils.calculateLineDistance(oldLatLng, bijin_marker.getPosition()) <= 50) {
                            MyApplication.isBijindian = true;
                            System.out.println("距离的多少： " + AMapUtils.calculateLineDistance(oldLatLng, bijin_marker.getPosition()) + "\nmarker: " + bijin_marker.getPosition());
                            bofang("您已到达必经点，请开始进行指纹和面部解锁验证，验证成功后继续" + "跑步");
                            zhiWen();
                            bijin_marker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                    .decodeResource(getResources(), R.drawable.marker2)));
                        }
                        if (MyApplication.isBijindian && tujindian.size() <= 2) {
//                            lbsTraceClient.stopTrace();//在不需要轨迹纠偏时（如行程结束），可调用此接口结束纠偏
                            MyApplication.isBijindian = false;
                            Toast.makeText(DituActivity.this, "你的跑步已完成", Toast.LENGTH_LONG).show();
                            a = Math.round(a);
                            bofang("本次跑步已完成，请点结束按钮，结束本次跑步");
                            isjieshu = true;
                            return;
                        }
                        for (int i = 0; i < tujindian.size(); i++) {
                            if (!isjieshu && !iszhiwen && AMapUtils.calculateLineDistance(oldLatLng, tujindian.get(i).getPosition()) <= 50) {
                                MyApplication.cishu++;
                                tujindian.get(i).setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                        .decodeResource(getResources(), R.drawable.marker2)));
                                bofang("您已到达第" + MyApplication.cishu + "个途经点，请开始进行指纹和面部解锁验证，验证成功后继续" + "跑步");
                                iszhiwen = true;
                                zhiWen();
                                tujindian.remove(i);
                            }
                        }


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
        avi.show();
        if (!mLineInit.isEmpty()) {
            Douglas douglas = new Douglas(mLineInit, 0.5);
            guiji = douglas.compress();
        }
//        guiji.add(new LatLng(32.125266, 118.940807));
//        guiji.add(new LatLng(32.125280, 118.941107));
//        guiji.add(new LatLng(32.125299, 118.941207));
//        guiji.add(new LatLng(32.125366, 118.941307));
//        guiji.add(new LatLng(32.125566, 118.941407));
//        guiji.add(new LatLng(32.125766, 118.941507));
//        guiji.add(new LatLng(32.125966, 118.942107));
//        guiji.add(new LatLng(32.126266, 118.942207));
//        guiji.add(new LatLng(32.126566, 118.942307));
        if (!guiji.isEmpty()) {
            for (int i = 0; i < guiji.size(); i++) {
                list.add(aMap.getProjection().toScreenLocation(guiji.get(i)));
            }
            System.out.println("轨迹：" + list.get(0));
        }
        guijia.drawSportLine(list, R.drawable.qidian, R.drawable.xiaoliangqiu, this);

        // or avi.smoothToShow();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void stopAnim() {
        isjiazai = true;
        tiaozhuan();
        avi.hide();

        // or avi.smoothToHide();
    }

    private static final String NOTIFICATION_CHANNEL_NAME = "BackgroundLocation";
    private NotificationManager notificationManager = null;
    boolean isCreateChannel = false;

    @SuppressLint("NewApi")
    private Notification buildNotification() {

        Notification.Builder builder = null;
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= 26) {
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

        if (android.os.Build.VERSION.SDK_INT >= 16) {
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        setContentView(R.layout.ditu);


        MyApplication.cishu = 0;
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


        MyApplication.isBijindian = false;
        avi = findViewById(R.id.jiazai);
//        tv_kaluli = findViewById(R.id.tv_kaluli);
        tv_licheng = findViewById(R.id.tv_licheng);
        tv_shijian = findViewById(R.id.tv_shijian);
        guijia = findViewById(R.id.guiji_ditu);
        Intent i = getIntent();
        moshi = i.getStringExtra("moshi");

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
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
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.showIndoorMap(true);//显示室内地图
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        getdian();


//        //开始记录轨迹，每2s记录一次轨迹，每隔5个点合并请求一次纠偏并回调。
//        lbsTraceClient = LBSTraceClient.getInstance(this);
//        lbsTraceClient.startTrace((TraceStatusListener) DituActivity.this); //开始采集,需要传入一个状态回调监听。

        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                System.out.println("Mark的位置：" + marker.getPosition().latitude);
//                Intent intent = new Intent();
//                intent.putExtra("qidian_v", marker.getPosition().latitude);
//                intent.putExtra("qidian_v1",marker.getPosition().longitude);
//                intent.setClass(DituActivity.this, DaohangActivity.class);
//                startActivity(intent);
//                finish();
                return false;
            }
        });
        //使用 aMap.setMapTextZIndex(2) 可以将地图底图文字设置在添加的覆盖物之上
        aMap.setMapTextZIndex(2);
        aMap.showIndoorMap(true);
        kaishi = findViewById(R.id.kaishipaobu);
        shuaxin = findViewById(R.id.ditu_shuaxing);
        jieshu = findViewById(R.id.jieshupaobu);
        zanting = findViewById(R.id.zantingpaobu);
        view = findViewById(R.id.zantingjieshu);
        kaishi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
//获取当前时间
                Date date = new Date(System.currentTimeMillis());
                s6[0] =  simpleDateFormat.format(date);
                iskaishi = true;
                mLocationClient.startLocation();
                shuaxin.setClickable(false);
                shuaxin.setVisibility(View.GONE);
                kaishi.setVisibility(View.GONE);
                zanting.setVisibility(View.VISIBLE);
                jieshu.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
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
                a = Math.round(a);
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(DituActivity.this);
                builder.setTitle("结束跑步");
                builder.setMessage("你已跑了" + a + "米，是否结束跑步");
                builder.setCancelable(false);
                builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (a <= 800 ){
                            showToast("没跑到800米，不记成绩的哦！");
                            finish();
                        }else {
                        startAnim();
                        jieshu.setEnabled(false);
                        zanting.setEnabled(false);
                        timer.cancel();
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog = builder.create();
                dialog.show();
            }
        });
        shuaxin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bijin_marker != null) {
                    bijin_marker.destroy();
                }
                for (int i = tujindian.size() - 1; i >= 0; i--) {
                    tujindian.get(i).destroy();
                    tujindian.remove(i);
                }
                getdian();
            }
        });
//        String ss = mLocationClient.getLastKnownLocation().getAddress();
//        System.out.println("位置：" + ss);
    }

    /**
     * 根据一个点的经纬度和距离得到另外一个点的经纬度
     *
     * @param distance:单位是KM
     * @param latlngA
     * @param angle：角度
     * @return
     */
    public static LatLng getLatlng(float distance, LatLng latlngA, double angle) {
        return new LatLng(latlngA.latitude + (distance * Math.cos(angle * Math.PI / 180)) / 111,
                latlngA.longitude + (distance * Math.sin(angle * Math.PI / 180)) / (111 * Math.cos(latlngA.latitude * Math.PI / 180))
        );
    }

    private void getdian(){
        HttpUtils.post(MyApplication.getyundongarea, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                System.out.println("获取运动点：" + s);
                Gson gson = new Gson();
                zuobiaoBean = gson.fromJson(s, ZuobiaoBean.class);
                double v = (Math.random() * (zuobiaoBean.getX1() - zuobiaoBean.getX2())) + zuobiaoBean.getX2();
                double v1 = (Math.random() * (zuobiaoBean.getY1() - zuobiaoBean.getY2())) + zuobiaoBean.getY2();
                latLng = new LatLng(v1, v);
                geocoderSearch = new GeocodeSearch(DituActivity.this);
                geocoderSearch.setOnGeocodeSearchListener(DituActivity.this);
                // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
                RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latLng.latitude, latLng.longitude), 50, GeocodeSearch.AMAP);
                geocoderSearch.getFromLocationAsyn(query);
            }
        },null);
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

    public void bofang(String strTextToSpeech) {
        SpeechUtility.createUtility(DituActivity.this, SpeechConstant.APPID + "=5abadfdd");
        mTts = SpeechSynthesizer.createSynthesizer(DituActivity.this, new InitListener() {
            @Override
            public void onInit(int i) {
                if (ErrorCode.SUCCESS == i) {
                } else {
                    Toast.makeText(DituActivity.this, "语音合成初始化失败!", Toast.LENGTH_SHORT);
                }

            }
        });

        //设置语速,值范围：[0, 100],默认值：50
        mTts.setParameter(SpeechConstant.SPEED, "50");
        //设置音量
        mTts.setParameter(SpeechConstant.VOLUME, "tts_volume");
        //设置语调
        mTts.setParameter(SpeechConstant.PITCH, "tts_pitch");
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        mTts.startSpeaking(strTextToSpeech, new SynthesizerListener() {
            @Override
            public void onSpeakBegin() {

            }

            @Override
            public void onBufferProgress(int i, int i1, int i2, String s) {

            }

            @Override
            public void onSpeakPaused() {

            }

            @Override
            public void onSpeakResumed() {

            }

            @Override
            public void onSpeakProgress(int i, int i1, int i2) {

            }

            @Override
            public void onCompleted(SpeechError speechError) {

            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });
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
        //解析result获取地址描述信息
        if (!regeocodeResult.getRegeocodeAddress().getRoads().isEmpty() && regeocodeResult.getRegeocodeQuery().getPoint().getLatitude() >= zuobiaoBean.getY1() && regeocodeResult.getRegeocodeQuery().getPoint().getLatitude() <= zuobiaoBean.getY2() && regeocodeResult.getRegeocodeQuery().getPoint().getLongitude() <= zuobiaoBean.getX2() && regeocodeResult.getRegeocodeQuery().getPoint().getLongitude() >= zuobiaoBean.getX1()) {
            MarkerOptions markerOption = new MarkerOptions();
            markerOption.position(latLng);
            markerOption.title("必经点").snippet("必须经过这个点");
            markerOption.draggable(false);//设置Marker可拖动
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(), R.drawable.bijindian)));
            // 将Marker设置为贴地显示，可以双指下拉地图查看效果
            markerOption.setFlat(false);//设置marker平贴地图效果
            bijin_marker = aMap.addMarker(markerOption);
            aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(bijin_marker.getPosition(), 17, 0, 0)));
//            //更新marker图标
//            marker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
//                    .decodeResource(getResources(),R.drawable.marker2)));
//            aMap.addMarker(new MarkerOptions().position(latLng).title("必经点").snippet("必须经过这个点"));
            for (int i1 = 0; i1 < 4; i1++) {
                LatLng latLng1 = getLatlng((float) 0.1, latLng, Math.random() * 360);
                tujindian.add(aMap.addMarker(new MarkerOptions().position(latLng1).title("选过点").snippet("选择两个点通过")));
            }

        } else {
            System.out.println("进入else" + regeocodeResult.getRegeocodeAddress().getRoads() + "\n"+regeocodeResult.getRegeocodeQuery().getPoint().toString() + "\n" +regeocodeResult.getRegeocodeQuery().getPoint().getLongitude() + "\n" + zuobiaoBean.getX2() + "   "  +zuobiaoBean.getX1() );
            double v = (Math.random() * (zuobiaoBean.getY2() - zuobiaoBean.getY1())) + zuobiaoBean.getY1();
            double v1 = (Math.random() * (zuobiaoBean.getX2() - zuobiaoBean.getX1())) + zuobiaoBean.getX1();
            latLng = new LatLng(v, v1);
            geocoderSearch = new GeocodeSearch(this);
            geocoderSearch.setOnGeocodeSearchListener(this);
            // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
            RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latLng.latitude, latLng.longitude), 50, GeocodeSearch.AMAP);
            geocoderSearch.getFromLocationAsyn(query);

        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }
    //轨迹纠偏回调
//    @Override
//    public void onTraceStatus(List<TraceLocation> list, List<LatLng> list1, String s) {
//     //list 定位得到的轨迹点集，list1 纠偏后的点集，s 轨迹纠偏错误信息
//        System.out.println("轨迹纠偏错误:  " + s);
//        Toast.makeText(DituActivity.this,"轨迹纠偏错误：" + s, Toast.LENGTH_LONG).show();
//        aMap.addPolyline(new PolylineOptions().
//                addAll(list1).width(10).color(Color.argb(255, 1, 1, 1)));
//    }

    //指纹
    private void zhiWen() {
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
        if (null != alarm) {
            //停止定位的时候取消闹钟
            alarm.cancel(alarmPi);
        }
        final int[] cc = {0};
        FingerprintUtil.callFingerPrint(new FingerprintUtil.OnCallBackListenr() {
            AlertDialog dialog;

            @Override
            public void onSupportFailed() {
                showToast("当前设备不支持指纹，已为你跳过指纹解锁。进行拍照验证");
                paizhao();
            }

            @Override
            public void onInsecurity() {
                showToast("当前设备未处于安全保护中");
            }

            @Override
            public void onEnrollFailed() {
                showToast("请到设置中设置指纹");
            }

            @Override
            public void onAuthenticationStart() {
                AlertDialog.Builder builder = new AlertDialog.Builder(DituActivity.this);
                builder.setTitle("正在解锁中");
                builder.setMessage("指纹解锁");
                builder.setIcon(android.R.drawable.stat_sys_warning);
                builder.setCancelable(false);
//                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        FingerprintUtil.cancel();
//                    }
//                });
                dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                showToast(errString.toString());
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                iszhiwen = false;
            }

            @Override
            public void onAuthenticationFailed() {
                cc[0]++;
                if (cc[0] == 1) {
                    showToast("解锁失败你还有两次机会，两次后本次成绩作废");
                } else if (cc[0] == 2) {
                    showToast("解锁失败你还有一次机会，两次后本次成绩作废");
                } else if (cc[0] == 3) {
                    showToast("解锁失败,本次成绩作废");
                    dialog.dismiss();
                }
                mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
                bofang("解锁失败");

            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                showToast(helpString.toString());
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                showToast("指纹解锁成功，请进行拍照解锁必须正面照。");
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                paizhao();

            }
        });
    }

    private void paizhao() {
        Album.camera(DituActivity.this)//打开相机
//                .toolBarColor(ContextCompat.getColor(this, R.color.colorPrimary))
//                .statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                .image() // 拍照。
                .requestCode(1)
                .onResult(new Action<String>() {
                    @Override
                    public void onAction(int requestCode, @NonNull String result) {
                        tupianlist.add(result);
                        System.out.println("照相结果" + result);
                        iszhiwen = false;
                        mLocationClient.startLocation();
                        if (null != alarm) {
                            //设置一个闹钟，2秒之后每隔一段时间执行启动一次定位程序

                            alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000,
                                    1000, alarmPi);
                        }
                        String ss = mLocationClient.getLastKnownLocation().getAddress();
                        paizhaodidian.add(ss);

                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(int requestCode, @NonNull String result) {
                        //用户取消
                        showToast("请进行拍照，否则跑步结果无效。");
                        paizhao();

                    }
                })
                .start();
    }

    public void showToast(String name) {
        Toast.makeText(DituActivity.this, name, Toast.LENGTH_SHORT).show();
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

    public String m2(float f) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(f);
    }
    @Override
    public void onFinish() {
        //轨迹绘制完成返回
        saveCurrentImage();
        ishuizhi = true;
        shangchuan();
        tiaozhuan();
        guijia.setVisibility(View.GONE);
        // 绘制一个大地曲线
        aMap.addPolyline((new PolylineOptions())
                .addAll(guiji)
                .width(20)
                .geodesic(true).color( Color.parseColor("#22c1c3")));
        Marker marker = aMap.addMarker(new MarkerOptions().position(guiji.get(0)));
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.drawable.qidian)));
        Marker marker1 = aMap.
                addMarker(new MarkerOptions().position(guiji.get(guiji.size() - 1)));
        marker1.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.drawable.zhongdian)));
    }

    private void shangchuan(){
        if (isjieping && ishuizhi) {
            PaoBuJieGuoBean paoBuJieGuoBean = new PaoBuJieGuoBean(guiji, MyApplication.user.getId(), 100, paizhaodidian);
            Gson gson = new Gson();
            String ss = gson.toJson(paoBuJieGuoBean);
            String s1[] = new String[1];
            s1[0] = ss;
            String s2[] = new String[1];
            s2[0] = moshi;
            float shi0 = 0, fen0 = 0, miao0 = 0;
            shi0 = (float) ((shi[1] * 10 + shi[0]) * 60.0);
            miao0 = (float) ((miao[1] * 10 + miao[0]) / 60.0);
            fen0 = fen[1] * 10 + fen[0] + shi0 + miao0;
            float peisu = (float) (fen0 / (a / 1000.0));
            int peisu1 = (int) Math.floor(peisu);
            int peisu2 = (int) Math.floor((peisu - peisu1) * 60.0);
            s3[0] = String.valueOf(peisu1) + "'" + String.valueOf(peisu2);
            System.out.println("s3[0]: " + s3[0]);
            s4[0] = m2(a);
            s5[0] = String.format(getResources().getString(R.string.current_time), shi[1], shi[0], fen[1], fen[0], miao[1], miao[0]);
            Map<String, String[]> map = new HashMap<>();
            map.put("guiji", s1);
            map.put("moshi", s2);
            map.put("peisu", s3);
            map.put("licheng", s4);
            map.put("kaishi", s6);
            System.out.println("里程：" + s4[0]);
            tupianlist1.addAll(tupianlist);
            tupianlist1.add(lujing);
            HttpUtils.jieshupaobu("1", map, tupianlist1, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    System.out.println("跑步结束返回: " + response.body().string());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                stopAnim();
                            }
                        }
                    });


                }
            });
        }
    }
    private void tiaozhuan(){
        if (isjieping && isjiazai && ishuizhi){
            final String shijian = String.format(getResources().getString(R.string.current_time), shi[1], shi[0], fen[1], fen[0], miao[1], miao[0]);
            Bundle bundle = new Bundle();
            bundle.putString("name", MyApplication.user.getVname());
            bundle.putString("shijian", s5[0]);
            bundle.putString("licheng", s4[0] + "米");
            bundle.putString("peisu", s3[0]);
            bundle.putStringArrayList("tupian", tupianlist);
            bundle.putByteArray("jieping", bitmapByte);
            e.setArguments(bundle);
            e.show(DituActivity.this.getSupportFragmentManager(), "alert");
            e.setYesOnclickListener("确定", new ExampleDialogFragment.onYesOnclickListener() {
                @Override
                public void onYesClick() {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("tupian", tupianlist);
                    MyApplication.lujing = lujing;
                    intent.putExtra("bu", bundle);
                    intent.setClass(DituActivity.this, FabiaoshuoshuoActivity.class);
                    startActivity(intent);
                    System.out.println("进入YES");
                    DituActivity.this.finish();
                }
            });
            e.setNoOnclickListener("取消", new ExampleDialogFragment.onNoOnclickListener() {
                @Override
                public void onNoClick() {
                    e.dismiss();
                }
            });

        }
    }
    //这种方法状态栏是空白，显示不了状态栏的信息
    private Bitmap saveCurrentImage() {
        //获取当前屏幕的大小
        int width = getWindow().getDecorView().getRootView().getWidth();
        int height = getWindow().getDecorView().getRootView().getHeight();
        //生成相同大小的图片
        Bitmap temBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //找到当前页面的跟布局
        View view = getWindow().getDecorView().getRootView();
        //设置缓存
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        //从缓存中获取当前屏幕的图片
        temBitmap = view.getDrawingCache();
        /**
         * 设置截屏
         */
        final Bitmap finalTemBitmap = temBitmap;
        aMap.getMapScreenShot(new AMap.OnMapScreenShotListener() {
            @Override
            public void onMapScreenShot(Bitmap bitmap) {
                Bitmap bitmap3 = Bitmap.createBitmap(finalTemBitmap.getWidth(), finalTemBitmap.getHeight(), finalTemBitmap.getConfig());
                Canvas canvas = new Canvas(bitmap3);
                canvas.drawBitmap(finalTemBitmap, new Matrix(), null);
                canvas.drawBitmap(bitmap, 0, 130, null);  //120、350为bitmap2写入点的x、y坐标
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap3.compress(Bitmap.CompressFormat.PNG, 100, baos);
                bitmapByte = baos.toByteArray();
                lujing = saveBitmap(DituActivity.this,bitmap3);
                isjieping = true;
                shangchuan();
                tiaozhuan();
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//                try {
//                    // 保存在SD卡根目录下，图片为png格式。
//                    FileOutputStream fos = new FileOutputStream(
//                            Environment.getExternalStorageDirectory() + "/DCIM/Camera/test_"
//                                    + sdf.format(new Date()) + ".png");
//                    boolean ifSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
//                    try {
//                        fos.flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        fos.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    if (ifSuccess) {

                         //将合并后的bitmap3保存为png图片到本地
//                        FileOutputStream out = new FileOutputStream(
//                                Environment.getExternalStorageDirectory() + "/DCIM/Camera/test_"
//                                        + sdf.format(new Date()) + ".png");
//                        bitmap3.compress(Bitmap.CompressFormat.PNG, 90, out);
//                    }
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void onMapScreenShot(Bitmap bitmap, int i) {

            }
        });
        return temBitmap;
    }
}