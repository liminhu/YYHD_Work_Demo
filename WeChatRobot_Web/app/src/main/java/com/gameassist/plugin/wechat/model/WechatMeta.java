package com.gameassist.plugin.wechat.model;

import android.content.Context;

import com.gameassist.plugin.utils.MyLog;
import com.gameassist.plugin.wechat.cookie.PersistentCookieStore;

import org.json.JSONObject;

/**
 * Created by hulimin on 2017/10/18.
 */

public class WechatMeta {
    public static String WXUIN="wxuin";
    public static String WXSID="wxsid";
    public static String SKEY="skey";
    public static String PASS_TICKET="pass_ticket";
    public static String DEVICEID="DeviceID";

    public static String deviceID;
    public static String wxuin;
    public static String wxsid;
    public static String skey;
    public static String pass_ticket;


    public static  String redirect_uri;
    public static  String base_uri;
    //webwx_data_ticket:需要从登录后的Cookie中获取这个字段
    public static String webwx_data_ticket;


    private  String webpush_url;

    public String getWebpush_url() {
        return webpush_url;
    }

    public void setWebpush_url(String webpush_url) {
        this.webpush_url = webpush_url;
    }

    //用户信息
    public static  WechatUser user=new WechatUser();



    public WechatMeta() {
    }

    public WechatMeta(String redirect_uri) {
        super();
        this.redirect_uri=redirect_uri;
        base_uri=getBaseUriFromRedirectUri(redirect_uri);
    }


    public void initData(String data) {
        try {
            MyLog.e(data);
            JSONObject js=new JSONObject(data);
            wxuin=js.getString("Uin");
            skey=js.getString("Skey");
            wxsid=js.getString("Sid");
            deviceID=js.getString("DeviceID");
            pass_ticket=js.getString("pass_ticket");
            base_uri=js.getString("base_uri");
        }catch (Exception e) {
            MyLog.e(e.getMessage());
        }
    }


    public   void saveWechatUserToSharedPre(Context context){
        MyLog.e("save ------- ");
        JSONObject js=new JSONObject();
        try {
            js.put("Uin", user.Uin);
            js.put("UserName", user.UserName);
            js.put("NickName", user.NickName);
            PersistentCookieStore.saveDataToSharedPre(context, "wechatUser", js.toString());
        }catch (Exception e){
            MyLog.e(e.getMessage());
        }
    }


    public   void saveWechatMetaToSharedPre(Context context){
        String data=getJSONStr();
        MyLog.e("save ------- "+data);
        PersistentCookieStore.saveDataToSharedPre(context, "wechatMeta", data);
    }


   public String getJSONStr(){
       JSONObject js=new JSONObject();
       try {
           js.put("Uin", Long.valueOf(wxuin));
           js.put("Skey", skey);
           js.put("Sid", wxsid);
           js.put("DeviceID", deviceID);
           js.put("pass_ticket", pass_ticket);
           js.put("base_uri", base_uri);
           return  js.toString();
       } catch (Exception e) {

       }
       return null;
   }

    private static String getBaseUriFromRedirectUri(String url) {
        int index = url.indexOf('?');
        if (index > 0) {
            String temp = url.substring(0, index);
            System.out.println(temp);
            int end=temp.lastIndexOf('/');
            System.err.println(temp.substring(0, end));
            return temp.substring(0, end);
        }
        return url;
    }

}
