package com.my.hu.myinterface;

import android.os.Parcelable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hu on 9/9/16.
 */
public class JsonData implements Serializable {
    public int id;
    public String platform_name;
    public String md5;
    public String old_url;
    public String update_time;


    public static final String ID="id";
    public static final String PLATFORM_NAME="platform_name";
    public static final String MD5="md5";
    public static final String OLD_URL="old_url";
    public static final String UPDATE_TIME="update_time";


    public JsonData(){
        setUpdate_time(new Date());
    }

    private void setUpdate_time(Date date) {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=format.format(date);
        this.update_time = time;
    }


}
