package com.baidu.netdisk.share.io.a;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class SaveDownloadFileName {
	private static Context context;
	public static String gameId="";

	public static void setContext(Context context){
		SaveDownloadFileName.context=context;
	}
	
    public static void saveGGZSDownloadFileName(String shareListParserPath){
    	 Log.v("hook_DownloadFileName", gameId);
    	 if(shareListParserPath!=null && shareListParserPath.length()>0){
    		 if(shareListParserPath.contains("md5")){
    			 String[] data=shareListParserPath.split(",");
    			 String fileMd5=findGGZSDownloadFileMd5(data);
    			     			 
    			 SharedPreferences.Editor sharedata=context.getSharedPreferences("ggzs_gameId_md5",0).edit();
    		     sharedata.putString(fileMd5, gameId);
    		     sharedata.commit();
    		 }
    	 }
    }
    
    private static String findGGZSDownloadFileMd5(String[] array){
    	String fileGGZSDownloadName=null;
    	for(int i=0; i<array.length;i++){
    		String data=array[i];
    		if(data.contains("md5")){
    			String[] temp=data.split(":");
    			fileGGZSDownloadName=temp[1].substring(1,temp[1].length()-1);
    			Log.v("hook_find_file_md5", fileGGZSDownloadName);
    			break;
    		}
    	}
    	return fileGGZSDownloadName;
    }
}
