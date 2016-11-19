package com.my.hu.webviewjsinjectv2.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.my.hu.webviewjsinjectv2.domain.ConstantData;

import java.util.Map;

/**
 * Created by hu on 9/23/16.
 */
public class DownLoadUrlPostThread implements Runnable{
        private Map<String,String> map;
        private Handler handler;
        private String link_url;

        public DownLoadUrlPostThread(Map<String,String> map,Handler handler, String link_url){
            this.map=map;
            this.handler=handler;
            this.link_url=link_url;
        }
        @Override
        public void run() {
            String data= HttpUtils.submitPostData(ConstantData.SUBMIT_DATA_LINK_URL,map,"utf-8");
            Message msg=Message.obtain();
            msg.what=1;
            Bundle b=new Bundle();
            b.putString("data", data);
            b.putString("url", link_url);
            msg.setData(b);
            handler.sendMessage(msg);
        }

}
