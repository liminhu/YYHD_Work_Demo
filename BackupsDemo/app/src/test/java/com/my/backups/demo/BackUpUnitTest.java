package com.my.backups.demo;

import android.util.Log;

import com.my.backups.demo.test.BackUpDataOpt2;
import com.my.backups.demo.utils.BackupUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by hu on 17-2-6.
 */

public class BackUpUnitTest {

    public void addition_isCorrect() throws Exception {
  /*      String test=initbackupJsonStr();
        System.out.print(test);
*/
    //    testParsonJson();
        assertEquals(4, 2 + 2);
    }








     private String initbackupJsonStr(){
         BackUpData backupData=new BackUpData("com.and.games505.TerrariaPaid", "12785");
         String[] remove=new String[10];
         String removeStr="shared_prefs/com.codeglue.terraria.Terraria.xml";
         remove[0]=removeStr;
         backupData.setBackupRemove(initBackupRemove(remove));

         String[] contain=new String[10];
         contain[0]="";
         backupData.setBackupContain(initBackupContain(contain));
         return backupData.toString();
     }




    private JSONArray initBackupRemove(String[] remove){
        JSONArray json=new JSONArray();
        for(int i=0; i<remove.length; i++){
            try {
                json.put(i, remove[i]);
            }catch (Exception e){

            }
        }
        return json;
    }



    private JSONArray initBackupContain(String[] contain){
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
