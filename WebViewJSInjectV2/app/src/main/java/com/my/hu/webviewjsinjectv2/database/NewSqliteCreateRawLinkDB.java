package com.my.hu.webviewjsinjectv2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.my.hu.webviewjsinjectv2.domain.ConstantData;
import com.my.hu.webviewjsinjectv2.domain.LinkJsonData;

import java.io.File;

/**
 * Created by hu on 9/9/16.
 */
public class NewSqliteCreateRawLinkDB extends SQLiteOpenHelper{
    public NewSqliteCreateRawLinkDB(Context context){
        super(context, ConstantData.DB_RAW_LINK_NAME, null,ConstantData.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.v("hook_db","crtate_db");
        sqLiteDatabase.execSQL(" CREATE TABLE " + ConstantData.TB_RAW_LINK + "( [" +
                LinkJsonData.ID+"] integer primary key autoincrement, ["+ LinkJsonData.PLATFORM_NAME+"] varchar(30), " +
                "["+ LinkJsonData.MD5+"] varchar(60), ["+ LinkJsonData.STATE +"] integer, ["+ LinkJsonData.GEN_TIME +"] integer,"+
                "["+ LinkJsonData.Link_URL +"] varchar(250), ["+ LinkJsonData.UPDATE_TIME+"] varchar(30) )" );

        sqLiteDatabase.execSQL(" CREATE TABLE " + ConstantData.TB_OLD_BAIDU_LINK + "( [" +
                LinkJsonData.ID+"] integer primary key autoincrement, ["+ LinkJsonData.PLATFORM_NAME+"] varchar(30), " +
                "["+ LinkJsonData.MD5+"] varchar(60), ["+ LinkJsonData.STATE +"] integer, ["+ LinkJsonData.GEN_TIME +"] integer,"+
                "["+ LinkJsonData.Link_URL +"] text, ["+ LinkJsonData.UPDATE_TIME+"] varchar(30) )" );

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.v("hook_db","onUpgrade_db");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ConstantData.TB_RAW_LINK);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ConstantData.TB_OLD_BAIDU_LINK);
        onCreate(sqLiteDatabase);
    }




}
