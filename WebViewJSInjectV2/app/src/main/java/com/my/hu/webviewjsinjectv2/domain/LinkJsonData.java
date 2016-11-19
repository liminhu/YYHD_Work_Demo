package com.my.hu.webviewjsinjectv2.domain;

import java.io.Serializable;

/**
 * Created by hu on 9/23/16.
 */
public class LinkJsonData implements Serializable {
    public int  state;    //不能是静态变量
    public int gen_time;

    public int id;
    public String md5;
    public String platform_name;
    public String link_url;
    public String update_time;

    public static final String ID="id";
    public static final String PLATFORM_NAME="platform_name";
    public static final String MD5="md5";
    public static final  String STATE="state";
    public static final String Link_URL ="link_url";
    public static final String UPDATE_TIME="update_time";


    public static final  String GEN_TIME="gen_time";
}
