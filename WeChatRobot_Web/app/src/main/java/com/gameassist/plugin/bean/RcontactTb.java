package com.gameassist.plugin.bean;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.gameassist.plugin.common.DateBaseMonitor;
import com.gameassist.plugin.utils.MyLog;

/**
 * Created by hulimin on 2017/9/27.
 */

public class RcontactTb {
    private static final  String RCONTACK_TB_NAME ="rcontact";
    public String username;
    public String nickname;

    public static final String USERNAME="username";
    public static final String NICKNAME="nickname";
    private static String my_login_weixin_username;


    public static RcontactTb getChatroomNickNameByUserNameFromDb(String username, DateBaseMonitor dateBaseMonitor){
        return dateBaseMonitor.getChatroomNickNameByUserNameFromDb(RCONTACK_TB_NAME, username);
    }


    private static  final String LOGIN_WEIXIN_USERNAME="login_weixin_username";
    private static  final String LOGIN_WEIXIN_USERNAME_PREF_FILE="com.tencent.mm_preferences";




    // TODO: 2017/10/9 ---  通过获取微信的id
    //<string name="login_weixin_username">wxid_kw5j68dk4wy022</string>
    public static String getLoginWeixinUsername(Context context){
        if(TextUtils.isEmpty(my_login_weixin_username)){
            SharedPreferences sp = context.getSharedPreferences(LOGIN_WEIXIN_USERNAME_PREF_FILE, Context.MODE_PRIVATE);
            my_login_weixin_username=sp.getString(LOGIN_WEIXIN_USERNAME, "");
        }
        MyLog.e("login_weixin_username:%s   ---- ", my_login_weixin_username);
        return my_login_weixin_username;
    }
}
