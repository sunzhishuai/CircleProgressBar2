package com.newborntown.circleprogressbar;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Integer progress = (Integer) msg.obj;
            mProgress.setProgress(progress);
            Message message = handler.obtainMessage();
            message.obj = progress == 100 ? 0 : ++progress;
            handler.sendMessageDelayed(message,500);
        }
    };
    private CircleProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgress = (CircleProgressBar) findViewById(R.id.cpv_progress);
        Message message = handler.obtainMessage();
        message.obj=0;
        handler.sendMessage(message);
    }
}
