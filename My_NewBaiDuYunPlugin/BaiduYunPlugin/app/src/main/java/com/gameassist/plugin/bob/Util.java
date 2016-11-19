package com.gameassist.plugin.bob;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.netdisk.ui.transfer.MyTransferListTabActivity;

public class Util {
	private static int num = 0;
	private Context context;

	private static SharedPreferences share_gameId;
	private static SharedPreferences share_gameId_md5;
	
	private static long beginCurrentTime;
	
	private static MyBroadcastBean bean = new MyBroadcastBean();
	public static final String broadcastIntent = "action.bd.d.status.changed";
    public static String bduss="";
   
    //
    
	public void startMyDownLoadListening(Context context) {
		Log.i("hook_MyDown","is do ...");
		//share = context.getSharedPreferences("ggzs", 0);
		share_gameId = context.getSharedPreferences("ggzs_gameId", 0);
		share_gameId_md5=context.getSharedPreferences("ggzs_gameId_md5", 0);
		
		this.context = context;
		SharedPreferences v0 = context.getSharedPreferences("Setting", 0);
		ContentResolver contentResolver = context.getContentResolver();
		// 查找是否登录
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
		Cursor cursor_login = contentResolver.query(login, login_parame, null,
				null, null);
		if(cursor_login.getCount()==0){
			return ;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("bduss=");
		if (cursor_login.moveToNext()) {
			Log.v("gameassist_id", String.valueOf(cursor_login.getInt(0)));
			Log.v("gameassist_account_uid", cursor_login.getString(1));
			Log.v("gameassist_account_name", cursor_login.getString(2));
			String bduss = cursor_login.getString(5);
			Log.v("gameassist_account_bduss", bduss); // 记录用户的登录状态
			sb.append(bduss);
		} else {
			Log.v("gameassist_login", "is null");
		}
		cursor_login.close();
		String url = "content://com.baidu.netdisk/transfer/downloadtasks?"
				+ sb.toString();
	    this.bduss=url;
		contentResolver.registerContentObserver(Uri.parse(url), true,
				new NotifyDBChange(new Handler(), contentResolver, url));
		
    	if(MyTransferListTabActivity.getMyActivity()!=null){
    		Log.i("hook_activity", "MyTransferListTabActivity");
    		MyTransferListTabActivity.onMyDestory();
    	}
		/*String delete_url = "content://com.baidu.netdisk/transfer/downloadtasks/deleted?"
				+ sb.toString();
		contentResolver.registerContentObserver(Uri.parse(delete_url), true,
				new NotifyDBDelete(new Handler(), contentResolver, delete_url));*/
	}
	
	class NotifyDBDelete extends ContentObserver{
		private ContentResolver contentResolver;
		private String url;
		private final String[] v2 = new String[] { "_id", "local_url","transmitter_type",
				"is_delete_file"};
		private static final String LOG_TAG="hook_delete_Change";
		
		public NotifyDBDelete(Handler handler, ContentResolver contentResolver,
				String url) {
			super(handler);
			this.contentResolver = contentResolver;
			this.url = url;
		}
		
		@Override
		public void onChange(boolean selfChange) {
			Log.i(LOG_TAG, "onChange");
			Uri uri = Uri.parse(url);
			Cursor v1_1 = contentResolver.query(uri, v2, "", null, null);
			while (v1_1.moveToNext()) {
				Log.v(LOG_TAG, String.valueOf(v1_1.getInt(0)));
				Log.v(LOG_TAG, v1_1.getString(1));
				Log.v(LOG_TAG,  String.valueOf(v1_1.getInt(2)));
				Log.v(LOG_TAG,  String.valueOf(v1_1.getInt(3)));
			}
			v1_1.close();
		}


	}
	
	

  class NotifyDBChange extends ContentObserver {
		private ContentResolver contentResolver;
		private String url;
		private final String[] v2 = new String[] { "_id", "local_url",
				"remote_url", "offset_size", "size", "file_md5", "state",
				"rate" };
		public NotifyDBChange(Handler handler, ContentResolver contentResolver,
				String url) {
			super(handler);
			this.contentResolver = contentResolver;
			this.url = url;
			beginCurrentTime=System.currentTimeMillis();
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			Log.i("gameassist_DB", "onChange_" + num);
			num++;
			Uri uri = Uri.parse(url);
			Cursor v1_1 = contentResolver.query(uri, v2, "", null, null);
			while (v1_1.moveToNext()) {
				Log.v("hook_id", String.valueOf(v1_1.getInt(0)));
				Log.v("hook_local_url", v1_1.getString(1));
				Log.v("hook_remote_url", v1_1.getString(2));
				Log.v("hook_offset_size",
						"" + Long.valueOf(v1_1.getLong(3)));
				Log.v("gameassist_hook_size",
						"" + Long.valueOf(v1_1.getLong(4)));
	
				Log.v("gameassist_hook_rate",
						v1_1.getInt(6) + "--"
								+ String.valueOf(v1_1.getInt(7)) + "--");
				String remote_url=v1_1.getString(2);
				String[]  array1=remote_url.split("\\?");
			    String[]  array2=array1[0].split("/");
		    	String md5=array2[array2.length-1];
		    	Log.i("hook_md5", md5);
			    String gameId=share_gameId_md5.getString(md5, "");
				Log.i("hook_md5_gameId", gameId);
			    
				if(TextUtils.isEmpty(gameId)){
					//return;
					continue;
				}
				
				
				long offset_size = Long.valueOf(v1_1.getLong(3));
				long size = Long.valueOf(v1_1.getLong(4));
				int state = v1_1.getInt(6);
				long current_speed = Long.valueOf(v1_1.getLong(7));
				
                bean.gameId=gameId;
				bean.local_url = getSandboxRealPath(v1_1.getString(1));
				bean.offset_size = offset_size;
				bean.size =size;
				bean.state=state;
                bean.downloadId=v1_1.getInt(0);
                bean.current_speed=current_speed;
    			
                
                String  db_id = share_gameId.getString(
						gameId, "");
				Intent intent = new Intent(broadcastIntent);
				if (TextUtils.isEmpty(db_id)) {
					SharedPreferences.Editor sharedata1 = context
							.getSharedPreferences("ggzs_gameId", 0).edit();
					sharedata1.putString(gameId,String.valueOf(v1_1.getInt(0)));
					sharedata1.commit();
					
					JSONObject jsonObject=new JSONObject();
					 try {
						jsonObject.put("filePath", bean.local_url);
						jsonObject.put("gameId", bean.gameId);
						jsonObject.put("currentSize", bean.offset_size);
						jsonObject.put("totalSize", bean.size);
						jsonObject.put("status", Integer.valueOf(100));
						jsonObject.put("downloadId", bean.downloadId);
						jsonObject.put("currentSpeed", bean.current_speed);
						String json=jsonObject.toString();
						intent.putExtra("json", json);  
						Log.e("hook_state", "100 sendBroadcast");
						context.sendBroadcast(intent);  //首次下载开始发广播
					 } catch (JSONException e) {
							e.printStackTrace();
					}
				}    
		        if(bean.state==100){
		        	//return;
		        	continue;
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
					intent.putExtra("json", json);
			//		long currentTime=System.currentTimeMillis();
			//		Log.i("hook_currenttime",""+currentTime);
			//		Log.i("hook_begin-currentTime",""+(currentTime-beginCurrentTime));
		
	/*				if(state==104){  // && (currentTime-beginCurrentTime)>1000
						//Log.i("hook_currenttime",""+currentTime);
				//		beginCurrentTime=System.currentTimeMillis();
						Log.e("hook_state_1", "104 sendBroadcast1");
						Log.i("<json>","ee"+json);
	                    context.sendBroadcast(intent);
					}else if(state != 104){
						Log.e("hook_state", state+"state sendBroadcast");
					    context.sendBroadcast(intent);
					}*/
					 context.sendBroadcast(intent);
					if (offset_size == size) {
						Log.i("hook_json", json.toString());
						jsonObject.put("status", "110");
						
						SharedPreferences.Editor sharedata1 = context.getSharedPreferences("ggzs_gameId", 0).edit();
						sharedata1.putString(gameId,"");
					    sharedata1.commit();
						Log.e("hook_state", "110 sendBroadcast");
						context.sendBroadcast(intent);
						//contentResolver.unregisterContentObserver(this);
						//Log.e("hook_gameassist", "unregisterContentObserver");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	
	public final static String DIRECTORY = "GGZS_Download";
	public final static String SANDBOX_DIRECTORY = "files/sandbox/";
	public static String getSandboxRealPath(String localPath){
		StringBuffer prePath = new StringBuffer();
		String data[] = localPath.split("/");
		int postion = -1;
		for (int i = 0; i < data.length; i++) {
			String name = data[i];
			if(TextUtils.equals(DIRECTORY, name)){
				postion = i;
				break;
			}
			else{
				prePath.append(data[i]+"/");
			}
		}
		
		if(postion >0){
		 String whichSD = data[postion- 1];
		 localPath = localPath.replace(prePath,prePath+SANDBOX_DIRECTORY+whichSD+"/");
		}
		return localPath;
	}

}
