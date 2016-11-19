package com.my.hu.postdata;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.my.hu.Util.HttpUtils;
import com.my.hu.Util.SqliteDataHelper;
import com.my.hu.baiduyun.BaiduUrlActivity;
import com.my.hu.baiduyun.HightDownloadActivity;
import com.my.hu.demain.UrlInfo;
import com.my.hu.myfirstdemo.R;
import com.my.hu.myfirstdemo.Test1MainActivity;
import com.my.hu.myinterface.JsonActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyPostActivity extends AppCompatActivity {
    private static final String POST_URL="11http://ggapi-2014904031.cn-north-1.elb.amazonaws.com.cn:8010/api/put/dead_pan_links";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);
    }

    public void getJsonData(View view){
        Intent intent=new Intent(MyPostActivity.this, JsonActivity.class);
        startActivity(intent);
    }


    public void getNewUrlData(View view){
        Intent intent=new Intent(MyPostActivity.this, NewUrlActivity.class);
        startActivity(intent);
    }

    public void goToBaiduTest(View view){
        Intent intent=new Intent(MyPostActivity.this, BaiduUrlActivity.class);
        startActivity(intent);
    }


    public void goTohighDownloadTest(View view){
        Intent intent=new Intent(MyPostActivity.this, HightDownloadActivity.class);
        startActivity(intent);
    }

    private Handler postHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Bundle b=msg.getData();
                String data=b.getString("data");
                Log.d("hook_handleMessage",data);
            }else{
                Log.e("hook_handleMessage","网络异常！");
            }
        }
    };

    public void postDataTOServer(View view){
        SqliteDataHelper sqliteDataHelper=new SqliteDataHelper(this);
        List<UrlInfo> urlInfoLists=sqliteDataHelper.getUrlList();
        sqliteDataHelper.closeDB();
        for(int i=0; i<urlInfoLists.size(); i++){
            UrlInfo urlInfo=urlInfoLists.get(i);
            StringBuilder sb=new StringBuilder();
            sb.append("key=");
            sb.append(urlInfo.getMd5());
            sb.append("&"+"url=");
            sb.append(urlInfo.getDownLoad_url());
            Log.v("hook_post",sb.toString());
            Map<String,String> map=new HashMap<String, String>();
            map.put("key",urlInfo.getMd5());
            map.put("url",urlInfo.getDownLoad_url());
            UrlPostThread postThread=new UrlPostThread(map);
            new Thread(postThread).start();
        }
        if(urlInfoLists.size()>0) {
            new AlertDialog.Builder(MyPostActivity.this)
                    .setTitle("title").setMessage("所有新的直链已上传到服务器")
                    .setPositiveButton("close", null).show();
        }else{
            new AlertDialog.Builder(MyPostActivity.this)
                    .setTitle("title").setMessage("上传直链失败，原始直接数据库为空，请先生成直链new url保存到本地数据库!!!")
                    .setPositiveButton("close", null).show();
        }
    }






    private class UrlPostThread implements Runnable{
        private Map<String,String> map;

        public UrlPostThread(Map<String,String> map){
            this.map=map;
        }
        @Override
        public void run() {
             String data= HttpUtils.submitPostData(POST_URL,map,"utf-8");
             Message msg=Message.obtain();
             if(data.equals("-1")){
                 msg.what=-1;
             }else{
                 msg.what=0;
                 Bundle b=new Bundle();
                 b.putString("data",data);
                 msg.setData(b);
             }
            postHandler.sendMessage(msg);
        }
    }

    //goToFirstTest
    public void goToFirstTest(View view){
        Intent intent=new Intent(MyPostActivity.this, Test1MainActivity.class);
        startActivity(intent);
    }


}
