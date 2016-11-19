package com.baidu.netdisk.transfer.task;

import android.os.Handler;

public class MyGetHandler {
	private  static Handler handler; 
	
	public static void getHandler(Handler handler){
		MyGetHandler.handler=handler;
	}

	
	public static Handler getDeleteHandler(){
		return handler;
	}
	
}
