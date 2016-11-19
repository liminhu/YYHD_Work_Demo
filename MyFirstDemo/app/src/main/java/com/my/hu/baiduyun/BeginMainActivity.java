package com.my.hu.baiduyun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;

import com.my.hu.myfirstdemo.R;
import com.my.hu.myinterface.JsonActivity;
import com.my.hu.postdata.MyNewWebViewBaseUpdate1;
import com.my.hu.postdata.MyPostActivity;

public class BeginMainActivity extends AppCompatActivity {
    private static final String BAIDU_LOGIN_URL1="http://wappass.baidu.com/passport/login?clientfrom=native&tpl=netdisk&login_share_strategy=choice&client=android&adapter=3&t=";
    private static final String BAIDU_LOGIN_URL2="&act=implicit&loginLink=0&smsLoginLink=0&lPFastRegLink=0&lPlayout=0&subpro=netdiskandroid#login";

    private static final String LOGIN_2="https://wappass.baidu.com/wp/api/login?v=";
    private static final String PARAMS_2="username=1248024149%40qq.com&code=&password=0b128adaf08f608d469f1efdacee8c1656d482084d9deafa645c16f8037d65417390e79425be058a2f9d113536ecbfe626efedeb8348a96c7b9d7df0fc75ad9ee7d0c5800509e44e46850749755fc15bf579701f13acba3946d38e4d58d952f54d6e0cce13455dab42da48fe05183f3a04949877f9ee619ea6316de037b3abe6&verifycode=&clientfrom=native&tpl=netdisk&login_share_strategy=choice&client=android&adapter=3&t=1474358798294&act=implicit&loginLink=0&smsLoginLink=1&lPFastRegLink=0&lPlayout=0&subpro=netdiskandroid&lang=zh-cn&regLink=1&action=login&loginmerge=1&isphone=0&dialogVerifyCode=&dialogVcodestr=&dialogVcodesign=&gid=9175F99-CB50-4273-B61C-BF8AD2351975&countrycode=&servertime=9c8bbdadab&logLoginType=sdk_login&passAppHash=&passAppVersion=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin_main);
    }


    public void showLoginView2(View view){
        MyNewWebViewBaseUpdate1 webview = new MyNewWebViewBaseUpdate1(getBaseContext());
        webview.initWebSetting(webview);
        BaiduNoHandleClientBase mWebViewClientBase=new BaiduNoHandleClientBase();
        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(mWebViewClientBase);
        webview.loadUrl(initLoginUrl());
        // webview.post();
        webview.postUrl(initLoginUrl2(),PARAMS_2.getBytes());
        setContentView(webview);
    }

    public void showLoginView(View view){
        MyNewWebViewBaseUpdate1 webview = new MyNewWebViewBaseUpdate1(getBaseContext());
        webview.initWebSetting(webview);
        BaiduNoHandleClientBase mWebViewClientBase=new BaiduNoHandleClientBase();
        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(mWebViewClientBase);
        webview.loadUrl(initLoginUrl());
       // webview.post();
        setContentView(webview);
    }




    public String initLoginUrl2(){
        StringBuilder sb=new StringBuilder();
        sb.append(LOGIN_2);
        String time=""+System.currentTimeMillis();
        sb.append(time);
       // sb.append(PARAMS_2);
        return sb.toString();
    }


    public String initLoginUrl(){
        StringBuilder sb=new StringBuilder();
        sb.append(BAIDU_LOGIN_URL1);
        String time=""+System.currentTimeMillis();
        sb.append(time);
        sb.append(BAIDU_LOGIN_URL2);
        return sb.toString();
    }


    public void showhighDownloadLink(View view){
        Intent intent=new Intent(BeginMainActivity.this, BaiduUrlActivity.class);
        startActivity(intent);
    }


    public void returnOtherTheme(View view){
        Intent intent=new Intent(BeginMainActivity.this, MyPostActivity.class);
        startActivity(intent);
    }
}
