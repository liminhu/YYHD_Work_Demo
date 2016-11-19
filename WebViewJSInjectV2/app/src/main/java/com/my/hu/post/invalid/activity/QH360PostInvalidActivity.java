package com.my.hu.post.invalid.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.my.hu.webviewjsinjectv2.MainActivity;
import com.my.hu.webviewjsinjectv2.R;
import com.my.hu.webviewjsinjectv2.TimerTaskMainActivity;
import com.my.hu.webviewjsinjectv2.database.NewSqliteCreateRawLinkDB;
import com.my.hu.webviewjsinjectv2.database.UpdateSqliteRawLinkDBHelper;
import com.my.hu.webviewjsinjectv2.domain.ConstantData;
import com.my.hu.webviewjsinjectv2.domain.LinkJsonData;
import com.my.hu.webviewjsinjectv2.util.DownLoadUrlPostThread;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class QH360PostInvalidActivity extends AppCompatActivity {
    private TextView textView;
    private List<LinkJsonData> jsonDatas;
    private int num=0;
    private int whole_num=0;

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
                if(num>0 && num==whole_num){
                    handler.sendEmptyMessage(0);
                }
            }else if(msg.what == 0){
                Log.i("hook_360","onCreate_QH360PostInvalidActivity_finish");
                Intent intent=new Intent(QH360PostInvalidActivity.this, MainActivity.class);
                intent.putExtra("data","finish");
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
        Log.i("hook_360","onCreate_QH360PostInvalidActivity");
        NewSqliteCreateRawLinkDB createDb = new NewSqliteCreateRawLinkDB(getBaseContext());
        UpdateSqliteRawLinkDBHelper db = new UpdateSqliteRawLinkDBHelper(createDb);
        jsonDatas = db.getLinkJsonDataListByPlatformNameAndState(ConstantData.TB_RAW_LINK,ConstantData.PLATFORM_QH_360,0);
        Map<String, String> map = null;
        for (int i = 0; i < jsonDatas.size(); i++) {
            LinkJsonData jsonData = jsonDatas.get(i);
            Log.i("hook_360_gen_time",""+jsonData.gen_time);
            if(jsonData.gen_time>3){
                whole_num++;
                map = new LinkedHashMap<String, String>();
                map.put("key", jsonData.md5);
                //map.put("url", jsonData.link_url);
                map.put("is_expire", "1");
                Log.i("hook_map", jsonData.link_url);
                DownLoadUrlPostThread postThread = new DownLoadUrlPostThread(map, handler,jsonData.link_url);
                new Thread(postThread).start();
                jsonData.state=1;
                db.needUpdateJsonDataInfo(jsonData,ConstantData.TB_RAW_LINK);
            }

        }
        db.closeDataBase();
        if(whole_num==0){
            handler.sendEmptyMessage(0);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(QH360PostInvalidActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
