package com.my.backups.demo.utils;

import android.os.Environment;

import com.my.backups.demo.test.BackUpDataOpt2;
import com.my.backups.demo.test.BackUpOptTest2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipFile;

/**
 * Created by hu on 17-2-9.
 */

public class BackupUtils {
    private static final String re = "/.*\\.?";

    private static final String SDCRAD_DATA_PATH = Environment.getExternalStorageDirectory() + "/Android/data/%s/sandbox/0/%s/";
    private static final String SDCRAD_ROOT_PATH = Environment.getExternalStorageDirectory() + "/sandbox/0/Android/data/";

/*


{
    "target":{
        "verCode":"12785",
        "pkgName":"com.and.games505.TerrariaPaid"
    },
    "files":{
        "data":{
            "exclude":[
                "app_gameassist\/.\*.?",
                "lib\/.\*.?",
                "cache\/.\*.?",
                "files\/.\*.png",
                "shared_prefs/com.codeglue.terraria.Terraria.xml"
            ]
        }
    }
}




*/




    public static List<String> parseJsonGetDataExcludeList(String jsonStr){
        try {
            JSONObject json=new JSONObject(jsonStr);
            String files=json.getString(BackUpDataOpt2.FILES);
            JSONObject fileJson=new JSONObject(files);
            JSONObject data=fileJson.getJSONObject(BackUpDataOpt2.DATA);
            JSONArray  dataExcludeArray=data.getJSONArray(BackUpDataOpt2.EXCLUDE);
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



    /*public static boolean backupFolder(String srcFilePath,
                                       String zipFilePath,
                                       String pkgname,
                                       String password,
                                       List<String> dataIncludeList,
                                       List<String> dataExcludeList,
                                       List<String> sdcardDataIncludeList,
                                       List<String> sdcardDataExcludeList,
                                       List<String> sdcardRootIncludeList,
                                       List<String> sdcardRootExcludeList) {
        try {
            ZipFile zipFile = new ZipFile(zipFilePath);
            zipFile.setFileNameCharset("GBK");
            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); // set compression method to deflate compression
            zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            zipParameters.setPassword(password);
            File file = new File(srcFilePath);
            dataExcludeList.add("files" + re);
            zipFiles(file.getParent() + File.separator + file.getName() + File.separator, null, "", zipFile, zipParameters, pkgname, dataIncludeList, dataExcludeList);
            zipFiles(String.format(SDCRAD_DATA_PATH, IPlayApplication.getApp().getPackageName(), pkgname), null, "", zipFile, zipParameters, pkgname, sdcardDataIncludeList, sdcardDataExcludeList);
            zipFiles(String.format(SDCRAD_ROOT_PATH, IPlayApplication.getApp().getPackageName(), pkgname), null, "", zipFile, zipParameters, pkgname, sdcardDataIncludeList, sdcardDataExcludeList);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //Data存档
    private static void zipFiles(String folderPath, String folderName, String filePath, ZipFile zipFile, ZipParameters zipParameters, String pkgname,
                                 List<String> dataIncludeList,
                                 List<String> dataExcludeList)
            throws Exception {

        if (dataIncludeList.isEmpty() && !dataExcludeList.isEmpty()) {

            File file = new File(folderPath + (TextUtils.isEmpty(filePath) ? "" : filePath));
            if (!file.exists()) {
                return;
            }
            if (matchFile(dataExcludeList, filePath))
                return;

            if (file.isFile()) {
                zipParameters.setRootFolderInZip(folderName);
                zipFile.addFile(file, zipParameters);
            } else {
                String[] filelist = file.list();
                if (null != filelist) {
                    for (int i = 0; i < filelist.length; i++) {
                        zipFiles(folderPath, filePath, TextUtils.isEmpty(filePath) ? filelist[i] : filePath + File.separator + filelist[i], zipFile, zipParameters, pkgname, dataIncludeList, dataExcludeList);
                    }
                }
            }
        } else if (!dataIncludeList.isEmpty() && dataExcludeList.isEmpty()) {

            File file = new File(folderPath + (TextUtils.isEmpty(filePath) ? "" : filePath));
            if (!file.exists()) {
                return;
            }
            if (!matchFile(dataExcludeList, filePath))
                return;

            if (file.isFile()) {
                zipParameters.setRootFolderInZip(folderName);
                zipFile.addFile(file, zipParameters);
            } else {
                String[] filelist = file.list();
                if (null != filelist) {
                    for (int i = 0; i < filelist.length; i++) {
                        zipFiles(folderPath, filePath, TextUtils.isEmpty(filePath) ? filelist[i] : filePath + File.separator + filelist[i], zipFile, zipParameters, pkgname, dataIncludeList, dataExcludeList);
                    }
                }
            }
        }
    }


    public static boolean restoreFolder(String zipFilePath, String folderPath, String password) {
        File desDir = new File(folderPath);
        if (!desDir.exists()) {
            desDir.mkdirs();
        }
        try {
            ZipFile zipFile = new ZipFile(zipFilePath);
            if (!zipFile.isValidZipFile())
                return false;
            if (zipFile.isEncrypted()) {
                zipFile.setPassword(password);
            }
            zipFile.extractAll(folderPath);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }





    *//**
     * 正则匹配文件名
     * @param filePathList
     * @return
     *//*
    public static boolean matchFile(List<String> filePathList, String fileName) {
        if (null == filePathList || filePathList.isEmpty())
            return false;
        for (String filePathItem : filePathList) {
            if (fileName.matches(filePathItem))
                return true;
        }
        return false;
    }*/
}
