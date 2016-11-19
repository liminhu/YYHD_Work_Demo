package com.my.hu.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.my.hu.webviewjsinjectv2.MainActivity;
import com.my.hu.webviewjsinjectv2.R;
import com.my.hu.webviewjsinjectv2.database.NewSqliteCreateRawLinkDB;
import com.my.hu.webviewjsinjectv2.database.UpdateSqliteRawLinkDBHelper;
import com.my.hu.webviewjsinjectv2.domain.ConstantData;
import com.my.hu.webviewjsinjectv2.domain.RawLinkHashMapData;
import com.my.hu.webviewjsinjectv2.util.DataAdapter;
import com.my.hu.webviewjsinjectv2.util.DealJsonData;
import com.my.hu.webviewjsinjectv2.util.RawLinkRequestThread;

public class RawLinkActivity extends AppCompatActivity {
    private TextView textView;
    private DataAdapter dataAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw_link);
        textView=(TextView)findViewById(R.id.show_raw_link_result);
        RawLinkRequestThread rawThread=new RawLinkRequestThread(postHandler, ConstantData.RAW_LINK_URL);
        new Thread(rawThread).start();
    }

    private Handler postHandler = new Handler(){
        public void handleMessage(Message msg) {
            Bundle b=msg.getData();
            String data=b.getString("data");
            if(msg.what==0){
                Intent intent=new Intent(RawLinkActivity.this, MainActivity.class);
              //  intent.putExtra("data","fail");
                startActivity(intent);
                finish();
            }else if(msg.what==1){
                DealJsonData dealJsonData=new DealJsonData(data);
                NewSqliteCreateRawLinkDB createDb=new NewSqliteCreateRawLinkDB(getBaseContext());
                UpdateSqliteRawLinkDBHelper db=new UpdateSqliteRawLinkDBHelper(createDb);
                int num=saveDataToDb(dealJsonData,db,ConstantData.PLATFORM_BAIDU);
                saveDataToDb(dealJsonData,db,ConstantData.PLATFORM_BAIDU,ConstantData.TB_OLD_BAIDU_LINK);
                Log.i("hook_baidu","is ok");
                num+=saveDataToDb(dealJsonData,db,ConstantData.PLATFORM_QH_360);
                num+=saveDataToDb(dealJsonData,db,ConstantData.PLATFORM_TIANYI);
                db.closeDataBase();
                String str=textView.getText().toString();
                str=str+"\n 本次更新raw link共"+num+"条!!!";
                Intent intent=new Intent(RawLinkActivity.this, MainActivity.class);
                intent.putExtra("data","first");
                startActivity(intent);
                finish();
               /* new AlertDialog.Builder(RawLinkActivity.this)
                        .setTitle("title").setMessage(str)
                        .setPositiveButton("close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent=new Intent(RawLinkActivity.this, ShowRawDataActivity.class);
                                startActivity(intent);
                            }
                        }).show();*/
            }
        };
    };


    private int saveDataToDb(DealJsonData dealJsonData,UpdateSqliteRawLinkDBHelper db,String platformName){
        RawLinkHashMapData hashMapData=dealJsonData.getRawLinkHashMapDataByPlatformName(platformName);
        if(hashMapData!=null){
            int i=dealJsonData.saveDataJsonBean(hashMapData,db,ConstantData.TB_RAW_LINK);
            if(i>0){
                String str=textView.getText().toString();
                textView.setText(str+"\n updata "+platformName+" raw link "+i+"条新记录");
                return i;
            }
        }
        return 0;
    }


    private int saveDataToDb(DealJsonData dealJsonData,UpdateSqliteRawLinkDBHelper db,String platformName,String tb_name){
        RawLinkHashMapData hashMapData=dealJsonData.getRawLinkHashMapDataByPlatformName(platformName);
        if(hashMapData!=null){
            int i=dealJsonData.saveDataJsonBean(hashMapData,db,tb_name);
            if(i>0){
                String str=textView.getText().toString();
                textView.setText(str+"\n updata "+platformName+" raw link "+i+"条新记录");
                return i;
            }
        }
        return 0;
    }

}
