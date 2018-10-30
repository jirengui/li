package ruanjianbei.sport.mysport.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;

import com.githang.statusbar.StatusBarCompat;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

import ruanjianbei.sport.mysport.R;

public class TianqiActivity extends Activity {

    private ImageView tianqiFanhui;
    private TextView tianqiChengshi;
    private TextView tianqiTime;
    private ImageView tianqiTubiao;
    private LinearLayout aa1;
    private TextView tianqiFengxiang;
    private TextView tianqiFengji;
    private TextView tianqiShidu;
    private TextView tianqiTianqi;
    private TextView tianqiWendu;
    private TextView yundongjianyi;
    private String chengshi, time, fengxiang, fengli, shidu, tianqi, wendu;

    //雷达图
    private RadarChart radarChart;
    private ArrayList<String> x = new ArrayList<String>();
    private List<RadarEntry> y = new ArrayList<RadarEntry>();
    private ArrayList<RadarDataSet> radarDataSets = new ArrayList<RadarDataSet>();
    private RadarData radarData = null;
    //运动指数，根据天气温度风力湿度四项打分，满分100
    private int tiaoshengshu = 0, buxingshu = 0, qixingshu = 0, paobushu = 0, pashanshu = 0, youyongshu = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        //false为白色字体，ture为黑色字体
        StatusBarCompat.setLightStatusBar(getWindow(), false);
        setContentView(R.layout.tianqi);

        Intent intent = getIntent();
        chengshi = intent.getStringExtra("chengshi");
        time = intent.getStringExtra("shijian");
        tianqi = intent.getStringExtra("tianqi");
        wendu = intent.getStringExtra("wendu");
        fengxiang = intent.getStringExtra("fengxiang");
        fengli = intent.getStringExtra("fengli");
        shidu = intent.getStringExtra("shidu");


        tianqiFanhui = (ImageView) findViewById(R.id.tianqi_fanhui);
        tianqiChengshi = (TextView) findViewById(R.id.tianqi_chengshi);
        tianqiTime = (TextView) findViewById(R.id.tianqi_time);
        tianqiTubiao = (ImageView) findViewById(R.id.tianqi_tubiao);
        aa1 = (LinearLayout) findViewById(R.id.aa1);
        tianqiFengxiang = (TextView) findViewById(R.id.tianqi__fengxiang);
        tianqiFengji = (TextView) findViewById(R.id.tianqi_fengji);
        tianqiShidu = (TextView) findViewById(R.id.tianqi_shidu);
        tianqiTianqi = (TextView) findViewById(R.id.tianqi_tianqi);
        tianqiWendu = (TextView) findViewById(R.id.tianqi_wendu);
        yundongjianyi = (TextView) findViewById(R.id.yundongjianyi);
        radarChart = (RadarChart) findViewById(R.id.tianqi_leida);


        tianqiFanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tianqiChengshi.setText(chengshi);
        tianqiTime.setText(time + "发布");
        tianqiWendu.setText(wendu + "°");
        tianqiTianqi.setText(tianqi);
        tianqiFengxiang.setText(fengxiang + "风");
        tianqiFengji.setText(fengli + "级");
        tianqiShidu.setText(shidu + "％");


