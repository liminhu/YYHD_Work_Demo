package com.gameassist.plugin.wechat.service;


import android.content.Context;

import com.gameassist.plugin.wechat.exception.WechatException;
import com.gameassist.plugin.wechat.model.WechatGroup;
import com.gameassist.plugin.wechat.model.WechatMeta;

import org.json.JSONObject;

import java.util.List;

public interface WechatService {


	String loginWx(String url, int tip)throws WechatException;


	/**
	 * 获取UUID
	 * @return
	 */
	String getUUID() throws WechatException;

	/**
	 * 微信初始化
	 * @param wechatMeta
	 * @throws WechatException
	 */
	String wxInit(WechatMeta wechatMeta) throws WechatException;


	/**
	 * 获取联系人
	 * @param wechatMeta
	 * @return
	 */
	public List<WechatGroup> getWechatGroup(WechatMeta wechatMeta) throws WechatException;



	/**
	 * 消息检查
	 * @param wechatMeta
	 * @return
	 */
	int[] syncCheck(String url, WechatMeta wechatMeta) throws WechatException;



	/**
	 * 处理聊天信息返回图片
	 */
	public String  handleTextMsg(WechatMeta wechatMeta, String content) throws WechatException;

	public String  handleEmotionMsg(Context context, WechatMeta wechatMeta, String md5) throws WechatException;

	public void choiceSyncLine(WechatMeta wechatMeta) throws WechatException;

	/**
	 * 获取最新消息
	 * @param meta
	 * @return
	 */
	JSONObject webwxsync(WechatMeta meta) throws WechatException;




}
