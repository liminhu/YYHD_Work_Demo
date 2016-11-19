package com.my.hu.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by hu on 9/8/16.
 */
public class Provider {
    public static final String AUTHORITY="com.my.hu.myfirst.demo";

    public static final class UrlColumns implements BaseColumns{
        public static final Uri CONTENT_URI=Uri.parse("content://"+AUTHORITY+"/urlinfos");
        public static final String TABLE_NAME="ty_url_tb";
        public static final String DEFAULT_SORT_ORDER=" id asc"; //ctrl+sift+u
        public static final String ID="id";
        public static final String PLATFORM_NAME="platform_name";
        public static final String DOWNLOAD_PACKAGE_NAME="download_package_name";
        public static final String DOWNLOAD_URL="downLoad_url";
        public static final String REQUEST_TIME="request_time";
    }

}
