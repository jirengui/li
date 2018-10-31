package ruanjianbei.sport.mysport.util;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ruanjianbei.sport.mysport.bean.PaoBuJieGuoBean;
import ruanjianbei.sport.mysport.bean.UserIndividualInfoBean;
import skin.support.SkinCompatManager;
import skin.support.app.SkinCardViewInflater;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;

/**
 * Created by li on 2018/4/9.
 */

public class MyApplication extends MultiDexApplication {

    public static Context context;
    public static UserIndividualInfoBean user = null;
    public static String city;
    public static String tianqi;
    public static String ip = "192.168.43.44:8080";
//    192.168.43.44 手机热点
    public static String imageUri = "http://" + ip + "/image/touxiang/";
    public static String url = "http://" + ip + "/imooo/relogin";    //登录
    public static String postfileUrl = "http://" + ip + "/imooo/postFile";
    public static String addaddshuoshuoUrl = "http://" + ip + "/imooo/addshuoshuo";
    public static String zhuceUrl = "http://" + ip + "/imooo/zhuce";
    public static String getshuoshuoUri = "http://" + ip + "/imooo/getshuoshuo";
    public static String setpinlunUri = "http://" + ip + "/imooo/pinglun";
    public static String getpinlunUri = "http://" + ip + "/imooo/getpinglun";
    public static String postFriendurl = "http://" + ip + "/imooo/postFriend";
    public static String addfriendurl = "http://" + ip + "/imooo/addfriend";
    public static String huifupinglunurl = "http://" + ip + "/imooo/huifupinglun";
    public static String gethuifuurl = "http://" + ip + "/imooo/gethuifu";
    public static String updatelichengurl = "http://" + ip + "/imooo/updatelicheng";
    public static String getInformationurl = "http://" + ip + "/imooo/getInformation";
    public static String getplanurl = "http://" + ip + "/imooo/getPlan";
    public static String insertguijiurl = "http://" + ip + "/imooo/insertguiji";
    public static String addguanzhuurl = "http://" + ip + "/imooo/addfensi";
    public static String getGuanzhuurl = "http://" + ip + "/imooo/testzhuanchu";
    public static String updatejifenurl = "http://" + ip + "/imooo/updatejifen";
    public static String getpaimingurl = "http://" + ip + "/imooo/getpaiming";
    public static String updateshebeiurl = "http://" + ip + "/imooo/updateshebei";
    public static String getguijiurl = "http://" + ip + "/imooo/getguiji";
    public static String getLiWuurl = "http://" + ip + "/imooo/getLiWu";
    public static String getSaiShiurl = "http://" + ip + "/imooo/getReSaiShi";
    public static String getZhiBourl = "http://" + ip + "/imooo/getZhiBo";
    public static String updatemima = "http://" + ip + "/imooo/updatemima";
    public static String getPhoneNumber = "http://" + ip + "/imooo/getPhoneNumber"; //判断手机号是否已经存在
    public static String getgonggao = "http://" + ip + "/imooo/getgonggao";
    public static String createTeam = "http://" + ip + "/imooo/createTeam";
    public static String getTeam = "http://" + ip + "/imooo/getTeam";
    public static String getIdInformation = "http://" + ip + "/imooo/getIdInformation";
    public static String getAboutTeam = "http://" + ip + "/imooo/getAboutTeam";
    public static String getShuoshuoById = "http://" + ip + "/imooo/getShuoshuoById";
    public static String deletePlan = "http://" + ip + "/imooo/deletePlan";
    public static String updatePlan = "http://" + ip + "/imooo/updatePlan";
    public static String finishPlan = "http://" + ip + "/imooo/finishPlan";
    public static String insertPlan = "http://" + ip + "/imooo/insertPlan";
    public static String duihuan = "http://" + ip + "/imooo/duihuan";
    public static String duihuanjilu = "http://" + ip + "/imooo/duihuanjilu";
    public static String insertSaiShi = "http://" + ip + "/imooo/insertSaiShi";
    public static String getyundongarea = "http://" + ip + "/imooo/getyundongarea";
    public static String baoming = "http://" + ip + "/imooo/baoming";



