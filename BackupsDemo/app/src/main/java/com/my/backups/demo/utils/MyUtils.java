package com.my.backups.demo.utils;

import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by hu on 17-2-6.
 */

public class MyUtils {
    public JSONObject hashMapToJson(HashMap<String,String> contain){
        JSONObject json=new JSONObject();
        for(String key: contain.keySet()){
            String value=contain.get(key);
            try {
                json.put(key,value);
            }catch (Exception e){
                Log.e("hook_json", e.getMessage());
            }

        }
        return json;
    }



}
