package com.my.hu.postdata;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.my.hu.Util.StringUtil;
import com.my.hu.demain.MyProgressDialog;

/**
 * Created by hu on 8/30/16.
 */
public class WebView360ClientBase extends WebViewClient {
    private MyProgressDialog progressDialog;


    public WebView360ClientBase(){
        super();
    }
    public WebView360ClientBase(MyProgressDialog progressDialog){
        this.progressDialog=progressDialog;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        super.shouldOverrideUrlLoading(view, url);
        if(!url.equals("app360://yunpan_run")){
            view.loadUrl(url);
        }
        return true;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if(url.equals("app360://yunpan_run")){
            return;
        }
     //   Log.d("hook_url_onPageStarted", url);
        super.onPageStarted(view, url, favicon);
       // progressDialog.showProgressDialog();

    }

    @Override
    public void onPageFinished(WebView view, String url) {
        // TODO Auto-generated method stub
       // view.loadUrl("javascript:window.java_obj.getSource('<head>'+" +
       //         "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
        Log.e("hook_url_onPageFinished", url);
        addButtonClickListner(view,url);
        super.onPageFinished(view, url);
    }




    @Override
    public void onReceivedError(WebView view, int errorCode,
                                String description, String failingUrl) {
        // TODO Auto-generated method stub
        super.onReceivedError(view, errorCode, description, failingUrl);
        Log.e("hook_url_rceivedError", failingUrl);
        //view.loadUrl(failingUrl);
        progressDialog.closeProgressDialog();
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url,
                                       boolean isReload) {
        // TODO Auto-generated method stub
        super.doUpdateVisitedHistory(view, url, isReload);
    }



    //注入js函数监听
    private void addButtonClickListner(WebView view, String url){
        Log.e("hook_ButtonClickListner", "-------------->"+url);
        Log.v("hook_360_yunpan","-------------->"+ url+"\ntime:"+ System.currentTimeMillis());
        String downloadBtn="var singleViewTpl = document.getElementById('singleViewTpl'); var downloadBtn =document.getElementById(\"downloadBtn\");  ";
        String highSpeed ="var highSpeed =document.getElementById(\"highSpeed\");  var html = document.getElementById('singleViewTpl').innerHTML; ";
        String finaljs="javascript: function func(){"+downloadBtn+"  "+highSpeed
                    + " if(1==1){ if(downloadBtn != null){  downloadBtn.click(); "+       //window.alert("downloadBtn is not null!");  download_link.click();
                    " }else if(highSpeed != null){ window.alert(\"highSpeed is click!\");  }else{var download_link=document.getElementById(\"formDownload\");    } " +  //window.location.Reload();   if(download_link != null){window.alert('download_link is not null');}else{"+js+"}
                    "}else{  } window.onload=func;";
        Log.v("hook_js",finaljs);
        view.loadUrl(finaljs);
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
                    Log.d("hook_data", new String(array));
                } else {
                    System.arraycopy(html.getBytes(), index - 1023, array, 0, len - index);
                    Log.d("hook_data", new String(array));
                }
            }
        }
    }
}
