package com.my.hu.webviewjsinjectv2.webview;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.DownloadListener;

/**
 * Created by hu on 9/6/16.
 * WebView默认没有开启文件下载的功能，如果要实现文件下载的功能，
 * 需要设置WebView的DownloadListener，通过实现自己的DownloadListener
 * 来实现文件的下载
 */
public class WebViewDownLoadListener implements DownloadListener {
    private String tag="hook_DownLoadListener";
    private Handler handler;

    public WebViewDownLoadListener(Handler handler){
        this.handler=handler;
    }


    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        Log.e(tag,"onDownloadStart_url_:"+ url);
        Message msg= Message.obtain();
        msg.what=1;
        Bundle b=new Bundle();
        b.putString("data", url);
        msg.setData(b);
        handler.sendMessage(msg);
    }

}
