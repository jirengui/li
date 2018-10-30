package ruanjianbei.sport.mysport.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.activity.DuanlianjihuaActivity;
import ruanjianbei.sport.mysport.activity.GerenjiemianActivity;
import ruanjianbei.sport.mysport.activity.GuankanActivity;
import ruanjianbei.sport.mysport.activity.HaoyouliebiaoActivity;
import ruanjianbei.sport.mysport.activity.JibuqiActivity;
import ruanjianbei.sport.mysport.activity.LoginActivity;
import ruanjianbei.sport.mysport.activity.MainActivity;
import ruanjianbei.sport.mysport.activity.PaimingActivity;
import ruanjianbei.sport.mysport.activity.ShentisuzhiActivity;
import ruanjianbei.sport.mysport.activity.ShezhiActivity;
import ruanjianbei.sport.mysport.activity.WodedingdanActivity;
import ruanjianbei.sport.mysport.activity.ZhiboActivity;
import ruanjianbei.sport.mysport.bean.UserIndividualInfoBean;
import ruanjianbei.sport.mysport.bean.MessageEvent;
import ruanjianbei.sport.mysport.transition.ZhaiduiActivity;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.Listener;
import ruanjianbei.sport.mysport.util.MyApplication;
import ruanjianbei.sport.mysport.util.Xiaoxilei;

import static android.app.Activity.RESULT_OK;

public class PersonFragment extends Fragment implements View.OnClickListener {

