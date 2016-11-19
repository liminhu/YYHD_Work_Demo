package com.my.hu.Util;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

/**
 * Created by hu on 9/12/16.
 */
public class HttpUtils {
    private  static final int TIMEOUT_IN_MILLIONS = 5000;

    public static String submitPostData(String urlPath, Map<String,String> params, String encode){
        byte[] data=getRequestData(params,encode).toString().getBytes();
        try{
            URL url=new URL(urlPath);
            HttpURLConnection con=(HttpURLConnection)url.openConnection();
            con.setConnectTimeout(TIMEOUT_IN_MILLIONS);
            con.setDoInput(true);  //打开输入流,以便从服务器获取数据
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setUseCaches(false);  //使用Post方式不能使有Caches;
            con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            //设置请求体的长度
            con.setRequestProperty("Content-Length",String.valueOf(data.length));
            OutputStream os=con.getOutputStream();
            os.write(data);
            int responseCode=con.getResponseCode();
            Log.v("hook_responseCode",String.valueOf(responseCode));
            if(responseCode==HttpURLConnection.HTTP_OK){
                InputStream inputStream=con.getInputStream();
                return dealResponseRequest(inputStream);
            }
        }catch(Exception e){
            Log.e("hook_err",e.getMessage().toString());
        }
        return "-1";
    }


    public static StringBuilder getRequestData(Map<String,String> params, String encode){
        StringBuilder sb=new StringBuilder();
        for(Map.Entry entry : params.entrySet()){
            sb.append(entry.getKey())
                    .append("=")
                    .append(StringUtil.myUrlEncodedata((String)entry.getValue(),encode))
                    .append("&");
        }
        sb.deleteCharAt(sb.length()-1); //删除最后面的一个"&"
        return sb;
    }


    public static String dealResponseRequest(InputStream inputStream){
        String resultData=null;
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        byte[] data=new byte[1024];
        int len=0;
        try{
            while ((len=inputStream.read(data))!=-1){
                os.write(data,0,len);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        resultData=new String(os.toByteArray());
        return resultData;
    }

}
