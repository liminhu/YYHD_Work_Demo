package com.my.hu.webviewjsinjectv2.domain;

/**
 * Created by hu on 9/21/16.
 */
public class ConstantData {
    //url
    public static final  String RAW_LINK_URL="http://ggapi-2014904031.cn-north-1.elb.amazonaws.com.cn:8010/api/get/dead_pan_links";
    public static final  String SUBMIT_DATA_LINK_URL="http://ggapi-2014904031.cn-north-1.elb.amazonaws.com.cn:8010/api/put/dead_pan_links";


    //database
    public static final  String DB_RAW_LINK_NAME ="raw_link.db";
    public static final  String DB_SUBMIT_DATA_NAME="submit_link.db";
    public static int DB_VERSION=1;
    public static final  String TB_RAW_LINK="old_link_tb";
    public static final  String TB_OLD_BAIDU_LINK="old_Baidu_link_tb";

    public static final  String TB_BAIDU_CHAIN="baidu_chain_tb";
    public static final  String TB_TIANYI_CHAIN="tianyi_chain_tb";
    public static final  String TB_QH_360_CHAIN="qh360_chain_tb";

    public  static final String PLATFORM_OLD_BAIDU="old_baidu";
    public  static final String PLATFORM_BAIDU="baidu";
    public  static final String PLATFORM_TIANYI="tianyi";
    public  static final String PLATFORM_QH_360="360";

}
