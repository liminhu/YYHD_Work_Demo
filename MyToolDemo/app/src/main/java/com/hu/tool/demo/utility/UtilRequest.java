package com.hu.tool.demo.utility;

import android.content.Context;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Exchanger;

/**
 * Created by hu on 17-1-16.
 */

public class UtilRequest {
    /**
     * 获取设备各种信息
     */

    public synchronized static JSONObject getDeviceInfo(Context context){
        String linuxVerisonDesc=getLinuxVersionDesc();

    }


    private static String getLinuxVersionDesc(){
        InputStream is3=new FileInputStream("/proc/version");
        return getS
    }


    private static String getStringFromInputStream(InputStream is, Map formatter){
        StringBuilder sb=new StringBuilder();
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        String line=null;
        boolean isMapInitPut=(formatter == null || formatter.size()<1) ? false : true;
        try {
            while ((line=br.readLine()) != null){
                if(formatter != null && line.contains(":")){
                    String[] values=line.split(":");
                    //解析getprop, /proc/meminfo
                    if(isMapInitPut){
                        putValueToMap(formatter, values[0],values[1]);
                    }else{
                        if(values[0].trim().equals("processor")){
                            formatter.put("processorcnt",values[1].trim());
                        }else {
                            formatter.put(values[0].trim(),values[1].trim());
                        }
                    }
                    sb.append(line);
                    sb.append("\n");
                }
            }
        }catch (IOException e){
        }finally {
            if(br!=null){
                try {
                    br.close();
                }catch (IOException e){

                }
            }
        }
        return sb.toString();
    }


    private static void putValueToMap(Map<String,String>map, String key, String val){
        key=key.replace("[","").replace("]","").trim();
        val=val.replace("[","").replace("]","").trim();
        Iterator iterator=map.keySet().iterator();
        if(iterator != null && iterator.hasNext()){
            while (iterator.hasNext()){
                String lkey=(String)iterator.next();
                if(lkey.equals(key)) {
                    map.put(lkey, val);
                    break;
                }
            }
        }
    }

    private static String getMemTotal(){
        try{
            Map<String, String> memTotalMap=new HashMap<String, String>();
            memTotalMap.put("MemTotal", "");
            InputStream is2=new FileInputStream("/proc/meminfo");
            getStringFromInputStream(is2, memTotalMap);
            return memTotalMap.get("MemTotal");
        }catch (Exception e){
            return null;
        }
    }

    private static String parseLinuxVersion(String value)throws Exception{
        String linuxVersion=value.toLowerCase();
        if(linuxVersion.contains("(")){
            linuxVersion=linuxVersion.split("\\(")[0];
            if(linuxVersion.contains("version")){
                linuxVersion=linuxVersion.split("version")[1];
                if(linuxVersion.contains("-")){
                    linuxVersion=linuxVersion.split("-")[0];
                }
            }
        }
        return linuxVersion.trim();
    }


    private static JSONObject getCpuInfo(){
        try{
            Map<String,String> cpuInfoMap=new HashMap<String,String>();
            InputStream is=new FileInputStream("/proc/cpuinfo");

        }catch (Exception e){
            return null;
        }
    }


















}
