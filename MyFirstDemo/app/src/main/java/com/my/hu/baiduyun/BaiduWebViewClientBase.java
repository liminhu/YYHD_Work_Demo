package com.my.hu.baiduyun;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.my.hu.Util.SqliteDataHelper;
import com.my.hu.Util.StringUtil;
import com.my.hu.demain.MyProgressDialog;
import com.my.hu.demain.UrlInfo;

import java.util.Date;

/**
 * Created by hu on 8/30/16.
 */
public class BaiduWebViewClientBase extends WebViewClient {
    private MyProgressDialog progressDialog;
    private String md5;
    private Handler handler;
    private Context context;

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public BaiduWebViewClientBase(Context context,Handler handler){
        super();
        this.handler=handler;
        this.context=context;
    }
    public BaiduWebViewClientBase(MyProgressDialog progressDialog){
        this.progressDialog=progressDialog;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        super.shouldOverrideUrlLoading(view, url);
        if(!url.equals("app360://yunpan_run")){
            view.loadUrl(url);
        }
        return true;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if(url.equals("app360://yunpan_run")){
            return;
        }
     //   Log.d("hook_url_onPageStarted", url);
        super.onPageStarted(view, url, favicon);
       // progressDialog.showProgressDialog();

    }

    @Override
    public void onPageFinished(WebView view, String url) {
        // TODO Auto-generated method stub
      //  view.loadUrl("javascript:window.java_obj.getSource('<head>'+" +
      //  "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
        Log.e("hook_url_onPageFinished", url);
        addButtonClickListner(view,url);
        super.onPageFinished(view, url);
    }




    @Override
    public void onReceivedError(WebView view, int errorCode,
                                String description, String failingUrl) {
        // TODO Auto-generated method stub
        super.onReceivedError(view, errorCode, description, failingUrl);
        Log.e("hook_url_rceivedError", failingUrl);
        Message msg=Message.obtain();
        msg.what=2;
        UrlInfo urlInfo=new UrlInfo();
        urlInfo.setMd5(md5);
        urlInfo.setDownLoad_url(failingUrl);
        String data=StringUtil.myUrlDecodedata(failingUrl);
        int beginIndex=data.lastIndexOf('=');
        String fileName=data.substring(1+beginIndex,data.length());
        Log.d("hook_fileNmae",fileName);
        urlInfo.setDownload_package_name(fileName);
        urlInfo.setRequest_time(new Date());
        urlInfo.setPlatform_name("baidu");
        SqliteDataHelper dbHelper=new SqliteDataHelper(context);
        dbHelper.saveBaiduUrlInfo(urlInfo);
        dbHelper.closeDB();
        handler.sendMessage(msg);
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url,
                                       boolean isReload) {
        // TODO Auto-generated method stub
        super.doUpdateVisitedHistory(view, url, isReload);
    }



