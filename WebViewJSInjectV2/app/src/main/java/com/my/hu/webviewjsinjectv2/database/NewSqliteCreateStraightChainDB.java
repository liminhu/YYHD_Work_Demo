package com.my.hu.webviewjsinjectv2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.my.hu.webviewjsinjectv2.domain.ConstantData;
import com.my.hu.webviewjsinjectv2.domain.StraightChainData;

/**
 * Created by hu on 9/9/16.
 */
public class NewSqliteCreateStraightChainDB extends SQLiteOpenHelper{
    public NewSqliteCreateStraightChainDB(Context context){
        super(context, ConstantData.DB_SUBMIT_DATA_NAME, null, ConstantData.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.v("hook_db","crtate_db");
        sqLiteDatabase.execSQL(" CREATE TABLE " + ConstantData.TB_BAIDU_CHAIN + "( [" +
                StraightChainData.ID+"] integer primary key autoincrement, ["+ StraightChainData.PLATFORM_NAME+"] varchar(30), " +
                "["+ StraightChainData.MD5+"] varchar(60), ["+ StraightChainData.STATE +"] integer, ["+ StraightChainData.Link_URL +"] varchar(250), ["+ StraightChainData.UPDATE_TIME+"] varchar(30) )" );

        sqLiteDatabase.execSQL(" CREATE TABLE " + ConstantData.TB_QH_360_CHAIN + "( [" +
                StraightChainData.ID+"] integer primary key autoincrement, ["+ StraightChainData.PLATFORM_NAME+"] varchar(30), " +
                "["+ StraightChainData.MD5+"] varchar(60), ["+ StraightChainData.STATE +"] integer, ["+ StraightChainData.Link_URL +"] varchar(250), ["+ StraightChainData.UPDATE_TIME+"] varchar(30) )" );


        sqLiteDatabase.execSQL(" CREATE TABLE " + ConstantData.TB_TIANYI_CHAIN + "( [" +
                StraightChainData.ID+"] integer primary key autoincrement, ["+ StraightChainData.PLATFORM_NAME+"] varchar(30), " +
                "["+ StraightChainData.MD5+"] varchar(60), ["+ StraightChainData.STATE +"] integer, ["+ StraightChainData.Link_URL +"] varchar(250), ["+ StraightChainData.UPDATE_TIME+"] varchar(30) )" );

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.v("hook_db","onUpgrade_db");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ConstantData.TB_BAIDU_CHAIN);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ConstantData.TB_QH_360_CHAIN);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ConstantData.TB_TIANYI_CHAIN);
        onCreate(sqLiteDatabase);
    }


}
