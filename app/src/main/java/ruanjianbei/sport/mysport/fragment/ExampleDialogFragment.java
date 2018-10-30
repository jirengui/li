package ruanjianbei.sport.mysport.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.labo.kaji.swipeawaydialog.support.v4.SwipeAwayDialogFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.adapter.MyAdapter1;
import ruanjianbei.sport.mysport.util.MyApplication;

/**
 * Created by li on 2018/5/31.
 */

public class ExampleDialogFragment extends SwipeAwayDialogFragment {
    private Button yes;//确定按钮
    private Button no;//取消按钮
    private TextView titleTv;//消息标题文本
    private TextView nameTv, shijianTv, lichengTv, peisuTv;
    private CircleImageView touxiang;
    private GridView gridView;
    private ImageView jieping;
    private List<String> list = new ArrayList<>();
    //确定文本和取消文本的显示内容
    private String yesStr, noStr;

    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器
    private byte [] bitmapByte;



    /**
     * 设置取消按钮的显示内容和监听
     *
     * @param str
     * @param onNoOnclickListener
     */
    public void setNoOnclickListener(String str, onNoOnclickListener onNoOnclickListener) {
        if (str != null) {
            noStr = str;
        }
        this.noOnclickListener = onNoOnclickListener;
    }

    /**
     * 设置确定按钮的显示内容和监听
     *
     * @param str
     * @param onYesOnclickListener
     */
    public void setYesOnclickListener(String str, onYesOnclickListener onYesOnclickListener) {
        if (str != null) {
            yesStr = str;
        }
        this.yesOnclickListener = onYesOnclickListener;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mydialog, container, true);
        //初始化界面控件
        yes = (Button) view.findViewById(R.id.yes);
        no = (Button) view.findViewById(R.id.no);
        titleTv = (TextView) view.findViewById(R.id.title);
        nameTv = (TextView) view.findViewById(R.id.mydiolag_name);
        gridView = (GridView) view.findViewById(R.id.mydiolag_tupian);
        touxiang = view.findViewById(R.id.mydiolag_touxiang);
        shijianTv = view.findViewById(R.id.mydiolag_shijian);
        lichengTv = view.findViewById(R.id.mydiolag_licheng);
        peisuTv = view.findViewById(R.id.mydiolag_peisu);
        jieping = view.findViewById(R.id.mydiolag_jieping);
        Picasso.with(getContext())
                .load(MyApplication.imageUri + MyApplication.user.getTouxiang())
                .into(touxiang);
        Bundle bundle = getArguments();
        if (bundle != null) {
            nameTv.setText(bundle.getString("name"));
            list = bundle.getStringArrayList("tupian");
            shijianTv.setText(bundle.getString("shijian"));
            lichengTv.setText(bundle.getString("licheng"));
            peisuTv.setText(bundle.getString("peisu"));
            bitmapByte = bundle.getByteArray("jieping");
            Bitmap bitmap= null;
            if (bitmapByte != null) {
                bitmap = BitmapFactory.decodeByteArray(bitmapByte, 0, bitmapByte.length);
            }
            jieping.setImageBitmap(bitmap);
//            System.out.println("Dialog图片数据：" + list.get(0));
            gridView.setAdapter(new MyAdapter1(getContext(), list, 1));

        }
        //初始化界面控件的事件
        initEvent();
        return view;
    }
    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesClick();
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoClick();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setTupian(List<String> list){
        System.out.println("Dialog图片数据：" + list.get(0));
        gridView.setAdapter(new MyAdapter1(getContext(), list));
    }

    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface onYesOnclickListener {
        public void onYesClick();
    }

    public interface onNoOnclickListener {
        public void onNoClick();
    }
    @Override
    public boolean onSwipedAway(boolean toRight) {
        return false;
    }

}
