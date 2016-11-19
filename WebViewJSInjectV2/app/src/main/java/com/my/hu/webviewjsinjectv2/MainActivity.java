package com.my.hu.webviewjsinjectv2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.my.hu.activity.RawLinkActivity;
import com.my.hu.activity.ShowRawDataActivity;
import com.my.hu.activity.gen.downloadlink.BaiduDownloadLinkActivity;
import com.my.hu.activity.gen.downloadlink.ShowDownloadLinkActivity;
import com.my.hu.activity.gen.downloadlink.TianyiDownloadLinkActivity;
import com.my.hu.post.activity.BaiduPostActivity;
import com.my.hu.post.activity.TianyiPostActivity;

public class MainActivity extends AppCompatActivity {

    private Button first;
    private Button second;
    private Button three;
    private  SharedPreferences.Editor sharedata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        first=(Button)findViewById(R.id.get_raw_link_test);
        second=(Button)findViewById(R.id.get_download_link);
        three=(Button)findViewById(R.id.post_download_link);
        Intent intent=getIntent();
        String data=intent.getStringExtra("data");
        Log.i("hook_main","begin");
        if(data==null){
            Log.i("hook_data_begin","is null");
            sharedata=getSharedPreferences("data",0).edit();
            sharedata.putString("time","10");
            sharedata.commit();
            first.performClick();
        }else if(("first").equals(data)){
            Log.i("hook_data", data);
            second.performClick();
        }else if(("second").equals(data)){
            Log.i("hook_data", data);
            three.performClick();
        }else if("finish".equals(data)){
            Log.i("hook_finish","is data");
            SharedPreferences share=getSharedPreferences("data",0);
            String time=share.getString("time",null);
            if(time!=null){
                Log.i("hook_share_time",time);
                int num=Integer.valueOf(time);
                if(num>0){
                    num -= 1;
                    SharedPreferences.Editor sharedata=getSharedPreferences("data",0).edit();
                    sharedata.putString("time",String.valueOf(num));
                    sharedata.commit();
                    first.performClick();
                }else{
                    Intent intent1=new Intent(MainActivity.this, TimerTaskMainActivity.class);
                    intent1.putExtra("data","return");
                    startActivity(intent1);
                    finish();
                }
            }else{
                Log.i("hook_share_time_finish","is null");
            }
        }else{
            Log.i("hook_data",data);
        }
    }


    public void getRawLinkView(View view){
        Intent intent=new Intent(MainActivity.this, RawLinkActivity.class);
        startActivity(intent);
        finish();
    }


    //测试生成下载链接
    public void getDownLoadLink(View view){
        Intent intent=new Intent(MainActivity.this, BaiduDownloadLinkActivity.class);
        startActivity(intent);
        finish();
    }


    public void showDownLoadLink(View view){
        Intent intent=new Intent(MainActivity.this, ShowDownloadLinkActivity.class);
        startActivity(intent);
        finish();
    }


    public void show_rawdata_link(View view){
        Intent intent=new Intent(MainActivity.this, ShowRawDataActivity.class);
        startActivity(intent);
        finish();
    }



    //postDownLoadLink
    public void postDownLoadLink(View view){
        Intent intent=new Intent(MainActivity.this, BaiduPostActivity.class);
        startActivity(intent);
        finish();
    }






}
