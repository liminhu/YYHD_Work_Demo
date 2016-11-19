package com.my.hu.webviewjsinjectv2.domain;

import java.util.HashMap;

/**
 * Created by hu on 9/21/16.
 */
public class RawLinkHashMapData {
    private String platform_name;
    private HashMap<String,String> data;


    public String getPlatform_name() {
        return platform_name;
    }

    public void setPlatform_name(String platform_name) {
        this.platform_name = platform_name;
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public void setData(HashMap<String, String> data) {
        this.data = data;
    }
}
