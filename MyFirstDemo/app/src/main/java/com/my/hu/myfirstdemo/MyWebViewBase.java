package com.my.hu.myfirstdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.my.hu.demain.MyProgressDialog;
import com.my.hu.demain.MyWebViewDownLoadListener;
import com.my.hu.demain.WebViewClientBase;
public class MyWebViewBase extends WebView{
    private  String test_url;

    private Activity mActivity;
    private WebViewClientBase mWebViewClientBase;
    private WebChromeClientBase mWebChromeClientBase;
   // private MyProgressDialog progressDialog;


    private ProgressBar progressBar;
    private TextView textView;
    private String platform_name;
    private String md5;

    public MyWebViewBase(Context context, String url, ProgressBar progressBar,TextView textView){
        super(context);
        Log.v("hook_MyWebViewBase","MyWebViewBase_1");
        this.test_url=url;
        mActivity=(Activity)context;
       // progressDialog=new MyProgressDialog(context);
        mWebViewClientBase = new WebViewClientBase();
        mWebChromeClientBase = new WebChromeClientBase();
        this.progressBar=progressBar;
        this.textView=textView;
        platform_name="";
        md5="";
        init(context);
    }


    public MyWebViewBase(Context context, String url, ProgressBar progressBar,TextView textView, String platform_name, String md5){
        super(context);
        Log.v("hook_MyWebViewBase","viewbase_begin_2");
        this.test_url=url;
        mActivity=(Activity)context;
        // progressDialog=new MyProgressDialog(context);
        mWebViewClientBase = new WebViewClientBase();
        mWebChromeClientBase = new WebChromeClientBase();
        this.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        this.progressBar=progressBar;
        this.textView=textView;
        this.platform_name=platform_name;
        this.md5=md5;
        init(context);
      //  Log.v("hook_test","viewbase");
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init(Context context){
        Log.d("hook_html_init","init context");
        WebSettings webSettings=this.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("UTF-8");

        this.addJavascriptInterface(new InJavaScriptLocalObj(), "java_obj");
        this.setWebViewClient(mWebViewClientBase);
        this.setWebChromeClient(mWebChromeClientBase);
        this.setDownloadListener(new MyWebViewDownLoadListener(context,progressBar,textView,platform_name,md5));

        Log.d("hook_html_url",test_url);
       // this.loadDataWithBaseURL(null,test_url, "text/html",  "utf-8", null);
        this.getSettings().setDefaultTextEncodingName("utf-8");
        this.loadUrl(test_url);
       // this.onResume();
       // this.destroy();
    }


    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void getSource(String html) {
            int len = html.length();
            //   Log.d("hook_html_begin", html);
            Log.d("hook_html_len",String.valueOf(len));
            int index = 0;
            byte[] array = new byte[1024];
            for (int i = 0; i < len; i += 1023) {
                index += 1023;
                if (index < len) {
                    System.arraycopy(html.getBytes(), index - 1023, array, 0, 1023);
                    Log.d("hook_data", new String(array));
                } else {
                    System.arraycopy(html.getBytes(), index - 1023, array, 0, len - index);
                    Log.d("hook_data", new String(array));
                }
            }

            System.arraycopy(html.getBytes(), index - 1023, array, 0, len - index);
            Log.d("hook_data", new String(array));

        }
    }


    private class WebChromeClientBase extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mActivity.setProgress(newProgress * 1000);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            // TODO Auto-generated method stub
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onReceivedTouchIconUrl(WebView view, String url,
                                           boolean precomposed) {
            // TODO Auto-generated method stub
            super.onReceivedTouchIconUrl(view, url, precomposed);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog,
                                      boolean isUserGesture, Message resultMsg) {
            // TODO Auto-generated method stub
            return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        }
    }


}
