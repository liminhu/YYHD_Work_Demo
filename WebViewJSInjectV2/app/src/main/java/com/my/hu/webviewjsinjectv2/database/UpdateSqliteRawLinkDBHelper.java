package com.my.hu.webviewjsinjectv2.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.my.hu.webviewjsinjectv2.domain.LinkJsonData;
import com.my.hu.webviewjsinjectv2.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hu on 9/23/16.
 */
public class UpdateSqliteRawLinkDBHelper {
    private SQLiteDatabase db;

    public UpdateSqliteRawLinkDBHelper(NewSqliteCreateRawLinkDB dbCreateDB) {
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


    public int saveUpdateJsonDataInfo(LinkJsonData data, String tb_name) {
        ContentValues values = new ContentValues();
        values.put(LinkJsonData.MD5, data.md5);
        values.put(LinkJsonData.Link_URL, data.link_url);
        values.put(LinkJsonData.PLATFORM_NAME, data.platform_name);
        values.put(LinkJsonData.UPDATE_TIME, data.update_time);
        values.put(LinkJsonData.STATE, data.state);
        values.put(LinkJsonData.GEN_TIME, data.gen_time);  //更新的次数
        db.insert(tb_name, null, values);
        return 1;
    }


    public List<LinkJsonData> getUpateLinkJsonDataList(String tb_name) {
        List<LinkJsonData> linkJsonDatas = new ArrayList<LinkJsonData>();
        Cursor cursor = db.query(tb_name, null, null, null, null,
                null, "id asc");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {  //返回游标是否指向第最后一行的位置
            LinkJsonData jsonData = new LinkJsonData();
            jsonData.id = cursor.getInt(cursor.getColumnIndex(LinkJsonData.ID));
            jsonData.platform_name = cursor.getString(cursor.getColumnIndex(LinkJsonData.PLATFORM_NAME));
            jsonData.md5 = cursor.getString(cursor.getColumnIndex(LinkJsonData.MD5));
            jsonData.update_time = cursor.getString(cursor.getColumnIndex(LinkJsonData.UPDATE_TIME));
            jsonData.link_url = cursor.getString(cursor.getColumnIndex(LinkJsonData.Link_URL));
            jsonData.state = cursor.getInt(cursor.getColumnIndex(LinkJsonData.STATE));
            //Log.i("hook_state_show",""+jsonData.state);
            jsonData.gen_time = cursor.getInt(cursor.getColumnIndex(LinkJsonData.GEN_TIME));
            linkJsonDatas.add(jsonData);
            cursor.moveToNext();
        }
        cursor.close();
        return linkJsonDatas;
    }


    public List<LinkJsonData> getLinkJsonDataListOnlyByPlatformName(String tb_name, String platformName) {
        List<LinkJsonData> linkJsonDatas = new ArrayList<LinkJsonData>();
        Cursor cursor = db.rawQuery("select * from " + tb_name + " where platform_name=? order by id asc", new String[]{platformName});
        cursor.moveToFirst();
        //测试一条
        while (!cursor.isAfterLast()) {  //返回游标是否指向第最后一行的位置
            LinkJsonData jsonData = new LinkJsonData();
            jsonData.id = cursor.getInt(cursor.getColumnIndex(LinkJsonData.ID));
            jsonData.platform_name = cursor.getString(cursor.getColumnIndex(LinkJsonData.PLATFORM_NAME));
            jsonData.md5 = cursor.getString(cursor.getColumnIndex(LinkJsonData.MD5));
            jsonData.update_time = cursor.getString(cursor.getColumnIndex(LinkJsonData.UPDATE_TIME));
            jsonData.link_url = cursor.getString(cursor.getColumnIndex(LinkJsonData.Link_URL));
            jsonData.state = cursor.getInt(cursor.getColumnIndex(LinkJsonData.STATE));
            jsonData.gen_time = cursor.getInt(cursor.getColumnIndex(LinkJsonData.GEN_TIME));
            linkJsonDatas.add(jsonData);
            cursor.moveToNext();
        }
        cursor.close();
        return linkJsonDatas;
    }

    public List<LinkJsonData> getLinkJsonDataListByPlatformNameAndState(String tb_name, String platformName, int state) {
        List<LinkJsonData> linkJsonDatas = new ArrayList<LinkJsonData>();
        Cursor cursor = db.rawQuery("select * from " + tb_name + " where platform_name=? and state=? order by id asc", new String[]{platformName, String.valueOf(state)});
        cursor.moveToFirst();
        //测试一条
        while (!cursor.isAfterLast()) {  //返回游标是否指向第最后一行的位置
            LinkJsonData jsonData = new LinkJsonData();
            jsonData.id = cursor.getInt(cursor.getColumnIndex(LinkJsonData.ID));
            jsonData.platform_name = cursor.getString(cursor.getColumnIndex(LinkJsonData.PLATFORM_NAME));
            jsonData.md5 = cursor.getString(cursor.getColumnIndex(LinkJsonData.MD5));
            jsonData.update_time = cursor.getString(cursor.getColumnIndex(LinkJsonData.UPDATE_TIME));
            jsonData.link_url = cursor.getString(cursor.getColumnIndex(LinkJsonData.Link_URL));
            jsonData.state = cursor.getInt(cursor.getColumnIndex(LinkJsonData.STATE));
            jsonData.gen_time = cursor.getInt(cursor.getColumnIndex(LinkJsonData.GEN_TIME));
            linkJsonDatas.add(jsonData);
            cursor.moveToNext();
        }
        cursor.close();
        return linkJsonDatas;
    }



    public int needUpdateJsonDataInfo(LinkJsonData data, String tb_name) {
        data.update_time= StringUtil.getDateTime();
        data.gen_time+=1;
        ContentValues values = new ContentValues();
        values.put(LinkJsonData.MD5, data.md5);
        values.put(LinkJsonData.Link_URL, data.link_url);
        values.put(LinkJsonData.PLATFORM_NAME, data.platform_name);
        values.put(LinkJsonData.UPDATE_TIME, data.update_time);
        values.put(LinkJsonData.STATE, data.state);
        values.put(LinkJsonData.GEN_TIME, data.gen_time);
        String[] args = {String.valueOf(data.id)};
        Log.i("hook_i",data.id+"id :needUpdateJsonDataInfo_rawlink_"+data.state);
        return db.update(tb_name, values, "id=?" ,args);
    }


}
