package ruanjianbei.sport.mysport.transition;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.bean.TeamInfoBean;
import ruanjianbei.sport.mysport.bean.UserIndividualInfoBean;
import ruanjianbei.sport.mysport.util.MyApplication;

public class DetailActivity extends FragmentActivity {

    public static final String EXTRA_IMAGE_URL = "detailImageUrl";

    public static final String IMAGE_TRANSITION_NAME = "transitionImage";
    public static final String ADDRESS1_TRANSITION_NAME = "address1";
    public static final String ADDRESS2_TRANSITION_NAME = "address2";
    public static final String ADDRESS3_TRANSITION_NAME = "address3";
    public static final String ADDRESS4_TRANSITION_NAME = "address4";
    public static final String ADDRESS5_TRANSITION_NAME = "address5";
    public static final String RATINGBAR_TRANSITION_NAME = "ratingBar";

    public static final String HEAD1_TRANSITION_NAME = "head1";
    public static final String HEAD2_TRANSITION_NAME = "head2";
    public static final String HEAD3_TRANSITION_NAME = "head3";
    public static final String HEAD4_TRANSITION_NAME = "head4";


    private TextView address1, address3, address4, address5;
    private ImageView address2;
    private ImageView imageView;
    private RatingBar ratingBar;

    private TeamInfoBean teamInfoBean;
    private LinearLayout listContainer;
    private static final String[] nameStrs = {"王世杰", "张奥迪", "马英杰", "缪建国"};
    private static final String[] pinlunStrs = {"你们今天又没有看到提示，第名的战队想要挑战我们，诸君拔剑吧！","看见了，诸君，是时候拔剑了！",  "让他们见识见识我们的厉害。", "反正看不到，随便写。"};
    private static final String[] headStrs = {HEAD1_TRANSITION_NAME, HEAD2_TRANSITION_NAME, HEAD3_TRANSITION_NAME, HEAD4_TRANSITION_NAME};
    private static final int[] imageIds = {R.drawable.head1, R.drawable.head2, R.drawable.head3, R.drawable.head4};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = (ImageView) findViewById(R.id.image);
        address1 = (TextView)findViewById(R.id.address1);//战队名
        address2 = findViewById(R.id.address2);
        address3 = (TextView)findViewById(R.id.address3);//驻地
        address4 = (TextView)findViewById(R.id.address4);//成立时间
        address5 = (TextView)findViewById(R.id.address5);//排名第几
        ratingBar = (RatingBar) findViewById(R.id.rating);
        listContainer = (LinearLayout) findViewById(R.id.detail_list_container);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        Intent intent = getIntent();

        String imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL);
        teamInfoBean = (TeamInfoBean) intent.getSerializableExtra("team");
        address1.setText(teamInfoBean.getTeamname());
        address3.setText(teamInfoBean.getStation());
        address4.setText("创立于" + teamInfoBean.getCreatetime());
        address5.setText("No." + String.valueOf(teamInfoBean.getId()));
        Picasso.with(getApplicationContext())
                .load(MyApplication.imageUri +imageUrl)
                .placeholder(getApplication().getDrawable(R.drawable.wanou))
                .tag("PhotoTag")
                .into(imageView);
//        ImageLoader.getInstance().displayImage(imageUrl, imageView);

        ViewCompat.setTransitionName(imageView, IMAGE_TRANSITION_NAME);
        ViewCompat.setTransitionName(address1, ADDRESS1_TRANSITION_NAME);
        ViewCompat.setTransitionName(address2, ADDRESS2_TRANSITION_NAME);
        ViewCompat.setTransitionName(address3, ADDRESS3_TRANSITION_NAME);
        ViewCompat.setTransitionName(address4, ADDRESS4_TRANSITION_NAME);
        ViewCompat.setTransitionName(address5, ADDRESS5_TRANSITION_NAME);
        ViewCompat.setTransitionName(ratingBar, RATINGBAR_TRANSITION_NAME);

        dealListView();
    }

    private void dealListView() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);

        for (int i = 0; i < 20; i++) {
            View childView = layoutInflater.inflate(R.layout.detail_list_item, null);
            listContainer.addView(childView);
            ImageView headView = (ImageView) childView.findViewById(R.id.head);
            TextView textView = (TextView) childView.findViewById(R.id.zhanduigerenname);
            TextView textView1 = (TextView) childView.findViewById(R.id.zhanduigerenpinglun);
            if (i < headStrs.length) {
                headView.setImageResource(imageIds[i % imageIds.length]);
                textView.setText(nameStrs[i]);
                textView1.setText(pinlunStrs[i]);
                ViewCompat.setTransitionName(headView, headStrs[i]);
            }
        }
    }
}
