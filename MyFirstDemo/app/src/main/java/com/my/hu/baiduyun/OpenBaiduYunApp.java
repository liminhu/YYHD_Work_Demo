package com.my.hu.baiduyun;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;

import java.util.List;
import java.util.Random;

/**
 * Created by hu on 9/18/16.
 */
public class OpenBaiduYunApp {
    private Context context;
    public OpenBaiduYunApp(Context context){
        this.context=context;
    }

    public  void openStartApp(String packageName, String paramUrl,int a){
        try{
            Log.i("hook_paramUrl",paramUrl);
            Random random=new Random();
          //  int a=random.nextInt(100);
            paramUrl=paramUrl+"&gameID="+a;
            PackageManager pm=context.getPackageManager();
            PackageInfo packageInfo=context.getPackageManager().getPackageInfo(packageName,0);
            Intent intent=new Intent(Intent.ACTION_MAIN,null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setPackage(packageInfo.packageName);
            List<ResolveInfo> apps=pm.queryIntentActivities(intent,0);
            ResolveInfo ri=apps.iterator().next();
            if(ri!=null){
                String pName=ri.activityInfo.packageName;
                String className=ri.activityInfo.name;
                Intent start_intent=new Intent(Intent.ACTION_MAIN);
                start_intent.addCategory(Intent.CATEGORY_LAUNCHER);
                ComponentName cn=new ComponentName(pName,className);
                start_intent.setComponent(cn);
                start_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                start_intent.setData(Uri.parse(paramUrl));
                context.startActivity(start_intent);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
