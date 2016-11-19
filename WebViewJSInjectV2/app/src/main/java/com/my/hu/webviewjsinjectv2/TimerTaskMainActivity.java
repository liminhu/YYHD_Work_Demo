package com.my.hu.webviewjsinjectv2;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.my.hu.activity.gen.downloadlink.TianyiDownloadLinkActivity;
import com.my.hu.webviewjsinjectv2.domain.ConstantData;
import com.my.hu.webviewjsinjectv2.util.FileUtil;
import com.my.hu.webviewjsinjectv2.util.StringUtil;

import java.util.Timer;
import java.util.TimerTask;

public class TimerTaskMainActivity extends AppCompatActivity {
    private TextView currentTime;
    private final Timer timer = new Timer();
    private TimerTask task=new TimerTask() {
        @Override
        public void run() {
            FileUtil.executeDeleteDBFile(getBaseContext(), ConstantData.DB_RAW_LINK_NAME);
            FileUtil.executeDeleteDBFile(getBaseContext(), ConstantData.DB_SUBMIT_DATA_NAME);
            Intent intent=new Intent(TimerTaskMainActivity.this, MainActivity.class);
            startActivity(intent);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_task_main);
        currentTime=(TextView)findViewById(R.id.current_time_view);
        SetTextViewTime setTime=new SetTextViewTime();
        new Thread(setTime).start();
        Intent intent=getIntent();
        String data=intent.getStringExtra("data");
        if(data==null){
            timer.schedule(task, 0, 1000*60*60*2);
        }
    }



    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    currentTime.setText(StringUtil.getDateTime()); break;
            }
        }
    };

    class SetTextViewTime implements Runnable{
        @Override
        public void run() {
            while(true){
                try{
                    Thread.sleep(1000);
                    handler.obtainMessage(0).sendToTarget();
                }catch (Exception e){
                    Log.i("hook_sleep","sleep");
                }
            }
        }
    }


}
