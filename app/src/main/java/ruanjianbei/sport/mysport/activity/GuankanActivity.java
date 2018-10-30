package ruanjianbei.sport.mysport.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;


import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.IMediaController;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.PLOnErrorListener;
import com.pili.pldroid.player.widget.PLVideoView;

import java.io.IOException;

import ruanjianbei.sport.mysport.R;

public class GuankanActivity extends Activity implements PLOnErrorListener {

        private String rtmpUrl = "rtmp://39.108.14.189/android/teststream";
    private PLVideoView mVideoView;
    private ImageView fanhui;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.guankan);
        mVideoView = (PLVideoView) findViewById(R.id.play);
        fanhui = findViewById(R.id.guankan_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        //关联播放控制器
//        IMediaController mMediaController = new IMediaController() {
//            @Override
//            public void setMediaPlayer(MediaPlayerControl mediaPlayerControl) {
//
//            }
//
//            @Override
//            public void show() {
//
//            }
//
//            @Override
//            public void show(int i) {
//
//            }
//
//            @Override
//            public void hide() {
//
//            }
//
//            @Override
//            public boolean isShowing() {
//                return false;
//            }
//
//            @Override
//            public void setEnabled(boolean b) {
//
//            }
//
//            @Override
//            public void setAnchorView(View view) {
//
//            }
//        };
//        mVideoView.setMediaController(mMediaController);
        // 设置加载动画
        View loadingView = findViewById(R.id.LoadingView);
        mVideoView.setBufferingIndicator(loadingView);
        // 全屏
        mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);
        //配置播放参数
        AVOptions options = new AVOptions();
        // 打开重试次数，设置后若打开流地址失败，则会进行重试
        options.setInteger(AVOptions.KEY_OPEN_RETRY_TIMES, 3);
        // 是否开启直播优化，1 为开启，0 为关闭。若开启，视频暂停后再次开始播放时会触发追帧机制
       // 默认为 0
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);
        // 默认的缓存大小，单位是 ms
        // 默认值是：500
        options.setInteger(AVOptions.KEY_CACHE_BUFFER_DURATION, 0);
        mVideoView.setAVOptions(options);
        mVideoView.setVideoPath(rtmpUrl);
        mVideoView.start();
        mVideoView.setOnErrorListener(this);

    }

    @Override
    public boolean onError(int i) {
        System.out.println("错误信息：" + i + "网络联通性： " + isNetworkAvailable(this));
        if (!isNetworkAvailable(this)){
            mVideoView.stopPlayback();
        }
        return false;
    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
