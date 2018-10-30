package ruanjianbei.sport.mysport.activity;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.githang.statusbar.StatusBarCompat;


import java.text.SimpleDateFormat;
import java.util.Date;

import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.bean.UserIndividualInfoBean;
import ruanjianbei.sport.mysport.fragment.DongtaiFragment;
import ruanjianbei.sport.mysport.fragment.GuijijiluFragment;
import ruanjianbei.sport.mysport.fragment.PersonFragment;
import ruanjianbei.sport.mysport.fragment.SportFragment;
import ruanjianbei.sport.mysport.fragment.ZhibooFragment;
import ruanjianbei.sport.mysport.transition.ZhaiduiActivity;
import ruanjianbei.sport.mysport.util.MyApplication;
import ruanjianbei.sport.mysport.util.Xiaoxilei;
import skin.support.annotation.Skinable;

import static ruanjianbei.sport.mysport.util.MyApplication.dbHelper;
import static ruanjianbei.sport.mysport.util.MyApplication.user;

//每个需要换肤的界面都要加这个注解
@Skinable
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static Activity main;
    private Boolean isBlack = true;
    private ImageView im_yejian;
    private TextView tv_yejian;
    private ImageView im_shequ, im_zhandui, im_sport, im_guiji, im_wode;
    private TextView tv_shequ, tv_zhandui, tv_guiji, tv_wode;
    private Fragment sportFragment;
    private Fragment zhiBoFragment;
    private Fragment guijiFragment;
    private Fragment shequFragment;
    private Fragment personFragment;
    private Fragment currentFragment;
    private RelativeLayout shequLayout, zhanduiLayout, xiaoxiLayout, personlayout, yundongLayout;
//    private DrawerLayout drawerLayout;

    private int fragment_i = 0;
    private UserIndividualInfoBean UserIndividualInfoBean = null;
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
                    System.out.println("监听器：" + diLiWeiZhi_sheng);
                    MyApplication.city = diLiWeiZhi_shi;
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        //false为白色字体，ture为黑色字体
        StatusBarCompat.setLightStatusBar(getWindow(), true);
        setContentView(R.layout.content_main2);
        main = this;//可以在别的activity中关闭此Activity

        SharedPreferences pref = getSharedPreferences("name", MODE_PRIVATE);
        String qiandaoriqi = pref.getString(MyApplication.user.getId()+"qiandaoriqi","0");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        String dangqianshijian = simpleDateFormat.format(date);
        SharedPreferences.Editor editor = getSharedPreferences("name", MODE_PRIVATE).edit();
        editor.putInt("userId", MyApplication.user.getId());

        if (!dangqianshijian.equals(qiandaoriqi)){
            editor.putString(MyApplication.user.getId()+"qiandaoriqi", dangqianshijian);
            editor.putInt(MyApplication.user.getId()+"qiandaozhuangtai", -1);
            MyApplication.qiandaozhuangtai = -1;
        }else {
            MyApplication.qiandaozhuangtai = pref.getInt(MyApplication.user.getId()+"qiandaozhuangtai", -1);
        }
        editor.commit();
        add();
        initUI();
        initTab();
    }

