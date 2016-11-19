package com.my.hu.postdata;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.my.hu.myfirstdemo.R;
import com.my.hu.myinterface.JsonData;

import java.util.ArrayList;
import java.util.LinkedList;

public class MyNewUrlActivity extends AppCompatActivity {
    LinkedList<JsonData> jsonDatas=new LinkedList<JsonData>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_url);
        Log.v("hook_MyNewUrlActivity","onCreate");
        Intent intent1=getIntent();
        ArrayList<JsonData> jsonArray=(ArrayList<JsonData>)intent1.getSerializableExtra("jsonArray");
        Log.v("hook_MyNewUrl_size",String.valueOf(jsonArray.size()));
        if(jsonArray!=null && jsonArray.size()>0){
            for(int i=0; i<jsonArray.size(); i++){
                JsonData jsonData=jsonArray.get(i);
                Log.v("hook_data",jsonData.old_url);
                jsonDatas.add(jsonData);
            }
        }
        JsonData data1 = (JsonData) jsonDatas.removeFirst();
        Intent intent=new Intent(MyNewUrlActivity.this,MyWebViewActivity.class);
        Bundle b=new Bundle();
        b.putSerializable("json",data1);
        Log.v("hook_data1",data1.old_url);
        intent.putExtras(b);
        startActivityForResult(intent,jsonDatas.size());
       // startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("hook_Result_requestCode",String.valueOf(requestCode));
        if (jsonDatas != null&&jsonDatas.size() > 0){
            JsonData data1 = jsonDatas.removeFirst();
            Intent intent=new Intent(MyNewUrlActivity.this,MyWebViewActivity.class);
         //   intent.putExtra("json", data1);
            Bundle b=new Bundle();
            b.putSerializable("json",data1);
            intent.putExtras(b);
            startActivityForResult(intent,jsonDatas.size());
        }
    }

}
