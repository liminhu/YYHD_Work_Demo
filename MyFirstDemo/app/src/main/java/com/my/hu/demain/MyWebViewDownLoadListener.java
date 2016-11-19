package com.my.hu.demain;

import android.content.Context;
import android.content.Intent;
import android.icu.util.Output;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.webkit.DownloadListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.my.hu.Util.SqliteDataHelper;
import com.my.hu.Util.StringUtil;
import com.my.hu.myfirstdemo.Test1MainActivity;
import com.my.hu.postdata.MyNewWebViewBase;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Date;

/**
 * Created by hu on 9/6/16.
 * WebView默认没有开启文件下载的功能，如果要实现文件下载的功能，
 * 需要设置WebView的DownloadListener，通过实现自己的DownloadListener
 * 来实现文件的下载
 */
public class MyWebViewDownLoadListener implements DownloadListener {
    private String tag="hook_onDownLoadListenr";
    private Context mContext;
   // private MyProgressDialog progressDialog;
    private ProgressBar progressBar;
    private int needDownLoadFileLen;
    private int haveDownedFileLength;
    private TextView textView;
    private String platform_name;
    private String md5;
    private MyNewWebViewBase webViewBase;
    private Handler handler1=null;

    public MyWebViewDownLoadListener(Context context){
        this.mContext=context;
    }

    public MyWebViewDownLoadListener(Context context, MyProgressDialog progressDialog){
        this.mContext=context;
       // this.progressDialog=progressDialog;
    }


    public MyWebViewDownLoadListener(Context context, ProgressBar progressBar, TextView textView){
        this.mContext=context;
        this.progressBar=progressBar;
        this.textView=textView;
    }

    public MyWebViewDownLoadListener(Context context, ProgressBar progressBar, TextView textView, String platform_name,String md5){
        this.mContext=context;
        this.progressBar=progressBar;
        this.textView=textView;
        this.platform_name=platform_name;
        this.md5=md5;
    }

    public MyWebViewDownLoadListener(Handler handler, Context context, ProgressBar progressBar, TextView textView, String platform_name, String md5){
        this.mContext=context;
        this.progressBar=progressBar;
        this.textView=textView;
        this.platform_name=platform_name;
        this.md5=md5;
        this.webViewBase=webViewBase;
        this.handler1=handler;
    }


    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        Log.e(tag,"onDownloadStart_url_中文:"+StringUtil.myUrlDecodedata(url));

/*        Log.v(tag, "url="+url);
        Log.v(tag, "userAgent="+userAgent);
        Log.v(tag, "contentDisposition="+contentDisposition);
        Log.v(tag, "mimetype="+mimetype);
        Log.v(tag, "contentLength="+contentLength);*/
//        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
//            Toast.makeText(mContext, "需要SD卡.", Toast.LENGTH_LONG).show();
//            Log.e(tag,"sd_card: is not exit");
//            return;
//        }
        String fileName=contentDisposition;
        fileName=fileName.substring(fileName.indexOf("\"")+1,fileName.length()-1);
        UrlInfo urlInfo=new UrlInfo();
        String platformName=platform_name;
        md5 = (md5==null | md5.equals("")) ? "is null" : md5;
        String save_md5=md5;
        Log.e(tag,"onDownloadStart_url_中文:----------"+md5);
        urlInfo.setPlatform_name(platformName);
        urlInfo.setMd5(save_md5);
        Log.e("hook_md5",md5);
        urlInfo.setDownLoad_url(url);
        urlInfo.setDownload_package_name(fileName);
        urlInfo.setRequest_time(new Date());
        SqliteDataHelper  dbHelper=new SqliteDataHelper(mContext);
        dbHelper.saveUrlInfo(urlInfo);
        dbHelper.closeDB();
//        Message msg=Message.obtain();
//        msg.what=1;
//        handler1.sendMessage(msg);
        Log.v("hook_time",StringUtil.getDateTime());
        if(handler1!=null) {
            Log.d("hook_handler1","handler1.sendMessage!=null");
            handler1.obtainMessage(1).sendToTarget();
        }

