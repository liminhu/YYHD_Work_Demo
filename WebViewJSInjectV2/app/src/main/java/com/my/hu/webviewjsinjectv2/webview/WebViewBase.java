package com.my.hu.webviewjsinjectv2.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by hu on 9/13/16.
 */
public class WebViewBase extends WebView {
    private Handler handler;
    public WebViewBase(Context context){
        super(context);
        initWebSetting();
    }

    public WebViewBase(Context context, Handler handler){
        super(context);
        this.handler=handler;
        initBaiduTwoWebSetting();
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

    @SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
    private void initBaiduTwoWebSetting(){
        Log.d("hook_html_init","init context");
        WebSettings webSettings=getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
       // addJavascriptInterface(new InJavaScriptLocalObj(), "java_obj");
        Log.i("hook_baidu_two","addJavascriptInterface");
        addJavascriptInterface(getHtmlObject(),"js_obj");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && canGoBack()) {
            goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            onScrollChanged(getScrollX(), getScrollY(), getScrollX(), getScrollY());
        }
        return super.onTouchEvent(ev);
    }


    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            int len = html.length();
            Log.d("hook_showSource_len",String.valueOf(len));
            int index = 0;
            byte[] array = new byte[1024];
            for (int i = 0; i < len; i += 1023) {
                index += 1023;
                if (index < len) {
                    System.arraycopy(html.getBytes(), index - 1023, array, 0, 1023);
                    Log.d("hook_data_1", new String(array));
                } else {
                    System.arraycopy(html.getBytes(), index - 1023, array, 0, len - index);
                    Log.d("hook_data_1", new String(array));
                }
            }
        }
    }



    private Object getHtmlObject(){
        Object insertObj=new Object(){
            public String HtmlcallJava(){
                Log.i("hook_getHtmlObject","Html call java");
                handler.obtainMessage(0).sendToTarget();
                return "Html call java";
            }
        };
        return  insertObj;
    };

}
