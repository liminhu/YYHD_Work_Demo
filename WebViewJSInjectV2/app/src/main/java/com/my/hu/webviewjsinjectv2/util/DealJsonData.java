package com.my.hu.webviewjsinjectv2.util;

import android.util.Log;

import com.my.hu.webviewjsinjectv2.database.NewSqliteCreateRawLinkDB;
import com.my.hu.webviewjsinjectv2.database.UpdateSqliteRawLinkDBHelper;
import com.my.hu.webviewjsinjectv2.domain.LinkJsonData;
import com.my.hu.webviewjsinjectv2.domain.RawLinkHashMapData;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by hu on 9/21/16.
 */
public class DealJsonData {
    private String json_msg;

    public DealJsonData(String json_msg) {
        this.json_msg = json_msg;
    }

    public RawLinkHashMapData getRawLinkHashMapDataByPlatformName(String platName){
        RawLinkHashMapData hashMapData=null;
        try {
            JSONObject json = new JSONObject(json_msg);
            String json_msg=json.getString("msg");
            if(json_msg.equals("ok")){
                Log.v("hook_JSON_data","ok");
                JSONObject json1=json.getJSONObject("data");
                String rawStr=json1.getString(platName);
                hashMapData=parseJsonString(rawStr,platName);
                return hashMapData;
            }
        }catch (Exception e){
            Log.e("hook_handleMessage","handleMessage exception");
        }
        return null;
    }



    public int saveDataJsonBean(RawLinkHashMapData jb, UpdateSqliteRawLinkDBHelper db, String tb_name){
        HashMap<String,String> map=jb.getData();
        int num=0;
        for(String key: map.keySet()){
            if(!db.isHaveJsonDataByMD5(key,tb_name)){
                LinkJsonData jsonData=new LinkJsonData();
                jsonData.md5=key;
                jsonData.link_url=map.get(key);
              //  Log.e("hook_url",map.get(key));
                jsonData.platform_name=jb.getPlatform_name();
                jsonData.update_time= StringUtil.getDateTime();
                jsonData.state=0;
                jsonData.gen_time=0;
                num=num+db.saveUpdateJsonDataInfo(jsonData,tb_name);
            }
        }
        return num;
    }







    private RawLinkHashMapData parseJsonString(String dataStr, String platform_name){
        try {
            RawLinkHashMapData jb=new RawLinkHashMapData();
            jb.setPlatform_name(platform_name);
            HashMap<String, String> map=new HashMap<String,String>();
            String data_temp=dataStr.substring(dataStr.indexOf("{")+1,dataStr.lastIndexOf("}"));
            String temp1=data_temp.replace('\"',' ');
            String temp2=temp1.replace('/','\\');
            String temp3=temp2.replace("\\\\","\\");
            String temp=temp3.replace('\\','/');
            String[] dataArray=temp.split(",");
            for(int i=0; i<dataArray.length; i++){
                String data=dataArray[i];
                String[] str=data.split(" : ");
                map.put(str[0].trim(), str[1].trim());
            }
            jb.setData(map);
            return jb;
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("hook_parseJson", "exception");
        }
        return null;
    }
}
