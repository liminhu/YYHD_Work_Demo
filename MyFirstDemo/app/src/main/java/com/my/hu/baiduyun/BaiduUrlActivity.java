package com.my.hu.baiduyun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ListView;

import com.my.hu.datalist.ItemClickListener;
import com.my.hu.demain.UrlInfo;
import com.my.hu.myfirstdemo.R;
import com.my.hu.myinterface.JsonData;
import com.my.hu.myinterface.NewSqliteDBHelper;

import java.util.ArrayList;
import java.util.List;

public class BaiduUrlActivity extends AppCompatActivity {
    private ListView listView;
    private DataAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidu_url);
        listView=(ListView)findViewById(R.id.listView);
        //List<JsonData> jsonDatas=CommonMethod.getAllJsonDataByPlatName(getBaseContext(),CommonMethod.BAIDU_PLATFORM);
        //Log.v("hook_onCreate",R.id.listView+"--"+jsonDatas.size()+"\t--"+listView.getId());
        List<UrlInfo> urlInfos=CommonMethod.getOldUrlBaiduData(getBaseContext());
        List<JsonData> jsonDatas=CommonMethod.urlInfoToJsonData(urlInfos);
        dataAdapter=new DataAdapter(this, jsonDatas, R.layout.show_data_item);
        //listView.setOnItemClickListener(new ItemClickListener());
        listView.setAdapter(dataAdapter);
    }

}
