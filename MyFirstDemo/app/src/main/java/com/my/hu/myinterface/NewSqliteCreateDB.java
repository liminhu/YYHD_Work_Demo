package com.my.hu.myinterface;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.my.hu.Util.SqliteDataHelper;

/**
 * Created by hu on 9/9/16.
 */
public class NewSqliteCreateDB extends SQLiteOpenHelper{
    public static final String OLD_URL_TB_NAME="old_url_tb";
    public static final String NEW_DATA_TB_NAME="new_data_tb";
    public static final String DB_NAME="new_url.db";
    private static int DB_VERSION=1;

    public NewSqliteCreateDB(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.v("hook_db","crtate_db");
        sqLiteDatabase.execSQL(" CREATE TABLE " + OLD_URL_TB_NAME + "( [" +
                JsonData.ID+"] integer primary key autoincrement, ["+JsonData.PLATFORM_NAME+"] varchar(30), " +
                "["+JsonData.MD5+"] varchar(60), ["+JsonData.OLD_URL+"] varchar(50), ["+JsonData.UPDATE_TIME+"] varchar(30) )" );



        sqLiteDatabase.execSQL(" CREATE TABLE " + NEW_DATA_TB_NAME + "( " +
                "["+NewUrlData.ID+"] integer primary key autoincrement, ["+NewUrlData.DOWNLOAD_PACKAGE_NAME+"] varchar(100), " +
                "["+NewUrlData.NEW_DOWNLOAD_URL+"] varchar(150), ["+NewUrlData.PLATFORM_NAME+"] varchar(30), " +
                "["+NewUrlData.MD5+"] varchar(60), ["+NewUrlData.OLD_URL+"] varchar(50),["+NewUrlData.REQUEST_TIME+"] varchar(30) )" );
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.v("hook_db","onUpgrade_db");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+OLD_URL_TB_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+NEW_DATA_TB_NAME);
        onCreate(sqLiteDatabase);
    }


}