    //注入js函数监听
    private void addButtonClickListner(WebView view, String url){
        Log.e("hook_ButtonClickListner", "-------------->"+url);
        if(url.contains("pan.baidu.com")) {
            String downLoadJS="objs[0].click();";
            String highDownload="obj.click();";
            view.loadUrl("javascript: var obj = document.getElementById('highDownload'); var objs =document.getElementsByClassName('btn normal-download'); "
                    + " if(obj != null){"+highDownload+
                    " }else{ "+downLoadJS+"}");
        }else if(url.contains("yunpan")){
            Log.v("hook_360_yunpan","-------------->"+ url+"\ntime:"+ System.currentTimeMillis());
            Log.v("hook_time", StringUtil.getDateTime());
            String js="  html=html+'<script type=\"text/javascript\">var downloadBtn =document.getElementById(\"aDownload\"); " +
                    "   var highSpeed =document.getElementById(\"highSpeed\"); " +
                    "    if(downloadBtn != null){downloadBtn.onclick=function(){window.alert(\"欢迎360普通下载！请按“确定”继续。\");};  " +
                    "    }else if(highSpeed != null){ highSpeed.onclick=function(){ window.alert(\"欢迎360高速下载！请按“确定”继续。\");  }; " +
                    "   }</script>';" +
                    "   document.getElementById('singleViewTpl').innerHTML = html; var html1 = document.getElementById('singleViewTpl').innerHTML; window.alert(html1); ";

            String downloadBtn="var singleViewTpl = document.getElementById('singleViewTpl'); var downloadBtn =document.getElementById(\"downloadBtn\");  ";
            String highSpeed ="var highSpeed =document.getElementById(\"highSpeed\");  var html = document.getElementById('singleViewTpl').innerHTML; ";
            String finaljs="javascript: function func(){"+downloadBtn+"  "+highSpeed
                    + " if(1==1){ if(downloadBtn != null){   downloadBtn.click(); "+       //window.alert("downloadBtn is not null!");  download_link.click();
                    " }else if(highSpeed != null){ window.alert(\"highSpeed is click!\");  }else{ window.alert(\"downloadBtn is null!\"); var download_link=document.getElementById(\"formDownload\");  window.location.Reload();   } " +  //window.location.Reload();   if(download_link != null){window.alert('download_link is not null');}else{"+js+"}
                    "}else{ window.alert(\"singleViewTpl is null!\");} } window.onload=func;";
            Log.v("hook_js",finaljs);
            view.loadUrl(finaljs);
         /*   view.loadUrl("javascript: function func(){"+downloadBtn+"  "+highSpeed
                    + " if(singleViewTpl != null){ if(downloadBtn != null){ downloadBtn.click(); "+       //window.alert("downloadBtn is not null!");  download_link.click();
                    " }else if(highSpeed != null){  highSpeed.click(); }else{ window.alert(\"downloadBtn is null!\"); var download_link=document.getElementById(\"formDownload\");  window.location.Reload();   } " +  //window.location.Reload();   if(download_link != null){window.alert('download_link is not null');}else{"+js+"}
                    "}else{ window.alert(\"singleViewTpl is null!\");} } window.onload=func;");
*/


           /* view.loadUrl("javascript: var html = document.getElementById('singleViewTpl').innerHTML; "+
                    " html=html+'<script type=\"text/javascript\">var downloadBtn =document.getElementById(\"aDownload\");" +
                    " var highSpeed =document.getElementById(\"highSpeed\"); var obj_keep =document.getElementsByClassName(\"dump\"); " +
                    " if(downloadBtn != null){downloadBtn.onclick=function(){window.alert(\"欢迎360普通下载！请按“确定”继续。\");};  downloadBtn.click(); " +    //downloadBtn.click();
                    " }else if(highSpeed != null){ highSpeed.onclick=function(){ window.alert(\"欢迎360高速下载！请按“确定”继续。\");  };   " +
                    " }else{obj_keep[0].onclick=function(){window.alert(\"欢迎保存到360云盘！请按“确定”继续。\");};}</script>'; "+
                    " document.getElementById('singleViewTpl').innerHTML = html; "+
                    //   " html1 = document.getElementById('singleViewTpl').innerHTML; window.alert(html1); "+
                    ""
            );*/

        }else if(url.contains("m.cloud")){
            String J_DownloadJS="J_Download[0].click();";
            view.loadUrl("javascript: var J_Download =document.getElementsByClassName('J_Download'); "
                    +J_DownloadJS );
        }else{
            Log.e("hook_url","no 执行此方法！！！");
        }
    }


    //360有问题，天翼