    public static int cishu = 0;
    public static int qiandaozhuangtai = -1;
    public static PaoBuJieGuoBean paoBuJieGuoBean;
    public static boolean isBijindian = false;
    public static String lujing = null;
    public static MySQLiteOpenHelper dbHelper;
    public static int yanzhengma = 1111;
    //修改IP
//    public static void xiugaiIP (String newip){
//        MyApplication.ip = newip;
//        imageUri = "http://"+ip+"/image/touxiang/";
//        url = "http://"+ ip +"/imooo/login";
//        postfileUrl = "http://"+ip+"/imooo/postFile";
//        addaddshuoshuoUrl = "http://"+ip+"/imooo/addshuoshuo";
//        zhuceUrl = "http://"+ip+"/imooo/zhuce";
//        getshuoshuoUri = "http://"+ip+"/imooo/getshuoshuo";
//        setpinlunUri = "http://"+ip+"/imooo/pinglun";
//        getpinlunUri = "http://"+ip+"/imooo/getpinglun";
//        postFriendurl = "http://"+ip+"/imooo/postFriend";
//        addfriendurl = "http://"+ip+"/imooo/addfriend";
//        huifupinglunurl = "http://"+ip+"/imooo/huifupinglun";
//        gethuifuurl = "http://"+ip+"/imooo/gethuifu";
//        updatelichengurl = "http://"+ip+"/imooo/updatelicheng";
//        getInformationurl = "http://"+ip+"/imooo/getInformation";
//        getplanurl = "http://"+ip+"/imooo/getplan";
//        insertguijiurl = "http://"+ip+"/imooo/insertguiji";
//        addguanzhuurl = "http://"+ip+"/imooo/addfensi";
//        getGuanzhuurl = "http://"+ip+"/imooo/testzhuanchu";
//        updatejifenurl = "http://"+ip+"/imooo/updatejifen";
//        getpaimingurl = "http://"+ip+"/imooo/getpaiming";
//        updateshebeiurl = "http://"+ip+"/imooo/updateshebei";
//        getguijiurl = "http://"+ip+"/imooo/getguiji";
//        getLiWuurl = "http://"+ip+"/imooo/getLiWu";
//        getSaiShiurl = "http://"+ip+"/imooo/getSaiShi";
//        getZhiBourl = "http://"+ip+"/imooo/getZhiBo";
//    }

    public static String getyanzhengma(){

        yanzhengma =(int)((Math.random()*9+1)*1000);
        System.out.println("验证码：" + yanzhengma);
        return String.valueOf(yanzhengma);
    }
    public static int chushihua(int userid) {
        user = null;
        cishu = 0;
        isBijindian = false;
        lujing = null;
        if (userid != -1) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.query("tb_information", null, "userid=" + userid, null, null, null, null);
            int i = cursor.getCount();
            System.out.println("初始化数据库里的数据：" + i);
            if (i >= 1) {
                user = new UserIndividualInfoBean();
                if (cursor.moveToFirst()) {
                    user.setAge(String.valueOf(cursor.getInt(cursor.getColumnIndex("age"))));
                    user.setGidcount(cursor.getInt(cursor.getColumnIndex("gid")));
                    user.setDengji(cursor.getString(cursor.getColumnIndex("dengji")));
                    user.setFidcount(cursor.getInt(cursor.getColumnIndex("fid")));
                    user.setId(cursor.getInt(cursor.getColumnIndex("userid")));
                    user.setJifen(cursor.getString(cursor.getColumnIndex("jifen")));
                    user.setLicheng(cursor.getString(cursor.getColumnIndex("licheng")));
                    user.setQuanxian(cursor.getInt(cursor.getColumnIndex("quanxian")));
                    user.setSchool(cursor.getString(cursor.getColumnIndex("school")));
                    user.setSex(cursor.getString(cursor.getColumnIndex("sex")));
                    user.setShebei(cursor.getString(cursor.getColumnIndex("shebei")));
                    user.setStautus(cursor.getInt(cursor.getColumnIndex("stautus")));
                    user.setTime(cursor.getString(cursor.getColumnIndex("time")));
                    user.setTouxiang(cursor.getString(cursor.getColumnIndex("touxiang")));
                    user.setTname(cursor.getString(cursor.getColumnIndex("tname")));
                    user.setVname(cursor.getString(cursor.getColumnIndex("vname")));
                    user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                    user.setXuehao(cursor.getString(cursor.getColumnIndex("xuehao")));
                    user.setXueqilicheng(cursor.getDouble(cursor.getColumnIndex("xueqilicheng")));
                    user.setZonglicheng(cursor.getString(cursor.getColumnIndex("zonglicheng")));
                    user.setZongpaiming(cursor.getInt(cursor.getColumnIndex("zongpaiming")));
                    user.setTeam(cursor.getString(cursor.getColumnIndex("zhandui")));
                    user.setTeamPermission(cursor.getString(cursor.getColumnIndex("zhanduizhiwu")));
                }
                cursor.close();
                return 1;
            }
        }

        return -1;
    }

    public void onCreate() {
        super.onCreate();
        //创建SQLiteOpenHelper实例
        dbHelper = new MySQLiteOpenHelper(this, "contact.db", null, 1);

        SkinCompatManager.withoutActivity(this)                         // 基础控件换肤初始化
                .setSkinAllActivityEnable(false)
                .addInflater(new SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
                .addInflater(new SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
                .addInflater(new SkinCardViewInflater())                // CardView v7 控件换肤初始化[可0选]
                .setSkinStatusBarColorEnable(true)                     // 关闭状态栏换肤，默认打开[可选]
                .setSkinWindowBackgroundEnable(true)                   // 关闭windowBackground换肤，默认打开[可选]
                .loadSkin();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }

    public static void update(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<>();
                map.put("uid", String.valueOf(user.getId()));
                HttpUtils.post(getIdInformation, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String s = response.body().string();
                        System.out.println("更新数据: " + s);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        Gson gson  = new Gson();
                        user = gson.fromJson(s, UserIndividualInfoBean.class);
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
                        db.update("tb_information", values, null, null);
                    }
                },map);
            }
        }).start();
    }
}
