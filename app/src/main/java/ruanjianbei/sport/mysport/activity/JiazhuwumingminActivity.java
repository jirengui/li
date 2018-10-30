package ruanjianbei.sport.mysport.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.adapter.RcGuijijiluAdapter;
import ruanjianbei.sport.mysport.adapter.RcJianzhuwuAdapter;
import ruanjianbei.sport.mysport.util.DensityUtil;
import ruanjianbei.sport.mysport.util.MyApplication;

public class JiazhuwumingminActivity extends Activity implements View.OnClickListener, AMap.OnMapClickListener, GeocodeSearch.OnGeocodeSearchListener {

    private ImageView jianzhuwuFanhui;
    private MapView jianzhuwuMap;
    private RecyclerView jianzhuwuRc;
    private AMap aMap;

    private GeocodeSearch geocoderSearch;
    private RcJianzhuwuAdapter adapter;
    private List<String> list = new ArrayList<>();
    private boolean isone = true;
    private AlertDialog.Builder builder;
    private AlertDialog dialogDemo;
    private String ss;
    public AMapLocationClientOption mLocationOption = null;
    public AMapLocationClient mLocationClient = null;
    int cishu = 0;
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (cishu < 1) {
                aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), 17, 0, 0)));
                cishu++;
            }
        }
    };


    private Boolean istwo = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiazhuwumingmin);

        jianzhuwuFanhui = (ImageView) findViewById(R.id.jianzhuwu_fanhui);
        jianzhuwuMap = (MapView) findViewById(R.id.jianzhuwu_map);
        jianzhuwuRc = (RecyclerView) findViewById(R.id.jianzhuwu_rc);

        jianzhuwuMap.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = jianzhuwuMap.getMap();
        }
        aMap.setMapTextZIndex(2);
        aMap.showIndoorMap(true);

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
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


        //切换视角
        aMap.setOnMapClickListener(this);
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        jianzhuwuFanhui.setOnClickListener(this);


    }

    @Override
    public void onMapClick(LatLng latLng) {
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latLng.latitude, latLng.longitude), 100, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }

    @Override
    public void onRegeocodeSearched(final RegeocodeResult regeocodeResult, int i) {
        System.out.println("详细地址: " + regeocodeResult.getRegeocodeAddress().getFormatAddress());
        list = new ArrayList<>();
        for (int a = 0; a < regeocodeResult.getRegeocodeAddress().getPois().size(); a++) {
            list.add(regeocodeResult.getRegeocodeAddress().getPois().get(a).getTitle());
        }
        System.out.println("list的长度: " + list.size());

        if (isone) {
            adapter = new RcJianzhuwuAdapter(list, this);
            ViewGroup.LayoutParams lp = jianzhuwuRc.getLayoutParams();
            if (list.size() > 4) {
                lp.height = DensityUtil.dip2px(this, 41 * 4);
            } else {
                lp.height = DensityUtil.dip2px(this, 41 * list.size());
            }
            jianzhuwuRc.setLayoutParams(lp);
            jianzhuwuRc.setLayoutManager(new LinearLayoutManager(this));
            jianzhuwuRc.setAdapter(adapter);
        } else {
            ViewGroup.LayoutParams lp = jianzhuwuRc.getLayoutParams();
            if (list.size() > 4) {
                lp.height = DensityUtil.dip2px(this, 41 * 4);
            } else {
                lp.height = DensityUtil.dip2px(this, 41 * list.size());
            }
            jianzhuwuRc.setLayoutParams(lp);
            adapter.replaceAll(list);
        }
        adapter.setItemClickListener(new RcJianzhuwuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, final int o) {
                final EditText et = new EditText(JiazhuwumingminActivity.this);
                Drawable drawable = getDrawable(R.drawable.ed_dengluhuise);
                et.setBackground(drawable);
                et.setTextSize(16);
                builder = new AlertDialog.Builder(JiazhuwumingminActivity.this);
                builder.setTitle("你已选择")
                        .setMessage("\n" + list.get(o) + "\n\n请重新命名")
                        .setView(et)
                        .setCancelable(false)
                        .setPositiveButton(Html.fromHtml("<font color=\"#3cc6b3\">确定</font>"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (istwo) {
                                    String input = et.getText().toString();
                                    if (input.equals("")) {
                                        keepDialogOpen(dialogDemo);
                                        Toast.makeText(getApplicationContext(), "不能为空！", Toast.LENGTH_LONG).show();
                                    } else {
                                        keepDialogOpen(dialogDemo);
                                        dialogDemo.setTitle(Html.fromHtml("<font color=\"#3cc6b3\">提示</font>"));
                                        et.setHeight(0);
                                        et.setVisibility(View.GONE);
                                        dialogDemo.setMessage("\n恭喜你已将“" + list.get(o) + "”" + "\n\n命名为：" + input + "\n\n" + Html.fromHtml("<font color=\"red\">跑步者经过时将会自动播报,并显示您的昵称</font>"));
                                        istwo = false;
                                        ss = input;
                                    }
                                } else {
                                    System.out.println("进入第二个");
                                    closeDialog(dialogDemo);
                                    MarkerOptions markerOption = new MarkerOptions();
                                    LatLng latLng = new LatLng(regeocodeResult.getRegeocodeAddress().getPois().get(o).getLatLonPoint().getLatitude(), regeocodeResult.getRegeocodeAddress().getPois().get(o).getLatLonPoint().getLongitude());
                                    markerOption.position(latLng);
                                    markerOption.title(MyApplication.user.getVname()).snippet(list.get(o) + "(" + ss + ")");
                                    markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                            .decodeResource(getResources(), R.drawable.xiaoren2)));
                                    // 将Marker设置为贴地显示，可以双指下拉地图查看效果
                                    markerOption.setFlat(false);//设置marker平贴地图效果
//                                    markerOption.autoOverturnInfoWindow(true);
                                    Marker marker = aMap.addMarker(markerOption);
                                    marker.showInfoWindow();
                                    list = new ArrayList<>();
                                    ViewGroup.LayoutParams lp = jianzhuwuRc.getLayoutParams();
                                    if (list.size() > 4) {
                                        lp.height = DensityUtil.dip2px(JiazhuwumingminActivity.this, 41 * 4);
                                    } else {
                                        lp.height = DensityUtil.dip2px(JiazhuwumingminActivity.this, 41 * list.size());
                                    }
                                    jianzhuwuRc.setLayoutParams(lp);
                                    adapter.replaceAll(list);
                                    istwo = true;
                                }
                            }
                        })
                        .setNegativeButton(Html.fromHtml("<font color=\"#3cc6b3\">取消</font>"), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                closeDialog(dialogDemo);
                            }
                        });
                dialogDemo = builder.create();
//   显示对话框
                dialogDemo.show();
            }
        });
    }

    //保持dialog不关闭的方法
    private void keepDialogOpen(AlertDialog dialog) {
        try {
            java.lang.reflect.Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //关闭dialog的方法
    private void closeDialog(AlertDialog dialog) {
        try {
            java.lang.reflect.Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jianzhuwu_fanhui:
                finish();
                break;
        }
    }
}
