package com.my.backups.demo.test;

import com.my.backups.demo.BackUpData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hu on 17-2-6.
 */

public class BackUpOptTest1 {
    public static String initBackupDefaultJsonStr(){
        BackUpDataOpt backupData=new BackUpDataOpt("*", "*");
        JSONArray dataExclud=new JSONArray();
        try {
            dataExclud.put(0,"app_gameassist");
            dataExclud.put(1,"lib");
            dataExclud.put(2,"cache");
          //  dataExclud.put(3,"shared_prefs/com.codeglue.terraria.Terraria.xml");
        }catch (Exception e) {
        }
        backupData.setDataExclud(dataExclud);

        JSONArray files=new JSONArray();
        JSONObject uploadJson=new JSONObject();
        try{
            files.put(0, backupData.toDataExclud());
            uploadJson.put(BackUpDataOpt.TARGET, backupData.toTarget());
            uploadJson.put(BackUpDataOpt.FILES, files);
        }catch (Exception e){

        }
        return uploadJson.toString();
    }





     public static String initbackupJsonStr(){
         BackUpDataOpt backupData=new BackUpDataOpt("com.and.games505.TerrariaPaid", "12785");
         JSONArray dataExclud=new JSONArray();
         try {
             dataExclud.put(0,"app_gameassist");
             dataExclud.put(1,"lib");
             dataExclud.put(2,"cache");
             dataExclud.put(3,"shared_prefs/com.codeglue.terraria.Terraria.xml");
         }catch (Exception e) {
         }
         backupData.setDataExclud(dataExclud);

         JSONArray files=new JSONArray();
         JSONObject uploadJson=new JSONObject();
         try{
             files.put(0, backupData.toDataExclud());
             uploadJson.put(BackUpDataOpt.TARGET, backupData.toTarget());
             uploadJson.put(BackUpDataOpt.FILES, files);
         }catch (Exception e){

         }
         return uploadJson.toString();
     }




    public static String initbackupJsonStr_World3(){
        BackUpDataOpt backupData=new BackUpDataOpt("com.good.world3", "19");
        JSONArray dataExclud=new JSONArray();
        try {
            dataExclud.put(0,"app_gameassist");
            dataExclud.put(1,"lib");
            dataExclud.put(2,"cache");
        }catch (Exception e) {
        }
        backupData.setDataExclud(dataExclud);
        JSONArray sdcardDataInclude=new JSONArray();
        try {
            sdcardDataInclude.put(0,"files/*.sav");
        }catch (Exception e) {
        }
        backupData.setSdcardDataInclude(sdcardDataInclude);


        JSONArray files=new JSONArray();
        JSONObject uploadJson=new JSONObject();

        try{
            files.put(0, backupData.toDataExclud());
            files.put(1, backupData.toSdcardDataInclude());
            uploadJson.put(BackUpDataOpt.TARGET, backupData.toTarget());
            uploadJson.put(BackUpDataOpt.FILES, files);
        }catch (Exception e){
        }
        return uploadJson.toString();
    }


    public static String initbackupJsonStr_Common(String packagName, String versionCode){
        BackUpDataOpt backupData=new BackUpDataOpt(packagName, versionCode);
        JSONArray dataExclud=new JSONArray();
        try {
            dataExclud.put(0,"app_gameassist");
            dataExclud.put(1,"lib");
            dataExclud.put(2,"cache");
        }catch (Exception e) {
        }
        backupData.setDataExclud(dataExclud);
        JSONArray sdcardDataInclude=new JSONArray();
        try {
            sdcardDataInclude.put(0,"files/");
        }catch (Exception e) {
        }
        backupData.setSdcardDataInclude(sdcardDataInclude);


        JSONArray files=new JSONArray();
        JSONObject uploadJson=new JSONObject();

        try{
            files.put(0, backupData.toDataExclud());
            files.put(1, backupData.toSdcardDataInclude());
            uploadJson.put(BackUpDataOpt.TARGET, backupData.toTarget());
            uploadJson.put(BackUpDataOpt.FILES, files);
        }catch (Exception e){
        }
        return uploadJson.toString();
    }


    public static String initbackupJsonStr_HexageReaper(String packagName, String versionCode){
        BackUpDataOpt backupData=new BackUpDataOpt(packagName, versionCode);
        JSONArray dataExclud=new JSONArray();
        try {
            dataExclud.put(0,"app_gameassist");
            dataExclud.put(1,"lib");
            dataExclud.put(2,"cache");
        }catch (Exception e) {
        }
        backupData.setDataExclud(dataExclud);
        JSONArray sdcardRootInclude=new JSONArray();
        try {
            sdcardRootInclude.put(0,"Android/data/net.hexage.reaper.backup");
        }catch (Exception e) {
        }
        backupData.setSdcardRootInclude(sdcardRootInclude);


        JSONArray files=new JSONArray();
        JSONObject uploadJson=new JSONObject();

        try{
            files.put(0, backupData.toDataExclud());
            files.put(1,backupData.toSdcardRootInclude());
            uploadJson.put(BackUpDataOpt.TARGET, backupData.toTarget());
            uploadJson.put(BackUpDataOpt.FILES, files);
        }catch (Exception e){
        }
        return uploadJson.toString();
    }


}
