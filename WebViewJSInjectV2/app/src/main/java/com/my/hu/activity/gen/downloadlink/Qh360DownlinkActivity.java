package com.my.hu.activity.gen.downloadlink;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.my.hu.post.activity.BaiduPostActivity;
import com.my.hu.post.invalid.activity.BaiduPostInvalidActivity;
import com.my.hu.webviewjsinjectv2.MainActivity;
import com.my.hu.webviewjsinjectv2.R;
import com.my.hu.webviewjsinjectv2.database.NewSqliteCreateRawLinkDB;
import com.my.hu.webviewjsinjectv2.database.UpdateSqliteRawLinkDBHelper;
import com.my.hu.webviewjsinjectv2.domain.ConstantData;
import com.my.hu.webviewjsinjectv2.domain.LinkJsonData;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Qh360DownlinkActivity extends AppCompatActivity {
    private TextView textView;
    private LinkedList<LinkJsonData> jsonDatas=new LinkedList<LinkJsonData>();
    private int whole_num=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("hook_Qh360_Activity","Qh360DownlinkActivity_onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_result);
        textView = (TextView) findViewById(R.id.gen_down_load_link_result);
        NewSqliteCreateRawLinkDB createDb = new NewSqliteCreateRawLinkDB(getBaseContext());
        UpdateSqliteRawLinkDBHelper db = new UpdateSqliteRawLinkDBHelper(createDb);
        List<LinkJsonData> jsonDataList = db.getLinkJsonDataListByPlatformNameAndState(ConstantData.TB_RAW_LINK, ConstantData.PLATFORM_QH_360,0);
        db.closeDataBase();
        Log.i("hook_360_size",""+jsonDataList.size());

        ArrayList<LinkJsonData> jsonArray = new ArrayList<LinkJsonData>();
        for (int i = 0; i < jsonDataList.size(); i++) {
            jsonArray.add(jsonDataList.get(i));
        }

        Log.v("hook_Qh360__size",String.valueOf(jsonArray.size()));
        if(jsonArray!=null && jsonArray.size()>0){
            for(int i=0; i<jsonArray.size(); i++){
                LinkJsonData jsonData=jsonArray.get(i);
                jsonDatas.add(jsonData);
            }
        }
        if(jsonArray.size()>0){
            LinkJsonData data1 = (LinkJsonData) jsonDatas.removeFirst();
            Intent intent=new Intent(Qh360DownlinkActivity.this,Qh360WebViewActivity.class);
            whole_num=jsonArray.size();
            Bundle b=new Bundle();
            b.putSerializable("json",data1);
            b.putInt("num",jsonArray.size());
            b.putInt("whole_num",whole_num);
            intent.putExtras(b);
            textView.setText("总共有条"+whole_num+"记录,正在生成下载链接" + jsonDatas.size() + ":" + data1.link_url + "已保存。。");
            Log.i("hook_startActivity","总共有条"+whole_num+"记录,正在生成下载链接" + jsonDatas.size() + ":" + data1.link_url + "已保存。。");
            startActivityForResult(intent,jsonDatas.size());
        }else{
            Intent intent=new Intent(Qh360DownlinkActivity.this, BaiduPostActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("hook_onBackPressed","MainActivity");
        Intent intent=new Intent(Qh360DownlinkActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("hook_Result_requestCode",String.valueOf(requestCode));
        if (jsonDatas != null&&jsonDatas.size() > 0){
            Log.i("hook_TimerTask_1",""+jsonDatas.size());
            LinkJsonData data1 = jsonDatas.removeFirst();
            Intent intent=new Intent(Qh360DownlinkActivity.this,Qh360WebViewActivity.class);
            Bundle b=new Bundle();
            b.putSerializable("json",data1);
            b.putInt("num",jsonDatas.size());
            b.putInt("whole_num",whole_num);
            intent.putExtras(b);
            textView.setText("总共有条"+whole_num+"记录,正在生成下载链接" + jsonDatas.size() + ":" + data1.link_url + "已保存。。");
            startActivityForResult(intent,jsonDatas.size());
        }else{
            Intent intent=new Intent(Qh360DownlinkActivity.this, MainActivity.class);
            intent.putExtra("data","second");
            startActivity(intent);
            finish();
        }
    }
}