    //注入js函数监听
   /* private void addButtonClickListner(WebView view, String url){
        Log.e("hook_url_addButton", url);
        if(url.contains("pan.baidu.com")) {
            String downLoadJS="objs[0].onclick=function(){window.alert(\"欢迎普通下载！请按“确定”继续。\");};  objs[0].click();";
            String highDownload="var obj = document.getElementById('highDownload'); obj.onclick=function(){ window.alert(\"欢迎高速下载！请按“确定”继续。\");};  obj.click();";
            view.loadUrl("javascript: var objs =document.getElementsByClassName('btn normal-download'); "
                    + " if(objs[0] != null){"+downLoadJS+
                    " }else{ "+highDownload+"}");
        }else if(url.contains("lc.yunpan")){
            view.loadUrl("javascript: var html = document.getElementById('singleViewTpl').innerHTML; "+
                    " html=html+'<script type=\"text/javascript\">var downloadBtn =document.getElementById(\"aDownload\");" +
                    " var highSpeed =document.getElementById(\"highSpeed\"); var obj_keep =document.getElementsByClassName(\"dump\"); " +
                    " if(downloadBtn != null){downloadBtn.onclick=function(){window.alert(\"欢迎360普通下载！请按“确定”继续。\");};  downloadBtn.click(); " +    //downloadBtn.click();
                    " }else if(highSpeed != null){ highSpeed.onclick=function(){ window.alert(\"欢迎360高速下载！请按“确定”继续。\");  };   " +
                    " }else{obj_keep[0].onclick=function(){window.alert(\"欢迎保存到360云盘！请按“确定”继续。\");};}</script>'; "+
                     " document.getElementById('singleViewTpl').innerHTML = html; "+
                   //   " html1 = document.getElementById('singleViewTpl').innerHTML; window.alert(html1); "+
                    ""
            );
            String downloadBtn="var downloadBtn =document.getElementById(\"aDownload\"); downloadBtn.onclick=function(){window.alert(\"欢迎360普通下载！请按“确定”继续。\");   window.alert(downloadBtn.display);  };  ";
            String highSpeed ="var highSpeed =document.getElementById(\"highSpeed\"); highSpeed.onclick=function(){ window.alert(\"欢迎360高速下载！请按“确定”继续。\");}; ";

            view.loadUrl("javascript: "+downloadBtn+"  "+highSpeed
                    + " if(downloadBtn != null){ downloadBtn.click(); "+   // downloadBtn.click();
                    " }else if(highSpeed != null){  }" +   //highSpeed.click();
                    "else{ window.alert(\"is null!!\"); } ");


        }else if(url.contains("m.cloud")){
            String J_DownloadJS="J_Download[0].onclick=function(){window.alert(\"欢迎天翼下载！请按“确定”继续。\");}; J_Download[0].click();";
            view.loadUrl("javascript: var J_Download =document.getElementsByClassName('J_Download'); "
             +J_DownloadJS );
        }
    }
*/

  /*  //注入js函数监听
    private void addButtonClickListner(WebView view, String url){
        if(url.contains("pan.baidu.com")) {
            String downLoadJS="objs[0].click();";
            String highDownload="var obj = document.getElementById('highDownload'); obj.click();";
            view.loadUrl("javascript: var objs =document.getElementsByClassName('btn normal-download'); "
                    + " if(objs[0] != null){"+downLoadJS+
                    " }else{ "+highDownload+"}");
        }else if(url.contains("yunpan.cn")){
            view.loadUrl("javascript: var html = document.getElementById('singleViewTpl').innerHTML; "+
                    " html=html+'<script type=\"text/javascript\">var downloadBtn =document.getElementById(\"aDownload\");" +
                    " var highSpeed =document.getElementById(\"highSpeed\"); var obj_keep =document.getElementsByClassName(\"dump\"); " +
                    " if(downloadBtn != null){downloadBtn.click();  " +
                    " }else if(highSpeed != null){ highSpeed.click();  };   " +
                    " }else{obj_keep[0].click();}</script>'; "+
                    " document.getElementById('singleViewTpl').innerHTML = html; "
            );
        }else if(url.contains("cloud")){
            String J_DownloadJS="J_Download[0].onclick=function(){window.alert(\"欢迎天翼下载！请按“确定”继续。\");};  J_Download[0].click();";
            view.loadUrl("javascript: var J_Download =document.getElementsByClassName('J_Download'); "
                    +J_DownloadJS );
        }
    }*/


    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            int len = html.length();
            Log.d("hook_showSource_len",String.valueOf(len));
            int index = 0;
            byte[] array = new byte[1024];
            for (int i = 0; i < len; i += 1023) {
                index += 1023;
                if (index < len) {
                    System.arraycopy(html.getBytes(), index - 1023, array, 0, 1023);
                    Log.d("hook_data", new String(array));
                } else {
                    System.arraycopy(html.getBytes(), index - 1023, array, 0, len - index);
                    Log.d("hook_data", new String(array));
                }
            }
        }
    }
}
