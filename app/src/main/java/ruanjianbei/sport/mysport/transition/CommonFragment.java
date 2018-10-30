package ruanjianbei.sport.mysport.transition;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;

import de.hdodenhof.circleimageview.CircleImageView;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.bean.TeamInfoBean;
import ruanjianbei.sport.mysport.util.MyApplication;

public class CommonFragment extends Fragment implements DragLayout.GotoDetailListener {
    private ImageView imageView;
    private TextView address1, address3, address4, address5;
    private ImageView address2;
    private RatingBar ratingBar;
    private ImageView head2,  head3, head4;
    private CircleImageView head1;
    private String imageUrl, zhanduiming, zhudi;
    private TeamInfoBean teamInfoBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_common, null);
        DragLayout dragLayout = (DragLayout) rootView.findViewById(R.id.drag_layout);
        imageView = (ImageView) dragLayout.findViewById(R.id.image);
        System.out.println("战队图片地址：" + imageUrl);
        Picasso.with(getContext())
                .load(MyApplication.imageUri + imageUrl)
                .placeholder(getContext().getDrawable(R.drawable.wanou))
                .tag("PhotoTag")
                .into(imageView);
//        ImageLoader.getInstance().displayImage(imageUrl, imageView);
        address1 = dragLayout.findViewById(R.id.address1);
        address2 = dragLayout.findViewById(R.id.address2);
        address3 = dragLayout.findViewById(R.id.address3);
        address4 = dragLayout.findViewById(R.id.address4);
        address5 = dragLayout.findViewById(R.id.address5);
        ratingBar = (RatingBar) dragLayout.findViewById(R.id.rating);

        head1 = dragLayout.findViewById(R.id.head1);
        head2 = dragLayout.findViewById(R.id.head2);
        head3 = dragLayout.findViewById(R.id.head3);
        head4 = dragLayout.findViewById(R.id.head4);


        Picasso.with(getContext())
                .load(MyApplication.imageUri + teamInfoBean.getCreatertouxiang())
                .into(head1);
        dragLayout.setGotoDetailListener(this);
        address1.setText(zhanduiming);
        address3.setText(zhudi);
        address4.setText("创立于" + teamInfoBean.getCreatetime());
        address5.setText("No." + String.valueOf(teamInfoBean.getId()));
        ratingBar.setRating(3);
        return rootView;
    }

    @Override
    public void gotoDetail() {
        Activity activity = (Activity) getContext();
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                new Pair(imageView, DetailActivity.IMAGE_TRANSITION_NAME),
                new Pair(address1, DetailActivity.ADDRESS1_TRANSITION_NAME),
                new Pair(address2, DetailActivity.ADDRESS2_TRANSITION_NAME),
                new Pair(address3, DetailActivity.ADDRESS3_TRANSITION_NAME),
                new Pair(address4, DetailActivity.ADDRESS4_TRANSITION_NAME),
                new Pair(address5, DetailActivity.ADDRESS5_TRANSITION_NAME),
                new Pair(ratingBar, DetailActivity.RATINGBAR_TRANSITION_NAME),
                new Pair(head1, DetailActivity.HEAD1_TRANSITION_NAME),
                new Pair(head2, DetailActivity.HEAD2_TRANSITION_NAME),
                new Pair(head3, DetailActivity.HEAD3_TRANSITION_NAME),
                new Pair(head4, DetailActivity.HEAD4_TRANSITION_NAME)
        );
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_IMAGE_URL, imageUrl);
        intent.putExtra("team", (Serializable) teamInfoBean);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    public void bindData(TeamInfoBean teamInfoBean) {
        this.imageUrl = teamInfoBean.getTouxiang();
        this.zhanduiming = teamInfoBean.getTeamname();
        this.zhudi = teamInfoBean.getStation();
        this.teamInfoBean = teamInfoBean;
    }
}