        switch (tianqi) {
            case "多云": {
                tiaoshengshu += 20;
                paobushu += 23;
                buxingshu += 21;
                qixingshu += 23;
                pashanshu += 22;
                youyongshu += 21;
                Drawable drawable = getDrawable(R.drawable.tianqi_duoyun);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "阴": {
                tiaoshengshu += 20;
                paobushu += 17;
                buxingshu += 18;
                qixingshu += 17;
                pashanshu += 16;
                youyongshu += 15;
                Drawable drawable = getDrawable(R.drawable.tianqi_yin);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "阵雨": {
                tiaoshengshu += 23;
                paobushu += 13;
                buxingshu += 10;
                qixingshu += 11;
                pashanshu += 8;
                youyongshu += 9;
                Drawable drawable = getDrawable(R.drawable.tianqi_zhengyu);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "雷阵雨": {
                tiaoshengshu += 22;
                paobushu += 10;
                buxingshu += 8;
                qixingshu += 5;
                pashanshu += 3;
                youyongshu += 3;
                Drawable drawable = getDrawable(R.drawable.tianqi_leizhengyu);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "晴": {
                tiaoshengshu += 20;
                paobushu += 23;
                buxingshu += 22;
                qixingshu += 22;
                pashanshu += 21;
                youyongshu += 23;
                Drawable drawable = getDrawable(R.drawable.tianqi_qing);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "雷阵雨并伴有冰雹": {
                tiaoshengshu += 18;
                paobushu += 3;
                buxingshu += 5;
                qixingshu += 3;
                pashanshu += 2;
                youyongshu += 2;
                Drawable drawable = getDrawable(R.drawable.tianqi_leizhengyubingbao);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "雨夹雪": {
                tiaoshengshu += 18;
                paobushu += 3;
                buxingshu += 5;
                qixingshu += 3;
                pashanshu += 2;
                youyongshu += 2;
                Drawable drawable = getDrawable(R.drawable.tianqi_yujiaxue);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "小雨": {
                tiaoshengshu += 20;
                paobushu += 5;
                buxingshu += 6;
                qixingshu += 6;
                pashanshu += 4;
                youyongshu += 10;
                Drawable drawable = getDrawable(R.drawable.tianqi_xiaoyu);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "中雨": {
                tiaoshengshu += 20;
                paobushu += 3;
                buxingshu += 5;
                qixingshu += 3;
                pashanshu += 2;
                youyongshu += 2;
                Drawable drawable = getDrawable(R.drawable.tianqi_zhongyu);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "大雨": {
                tiaoshengshu += 21;
                paobushu += 2;
                buxingshu += 3;
                qixingshu += 2;
                pashanshu += 1;
                youyongshu += 1;
                Drawable drawable = getDrawable(R.drawable.tianqi_dayu);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "暴雨": {
                tiaoshengshu += 18;
                paobushu += 1;
                buxingshu += 1;
                qixingshu += 1;
                pashanshu += 1;
                youyongshu += 1;
                Drawable drawable = getDrawable(R.drawable.tianqi_baoyu);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "大暴雨": {
                tiaoshengshu += 16;
                paobushu += 0;
                buxingshu += 2;
                qixingshu += 0;
                pashanshu += 0;
                youyongshu += 0;
                Drawable drawable = getDrawable(R.drawable.tianqi_dabaoyu);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "特大暴雨": {
                tiaoshengshu += 8;
                paobushu += 0;
                buxingshu += 0;
                qixingshu += 0;
                pashanshu += 0;
                youyongshu += 0;
                Drawable drawable = getDrawable(R.drawable.tianqi_tedabaoyu);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "阵雪": {
                tiaoshengshu += 22;
                paobushu += 12;
                buxingshu += 18;
                qixingshu += 8;
                pashanshu += 10;
                youyongshu += 8;
                Drawable drawable = getDrawable(R.drawable.tianqi_zhenxue);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "小雪": {
                tiaoshengshu += 22;
                paobushu += 12;
                buxingshu += 15;
                qixingshu += 7;
                pashanshu += 9;
                youyongshu += 7;
                Drawable drawable = getDrawable(R.drawable.tianqi_xiaoxue);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "中雪": {
                tiaoshengshu += 22;
                paobushu += 8;
                buxingshu += 10;
                qixingshu += 6;
                pashanshu += 7;
                youyongshu += 3;
                Drawable drawable = getDrawable(R.drawable.tianqi_zhongxue);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "大雪": {
                tiaoshengshu += 22;
                paobushu += 3;
                buxingshu += 8;
                qixingshu += 4;
                pashanshu += 3;
                youyongshu += 2;
                Drawable drawable = getDrawable(R.drawable.tianqi_daxue);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "暴雪": {
                tiaoshengshu += 18;
                paobushu += 2;
                buxingshu += 3;
                qixingshu += 1;
                pashanshu += 1;
                youyongshu += 1;
                Drawable drawable = getDrawable(R.drawable.tianqi_baoxue);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "雾": {
                tiaoshengshu += 16;
                paobushu += 10;
                buxingshu += 12;
                qixingshu += 8;
                pashanshu += 10;
                youyongshu += 10;
                Drawable drawable = getDrawable(R.drawable.tianqi_wu);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "冻雨": {
                tiaoshengshu += 18;
                paobushu += 8;
                buxingshu += 10;
                qixingshu += 5;
                pashanshu += 5;
                youyongshu += 7;
                Drawable drawable = getDrawable(R.drawable.tianqi_dongyu);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "沙尘暴": {
                tiaoshengshu += 8;
                paobushu += 1;
                buxingshu += 1;
                qixingshu += 1;
                pashanshu += 1;
                youyongshu += 1;
                Drawable drawable = getDrawable(R.drawable.tianqi_shachenbao);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "小雨-中雨": {
                tiaoshengshu += 20;
                paobushu += 3;
                buxingshu += 5;
                qixingshu += 3;
                pashanshu += 2;
                youyongshu += 2;
                Drawable drawable = getDrawable(R.drawable.tianqi_xiaoyu);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "中雨-大雨": {
                tiaoshengshu += 16;
                paobushu += 2;
                buxingshu += 3;
                qixingshu += 2;
                pashanshu += 1;
                youyongshu += 1;
                Drawable drawable = getDrawable(R.drawable.tianqi_zhongyu);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "大雨-暴雨": {
                tiaoshengshu += 14;
                paobushu += 1;
                buxingshu += 2;
                qixingshu += 1;
                pashanshu += 1;
                youyongshu += 1;
                Drawable drawable = getDrawable(R.drawable.tianqi_dayu);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "暴雨-大暴雨": {
                tiaoshengshu += 12;
                paobushu += 0;
                buxingshu += 2;
                qixingshu += 0;
                pashanshu += 0;
                youyongshu += 0;
                Drawable drawable = getDrawable(R.drawable.tianqi_baoyu);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "大暴雨-特大暴雨": {
                tiaoshengshu += 10;
                paobushu += 0;
                buxingshu += 0;
                qixingshu += 0;
                pashanshu += 0;
                youyongshu += 0;
                Drawable drawable = getDrawable(R.drawable.tianqi_dabaoyu);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "小雪-中雪": {
                tiaoshengshu += 20;
                paobushu += 6;
                buxingshu += 8;
                qixingshu += 5;
                pashanshu += 6;
                youyongshu += 3;
                Drawable drawable = getDrawable(R.drawable.tianqi_xiaoxue);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "中雪-大雪": {
                tiaoshengshu += 16;
                paobushu += 4;
                buxingshu += 6;
                qixingshu += 3;
                pashanshu += 4;
                youyongshu += 1;
                Drawable drawable = getDrawable(R.drawable.tianqi_zhongxue);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "大雪-暴雪": {
                tiaoshengshu += 14;
                paobushu += 1;
                buxingshu += 1;
                qixingshu += 1;
                pashanshu += 1;
                youyongshu += 1;
                Drawable drawable = getDrawable(R.drawable.tianqi_daxue);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "浮尘": {
                tiaoshengshu += 12;
                paobushu += 8;
                buxingshu += 10;
                qixingshu += 10;
                pashanshu += 8;
                youyongshu += 10;
                Drawable drawable = getDrawable(R.drawable.tianqi_fuchen);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "扬沙": {
                tiaoshengshu += 12;
                paobushu += 6;
                buxingshu += 8;
                qixingshu += 8;
                pashanshu += 8;
                youyongshu += 7;
                Drawable drawable = getDrawable(R.drawable.tianqi_yangsha);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "强沙尘暴": {
                tiaoshengshu += 12;
                paobushu += 0;
                buxingshu += 1;
                qixingshu += 1;
                pashanshu += 0;
                youyongshu += 0;
                Drawable drawable = getDrawable(R.drawable.tianqi_qiangshachenbao);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "飑": {
                tiaoshengshu += 10;
                paobushu += 1;
                buxingshu += 1;
                qixingshu += 1;
                pashanshu += 1;
                youyongshu += 1;
                Drawable drawable = getDrawable(R.drawable.tianqi_bao);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "龙卷风": {
                tiaoshengshu += 12;
                paobushu += 0;
                buxingshu += 0;
                qixingshu += 0;
                pashanshu += 0;
                youyongshu += 0;
                Drawable drawable = getDrawable(R.drawable.tianqi_longjuanfen);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "弱高吹雪": {
                tiaoshengshu += 18;
                paobushu += 8;
                buxingshu += 12;
                qixingshu += 8;
                pashanshu += 8;
                youyongshu += 6;
                Drawable drawable = getDrawable(R.drawable.tianqi_ruogaochuixue);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "轻霾": {
                tiaoshengshu += 22;
                paobushu += 16;
                buxingshu += 18;
                qixingshu += 14;
                pashanshu += 13;
                youyongshu += 15;
                Drawable drawable = getDrawable(R.drawable.tianqi_mai);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            case "霾": {
                tiaoshengshu += 22;
                paobushu += 12;
                buxingshu += 15;
                qixingshu += 13;
                pashanshu += 10;
                youyongshu += 13;
                Drawable drawable = getDrawable(R.drawable.tianqi_zhongmai);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
            default: {
                Drawable drawable = getDrawable(R.drawable.tianqi_qing);
                tianqiTubiao.setImageDrawable(drawable);
                break;
            }
        }
        if (Integer.parseInt(wendu) <= 10){
            tiaoshengshu += 16;
            paobushu += 18;
            buxingshu += 22;
            qixingshu += 15;
            pashanshu += 18;
            youyongshu += 10;
        }else if (Integer.parseInt(wendu) <= 20){
            tiaoshengshu += 23;
            paobushu += 22;
            buxingshu += 24;
            qixingshu += 20;
            pashanshu += 20;
            youyongshu += 18;
        }else if (Integer.parseInt(wendu) <= 28){
            tiaoshengshu += 24;
            paobushu += 25;
            buxingshu += 25;
            qixingshu += 24;
            pashanshu += 23;
            youyongshu += 22;
        }else if (Integer.parseInt(wendu) <= 35){
            tiaoshengshu += 23;
            paobushu += 20;
            buxingshu += 22;
            qixingshu += 15;
            pashanshu += 20;
            youyongshu += 25;
        }else{
            tiaoshengshu += 18;
            paobushu += 18;
            buxingshu += 20;
            qixingshu += 20;
            pashanshu += 21;
            youyongshu += 23;
        }
        if (fengli.equals("≤3")){
            fengli = "2";
        }
        if (Integer.parseInt(fengli) <= 5){
            tiaoshengshu += 20;
            paobushu += 20;
            buxingshu += 22;
            qixingshu += 23;
            pashanshu += 22;
            youyongshu += 22;
        }else if (Integer.parseInt(fengli) <= 7){
            tiaoshengshu += 20;
            paobushu += 18;
            buxingshu += 20;
            qixingshu += 16;
            pashanshu += 19;
            youyongshu += 18;
        }else {
            tiaoshengshu += 20;
            paobushu += 6;
            buxingshu += 8;
            qixingshu += 4;
            pashanshu += 8;
            youyongshu += 3;
        }

        if (Integer.parseInt(shidu) < 30){
            tiaoshengshu += 20;
            paobushu += 20;
            buxingshu += 22;
            qixingshu += 20;
            pashanshu += 21;
            youyongshu += 22;
        }else if (Integer.parseInt(shidu) < 50){
            tiaoshengshu += 20;
            paobushu += 18;
            buxingshu += 20;
            qixingshu += 17;
            pashanshu += 18;
            youyongshu += 20;
        }else {
            tiaoshengshu += 18;
            paobushu += 8;
            buxingshu += 10;
            qixingshu += 8;
            pashanshu += 6;
            youyongshu += 6;
        }

        RadarData resultLineData = getRadarData();
        showChart();
    }

    /**
     * gv
     * 初始化数据
     * count 表示坐标点个数，range表示等下y值生成的范围
     */
    public RadarData getRadarData() {
        x.add("跑步");
        x.add("爬山");
        x.add("游泳");
        x.add("跳绳");
        x.add("步行");
        x.add("骑行");

        System.out.println("跑步指数：" + paobushu);
        y.add(new RadarEntry(paobushu, 0));
        y.add(new RadarEntry(pashanshu, 1));
        y.add(new RadarEntry(youyongshu, 2));
        y.add(new RadarEntry(tiaoshengshu, 3));
        y.add(new RadarEntry(buxingshu, 4));
        y.add(new RadarEntry(qixingshu, 5));
        RadarDataSet radarDataSet = new RadarDataSet(y, "雷达图");//y轴数据集合
        radarDataSet.setLineWidth(1f);//线宽
        radarDataSet.setColor(Color.RED);//现实颜色
        radarDataSet.setHighLightColor(Color.WHITE);//高度线的颜色


        radarDataSets.add(radarDataSet);

        radarData = new RadarData(radarDataSet);

        return radarData;
    }

    /**
     * 设置样式
     */
    public void showChart() {
//        radarChart.setDrawBorders(false);//是否添加边框
        radarChart.setDescription("");//数据描述
        radarChart.setNoDataTextDescription("我需要数据");//没数据显示
//        radarChart.setDrawGridBackground(true);//是否显示表格颜色
        radarChart.setBackgroundColor(Color.TRANSPARENT);//背景颜色
        radarChart.getYAxis().setEnabled(false);
        XAxis xAxis = radarChart.getXAxis();
        xAxis.setLabelCount(y.size() );//X轴显示几个值
        //自定义增加X轴的值
        xAxis.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return x.get((int) value % x.size());
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
        radarChart.setData(radarData);//设置数据

        Legend legend = radarChart.getLegend();//设置比例图片标示，就是那一组Y的value
        legend.setForm(Legend.LegendForm.SQUARE);//样式
        legend.setFormSize(6f);//字体
        legend.setTextColor(Color.WHITE);//设置颜色
        radarChart.animateY(2000);//X轴的动画
    }


}
