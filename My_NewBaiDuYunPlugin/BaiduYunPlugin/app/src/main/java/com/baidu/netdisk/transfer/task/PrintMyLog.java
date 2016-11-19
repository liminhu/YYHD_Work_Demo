package com.baidu.netdisk.transfer.task;

import android.net.Uri;
import android.util.Log;

public class PrintMyLog {
  
	public static void printlog(Uri uri){
		//Log.i("hook_delete_uri", "is begin ");
		String data="";
		if(uri!=null){
			data=uri.toString();
		}
		//Log.i("hook_delete_uri",data);
	}
	
	public static void printlog(int data){
		
	}
	
	public static void printlog(String data){
		//Log.i("hook_delete_log",data);
	}
}
