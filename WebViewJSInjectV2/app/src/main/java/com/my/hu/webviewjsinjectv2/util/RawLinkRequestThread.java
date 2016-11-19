package com.my.hu.webviewjsinjectv2.util;

import android.os.Handler;
import android.util.Log;

/**
 * Created by hu on 9/21/16.
 */
public class RawLinkRequestThread implements  Runnable{
    private  static final int TIMEOUT_IN_MILLIONS = 5000;
    private Handler handler;
    private String request_url;

    public RawLinkRequestThread(Handler handler, String url) {
        this.handler=handler;
        this.request_url=url;
    }

    @Override
    public void run() {
        Log.v("hook_sendmessage","run_data_begin");
        HttpUtils.requestData(handler,request_url);
    };

}
