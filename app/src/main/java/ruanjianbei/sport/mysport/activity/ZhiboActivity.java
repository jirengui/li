package ruanjianbei.sport.mysport.activity;

import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.LinkedList;

import jp.co.cyberagent.android.gpuimage.GPUImageAddBlendFilter;
import me.lake.librestreaming.core.listener.RESConnectionListener;
import me.lake.librestreaming.filter.hardvideofilter.BaseHardVideoFilter;
import me.lake.librestreaming.filter.hardvideofilter.HardVideoGroupFilter;
import me.lake.librestreaming.ws.StreamAVOption;
import me.lake.librestreaming.ws.StreamLiveCameraView;
import me.lake.librestreaming.ws.filter.hardfilter.GPUImageBeautyFilter;
import me.lake.librestreaming.ws.filter.hardfilter.WatermarkFilter;
import me.lake.librestreaming.ws.filter.hardfilter.extra.GPUImageCompatibleFilter;
import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.util.StatusBarUtils;

public class ZhiboActivity extends AppCompatActivity {
    private String rtmpUrl = "rtmp://39.108.14.189/android/teststream";
    private static final String TAG = ZhiboActivity.class.getSimpleName();
    private StreamLiveCameraView mLiveCameraView;
    private StreamAVOption streamAVOption;
    private LiveUI mLiveUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhibo);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        StatusBarUtils.setTranslucentStatus(this);

        initLiveConfig();
        mLiveUI = new LiveUI(this,mLiveCameraView,rtmpUrl);
    }

    /**
     * 设置推流参数
     */
    public void initLiveConfig() {
        mLiveCameraView = (StreamLiveCameraView) findViewById(R.id.stream_previewView);

        //参数配置 start
        streamAVOption = new StreamAVOption();
        streamAVOption.streamUrl = rtmpUrl;
        //参数配置 end

        mLiveCameraView.init(this, streamAVOption);
        mLiveCameraView.addStreamStateListener(resConnectionListener);
        //设置滤镜组
        LinkedList<BaseHardVideoFilter> files = new LinkedList<>();
        files.add(new GPUImageCompatibleFilter(new GPUImageBeautyFilter()));
        files.add(new GPUImageCompatibleFilter(new GPUImageAddBlendFilter()));
        mLiveCameraView.setHardVideoFilter(new HardVideoGroupFilter(files));
    }
    RESConnectionListener resConnectionListener = new RESConnectionListener() {
        @Override
        public void onOpenConnectionResult(int result) {
            //result 0成功  1 失败
            Toast.makeText(ZhiboActivity.this,"打开推流连接 状态："+result+ " 推流地址："+rtmpUrl,Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWriteError(int errno) {
            Toast.makeText(ZhiboActivity.this,"推流出错,请尝试重连",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCloseConnectionResult(int result) {
            //result 0成功  1 失败
            Toast.makeText(ZhiboActivity.this,"关闭推流连接 状态："+result,Toast.LENGTH_LONG).show();
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLiveCameraView.destroy();
    }


}
