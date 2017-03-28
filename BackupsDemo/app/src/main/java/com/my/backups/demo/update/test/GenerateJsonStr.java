package com.my.backups.demo.update.test;

import com.my.backups.demo.test.BackUpDataOpt;
import com.my.backups.demo.test.BackUpDataOpt2;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.my.backups.demo.test.BackUpOptTest2.initDataExclude;

/**
 * Created by hu on 17-2-23.
 */

public class GenerateJsonStr {
    private String packageName;
    private String versionCode;


    public GenerateJsonStr(String packageName, String versionCode) {
        this.packageName = packageName;
        this.versionCode = versionCode;
    }

    public String genBackupJsonStr(){
        BackUpDataOpt2 backupData = new BackUpDataOpt2(packageName, versionCode);
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


    public String genBackupJsonStr_example_1(){
        BackUpDataOpt2 backupData = new BackUpDataOpt2(packageName, versionCode);
        List<String> dataExclude=initDataExclude();

        List<String> sdcardDataInclude=new ArrayList<String>();
        sdcardDataInclude.add("files/.*\\.?");   //通配符处理
        sdcardDataInclude.add(".*\\.dat");   //通配符处理
        JSONObject uploadJson = new JSONObject();
        try {
            uploadJson.put(BackUpDataOpt.TARGET, backupData.toTargetJSONObject());
            uploadJson.put(BackUpDataOpt.FILES, backupData.toFilesJSONObjectIncludeSdcardData(dataExclude,sdcardDataInclude));
        } catch (Exception e) {
        }
        return uploadJson.toString();
    }


    public String genBackupJsonStr_example_2(){
        BackUpDataOpt2 backupData = new BackUpDataOpt2(packageName, versionCode);
        List<String> dataExclude=initDataExclude();

        List<String> sdcardDataInclude=new ArrayList<String>();

       // sdcardDataInclude.add("files/.*\\.?");   //通配符处理  --  待定，文件夹下的所有文件

        JSONObject uploadJson = new JSONObject();
        try {
            uploadJson.put(BackUpDataOpt.TARGET, backupData.toTargetJSONObject());
            uploadJson.put(BackUpDataOpt.FILES, backupData.toFilesJSONObjectIncludeSdcardData(dataExclude,sdcardDataInclude));
        } catch (Exception e) {
        }
        return uploadJson.toString();
    }



    public String genBackupJsonStr_example_3(){
        BackUpDataOpt2 backupData = new BackUpDataOpt2(packageName, versionCode);
        List<String> dataExclude=initDataExclude();

        List<String> sdcardDataInclude=new ArrayList<String>();
        sdcardDataInclude.add("files/.*\\.bin");   //通配符处理
        JSONObject uploadJson = new JSONObject();
        try {
            uploadJson.put(BackUpDataOpt.TARGET, backupData.toTargetJSONObject());
            uploadJson.put(BackUpDataOpt.FILES, backupData.toFilesJSONObjectIncludeSdcardData(dataExclude,sdcardDataInclude));
        } catch (Exception e) {
        }
        return uploadJson.toString();
    }




}