//    private void changeStatusBarTextColor(boolean isBlack) {
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
//            if (isBlack) {
//                if (isMIUI()) {
//                    Class<? extends Window> clazz = MainActivity.this.getWindow().getClass();
//                    try {
//                        int darkModeFlag = 0;
//                        Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
//                        Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
//                        darkModeFlag = field.getInt(layoutParams);
//                        Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
//                        extraFlagField.invoke(MainActivity.this.getWindow(), true ? darkModeFlag : 0, darkModeFlag);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏黑色字体
//                }
//            } else {
//                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);//恢复状态栏白色字体
//            }
//        } else {
//            if (isBlack) {
//                if (isMIUI()) {
//                    Class<? extends Window> clazz = MainActivity.this.getWindow().getClass();
//                    try {
//                        int darkModeFlag = 0;
//                        Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
//                        Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
//                        darkModeFlag = field.getInt(layoutParams);
//                        Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
//                        extraFlagField.invoke(MainActivity.this.getWindow(), true ? darkModeFlag : 0, darkModeFlag);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    Window window = getWindow();
//                    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                }
//            } else {
//                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);//恢复状态栏白色字体
//            }
//        }
//    }

    private void add(){
        //获得一个可写的数据库的一个引用
        SQLiteDatabase db = MyApplication.dbHelper.getWritableDatabase();
        Cursor cursor = db.query("tb_information", null,"userid=" + MyApplication.user.getId(),null,null,null,null);
        int i  = cursor.getCount();
        System.out.println("数据库里的数据：" + i);
        cursor.close();
        if (i < 1) {
            //如果数据库里没有此条记录则增加
            ContentValues values = new ContentValues();
            values.put("userid", MyApplication.user.getId()); // KEY 是列名，vlaue 是该列的值
            values.put("username", MyApplication.user.getUsername());// KEY 是列名，vlaue 是该列的值
            values.put("password", "null");// KEY 是列名，vlaue 是该列的值
            values.put("name", MyApplication.user.getVname());// KEY 是列名，vlaue 是该列的值
            values.put("school", MyApplication.user.getSchool());// KEY 是列名，vlaue 是该列的值
            values.put("jifen", MyApplication.user.getJifen());// KEY 是列名，vlaue 是该列的值
            values.put("licheng", MyApplication.user.getLicheng());// KEY 是列名，vlaue 是该列的值
            values.put("day", "null");// KEY 是列名，vlaue 是该列的值
            values.put("sex", MyApplication.user.getSex());// KEY 是列名，vlaue 是该列的值
            values.put("touxiang", MyApplication.user.getTouxiang());// KEY 是列名，vlaue 是该列的值
            values.put("xueqilicheng", MyApplication.user.getXueqilicheng());// KEY 是列名，vlaue 是该列的值
            values.put("diqu", "null");// KEY 是列名，vlaue 是该列的值
            values.put("time", MyApplication.user.getTime());// KEY 是列名，vlaue 是该列的值
            values.put("stautus", MyApplication.user.getStautus());// KEY 是列名，vlaue 是该列的值
            values.put("zongpaiming", MyApplication.user.getZongpaiming());// KEY 是列名，vlaue 是该列的值
            values.put("shebei", MyApplication.user.getShebei());// KEY 是列名，vlaue 是该列的值
            values.put("tname", MyApplication.user.getTname());// KEY 是列名，vlaue 是该列的值
            values.put("vname", MyApplication.user.getVname());// KEY 是列名，vlaue 是该列的值
            values.put("quanxian", MyApplication.user.getQuanxian());// KEY 是列名，vlaue 是该列的值
            values.put("zonglicheng", MyApplication.user.getZonglicheng());// KEY 是列名，vlaue 是该列的值
            values.put("age", MyApplication.user.getAge());// KEY 是列名，vlaue 是该列的值
            values.put("fid", MyApplication.user.getFidcount());// KEY 是列名，vlaue 是该列的值
            values.put("gid", MyApplication.user.getGidcount());// KEY 是列名，vlaue 是该列的值
            values.put("xuehao", MyApplication.user.getXuehao());// KEY 是列名，vlaue 是该列的值
            values.put("dengji", MyApplication.user.getDengji());// KEY 是列名，vlaue 是该列的值
            values.put("zhandui", MyApplication.user.getTeam());

            values.put("zhanduizhiwu", MyApplication.user.getTeamPermission());
            // 参数一：表名，参数三，是插入的内容
            // 参数二：只要能保存 values中是有内容的，第二个参数可以忽略
            db.insert("tb_information", null, values);
        }else {
            MyApplication.update();
        }
    }
    private void initUI() {

        shequLayout = (RelativeLayout) findViewById(R.id.rl_shequ);
        zhanduiLayout = (RelativeLayout) findViewById(R.id.rl_zhandui);
        xiaoxiLayout = (RelativeLayout) findViewById(R.id.rl_guijijilu);
        personlayout = (RelativeLayout) findViewById(R.id.person);
        yundongLayout = (RelativeLayout) findViewById(R.id.rl_sport);
        im_guiji = findViewById(R.id.iv_xiaoxi);
        im_shequ = findViewById(R.id.iv_shequ);
        im_sport = findViewById(R.id.iv_sport);
        im_wode = findViewById(R.id.iv_my);
        im_zhandui = findViewById(R.id.iv_zhandui);
        tv_guiji = findViewById(R.id.tv_guiji);
        tv_shequ = findViewById(R.id.tv_shequ);
        tv_wode = findViewById(R.id.tv_wode);
        tv_zhandui = findViewById(R.id.tv_zhandui);
        yundongLayout.setOnClickListener(this);
        shequLayout.setOnClickListener((View.OnClickListener) this);
        zhanduiLayout.setOnClickListener((View.OnClickListener) this);
        xiaoxiLayout.setOnClickListener((View.OnClickListener) this);
        personlayout.setOnClickListener((View.OnClickListener) this);
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
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


    }

    private void setim() {
        im_zhandui.setImageResource(R.drawable.zhandui);
        im_wode.setImageResource(R.drawable.wode);
        im_shequ.setImageResource(R.drawable.shequ);
        im_guiji.setImageResource(R.drawable.guijijilu);
        tv_zhandui.setTextColor(Color.parseColor("#101010"));
        tv_wode.setTextColor(Color.parseColor("#101010"));
        tv_shequ.setTextColor(Color.parseColor("#101010"));
        tv_guiji.setTextColor(Color.parseColor("#101010"));
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_sport: // 运动
                //false为白色字体，ture为黑色字体
                setim();
                StatusBarCompat.setLightStatusBar(getWindow(), true);
                clickTab1Layout();
                break;
            case R.id.rl_shequ: // 社区
                if (MyApplication.user != null && MyApplication.user.getId() != -1) {
                    setim();
                    im_shequ.setImageResource(R.drawable.shequhou);
                    tv_shequ.setTextColor(Color.parseColor("#3dceba"));
                    //false为白色字体，ture为黑色字体
                    StatusBarCompat.setLightStatusBar(getWindow(), false);
                    clickTab3Layout();
                } else {
                    Toast.makeText(MainActivity.this, "请先登录。", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.rl_zhandui: //战队
                MyApplication.getyanzhengma();
                if (MyApplication.user != null && MyApplication.user.getId() != -1) {
                    setim();
                    tv_zhandui.setTextColor(Color.parseColor("#3dceba"));
                    im_zhandui.setImageResource(R.drawable.zhanduihou);
                    //false为白色字体，ture为黑色字体     黑色字体沉浸式，白色透明
                    StatusBarCompat.setLightStatusBar(getWindow(), true);
                    clickTab2Layout();
                } else {
                    Toast.makeText(MainActivity.this, "请先登录。", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.rl_guijijilu: // 轨迹记录

                //false为白色字体，ture为黑色字体
                if (MyApplication.user != null && MyApplication.user.getId() != -1) {
                    setim();
                    tv_guiji.setTextColor(Color.parseColor("#3dceba"));
                    im_guiji.setImageResource(R.drawable.guijihou);
                    StatusBarCompat.setLightStatusBar(getWindow(), false);
                    clickguiji();
                } else {
                    Toast.makeText(MainActivity.this, "请先登录。", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.person: // 我的
                setim();
                im_wode.setImageResource(R.drawable.wodehou);
                tv_wode.setTextColor(Color.parseColor("#3dceba"));
                //false为白色字体，ture为黑色字体
                StatusBarCompat.setLightStatusBar(getWindow(), false);
                clickTab4Layout();
                break;
//            case R.id.im_yejian:
//                if (tv_yejian.getText().toString().equals("夜间")) {
//                    // 后缀加载
//                    SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
//                    tv_yejian.setText("白天");
//                    changeStatusBarTextColor(false);
//                }else {
//                    // 恢复应用默认皮肤
//                    SkinCompatManager.getInstance().restoreDefaultTheme();
//                    tv_yejian.setText("夜间");
//                    changeStatusBarTextColor(true);
//                }
//                break;
//            case R.id.tv_yejian:
//                if (tv_yejian.getText().toString().equals("夜间")) {
//                    // 后缀加载
//                    SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
//                    tv_yejian.setText("白天");
//                    changeStatusBarTextColor(false);
//                }else {
//                    // 恢复应用默认皮肤
//                    SkinCompatManager.getInstance().restoreDefaultTheme();
//                    tv_yejian.setText("夜间");
//                    changeStatusBarTextColor(true);
//                }
//                break;
            default:
                break;
        }
    }

    private void clickTab1Layout() {
        if (sportFragment == null) {
            sportFragment = new SportFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), sportFragment);
    }

    private void initTab() {
        if (sportFragment == null) {
            sportFragment = new SportFragment();
        }

        if (!sportFragment.isAdded()) {
            // 提交事务
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_layout, sportFragment).commit();
            // 记录当前Fragment
            currentFragment = sportFragment;
        }
//        Intent intent = getIntent();
//        if (intent  != null) {
//            if (intent.getIntExtra("ss",-1) == 2) {
//                if (MyApplication.user != null && MyApplication.user.getUserId() != -1) {
//                    setim();
//                    im_shequ.setImageResource(R.drawable.shequhou);
//                    tv_shequ.setTextColor(Color.parseColor("#3dceba"));
//                    //false为白色字体，ture为黑色字体
//                    StatusBarCompat.setLightStatusBar(getWindow(), true
//                    );
//
//                    clickTab3Layout();
//                } else {
//                    Toast.makeText(MainActivity.this, "请先登录。", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
        if (fragment_i != 0) {
            switch (fragment_i) {
                case 1:
                    clickTab1Layout();
                    break;
                case 2:
                    clickTab2Layout();
                    break;
                case 3:
                    clickTab3Layout();
                    break;
                case 4:
                    clickTab4Layout();
                    break;
                default:
                    break;
            }

        }
    }

    /**
     * 点击第二个tab
     */
    private void clickTab2Layout() {
        if (zhiBoFragment == null) {
            zhiBoFragment = new ZhaiduiActivity();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), zhiBoFragment);
    }

    /**
     * 点击第二个tab
     */
    private void clickguiji() {
        if (guijiFragment == null) {
            guijiFragment = new GuijijiluFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), guijiFragment);
    }

    /**
     * 点击第三个tab
     */
    private void clickTab3Layout() {
        if (shequFragment == null) {
            shequFragment = new DongtaiFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), shequFragment);
    }

    private void clickTab4Layout() {
        if (personFragment == null) {
            personFragment = new PersonFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), personFragment);
    }

    /**
     * 添加或者显示碎片
     *
     * @param transaction
     * @param fragment
     */
    private void addOrShowFragment(FragmentTransaction transaction,
                                   Fragment fragment) {
        if (currentFragment == fragment)
            return;

        if (!fragment.isAdded()) { // 如果当前fragment未被添加，则添加到Fragment管理器中
            transaction.hide(currentFragment)
                    .add(R.id.content_layout, fragment).commit();
        } else {
            transaction.hide(currentFragment).show(fragment).commit();
        }

        currentFragment = fragment;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭聊天链接
        if (Xiaoxilei.webSocket != null) {
            Xiaoxilei.webSocket.close(1000, "正常退出软件");
        }
    }



}
