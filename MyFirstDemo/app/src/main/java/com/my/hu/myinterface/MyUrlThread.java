package com.my.hu.myinterface;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by hu on 9/9/16.
 */
public class MyUrlThread implements Runnable {
    private  static final int TIMEOUT_IN_MILLIONS = 5000;
    private Handler handler;
    private String url_Interface;

    public MyUrlThread(Handler handler, String url) {
        this.handler=handler;
        this.url_Interface=url;
    }



    @Override
    public void run() {
        Log.v("hook_sendmessage","run_data_begin");
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            url = new URL(url_Interface);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
            conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
          //  Log.v("hook_getCode_url", url_Interface);
          //  Log.v("hook_getResponseCode", String.valueOf(conn.getResponseCode()));
            if (conn.getResponseCode() == 200) {
                Log.v("hook_getResponseCode","200");
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                int len = -1;
                byte[] buf = new byte[128];

                while ((len = is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                baos.flush();
                String result=baos.toString();
                Message msg=Message.obtain();
                msg.what=1;
                Bundle b=new Bundle();
                b.putString("data", baos.toString());
                msg.setData(b);
                Log.v("hook_sendmessage","data");
                handler.sendMessage(msg);
            } else {
                throw new RuntimeException(" responseCode is not 200 ... ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null)
                    baos.close();
                if (is != null)
                    is.close();
                if(conn!=null) {
                    conn.disconnect();
                }
            } catch (IOException e) {
                Log.e("hook_Interface","url request exception");
            }
        }
    };
}
