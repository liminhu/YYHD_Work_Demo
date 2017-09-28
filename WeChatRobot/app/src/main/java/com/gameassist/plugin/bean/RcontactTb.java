package com.gameassist.plugin.bean;

import com.gameassist.plugin.common.DateBaseMonitor;

/**
 * Created by hulimin on 2017/9/27.
 */

public class RcontactTb {
    private static final  String RCONTACK_TB_NAME ="rcontact";
    public String username;
    public String nickname;

    public static final String USERNAME="username";
    public static final String NICKNAME="nickname";



    public static RcontactTb getChatroomNickNameByUserNameFromDb(String username, DateBaseMonitor dateBaseMonitor){
        return dateBaseMonitor.getChatroomNickNameByUserNameFromDb(RCONTACK_TB_NAME, username);
    }

}
