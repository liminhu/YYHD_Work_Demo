package com.my.hu.activity.gen.downloadlink;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.widget.TextView;

import com.my.hu.webviewjsinjectv2.R;
import com.my.hu.webviewjsinjectv2.database.NewSqliteCreateRawLinkDB;
import com.my.hu.webviewjsinjectv2.database.NewSqliteCreateStraightChainDB;
import com.my.hu.webviewjsinjectv2.database.UpdateSqliteRawLinkDBHelper;
import com.my.hu.webviewjsinjectv2.database.UpdateSqliteStraightChainDBHelper;
import com.my.hu.webviewjsinjectv2.domain.ConstantData;
import com.my.hu.webviewjsinjectv2.domain.LinkJsonData;
import com.my.hu.webviewjsinjectv2.domain.StraightChainData;
import com.my.hu.webviewjsinjectv2.util.StringUtil;
import com.my.hu.webviewjsinjectv2.webview.Qh360WebViewClientBase;
import com.my.hu.webviewjsinjectv2.webview.WebViewBase;
import com.my.hu.webviewjsinjectv2.webview.WebViewDownLoadListener;

import java.util.ArrayList;

public class Qh360WebViewActivity extends AppCompatActivity {
    private TextView textView;
    private ArrayList<LinkJsonData> jsonArray;
    private Context context;
    private WebViewBase webView = null;
    private final static int RUN = 2;
    private LinkJsonData js;
    private int num;
    private int whole_num=0;

    private Handler handler = new Handler() {

//假若从T1Activity跳转到下一个Text2Activity，而当这个Text2Activity调用了finish()方法以后，程序会自动跳转回T1Activity，并调用前一个T1Activity中的onActivityResult( )方法。
        @Override
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case 0:
                        setResult(-1);
                        finish();
                        break;
                    case 1:
                        Bundle b=msg.getData();
                        String url=b.getString("data");
                        b.clear();
                        StraightChainData straightChainData=new StraightChainData();
                        straightChainData.link_url=url;
                        straightChainData.md5=js.md5;
                        straightChainData.platform_name=js.platform_name;
                        straightChainData.update_time= StringUtil.getDateTime();
                        straightChainData.state=0;
                        NewSqliteCreateStraightChainDB createDb=new NewSqliteCreateStraightChainDB(getBaseContext());
                        UpdateSqliteStraightChainDBHelper db=new UpdateSqliteStraightChainDBHelper(createDb);
                        db.saveUpdateStraightChainInfo(straightChainData, ConstantData.TB_QH_360_CHAIN);
                        db.closeDataBase();

                        NewSqliteCreateRawLinkDB createDb1 = new NewSqliteCreateRawLinkDB(getBaseContext());
                        UpdateSqliteRawLinkDBHelper db1 = new UpdateSqliteRawLinkDBHelper(createDb1);
                        js.state=1;
                        db1.needUpdateJsonDataInfo(js, ConstantData.TB_RAW_LINK);
                        db1.closeDataBase();

                        textView.setText("总共有条"+whole_num+"记录,正在生成下载链接:"+url+"已保存,还剩"+num+"条记录。。");
                        handler.obtainMessage(0).sendToTarget();
                    case RUN:
                        Log.d("hook_run_2", "handleMessage");
                        Log.v("hook_MyWebView_url", js.Link_URL);
                        webView = new WebViewBase(context);
                        webView=new WebViewBase(getBaseContext());
                        Qh360WebViewClientBase mWebViewClientBase = new Qh360WebViewClientBase(handler);
                        webView.setWebChromeClient(new WebChromeClient());
                        webView.setWebViewClient(mWebViewClientBase);
                        webView.setDownloadListener(new WebViewDownLoadListener(handler));
                        webView.loadUrl(js.link_url);
                        sendEmptyMessageDelayed(5, 3000);
                        break;
                    case 5:
                        setResult(0);
                        finish();
                        break;
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_result);
        Log.v("hook_Qh360_Activity", "onCreate");
        Intent intent = getIntent();
        textView = (TextView) findViewById(R.id.gen_down_load_link_result);
        js = (LinkJsonData) intent.getSerializableExtra("json");
        num=intent.getIntExtra("num",0);
        whole_num=intent.getIntExtra("whole_num",0);
        Log.i("hook_js",js.link_url);
        if (js != null) {
            NewSqliteCreateRawLinkDB createDb = new NewSqliteCreateRawLinkDB(getBaseContext());
            UpdateSqliteRawLinkDBHelper db = new UpdateSqliteRawLinkDBHelper(createDb);
            js.gen_time+=1;
            db.needUpdateJsonDataInfo(js, ConstantData.TB_RAW_LINK);
            db.closeDataBase();
            handler.obtainMessage(RUN).sendToTarget();
        }
    }


    @Override
    protected void onStop() {
        Log.v("hook_onStop", " System.exit");     //finish 完了要经历 onPause  onStop   onDestory
        System.exit(0);
        super.onStop();
    }
}


