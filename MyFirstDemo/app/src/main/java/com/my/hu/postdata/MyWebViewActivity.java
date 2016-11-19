package com.my.hu.postdata;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.my.hu.datalist.DataListActivity;
import com.my.hu.demain.MyWebViewDownLoadListener;
import com.my.hu.demain.WebViewClientBase;
import com.my.hu.myfirstdemo.MyWebViewBase;
import com.my.hu.myfirstdemo.R;
import com.my.hu.myinterface.JsonData;

import java.util.ArrayList;

public class MyWebViewActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView textView;
    private ArrayList<JsonData> jsonArray;
    private Context activity;
    private MyNewWebViewBaseUpdate1 webview = null;
    private final static int RUN = 2;
    private JsonData json;

    private Handler handler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case 0:
                        break;
                    case 1:
                        setResult(-1);
                        finish();
                        break;
                    case RUN:
                        Log.d("hook_run_2", "handleMessage");
                        Log.v("hook_MyWebView_url", json.old_url);
                        webview = new MyNewWebViewBaseUpdate1(activity);
                        webview.initWebSetting(webview);
                        WebViewClientBase mWebViewClientBase = new WebViewClientBase();
                        webview.setWebChromeClient(new WebChromeClient());
                        webview.setWebViewClient(mWebViewClientBase);
                        webview.setDownloadListener(new MyWebViewDownLoadListener(handler, activity, progressBar, textView, json.platform_name, json.md5));
                        webview.loadUrl(json.old_url);
                        setContentView(webview);
                        break;
                }
            }
        }
    };


    private class WebViewThread implements Runnable {
        private String url;


        public WebViewThread(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            Log.v("hook_thread_url", url);
            MyNewWebViewBaseUpdate1 webview = new MyNewWebViewBaseUpdate1(activity);
            WebViewClientBase mWebViewClientBase = new WebViewClientBase();
            WebChromeClient mWebChromeClientBase = new WebChromeClient();
            webview.setWebViewClient(mWebViewClientBase);
            webview.setWebChromeClient(mWebChromeClientBase);
            webview.loadUrl(url);
            setContentView(webview);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_web_view);
        Log.v("hook_MyWebViewActivity", "onCreate");
        Intent intent = getIntent();
        progressBar = (ProgressBar) findViewById(R.id.bar);
        textView = (TextView) findViewById(R.id.tv);
        json = (JsonData) intent.getSerializableExtra("json");
        Log.v("hook_json_url", json.old_url);
        if (json != null) {
            handler.obtainMessage(RUN).sendToTarget();
        } else {
            new AlertDialog.Builder(MyWebViewActivity.this)
                    .setTitle("title").setMessage("未发现合适的Url!")
                    .setPositiveButton("close", null).show();

        }
    }


    @Override
    protected void onStop() {
        Log.v("hook_onStop", " System.exit");
        System.exit(0);
        super.onStop();
    }
}


