package com.gameassist.plugin.bean;

import android.text.TextUtils;
import android.util.Log;

import com.gameassist.plugin.utils.MyLog;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by hulimin on 2017/9/13.
 */

public class ChatroomTb {
    public static  final String  USER_NAME="username";
    public static  final String  NICK_NAME="nickname";
    public static   final String  DISPLAY_NAME="displayname";
    public static   final String ROOM_OWNER="roomowner";
    public static   final String CHATROOM_NAME="chatroomname";
    public static   final String MEMBER_LIST="memberlist";

    private  String  user_name;
    private  String  nick_name;
    private   String  display_name;
    private   String room_owner;
    private    String chatroom_name;
    private   String member_list;


    public  static   Map<String, String> map=new HashMap<>();

    public  void updateMemberMap(){
         map.clear();
        MyLog.e(MEMBER_LIST+":\t"+member_list);
        MyLog.e(DISPLAY_NAME+":\t"+display_name);
         if(!TextUtils.isEmpty(member_list) && !TextUtils.isEmpty(display_name)){
             String[] member=member_list.split(";");  //wxid_kw5j68dk4wy022;wxid_83kmdbom0g9a22
             String[] display=display_name.split("、"); //lmhu、胡利民
             if(member.length==display.length){
                 for(int i=0; i<member.length; i++){
                     map.put(member[i], display[i]);
                 }
             }
         }
    }


    //通过消息内容，返回发送者的userName
    public static  String getUserNameFormContent(String content, String defaultUserName){
        Iterator<String> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            if(content.contains(key)){
                return key;
            }
        }
        return defaultUserName;
    }



    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public static String getUserName() {
        return USER_NAME;
    }

    public static String getNickName() {
        return NICK_NAME;
    }

    public static String getDisplayName() {
        return DISPLAY_NAME;
    }

    public static String getRoomOwner() {
        return ROOM_OWNER;
    }

    public static String getChatroomName() {
        return CHATROOM_NAME;
    }

    public static String getMemberList() {
        return MEMBER_LIST;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getRoom_owner() {
        return room_owner;
    }

    public void setRoom_owner(String room_owner) {
        this.room_owner = room_owner;
    }

    public String getChatroom_name() {
        return chatroom_name;
    }

    public void setChatroom_name(String chatroom_name) {
        this.chatroom_name = chatroom_name;
    }

    public String getMember_list() {
        return member_list;
    }

    public void setMember_list(String member_list) {
        this.member_list = member_list;
    }
}
