    package ruanjianbei.sport.mysport.activity;

    /**
     * Created by li on 2017/10/20.
     */
    import android.content.Context;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.graphics.drawable.AnimationDrawable;
    import android.graphics.drawable.Drawable;
    import android.os.Bundle;
    import android.os.CountDownTimer;
    import android.os.Handler;
    import android.support.v7.app.AppCompatActivity;
    import android.view.Window;
    import android.view.WindowManager;
    import android.widget.ImageView;
    import android.widget.RelativeLayout;
    import android.widget.TextView;

    import ruanjianbei.sport.mysport.R;
    import ruanjianbei.sport.mysport.util.MyApplication;


    public class SplashActivity extends AppCompatActivity {
        private MyCountDownTimer mc;
        private RelativeLayout iv;
        private AnimationDrawable animationDrawable;
        private int tiaozhuan = -1;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //全屏
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.splash);
            SharedPreferences pref = getSharedPreferences("name", MODE_PRIVATE);
            int userid = pref.getInt("userId", -1);
            tiaozhuan = MyApplication.chushihua(userid);
            iv = findViewById(R.id.iv);
    //        animationDrawable = new AnimationDrawable();
    //        for (int i = 4; i <= 25; i++) {
    //            int id = 0;
    //            if (i < 10) {
    //                id = getResources().getIdentifier("@drawable/dubleclick0000" + i, "drawable", getPackageName());
    //            }else {
    //                 id = getResources().getIdentifier("@drawable/dubleclick000" + i, "drawable", getPackageName());
    //
    //            }
    //            Drawable drawable = getResources().getDrawable(id);
    //            animationDrawable.addFrame(drawable, 100);
    //        }
    //
    //        animationDrawable.setOneShot(false);
    //        iv.setImageDrawable(animationDrawable);
    //        // 1. 设置动画
    //        animationDrawable.stop();
    //        // 2. 获取动画对象
    //        animationDrawable.start();
    //        // 3. 启动动画

            mc = new MyCountDownTimer(2000, 1000);
            mc.start();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (tiaozhuan == -1) {
                        Intent it = new Intent();
                        it.setClass(SplashActivity.this, LoginActivity.class);
                        Bundle bundle = new Bundle();
                        it.putExtras(bundle);
                        startActivity(it);
                        finish();
                    }else {
                        Intent it = new Intent();
                        it.setClass(SplashActivity.this, MainActivity.class);
                        startActivity(it);
                        finish();
                    }
                }
            }, 1000 * 2);
        }

        class MyCountDownTimer extends CountDownTimer {
            /**
             *
             * @param millisInFuture
             * 表示以毫秒为单位 倒计时的总数
             *
             * 例如 millisInFuture=1000 表示1秒
             *
             * @param countDownInterval
             * 表示 间隔 多少微秒 调用一次 onTick 方法
             *
             * 例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
             *
             */
            public MyCountDownTimer(long millisInFuture, long countDownInterval) {
                super(millisInFuture, countDownInterval);
            }
            public void onFinish() {
            }
            public void onTick(long millisUntilFinished) {
            }
        }
    }

