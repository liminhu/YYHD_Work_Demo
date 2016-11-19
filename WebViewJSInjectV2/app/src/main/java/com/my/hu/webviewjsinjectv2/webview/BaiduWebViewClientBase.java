package com.my.hu.webviewjsinjectv2.webview;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by hu on 8/30/16.
 */
public class BaiduWebViewClientBase extends WebViewClient {
    private Handler handler;
    public BaiduWebViewClientBase(Handler handler){
        this.handler=handler;
    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
      //  super.shouldOverrideUrlLoading(view, url);
        if(!url.equals("app360://yunpan_run")){
            view.loadUrl(url);
        }
        return true;  //即使是重定向url也不会再执行
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        Log.i("hook_onPageStarted",url);
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        //view.loadUrl("javascript:window.java_obj.getSource('<head>'+" +
        //"document.getElementsByTagName('html')[0].innerHTML+'</head>');");
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
        if(failingUrl.contains("baiduyun://127.0.0.1/")) {
            Message msg = Message.obtain();
            msg.what = 1;
            Bundle b = new Bundle();
            b.putString("data", failingUrl);
            msg.setData(b);
            handler.sendMessage(msg);
        }
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url,
                                       boolean isReload) {
        // TODO Auto-generated method stub
        super.doUpdateVisitedHistory(view, url, isReload);
    }

    //注入js函数监听
    private void addButtonClickListner(WebView view, String url) {
        Log.e("hook_ButtonClickListner", "-------------->" + url);
        if (url.contains("pan.baidu.com")) {
            String downLoadJS="objs[0].click();";
            String highDownload="obj.click();";
            view.loadUrl("javascript: var obj = document.getElementById('highDownload'); var objs =document.getElementsByClassName('btn normal-download'); "
                    + " if(obj != null){"+highDownload+
                    " }else{ "+downLoadJS+"}");
        }
    }



}
