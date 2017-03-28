package com.my.backups.demo;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by hu on 17-2-6.
 */

public class BackUpData {
    public final static String PACKAGE_NAME="pkgName";
    public final static String VERSION_CODE="verCode";
    public final static String BACKUP_CONTAIN="backupContain";
    public final static String BACKUP_REMOVE="backupRemove";


    private String  pkgName;
    private String  verCode;
    private JSONArray  backupContain;
    private JSONArray  backupRemove;



    public BackUpData(String pkgName, String verCode) {
        this.pkgName = pkgName;
        this.verCode = verCode;
    }

    @Override
    public String toString() {
        JSONObject jsonObject=new JSONObject();
        try{
            jsonObject.put(PACKAGE_NAME, pkgName);
            jsonObject.put(VERSION_CODE, verCode);
            jsonObject.put(BACKUP_CONTAIN, backupContain.toString());
            jsonObject.put(BACKUP_REMOVE, backupRemove.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject.toString();
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

    public JSONArray getBackupContain() {
        return backupContain;
    }

    public void setBackupContain(JSONArray backupContain) {
        this.backupContain = backupContain;
    }

    public JSONArray getBackupRemove() {
        return backupRemove;
    }

    public void setBackupRemove(JSONArray backupRemove) {
        this.backupRemove = backupRemove;
    }



}
