package ruanjianbei.sport.mysport.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.adapter.ChatAdapter;
import ruanjianbei.sport.mysport.bean.MessageEvent;
import ruanjianbei.sport.mysport.bean.UserIndividualInfoBean;
import ruanjianbei.sport.mysport.util.Listener;
import ruanjianbei.sport.mysport.util.MyApplication;
import ruanjianbei.sport.mysport.util.Xiaoxilei;

public class XiaoxiActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText editText;
    private Button button;
    final ChatAdapter adapter = new ChatAdapter();
    private List<String> list = new ArrayList<>();
    private String otherName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#3cc6b3"));
        setContentView(R.layout.activity_xiaoxi);
        Toolbar tb = (Toolbar) findViewById(R.id.xiaoxi_toolbar);
        tb.setBackgroundColor(Color.parseColor("#3cc6b3"));
        setSupportActionBar(tb);
        ImageView fanhui = findViewById(R.id.fanhui_xiaoxi);

        Intent intent = getIntent();
        UserIndividualInfoBean userIndividualInfoBean = (UserIndividualInfoBean)intent.getSerializableExtra("otherId");
        final String otherId = String.valueOf(userIndividualInfoBean.getId());
        final String userName = MyApplication.user.getVname();
        otherName =userIndividualInfoBean.getVname();
        list = (List<String>) intent.getSerializableExtra("xiaoxi");

        recyclerView = findViewById(R.id.rc_xiaoxi);
        recyclerView.setHasFixedSize(true);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        editText = findViewById(R.id.ed_xiaoxi);
        button = findViewById(R.id.bt_send);
        recyclerView.setAdapter(adapter);



        final LinearLayout tijiaoLayout = findViewById(R.id.xiaoxi_se);
        final int[] top = new int[1];
        top[0] = -1;
        ViewTreeObserver vto = tijiaoLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (top[0] == -1) {
                    top[0] = tijiaoLayout.getTop();
                }
                if (top[0] != tijiaoLayout.getTop()) {
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                    top[0] = tijiaoLayout.getTop();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                final String xx = editText.getText().toString();
                if (!xx.equals("")) {
                    ArrayList<Map<String, String>> list1 = new ArrayList<>();
                    Map<String, String> map = new HashMap<>();
                    map.put("leixing", "1");
                    map.put("xiaoxi", xx);
                    map.put("name", userName);
                    list1.add(map);
                    adapter.addAll(list1);
                    editText.getText().clear();
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                    if ( Xiaoxilei.webSocket != null) {
                        Xiaoxilei.webSocket.send(otherId+"pIN1j0fd"+map.get("xiaoxi"));
                    }
                } else {
                    Toast toast;
                    toast = Toast.makeText(XiaoxiActivity.this, "请输入消息。", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
        if (list != null) {
            ArrayList<Map<String, String>> list1 = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                System.out.println("傻缪缪：  " + list.get(i) );
                Map<String, String> map = new HashMap<>();
                map.put("leixing", "2");
                map.put("xiaoxi", list.get(i));
                map.put("name", otherName);
                list1.add(map);
            }
            adapter.addAll(list1);
        }
    }

    //接收消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        System.out.println("消息界面获取的消息为： " + messageEvent.getMessage());
        String a[] = messageEvent.getMessage().split("pIN1j0fd");
        ArrayList<Map<String, String>> list1 = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("leixing", "2");
        map.put("xiaoxi", a[1]);
        if (otherName != null){
            map.put("name", otherName);
        }else {
            map.put("name", a[0]);
        }
        list1.add(map);
        adapter.addAll(list1);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Xiaoxilei.otherId = -1;
        Xiaoxilei.xiaoxi.clear();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
