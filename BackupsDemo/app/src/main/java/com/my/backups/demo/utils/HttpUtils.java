package com.my.backups.demo.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by hu on 9/12/16.
 */
public class HttpUtils {
    private  static final int REQUEST_TIMEOUT_IN_MILLIONS = 5000;

    public static void requestData(Handler handler,String requestUrl){
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            URL url = new URL(requestUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(2000);
            conn.setConnectTimeout(REQUEST_TIMEOUT_IN_MILLIONS);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            Log.i("hook_requestUrl",requestUrl);
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Log.v("hook_getResponseCode","200");
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                int len = -1;
                byte[] buf = new byte[1024];
                while ((len = is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                baos.flush();
                String result=baos.toString();
                Message msg=Message.obtain();
                msg.what=1;
                Bundle b=new Bundle();
                b.putString("data", baos.toString());
                b.putString("url", requestUrl);
                msg.setData(b);
                handler.sendMessage(msg);
            } else {
                Log.e("hook_requestData_3","url getResponseCode not 200");
                Message msg=Message.obtain();
                msg.what=0;
                Bundle b=new Bundle();
                b.putString("data", "url getResponseCode not 200");
                b.putString("url", requestUrl);
                msg.setData(b);
                handler.sendMessage(msg);
            }
        } catch (Exception e) {
            Log.e("hook_requestData_2","url request close exception");
            Message msg=Message.obtain();
            msg.what=0;
            Bundle b=new Bundle();
            b.putString("data", "url request close exception");
            b.putString("url", requestUrl);
            msg.setData(b);
            handler.sendMessage(msg);
        } finally {
            try {
                if (baos != null)
                    baos.close();
                if (is != null)
                    is.close();
                if(conn!=null) {
                    conn.disconnect();
                }
            } catch (IOException e) {
                Log.e("hook_requestData_1","url request close exception");
            }
        }
    }





    public static String submitPostData(String urlPath, Map<String,String> params, String encode){
        Log.i("hook_submitPostData","begin");
        byte[] data=getRequestData(params,encode).toString().getBytes();
        String result=null;
        HttpURLConnection con=null;
        OutputStream os=null;
        InputStream inputStream=null;
        try{
            URL url=new URL(urlPath);
            con=(HttpURLConnection)url.openConnection();
            con.setConnectTimeout(REQUEST_TIMEOUT_IN_MILLIONS);
            con.setDoInput(true);  //打开输入流,以便从服务器获取数据
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setUseCaches(false);  //使用Post方式不能使有Caches;
            con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            //设置请求体的长度
            con.setRequestProperty("Content-Length",String.valueOf(data.length));
            os=con.getOutputStream();
            os.write(data);
            int responseCode=con.getResponseCode();
            Log.v("hook_responseCode",String.valueOf(responseCode));
            if(responseCode==HttpURLConnection.HTTP_OK){
                inputStream=con.getInputStream();
                result=dealResponseRequest(inputStream);
                return result;
            }else{
               return ""+responseCode;
            }
        }catch(Exception e){
            Log.e("hook_err",e.getMessage().toString());
        }finally {
            try {
                if (con != null) {
                    con.disconnect();
                }
                if (os != null) {
                    os.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }catch (Exception e){
                Log.e("hook_Interface","url submitPostData close exception");
            }
        }
        if(result!=null){
            return result;
        }
        return "-1";
    }


    private static StringBuilder getRequestData(Map<String,String> params, String encode){
        StringBuilder sb=new StringBuilder();
        for(Map.Entry entry : params.entrySet()){
            sb.append(entry.getKey())
                    .append("=")
                    .append(StringUtil.myUrlEncodedata((String)entry.getValue(),encode))
                    .append("&");
        }
        sb.deleteCharAt(sb.length()-1); //删除最后面的一个"&"
        Log.i("hook_getRequestData",sb.toString());
        return sb;
    }


    private static String dealResponseRequest(InputStream inputStream){
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
