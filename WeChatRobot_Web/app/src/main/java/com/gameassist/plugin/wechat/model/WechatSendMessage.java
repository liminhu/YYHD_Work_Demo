package com.gameassist.plugin.wechat.model;

import android.os.Message;
import android.text.TextUtils;

import com.gameassist.plugin.mm.robot.PluginEntry;
import com.gameassist.plugin.utils.MyLog;
import com.gameassist.plugin.wechat.utils.CommonUtils;

import org.json.JSONObject;

/**
 * Created by hulimin on 2017/10/19.
 */

public class WechatSendMessage {
    private String upload_url="https://file2.wx.qq.com/cgi-bin/mmwebwx-bin/webwxuploadmedia?f=json";


    public static JSONObject getTextMessageParmentJsonStr(WechatMeta wechatMeta, String content) {
      //  MyLog.e("getTextMessageParmentJsonStr ****");
        JSONObject js=new JSONObject();
        try {
            js.put("Type", 1);
            js.put("Content", content);
            if(TextUtils.isEmpty(content)){
                js.put("FromUserName", "");
                js.put("ToUserName", "");
            }else{
                js.put("FromUserName", wechatMeta.user.UserName);
                js.put("ToUserName", PluginEntry.ToUserName);
            }
            String id=String.valueOf(System.currentTimeMillis())+ CommonUtils.genRandom(4);
            js.put("LocalID", id);
            js.put("ClientMsgId", id);
          //  MyLog.e(js.toString());
        } catch (Exception e) {
            // TODO: handle exception
            MyLog.e("exception --- "+e.getMessage());
        }
        return js;
    }




    public static String genSendTextWebwxUrl(WechatMeta urlBean) {
        StringBuilder sb=new StringBuilder();
        sb.append(urlBean.base_uri);
        sb.append("/webwxsendmsg?pass_ticket=");
        sb.append(urlBean.pass_ticket);
        String result=sb.toString();
       // MyLog.e("genSendTextWebwxUrl : "+result);
        return result;
    }



    public static String genWebwxSendEmoticonUrl(WechatMeta urlBean) {
        StringBuilder sb=new StringBuilder();
        sb.append(urlBean.base_uri);
        sb.append("/webwxsendemoticon?fun=sys&f=json&pass_ticket=");
        sb.append(urlBean.pass_ticket);
        String result=sb.toString();
        MyLog.e("genWebwxSendEmoticonUrl : "+result);
        return result;
    }



    public static JSONObject  getEmotionParmentJsonStr(WechatMeta wechatMetam, String media_id) {
        MyLog.e("getEmotionParmentJsonStr ****");
        JSONObject js=new JSONObject();
        try {
            js.put("Type", 47);
            js.put("MediaId", media_id);
            js.put("EmojiFlag", 2);
            js.put("FromUserName", wechatMetam.user.UserName);
            js.put("ToUserName", PluginEntry.ToUserName);
            String id=String.valueOf(System.currentTimeMillis())+ CommonUtils.genRandom(4);
            js.put("LocalID", id);
            js.put("ClientMsgId", id);
            MyLog.e(js.toString());
        } catch (Exception e) {
           MyLog.e("exception --- "+e.getMessage());
        }
        return js;
    }


}
