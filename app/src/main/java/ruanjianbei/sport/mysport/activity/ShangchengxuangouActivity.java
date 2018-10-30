package ruanjianbei.sport.mysport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

import android.support.v7.widget.RecyclerView;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.adapter.RcShangChengXuanGouAdapter;
import ruanjianbei.sport.mysport.bean.LiWuBean;
import ruanjianbei.sport.mysport.util.HttpUtils;
import ruanjianbei.sport.mysport.util.MyApplication;

public class ShangchengxuangouActivity extends Activity {

    private ImageView shangchengxuangouFanhui;
    private PullToRefreshLayout shangchengxuangouPu;
    private RecyclerView shangchengxuangouRc;
    private RcShangChengXuanGouAdapter adapter;
    private List<LiWuBean> liWuBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shangchengxuangou);

        shangchengxuangouFanhui = (ImageView) findViewById(R.id.shangchengxuangou_fanhui);
        shangchengxuangouPu = (PullToRefreshLayout) findViewById(R.id.shangchengxuangou_pu);
        shangchengxuangouRc = (RecyclerView) findViewById(R.id.shangchengxuangou_rc);
        shangchengxuangouPu.setCanLoadMore(false);
        shangchengxuangouPu.setCanRefresh(false);


        shangchengxuangouFanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        HttpUtils.post(MyApplication.getLiWuurl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                System.out.println("商品信息：" + responseBody + "萨达：" + response.toString());
                Gson gson = new Gson();
                Type type = new TypeToken<List<LiWuBean>>() {
                }.getType();
                liWuBeanList = gson.fromJson(responseBody, type);
                if (!liWuBeanList.isEmpty()) {
                    System.out.println("商品List的数据：" + liWuBeanList.get(0).getName());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.replaceAll(liWuBeanList);
                    }
                });

            }
        }, null);

        adapter = new RcShangChengXuanGouAdapter(liWuBeanList, this);
        shangchengxuangouRc.setLayoutManager(new GridLayoutManager(this, 2));
        shangchengxuangouRc.setAdapter(adapter);

        adapter.setItemClickListener(new RcShangChengXuanGouAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int o) {
                Intent intent = new Intent(ShangchengxuangouActivity.this, ShangpingxiangqingActivity.class);
                intent.putExtra("bean", (Serializable) liWuBeanList.get(o));
                startActivity(intent);
            }
        });
    }

}
