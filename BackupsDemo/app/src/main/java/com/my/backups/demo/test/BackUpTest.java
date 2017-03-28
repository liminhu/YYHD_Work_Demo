package com.my.backups.demo.test;

import com.my.backups.demo.BackUpData;

import org.json.JSONArray;

/**
 * Created by hu on 17-2-6.
 */

public class BackUpTest {

     public static String initbackupJsonStr(){
         BackUpData backupData=new BackUpData("com.and.games505.TerrariaPaid", "12785");
         String[] remove=new String[2];
         String removeStr="shared_prefs/com.codeglue.terraria.Terraria.xml";
         String removeStr1="/sdcard/Android/data/com.example.gg/sandbox/0/com.good.world3/files/save122.sav";
         remove[0]=removeStr;
         remove[1]=removeStr1;

         backupData.setBackupRemove(initBackupRemove(remove));

         String[] contain=new String[1];
         backupData.setBackupContain(initBackupContain(contain));
         return backupData.toString();
     }


    private static JSONArray initBackupRemove(String[] remove){
        JSONArray json=new JSONArray();
        for(int i=0; i<remove.length; i++){
            try {
                json.put(i, remove[i]);
            }catch (Exception e){

            }
        }
        return json;
    }



    private static JSONArray initBackupContain(String[] contain){
        JSONArray json=new JSONArray();
        for(int i=0; i<contain.length; i++){
            try {
                json.put(i, contain[i]);
            }catch (Exception e){

            }
        }
        return json;
    }





/*
    private JSONObject initBackupContain(HashMap<String,String> contain){
        JSONObject json=new JSONObject();
        for(String key: contain.keySet()){
            String value=contain.get(key);
            try {
                json.put(key,value);
            }catch (Exception e){

            }

        }
        return json;
    }*/

}
