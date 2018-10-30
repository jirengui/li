package ruanjianbei.sport.mysport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.bean.PlanBean;
import ruanjianbei.sport.mysport.bean.PlanBean1;
import ruanjianbei.sport.mysport.fragment.MyDialogFrament;
import ruanjianbei.sport.mysport.util.TimeUtil;

public class XiugaiActivity extends Activity implements View.OnClickListener, MyDialogFrament.MyDialogFragment_Listener {

    private Toolbar xiugaijihua;
    private ImageView xiugaiFanhui;
    private ImageView xiugaiXuanzhongyundong;
    private ImageView xiugaiPaobu;
    private ImageView xiugaiBuxing;
    private ImageView xiugaiYouyong;
    private ImageView xiugaiQixing;
    private ImageView xiugaiTiaosheng;
    private ImageView xiugaiPashan;
    private TextView xiugaiStarttime;
    private TextView xiugaiEndtime;
    private TextView xiugaiYundong;
    private TextView xiugaiWancheng;
    private TextView xiugaiBiaoti;

    private List<PlanBean1> objects = new ArrayList<>();
    private List<PlanBean> planBeanList = new ArrayList<>();
    private List<String> riqiq = new ArrayList<>();
    private int position = 0;
    private String xiaoshi, fenzhong, riqi, starttime, endtime;
    private String jiangeshijian;
    final String[] leixing = new String[1];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xiugai);

        iniv();
        setDate();

    }

    private void iniv() {
        xiugaiBiaoti = findViewById(R.id.xiugai_biaoti);
        xiugaijihua = (Toolbar) findViewById(R.id.xiugaijihua);
        xiugaiFanhui = (ImageView) findViewById(R.id.xiugai_fanhui);
        xiugaiXuanzhongyundong = (ImageView) findViewById(R.id.xiugai_xuanzhongyundong);
        xiugaiPaobu = (ImageView) findViewById(R.id.xiugai_paobu);
        xiugaiBuxing = (ImageView) findViewById(R.id.xiugai_buxing);
        xiugaiYouyong = (ImageView) findViewById(R.id.xiugai_youyong);
        xiugaiQixing = (ImageView) findViewById(R.id.xiugai_qixing);
        xiugaiTiaosheng = (ImageView) findViewById(R.id.xiugai_tiaosheng);
        xiugaiPashan = (ImageView) findViewById(R.id.xiugai_pashan);
        xiugaiStarttime = (TextView) findViewById(R.id.xiugai_starttime);
        xiugaiEndtime = (TextView) findViewById(R.id.xiugai_endtime);
        xiugaiYundong = (TextView) findViewById(R.id.xiugai_yundong);
        xiugaiWancheng = (TextView) findViewById(R.id.xiugai_wancheng);

        xiugaiFanhui.setOnClickListener(this);
    }

    private void setDate() {

        final Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bu");
        if (bundle != null) {
            planBeanList = (List<PlanBean>) bundle.getSerializable("planBeanList");
            objects = (List<PlanBean1>) bundle.getSerializable("objects");
            position = bundle.getInt("position");
        }
        riqiq = TimeUtil.getBetweenDates(planBeanList.get(0).getTime(), planBeanList.get(planBeanList.size()-1).getTime());
        if (position == objects.size()) {
            jiangeshijian = objects.get(position-1).getShijian();
            xiugaiBiaoti.setText("添加计划");
            riqi = objects.get(position - 1).getTime();
            starttime = objects.get(position - 1).getStarttime();
            endtime = objects.get(position - 1).getEndtime();
            xiugaiStarttime.setText(objects.get(position - 1).getTime() + " " + objects.get(position - 1).getStarttime());
            xiugaiEndtime.setText(objects.get(position - 1).getTime() + " " + objects.get(position - 1).getEndtime());
            xiugaiYundong.setText(objects.get(position - 1).getPlan() + objects.get(position - 1).getShijian() + "分钟");
            switch (objects.get(position - 1).getPlan()) {
                case "游泳":
                    xiugaiXuanzhongyundong.setImageResource(R.drawable.youyonglv);
                    xiugaiYouyong.setImageResource(R.drawable.youyonglv);
                    leixing[0] = "游泳";
                    break;
                case "跑步":
                    xiugaiXuanzhongyundong.setImageResource(R.drawable.paobulv);
                    xiugaiPaobu.setImageResource(R.drawable.paobulv);
                    leixing[0] = "跑步";
                    break;
                case "骑行":
                    xiugaiXuanzhongyundong.setImageResource(R.drawable.qixinglv);
                    xiugaiQixing.setImageResource(R.drawable.qixinglv);
                    leixing[0] = "骑行";
                    break;
                case "跳绳":
                    xiugaiXuanzhongyundong.setImageResource(R.drawable.tiaoshenglv);
                    xiugaiTiaosheng.setImageResource(R.drawable.tiaoshenglv);
                    leixing[0] = "跳绳";
                    break;
                case "爬山":
                    xiugaiXuanzhongyundong.setImageResource(R.drawable.pashanlv);
                    xiugaiPashan.setImageResource(R.drawable.pashanlv);
                    leixing[0] = "爬山";
                    break;
                case "步行":
                    xiugaiXuanzhongyundong.setImageResource(R.drawable.buxinglv);
                    xiugaiBuxing.setImageResource(R.drawable.buxinglv);
                    leixing[0] = "步行";
                    break;
            }

        } else {
//            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
//            try {
//                Date date = sdf1.parse(objects.get(position).getTime());
//                yuanshiriqi = String.valueOf(date.getDate());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
            jiangeshijian = objects.get(position).getShijian();
            xiugaiBiaoti.setText("修改计划");
            riqi = objects.get(position).getTime();
            starttime = objects.get(position).getStarttime();
            endtime = objects.get(position).getEndtime();
            xiugaiStarttime.setText(objects.get(position).getTime() + " " + objects.get(position).getStarttime());
            xiugaiEndtime.setText(objects.get(position).getTime() + " " + objects.get(position).getEndtime());
            xiugaiYundong.setText(objects.get(position).getPlan() + objects.get(position).getShijian() + "分钟");
            System.out.println("运动时间："  +objects.get(position).getPlan() );
            switch (objects.get(position).getPlan()) {
                case "游泳":
                    xiugaiXuanzhongyundong.setImageResource(R.drawable.youyonglv);
                    xiugaiYouyong.setImageResource(R.drawable.youyonglv);
                    leixing[0] = "游泳";
                    break;
                case "跑步":
                    xiugaiXuanzhongyundong.setImageResource(R.drawable.paobulv);
                    xiugaiPaobu.setImageResource(R.drawable.paobulv);
                    leixing[0] = "跑步";
                    break;
                case "骑行":
                    xiugaiXuanzhongyundong.setImageResource(R.drawable.qixinglv);
                    xiugaiQixing.setImageResource(R.drawable.qixinglv);
                    leixing[0] = "骑行";
                    break;
                case "跳绳":
                    xiugaiXuanzhongyundong.setImageResource(R.drawable.tiaoshenglv);
                    xiugaiTiaosheng.setImageResource(R.drawable.tiaoshenglv);
                    leixing[0] = "跳绳";
                    break;
                case "爬山":
                    xiugaiXuanzhongyundong.setImageResource(R.drawable.pashanlv);
                    xiugaiPashan.setImageResource(R.drawable.pashanlv);
                    leixing[0] = "爬山";
                    break;
                case "步行":
                    xiugaiXuanzhongyundong.setImageResource(R.drawable.buxinglv);
                    xiugaiBuxing.setImageResource(R.drawable.buxinglv);
                    leixing[0] = "步行";
                    break;
            }
        }
        xiugaiPaobu.setOnClickListener(this);
        xiugaiYouyong.setOnClickListener(this);
        xiugaiPashan.setOnClickListener(this);
        xiugaiBuxing.setOnClickListener(this);
        xiugaiTiaosheng.setOnClickListener(this);
        xiugaiQixing.setOnClickListener(this);

        xiugaiStarttime.setOnClickListener(this);
        xiugaiEndtime.setOnClickListener(this);

        xiugaiWancheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean a = true;
                for (int i = 0; i < objects.size(); i++){
                    if (riqi.equals(objects.get(i).getTime()) && leixing[0].equals(objects.get(i).getPlan())){
                        try {
                            if (TimeUtil.getshijiandaxiao(starttime, objects.get(i).getEndtime()) && TimeUtil.getshijiandaxiao(objects.get(i).getStarttime(), endtime)){
                                Toast toast;
                                toast = Toast.makeText(XiugaiActivity.this, "在该时间段内有相同的运动类型，请重新选择", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                a = false;
                                break;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (a) {
                    if (position == objects.size()) {
                        PlanBean1 planBean1 = new PlanBean1();
                        planBean1.setTime(riqi);
                        planBean1.setPlan(leixing[0]);
                        planBean1.setShijian(jiangeshijian);
                        planBean1.setEndtime(endtime);
                        planBean1.setStarttime(starttime);
                        objects.add(planBean1);
//                        jihuaItemAdapter.replaceAll(objects);
                        Intent intent1 = new Intent();
                        intent1.putExtra("obj", (Serializable) objects);
                        intent1.putExtra("position", position);
                        setResult(3, intent1);
                        finish();
                    } else {
                        PlanBean1 planBean1 = new PlanBean1();
                        planBean1.setTime(riqi);
                        planBean1.setPlan(leixing[0]);
                        planBean1.setShijian(jiangeshijian);
                        planBean1.setEndtime(endtime);
                        planBean1.setStarttime(starttime);
                        objects.set(position, planBean1);
//                        jihuaItemAdapter.replaceAll(objects);
                        Intent intent1 = new Intent();
                        intent1.putExtra("obj", (Serializable) objects);
                        intent1.putExtra("position", position);
                        setResult(2, intent1);
                        finish();
                    }
                }

            }
        });

    }

    /**
     * Called when 1 view has been clicked.00
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.xiugai_fanhui:
                finish();
                break;
            case R.id.xiugai_paobu:
                chushihuatu();
                xiugaiPaobu.setImageResource(R.drawable.paobulv);
                xiugaiXuanzhongyundong.setImageResource(R.drawable.paobulv);
                leixing[0] = "跑步";
                xiugaiYundong.setText(leixing[0] + jiangeshijian + "分钟");
                xiugaiPaobu.setPadding(5,5,5,5);
                break;
            case R.id.xiugai_pashan:
                chushihuatu();
                leixing[0] = "爬山";
                xiugaiPashan.setImageResource(R.drawable.pashanlv);
                xiugaiXuanzhongyundong.setImageResource(R.drawable.pashanlv);
                xiugaiYundong.setText(leixing[0] + jiangeshijian + "分钟");
                xiugaiPashan.setPadding(5,5,5,5);
                break;
            case R.id.xiugai_youyong:
                chushihuatu();
                leixing[0] = "游泳";
                xiugaiYouyong.setImageResource(R.drawable.youyonglv);
                xiugaiXuanzhongyundong.setImageResource(R.drawable.youyonglv);
                xiugaiYundong.setText(leixing[0] + jiangeshijian + "分钟");
                xiugaiYouyong.setPadding(5,5,5,5);
                break;
            case R.id.xiugai_qixing:
                chushihuatu();
                leixing[0] = "骑行";
                xiugaiQixing.setPadding(5,5,5,5);
                xiugaiQixing.setImageResource(R.drawable.qixinglv);
                xiugaiXuanzhongyundong.setImageResource(R.drawable.qixinglv);
                xiugaiYundong.setText(leixing[0] + jiangeshijian + "分钟");
                break;
            case R.id.xiugai_buxing:
                chushihuatu();
                leixing[0] = "步行";
                xiugaiBuxing.setPadding(5,5,5,5);
                xiugaiBuxing.setImageResource(R.drawable.buxinglv);
                xiugaiXuanzhongyundong.setImageResource(R.drawable.buxinglv);
                xiugaiYundong.setText(leixing[0] + jiangeshijian + "分钟");
                break;
            case R.id.xiugai_tiaosheng:
                chushihuatu();
                leixing[0] = "跳绳";
                xiugaiTiaosheng.setPadding(10,10,10,10);
                xiugaiTiaosheng.setImageResource(R.drawable.tiaoshenglv);
                xiugaiXuanzhongyundong.setImageResource(R.drawable.tiaoshenglv);
                xiugaiYundong.setText(leixing[0] + jiangeshijian + "分钟");
                break;
            case R.id.xiugai_starttime:
                MyDialogFrament myDialogFrament = new MyDialogFrament();
                Bundle bundle = new Bundle();
                bundle.putInt("id", 1);
                bundle.putStringArrayList("riqiq", (ArrayList<String>) riqiq);
                if (position == objects.size()){
                    bundle.putString("riqi", objects.get(position-1).getTime());
                    bundle.putString("shijian", objects.get(position-1).getStarttime());
                }else {
                    bundle.putString("riqi", objects.get(position).getTime());
                    bundle.putString("shijian", objects.get(position).getStarttime());
                }
                myDialogFrament.setArguments(bundle);
                myDialogFrament.show(getFragmentManager(), "DialogFragment");
                break;
            case R.id.xiugai_endtime:
                MyDialogFrament myDialogFrament1 = new MyDialogFrament();
                Bundle bundle1 = new Bundle();
                bundle1.putInt("id", 2);
                ArrayList<String> endriqiq = new ArrayList<String>();
                endriqiq.add(riqi);
                bundle1.putStringArrayList("riqiq", (ArrayList<String>) endriqiq);
                if (position == objects.size()){
                    bundle1.putString("riqi", objects.get(position-1).getTime());
                    bundle1.putString("shijian", objects.get(position-1).getEndtime());
                }else {
                    bundle1.putString("riqi", objects.get(position).getTime());
                    bundle1.putString("shijian", objects.get(position).getEndtime());
                }
                myDialogFrament1.setArguments(bundle1);
                myDialogFrament1.show(getFragmentManager(), "DialogFragment");
                break;

        }
    }

    private void chushihuatu() {
        xiugaiPaobu.setImageDrawable(getDrawable(R.drawable.paobu2));
        xiugaiYouyong.setImageDrawable(getDrawable(R.drawable.youyong2));
        xiugaiPashan.setImageDrawable(getDrawable(R.drawable.pashan2));
        xiugaiTiaosheng.setImageDrawable(getDrawable(R.drawable.tiaosheng2));
        xiugaiBuxing.setImageDrawable(getDrawable(R.drawable.buxing2));
        xiugaiQixing.setImageDrawable(getDrawable(R.drawable.qixing2));

        xiugaiPaobu.setVisibility(View.VISIBLE);
        xiugaiYouyong.setVisibility(View.VISIBLE);
        xiugaiPashan.setVisibility(View.VISIBLE);
        xiugaiTiaosheng.setVisibility(View.VISIBLE);
        xiugaiBuxing.setVisibility(View.VISIBLE);
        xiugaiQixing.setVisibility(View.VISIBLE);

        xiugaiPaobu.setPadding(0,0,0,0);
        xiugaiYouyong.setPadding(0,0,0,0);
        xiugaiPashan.setPadding(0,0,0,0);
        xiugaiTiaosheng.setPadding(0,0,0,0);
        xiugaiBuxing.setPadding(0,0,0,0);
        xiugaiQixing.setPadding(0,0,0,0);
    }


    @Override
    public void getDataFrom_DialogFragment(String riqi, String xiaoshi, String fenzhong, int id) {
        //id = 1 开始时间； id = 2结束时间
        if (id == 1) {
            this.riqi = riqi;
            this.xiaoshi = xiaoshi;
            this.fenzhong = fenzhong;
            this.starttime = xiaoshi + ":" + fenzhong;
            long[] aa = TimeUtil.getDistanceTimes(riqi + " " + xiaoshi + ":" + fenzhong, this.riqi + " " + this.endtime);
            jiangeshijian = String.valueOf(aa[0]);
            if (aa[0] > 0) {
                xiugaiStarttime.setText(riqi + " " + starttime);
                xiugaiEndtime.setText(riqi + " " + endtime);
                xiugaiYundong.setText(leixing[0] + jiangeshijian + "分钟");
            }else {
                jiangeshijian = "0";
                endtime = starttime;
                xiugaiStarttime.setText(riqi + " " + starttime);
                xiugaiEndtime.setText(riqi + " " + endtime);
                xiugaiYundong.setText(leixing[0] + jiangeshijian + "分钟");
            }
        } else {
            long[] aa = TimeUtil.getDistanceTimes(this.riqi + " " + this.starttime, riqi + " " + xiaoshi + ":" + fenzhong);
            jiangeshijian = String.valueOf(aa[0]);
            this.riqi = riqi;
            this.xiaoshi = xiaoshi;
            this.fenzhong = fenzhong;
            this.endtime = xiaoshi + ":" + fenzhong;
            if (aa[0] > 0) {
                xiugaiStarttime.setText(riqi + " " + starttime);
                xiugaiEndtime.setText(riqi + " " + endtime);
                xiugaiYundong.setText(leixing[0] + jiangeshijian + "分钟");
            }else {
                this.endtime = starttime;
                jiangeshijian = "0";
                xiugaiStarttime.setText(riqi + " " + starttime);
                xiugaiEndtime.setText(riqi + " " + endtime);
                xiugaiYundong.setText(leixing[0] + jiangeshijian + "分钟");
            }
        }
    }
}
