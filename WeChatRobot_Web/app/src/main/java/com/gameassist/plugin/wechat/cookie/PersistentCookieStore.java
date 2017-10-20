package com.gameassist.plugin.wechat.cookie;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.gameassist.plugin.utils.MyLog;
import com.gameassist.plugin.utils.StringUtils;
import com.gameassist.plugin.wechat.model.WechatMeta;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class PersistentCookieStore implements CookieStore {
    public static final String COOKIE_PREFS = "CookiePrefsFile";
    private  static final String TAG="Cookie_";
    private static final String COOKIE_NAME_PREFIX = "cookie_";

    private final SharedPreferences cookiePrefs;
    private  HashMap<String, ConcurrentHashMap<String, Cookie>> cookies;
    private static boolean isCookieExpired(Cookie cookie) {
        return cookie.expiresAt() < System.currentTimeMillis();
    }

    public PersistentCookieStore(Context context, WechatMeta wechatMeta) {
        cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, 0);
        String js = cookiePrefs.getString("wechatMeta", null);
        if(!TextUtils.isEmpty(js)){
            wechatMeta.initData(StringUtils.hexStringToString(js));
        }
        cookies = new HashMap<String, ConcurrentHashMap<String, Cookie>>();
     //   MyLog.e(TAG+"PersistentCookieStore ----- ");
        Map<String, ?> prefsMap = cookiePrefs.getAll();
        for (Map.Entry<String, ?> entry : prefsMap.entrySet()) {
            if (((String) entry.getValue()) != null && !((String) entry.getValue()).startsWith(COOKIE_NAME_PREFIX)) {
                String[] cookieNames = TextUtils.split((String) entry.getValue(), ",");
                for (String name : cookieNames) {
                    String encodedCookie = cookiePrefs.getString(COOKIE_NAME_PREFIX + name, null);
                    if (encodedCookie != null) {
                        Cookie decodedCookie = decodeCookie(encodedCookie);
                        if (decodedCookie != null) {
                            if (!cookies.containsKey(entry.getKey()))
                                cookies.put(entry.getKey(), new ConcurrentHashMap<String, Cookie>());
                            cookies.get(entry.getKey()).put(name, decodedCookie);
                            if("webwx_data_ticket".equals(decodedCookie.name())) {
                                WechatMeta.webwx_data_ticket=decodedCookie.value();
                            }
                        }
                       // MyLog.e(TAG+"----- "+decodedCookie.name()+"----"+decodedCookie.value());
                    }
                }

            }
        }
	}
    
    protected void add(HttpUrl uri, Cookie cookie) {
        String name = getCookieToken(cookie);
        if (cookie.persistent()) {
       // 	MyLog.e(TAG+" ---- getCookieToken --- "+name+"\t---"+cookie.name()+" cookie.value() --"+ cookie.value());
        	if("webwx_data_ticket".equals(cookie.name())) {
        		//System.out.println(TAG+" ---- getCookieToken --- "+name+"\t---"+cookie.name()+" cookie.value() --"+ cookie.value());
        		WechatMeta.webwx_data_ticket=cookie.value();
        	}
            if(cookie.name().contains("SyncKey")){
              //  MyLog.e(TAG+" ---- getCookieToken --- "+name+"\t---"+cookie.name()+" cookie.value() --"+ cookie.value());
            }
        	if (!cookies.containsKey(uri.host())) {
                cookies.put(uri.host(), new ConcurrentHashMap<String, Cookie>());
            }
            cookies.get(uri.host()).put(name, cookie);
        } else {
            if (cookies.containsKey(uri.host())) {
                cookies.get(uri.host()).remove(name);
            } else {
                return;
            }
        }
       // MyLog.e(TAG+"name; "+name+"  ---- add --- "+uri.host());
        SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
        prefsWriter.putString(uri.host(), TextUtils.join(",", cookies.get(uri.host()).keySet()));
        prefsWriter.putString(COOKIE_NAME_PREFIX + name, encodeCookie(new SerializableHttpCookie(cookie)));
        prefsWriter.apply();
    }



    public static  void saveDataToSharedPre(Context context, String key, String value){
        if(!TextUtils.isEmpty(value)){
            SharedPreferences cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, 0);
            SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
            prefsWriter.putString(key, StringUtils.byteArrayToHexString(value.getBytes()));
            prefsWriter.apply();
        }
    }



    protected String getCookieToken(Cookie cookie) {
        return cookie.name() + cookie.domain();
    }

    @Override
    public void add(HttpUrl uri, List<Cookie> cookies) {
    	System.out.println(TAG+"add --- "+uri.toString()+"  "+cookies.size());
        for (Cookie cookie : cookies) {
            add(uri, cookie);
        }
    }

    @Override
    public List<Cookie> get(HttpUrl uri) {
    	//MyLog.e(TAG+"get --- "+uri.toString()+" -- "+uri.host());
        ArrayList<Cookie> ret = new ArrayList<Cookie>();
        if (cookies.containsKey(uri.host())) {
            Collection<Cookie> cookies = this.cookies.get(uri.host()).values();
            for (Cookie cookie : cookies) {
                if (isCookieExpired(cookie)) {
                    remove(uri, cookie);
                } else {
                    ret.add(cookie);
                }
            }
        }
        return ret;
    }

    @Override
    public boolean removeAll() {
      //  MyLog.e(TAG+"remove --- all");
        SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
        prefsWriter.clear();
        prefsWriter.apply();
        cookies.clear();
        return true;
    }


    @Override
    public boolean remove(HttpUrl uri, Cookie cookie) {
        String name = getCookieToken(cookie);
      //  MyLog.e("remove ---"+name);
        if (cookies.containsKey(uri.host()) && cookies.get(uri.host()).containsKey(name)) {
            cookies.get(uri.host()).remove(name);

            SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
            if (cookiePrefs.contains(COOKIE_NAME_PREFIX + name)) {
                prefsWriter.remove(COOKIE_NAME_PREFIX + name);
            }
            prefsWriter.putString(uri.host(), TextUtils.join(",", cookies.get(uri.host()).keySet()));
            prefsWriter.apply();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Cookie> getCookies() {
    	//MyLog.e(TAG+"getCookies");
        ArrayList<Cookie> ret = new ArrayList<Cookie>();
        for (String key : cookies.keySet())
            ret.addAll(cookies.get(key).values());
        return ret;
    }


    protected String encodeCookie(SerializableHttpCookie cookie) {
        if (cookie == null)
            return null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(os);
            outputStream.writeObject(cookie);
        } catch (IOException e) {
            //Log.d("google_lenve_fb", "IOException in encodeCookie", e);
            return null;
        }
        return StringUtils.byteArrayToHexString(os.toByteArray());
    }

    protected Cookie decodeCookie(String cookieString) {
        byte[] bytes = StringUtils.hexStringtoByteArray1(cookieString);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Cookie cookie = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            cookie = ((SerializableHttpCookie) objectInputStream.readObject()).getCookie();
        } catch (IOException e) {
           // M.d("google_lenve_fb", "IOException in decodeCookie", e);
        } catch (ClassNotFoundException e) {
            //Log.d("google_lenve_fb", "ClassNotFoundException in decodeCookie", e);
        }
        return cookie;
    }

}
