package com.my.hu.baiduyun;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.widget.TextView;

import com.my.hu.demain.MyWebViewDownLoadListener;
import com.my.hu.demain.WebViewClientBase;
import com.my.hu.myfirstdemo.R;
import com.my.hu.myinterface.JsonData;
import com.my.hu.postdata.MyNewWebViewBaseUpdate1;

import java.util.List;

public class HightDownloadActivity extends AppCompatActivity {
    private  MyNewWebViewBaseUpdate1 webview;
    private  List<JsonData> jsonDatas;
    private final static int RUN = 2;
    private TextView textView;
    private BaiduWebViewClientBase mWebViewClientBase;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case 0:
                        break;
                    case RUN:
                        Log.d("hook_run_2", "handleMessage");
                        if(jsonDatas.size()>0){
                            JsonData json=jsonDatas.remove(0);
                            textView.setText("正在请求： id--"+json.id+",url:"+json.old_url);
                            mWebViewClientBase.setMd5(json.md5);
                            webview.setWebViewClient(mWebViewClientBase);
                            webview.loadUrl(json.old_url);
                        }else{
                            Intent intent=new Intent(HightDownloadActivity.this,ShowBaiduDataActivity.class);
                            startActivity(intent);
                            /*new AlertDialog.Builder(HightDownloadActivity.this)
                                    .setTitle("title").setMessage("所有新的百度云请求已生成!!!")
                                    .setPositiveButton("close", null).show();*/
                        }
                        break;
                }
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hight_download);
        textView=(TextView)findViewById(R.id.request_url);
        jsonDatas=CommonMethod.getAllJsonDataByPlatName(getBaseContext(),CommonMethod.BAIDU_PLATFORM);
        webview = new MyNewWebViewBaseUpdate1(getBaseContext());
        webview.initWebSetting(webview);
        mWebViewClientBase = new BaiduWebViewClientBase(getBaseContext(),handler);
        webview.setWebChromeClient(new WebChromeClient());
        if(jsonDatas.size()>0){
            handler.obtainMessage(RUN).sendToTarget();
        }else{
            new AlertDialog.Builder(HightDownloadActivity.this)
                    .setTitle("title").setMessage("原始百度云的链接为空!!!")
                    .setPositiveButton("close", null).show();
        }
    }
}
