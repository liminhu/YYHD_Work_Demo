package com.my.utils;

import android.content.Context;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by hulimin on 2017/3/14.
 */

public class FilesUtils {
    public String loadReadFile(Context context, String fileName){
        FileInputStream in=null;
        BufferedReader reader=null;
        StringBuilder sb=new StringBuilder();
        try{
            in=context.openFileInput(fileName);
            //Log.d("hook_read",in.g)
            reader=new BufferedReader(new InputStreamReader(in));
            String line="";
            while ((line=reader.readLine())!=null){
                sb.append(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(reader!=null){
                try{
                    reader.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public void saveDataToFile(Context context, String fileName, String data){
        FileOutputStream out=null;
        BufferedWriter writer=null;
        try{
            out=context.openFileOutput(fileName, Context.MODE_PRIVATE);
            writer=new BufferedWriter(new OutputStreamWriter(out));
            writer.write(data);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(writer!=null){
                    writer.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }



    public static String readFileByBytes(String fileName){
        File file=new File(fileName);
        if(!file.exists()){
            return null;
        }
        InputStream in=null;
        StringBuilder sb=sb=new StringBuilder();
        try {
            //一次读多个字节
            byte[] tempBytes=new byte[1024];
            in=new FileInputStream(file);
            int count=0;
            while ((count=in.read(tempBytes)) != -1) {
                sb.append(new String(tempBytes,0,count));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }







    public static void writeDataToFile(String fileName, byte[] data) {
        File file = new File(fileName);
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            FileOutputStream fstream = new FileOutputStream(file);
            BufferedOutputStream stream = new BufferedOutputStream(fstream);
            stream.write(data);// 调试到这里文件已经生成
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }





    public static void saveDataToFile(Context context, String data){
        FileOutputStream out=null;
        BufferedWriter writer=null;
        try{
            out=context.openFileOutput("test_data", Context.MODE_PRIVATE);
            writer=new BufferedWriter(new OutputStreamWriter(out));
            writer.write(data);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(writer!=null){
                    writer.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
