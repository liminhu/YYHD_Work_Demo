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
public class TianyiWebViewClientBase extends WebViewClient {
    private Handler handler;
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
      //  super.shouldOverrideUrlLoading(view, url);  //这个返回的方法会调用父类方法，也就是跳转至手机浏览器
/*        if(!url.equals("app360://yunpan_run")){
            view.loadUrl(url);
        }*/
 // return true 表示当前url即使是重定向url也不会再执行（除了在return true之前使用webview.loadUrl(url)除外，因为这个会重新加载）
  //return false  表示由系统执行url，直到不再执行此方法，即加载完重定向的url（即具体的url，不再有重定向）
        return false;
    }

    public TianyiWebViewClientBase(Handler handler){
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
       // view.loadUrl("javascript:window.java_obj.showSource('<head>'+" +
       // "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
        Log.e("hook_url_onPageFinished", url);
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
        Log.e("hook_onLoadResource", ""+view.getUrl());
    }

//ctrl+O:查看所有重载的方法



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
        Log.e("hook_ButtonClickListner", "--->"+url);
        if(url.contains("m.cloud")){
            String J_DownloadJS="J_Download[0].click();";
            view.loadUrl("javascript: var J_Download =document.getElementsByClassName('J_Download'); if(J_Download != null){ "
                    +J_DownloadJS +" }");
        }else if(url.contains("yunpan")){
            Log.v("hook_360_yunpan","-------------->"+ url+"\ntime:"+ System.currentTimeMillis());
            String downloadBtn="var singleViewTpl = document.getElementById('singleViewTpl'); var downloadBtn =document.getElementById(\"downloadBtn\");  ";
            String highSpeed ="var highSpeed =document.getElementById(\"highSpeed\");  var html = document.getElementById('singleViewTpl').innerHTML; ";
            String finaljs="javascript: function func(){"+downloadBtn+"  "+highSpeed
                    + " if(1==1){ if(downloadBtn != null){ downloadBtn.click(); "+       //window.alert("downloadBtn is not null!");  download_link.click();
                    " }else if(highSpeed != null){ window.alert(\"highSpeed is click!\");  }else{ window.alert(\"downloadBtn is null!\"); var download_link=document.getElementById(\"formDownload\");  window.location.Reload();   } " +  //window.location.Reload();   if(download_link != null){window.alert('download_link is not null');}else{"+js+"}
                    "}else{ window.alert(\"singleViewTpl is null!\");} } window.onload=func;";
            view.loadUrl(finaljs);
        }
    }

}
