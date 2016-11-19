package com.gameassist.plugin.bob;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.baidu.netdisk.share.io.a.SaveDownloadFileName;
import com.baidu.netdisk.transfer.task.MyChangePath;
import com.baidu.netdisk.ui.transfer.MyTransferListTabActivity;
import com.baidu.netdisk.wap.launch.SaveMyGameIDInfo;
import com.gameassist.plugin.ActivityCallback;
import com.gameassist.plugin.ClassLoaderCallback;
import com.gameassist.plugin.Plugin;



public class PluginEntry extends Plugin implements View.OnClickListener{
	private BroadcastReceiver actionReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			String json = arg1.getStringExtra("json");
			Log.d("<plugin_log>", json);
		}
	};
	private static final String START_PLUGIN_ACTION = "action.bd.d.status.start_plugin";
	private View pluginView;
	private static Context plugcxt; 
	private String[] activityArray=new String[]{"com.baidu.netdisk.ui.Navigate","com.baidu.netdisk.ui.account.LoginRegisterActivity","com.baidu.netdisk.ui.QuickSettingsActivity"
			,"com.baidu.netdisk.module.sharelink.ShareLinkActivity"};

	private static CustomGGZSBroadReceiver mBroadcastReceiver;
	private static String bduss="";
	private static Util util=null;
	
	private  Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			 if (msg.what == 1) {
				Log.i("hook_gameassist","msg.what==1");
				if(util==null){
				   util=new Util();
				}
				util.startMyDownLoadListening(getTargetApplication().getApplicationContext());
			}
		}
	};
	
	public static Context getPlugcxt() {
		return plugcxt;
	}

	private static Context targetcxt;
	public static Context getTargetcxt() {
		return targetcxt;
	}

	
	private static String getCmdline() {
		String readCmdline="/proc/self/cmdline";
		File file = new File(readCmdline); //new File(String.format("/proc/%d/cmdline", pid));
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String cmdline = br.readLine().trim();
			return cmdline;
		} catch (Exception e) {
			return "";
		} finally {
			try {
				br.close();
			} catch (Exception e) {
			}
		}
	}

	
	
	@Override
	public boolean OnPluginCreate() {
		//getTargetApplication().registerReceiver(actionReceiver, new IntentFilter("action.bd.d.status.changed"));

		try {
		    Log.i("gameassist", "1000");	
			String processName=getCmdline();
			if(processName.contains("bdservice")){
		    	Log.v("hook_getCmdline","begin bdservice");
				return false;
			}
			Log.v("hook_processName",processName);
		    if(mBroadcastReceiver==null){
		    	Log.v("hook_registerReceiver","begin is null");
				mBroadcastReceiver = new CustomGGZSBroadReceiver();
				IntentFilter intentFilter = new IntentFilter();
				intentFilter.addAction(CustomGGZSBroadReceiver.ACTION);
				getTargetApplication().registerReceiver(mBroadcastReceiver, intentFilter);
				//
				Log.v("hook_start_plugin", "action.bd.d.status.start_plugin");
				getTargetApplication().sendBroadcast(new Intent(START_PLUGIN_ACTION));
				ReadBaiduDBGetDownloadData.broadCastBaiduDBdownloadData(getContext());
				
				bduss=CustomGGZSBroadReceiver.getLoginBdussInfo(getTargetApplication());
				if(!TextUtils.isEmpty(bduss)){
					Log.i("hook_listen", "begin listen ");
				    handler.sendEmptyMessage(1);
				}else{
					Log.i("hook_listen", "no login no listen ");
				}
		    }
		    Log.i("gameassist", "1111");	
		    MyChangePath.setHandler(handler);
		    SaveDownloadFileName.setContext(getTargetApplication());
		    SaveMyGameIDInfo.context=getTargetApplication();
		    getPluginManager().registerClassLoaderOverride(this,getClass().getClassLoader(), new ClassLoaderCallback() {
		
		    @Override
			public boolean shouldOverrideResource(String className) {
				return false;
			}
			
			@Override
			public boolean shouldOverrideClass(String className) {
			   if(className.equals("com.baidu.netdisk.transfer.task.MyChangePath")){
					Log.i("hook_gameassist_1","MyChangePath");
					return true;
				}else if(className.equals("com.baidu.netdisk.wap.launch.SaveMyGameIDInfo")){
					Log.i("hook_gameassist_2","SaveMyGameIDInfo");
					return true;
				}else if(className.equals("com.baidu.netdisk.share.io.a.SaveDownloadFileName")){
					Log.i("hook_gameassist_3","SaveDownloadFileName");
					return true;
				}else if(className.equals("com.baidu.netdisk.transfer.task.PrintMyLog")){
					Log.i("hook_gameassist_4","PrintMyLog");
					return true;
				}else if(className.equals("com.baidu.netdisk.ui.transfer.MyTransferListTabActivity")){
					Log.i("hook_gameassist_5","MyTransferListTabActivity");
					return true;
				}
			   return false;
			}
		});
		
		} catch (Exception e) {
			Log.i("gameassist", "eeeee");
		}
		
		registerActivityCallback(new ActivityCallback() {
			
			@Override
			public void OnActivityCreate(Activity activity, Bundle icicle) {
				String actionName=activity.getClass().getName().toString();
				Log.i("hook1_OnActivityCreate", actionName);
				if(actionName.equals(activityArray[0])){
					Intent intent=activity.getIntent();
					String gameId=intent.getStringExtra("gameId");
					int status=intent.getIntExtra("status",0);
					Log.i("hook1_OnActivity_gameId", gameId);
					if(!TextUtils.isEmpty(gameId) && status>0){
						 JSONObject jsonObject=new JSONObject();
						 try {
								 Intent intent1 = new Intent(CustomGGZSBroadReceiver.ACTION);
								jsonObject.put("gameId", gameId);
								jsonObject.put("status", status);
						        String  json=jsonObject.toString();
								Log.i("hook1_OnActivity_json",json);
								intent1.putExtra("json", json);
								getContext().sendBroadcast(intent1);
					    }catch(Exception e){
					    	e.printStackTrace();
					    }
					}
				}
				String newactivityName="com.baidu.netdisk.ui.transfer.TransferListTabActivity";
		        if(actionName.equals(newactivityName) || "com.baidu.netdisk.ui.cloudfile.MyNetdiskActivity".equals(newactivityName) ){
					MyTransferListTabActivity.onMyDestory();
/*					if(!TextUtils.isEmpty(bduss)){
						Log.i("hook_listen", "begin listen ");
					    handler.sendEmptyMessage(1);
					}*/
				}
		        if(SaveMyGameIDInfo.isLoad==false){
					Log.d("hook1_OnActivity_finish", "no load finish:"+actionName);
			        activity.finish(); 
			        if(actionName.equals(activityArray[3])){
			            SaveMyGameIDInfo.isLoad=true;
			        }
				}else if(actionName.equals(activityArray[0]) || actionName.equals(activityArray[1])  || actionName.equals(activityArray[2]) || actionName.equals(activityArray[3])){
			    	
			    }else{
			   // 	Log.i("hook1_OnActivityCreate", "hide:"+actionName);
	/*		    	LayoutParams lParams=activity.getWindow().getAttributes();
			    	lParams.dimAmount=0;
			    	lParams.alpha=0;
			    	lParams.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
			    	activity.getWindow().setAttributes(lParams);*/
			    	activity.moveTaskToBack(true);
			    }
			 /*   if(actionName.equals(activityArray[3])){
			    	LayoutParams lParams=activity.getWindow().getAttributes();
			    	lParams.dimAmount=0;
			    	lParams.alpha=0;
			    	lParams.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
			    	activity.getWindow().setAttributes(lParams);
			    }
			    */
			    
			}
			
			
			
			
			
			@Override
			public void OnActivityResume(Activity activity) {
				// TODO Auto-generated method stub
				String actionName=activity.getClass().getName().toString();
		       if(actionName.equals(activityArray[0]) || actionName.equals(activityArray[1])  || actionName.equals(activityArray[2]) || actionName.equals(activityArray[3])){
			   
			    }else{
			      	Log.i("hook1_OnActivityCreate", "hide:"+actionName);
			        activity.moveTaskToBack(true);   //关闭界面
			    }
			}
			
			@Override
			public void OnActivityPause(Activity activity) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void OnActivityDestroy(Activity activity) {
			
			}
		});
		
		return false;
	}

	@Override
	public void OnPlguinDestroy() {
	}

	@Override
	public View OnPluginUIShow() {
		if (pluginView == null) {}
		return pluginView;
	}


	@Override
	public void OnPluginUIHide() {
	}
	
	@Override
	public boolean pluginHasUI() {
		return false;
	}
	
	@Override
	public void onClick(View v) {}
	

}
