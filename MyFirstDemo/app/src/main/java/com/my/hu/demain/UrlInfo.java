package com.my.hu.demain;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hu on 9/7/16.
 */
public class UrlInfo {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private  int  id;
    private  String platform_name;
    private  String download_package_name;
    private  String downLoad_url;
    private  String request_time;
    private  String  md5;


    public UrlInfo() {
        md5="";
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }



    public String getPlatform_name() {
        return platform_name;
    }

    public void setPlatform_name(String platform_name) {
        this.platform_name = platform_name;
    }

    public String getDownload_package_name() {
        return download_package_name;
    }

    public void setDownload_package_name(String download_package_name) {
        this.download_package_name = download_package_name;
    }

    public String getDownLoad_url() {
        return downLoad_url;
    }

    public void setDownLoad_url(String downLoad_url) {
        this.downLoad_url = downLoad_url;
    }

    public String getRequest_time() {
        return request_time;
    }

    public void setRequest_time(String request_time) {
        this.request_time = request_time;
    }


    public void setRequest_time(Date currentDateTime) {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=format.format(currentDateTime);
        this.request_time = time;
    }



}
