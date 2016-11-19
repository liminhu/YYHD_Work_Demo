package com.baidu.netdisk.transfer.task;

import java.io.File;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.netdisk.share.io.a.SaveDownloadFileName;


public class MyChangePath {
private static Handler handler;
	
	public static Handler getHandler() {
		return handler;
	}

	public static void setHandler(Handler handler) {
		MyChangePath.handler = handler;
	}

	public static String changePath(String path){
/*		 Message msg =Message.obtain();
		 msg.what=1;
		 handler.sendMessage(msg);	 
		 Log.i("hook_changePath", path);
		 return path;
		*/
		
		//需要改路径时加上
		
/*		if(TextUtils.isEmpty(SaveDownloadFileName.gameId)){
				return path;
		}*/
		File directory=Environment.getExternalStorageDirectory();
	    StringBuilder sb=new StringBuilder();
	    sb.append(directory.getAbsoluteFile());
	    sb.append("/GGZS_Download/");//GGZS_Download/
		String[] dataStrings=path.split("/");
		Log.i("hook_file_name", dataStrings[dataStrings.length-1]);
		if(dataStrings.length>1){
			 sb.append(dataStrings[dataStrings.length-1]);	
			 Log.v("hook_changePath",sb.toString());
			 Message msg =Message.obtain();
			 msg.what=1;
			 handler.sendMessage(msg);	 
			 Log.i("hook_changePath", sb.toString());
			 return sb.toString();
		}else{
			return path;
		}
	
	}
}
