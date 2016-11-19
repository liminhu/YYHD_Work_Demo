package com.my.hu.myinterface;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by hu on 9/9/16.
 */
public class SaveDataToDB {
    private NewSqliteDBHelper db;

    public SaveDataToDB(Context context){
        db=new NewSqliteDBHelper(context);
    }

    public void saveDataJsonBean(JsonBean jb){
        HashMap<String,String> map=jb.getData();
        for(String key: map.keySet()){
         //   Log.e("hook_key", key);
            if(!db.isHaveJsonDataByMD5(key)){
                JsonData jsonData=new JsonData();
                jsonData.md5=key;
                jsonData.old_url=map.get(key);
             //   Log.e("hook_url",map.get(key));
                jsonData.platform_name=jb.getPlatform_name();
                db.saveJsonDataInfo(jsonData);
            }
        }
    }



    public void closeSqliteDBHelp(){
        db.closeDataBase();
    }

}
