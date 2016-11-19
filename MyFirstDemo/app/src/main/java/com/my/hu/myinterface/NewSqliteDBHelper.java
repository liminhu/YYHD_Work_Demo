package com.my.hu.myinterface;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hu on 9/9/16.
 */
public class NewSqliteDBHelper {
    private NewSqliteCreateDB dbCreateDB;
    private SQLiteDatabase db;

    public NewSqliteDBHelper(Context context){
        dbCreateDB=new NewSqliteCreateDB(context);
        db=dbCreateDB.getWritableDatabase();
    }

    public void closeDataBase(){
        if(db!=null){
            db.close();
            dbCreateDB.close();
        }
    }


    public boolean isHaveJsonDataByMD5(String md5){
        boolean result=false;
        Cursor c = db.rawQuery("select * from "+NewSqliteCreateDB.OLD_URL_TB_NAME+" where md5=?",new String[]{md5});
        if(c.moveToFirst()){
            result=true;
            Log.d("hook_md5_exist","isHaveJsonDataByMD5");
        }
        c.close();
        return result;
    }





    public int saveJsonDataInfo(JsonData data){
        ContentValues values=new ContentValues();
        Log.v("hook_saveJson_md5",data.md5);
        values.put(JsonData.MD5, data.md5);
        values.put(JsonData.OLD_URL, data.old_url);
        Log.v("hook_saveJson_old_url",data.old_url);
        values.put(JsonData.PLATFORM_NAME, data.platform_name);
        values.put(JsonData.UPDATE_TIME,data.update_time);
        db.insert(NewSqliteCreateDB.OLD_URL_TB_NAME, null, values);
        Log.v("hook_saveJsonDataInfo","db.insert");
        return 1;
    }


    public int saveNewDataInfo(NewUrlData data){
        ContentValues values=new ContentValues();
        values.put(NewUrlData.MD5, data.md5);
        values.put(NewUrlData.OLD_URL, data.old_url);
        values.put(NewUrlData.PLATFORM_NAME, data.platform_name);
        values.put(NewUrlData.DOWNLOAD_PACKAGE_NAME, data.download_package_name);
        values.put(NewUrlData.NEW_DOWNLOAD_URL, data.new_downLoad_url);
        values.put(NewUrlData.REQUEST_TIME,data.REQUEST_TIME);
        db.insert(NewSqliteCreateDB.NEW_DATA_TB_NAME, null, values);
        return 1;
    }




    public List<JsonData> getOldUrl_TB_DataList(){
        List<JsonData> oldUrlList=new ArrayList<JsonData>();
        Cursor cursor=db.query(NewSqliteCreateDB.OLD_URL_TB_NAME, null, null,null,null,
                null, "id asc");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){  //返回游标是否指向第最后一行的位置
            JsonData jsonData=new JsonData();
            jsonData.id=cursor.getInt(cursor.getColumnIndex(JsonData.ID));
            jsonData.platform_name=cursor.getString(cursor.getColumnIndex(JsonData.PLATFORM_NAME));
            jsonData.md5=cursor.getString(cursor.getColumnIndex(JsonData.MD5));
            jsonData.update_time=cursor.getString(cursor.getColumnIndex(JsonData.UPDATE_TIME));

            jsonData.old_url=cursor.getString(cursor.getColumnIndex(JsonData.OLD_URL));
          //  Log.v("hook_show_json_url_"+i, jsonData.old_url);
            oldUrlList.add(jsonData);
            cursor.moveToNext();
        }
        cursor.close();
        return oldUrlList;
    }



    public List<NewUrlData> getNewData_TB_DataList(){
        List<NewUrlData> newUrlList=new ArrayList<NewUrlData>();
        Cursor cursor=db.query(NewSqliteCreateDB.NEW_DATA_TB_NAME, null, null,null,null,
                null, "id asc");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){  //返回游标是否指向第最后一行的位置
            NewUrlData url=new NewUrlData();
            url.id=cursor.getInt(cursor.getColumnIndex(NewUrlData.ID));
            url.platform_name=cursor.getString(cursor.getColumnIndex(NewUrlData.PLATFORM_NAME));
            url.md5=cursor.getString(cursor.getColumnIndex(NewUrlData.MD5));
            url.old_url=cursor.getString(cursor.getColumnIndex(NewUrlData.OLD_URL));
            url.download_package_name=cursor.getString(cursor.getColumnIndex(NewUrlData.DOWNLOAD_PACKAGE_NAME));
            url.request_time=cursor.getString(cursor.getColumnIndex(NewUrlData.REQUEST_TIME));
            url.new_downLoad_url=cursor.getString(cursor.getColumnIndex(NewUrlData.NEW_DOWNLOAD_URL));
            newUrlList.add(url);
            cursor.moveToNext();
        }
        cursor.close();
        return newUrlList;
    }






}
