package com.gameassist.plugin.wechat.exception;

import com.gameassist.plugin.utils.MyLog;

import java.io.IOException;

public class WechatException extends RuntimeException{
	
	private static final long serialVersionUID = 209248116271894410L;

	public WechatException() {
		super();
	}
	
	public WechatException(String message) {
		super(message);
		MyLog.e(message);
	}

	public WechatException(Throwable cause) {
		super(cause);
		MyLog.e(cause.getMessage());
	}

}

