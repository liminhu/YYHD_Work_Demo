package com.my.hu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;


import com.my.hu.webviewjsinjectv2.MainActivity;
import com.my.hu.webviewjsinjectv2.R;
import com.my.hu.webviewjsinjectv2.database.NewSqliteCreateRawLinkDB;
import com.my.hu.webviewjsinjectv2.database.UpdateSqliteRawLinkDBHelper;
import com.my.hu.webviewjsinjectv2.domain.ConstantData;
import com.my.hu.webviewjsinjectv2.domain.LinkJsonData;
import com.my.hu.webviewjsinjectv2.util.DataAdapter;

import java.util.List;

public class ShowRawDataActivity extends AppCompatActivity {
    private DataAdapter dataAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.raw_link_item_head);
        listView=(ListView)findViewById(R.id.listView);
        NewSqliteCreateRawLinkDB createDb=new NewSqliteCreateRawLinkDB(getBaseContext());
        UpdateSqliteRawLinkDBHelper db=new UpdateSqliteRawLinkDBHelper(createDb);
        List<LinkJsonData> jsonDatas=db.getUpateLinkJsonDataList(ConstantData.TB_RAW_LINK);
        db.closeDataBase();
        dataAdapter=new DataAdapter(this, jsonDatas, R.layout.show_data_item);
        listView.setAdapter(dataAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(ShowRawDataActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
