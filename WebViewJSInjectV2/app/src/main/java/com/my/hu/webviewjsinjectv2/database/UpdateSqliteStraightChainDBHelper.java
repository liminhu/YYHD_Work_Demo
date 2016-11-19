package com.my.hu.webviewjsinjectv2.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.my.hu.webviewjsinjectv2.domain.StraightChainData;
import com.my.hu.webviewjsinjectv2.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hu on 9/23/16.
 */
public class UpdateSqliteStraightChainDBHelper {
    private SQLiteDatabase db;

    public UpdateSqliteStraightChainDBHelper(NewSqliteCreateStraightChainDB dbCreateDB) {
        db = dbCreateDB.getWritableDatabase();
    }

    public void closeDataBase() {
        if (db != null) {
            db.close();
        }
    }

    public boolean isHaveJsonDataByMD5(String md5, String tb_name) {
        boolean result = false;
        Cursor c = db.rawQuery("select * from " + tb_name + " where md5=?", new String[]{md5});
        if (c.moveToFirst()) {
            Log.d("hook_md5_exist", "isHaveJsonDataByMD5");
            result = true;
        }
        c.close();
        return result;
    }


    public int saveUpdateStraightChainInfo(StraightChainData data, String tb_name) {
        ContentValues values = new ContentValues();
        values.put(StraightChainData.MD5, data.md5);
        values.put(StraightChainData.Link_URL, data.link_url);
        values.put(StraightChainData.PLATFORM_NAME, data.platform_name);
        values.put(StraightChainData.UPDATE_TIME, data.update_time);
        values.put(StraightChainData.STATE, 0);
        db.insert(tb_name, null, values);
        return 1;
    }






    public List<StraightChainData> getStraightChainDataList(String tb_name) {
        List<StraightChainData> linkJsonDatas = new ArrayList<StraightChainData>();
        Cursor cursor = db.query(tb_name, null, null, null, null,
                null, "id asc");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {  //返回游标是否指向第最后一行的位置
            StraightChainData jsonData = new StraightChainData();
            jsonData.id = cursor.getInt(cursor.getColumnIndex(StraightChainData.ID));
            jsonData.platform_name = cursor.getString(cursor.getColumnIndex(StraightChainData.PLATFORM_NAME));
            jsonData.md5 = cursor.getString(cursor.getColumnIndex(StraightChainData.MD5));
            jsonData.update_time = cursor.getString(cursor.getColumnIndex(StraightChainData.UPDATE_TIME));
            jsonData.link_url = cursor.getString(cursor.getColumnIndex(StraightChainData.Link_URL));
            jsonData.state = cursor.getInt(cursor.getColumnIndex(StraightChainData.STATE));
            linkJsonDatas.add(jsonData);
            cursor.moveToNext();
        }
        cursor.close();
        return linkJsonDatas;
    }


    public List<StraightChainData> getStraightChainDataListOnlyByPlatformName(String tb_name, String platformName) {
        List<StraightChainData> linkJsonDatas = new ArrayList<StraightChainData>();
        Cursor cursor = db.rawQuery("select * from " + tb_name + " where platform_name=? order by id asc", new String[]{platformName});
        cursor.moveToFirst();
        //测试一条
        while (!cursor.isAfterLast()) {  //返回游标是否指向第最后一行的位置
            StraightChainData jsonData = new StraightChainData();
            jsonData.id = cursor.getInt(cursor.getColumnIndex(StraightChainData.ID));
            jsonData.platform_name = cursor.getString(cursor.getColumnIndex(StraightChainData.PLATFORM_NAME));
            jsonData.md5 = cursor.getString(cursor.getColumnIndex(StraightChainData.MD5));
            jsonData.update_time = cursor.getString(cursor.getColumnIndex(StraightChainData.UPDATE_TIME));
            jsonData.link_url = cursor.getString(cursor.getColumnIndex(StraightChainData.Link_URL));
            jsonData.state = cursor.getInt(cursor.getColumnIndex(StraightChainData.STATE));
            linkJsonDatas.add(jsonData);
            cursor.moveToNext();
        }
        cursor.close();
        return linkJsonDatas;
    }


    public List<StraightChainData> getStraightChainDataListOnlyByPlatformNameAndState(String tb_name, String platformName, int state) {
        List<StraightChainData> linkJsonDatas = new ArrayList<StraightChainData>();
        Cursor cursor = db.rawQuery("select * from " + tb_name + " where platform_name=? and state=? order by id asc", new String[]{platformName, String.valueOf(state)});
        cursor.moveToFirst();
        //测试一条
        while (!cursor.isAfterLast()) {  //返回游标是否指向第最后一行的位置
            StraightChainData jsonData = new StraightChainData();
            jsonData.id = cursor.getInt(cursor.getColumnIndex(StraightChainData.ID));
            jsonData.platform_name = cursor.getString(cursor.getColumnIndex(StraightChainData.PLATFORM_NAME));
            jsonData.md5 = cursor.getString(cursor.getColumnIndex(StraightChainData.MD5));
            jsonData.update_time = cursor.getString(cursor.getColumnIndex(StraightChainData.UPDATE_TIME));
            jsonData.link_url = cursor.getString(cursor.getColumnIndex(StraightChainData.Link_URL));
            jsonData.state = cursor.getInt(cursor.getColumnIndex(StraightChainData.STATE));
            linkJsonDatas.add(jsonData);
            cursor.moveToNext();
        }
        cursor.close();
        Log.i("hook_db","getStraightChainDataListOnlyByPlatformNameAndState");
        return linkJsonDatas;
    }



    public List<StraightChainData> getStraightChainDataListByState(String tb_name,  int state) {
        List<StraightChainData> linkJsonDatas = new ArrayList<StraightChainData>();
        Cursor cursor = db.rawQuery("select * from " + tb_name + " where  state=? order by id asc", new String[]{ String.valueOf(state)});
        cursor.moveToFirst();
        //测试一条
        while (!cursor.isAfterLast()) {  //返回游标是否指向第最后一行的位置
            StraightChainData jsonData = new StraightChainData();
            jsonData.id = cursor.getInt(cursor.getColumnIndex(StraightChainData.ID));
            jsonData.platform_name = cursor.getString(cursor.getColumnIndex(StraightChainData.PLATFORM_NAME));
            jsonData.md5 = cursor.getString(cursor.getColumnIndex(StraightChainData.MD5));
            jsonData.update_time = cursor.getString(cursor.getColumnIndex(StraightChainData.UPDATE_TIME));
            jsonData.link_url = cursor.getString(cursor.getColumnIndex(StraightChainData.Link_URL));
            jsonData.state = cursor.getInt(cursor.getColumnIndex(StraightChainData.STATE));
            linkJsonDatas.add(jsonData);
            cursor.moveToNext();
        }
        cursor.close();
        return linkJsonDatas;
    }
    
    public int needUpdateStraightChainDataInfo(StraightChainData data, String tb_name) {
        data.update_time= StringUtil.getDateTime();
        ContentValues values = new ContentValues();
        values.put(StraightChainData.MD5, data.md5);
        values.put(StraightChainData.Link_URL, data.link_url);
        values.put(StraightChainData.PLATFORM_NAME, data.platform_name);
        values.put(StraightChainData.UPDATE_TIME, data.update_time);
        values.put(StraightChainData.STATE, data.state);
        String[] args = {String.valueOf(data.id)};
        return db.update(tb_name, values, "id=?" ,args);
    }


}
