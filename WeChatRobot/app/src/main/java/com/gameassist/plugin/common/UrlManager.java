package com.gameassist.plugin.common;

import android.content.Context;

import com.gameassist.plugin.bean.TokenBean;
import com.gameassist.plugin.utils.MyLog;
import com.gameassist.plugin.utils.SharedPreferenceUtils;

/**
 * Created by hulimin on 2017/9/17.
 */

public class UrlManager {

    public static  final  String CURRENT_IP="192.168.1.54:8887";
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
        sb.append("?user_name=");
        sb.append(tokenBean.username);
        sb.append("&token=");
        sb.append(tokenBean.token);
        sb.append("&group_id=");
        sb.append(groupId);
        sb.append("&group_name=");  //测试
        sb.append(groupName);
        MyLog.e("genWebSocketUrl --- "+sb.toString());
        return  sb.toString();
    }


}
