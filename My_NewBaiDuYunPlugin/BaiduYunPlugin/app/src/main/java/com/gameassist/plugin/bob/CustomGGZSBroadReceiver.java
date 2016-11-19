package com.gameassist.plugin.bob;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.netdisk.ui.transfer.MyTransferListTabActivity;



public class CustomGGZSBroadReceiver extends BroadcastReceiver{
	public static final String ACTION = "action.bd.d.action";
	private static String TAG="hook_plugin_receive";
	public static String uri_bduss="";
	
	private static String update="UPDATE download_tasks SET state=?,rate=? WHERE (_id=? AND (state=100 OR state=104))";

	
	
	//state:100:刚开始下，104：正在下，105:暂停,106:出错，110：完成, 111:删除 , (112:删除成功)
	
	@Override
	public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)){
        	Log.e(TAG,"OnReceive begin");
        	String json=intent.getStringExtra("json");
        	try {
        		Log.e(TAG,json);
				JSONObject jsonObject=new JSONObject(json);
				String gameId=jsonObject.getString("gameId");
				SharedPreferences share = context.getSharedPreferences("ggzs_gameId", 0);
				String  db_id = share.getString(gameId, "");
				if(!TextUtils.isEmpty(db_id)){
					int  downloadId=Integer.valueOf(db_id);
		        	int  status=jsonObject.getInt("status");
		        	Log.i(TAG, ""+gameId);
		        	Log.i(TAG, ""+downloadId);
		           	Log.i(TAG, ""+status);
		           	if(!TextUtils.isEmpty(Util.bduss)){
		           		uri_bduss=Util.bduss;
		           	}else{
		           		uri_bduss="content://com.baidu.netdisk/transfer/downloadtasks?bduss="+getLoginBdussInfo(context);
		           	}
		           	if(status==105){
		           		UpdateDB_Pause(downloadId, context);
		           	}else if(status==100 || status==104){
		           		UpdateDB_Start(downloadId, context);
		           	}else if(status==111){
		           		//UpdateDB_Pause(downloadId,context);
		           		UpdateDB_Delete(downloadId, context, gameId);
		           	}else if(status==106){ //删除使用
		           	  // UpdateDB_New_Delete(downloadId,context);
		            // UpdateDB_Delete(downloadId, context, gameId);
		           	}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
      }
	}
	
	private static void UpdateDB_Pause(int downloadId,Context context){
	    ContentValues v0 = new ContentValues(2);
	    v0.put("state", Integer.valueOf(105));
	    v0.put("rate", Integer.valueOf(0));
	    Log.i("hook_UpdateDB",uri_bduss);
	    if(!TextUtils.isEmpty(uri_bduss)){
	    	Uri uri = Uri.parse(uri_bduss);
	    	context.getContentResolver().update(uri, v0, "_id=? AND (state=100 OR state=104)", new String[]{String.valueOf(downloadId)});
	    	Log.i("hook_pause",downloadId+"have finish");
	    }
	    
	}
	
	private static void UpdateDB_Start(int downloadId,Context context){
	    ContentValues v0 = new ContentValues(2);
	    v0.put("state", Integer.valueOf(100));
	    Log.i("hook_UpdateDB",uri_bduss);
	    if(!TextUtils.isEmpty(uri_bduss)){
	    	Uri uri = Uri.parse(uri_bduss);
	    	context.getContentResolver().update(uri, v0, "_id=? ", new String[]{String.valueOf(downloadId)});
	    	Log.i("hook_pause",downloadId+"have finish");
	    }
	}
	
	
/*	
	private static void UpdateDB_New_Delete(int downloadId,Context context){
	    ContentValues v0 = new ContentValues(2);
	    v0.put("state", Integer.valueOf(106));
	    v0.put("rate", Integer.valueOf(0));
	    Log.i("hook_new_delete",uri_bduss);
	    if(!TextUtils.isEmpty(uri_bduss)){
	    	Uri uri = Uri.parse(uri_bduss);
	    	context.getContentResolver().update(uri, v0, "_id=? ", new String[]{String.valueOf(downloadId)});
	    	Log.i("hook_new_delete",downloadId+"have finish");
	    }
	}*/
	
	
	public static void UpdateDB_Delete(int downloadId,Context context,String gameId){
	    Log.i("hook_delete_begin",uri_bduss);
	    if(!TextUtils.isEmpty(uri_bduss)){
	    	Uri uri = Uri.parse(uri_bduss);
	    	String[] v2 = new String[] { "_id", "local_url",
					"remote_url", "offset_size", "size", "transmitter_type", "state",
					"rate" };
	        Cursor v1_1 = context.getContentResolver().query(uri, v2, "", null, null);
	        String sandbox="";
	        String local_url="";
	        //int transmitter_type=1;
			while (v1_1.moveToNext()) {
				if(v1_1.getInt(0)==downloadId){
					 Log.e("hook_delete_id", String.valueOf(v1_1.getInt(0)));
					 local_url=v1_1.getString(1);
					 sandbox=Util.getSandboxRealPath(local_url);
					 String s=".!bn";
					 Log.e("hook_delete_path", sandbox);
					 
					 MyBroadcastBean bean = new MyBroadcastBean();
			         bean.gameId=gameId;
					 bean.local_url = Util.getSandboxRealPath(v1_1.getString(1));
					bean.offset_size = Long.valueOf(v1_1.getLong(3));
					bean.size = Long.valueOf(v1_1.getLong(4));;
					bean.state=112;
		            bean.downloadId=v1_1.getInt(0);
		            bean.current_speed=0;
					// transmitter_type=v1_1.getInt(5);
					 //sandbox=local_url;
					 File file1=new File(sandbox);
					 if(file1.exists()){
						 file1.delete();
					 }
					 
					 File file=new File(sandbox+s);
					 if(file.exists()){
						 file.delete();
					 }
					 Log.i("hook_delete","doing ....");
					 context.getContentResolver().delete(uri, "_id=? ", new String[]{String.valueOf(downloadId)});
					 SharedPreferences.Editor sharedata1 = context.getSharedPreferences("ggzs_gameId", 0).edit();
					 sharedata1.putString(gameId,"");
					 sharedata1.commit();
				     Log.i("hook_delete",downloadId+"have finish");
				    	if(MyTransferListTabActivity.getMyActivity()!=null){
				    		Log.i("hook_activity", "delete_onMyDestory");
				    	//	MyTransferListTabActivity.onMyDestory();
				     }
				    
						 JSONObject jsonObject=new JSONObject();
						 try {
							jsonObject.put("filePath", bean.local_url);
							jsonObject.put("gameId", bean.gameId);
							jsonObject.put("currentSize", bean.offset_size);
							jsonObject.put("totalSize", bean.size);
							jsonObject.put("status", bean.state);
							jsonObject.put("downloadId", bean.downloadId);
							jsonObject.put("currentSpeed", bean.current_speed);
							
							String json=jsonObject.toString();
							Intent intent = new Intent("action.bd.d.status.delete.finish");
							intent.putExtra("json", json);
							
							Log.e("hook_state", "112 delete finish  sendBroadcast");
							
							try{
								Thread.sleep(500);
								context.sendBroadcast(intent);    //延时0.5s发送删除广播
							}catch(Exception e){
								e.printStackTrace();
							}
						 }catch(Exception e){
							 e.printStackTrace();
						 }
					 break;
				}
			}
			v1_1.close();
/*	    	String uri_bduss_1="content://com.baidu.netdisk/transfer/downloadtasks/deleted?bduss="+getLoginBdussInfo(context);
	    	Uri uri_1 = Uri.parse(uri_bduss_1);
	    	Log.e("hook_delete_bd1",uri_bduss_1);
	    	ContentValues values = new ContentValues();
	    	values.put("_id", downloadId);
	    	values.put("local_url", local_url);
	      	values.put("transmitter_type", transmitter_type);
	      	values.put("is_delete_file", Integer.valueOf(1));
	    	context.getContentResolver().insert(uri_1, values);*/
	    	
	    //	context.getContentResolver().delete(uri_1, "task_id=? ", new String[]{String.valueOf(downloadId)});
/*	    	Handler handler=MyGetHandler.getDeleteHandler();
	    	if(handler!=null){
	    		Log.i("hook_sendEmptyMessage","is do ...");
	    		handler.sendEmptyMessage(0);
	    	}else{
	    		Log.i("hook_sendEmptyMessage","is null");
	    	}*/
	    }
	}
	
	
	public static String getLoginBdussInfo(Context context){
		Log.v("hook_bduss_getLogin", "begin");
		String login_uri = "content://com.baidu.netdisk.account/login";
		String[] login_parame = new String[] { "_id", "account_uid",
				"account_name", "account_phone", "account_email",
				"account_bduss", "account_auth", "account_ptoken",
				"account_stoken", "account_weakpass", "account_os_is_binded",
				"account_os_type", "account_os_sex", "account_os_headurl",
				"account_os_username", "account_type", "is_first_login",
				"lock_password", "is_current_login",
				"is_lock_password_enabled", "personal_page_follow_count",
				"personal_page_album_count", "personal_page_avatar_url",
				"personal_page_fans_count", "personal_page_intro",
				"personal_page_isvip", "personal_page_pubshare_count",
				"personal_page_uk", "personal_page_username",
				"personal_page_user_type", "uk", "name", "nick_name", "intro",
				"avatar_url", "display_name", "remark" };
		Uri login = Uri.parse(login_uri);
		Cursor cursor_login = context.getContentResolver().query(login, login_parame, null,
				null, null);
        if(cursor_login.getCount()==0){
        	return "";
        }
		if (cursor_login.moveToNext()) {
			String bduss = cursor_login.getString(5);
			Log.v("hook_bduss_getLogin", bduss); // 记录用户的登录状态
			/*String url = "content://com.baidu.netdisk/transfer/downloadtasks?"
					+ bduss;*/
			return bduss;
		} else {
			Log.v("hook_bduss_getLog", "is null");
		}
		return "";
	}
	
	

}
