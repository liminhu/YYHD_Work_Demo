package com.gameassist.plugin.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by hulimin on 2017/9/19.
 */

public class FileUtils {
    //  File f=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),   "/"+dirName);  //Camera"/gg_"+time+".jpg");      //
    public static void copyFile(InputStream inStream, String fileName) {
        deleteAllGitFile();
        File f=new File(fileName);
        String newPath=f.getAbsolutePath();
        try {
            MyLog.e("newPath : "+newPath);
            int bytesum = 0;
            int byteread = 0;
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[2048];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    //System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
               MyLog.e("file size --- "+bytesum);
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
    }


    public static void  deleteAllGitFile() {
        File f=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        String[] str=f.list();
        for(int i=0; i<str.length; i++){
            if(str[i].contains(".gif")){
                MyLog.e("delete -- "+str[i]);
                File newFile=new File(f, str[i]);
                newFile.delete();
            }
        }
    }


    // TODO: 2017/9/7  ---- 保存view的视图到sdcard  --- 注意路径被重定向

    /**
     * 通过drawingCache获取bitmap
     */
    public static Bitmap copyByDrawingCache(View mOriginImageView) {
        mOriginImageView.setDrawingCacheEnabled(true);
        mOriginImageView.buildDrawingCache(true);
        final Bitmap bp = mOriginImageView.getDrawingCache();
        if(bp != null){
            Bitmap finalBp = Bitmap.createBitmap(bp);
            mOriginImageView.setDrawingCacheEnabled(false);
            return finalBp;
        }
        return null;
    }

    public static  void saveBitmap(Bitmap bm,String name) {
        MyLog.e("view--  saveBitmap:"+bm.toString());
        File f = new File(Environment.getExternalStorageDirectory(), "/aaa/"+name+".png");
        MyLog.e("view--  name:"+f.getAbsolutePath());
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            MyLog.e("view--  saveBitmap: succ"+name);
        } catch (Exception e) {
            MyLog.e("view--  1111: succ"+       e.getMessage());

        }
    }





}
