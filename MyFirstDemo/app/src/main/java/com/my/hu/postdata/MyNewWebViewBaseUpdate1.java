package com.my.hu.postdata;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.my.hu.demain.MyWebViewDownLoadListener;

/**
 * Created by hu on 9/13/16.
 */
public class MyNewWebViewBaseUpdate1 extends WebView {
    private Activity mActivity;
    private ProgressBar progressBar;
    private TextView textView;
    private String platform_name;
    private String md5;

//    public MyNewWebViewBaseUpdate1(Context context){
//        super(context);
//        Log.v("hook_MyNewWebViewBase","viewbase_begin");
//        initWebSetting();
//    /*    mActivity=(Activity)context;
//        //   Log.v("hook_MyNewWebViewBase","viewbase_end_0");
//        this.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        //   Log.v("hook_MyNewWebViewBase","viewbase_end_1");
//        this.progressBar=progressBar;
//        this.textView=textView;
//        this.platform_name=platform_name;*/
//    }

    public MyNewWebViewBaseUpdate1(Context context){
        super(context);
        Log.v("hook_MyNewWebViewBase","viewbase_begin");
        initWebSetting();
    /*    mActivity=(Activity)context;
        //   Log.v("hook_MyNewWebViewBase","viewbase_end_0");
        this.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //   Log.v("hook_MyNewWebViewBase","viewbase_end_1");
        this.progressBar=progressBar;
        this.textView=textView;
        this.platform_name=platform_name;*/
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void initWebSetting(WebView webView){
        Log.d("hook_html_init","init context");
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //js open new web window
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
      //  webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "java_obj");
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebSetting(){
        Log.d("hook_html_init","init context");
        WebSettings webSettings=getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        addJavascriptInterface(new InJavaScriptLocalObj(), "java_obj");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && canGoBack()) {
            goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

   /* @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            onScrollChanged(getScrollX(), getScrollY(), getScrollX(), getScrollY());
        }
        return super.onTouchEvent(ev);
    }*/

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            onScrollChanged(getScrollX(), getScrollY(), getScrollX(), getScrollY());
        }
        return super.onTouchEvent(ev);
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
                if (index < len) {
                    System.arraycopy(html.getBytes(), index, array, 0, 1023);
                    Log.d("hook_data_"+i, new String(array));
                } else {
                    System.arraycopy(html.getBytes(), index, array, 0, len - index);
                    Log.d("hook_data_last_"+len, new String(array));
                }
                index += 1023;
                Log.v("hook_index",""+index);
                Log.v("hook_i",""+i);
            }
            System.arraycopy(html.getBytes(), index-1023, array, 0, len - index+1023);
            Log.d("hook_data_last_"+len, new String(array));
        }
    }

}
