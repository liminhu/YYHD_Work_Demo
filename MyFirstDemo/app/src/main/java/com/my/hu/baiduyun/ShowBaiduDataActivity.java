package com.my.hu.baiduyun;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.my.hu.Util.SqliteDataHelper;
import com.my.hu.demain.UrlInfo;
import com.my.hu.myfirstdemo.R;
import com.my.hu.myinterface.JsonData;
import com.my.hu.myinterface.NewSqliteDBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowBaiduDataActivity extends AppCompatActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        listView=(ListView)findViewById(R.id.listView);
        showData();
    }



    /**
     * 展示所有的数据
     */
    private void showData(){
        List<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
        SqliteDataHelper dbHelper=new SqliteDataHelper(this);
        List<UrlInfo> jsonDataList=dbHelper.getBaiduUrlList();
        int i=1;
        for(UrlInfo json:jsonDataList){
            HashMap<String, Object> item=new HashMap<String, Object>();
            item.put(JsonData.ID, json.getId());
            Log.v("hook_show_json_md5_"+i, json.getMd5());
            item.put(JsonData.MD5, json.getMd5());
            item.put(JsonData.OLD_URL, json.getDownLoad_url());
            Log.v("hook_show_json_url_"+i, json.getDownLoad_url());
            item.put(JsonData.PLATFORM_NAME, json.getPlatform_name());
            i++;
            data.add(item);
        }
        SimpleAdapter adapter=new SimpleAdapter(this, data, R.layout.show_data_item, new String[]{
                "id", "md5",  "platform_name",  "old_url"},   //"request_time"}, //,
                new int[]{R.id.id, R.id.md5, R.id.platform_name, R.id.old_url});  //R.id.request_time});  //,
        listView.setAdapter(adapter);
    }

}
