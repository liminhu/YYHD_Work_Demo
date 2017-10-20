package com.gameassist.plugin.common;

import android.content.Context;
import android.util.Base64;

import com.gameassist.plugin.bean.TokenBean;
import com.gameassist.plugin.utils.MyLog;
import com.gameassist.plugin.utils.SharedPreferenceUtils;
import com.gameassist.plugin.utils.StringUtils;

/**
 * Created by hulimin on 2017/9/17.
 */

public class UrlManager {
    public static String upload_qr_url="/redbull/app/upload_qrcode";

    public static    String CURRENT_IP="192.168.1.215:8887";  //redbull.ggdawanjia.com
    public static final  String LOGIN_URL="/redbull/app/login";
    public static final  String LOGIN_OUT_URL="/redbull/app/logout";

    public static final String   WS_PROTOCOL="ws";
    public static final String   HTTP_PROTOCOL="http";

    //  public static  final  String CURRENT_IP="101.200.146.196:8887";  //yun
    public static String getLoginOutTokenUrl(String protocol){
        StringBuilder sb=new StringBuilder();
        sb.append(protocol);
        sb.append("://");
        sb.append(CURRENT_IP);
        sb.append(LOGIN_OUT_URL);
        MyLog.e("login _ token -- "+sb.toString());
        String loginUrl=sb.toString();
        return loginUrl;
    }


    public static String getUpload_qrcode(String protocol){
        StringBuilder sb=new StringBuilder();
        sb.append(protocol);
        sb.append("://");
        sb.append(CURRENT_IP);
        sb.append(upload_qr_url);
        MyLog.e("lupload_qr_url -- "+sb.toString());
        String loginUrl=sb.toString();
        return loginUrl;
    }

    public static String getTokenUrl(String protocol){
        StringBuilder sb=new StringBuilder();
        sb.append(protocol);
        sb.append("://");
        sb.append(CURRENT_IP);
        sb.append(LOGIN_URL);
        MyLog.e("login _ token -- "+sb.toString());
        String loginUrl=sb.toString();
        return loginUrl;
    }



    public static String genWebSocketUrl(Context context, String groupId, String groupName){
        String protocol= WS_PROTOCOL;
        TokenBean tokenBean= SharedPreferenceUtils.getTokenFromSharedPre(context);
        StringBuilder sb=new StringBuilder();
        sb.append(protocol);
        sb.append("://");
        sb.append(CURRENT_IP);
      //  sb.append("?user_name=");
      //  sb.append(tokenBean.username);
        sb.append("?token=");
        sb.append(tokenBean.token);
        sb.append("&group_id=");
        sb.append(groupId);
        sb.append("&group_name=");  //测试
        sb.append(StringUtils.byteArrayToHexString(groupName.getBytes())); //转成hex string 以免乱码
        MyLog.e("genWebSocketUrl --- "+sb.toString());
        return   sb.toString();
    }

}
