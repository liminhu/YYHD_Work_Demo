package com.gameassist.plugin.wechat.model;

import android.text.TextUtils;

import com.gameassist.plugin.utils.MyLog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hulimin on 2017/10/19.
 */

public class WechatGroup{
    public String UserName;
    public String NickName;






    public static  String getToUserNameFromGroup(List wechatGroupList, List contactList, String chatroom_nickname){
        String ToUserName=null;
        if(wechatGroupList!=null){
            ToUserName=WechatGroup.getGroupUserName(wechatGroupList, chatroom_nickname);
            if(!TextUtils.isEmpty(ToUserName)){
                return  ToUserName;
            }else if(contactList!=null && contactList.size()>0){
                ToUserName=WechatGroup.getGroupUserName(contactList, chatroom_nickname);
                if(!TextUtils.isEmpty(ToUserName)){
                    return  ToUserName;
                }
            }
        }
        MyLog.e("获取当前群ToUserName 信息失败。。。");
        return  ToUserName;
    }




    public static  String getGroupUserName(List<WechatGroup> list, String nickName){
        MyLog.e(nickName);
        for(int i=0; i<list.size(); i++){
            MyLog.e("is %s.... ", list.get(i).NickName);
            if(list.get(i).NickName.equals(nickName)){
                MyLog.e(list.get(i).UserName);
                return list.get(i).UserName;
            }
        }
      //  MyLog.e("is null.... ");
        return null;
    }


    public static  List<WechatGroup> getContactList(String data) {
        try{
         //   MyLog.e("getContactList ---- "+data);
            JSONObject js=new JSONObject(data);
            JSONObject baseResponse=js.getJSONObject("BaseResponse");
            int Ret=baseResponse.getInt("Ret");
            if(Ret!=0) {
                MyLog.e(baseResponse.toString());
                return null;
            }
            int MemberCount=js.getInt("Count");
            List<WechatGroup> groupBeans=null;
            if(MemberCount>0) {
                groupBeans=new ArrayList<>();
                JSONArray array=js.getJSONArray("ContactList");
                for(int i=0; i<array.length(); i++) {
                    JSONObject jsonObject=array.getJSONObject(i);

                    MyLog.e("%s --- "+jsonObject.getString("NickName"), i+"");

                    WechatGroup gr=new WechatGroup();
                    gr.UserName=jsonObject.getString("UserName");
                    gr.NickName=jsonObject.getString("NickName");
                    if(gr.UserName.startsWith("@@")) {//群
                        groupBeans.add(gr);
                    }
                }
            }
            return groupBeans;
        }catch (Exception e){

        }
        return null;
    }



    public static  List<WechatGroup> getGroupBeanList(String data) {
        try{
        //    MyLog.e("getGroupBeanList ---- "+data);
            JSONObject js=new JSONObject(data);
            JSONObject baseResponse=js.getJSONObject("BaseResponse");
            int Ret=baseResponse.getInt("Ret");
            if(Ret!=0) {
                MyLog.e(baseResponse.toString());
                if(Ret==1205){
                    Thread.sleep(2000);
                }
                return null;
            }
            int MemberCount=js.getInt("MemberCount");
            List<WechatGroup> groupBeans=null;
            if(MemberCount>0) {
                groupBeans=new ArrayList<>();
                JSONArray array=js.getJSONArray("MemberList");
                for(int i=0; i<array.length(); i++) {
                    JSONObject jsonObject=array.getJSONObject(i);

                  //  MyLog.e("%s --- "+jsonObject.getString("NickName"), i+"");

                    WechatGroup gr=new WechatGroup();
                    gr.UserName=jsonObject.getString("UserName");
                    gr.NickName=jsonObject.getString("NickName");
                    if(gr.UserName.startsWith("@@")) {//群
                        groupBeans.add(gr);
                    }
                }
            }
            return groupBeans;
        }catch (Exception e){

        }
        return null;
    }
}
