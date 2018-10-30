package ruanjianbei.sport.mysport.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;


import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.util.FingerprintUtil;
import skin.support.SkinCompatManager;


public class DaohangActivity extends Activity implements GeocodeSearch.OnGeocodeSearchListener, AMapNaviViewListener, AMapNaviListener {

    private AMapNaviView mAMapNaviView;
    private AMapNavi mAMapNavi;
    private SpeechSynthesizer mTts;
    private int i;
    private int c = 0;//第几段路
    private boolean aBoolean = false;
    private double qidian_v, qidian_v1;
    private Bundle savedIn;
    private GeocodeSearch geocoderSearch;
    private LatLng latLng;
    private double ra = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daohang);
        savedIn = savedInstanceState;
        initView();
    }

    public void initView() {
        Intent intent = getIntent();
        qidian_v = intent.getDoubleExtra("qidian_v", 0);
        qidian_v1 = intent.getDoubleExtra("qidian_v1", 0);
        //讯飞语音
        SpeechUtility.createUtility(DaohangActivity.this, SpeechConstant.APPID + "=5abadfdd");
        mTts = SpeechSynthesizer.createSynthesizer(DaohangActivity.this, new InitListener() {
            @Override
            public void onInit(int i) {
                if (ErrorCode.SUCCESS == i) {
                } else {
                    Toast.makeText(DaohangActivity.this, "语音合成初始化失败!", Toast.LENGTH_SHORT);
                }

            }
        });

        //设置语速,值范围：[0, 100],默认值：50
        mTts.setParameter(SpeechConstant.SPEED, "50");
        //设置音量
        mTts.setParameter(SpeechConstant.VOLUME, "tts_volume");
        //设置语调
        mTts.setParameter(SpeechConstant.PITCH, "tts_pitch");
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view1);
        mAMapNaviView.onCreate(savedIn);
        mAMapNaviView.setAMapNaviViewListener(this);
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);
        //开启语音播报
        mAMapNavi.setUseInnerVoice(true);
