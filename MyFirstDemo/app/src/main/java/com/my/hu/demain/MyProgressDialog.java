package com.my.hu.demain;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;

/**
 * Created by hu on 9/6/16.
 */
public class MyProgressDialog {
    private ProgressDialog mDialog;
    private Context mContext;


    public MyProgressDialog(Context context){
        this.mContext=context;
    }


    public void updateProgressDialog(int id){
        if(id==100){
            closeProgressDialog();
        }
        if(mDialog!=null){
            closeProgressDialog();
            mDialog=new ProgressDialog(mContext);
            mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); //设置风格
            mDialog.setIndeterminate(false);  //设置进度条是否为不明确
            mDialog.setCancelable(true);  //设置进度条是否可以按退回键取消
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setMax(100);
            mDialog.setProgress(id);
            mDialog.setMessage("已完成："+id+"%");
            mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    mDialog=null;
                }
            });
            mDialog.show();
        }
    }


    public void showProgressDialog(){
        if(mDialog==null){
            mDialog=new ProgressDialog(mContext);
            mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); //设置风格
            mDialog.setMessage("正在下载，请等待。。。");
            mDialog.setIndeterminate(false);  //设置进度条是否为不明确
            mDialog.setCancelable(true);  //设置进度条是否可以按退回键取消
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setMax(100);
            mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    mDialog=null;
                }
            });
            mDialog.show();
        }
    }

    public   void closeProgressDialog(){
        if(mDialog!=null){
            mDialog.dismiss();
            mDialog=null;
        }
    }



    public Intent getFileIntent(File file){
        Uri uri= Uri.fromFile(file);
        String type=getMIMEType(file);
        Log.d("hook_getFileIntent", "type="+type);
        Intent intent=new Intent("andorid.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri,type);
        return intent;
    }

    private String getMIMEType(File f){
        String type="";
        String fName=f.getName();
        //取得扩展名
        String end=fName.substring(fName.lastIndexOf(".")+1, fName.length()).toLowerCase();
        if(end.equals("pdf")){
            type="application/pdf";
        }else if(end.equals("m4a") || end.equals("mp3") || end.equals("mid")
                || end.equals("xmf") || end.equals("ogg") || end.equals("wav")){
            type="audio/*";
        }else if(end.equals("3gp") || end.equals("mp4")){
            type="video/*";
        }else if(end.equals("jpg") || end.equals("gif") || end.equals("png")
                || end.equals("jpeg") || end.equals("bmp")){
            type="image/*";
        }else if(end.equals("apk")){
            type="application/vnd.android.package-archive";
        }else{
            //如果无法直接打开，就跳到软件列表给用户选择
            type="*/*";
        }
        return type;

    }

}
