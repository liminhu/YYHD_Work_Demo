package com.gameassist.plugin.wechat.service;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.Message;
import android.text.TextUtils;

import com.gameassist.plugin.common.SendManager;
import com.gameassist.plugin.mm.robot.PluginEntry;
import com.gameassist.plugin.utils.FileUtils;
import com.gameassist.plugin.utils.MyLog;
import com.gameassist.plugin.wechat.cookie.PersistentCookieStore;
import com.gameassist.plugin.wechat.exception.WechatException;
import com.gameassist.plugin.wechat.model.WechatGroup;
import com.gameassist.plugin.wechat.model.WechatMeta;
import com.gameassist.plugin.wechat.model.WechatSendMessage;
import com.gameassist.plugin.wechat.model.WechatUploadEmotion;
import com.gameassist.plugin.wechat.utils.CommonUtils;
import com.gameassist.plugin.wechat.utils.OkhttpsCookieUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WechatServiceImpl implements WechatService {
	private static final String APPID="wx782c26e4c19acffb";   //注意appId不能错，否则请求不数据
	private static final String LANG="zh_CN";
	private static final String FUN="new";

	private static final String APPID_KEY="appid";
	private static final String FUN_KEY="fun";
	private static final String LANG_KEY="lang";
	private static final String TIME_KEY="_";
	private static final String UUID_URL= "https://login.weixin.qq.com/jslogin";
    private OkhttpsCookieUtils okhttpsCookieUtils;
    private PersistentCookieStore persistentCookieStore;

	public WechatServiceImpl(PersistentCookieStore persistentCookieStore) {
		this.persistentCookieStore=persistentCookieStore;
		okhttpsCookieUtils=new OkhttpsCookieUtils();
	}

	@Override
	public String getUUID() throws WechatException {
		Map<String,String> map=new HashMap<>();
		map.put(APPID_KEY, APPID);
		map.put(FUN_KEY, FUN);
		map.put(LANG_KEY, LANG);
		map.put(TIME_KEY, CommonUtils.getCurrentTime());
		return okhttpsCookieUtils.getUUID(UUID_URL, map);
	}


	@Override
	public String loginWx(String url, int tip) throws WechatException {
		if(tip==1){
			//传入uuid
			url=CommonUtils.genLoginUrlByUUID(url);
			return okhttpsCookieUtils.get(url);
		}else {
			MyLog.e(url);
		    return  okhttpsCookieUtils.getWithCookieLogin(url,persistentCookieStore);
		}
	}

	@Override
	public String wxInit(WechatMeta wechatMeta) throws WechatException {
		String url=CommonUtils.genInitWebwxUrl(wechatMeta);
		return  okhttpsCookieUtils.getInitWebwxWithCookie(url,persistentCookieStore,wechatMeta);
	}



	@Override
	public List<WechatGroup> getWechatGroup(WechatMeta wechatMeta) throws WechatException {
        String url= CommonUtils.genContactUrl(wechatMeta);
		String data=okhttpsCookieUtils.getInitWebwxWithCookie(url,persistentCookieStore,wechatMeta);
		//MyLog.e(data);
		List<WechatGroup> list= WechatGroup.getGroupBeanList(data);
		return list;
	}


	@Override
	public int[] syncCheck(String url, WechatMeta wechatMeta) throws WechatException {
		JSONObject js=new JSONObject();
		String time=CommonUtils.getCurrentTime();
		int[] arr = new int[] { -1, -1 };
		try{
			js.put("r", time);
			js.put("sid", wechatMeta.wxsid);
			js.put("uin", wechatMeta.wxuin);
			js.put("deviceid", wechatMeta.deviceID);
			js.put("skey", wechatMeta.skey);
			js.put("synckey", wechatMeta.user.SyncKey);
			js.put(TIME_KEY,time);
			String data=okhttpsCookieUtils.getDataWebwxWithCookie(url, js, persistentCookieStore);
			if(TextUtils.isEmpty(data)){
				return arr;
			}else {
				MyLog.e("syncCheck ----- 11111 --" +data);
			}
		}catch (Exception e){
			MyLog.e("syncCheck -----" +e.getMessage());
		}
		return arr;
	}

	@Override
	public JSONObject webwxsync(WechatMeta meta) throws WechatException {

		return null;
	}




	@Override
	public String  handleTextMsg(WechatMeta wechatMeta, String content) throws WechatException{
		if(TextUtils.isEmpty(content)){
			MyLog.e("  心跳测试。。。");
		}
		JSONObject msg= WechatSendMessage.getTextMessageParmentJsonStr(wechatMeta, content);
        String url=WechatSendMessage.genSendTextWebwxUrl(wechatMeta);
		JSONObject baseRequest=CommonUtils.getInitWebwxParmentJsonStr(wechatMeta);
		String result=okhttpsCookieUtils.sendDataWebwxWithCookie(url, msg, baseRequest, persistentCookieStore);
		//MyLog.e(result);
		try{
			JSONObject js=new JSONObject(result);
			JSONObject baseResponse=js.getJSONObject("BaseResponse");
			int Ret=baseResponse.getInt("Ret");
			if(Ret==0) {
				MyLog.e(baseResponse.toString());
				return result;
			}
		}catch (Exception e){
		}
		return null;
	}




	@Override
	public String  handleEmotionMsg(Context context, WechatMeta wechatMeta, String md5) throws WechatException{
		final String file = md5 + ".gif";
		File f = new File(Environment.getExternalStorageDirectory(), "/aaa/"+file);
		MyLog.e("view--  name:"+f.getAbsolutePath());
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}
		try {
			InputStream is = context.getAssets().open(file);
			boolean flag=CommonUtils.copyFile(is, f.getAbsolutePath());
			if(flag){
				String  result= WechatUploadEmotion.upload(f.getAbsolutePath(), wechatMeta);
				MyLog.e("SendEmotionBean.result()  ********************** "+result);
				//{"BaseResponse": {"Ret": 0,"ErrMsg": ""},"MediaId": "@crypt_5c691f2e_e9312ae74a5a1c24f9c5cf29fadb3af67342db9af0ab6aed978d7a456a7824a3e035715ea6481f14174a48bcdc16574e97acdb1eca1a1505eb66a731366af2e48aa8aa2732442280ae7ef66320946590fd516501c40fe661bf875b4f1adca52a5d065d050acec87016dc79475f819a29abb96e32faca35bcfd59c261de6333595c53233ec275220f0a8136dd860cb8c270f01a38aea1ce8e3fd9dc717482927b1c7920d7fbda302c577bf5860b51ad3dc621b4a4a17f7c5a4acb6e7b1ed6189487bf72aa7c1417604e40c0e3d1bcb8a7719c1e2329bef8f6ee232e5be5cc397542798140e683fc6f8c816d7219ff74d6d0df41bf325d7099b723f37017d9d55c66514b02f157cf7f2b27db1716b55b4065b411969f4737d0d792cfb9a3dbfa109ed3739b66fac5a059f0b03444c3366173d8454f15635bc53553e2d4e1584708","StartPos": 19815,"CDNThumbImgHeight": 100,"CDNThumbImgWidth": 100}
				try{
					if(!TextUtils.isEmpty(result)){
						MyLog.e("国片上传成功。。。");
						JSONObject data=new JSONObject(result);
						JSONObject baseResponse=data.getJSONObject("BaseResponse");
						int Ret=baseResponse.getInt("Ret");
						if(Ret!=0) {
							MyLog.e("国片上传失败。。。");
							MyLog.e(baseResponse.toString());
							return null;
						}
						String media_id=data.getString("MediaId");
						MyLog.e("国片上传成功。。。"+media_id);
						String url=WechatSendMessage.genWebwxSendEmoticonUrl(wechatMeta);
						JSONObject baseRequest=CommonUtils.getInitWebwxParmentJsonStr(wechatMeta);
						JSONObject msg= WechatSendMessage.getEmotionParmentJsonStr(wechatMeta, media_id);
					//	MyLog.e(msg.toString());
						result=okhttpsCookieUtils.sendDataWebwxWithCookie(url, msg, baseRequest, persistentCookieStore);
                        JSONObject js=new JSONObject(result);
						baseResponse=js.getJSONObject("BaseResponse");
						Ret=baseResponse.getInt("Ret");
						if(Ret==0) {
							MyLog.e(baseResponse.toString());
							return result;
						}
						//MyLog.e(result);
						return null;
					}else{
						MyLog.e("国片上传失败。。。");
					}
				}catch (Exception e){
					MyLog.e(e.getMessage());
				}
			}
		}catch (Exception e){
			MyLog.e(e.getMessage());
		}
		return null;
	}



    @Override
	public void choiceSyncLine(WechatMeta wechatMeta) throws WechatException {
		boolean enabled = false;
		int i=0;
		for (String syncUrl : CommonUtils.SYNC_HOST) {
			String url = CommonUtils.genSyncUrl(syncUrl);
			i++;
			MyLog.e("心跳测试-%d。。。 "+url, i);
			int[] res = syncCheck(url, wechatMeta);
			if (res[0] == 0) {
				wechatMeta.setWebpush_url(url);
				MyLog.e("选择线路：[{}]" + syncUrl);
				enabled = true;
				break;
			}
			try{
				Thread.sleep(2000);
			}catch (Exception e){
				MyLog.e(e.getMessage());
			}
		}
		if (!enabled) {
			throw new WechatException("同步线路不通畅");
		}
	}

}
