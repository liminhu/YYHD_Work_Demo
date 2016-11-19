package com.baidu.netdisk.wap.launch;

import com.baidu.netdisk.share.io.a.SaveDownloadFileName;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;


public class SaveMyGameIDInfo {
    public static boolean isLoad=true;
    public static Context context=null;
	
	public static void saveMyGameIDToSharedPreference(String path){
		String[] array=path.split("&");
		String gameId=array[array.length-1];
		Log.v("hook_gameId_save", gameId);
		String newGameId="";
		if(!TextUtils.isEmpty(gameId) && gameId.contains("=")){
			String[] splict=gameId.split("=");
			newGameId=splict[1];
		}else{
			newGameId=gameId;	
		}
		if(context!=null){
			SharedPreferences share_gameId = context.getSharedPreferences("ggzs_gameId", 0);
	        String  db_id = share_gameId.getString(
	        		newGameId, "");
			if(TextUtils.isEmpty(db_id)){
				Log.i("hook1_OnActivityCreate_isload","true");
				isLoad=true;
			}else{
				isLoad=false;
			}
		}
		SaveDownloadFileName.gameId=newGameId;
	}
}
