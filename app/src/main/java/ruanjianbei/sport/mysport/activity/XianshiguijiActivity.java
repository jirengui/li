package ruanjianbei.sport.mysport.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

import java.util.ArrayList;
import java.util.List;

import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.bean.PaoBuJieGuoBean;
import ruanjianbei.sport.mysport.util.MyApplication;
import ruanjianbei.sport.mysport.zidingyi_view.Guiji;

public class XianshiguijiActivity extends Activity implements Guiji.OnTrailChangeListener {

    private MapView guijiDitu;
    private AMap aMap;
    private PaoBuJieGuoBean paoBuJieGuoBean;
    private Guiji guijia;
    private List<Point> list = new ArrayList<>();
    private ArrayList<LatLng> guiji = new ArrayList<LatLng>();

    private GeocodeSearch geocoderSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        setContentView(R.layout.xianshiguiji);

        guijia = findViewById(R.id.xianshiguiji_guiji);
        guijiDitu = (MapView) findViewById(R.id.xianshiguiji_ditu);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        guijiDitu.onCreate(savedInstanceState);
        //初始化地图控制器对象
        if (aMap == null) {
            aMap = guijiDitu.getMap();
        }
        aMap.setMapTextZIndex(2);
        aMap.showIndoorMap(true);
        paoBuJieGuoBean = MyApplication.paoBuJieGuoBean;
        if (!paoBuJieGuoBean.getGuiji().isEmpty()) {
            for (int i = 0; i < paoBuJieGuoBean.getGuiji().size(); i++) {
                LatLng latLng = new LatLng(paoBuJieGuoBean.getGuiji().get(i).longitude, paoBuJieGuoBean.getGuiji().get(i).latitude);
                guiji.add(latLng);
                System.out.println("轨迹个数：" + i);
            }
        }
        //移动视角
//        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(paoBuJieGuoBean.getGuiji().get(0).longitude,paoBuJieGuoBean.getGuiji().get(0).latitude), 17, 0, 0)));
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(guiji.get(0), 17, 0, 0)));
//
//        aMap.addPolyline((new PolylineOptions())
//                .addAll(guiji)
//                .width(20)
//                .geodesic(true).color( Color.parseColor("#22c1c3")));
//        System.out.println("轨迹记录：" + aMap.getProjection().toScreenLocation(new LatLng(32.125280, 118.941107)));
        Button button = findViewById(R.id.xianshiguiji_bu);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!guiji.isEmpty()) {
                    for (int i = 0; i < guiji.size(); i++) {
//                guiji.add(new LatLng(32.125266, 118.940807));
                        list.add(aMap.getProjection().toScreenLocation(guiji.get(i)));
//                list.add(aMap.getProjection().toScreenLocation(new LatLng(paoBuJieGuoBean.getGuiji().get(i).longitude,paoBuJieGuoBean.getGuiji().get(i).latitude)));
                        System.out.println("个数：" + i + "位置：" + aMap.getProjection().toScreenLocation(guiji.get(i)));
                    }
                    guijia.drawSportLine(list, R.drawable.qidian, R.drawable.xiaoliangqiu, XianshiguijiActivity.this);
                }
            }
        });
//        aMap.setOnMapClickListener(this);
//        geocoderSearch = new GeocodeSearch(this);
//        geocoderSearch.setOnGeocodeSearchListener(this);

    }

    @Override
    public void onFinish() {
        System.out.println("完成");
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


    }

//    @Override
//    public void onMapClick(LatLng latLng) {
////        new LatLonPoint(latLng.latitude, latLng.longitude)
//        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latLng.latitude, latLng.longitude), 50, GeocodeSearch.AMAP);
//        geocoderSearch.getFromLocationAsyn(query);
//    }
//
//    //解析返回的结果
//    @Override
//    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
//        System.out.println("build: " + regeocodeResult.getRegeocodeAddress().getBuilding());
//        System.out.println("详细地址: " + regeocodeResult.getRegeocodeAddress().getFormatAddress());
//        for (int a = 0; a < regeocodeResult.getRegeocodeAddress().getPois().size(); a++){
//            System.out.println("build: " + regeocodeResult.getRegeocodeAddress().getPois().get(a).getTypeDes() + "sd: " +  regeocodeResult.getRegeocodeAddress().getPois().get(a).getTitle());
//        }
//    }
//
//
//    @Override
//    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
//
//    }
}
