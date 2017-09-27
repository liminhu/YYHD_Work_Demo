package com.gameassist.plugin.common;

import android.content.Context;
import android.database.Cursor;

import com.gameassist.plugin.bean.ChatroomTb;
import com.gameassist.plugin.bean.MessageTb;
import com.gameassist.plugin.utils.MyLog;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hulimin on 2017/9/6.
 */

public class DateBaseMonitor {
    public  static final String CHATROOM_TB_NAME="chatroom";
    private static final String DATABASE_NAME="EnMicroMsg.db";
    private Context context;
    private String clzSqliteDatabase,methodActivDb;
    private Class clzActiveDb;
    private Object sqliteClass;



    public DateBaseMonitor(Context context) {
        this.context = context;
        clzSqliteDatabase=CommonData.CLZ_SQLITE_DATABASE;
        methodActivDb=CommonData.METHOD_ACTIVDB;
    }



    private Object  getSQLiteDatabaseClass(){
        try {
            clzActiveDb = context.getClassLoader().loadClass(clzSqliteDatabase);
            Method activmethod = clzActiveDb.getDeclaredMethod(methodActivDb, new Class[]{});
            activmethod.setAccessible(true);
            // TODO: 2017/9/5 静态方法才能调
            ArrayList activeList = (ArrayList) activmethod.invoke(clzActiveDb);
            MyLog.e(clzSqliteDatabase);
            MyLog.e(methodActivDb);
            if (activeList.size() != 0) {
                for (int i = 0; i < activeList.size(); i++) {
                    if (activeList.get(i).toString().contains(DATABASE_NAME)) {
                        sqliteClass =activeList.get(i);
                        return sqliteClass;
                    }
                }
            }
        }catch (Exception e){

        }
        return null;
    }




    public List<MessageTb> queryMessageFromDB(String sqlQuery) {
        List<MessageTb> list = new ArrayList<>();
        if(sqliteClass ==null){
            sqliteClass =getSQLiteDatabaseClass();
        }

        try{
            if(sqliteClass !=null){
                Method query = sqliteClass.getClass().getDeclaredMethod("rawQuery", String.class, String[].class);
                Cursor cur = (Cursor) query.invoke(sqliteClass, sqlQuery, null);
                while (cur.moveToNext()) {
                    MessageTb mess = new MessageTb();
                    mess.setMsgId(cur.getInt(cur.getColumnIndex(MessageTb._msgId)));
                    mess.setMsgSvrId(cur.getInt(cur.getColumnIndex(MessageTb._msgSvrId)));
                    mess.setType(cur.getInt(cur.getColumnIndex(MessageTb._type)));
                    mess.setStatus(cur.getInt(cur.getColumnIndex(MessageTb._status)));
                    mess.setIsSend(cur.getInt(cur.getColumnIndex(MessageTb._isSend)));
                    mess.setIsShowTimer(cur.getInt(cur.getColumnIndex(MessageTb._isShowTimer)));
                    mess.setCreateTime(cur.getLong(cur.getColumnIndex(MessageTb._createTime)));
                    mess.setTalker(cur.getString(cur.getColumnIndex(MessageTb._talker)));
                    mess.setContent(cur.getString(cur.getColumnIndex(MessageTb._content)));


                    mess.setImgPath(cur.getString(cur.getColumnIndex(MessageTb._imgPath)));
                    mess.setReserved(cur.getString(cur.getColumnIndex(MessageTb._reserved)));
                    byte[] data = cur.getBlob(cur.getColumnIndex(MessageTb._lvbuffer));
                    mess.setLvbuffer(data);
                    mess.setTransContent(cur.getString(cur.getColumnIndex(MessageTb._transContent)));
                    mess.setTransBrandWording(cur.getString(cur.getColumnIndex(MessageTb._transBrandWording)));
                    mess.setTalkerId(cur.getInt(cur.getColumnIndex(MessageTb._talkerId)));
                    mess.setBizChatId(cur.getInt(cur.getColumnIndex(MessageTb._bizClientMsgId)));
                    mess.setBizChatId(cur.getInt(cur.getColumnIndex(MessageTb._bizChatId)));
                    mess.setBizChatUserId(cur.getString(cur.getColumnIndex(MessageTb._bizChatUserId)));
                    mess.setMsgSeq(cur.getInt(cur.getColumnIndex(MessageTb._msgSeq)));
                    mess.setFlag(cur.getInt(cur.getColumnIndex(MessageTb._flag)));
                    String str = mess.genJSONData();
                    // MyLog.e(str);
                    list.add(mess);
                }
                cur.close();
                return list;
            }
        }catch (Exception e){
            MyLog.e("excep  --- "+e.getMessage());
        }
        return  null;

    }





     public ChatroomTb getChatRoomInfoByUserName(String tableName, String chatroomName) {
         String sqlQuery = "select * from " + tableName + " where chatroomname='" + chatroomName + "' ";
         MyLog.e(sqlQuery);
         ChatroomTb chatroomTb = null;
         if (sqliteClass == null) {
             sqliteClass = getSQLiteDatabaseClass();
         }

         try {
             if (sqliteClass != null) {
                 Method query = sqliteClass.getClass().getDeclaredMethod("rawQuery", String.class, String[].class);
                 Cursor cur = (Cursor) query.invoke(sqliteClass, sqlQuery, null);
                 if (cur.moveToNext()) {
                     chatroomTb=new ChatroomTb();
                     chatroomTb.setChatroom_name(chatroomName);
                     chatroomTb.setDisplay_name(cur.getString(cur.getColumnIndex(ChatroomTb.DISPLAY_NAME)));
                     chatroomTb.setMember_list(cur.getString(cur.getColumnIndex(ChatroomTb.MEMBER_LIST)));
                     chatroomTb.setRoom_owner(cur.getString(cur.getColumnIndex(ChatroomTb.ROOM_OWNER)));
                     MyLog.e("user name  --- "+cur.getString(cur.getColumnIndex(ChatroomTb.ROOM_OWNER)));
                 }
                 cur.close();
             }
         } catch (Exception e) {

         }
         return chatroomTb;
     }


}