//        AMapNaviViewOptions options = mAMapNaviView.getViewOptions();
////关闭自动绘制路线（如果你想自行绘制路线的话，必须关闭！！！）
//        options.setAutoDrawRoute(false);
//        RouteOverLay routeOverlay = new RouteOverLay(mAMapNaviView.getMap(), mAMapNavi.getNaviPath(), this);
////设置起点的图标
//        routeOverlay.setStartPointBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher_background));
////设置终点的图标
//        routeOverlay.setEndPointBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher_background));
////设置途经点的图标
//        routeOverlay.setWayPointBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher_foreground));
//        try {
//            routeOverlay.setWidth(30);
//        } catch (AMapNaviException e) {
////宽度须>0
//            e.printStackTrace();
//        }
//        int color[] = new int[10];
//        color[0] = Color.BLACK;
//        int i[] = new int[1];
//        i[0] = 0;
////以途径点的index分隔，用不同的颜色绘制路段
//        routeOverlay.addToMap(color, i);
//        mAMapNaviView.setViewOptions(options);

    }
    private void getra(){
        ra = Math.random();
    }
    public void bofang(String strTextToSpeech) {
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
    public void onInitNaviFailure() {

    }

    //初始化成功
    //当 AMapNavi 对象初始化成功后，会进入 onInitNaviSuccess 回调函数，
    // 在该回调函数中调用路径规划方法计算路径。
    //当步行路线规划成功时，会进 onCalculateRouteSuccess 回调
    @Override
    public void onInitNaviSuccess() {
        if (!aBoolean) {
            mAMapNavi.calculateWalkRoute(new NaviLatLng(qidian_v, qidian_v1));
        }else {
                latLng = getLatlng((float) 0.1, new LatLng(qidian_v, qidian_v1), Math.random() * 360);
                geocoderSearch = new GeocodeSearch(this);
                geocoderSearch.setOnGeocodeSearchListener(this);
                // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
                RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latLng.latitude, latLng.longitude), 50, GeocodeSearch.AMAP);
                geocoderSearch.getFromLocationAsyn(query);
        }
    }

    @Override
    public void onStartNavi(int i) {
        //开始导航回调

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

        //当前位置回调
        NaviInfo naviInfo = mAMapNavi.getNaviInfo();
        if (aBoolean) {
            if ((i - naviInfo.getPathRetainDistance()) >= 100) {
                mTts.setParameter(SpeechConstant.VOICE_NAME, "nannan");
                bofang("已完成本日任务");
            }
            if (naviInfo.getPathRetainDistance() == ra * 50 && c == 0){
                mAMapNavi.pauseNavi();
                getra();
                c++;
                mTts.setParameter(SpeechConstant.VOICE_NAME, "nannan");
                bofang("开始第一次指纹验证");
                zhiWen();
            }
            if (naviInfo.getPathRetainDistance() == (ra * 50 + 50) && c == 1){
                mAMapNavi.pauseNavi();
                c++;
                mTts.setParameter(SpeechConstant.VOICE_NAME, "nannan");
                bofang("开始第二次指纹验证");
                zhiWen();
            }
        }
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

    @Override
    public void onGetNavigationText(int i, String s) {
        //播报类型和播报文字回调
    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onEndEmulatorNavi() {
        //结束模拟导航
    }

    @Override
    public void onArriveDestination() {
        //到达目的地
        if (!aBoolean) {
            mAMapNavi.setUseInnerVoice(false);
            mAMapNavi.stopNavi();
            mAMapNavi.destroy();
            mAMapNaviView.onDestroy();
            mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaorong");
            bofang("已到达起点，请开始第一段跑步");
            onCreate(savedIn);
            aBoolean = true;
        }

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onReCalculateRouteForYaw() {
        //偏航后重新计算路线回调
    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
        //拥堵后重新计算路线回调
    }

    @Override
    public void onArrivedWayPoint(int i) {
        //到达途径点
    }

    @Override
    public void onGpsOpenStatus(boolean b) {
        //GPS开关状态回调
    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {
        //导航过程中的信息更新，请看NaviInfo的具体说明

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {
        //显示转弯回调
    }

    @Override
    public void hideCross() {
        //隐藏转弯回调
    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

    }

    @Override
    public void hideLaneInfo() {

    }

    //当路径规划成功是，开启导航。
    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        mAMapNavi.startNavi(NaviType.GPS);
        i = mAMapNavi.getNaviPath().getAllLength();
    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mAMapNaviView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAMapNaviView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAMapNaviView.onDestroy();
        //since 1.6.0 不再在naviview destroy的时候自动执行AMapNavi.stopNavi();请自行执行
        mAMapNavi.stopNavi();
        mAMapNavi.destroy();
    }

    @Override
    public void onNaviSetting() {

    }

    @Override
    public void onNaviCancel() {

    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }

    @Override
    public void onNaviMapMode(int i) {

    }

    @Override
    public void onNaviTurnClick() {

    }

    @Override
    public void onNextRoadClick() {

    }

    @Override
    public void onScanViewButtonClick() {

    }

    @Override
    public void onLockMap(boolean b) {

    }

    @Override
    public void onNaviViewLoaded() {

    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        //解析result获取地址描述信息
        if (!regeocodeResult.getRegeocodeAddress().getRoads().isEmpty() && regeocodeResult.getRegeocodeQuery().getPoint().getLatitude() >= 32.125265 && regeocodeResult.getRegeocodeQuery().getPoint().getLatitude() <= 32.130913 && regeocodeResult.getRegeocodeQuery().getPoint().getLongitude() <= 118.944779 && regeocodeResult.getRegeocodeQuery().getPoint().getLongitude() >= 118.940707) {
            mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaorong");
            bofang("请开始进行指纹和声纹验证，验证成功后开始第" + c + 1 + "段跑步");
            zhiWen();
        } else {
            latLng = getLatlng((float) 0.1, new LatLng(qidian_v, qidian_v1), Math.random() * 360);
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
    private void zhiWen(){
        FingerprintUtil.callFingerPrint(new FingerprintUtil.OnCallBackListenr() {
            AlertDialog dialog;
            @Override
            public void onSupportFailed() {
                showToast("当前设备不支持指纹");
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
                AlertDialog.Builder builder = new AlertDialog.Builder(DaohangActivity.this);
                builder.setTitle("正在解锁中");
                builder.setMessage("指纹解锁");
                builder.setIcon(android.R.drawable.stat_sys_warning);
                builder.setCancelable(false);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FingerprintUtil.cancel();
                    }
                });
                dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                showToast(errString.toString());
                if (dialog != null  &&dialog.isShowing()){
                    dialog.dismiss();
                }
            }

            @Override
            public void onAuthenticationFailed() {
                showToast("解锁失败");
                mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaorong");
                bofang("解锁失败");
            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                showToast(helpString.toString());
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                showToast("解锁成功");
                if (c == 0) {
                    mTts.setParameter(SpeechConstant.VOICE_NAME, "nannan");
                    bofang("指纹验证成功开始第" + c + 1 + "段跑步");
                    mAMapNavi.calculateWalkRoute(new NaviLatLng(latLng.latitude, latLng.longitude));
                }else {
                    mTts.setParameter(SpeechConstant.VOICE_NAME, "nannan");
                    bofang("第" + c +"次指纹验证成功继续跑步");
                    mAMapNavi.resumeNavi();
                }
                if (dialog != null  &&dialog.isShowing()){
                    dialog.dismiss();
                }

            }
        });
    }

    public void showToast(String name ){
        Toast.makeText(DaohangActivity.this,name,Toast.LENGTH_SHORT).show();
    }
}

