package com.my.hu.Util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hu on 9/7/16.
 */
public class SqliteCreateDB extends SQLiteOpenHelper {
    public static final String TY_TB_NAME="ty_url_tb";
    public static final String BAIDY_TB_NAME="baidu_old_url_tb";
    private static String DB_NAME="my_download_url.db";
    private static int DB_VERSION=1;

    public SqliteCreateDB(Context context, String name, int version){
        super(context, name, null, version);
    }



    public SqliteCreateDB(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }
    //实现方法  是一个回调方法
    //在创建数据库时调用
    //什么时候创建数据库：连接数据库的时候，如果数据库文件不存在
    //只调用一次
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(" CREATE TABLE " + TY_TB_NAME + "( " +
                "[id] integer primary key autoincrement, [download_package_name] varchar(100), " +
                "[downLoad_url] varchar(100), [md5] varchar(60), [platform_name] varchar(30),[request_time] varchar(30) )" );

        sqLiteDatabase.execSQL(" CREATE TABLE " + BAIDY_TB_NAME + "( " +
                "[id] integer primary key autoincrement, [download_package_name] varchar(100), " +
                "[downLoad_url] varchar(100), [md5] varchar(60), [platform_name] varchar(30),[request_time] varchar(30) )" );

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TY_TB_NAME);
        onCreate(sqLiteDatabase);
    }

    public void updateColumn(SQLiteDatabase db, String oldColumn,
                             String newColumn, String typeColumn){
        db.execSQL("alter table "+TY_TB_NAME+" change "+oldColumn+
        " "+newColumn+" "+typeColumn);
    }

}
