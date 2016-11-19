package com.my.hu.activity.gen.downloadlink;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
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
import com.my.hu.webviewjsinjectv2.webview.BaiduTwoWebViewClientBase;
import com.my.hu.webviewjsinjectv2.webview.BaiduWebViewClientBase;
import com.my.hu.webviewjsinjectv2.webview.WebViewBase;
import com.my.hu.webviewjsinjectv2.webview.WebViewDownLoadListener;

import java.util.List;

public class BaiduTwoDownloadLinkActivity extends AppCompatActivity {
    private List<LinkJsonData> jsonDataList;
    private LinkJsonData js;
    private TextView textView;
    private WebView webView;
    private int num = 0;
    private int whole_num=0;

    private static int flag=0;
    private int fail_time=0;
    private boolean while_flag=true;

    //Thread.currentThread().isInterrupted()
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Log.i("hook_handle",""+jsonDataList.size());
                if (jsonDataList.size() > 0 && num < whole_num) {
                    js = jsonDataList.remove(0);
                    num++;
                    setFlagAdd();

                    NewSqliteCreateRawLinkDB createDb = new NewSqliteCreateRawLinkDB(getBaseContext());
                    UpdateSqliteRawLinkDBHelper db = new UpdateSqliteRawLinkDBHelper(createDb);
                    js.gen_time+=1;
                    db.needUpdateJsonDataInfo(js, ConstantData.TB_OLD_BAIDU_LINK);
                    db.closeDataBase();

                    if(js.gen_time<=1){
                        webView.loadUrl(js.link_url);
                    }
                }else {
                    handler.obtainMessage(2).sendToTarget();
                }
            } else if (msg.what == 1) {
                fail_time=0;
                setFlagSubtract();
                Log.i("hook_num",""+num);
                Log.i("hook_msg_1","BaiduTwoDownloadLinkActivity");
                Bundle b = msg.getData();
                String url = b.getString("data");
                b.clear();
                StraightChainData straightChainData = new StraightChainData();
                straightChainData.link_url = url;
                straightChainData.md5 = js.md5;
                straightChainData.platform_name = ConstantData.PLATFORM_OLD_BAIDU;
                straightChainData.update_time = StringUtil.getDateTime();
                straightChainData.state = 1;
                NewSqliteCreateStraightChainDB createDb = new NewSqliteCreateStraightChainDB(getBaseContext());
                UpdateSqliteStraightChainDBHelper db = new UpdateSqliteStraightChainDBHelper(createDb);
                num += db.saveUpdateStraightChainInfo(straightChainData, ConstantData.TB_BAIDU_CHAIN);
                db.closeDataBase();

                NewSqliteCreateRawLinkDB createDb1 = new NewSqliteCreateRawLinkDB(getBaseContext());
                UpdateSqliteRawLinkDBHelper db1 = new UpdateSqliteRawLinkDBHelper(createDb1);
                js.state=1;
                db1.needUpdateJsonDataInfo(js, ConstantData.TB_OLD_BAIDU_LINK);
                db1.closeDataBase();

                textView.setText("总共有条"+whole_num+"记录,正在生成下载链接" + num + ":" + url + "已保存。。");
                if (jsonDataList.size() > 0) {
                    handler.obtainMessage(0).sendToTarget();
                }else{
                    handler.obtainMessage(2).sendToTarget();
                }
            }else if(msg.what==2){
                //暂时转main
                Intent intent=new Intent(BaiduTwoDownloadLinkActivity.this, Qh360DownlinkActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };


    @Override
    protected void onStop() {
        Log.i("hook_onstop","exit");
        super.onStop();
        while_flag=false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CheckTime checkTime=new CheckTime();
        new Thread(checkTime).start();   //activity退出进程不销毁，得手动操作
        setContentView(R.layout.show_result);
        textView = (TextView) findViewById(R.id.gen_down_load_link_result);
        Log.d("hook_onCreate","BaiduTwoDownloadLinkActivity");
        NewSqliteCreateRawLinkDB createDb = new NewSqliteCreateRawLinkDB(getBaseContext());
        UpdateSqliteRawLinkDBHelper db = new UpdateSqliteRawLinkDBHelper(createDb);
        jsonDataList = db.getLinkJsonDataListByPlatformNameAndState(ConstantData.TB_OLD_BAIDU_LINK, ConstantData.PLATFORM_BAIDU,0);
        db.closeDataBase();
        webView = new WebViewBase(getBaseContext(),handler);
        BaiduTwoWebViewClientBase mWebViewClientBase = new BaiduTwoWebViewClientBase(handler);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(mWebViewClientBase);
        webView.setDownloadListener(new WebViewDownLoadListener(handler));
        Log.i("hook_size",jsonDataList.size()+"");
        if (jsonDataList.size() > 0) {
            whole_num=jsonDataList.size();
            handler.obtainMessage(0).sendToTarget();
        }else{
            handler.obtainMessage(2).sendToTarget();
        }
    }



    static synchronized void setFlagAdd() {
        flag++;
        Log.i("hook_add",""+flag);
    }
    static synchronized void setFlagSubtract() {
        flag--;
        Log.i("hook_setFlagSubtract",""+flag);
    }

    class CheckTime implements Runnable{
        @Override
        public void run() {
            while(while_flag){
                try{
                    Thread.sleep(3000);
                }catch (Exception e){
                    Log.i("hook_sleep","sleep");
                }
                if(flag>0){
                    Log.i("hook_checktime","run");
                    if(fail_time>5){
                        handler.obtainMessage(2).sendToTarget();  //连续失败5次
                    }else{
                        Log.i("hook_fail_time",""+fail_time);
                        fail_time++;
                        handler.obtainMessage(0).sendToTarget();
                    }
                }
            }
        }
    }

}
