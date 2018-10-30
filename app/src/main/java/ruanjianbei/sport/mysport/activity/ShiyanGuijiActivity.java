package ruanjianbei.sport.mysport.activity;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.util.GradientHelper;
import ruanjianbei.sport.mysport.zidingyi_view.Guiji;

public class ShiyanGuijiActivity extends Activity implements Guiji.OnTrailChangeListener {

    private Guiji guiji;
    private List<Point> list = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.shiyan_guiji);
        guiji = findViewById(R.id.guiji);
        // 通过WindowManager获取
        //1080,*,1920
        list.add(new Point(40,960));
        list.add(new Point(140,1200));
        list.add(new Point(240,960));
        list.add(new Point(340,1200));
        list.add(new Point(440,960));
        list.add(new Point(540,1200));
        list.add(new Point(640,960));
        list.add(new Point(740,1200));
        list.add(new Point(840,960));
        list.add(new Point(940,1080));
        list.add(new Point(40,1080));
        guiji.drawSportLine(list, R.drawable.xin, R.drawable.xin, this);
    }

    @Override
    public void onFinish() {
        Toast.makeText(this,"傻缪缪到了。。。", Toast.LENGTH_LONG).show();
    }
}