    private ImageView touxiang;
    private TextView name;
    private TextView school;
    private TextView xueqimubiao;
    private TextView yipaolicheng;
    private TextView jiruchengji;
//    private TextView haoyouliebiao;
    private TextView xunlianjihua;
    private TextView wodedingdan;
    private TextView shentisuzhi;
    private TextView wodesaishi;
    private TextView jibenshezhi;
    private TextView guanzhu;
    private TextView fensi;
    private TextView dengji;
    private TextView paiming;
    private UserIndividualInfoBean userIndividualInfoBean;
    private String uri = null;
    private int userId = -1;
    private Uri originalUri = null;
    private HashMap<String, String> map = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.person, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        iniv(view);
        setData();
    }

    private void iniv(View view) {
        touxiang = (ImageView) view.findViewById(R.id.touxiang);
        name = (TextView) view.findViewById(R.id.name);
        school = (TextView) view.findViewById(R.id.school);
        xueqimubiao = (TextView) view.findViewById(R.id.xueqimubiao);
        yipaolicheng = (TextView) view.findViewById(R.id.yipaolicheng);
        jiruchengji = (TextView) view.findViewById(R.id.jiruchengji);
//        haoyouliebiao = (TextView) view.findViewById(R.id.haoyouliebiao);
        xunlianjihua = (TextView) view.findViewById(R.id.xunlianjihua);
        wodedingdan = (TextView) view.findViewById(R.id.wodedingdan);
        shentisuzhi = (TextView) view.findViewById(R.id.shentisuzhi);
        wodesaishi = (TextView) view.findViewById(R.id.wodesaishi);
        jibenshezhi = (TextView) view.findViewById(R.id.jibenshezhi);
        paiming = (TextView) view.findViewById(R.id.paiming);
        guanzhu = (TextView) view.findViewById(R.id.guanzhushu);
        fensi = (TextView) view.findViewById(R.id.fensishu);
        dengji = (TextView) view.findViewById(R.id.dengji);


//        haoyouliebiao.setOnClickListener(this);
        xunlianjihua.setOnClickListener(this);
        touxiang.setOnClickListener(this);
        wodedingdan.setOnClickListener(this);
        name.setOnClickListener(this);
        shentisuzhi.setOnClickListener(this);
        wodesaishi.setOnClickListener(this);
        jibenshezhi.setOnClickListener(this);
    }

    private void setData() {
        userIndividualInfoBean = MyApplication.user;
        System.out.println("个人信息：" + userIndividualInfoBean );
        name.setText(userIndividualInfoBean.getVname());
        school.setText(userIndividualInfoBean.getSchool());
        userId = userIndividualInfoBean.getId();
        Picasso.with(getContext())
                .load(MyApplication.imageUri + userIndividualInfoBean.getTouxiang())
                .into(touxiang);
        paiming.setText(String.valueOf(userIndividualInfoBean.getZongpaiming()));
        yipaolicheng.setText(String.valueOf(userIndividualInfoBean.getZonglicheng()));
        xueqimubiao.setText(String.valueOf(userIndividualInfoBean.getXueqilicheng()));
        if (userIndividualInfoBean.getGidcount() == -1) {
            userIndividualInfoBean.setGidcount(0);
        }
        if (userIndividualInfoBean.getFidcount() == -1) {
            userIndividualInfoBean.setFidcount(0);
        }
        guanzhu.setText(String.valueOf(userIndividualInfoBean.getGidcount()));
        fensi.setText(String.valueOf(userIndividualInfoBean.getFidcount()));
        dengji.setText(String.valueOf(userIndividualInfoBean.getDengji()));
        jiruchengji.setText(String.valueOf(userIndividualInfoBean.getLicheng()));


    }


    //接收消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        System.out.println("个人界面获取的消息为： " + messageEvent.getMessage());
        Gson gson = new Gson();
        map = gson.fromJson(messageEvent.getMessage(), HashMap.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.name:
                //TODO implement
                startActivity(new Intent(getContext(), GerenjiemianActivity.class));
                break;
            case R.id.xunlianjihua:
                startActivity(new Intent(getContext(), DuanlianjihuaActivity.class));
                break;
            case R.id.touxiang:
//                if (userId != -1) {
//                    pickPhoto();
//                } else {
                startActivity(new Intent(getContext(), GerenjiemianActivity.class));
                break;
            case R.id.wodedingdan:
//                startActivity(new Intent(getContext(), GuankanActivity.class));
                startActivity(new Intent(getContext(), WodedingdanActivity.class));
                break;
            case R.id.shentisuzhi:
//                startActivity(new Intent(getContext(), ZhiboActivity.class));
                startActivity(new Intent(getContext(), ShentisuzhiActivity.class));
                break;
            case R.id.wodesaishi:
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
                //刷新个人信息
//                        Map<String, String> map = new HashMap<>();
//                        map.put("uid", String.valueOf(MyApplication.user.getUserId()));
//                        HttpUtils.post(MyApplication.getInformationurl, new Callback() {
//                            @Override
//                            public void onFailure(Call call, IOException e) {
//
//                            }
//
//                            @Override
//                            public void onResponse(Call call, Response response) throws IOException {
//                                String responseBody = response.body().string();
//                                System.out.println("更新信息：" + responseBody);
//                                Gson gson = new Gson();
//                                MyApplication.user = gson.fromJson(responseBody, UserIndividualInfoBean.class);
//                                //发送登录成功的消息
//                                Message msg = handler.obtainMessage();
//                                msg.what = 112;
//                                handler.sendMessage(msg);
//                            }
//                        },map);
//                    }
//                }).start();
                startActivity(new Intent(getContext(), PaimingActivity.class));
                break;
            case R.id.jibenshezhi:
//                String gongwangip = "47.95.123.168:8080";
//                String juyuip = "192.168.43.44:8080";
//                if (MyApplication.ip.equals(gongwangip)) {
//                    MyApplication.xiugaiIP(juyuip);
//                    Toast.makeText(getContext(), "已切换到局域网", Toast.LENGTH_LONG).show();
//                    System.out.println("ip地址为：" + MyApplication.ip + "用户IP：" + MyApplication.addguanzhuurl);
//                } else if (MyApplication.ip.equals(juyuip)) {
//                    MyApplication.xiugaiIP(gongwangip);
//                    Toast.makeText(getContext(), "已切换到公网", Toast.LENGTH_LONG).show();
//                    System.out.println("ip地址为：" + MyApplication.ip + "用户IP：" + MyApplication.addguanzhuurl);
//                }
//                Toast.makeText(getContext(), "敬请期待。", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getContext(), ShezhiActivity.class));
                break;
        }
    }

    //从相册中取图片
    private void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("返回来了。。" + resultCode + "数据" + data + "返回requestCode" + requestCode);
        if (resultCode != RESULT_OK) {        //此处的 RESULT_OK 是系统自定义得一个常量
            Log.e("TAG->onresult", "ActivityResult resultCode error");
            return;
        }
        // 此处的用于判断接收的Activity是不是你想要的那个
        if (requestCode == 2) {
            System.out.println("返回图片。。" + data.getData());
            originalUri = data.getData();        //获得图片的uri
            uri = getImagePathFromURI(originalUri);//华为屌
            if (uri == null) {
                uri = originalUri.getEncodedPath();
            }
            touxiang.setImageURI(originalUri);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File file = new File(uri);

                    HttpUtils.upload(String.valueOf(userId), file, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            System.out.println("回调：" + response.toString());
                        }
                    });
                }
            }).start();

        }
    }

    //正确的图片地址
    public String getImagePathFromURI(Uri uri) {
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        String path = null;
        if (cursor != null) {
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
            cursor.close();

            cursor = getContext().getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
            if (cursor != null) {
                cursor.moveToFirst();
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();
            }
        }
        return path;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
