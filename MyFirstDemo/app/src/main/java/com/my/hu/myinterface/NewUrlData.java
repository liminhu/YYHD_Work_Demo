package com.my.hu.myinterface;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hu on 9/9/16.
 */
public class NewUrlData {
    public  int  id;
    public  String platform_name;
    public  String download_package_name;
    public  String new_downLoad_url;
    public  String request_time;

    public String md5;
    public String old_url;


    public static final String ID="id";
    public static final String PLATFORM_NAME="platform_name";

    public  static final String DOWNLOAD_PACKAGE_NAME="download_package_name";
    public  static final String NEW_DOWNLOAD_URL="new_downLoad_url";
    public  static final String REQUEST_TIME="request_time";



    public static final String MD5="md5";
    public static final String OLD_URL="old_url";


    public NewUrlData(){
        setRequest_time(new Date());
    }

    private void setRequest_time(Date date) {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=format.format(date);
        this.request_time = time;
    }

}
