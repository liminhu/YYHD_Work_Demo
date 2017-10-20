package com.gameassist.plugin.wechat.model;

import com.gameassist.plugin.utils.MyLog;
import com.gameassist.plugin.utils.StringUtils;
import com.gameassist.plugin.wechat.exception.WechatException;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by hulimin on 2017/10/19.
 */

public class WechatUser extends WechatGroup {
    public  String Uin;
    public  String SyncKey;


    public void  initWechatUser(String data){
        //MyLog.e(data);
        try{
            JSONObject js=new JSONObject(data);
            JSONObject baseResponse=js.getJSONObject("BaseResponse");
            int Ret=baseResponse.getInt("Ret");
            if(Ret!=0) {
                System.out.println(baseResponse.toString());
                return ;
            }

            JSONObject syncKey_js=js.getJSONObject("SyncKey");
            if(syncKey_js!=null){

                StringBuffer synckey = new StringBuffer();
                JSONArray list = syncKey_js.getJSONArray("List");
                for (int i = 0, len = list.length(); i < len; i++) {
                    JSONObject item = list.getJSONObject(i);
                    synckey.append(item.getString("Key"));
                    synckey.append("_");
                    synckey.append(item.getString("Val"));
                    if(i+1<len){
                        synckey.append("|");
                    }
                }
                SyncKey=synckey.toString();
                MyLog.e("同步key-------"+SyncKey);
            }


            JSONObject User=js.getJSONObject("User");
            MyLog.e("User --- "+User.toString());
            Uin=User.getString("Uin");
            UserName=User.getString("UserName");
            NickName=User.getString("NickName");
        }catch (Exception e){

        }

    }

}
