package com.gameassist.plugin.common;

import android.app.Activity;

import com.gameassist.plugin.bean.MessageTb;
import com.gameassist.plugin.utils.MyLog;
import com.gameassist.plugin.utils.ReflectionUtils;

import java.util.List;


/**
 * Created by hulimin on 2017/9/19.
 */

public class InitDataManager {
    public static  boolean isMainActivity(Activity activity){
        if(activity.toString().contains(CommonData.FIRST_MAIN_ACTIVITY) || activity.toString().contains(CommonData.SECOND_MAIN_ACTIVITY)){
            return true;
        }
        return false;
    }



    public  static String  getCurrentChatRoomNameByActivity(Activity currentActivity){
        if(currentActivity.toString().contains(CommonData.SECOND_MAIN_ACTIVITY)){
            String  chatroom=currentActivity.getIntent().getStringExtra("Chat_User");
            MyLog.e(currentActivity.toString() + "\tchatroom  ---- "+chatroom);
            return chatroom;
        }

        if(!currentActivity.getClass().getName().contains(CommonData.FIRST_MAIN_ACTIVITY)){
            return null;
        }
        Object homeUI= ReflectionUtils.getValue(currentActivity, "uPX");
        if(homeUI!=null){
            Object uPb= ReflectionUtils.getValue(homeUI, "uPb");
            return (String) uPb;
        }
        return  null;
    }



    public static  Long initBeginMesasgeTime(DateBaseMonitor dm, String currentChatRoomName){
        String sqlQuery1 = "select * from message where talker= '"+currentChatRoomName+"' order by createTime DESC limit 1"; //取特定数据开始
        List<MessageTb> list1 = dm.queryMessageFromDB(sqlQuery1);
        Long currentTime=-1L;
        if(list1.size()>0){
            MyLog.e(" ----  "+list1.get(0).getCreateTime());
            currentTime=list1.get(0).getCreateTime();
        }else {
            currentTime = System.currentTimeMillis();
        }
        return  currentTime;
    }


}
