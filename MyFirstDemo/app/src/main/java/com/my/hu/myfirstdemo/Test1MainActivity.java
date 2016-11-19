package com.my.hu.myfirstdemo;

import android.content.Intent;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.my.hu.datalist.DataListActivity;
import com.my.hu.myinterface.JsonActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Test1MainActivity extends AppCompatActivity {
    private static final String BAIDUYUN_URL="http://blog.csdn.net/lmj623565791/article/details/48129405";
    private static final String test1_url="http://pan.baidu.com/s/1mijOgHU";
    private static final String test2_url="http://pan.baidu.com/s/1pJoryxP";   //两个都有
    private static final String test3_url="https://yunpan.cn/cMkb4bjXRcms8";   //两个
    private static final String test4_url="http://cloud.189.cn/t/eq2mMz6Nn6zm"; //天翼

    private static final String test5_url="https://yunpan.cn/cM8Bg2mQ46PLH";
    private  static final  String test6_360_url="https://yunpan.cn/cMNwu9gX3e8St";//"https://yunpan.cn/cMNwu9gX3e8St";  //"http://4b2a74.l52.yunpan.cn/lk/cPHfuGJIkQ6aV";

    private ProgressBar progressBar;
    private TextView textView;



    //测试百度的下载
    public void LoadSo(View view){
      /*  WebView webView=new MyWebViewBase(this,test2_url,progressBar,textView);
        String data=webView.toString();
        Log.d("hook_data_len",String.valueOf(data.length()));
        Log.d("hook_data",data);
        setContentView(webView);*/


       // setContentView(R.layout.set_progressbar);
        progressBar=(ProgressBar) findViewById(R.id.bar);
        textView=(TextView)findViewById(R.id.tv);
        WebView webView=new MyWebViewBase(this, test2_url,progressBar,textView);
        setContentView(webView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop(){
        System.exit(0);  //自杀
        super.onStop();
    }

    //测试360的下载
    public void LoadSo1(View view){
/*        WebView webView=new MyWebViewBase(this, test5_url,progressBar,textView);
        String data=webView.toString();
        Log.d("hook_data_len",String.valueOf(data.length()));
        Log.d("hook_data",data);
        setContentView(webView);*/


        setContentView(R.layout.set_progressbar);
        progressBar=(ProgressBar) findViewById(R.id.bar);
        textView=(TextView)findViewById(R.id.tv);
        WebView webView=new MyWebViewBase(this, test6_360_url,progressBar,textView);
        setContentView(webView);
       // webView.destroy();
      //  finish();

    }



    //测试天翼的下载
    public void LoadSo2(View view){
        setContentView(R.layout.set_progressbar);
        progressBar=(ProgressBar) findViewById(R.id.bar);
        textView=(TextView)findViewById(R.id.tv);
        WebView webView=new MyWebViewBase(this, test4_url,progressBar,textView);
        String data=webView.toString();
        Log.d("hook_data_len",String.valueOf(data.length()));
        Log.d("hook_data",data);
        Log.d("hook_data_url",test4_url);
       // setContentView(webView);
    }



    public void LoadSo3(View view){
        Intent intent = new Intent(Test1MainActivity.this, DataListActivity.class);
        //启动活动
        intent.putExtra("view_page","0");
        startActivity(intent);
    }




    public void LoadProvider(View view){
        Intent intent = new Intent(Test1MainActivity.this, DataListActivity.class);
        intent.putExtra("view_page","1");
        //启动活动
        startActivity(intent);
    }


    public void LoadJson(View view){
        Intent intent = new Intent(Test1MainActivity.this, JsonActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1_main);
    }

    private String getHttps(){
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new MyTrustManager()}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());
            HttpsURLConnection conn = (HttpsURLConnection)new URL(BAIDUYUN_URL).openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null)
                sb.append(line);
            return sb.toString();
        }catch (Exception e){
            return  e.getMessage();
        }

    }

    private class MyHostnameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            // TODO Auto-generated method stub
            return true;
        }
    }

    private class MyTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            // TODO Auto-generated method stub

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            // TODO Auto-generated method stub

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }
    }

}
