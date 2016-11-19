package com.my.hu.post.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.my.hu.webviewjsinjectv2.MainActivity;
import com.my.hu.webviewjsinjectv2.R;
import com.my.hu.webviewjsinjectv2.database.NewSqliteCreateStraightChainDB;
import com.my.hu.webviewjsinjectv2.database.UpdateSqliteStraightChainDBHelper;
import com.my.hu.webviewjsinjectv2.domain.ConstantData;
import com.my.hu.webviewjsinjectv2.domain.StraightChainData;
import com.my.hu.webviewjsinjectv2.util.DownLoadUrlPostThread;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class BaiduPostActivity extends AppCompatActivity {
    private TextView textView;
    private List<StraightChainData> jsonDatas;
    private int num=0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Bundle b=msg.getData();
                String data=b.getString("data");
                String url=b.getString("url");
                textView.setText(data+"\nurl:"+url);
                Log.d("hook_handleMessage",data);
                num++;
                if(num==jsonDatas.size()){
                    handler.sendEmptyMessage(0);
                }
            }else if(msg.what == 0){
                Intent intent=new Intent(BaiduPostActivity.this, QH360PostActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_result);
        textView = (TextView) findViewById(R.id.gen_down_load_link_result);
        Log.i("hook_baidu","BaiduPostActivity");
        NewSqliteCreateStraightChainDB createDb = new NewSqliteCreateStraightChainDB(getBaseContext());
        UpdateSqliteStraightChainDBHelper db = new UpdateSqliteStraightChainDBHelper(createDb);
        jsonDatas = db.getStraightChainDataListByState(ConstantData.TB_BAIDU_CHAIN,0);
        Map<String, String> map = null;
        for (int i = 0; i < jsonDatas.size(); i++) {
            StraightChainData urlInfo = jsonDatas.get(i);
            map = new LinkedHashMap<String, String>();
            map.put("key", urlInfo.md5);
            map.put("intent_url", urlInfo.link_url);
            Log.i("hook_map", urlInfo.link_url);
            DownLoadUrlPostThread postThread = new DownLoadUrlPostThread(map, handler,urlInfo.link_url);
            new Thread(postThread).start();
            urlInfo.state=1;
            db.needUpdateStraightChainDataInfo(urlInfo,ConstantData.TB_BAIDU_CHAIN);
        }
        db.closeDataBase();
        if(jsonDatas.size()==0){
            handler.sendEmptyMessage(0);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(BaiduPostActivity.this, MainActivity.class);
        startActivity(intent);
    }
}

