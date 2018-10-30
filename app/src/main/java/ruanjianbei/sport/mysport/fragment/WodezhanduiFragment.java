package ruanjianbei.sport.mysport.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import de.hdodenhof.circleimageview.CircleImageView;
import ruanjianbei.sport.mysport.R;

import android.widget.TextView;

public class WodezhanduiFragment extends Fragment  {

    private CircleImageView gerenZhanduiTouxiang;
    private TextView gerenZhanduiMingzi;
    private TextView gerenZhanduiPaiming;
    private TextView gerenZhanduiDengji;
    private TextView gerenZhanduiRongyaoduiyuan;
    private TextView gerenZhanduiRenqiduiyuan;
    private TextView gerenZhanduiLishizhanji;
    private TextView gerenZhanduiRongyaochengyuan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.wodezhandui, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gerenZhanduiTouxiang = (CircleImageView) view.findViewById(R.id.geren_zhandui_touxiang);
        gerenZhanduiMingzi = (TextView) view.findViewById(R.id.geren_zhandui_mingzi);
        gerenZhanduiPaiming = (TextView) view.findViewById(R.id.geren_zhandui_paiming);
        gerenZhanduiDengji = (TextView) view.findViewById(R.id.geren_zhandui_dengji);
        gerenZhanduiRongyaoduiyuan = (TextView) view.findViewById(R.id.geren_zhandui_rongyaoduiyuan);
        gerenZhanduiRenqiduiyuan = (TextView) view.findViewById(R.id.geren_zhandui_renqiduiyuan);
        gerenZhanduiLishizhanji = (TextView) view.findViewById(R.id.geren_zhandui_lishizhanji);
        gerenZhanduiRongyaochengyuan = (TextView) view.findViewById(R.id.geren_zhandui_rongyaochengyuan);
    }

}
