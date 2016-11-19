package com.my.hu.baiduyun;

import android.content.Context;

import com.my.hu.Util.SqliteDataHelper;
import com.my.hu.demain.UrlInfo;
import com.my.hu.myinterface.JsonData;
import com.my.hu.myinterface.NewSqliteDBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hu on 9/18/16.
 */
public class CommonMethod {
    public  static final String BAIDU_PLATFORM="baidu";

    public static List<JsonData> getAllJsonDataByPlatName(Context context,String platName){
        NewSqliteDBHelper dbHelper=new NewSqliteDBHelper(context);
        List<JsonData> jsonDataList=dbHelper.getOldUrl_TB_DataList();
        dbHelper.closeDataBase();
        ArrayList<JsonData> jsonArray=new ArrayList<JsonData>();
        for(int i=0; i<jsonDataList.size(); i++){
            JsonData json=jsonDataList.get(i);
            if(json.platform_name.equals(platName)){
                jsonArray.add(json);
            }
        }
        return jsonArray;
    }

    public static List<UrlInfo> getOldUrlBaiduData(Context context){
        List<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
        SqliteDataHelper dbHelper=new SqliteDataHelper(context);
        List<UrlInfo> jsonDataList=dbHelper.getBaiduUrlList();
        return jsonDataList;
    }

    public static List<JsonData> urlInfoToJsonData(List<UrlInfo> urlInfos){
        List<JsonData> jsonDatas=new ArrayList<JsonData>();
        for(int i=0; i<urlInfos.size(); i++){
            JsonData jsonData=new JsonData();
            jsonData.old_url=urlInfos.get(i).getDownLoad_url();
            jsonData.id=urlInfos.get(i).getId();
            jsonData.platform_name=urlInfos.get(i).getPlatform_name();
            jsonData.md5=urlInfos.get(i).getMd5();
            jsonData.update_time=urlInfos.get(i).getRequest_time();
            jsonDatas.add(jsonData);
        }
        return jsonDatas;
    }

}
