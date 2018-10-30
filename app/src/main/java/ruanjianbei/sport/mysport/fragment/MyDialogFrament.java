package ruanjianbei.sport.mysport.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pl.wheelview.WheelView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.bean.PlanBean;
import ruanjianbei.sport.mysport.bean.PlanBean1;

/**
 * Created by li on 2018/8/18.
 */

public class MyDialogFrament extends DialogFragment implements View.OnClickListener {
    private TextView tv_chushishijian, tv_queding, tv_quxiao;
    private WheelView wh_riqi, wh_xiaoshi, wh_fenzhong;
    private ArrayList<String> riqiq = new ArrayList<>();
    private List<String> xiaoshiq = new ArrayList<>();
    private List<String> fenzhongq = new ArrayList<>();
    private String xiaoshi, fenzhong, riqi;
    private int id;
    private MyDialogFragment_Listener myDialogFragment_Listener;

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shijian_queding:
                riqi = wh_riqi.getSelectedText();
                xiaoshi = wh_xiaoshi.getSelectedText();
                fenzhong = wh_fenzhong.getSelectedText();
                // 通过接口回传数据给activity
                if (myDialogFragment_Listener != null) {
                    myDialogFragment_Listener.getDataFrom_DialogFragment(riqi, xiaoshi, fenzhong, id);
                }
                dismiss();
                break;
            case R.id.shijian_quxiao:
                dismiss();

        }
    }

    // 回调接口，用于传递数据给Activity -------
    public interface MyDialogFragment_Listener {
        void getDataFrom_DialogFragment(String riqi, String xiaoshi, String fenzhong, int id);
    }

    @Override
    public void onAttach(Context context) {
        try {
            myDialogFragment_Listener = (MyDialogFragment_Listener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implementon MyDialogFragment_Listener");
        }

        super.onAttach(context);
    }

    //SDK API<23时，onAttach(Context)不执行，需要使用onAttach(Activity)。Fragment自身的Bug，v4的没有此问题
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            try {
                myDialogFragment_Listener = (MyDialogFragment_Listener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must implementon MyDialogFragment_Listener");
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.shijiandialog, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tv_chushishijian = view.findViewById(R.id.shijian_chushi);
        tv_queding = view.findViewById(R.id.shijian_queding);
        tv_quxiao = view.findViewById(R.id.shijian_quxiao);
        wh_xiaoshi = view.findViewById(R.id.xiaoshi);
        wh_fenzhong = view.findViewById(R.id.fenzhong);
        wh_riqi = view.findViewById(R.id.riqi);

        tv_queding.setOnClickListener(this);
        tv_quxiao.setOnClickListener(this);

        Bundle bundle = getArguments();
        riqiq = bundle.getStringArrayList("riqiq");
        id = bundle.getInt("id");
        String shijian = bundle.getString("shijian");
        riqi = bundle.getString("riqi");
        tv_chushishijian.setText(riqi + " " + shijian);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        try {
            Date startDate = simpleDateFormat.parse(shijian);
            xiaoshi = String.valueOf(startDate.getHours());
            fenzhong = String.valueOf(startDate.getMinutes());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (int i = 0; i <= 24; i++) {
            if (i < 10) {
                xiaoshiq.add("0" + String.valueOf(i));
            } else {
                xiaoshiq.add(String.valueOf(i));
            }
        }
        for (int i = 0; i <= 60; i++) {
            if (i < 10) {
                fenzhongq.add("0" + String.valueOf(i));
            } else {
                fenzhongq.add(String.valueOf(i));
            }
        }
        wh_riqi.setData((ArrayList<String>) riqiq);
        wh_fenzhong.setData((ArrayList<String>) fenzhongq);
        wh_xiaoshi.setData((ArrayList<String>) xiaoshiq);

        for (int i = 0; i < riqiq.size(); i++) {
            if (riqi.equals(riqiq.get(i))) {
                wh_riqi.setDefault(i);
            }
        }
        for (int i = 0; i < xiaoshiq.size(); i++) {
            if (xiaoshi.equals(xiaoshiq.get(i))) {
                wh_xiaoshi.setDefault(i);
            }
        }
        for (int i = 0; i < fenzhongq.size(); i++) {
            if (fenzhong.equals(fenzhongq.get(i))) {
                wh_fenzhong.setDefault(i);
            }
        }
    }


    // DialogFragment关闭时回传数据给Activity
    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}