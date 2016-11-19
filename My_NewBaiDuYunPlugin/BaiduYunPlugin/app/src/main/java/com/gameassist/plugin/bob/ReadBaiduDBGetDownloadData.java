package com.gameassist.plugin.bob;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class ReadBaiduDBGetDownloadData {
	
	public static void  broadCastBaiduDBdownloadData(Context context){
		String bduss=CustomGGZSBroadReceiver.getLoginBdussInfo(context);
		if(TextUtils.isEmpty(bduss)){
			return;
		}
   		String uri_bduss="content://com.baidu.netdisk/transfer/downloadtasks?bduss="+bduss;
   		String[] v2 = new String[] { "_id", "local_url",
				"remote_url", "offset_size", "size", "file_md5", "state",
				"rate" };
   		Uri uri = Uri.parse(uri_bduss);
		Cursor v1_1 = context.getContentResolver().query(uri, v2, "", null, null);
		SharedPreferences share_gameId_md5=context.getSharedPreferences("ggzs_gameId_md5", 0);
		String broadcastIntent = "action.bd.d.status.query.download.task";
		JSONArray   jsonArray=new JSONArray();
		Intent intent = new Intent(broadcastIntent);
		int num=0;
		while (v1_1.moveToNext()) {
			String remote_url=v1_1.getString(2);
			String[]  array1=remote_url.split("\\?");
		    String[]  array2=array1[0].split("/");
	    	String md5=array2[array2.length-1];
		    String gameId=share_gameId_md5.getString(md5, "");
			MyBroadcastBean bean = new MyBroadcastBean();
		     bean.downloadId=v1_1.getInt(0);
			if(TextUtils.isEmpty(gameId)){
				CustomGGZSBroadReceiver.UpdateDB_Delete(bean.downloadId,context,"");
				continue;
			}
			
			
			long offset_size = Long.valueOf(v1_1.getLong(3));
			long size = Long.valueOf(v1_1.getLong(4));
			int state = v1_1.getInt(6);
			long current_speed = Long.valueOf(v1_1.getLong(7));
			
            bean.gameId=gameId;
			bean.local_url = Util.getSandboxRealPath(v1_1.getString(1));
			bean.offset_size = offset_size;
			bean.size =size;
			bean.state=state;
            bean.current_speed=current_speed;
		
			JSONObject jsonObject=new JSONObject();
			try {
					jsonObject.put("filePath", bean.local_url);
					jsonObject.put("gameId", bean.gameId);
					jsonObject.put("currentSize", bean.offset_size);
					jsonObject.put("totalSize", bean.size);
					jsonObject.put("status", bean.state);
					jsonObject.put("downloadId", bean.downloadId);
					jsonObject.put("currentSpeed", bean.current_speed);
					num++;
					jsonArray.put(jsonObject);
				 } catch (JSONException e) {
						e.printStackTrace();
				}
			} //while   
		JSONObject jsObject=new JSONObject();
		try {
			jsObject.put("num",num);
			jsObject.put("data", jsonArray.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		intent.putExtra("json_array", jsObject.toString());  
		Log.i("hook_json_array",jsObject.toString());
		context.sendBroadcast(intent);  
	}
	
	 
      
}
