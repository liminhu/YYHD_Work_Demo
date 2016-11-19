package com.my.hu.webviewjsinjectv2.util;

import android.content.Context;
import android.provider.SyncStateContract;
import android.util.Log;

import com.my.hu.webviewjsinjectv2.domain.ConstantData;

import java.io.File;

/**
 * Created by hu on 9/26/16.
 */
public class FileUtil {

    public static void executeDeleteDBFile(Context context,String fileName){
        StringBuilder sb=new StringBuilder();
        sb.append("/data/data/");
        String packageName=context.getPackageName();
        sb.append(packageName);
        sb.append("/databases/");
        sb.append(fileName);
        dbFileIsExistAndDeleteFile(sb.toString());
    }


    private static void dbFileIsExistAndDeleteFile(String fileName){
        File file=new File(fileName);
        if(file.exists()){
            try {
                file.delete();
                Log.i("hook_db","dbFileIsExistAndhaveDeleteFile_"+fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            Log.i("hook_db","delete "+fileName+" no find!!!");
        }
    }
}
