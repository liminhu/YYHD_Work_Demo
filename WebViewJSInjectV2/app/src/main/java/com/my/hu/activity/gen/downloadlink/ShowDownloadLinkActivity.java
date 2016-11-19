package com.my.hu.activity.gen.downloadlink;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.my.hu.webviewjsinjectv2.MainActivity;
import com.my.hu.webviewjsinjectv2.R;
import com.my.hu.webviewjsinjectv2.database.NewSqliteCreateStraightChainDB;
import com.my.hu.webviewjsinjectv2.database.UpdateSqliteStraightChainDBHelper;
import com.my.hu.webviewjsinjectv2.domain.ConstantData;
import com.my.hu.webviewjsinjectv2.domain.LinkJsonData;
import com.my.hu.webviewjsinjectv2.domain.StraightChainData;
import com.my.hu.webviewjsinjectv2.util.DataAdapter;

import java.util.List;

public class ShowDownloadLinkActivity extends AppCompatActivity {
    private ListView listView;
    private DataAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_download_link);
    }





    public void showTianyi(View view){
        setContentView(R.layout.raw_link_item_head);
        listView=(ListView)findViewById(R.id.listView);
        NewSqliteCreateStraightChainDB createDb=new NewSqliteCreateStraightChainDB(getBaseContext());
        UpdateSqliteStraightChainDBHelper db=new UpdateSqliteStraightChainDBHelper(createDb);
        List<StraightChainData> jsonDatas=db.getStraightChainDataList(ConstantData.TB_TIANYI_CHAIN);
        db.closeDataBase();
        Log.i("hook_showTianyi",""+jsonDatas.size());
        List<LinkJsonData> jsonDataList=DataAdapter.straightChainToLinkJsonData(jsonDatas);
        dataAdapter=new DataAdapter(this, jsonDataList, R.layout.show_data_item);
        listView.setAdapter(dataAdapter);
    }

    public void showQh360(View view){
        setContentView(R.layout.raw_link_item_head);
        listView=(ListView)findViewById(R.id.listView);
        NewSqliteCreateStraightChainDB createDb=new NewSqliteCreateStraightChainDB(getBaseContext());
        UpdateSqliteStraightChainDBHelper db=new UpdateSqliteStraightChainDBHelper(createDb);
        List<StraightChainData> jsonDatas=db.getStraightChainDataList(ConstantData.TB_QH_360_CHAIN);
        db.closeDataBase();
        Log.i("hook_showTianyi",""+jsonDatas.size());
        List<LinkJsonData> jsonDataList=DataAdapter.straightChainToLinkJsonData(jsonDatas);
        dataAdapter=new DataAdapter(this, jsonDataList, R.layout.show_data_item);
        listView.setAdapter(dataAdapter);
    }
    public void showBaidu(View view){
        setContentView(R.layout.raw_link_item_head);
        listView=(ListView)findViewById(R.id.listView);
        NewSqliteCreateStraightChainDB createDb=new NewSqliteCreateStraightChainDB(getBaseContext());
        UpdateSqliteStraightChainDBHelper db=new UpdateSqliteStraightChainDBHelper(createDb);
        List<StraightChainData> jsonDatas=db.getStraightChainDataList(ConstantData.TB_BAIDU_CHAIN);
        db.closeDataBase();
        Log.i("hook_showTianyi",""+jsonDatas.size());
        List<LinkJsonData> jsonDataList=DataAdapter.straightChainToLinkJsonData(jsonDatas);
        dataAdapter=new DataAdapter(this, jsonDataList, R.layout.show_data_item);
        listView.setAdapter(dataAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(ShowDownloadLinkActivity.this, MainActivity.class);
        intent.putExtra("data","return");
        startActivity(intent);
    }
}
