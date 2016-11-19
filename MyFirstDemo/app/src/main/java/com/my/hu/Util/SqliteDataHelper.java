package com.my.hu.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.my.hu.demain.UrlInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by hu on 9/7/16.
 */
public class SqliteDataHelper {
    private static String DB_NAME="my_download_url.db";
    private static int DB_VERSION=1;
    private SqliteCreateDB dbCreateHelper;
    private SQLiteDatabase db;

    //private static final String platform_name="ty";

    public SqliteDataHelper(Context context){
        dbCreateHelper=new SqliteCreateDB(context, DB_NAME, DB_VERSION);
        db=dbCreateHelper.getWritableDatabase();
    }

    public void closeDB(){
        db.close();
        dbCreateHelper.close();
    }

    public List<UrlInfo> getUrlList(){
        List<UrlInfo> urlList=new ArrayList<UrlInfo>();
        Cursor cursor=db.query(SqliteCreateDB.TY_TB_NAME, null, null,null,null,
                null, "id asc");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){  //返回游标是否指向第最后一行的位置
            UrlInfo url=new UrlInfo();
            url.setId(cursor.getInt(cursor.getColumnIndex("id")));
            url.setDownload_package_name(cursor.getString(cursor.getColumnIndex("download_package_name")));
            url.setDownLoad_url(cursor.getString(cursor.getColumnIndex("downLoad_url")));
            url.setRequest_time(cursor.getString(cursor.getColumnIndex("request_time")));
            url.setPlatform_name(cursor.getString(cursor.getColumnIndex("platform_name")));
            url.setMd5(cursor.getString(cursor.getColumnIndex("md5")));
          //  Log.v("hook_md5",url.getMd5());
            urlList.add(url);
            cursor.moveToNext();
        }
        cursor.close();
        return urlList;
    }


    public List<UrlInfo> getBaiduUrlList(){
        List<UrlInfo> urlList=new ArrayList<UrlInfo>();
        Cursor cursor=db.query(SqliteCreateDB.BAIDY_TB_NAME, null, null,null,null,
                null, "id asc");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){  //返回游标是否指向第最后一行的位置
            UrlInfo url=new UrlInfo();
            url.setId(cursor.getInt(cursor.getColumnIndex("id")));
            url.setDownload_package_name(cursor.getString(cursor.getColumnIndex("download_package_name")));
            url.setDownLoad_url(cursor.getString(cursor.getColumnIndex("downLoad_url")));
            url.setRequest_time(cursor.getString(cursor.getColumnIndex("request_time")));
            url.setPlatform_name(cursor.getString(cursor.getColumnIndex("platform_name")));
            url.setMd5(cursor.getString(cursor.getColumnIndex("md5")));
            //  Log.v("hook_md5",url.getMd5());
            urlList.add(url);
            cursor.moveToNext();
        }
        cursor.close();
        return urlList;
    }


    public boolean isHaveUrlInfoById(int id){
        boolean result=false;
        Cursor cursor=db.query(SqliteCreateDB.TY_TB_NAME, null, "id="+id, null,null,null,null);
        result=cursor.moveToFirst();
        cursor.close();
        return result;
    }

    public UrlInfo getUrlInfoById(int id){
        Cursor cursor=db.query(SqliteCreateDB.TY_TB_NAME, null, "id="+id, null,null,null,null);
        UrlInfo url=null;
        if(cursor.moveToFirst()){
            url=new UrlInfo();
            url.setId(cursor.getInt(cursor.getColumnIndex("id")));
            url.setDownload_package_name(cursor.getString(cursor.getColumnIndex("download_package_name")));
            url.setDownLoad_url(cursor.getString(cursor.getColumnIndex("downLoad_url")));
            url.setRequest_time(cursor.getString(cursor.getColumnIndex("request_time")));
            url.setPlatform_name(cursor.getString(cursor.getColumnIndex("platform_name")));
        }
        cursor.close();
        return url;
    }


    public boolean isHaveUrlByPackageName(String packageName){
        boolean result=false;
        Cursor cursor=db.query(SqliteCreateDB.TY_TB_NAME, null, "download_package_name="+packageName, null,null,null,null);
        result=cursor.moveToFirst();
        cursor.close();
        return result;
    }



    public int updateUrlInfoById(UrlInfo newUrl){
        ContentValues values=new ContentValues();
        values.put("download_package_name", newUrl.getDownload_package_name());
        values.put("downLoad_url", newUrl.getDownLoad_url());
        values.put("request_time", newUrl.getRequest_time());
        values.put("md5", newUrl.getMd5());
        int id=db.update(SqliteCreateDB.TY_TB_NAME, values, "id="+newUrl.getId(), null);
        return id;
    }

    public int saveUrlInfo(UrlInfo url){
        Log.d("hook_db_save_packNme",url.getDownload_package_name());
        ContentValues values=new ContentValues();
        values.put("download_package_name", url.getDownload_package_name());
        values.put("downLoad_url", url.getDownLoad_url());
        values.put("request_time", url.getRequest_time());
        values.put("md5", url.getMd5());
        db.insert(SqliteCreateDB.TY_TB_NAME, null, values);
        return 1;
    }


    public int saveBaiduUrlInfo(UrlInfo url){
        Log.d("hook_db_save_packNme",url.getDownload_package_name());
        ContentValues values=new ContentValues();
        values.put("download_package_name", url.getDownload_package_name());
        values.put("downLoad_url", url.getDownLoad_url());
        values.put("request_time", url.getRequest_time());
        values.put("md5", url.getMd5());
        db.insert(SqliteCreateDB.BAIDY_TB_NAME, null, values);
        return 1;
    }


    public int deleteUrlInfoById(int urlId){
        String[]  args={ String.valueOf(urlId)};
        return db.delete(SqliteCreateDB.TY_TB_NAME, "id=?",args);
    }

}
