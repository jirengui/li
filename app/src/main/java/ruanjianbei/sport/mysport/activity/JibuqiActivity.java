package ruanjianbei.sport.mysport.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import ruanjianbei.sport.mysport.R;
import ruanjianbei.sport.mysport.util.StepService;
import ruanjianbei.sport.mysport.util.UpdateUiCallBack;

public class JibuqiActivity extends Activity implements View.OnClickListener {

    private TextView tvBushu;
    private StepService mService;
    private boolean mIsRunning;
    private SharedPreferences mySharedPreferences;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                tvBushu.setText(mySharedPreferences.getString("steps","0"));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jibuqi);

        tvBushu = (TextView) findViewById(R.id.tv_bushu);
        findViewById(R.id.bt_kaishi).setOnClickListener(this);
        mySharedPreferences = getSharedPreferences("relevant_data",Activity.MODE_PRIVATE);
        startStepService();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_kaishi:
                //TODO implement
                mService.resetValues();
                tvBushu.setText(mySharedPreferences.getString("steps", "0"));
                break;
        }
    }
    protected void onPause() {
        unbindStepService();
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
        tvBushu.setText(mySharedPreferences.getString("steps", "0"));
        if (this.mIsRunning){
            bindStepService();
        }
    }



    private UpdateUiCallBack mUiCallback = new UpdateUiCallBack() {
        @Override
        public void updateUi() {
            Message message = mHandler.obtainMessage();
            message.what = 1;
            mHandler.sendMessage(message);
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StepService.StepBinder binder = (StepService.StepBinder) service;
            mService = binder.getService();
            mService.registerCallback(mUiCallback);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void bindStepService() {
        bindService(new Intent(this, StepService.class), this.mConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindStepService() {
        unbindService(this.mConnection);
    }

    private void startStepService() {
        this.mIsRunning = true;
        startService(new Intent(this, StepService.class));
    }

    private void stopStepService() {
        this.mIsRunning = false;
        if (this.mService != null)
            stopService(new Intent(this, StepService.class));
    }

}

