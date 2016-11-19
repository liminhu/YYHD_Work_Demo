package com.my.hu.datalist;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.my.hu.Util.SqliteDataHelper;
import com.my.hu.demain.UrlInfo;
import com.my.hu.myfirstdemo.R;
import com.my.hu.provider.Provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataListActivity extends AppCompatActivity {
    private ListView listView;
    // SqliteDataHelper sqliteDataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);
        listView=(ListView)findViewById(R.id.listView);
        Intent intent=getIntent();
        String view_page=intent.getStringExtra("view_page");
        if(view_page.equals("0")) {
            showData();
        }else{
            showProviderData();
        }
    }


    private void showProviderData(){
        List<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
        Cursor cursor=getContentResolver().query(Provider.UrlColumns.CONTENT_URI,new String[]{
            Provider.UrlColumns.ID,Provider.UrlColumns.DOWNLOAD_PACKAGE_NAME,
                Provider.UrlColumns.DOWNLOAD_URL,Provider.UrlColumns.REQUEST_TIME
        }, null,null,null);
        List<UrlInfo> urlInfos=new ArrayList<UrlInfo>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast() ){
            UrlInfo url=new UrlInfo();
            url.setId(cursor.getInt(cursor.getColumnIndex("id")));
            url.setDownload_package_name(cursor.getString(cursor.getColumnIndex("download_package_name")));
            url.setDownLoad_url(cursor.getString(cursor.getColumnIndex("downLoad_url")));
            url.setRequest_time(cursor.getString(cursor.getColumnIndex("request_time")));
            url.setPlatform_name("ty");
            urlInfos.add(url);
            cursor.moveToNext();
        }
        cursor.close();
        int i=1;
        for(UrlInfo url:urlInfos){
            HashMap<String, Object> item=new HashMap<String, Object>();
            item.put("id", url.getId());
            item.put("download_package_name", url.getDownload_package_name());
            //item.put("request_time", url.getRequest_time());
            item.put("downLoad_url",url.getDownLoad_url());
            Log.v("hook_2_package_name_"+i, url.getDownload_package_name());
            Log.d("hook_2_show_url_"+i, url.getDownLoad_url());
            i++;
            data.add(item);
        }
        SimpleAdapter adapter=new SimpleAdapter(this, data,R.layout.item, new String[]{
                "id", "download_package_name",  "downLoad_url"},   //"request_time"}, //,
                new int[]{R.id.id, R.id.download_package_name, R.id.downLoad_url});  //R.id.request_time});  //,
        listView.setAdapter(adapter);
    }




    /**
     * 展示所有的数据
     */
    private void showData(){
        List<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
        SqliteDataHelper sqliteDataHelper=new SqliteDataHelper(this);
        List<UrlInfo> urlInfoLists=sqliteDataHelper.getUrlList();
        int i=1;
        for(UrlInfo url:urlInfoLists){
            HashMap<String, Object> item=new HashMap<String, Object>();
            item.put("id", url.getId());
            item.put("download_package_name", url.getDownload_package_name());
            //item.put("request_time", url.getRequest_time());
            item.put("downLoad_url",url.getDownLoad_url());
           // Log.v("hook_show_package_name_"+i, url.getDownload_package_name());
           // Log.d("hook_show_md5_"+i, url.getMd5());
            i++;
            data.add(item);
        }
        SimpleAdapter adapter=new SimpleAdapter(this, data,R.layout.item, new String[]{
          "id", "download_package_name",  "downLoad_url"},   //"request_time"}, //,
                new int[]{R.id.id, R.id.download_package_name, R.id.downLoad_url});  //R.id.request_time});  //,
        listView.setAdapter(adapter);
    }




}
