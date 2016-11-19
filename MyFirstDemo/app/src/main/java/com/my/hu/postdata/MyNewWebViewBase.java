package com.my.hu.postdata;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.my.hu.demain.MyWebViewDownLoadListener;
import com.my.hu.demain.WebViewClientBase;

/**
 * Created by hu on 9/13/16.
 */
public class MyNewWebViewBase extends WebView{
    private  String test_url;
    private Activity mActivity;
    private WebViewClientBase mWebViewClientBase;
    private WebChromeClientBase mWebChromeClientBase;

    private ProgressBar progressBar;
    private TextView textView;
    private String platform_name;
    private String md5;
    private Handler handler;



    public MyNewWebViewBase(Handler handler,Context context, String url, ProgressBar progressBar, TextView textView, String platform_name, String md5){
        super(context);
        Log.v("hook_MyNewWebViewBase","viewbase_begin_2");
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
        this.handler=handler;
        init(context);
    }

    public MyNewWebViewBase(Context context, ProgressBar progressBar, TextView textView, String platform_name,String md5,Handler handler){
        super(context);
        Log.v("hook_MyNewWebViewBase","viewbase_begin");
        mActivity=(Activity)context;
     //   Log.v("hook_MyNewWebViewBase","viewbase_end_0");
        mWebViewClientBase = new WebViewClientBase();
        mWebChromeClientBase = new WebChromeClientBase();
        this.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
     //   Log.v("hook_MyNewWebViewBase","viewbase_end_1");
        this.progressBar=progressBar;
        this.textView=textView;
        this.platform_name=platform_name;
        this.handler=handler;
     //   Log.v("hook_MyNewWebViewBase","viewbase_end_2");
        setMd5(md5);
        init_set_webView(context);
    }



    public String getTest_url() {
        return test_url;
    }

    public void setTest_url(String test_url) {
        this.test_url = test_url;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public void myDestroy() {
        //onDestroy();
        this.removeAllViews();
        this.destroy();
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
        this.setDownloadListener(new MyWebViewDownLoadListener(handler,context,progressBar,textView,platform_name,md5));

        Log.d("hook_html_url",test_url);
        // this.loadDataWithBaseURL(null,test_url, "text/html",  "utf-8", null);
        this.getSettings().setDefaultTextEncodingName("utf-8");
        loadUrl(test_url);
       // this.onResume();
        // this.destroy();
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void init_set_webView(Context context){
        Log.d("hook_html_init","init context");
        WebSettings webSettings=this.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        Log.v("hook_this",this.toString());
        this.addJavascriptInterface(new InJavaScriptLocalObj(), "java_obj");
        this.setWebViewClient(mWebViewClientBase);
        this.setWebChromeClient(mWebChromeClientBase);
        this.setDownloadListener(new MyWebViewDownLoadListener(handler,context,progressBar,textView,platform_name,md5));
        this.getSettings().setDefaultTextEncodingName("utf-8");
        //this.loadUrl(test_url);
    }


    public void loadMyUrl(String url){
        setTest_url(url);
        Log.v("hook_request",test_url);
        this.loadUrl(test_url);   //this 对象有问题
        //this.onResume();
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