        //DownLoaderTask task=new DownLoaderTask();
        //task.execute(url,contentDisposition);


    }

    private  class DownLoaderTask extends AsyncTask<String, Void, String>{

        public DownLoaderTask(){}

        @Override
        protected String doInBackground(String... params) {
            String url=params[0];
            String fileName=params[1];
            Log.d(tag+"_doInBackground_url", "url="+url);
           // fileName=url.substring(url.lastIndexOf("/")+1);
            fileName=fileName.substring(fileName.indexOf("\"")+1,fileName.length()-1);
            Log.d(tag+"_doInBackground_Name", "fileName="+fileName);
            File directory=Environment.getExternalStorageDirectory();
            File file=new File(directory,fileName);
            if (file.exists()) {
                Log.v(tag+"_doInBackground_exist","the file"+fileName+" has already exists.");
                return fileName;
            }
            File downFile=downLoadFile(url, Environment.getExternalStorageDirectory().toString(),fileName);
            if(downFile!=null){
                return  downFile.getName();
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result==null){
                Toast t=Toast.makeText(mContext, "连接错误！请稍后再试！"+result, Toast.LENGTH_LONG);
                t.setGravity(Gravity.CENTER, 0, 0);
                t.show();
                return;
            }
            Message message2=new Message();
            message2.what=2;
            handler.sendMessage(message2);
            Toast t=Toast.makeText(mContext,result+"已保存到SD卡上.", Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER, 0, 0);
            t.show();
            File directory=Environment.getExternalStorageDirectory();
            File file=new File(directory, result);
            Log.d(tag+"_onPostExecute_result", result+"Path="+file.getAbsolutePath());
            //Intent intent=progressDialog.getFileIntent(file);
            //mContext.startActivity(intent);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
           if(!Thread.currentThread().isInterrupted()){
               switch (msg.what){
                   case 0:
                       progressBar.setMax(needDownLoadFileLen); break;
                   case 1:
                       long temp=((long)haveDownedFileLength & 0xFFFFFFFF)*100;
                       long data=(long)needDownLoadFileLen;
                   //    Log.d(tag+"_temp", String.valueOf(temp));
                       int x=(int)(temp/data);
                   //    Log.d(tag+"_x", String.valueOf(x));
                       progressBar.setProgress(x);
                       textView.setText(x+"%");
                       break;
                   case 2:
                       progressBar.setProgress(100);
                       textView.setText("100%");
                       Toast.makeText(mContext.getApplicationContext(), "下载完成", Toast.LENGTH_LONG).show();
                       break;
                   default:
                       break;
               }
           }
        }
    };



    public  File downLoadFile(String urlPath, String downLoadDir, String fileName){
        File file=null;
        try{
            URL url=new URL(urlPath);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.connect();
            Log.v(tag+"_downLoadFile_url", urlPath);
            Log.v(tag+"_downLoadFile_dir","the fileDirectory"+downLoadDir+"/"+fileName);
            //文件大小
            int fileLength=httpURLConnection.getContentLength();
            needDownLoadFileLen=fileLength;
            Log.v(tag+"_downLoadFile_length", String.valueOf(fileLength));

            BufferedInputStream bin=new BufferedInputStream(httpURLConnection.getInputStream());

            file=new File(downLoadDir,fileName);
            Log.v(tag+"_downLoadFile_fileName",file.getAbsolutePath());
            OutputStream out=new FileOutputStream(file);
            int size=0;
            int len=0;
            Message message2=new Message();
            byte[]  buf=new byte[1024];
            while ((size=bin.read(buf))!=-1){
                len+=size;
                out.write(buf, 0, size);
                //打印进度条
                haveDownedFileLength=len;
               // Log.v(tag+"_downLoadFile_len",String.valueOf(len));
                Message message1=new Message();
                message1.what=1;
                handler.sendMessage(message1);
            }
            bin.close();
            out.close();
            message2.what=2;
            handler.sendMessage(message2);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return file;
        }
    }



}
