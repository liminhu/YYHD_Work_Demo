package com.my.backups.demo.test;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hu on 17-2-6.
 */

public class BackUpDataOpt2 {
    public final static String TARGET="target";
    public final static String FILES="files";

    public final static String PACKAGE_NAME="pkgName";
    public final static String VERSION_CODE="verCode";

    public final static String INCLUDE="include";
    public final static String EXCLUDE="exclude";


    public final static String DATA="data";
    public final static String SDCARDROOT="sdcardRoot";
    public final static String SDCARDDATA="sdcardData";

    public BackUpDataOpt2(String pkgName, String verCode) {
        this.pkgName = pkgName;
        this.verCode = verCode;
    }

    private String  pkgName;
    private String  verCode;

    public JSONObject toTargetJSONObject(){
        JSONObject json=new JSONObject();
        try{
            json.put(PACKAGE_NAME, pkgName);
            json.put(VERSION_CODE, verCode);
        }catch (Exception e){

        }
        return json;
    }



    public JSONObject toFilesJSONObjectIncludeSdcardData(List<String> dataExclude, List<String> sdcardDataInclude){
        JSONObject json=new JSONObject();
        JSONArray dataJsonArray=new JSONArray(dataExclude);
        JSONArray sdcardDataJsonArray=new JSONArray(sdcardDataInclude);
        try{
            JSONObject jsonData=new JSONObject();
            jsonData.put(EXCLUDE, dataJsonArray);
            json.put(DATA, jsonData);

            JSONObject jsonSdcardData=new JSONObject();
            jsonSdcardData.put(INCLUDE,sdcardDataJsonArray);
            json.put(SDCARDDATA,jsonSdcardData);
        }catch (Exception e){

        }
        return json;
    }





    public JSONObject toFilesJSONObjectIncludeSdcardRoot(List<String> dataExclude, List<String> sdcardRootInclude){
        JSONObject json=new JSONObject();
        BackupFiles backupFiles=new BackupFiles();
        backupFiles.data.exclude=dataExclude;
        backupFiles.sdcardRoot.include=sdcardRootInclude;
        try{
            JSONObject jsonData=new JSONObject();
            jsonData.put(EXCLUDE, backupFiles.getDataExcludeJsonArray());
            json.put(DATA, jsonData);

            JSONObject jsonSDCARDROOT=new JSONObject();
            jsonSDCARDROOT.put(INCLUDE,backupFiles.getSdcardRootIncludeJsonArray());
            json.put(SDCARDROOT,jsonSDCARDROOT);
        }catch (Exception e){

        }
        return json;
    }


    public JSONObject toDefaultFilesJSONObject(){
        BackupFiles backupFiles=new BackupFiles();
        List<String> dataExclude=BackUpOptTest2.initDataExclude();
        backupFiles.data.exclude=dataExclude;
        JSONObject json=new JSONObject();
        JSONArray jsonArray=new JSONArray(dataExclude);
        try{
            JSONObject jsonData=new JSONObject();
            jsonData.put(EXCLUDE, jsonArray);
            json.put(DATA, jsonData);
        }catch (Exception e){

        }
        return json;
    }

}
