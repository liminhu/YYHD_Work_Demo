package com.my.backups.demo.test;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hu on 17-2-6.
 */
public class BackUpOptTest2 {
    public static String initBackupDefaultJsonStr() {
        BackUpDataOpt2 backupData = new BackUpDataOpt2("*", "*");
        JSONObject uploadJson = new JSONObject();
        try {
            uploadJson.put(BackUpDataOpt.TARGET, backupData.toTargetJSONObject());
            uploadJson.put(BackUpDataOpt.FILES, backupData.toDefaultFilesJSONObject());
        } catch (Exception e) {

        }
        return uploadJson.toString();
    }


    public static String initbackupTerrariaPaidJsonStr() {
        BackUpDataOpt2 backupData = new BackUpDataOpt2("com.and.games505.TerrariaPaid", "12785");
        JSONObject uploadJson = new JSONObject();
        try {
            uploadJson.put(BackUpDataOpt.TARGET, backupData.toTargetJSONObject());
            uploadJson.put(BackUpDataOpt.FILES, toTerrariaPaidFilesJSONObject());
        } catch (Exception e) {

        }
        return uploadJson.toString();
    }


    public static String initbackupJsonStr_World3(){
        BackUpDataOpt2 backupData = new BackUpDataOpt2("com.good.world3", "19");
        List<String> dataExclude=initDataExclude();

        List<String> sdcardDataInclude=new ArrayList<String>();
        sdcardDataInclude.add("files/.*\\.sav");   //通配符处理

        JSONObject uploadJson = new JSONObject();
        try {
            uploadJson.put(BackUpDataOpt.TARGET, backupData.toTargetJSONObject());
            uploadJson.put(BackUpDataOpt.FILES, backupData.toFilesJSONObjectIncludeSdcardData(dataExclude,sdcardDataInclude));
        } catch (Exception e) {

        }
        return uploadJson.toString();
    }


    public static String initbackupJsonStr_HexageReaper(){
        BackUpDataOpt2 backupData = new BackUpDataOpt2("net.hexage.reaper","210413");
        List<String> dataExclude=initDataExclude();
        List<String> sdcardRootInclude=new ArrayList<String>();
        //Android/data/net.hexage.reaper.backup
        sdcardRootInclude.add("net.hexage.reaper.backup/.*\\.?");
        JSONObject uploadJson = new JSONObject();
        try {
            uploadJson.put(BackUpDataOpt.TARGET, backupData.toTargetJSONObject());
            uploadJson.put(BackUpDataOpt.FILES, backupData.toFilesJSONObjectIncludeSdcardRoot(dataExclude,sdcardRootInclude));
        } catch (Exception e) {

        }
        return uploadJson.toString();
    }


    public static JSONObject toTerrariaPaidFilesJSONObject(){
        BackupFiles backupFiles=new BackupFiles();
        List<String> dataExclude=initDataExclude();
        dataExclude.add("files/.*\\.png");
        dataExclude.add("shared_prefs/com.codeglue.terraria.Terraria.xml");
        backupFiles.data.exclude=dataExclude;
        JSONObject json=new JSONObject();
        try{
            JSONObject jsonData=new JSONObject();
            jsonData.put(BackUpDataOpt2.EXCLUDE, backupFiles.getDataExcludeJsonArray());
            json.put(BackUpDataOpt2.DATA, jsonData);
        }catch (Exception e){

        }
        return json;
    }


    public static String initbackupJsonStr_Common(String packagName, String versionCode){
        BackUpDataOpt2 backupData=new BackUpDataOpt2(packagName, versionCode);
        List<String> dataExclude=initDataExclude();

        List<String> sdcardDataInclude=new ArrayList<String>();
        sdcardDataInclude.add("files/.*\\.?");   //通配符处理

        JSONObject uploadJson = new JSONObject();
        try {
            uploadJson.put(BackUpDataOpt.TARGET, backupData.toTargetJSONObject());
            uploadJson.put(BackUpDataOpt.FILES, backupData.toFilesJSONObjectIncludeSdcardData(dataExclude,sdcardDataInclude));
        } catch (Exception e) {

        }
        return uploadJson.toString();
    }


    public static List<String> initDataExclude(){
        List<String> dataExclude=new ArrayList<String>();
        dataExclude.add("app_gameassist/.*\\.?");
        dataExclude.add("lib/.*\\.?");
        dataExclude.add("cache/.*\\.?");
        return dataExclude;
    }
}
