package com.my.backups.demo.test;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by hu on 17-2-6.
 */

public class BackUpDataOpt {
    public final static String TARGET="target";
    public final static String FILES="files";

    public final static String PACKAGE_NAME="pkgName";
    public final static String VERSION_CODE="verCode";

    public final static String INCLUDE="include";
    public final static String EXCLUDE="exclude";


    public final static String DATA="data";
    public final static String SDCARDROOT="sdcardRoot";
    public final static String SDCARDDATA="sdcardData";

    public BackUpDataOpt(String pkgName, String verCode) {
        this.pkgName = pkgName;
        this.verCode = verCode;
    }

    private JSONArray  sdcardRootInclude;
    private JSONArray  dataExclud;
    private JSONArray  sdcardDataInclude;


    private String  pkgName;
    private String  verCode;


    public JSONArray getSdcardRootInclude() {
        return sdcardRootInclude;
    }

    public void setSdcardRootInclude(JSONArray sdcardRootInclude) {
        this.sdcardRootInclude = sdcardRootInclude;
    }

    public JSONArray getDataExclud() {
        return dataExclud;
    }

    public void setDataExclud(JSONArray dataExclud) {
        this.dataExclud = dataExclud;
    }

    public JSONArray getSdcardDataInclude() {
        return sdcardDataInclude;
    }

    public void setSdcardDataInclude(JSONArray sdcardDataInclude) {
        this.sdcardDataInclude = sdcardDataInclude;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getVerCode() {
        return verCode;
    }

    public void setVerCode(String verCode) {
        this.verCode = verCode;
    }


    public JSONObject toTarget(){
        JSONObject json=new JSONObject();
        try{
            json.put(PACKAGE_NAME, pkgName);
            json.put(VERSION_CODE, verCode);
        }catch (Exception e){

        }
        return json;
    }




    public JSONObject toDataExclud(){
        JSONObject json=new JSONObject();
        try{
            json.put(EXCLUDE, dataExclud); //无需toString
        }catch (Exception e){

        }
        //Log.i("hook_exclude", json.toString());

        JSONObject json1=new JSONObject();
        try{
            json1.put(DATA, json);  //无需toString
        }catch (Exception e){

        }
        return json1;
    }





    public JSONObject toSdcardDataInclude(){
        JSONObject json=new JSONObject();
        try{
            json.put(INCLUDE, sdcardDataInclude); //无需toString
        }catch (Exception e){

        }
        //Log.i("hook_include_1", json.toString());

        JSONObject json1=new JSONObject();
        try{
            json1.put(SDCARDDATA, json);  //无需toString
        }catch (Exception e){

        }
        return json1;
    }




    public JSONObject toSdcardRootInclude(){
        JSONObject json=new JSONObject();
        try{
            json.put(INCLUDE, sdcardRootInclude); //无需toString
        }catch (Exception e){

        }
        //Log.i("hook_include_1", json.toString());

        JSONObject json1=new JSONObject();
        try{
            json1.put(SDCARDROOT, json);  //无需toString
        }catch (Exception e){

        }
        return json1;
    }

/*
    public String toData(){
        JSONObject json=new JSONObject();
        try{
            json.put(DATA, dataExclud.toString());
        }catch (Exception e){

        }
        return json.toString();
    }*/

/*
    public JSONArray toFiles(){
        JSONArray json=new JSONArray();
        try{
            String data=toData();
            json.put(FILES, data);
        }catch (Exception e){

        }
        return json.toString();
    }*/

}
