package com.my.hu.webviewjsinjectv2.webview;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by hu on 8/30/16.
 */
public class BaiduTwoWebViewClientBase extends WebViewClient {
    private Handler handler;
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }

    public BaiduTwoWebViewClientBase(Handler handler){
        Log.i("hook_baidu","BaiduTwoWebViewClientBase");
        this.handler=handler;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if(url.equals("app360://yunpan_run")){
            return;
        }
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
       view.loadUrl("javascript:window.java_obj.showSource('<head>'+" +
          "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
        Log.e("hook_url_onPage_1", url);
        super.onPageFinished(view,url);
        addButtonClickListner(view,url);
    }

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        super.onReceivedHttpError(view, request, errorResponse);
        Log.e("hook_HttpError", view.getUrl());
    }


    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        Log.e("hook_onLoad_baidu", ""+view.getUrl());
    }

//ctrl+O:查看所有重载的方法



    @Override
    public void onReceivedError(WebView view, int errorCode,
                                String description, String failingUrl) {
        // TODO Auto-generated method stub
        super.onReceivedError(view, errorCode, description, failingUrl);
        Log.e("hook_url_Error_baidu", failingUrl);
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url,
                                       boolean isReload) {
        // TODO Auto-generated method stub
        super.doUpdateVisitedHistory(view, url, isReload);
    }



    //注入js函数监听
    private void addButtonClickListner(WebView view, String url){
        Log.e("hook_Button_baidu_two", "--->"+url);
        if (url.contains("pan.baidu.com")) {
            String downLoadJS="objs[0].click();";
            String highDownload="obj.click();";
            view.loadUrl("javascript: var obj = document.getElementById('highDownload'); var objs =document.getElementsByClassName('btn normal-download'); "
                    + " if(objs != null){"+downLoadJS+
                    " }else{ window.js_obj.HtmlcallJava(); }");
        }
    }



}
