package ruanjianbei.sport.mysport.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.TextView;

import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.util.MyApplication;

public class GerenxinxiFragment extends Fragment  {

    private TextView gerenDengji;
    private TextView gerenJifen;
    private TextView gerenXingbie;
    private TextView gerenZhandui;
    private TextView gerenXuexiao;
    private TextView gerenQianmin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.gerenxinxi, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gerenDengji = (TextView) view.findViewById(R.id.geren_dengji);
        gerenJifen = (TextView) view.findViewById(R.id.geren_jifen);
        gerenXingbie = (TextView) view.findViewById(R.id.geren_xingbie);
        gerenZhandui = (TextView) view.findViewById(R.id.geren_zhandui);
        gerenXuexiao = (TextView) view.findViewById(R.id.geren_xuexiao);
        gerenQianmin = (TextView) view.findViewById(R.id.geren_qianmin);

        gerenDengji.setText(MyApplication.user.getDengji());
        gerenJifen.setText(MyApplication.user.getJifen());
        gerenXingbie.setText(MyApplication.user.getSex());
        gerenXuexiao.setText(MyApplication.user.getSchool());
        gerenZhandui.setText(MyApplication.user.getTeam());
    }

}
