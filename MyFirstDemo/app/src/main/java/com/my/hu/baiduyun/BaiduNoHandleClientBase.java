package com.my.hu.baiduyun;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.my.hu.Util.SqliteDataHelper;
import com.my.hu.Util.StringUtil;
import com.my.hu.demain.MyProgressDialog;
import com.my.hu.demain.UrlInfo;

import java.util.Date;

/**
 * Created by hu on 8/30/16.
 */
public class BaiduNoHandleClientBase extends WebViewClient {
    public BaiduNoHandleClientBase(){
    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        super.shouldOverrideUrlLoading(view, url);
        return true;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        //view.loadUrl("javascript:window.java_obj.getSource('<head>'+" +
       // "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
        Log.e("hook_url_onPageFinished", url);
        super.onPageFinished(view, url);
        addButtonClickListner(view,url);
    }


    @Override
    public void onReceivedError(WebView view, int errorCode,
                                String description, String failingUrl) {
        // TODO Auto-generated method stub
        super.onReceivedError(view, errorCode, description, failingUrl);
        Log.e("hook_url_rceivedError", failingUrl);
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url,
                                       boolean isReload) {
        // TODO Auto-generated method stub
        super.doUpdateVisitedHistory(view, url, isReload);
    }



    //注入js函数监听
    private void addButtonClickListner(WebView view, String url){
        String newJs="javascript:(function(){if(window.Pass&&Pass.client&&Pass.client.net){Pass.client.net()}}())";
        Log.e("hook_ButtonClickListner", "-------------->"+url);
        String userName="1248024149@qq.com";
        String userNameBtn="document.getElementById('login-username').value='"+userName+"';  window.alert('"+newJs+"');";
        String password="123asd";
        String passwordBtn="document.getElementById('login-password').value='"+password+"'; ";
        String submitBtn="var submit=document.getElementById('login-submit');";
        String clickSubmit="submit.onclick=function(){window.alert('submit is click!!!'); }; submit.click(); ";
        String js=userNameBtn+"\t"+passwordBtn+"\t"+submitBtn+"\t"+clickSubmit;
        Log.v("hook_js_baidu",js);
        view.loadUrl("javascript: "+js);

       // view.loadUrl(newJs);
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
}
