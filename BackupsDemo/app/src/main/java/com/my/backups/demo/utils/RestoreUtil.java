package com.my.backups.demo.utils;

import com.my.backups.demo.test.BackUpDataOpt2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hu on 17-2-22.
 */

public class RestoreUtil {

   /*  {"target":{"verCode":"210413","pkgName":"net.hexage.reaper"},"files":{"data":{"exclude":["app_gameassist\/.*\\.?","lib\/.*\\.?","cache\/.*\\.?"]},"sdcardRoot":{"include":["net.hexage.reaper.backup\/.*\\.?"]}}}
	app_gameassist/.*\.?		lib/.*\.?		cache/.*\.?
*/




    private String testParsonJson(String data){
        String json=data;
        //"{\"target\":{\"verCode\":\"12785\",\"pkgName\":\"com.and.games505.TerrariaPaid\"},\"files\":{\"data\":{\"exclude\":[\"app_gameassist\\\\\\/.\\\\*.?\",\"lib\\\\\\/.\\\\*.?\",\"cache\\\\\\/.\\\\*.?\",\"files\\\\\\/.\\*.png\",\"shared_prefs\\/com.codeglue.terraria.Terraria.xml\"]}}}";
        List<String> list= parseJsonGetDataExcludeList(json);
        if(list==null){
            return "";
        }
        StringBuilder str=new StringBuilder();
        for(int i=0; i<list.size(); i++){
            str.append("\t"+list.get(i)+"\t");
        }
        return str.toString();
    }


    public static List<String> parseJsonGetIncludeSdcardRootList(String jsonStr){
        try {
            JSONObject json=new JSONObject(jsonStr);
            String files=json.getString(BackUpDataOpt2.FILES);
            JSONObject fileJson=new JSONObject(files);
            JSONObject data=fileJson.getJSONObject(BackUpDataOpt2.SDCARDROOT);
            JSONArray dataExcludeArray=data.getJSONArray(BackUpDataOpt2.EXCLUDE);
            List<String> exclude=parseJsonArrayGetList(dataExcludeArray);
            if(exclude.size()>0){
                return  exclude;
            }
        }catch (Exception e){
        }
        return null;
    }



    public static List<String> parseJsonGetIncludeSdcardDataList(String jsonStr){
        try {
            JSONObject json=new JSONObject(jsonStr);
            String files=json.getString(BackUpDataOpt2.FILES);
            JSONObject fileJson=new JSONObject(files);
            JSONObject data=fileJson.getJSONObject(BackUpDataOpt2.SDCARDDATA);
            JSONArray dataExcludeArray=data.getJSONArray(BackUpDataOpt2.EXCLUDE);
            List<String> exclude=parseJsonArrayGetList(dataExcludeArray);
            if(exclude.size()>0){
                return  exclude;
            }
        }catch (Exception e){
        }
        return null;
    }



    public static List<String> parseJsonGetDataExcludeList(String jsonStr){
        try {
            JSONObject json=new JSONObject(jsonStr);
            String files=json.getString(BackUpDataOpt2.FILES);
            JSONObject fileJson=new JSONObject(files);
            JSONObject data=fileJson.getJSONObject(BackUpDataOpt2.DATA);
            JSONArray dataExcludeArray=data.getJSONArray(BackUpDataOpt2.EXCLUDE);
            List<String> exclude=parseJsonArrayGetList(dataExcludeArray);
            if(exclude.size()>0){
                return  exclude;
            }
        }catch (Exception e){
        }
        return null;
    }


    private static List<String> parseJsonArrayGetList(JSONArray jsonArray){
        List<String> list=new ArrayList<String>();
        try{
            for(int i=0; i<jsonArray.length(); i++){
                list.add(i, jsonArray.getString(i));
            }
        }catch (Exception e){

        }
        return list;
    }

}
