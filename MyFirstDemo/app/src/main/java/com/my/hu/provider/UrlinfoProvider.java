package com.my.hu.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.CancellationSignal;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;
import android.util.Log;

import com.my.hu.Util.SqliteCreateDB;
import com.my.hu.Util.SqliteDataHelper;
import com.my.hu.demain.UrlInfo;

import java.util.HashMap;

/**
 * Created by hu on 9/9/16.
 */
public class UrlinfoProvider extends ContentProvider {
    private static HashMap<String,String> urlinfoProjectMap;
    private static final int URLINFOS=1;
    private static final int URLINFO_ID=2;
    private static final UriMatcher mUriMatcher;
    private SqliteCreateDB mSqliteDataHelper;

    @Override
    public boolean onCreate() {
        mSqliteDataHelper=new SqliteCreateDB(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder dbBuilder=new SQLiteQueryBuilder();
        dbBuilder.setTables(Provider.UrlColumns.TABLE_NAME);
        switch (mUriMatcher.match(uri)){
            case URLINFOS:
                dbBuilder.setProjectionMap(urlinfoProjectMap);
                break;
            case URLINFO_ID:
                dbBuilder.setProjectionMap(urlinfoProjectMap);
                dbBuilder.appendWhere(Provider.UrlColumns.ID +"="
                +uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknow URI "+uri);
        }
        String orderBy;
        if(TextUtils.isEmpty(sortOrder)){
            orderBy=Provider.UrlColumns.DEFAULT_SORT_ORDER;
        }else{
            orderBy=sortOrder;
        }
        SQLiteDatabase db=mSqliteDataHelper.getReadableDatabase();
        Cursor cr=dbBuilder.query(db, projection, selection, selectionArgs, null,null,orderBy);
        cr.setNotificationUri(getContext().getContentResolver(), uri);
        return cr;
    }


    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        if(mUriMatcher.match(uri)!=URLINFOS){
            throw new IllegalArgumentException("Unknow URI "+uri);
        }
        ContentValues values;
        if(contentValues != null){
            values=new ContentValues(contentValues);
        }else{
            values=new ContentValues();
        }

        if(values.containsKey(Provider.UrlColumns.PLATFORM_NAME)==false){
            values.put(Provider.UrlColumns.PLATFORM_NAME, "");
        }
        if(values.containsKey(Provider.UrlColumns.DOWNLOAD_PACKAGE_NAME)==false){
            values.put(Provider.UrlColumns.DOWNLOAD_PACKAGE_NAME, "");
        }
        if(values.containsKey(Provider.UrlColumns.DOWNLOAD_URL)==false){
            values.put(Provider.UrlColumns.DOWNLOAD_URL, "");
        }
        if(values.containsKey(Provider.UrlColumns.REQUEST_TIME)==false){
            values.put(Provider.UrlColumns.REQUEST_TIME,"");
        }
        SQLiteDatabase db=mSqliteDataHelper.getWritableDatabase();
        long rowId=db.insert(Provider.UrlColumns.TABLE_NAME, Provider.UrlColumns.DOWNLOAD_PACKAGE_NAME,values);
        if(rowId>0){
            Uri noteUri= ContentUris.withAppendedId(Provider.UrlColumns.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(noteUri,null);
            return noteUri;
        }
        Log.e("hook_inset_uri","SQL Failed to insert row into "+uri);
        return null;
    }


    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db=mSqliteDataHelper.getWritableDatabase();
        int count;
        switch (mUriMatcher.match(uri)){
            case URLINFOS:
                count=db.delete(Provider.UrlColumns.TABLE_NAME, where, whereArgs);
                break;
            case URLINFO_ID:
                String noteId=uri.getPathSegments().get(1);
                count=db.delete(Provider.UrlColumns.TABLE_NAME, Provider.UrlColumns.ID+"="+
                noteId+(!TextUtils.isEmpty(where) ? " AND ("+where+')' : ""),whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknow URI"+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }


    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    static {
        mUriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(Provider.AUTHORITY,"urlinfos",URLINFOS);
        mUriMatcher.addURI(Provider.AUTHORITY,"urlinfos/#", URLINFO_ID);
        urlinfoProjectMap=new HashMap<String,String>();
        urlinfoProjectMap.put(Provider.UrlColumns.ID,Provider.UrlColumns.ID);
        urlinfoProjectMap.put(Provider.UrlColumns.PLATFORM_NAME,Provider.UrlColumns.PLATFORM_NAME);
        urlinfoProjectMap.put(Provider.UrlColumns.DOWNLOAD_PACKAGE_NAME,Provider.UrlColumns.DOWNLOAD_PACKAGE_NAME);
        urlinfoProjectMap.put(Provider.UrlColumns.DOWNLOAD_URL,Provider.UrlColumns.DOWNLOAD_URL);
        urlinfoProjectMap.put(Provider.UrlColumns.REQUEST_TIME,Provider.UrlColumns.REQUEST_TIME);
    }
}
