package com.gameassist.plugin.wechat.utils;

import com.gameassist.plugin.utils.MyLog;
import com.gameassist.plugin.wechat.cookie.CookieJarImpl;
import com.gameassist.plugin.wechat.cookie.PersistentCookieStore;
import com.gameassist.plugin.wechat.exception.WechatException;
import com.gameassist.plugin.wechat.model.WechatMeta;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;


import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by hulimin on 2017/10/18.
 */

public class OkhttpsCookieUtils {
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.109 Safari/537.36";

    public String getUUID(String url, Map<String, String> map){
        try{
            FormBody.Builder formBodyBuild=new FormBody.Builder();
            Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                formBodyBuild.add(entry.getKey(), entry.getValue());
            }
            FormBody formBody=formBodyBuild.build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .addHeader("User-agent", USER_AGENT)
                    .build();
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            String result=response.body().string();
            MyLog.e(result);
            String code = MatchersUtils.match("window.QRLogin.code = (\\d+);", result);
            if (null != code) {
                if (code.equals("200")) {
                    return MatchersUtils.match("window.QRLogin.uuid = \"(.*)\";",result);
                } else {
                    throw new WechatException("错误的状态码: " + code);
                }
            }
            return null;
        }catch (Exception e){
            MyLog.e(e.getMessage());
        }
        return  null;
    }





    public String getInitWebwxWithCookie(String url, PersistentCookieStore persistentCookieStore, WechatMeta wechatMeta){
        try{
            CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
            OkHttpClient client = new OkHttpClient.Builder().cookieJar(cookieJarImpl).build();
            JSONObject data=CommonUtils.getInitWebwxParmentJsonStr(wechatMeta);
            JSONObject json=new JSONObject();
            json.put("BaseRequest", data);
            RequestBody body=RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json.toString());
            Request request = new Request.Builder()
                    .addHeader("ContentType", "application/json; charset=UTF-8")
                    .url(url)
                    .post(body).build();
            Response response = client.newCall(request).execute();
            String result=response.body().string();
           // MyLog.e("getInitWebwxWithCookie ---- "+result);
           // MyLog.e("getInitWebwxWithCookie ---- ---------------");
            return result;
        }catch (Exception e){
            MyLog.e(e.getMessage());
        }
        return  null;
    }




    public String sendDataWebwxWithCookie(String url, JSONObject msg,  JSONObject BaseRequest, PersistentCookieStore persistentCookieStore){
        try{
            CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
            OkHttpClient client = new OkHttpClient.Builder().cookieJar(cookieJarImpl).build();
            JSONObject json=new JSONObject();
            json.put("BaseRequest", BaseRequest);
            json.put("Msg", msg);
            RequestBody body=RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json.toString());
            Request request = new Request.Builder()
                    .addHeader("ContentType", "application/json; charset=UTF-8")
                    .url(url)
                    .post(body).build();
            Response response = client.newCall(request).execute();
            String result=response.body().string();
         //   MyLog.e(result);
            return result;
        }catch (Exception e){
            MyLog.e(e.getMessage());
        }
        return  null;
    }




    public String getDataWebwxWithCookie(String url, JSONObject json, PersistentCookieStore persistentCookieStore){
        try{
            CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
            OkHttpClient client = new OkHttpClient.Builder().cookieJar(cookieJarImpl).build();
            RequestBody body=RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json.toString());
            Request request = new Request.Builder()
                    .addHeader("ContentType", "application/json; charset=UTF-8")
                    .url(url)
                    .post(body).build();
            Response response = client.newCall(request).execute();
            String result=response.body().string();
           // MyLog.e(result);
            return result;
        }catch (Exception e){
            MyLog.e(e.getMessage());
        }
        return  null;
    }




    public String getWithCookieLogin(String url, PersistentCookieStore persistentCookieStore){
        try{
            CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
            OkHttpClient client = new OkHttpClient.Builder().cookieJar(cookieJarImpl).build();
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("User-agent", USER_AGENT)
                    .addHeader("Referer", "https://wx.qq.com/")
                    .build();
            Response response = client.newCall(request).execute();
            String result=response.body().string();
          //  MyLog.e(result);
            return result;
        }catch (Exception e){
            MyLog.e(e.getMessage());
        }
        return  null;
    }





    public String get(String url){
        try{
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("User-agent", USER_AGENT)
                    .addHeader("Referer", "https://wx.qq.com/")
                    .build();
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            String result=response.body().string();
            MyLog.e(result);
            String code = MatchersUtils.match("window.code=(\\d+);", result);
            if (null != code) {
                if (code.equals("200")) {
                    String redirect_uri=MatchersUtils.match("window.redirect_uri=\"(\\S+?)\";",result);
                    MyLog.e(redirect_uri);
                    return redirect_uri;
                } else {
                    throw new WechatException("错误的状态码: " + code);
                }
            }
            return null;
        }catch (Exception e){
            MyLog.e(e.getMessage());
        }
        return  null;
    }

}
