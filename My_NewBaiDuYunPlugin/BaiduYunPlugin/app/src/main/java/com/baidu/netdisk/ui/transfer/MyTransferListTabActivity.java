package com.baidu.netdisk.ui.transfer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.util.Log;

public class MyTransferListTabActivity {
	private static Activity activity;
	private static List<Activity> list=new ArrayList<Activity>();
	
    public static void setMyActivity(Activity activity){
    	//MyTransferListTabActivity.activity=activity;
    	Log.v("hook_setMyActivity_add",""+list.size());
    	list.add(activity);
    }
    
    public static  Activity getMyActivity(){
    	return activity;
    }
    
    public static void onMyDestory(){
    	//activity.on   //　　finish是Activity的类，仅仅针对Activity，当调用finish()时，只是将活动推向后台，并没有立即释放内存，活动的资源并没有被清理；当调用System.exit(0)时，杀死了整个进程，这时候活动所占的资源也会被释放。
    	while(list.size()>0){
    		Log.v("hook_onMyDestory",""+list.size());
    		Activity activity=list.remove(0);
    		if(activity!=null){
    		    activity.finish();
    		}
    	}
    }
}
