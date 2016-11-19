package com.my.hu.myinterface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.my.hu.Util.SqliteDataHelper;
import com.my.hu.demain.UrlInfo;
import com.my.hu.myfirstdemo.R;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowDataActivity extends AppCompatActivity {
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
        NewSqliteDBHelper dbHelper=new NewSqliteDBHelper(this);
        List<JsonData> jsonDataList=dbHelper.getOldUrl_TB_DataList();
        int i=1;
        for(JsonData json:jsonDataList){
            HashMap<String, Object> item=new HashMap<String, Object>();
            item.put(JsonData.ID, json.id);
            Log.v("hook_show_json_md5_"+i, json.md5);
            item.put(JsonData.MD5, json.md5);
            item.put(JsonData.OLD_URL, json.old_url);
            Log.v("hook_show_json_url_"+i, json.old_url);
            item.put(JsonData.PLATFORM_NAME, json.platform_name);
            i++;
            data.add(item);
        }
        SimpleAdapter adapter=new SimpleAdapter(this, data, R.layout.show_data_item, new String[]{
                "id", "md5",  "platform_name",  "old_url"},   //"request_time"}, //,
                new int[]{R.id.id, R.id.md5, R.id.platform_name, R.id.old_url});  //R.id.request_time});  //,
        listView.setAdapter(adapter);
    }

}
