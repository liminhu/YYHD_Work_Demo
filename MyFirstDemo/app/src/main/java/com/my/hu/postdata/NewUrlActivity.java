package com.my.hu.postdata;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.my.hu.datalist.DataListActivity;
import com.my.hu.myfirstdemo.MyWebViewBase;
import com.my.hu.myfirstdemo.R;
import com.my.hu.myinterface.JsonData;
import com.my.hu.myinterface.NewSqliteDBHelper;
import com.my.hu.myinterface.ShowDataActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class NewUrlActivity extends AppCompatActivity {
    private  static final String BAIDU_PLATFORM="baidu";
    private  static final String TIANYI_PLATFORM="tianyi";
    private  static final String QH_360_PLATFORM="360";

    private ProgressBar progressBar;
    private TextView textView;


    LinkedList<JsonData> jsonDatas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_url);
    }



    public void saveTianyi(View view){
        Log.v("hook_saveTianyi","saveTianyi");
        NewSqliteDBHelper dbHelper=new NewSqliteDBHelper(this);
        List<JsonData> jsonDataList=dbHelper.getOldUrl_TB_DataList();
        dbHelper.closeDataBase();
        for(int i=0; i<jsonDataList.size(); i++){
            JsonData json=jsonDataList.get(i);
            Log.v("hook_saveTianyi_md5",json.md5);
            if(json.platform_name.equals(TIANYI_PLATFORM)){
               // setContentView(R.layout.set_progressbar);
                progressBar=(ProgressBar) findViewById(R.id.bar);
                textView=(TextView)findViewById(R.id.tv);
                WebView webView=new MyWebViewBase(this, json.old_url,progressBar,textView, json.platform_name,json.md5);
                Log.v("hook_auto_ty",json.old_url);
            }else{
                Log.v("hook_auto_no",json.old_url);
            }
        }
        if(jsonDataList.size()>0) {
            new AlertDialog.Builder(NewUrlActivity.this)
                    .setTitle("title").setMessage("天翼直链已保存到数据库")
                    .setPositiveButton("close", null).show();
        }else{
            new AlertDialog.Builder(NewUrlActivity.this)
                    .setTitle("title").setMessage("原始数据库为空，请先测试从服务器拿原始json data 保存到本地数据库!!!")
                    .setPositiveButton("close", null).show();
        }

    }




    public void saveQh360(View view){
        Log.v("hook_360","saveQh360");
        NewSqliteDBHelper dbHelper=new NewSqliteDBHelper(this);
        List<JsonData> jsonDataList=dbHelper.getOldUrl_TB_DataList();
        dbHelper.closeDataBase();
        ArrayList<JsonData> jsonArray=new ArrayList<JsonData>();
        for(int i=0; i<jsonDataList.size(); i++){
            JsonData json=jsonDataList.get(i);
            if(json.platform_name.equals(QH_360_PLATFORM)){
                jsonArray.add(json);
            }
        }
        Intent intent=new Intent(NewUrlActivity.this,MyNewUrlActivity.class);
        intent.putExtra("jsonArray", jsonArray);
        startActivity(intent);
    }

    private class WebviewThread implements Runnable{
        private String platform_name;
        private String md5;
        private Context context;
        private String old_url;
        private ProgressBar progressBar;
        private TextView textView;

        public WebviewThread(Context context, String old_url, String platform_name, String md5, ProgressBar progressBar,TextView textView){
            this.platform_name=platform_name;
            this.md5=md5;
            this.old_url=old_url;
            this.context=context;
            this.textView=textView;
            this.progressBar=progressBar;
            Log.v("hook_thread_run","WebviewThread-构造函数");
        }

        @Override
        public void run() {
            Log.v("hook_thread_run","run is execute");
            Log.v("hook_thread_old_url",old_url);
            WebView webView=new MyWebViewBase(context, old_url, progressBar,textView, platform_name, md5);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop(){
      //  System.exit(0);  //自杀
        super.onStop();
    }


    public void saveBaidu(View view){
        Log.v("hook_saveBaidu","saveBaidu");
        NewSqliteDBHelper dbHelper=new NewSqliteDBHelper(this);
        List<JsonData> jsonDataList=dbHelper.getOldUrl_TB_DataList();
        dbHelper.closeDataBase();
        ArrayList<JsonData> jsonArray=new ArrayList<JsonData>();
        for(int i=0; i<jsonDataList.size(); i++){
            JsonData json=jsonDataList.get(i);
            if(json.platform_name.equals(BAIDU_PLATFORM)){
                jsonArray.add(json);
            }
        }
        Intent intent=new Intent(NewUrlActivity.this,MyNewUrlActivity.class);
        intent.putExtra("jsonArray", jsonArray);
        startActivity(intent);
    }


    public void showAllLinks(View view){
        Intent intent=new Intent(NewUrlActivity.this, DataListActivity.class);
        intent.putExtra("view_page","0");
        startActivity(intent);
    }





}
