package com.gameassist.plugin.wechat.model;

import com.gameassist.plugin.utils.MyLog;
import com.gameassist.plugin.wechat.exception.WechatException;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class WechatUploadEmotion {
	public static String upload(String filePath, WechatMeta urlBean) {
	//	MyLog.e("*******************************");
		MyLog.e("SendImagUtils --- [*] 测试上传消息表情给微信 ... "+filePath);

		String domain="wx2";
		String uIn =urlBean.wxuin;
    	String sId =urlBean.wxsid;
    	String sKey =urlBean.skey;
    	String deviceid =urlBean.deviceID;
    	String webwxDataTicket =urlBean.webwx_data_ticket;
    	String passTicket =  urlBean.pass_ticket;
    	String fromUserName = urlBean.user.UserName;

		MyLog.e(webwxDataTicket+"---- webwx_data_ticket");
		MyLog.e(fromUserName+"---- fromUserName");
    	String response = null;
    	InputStream inputStream = null;
    	InputStreamReader inputStreamReader = null;
    	BufferedReader bufferedReader = null;
    	HttpsURLConnection conn = null;
    	try {
    		File file = new File(filePath);
    		if (!file.exists() || !file.isFile()) {
				throw new WechatException("文件不存在");
    		}

			FileInputStream fis = new FileInputStream(filePath);
			MyLog.e("正在上传。。。。 %s, --- "+webwxDataTicket,fis.available()+ ""+file.length());
			//请求头参数
			String boundary = "---------------------------16968206128770"; //区分每个参数之间
			String freFix = "--";
			String newLine = "\r\n";
			URL urlObj = new URL("https://file."+domain+".qq.com/cgi-bin/mmwebwx-bin/webwxuploadmedia?f=json");
			conn = (HttpsURLConnection) urlObj.openConnection();
    		conn.setRequestMethod("POST");
    		conn.setDoOutput(true);
    		conn.setDoInput(true);
    		conn.setConnectTimeout(5000);  
    		conn.setUseCaches(false);
    		conn.setRequestMethod("POST");
    		conn.setRequestProperty("Host", "file."+domain+".qq.com");
    		conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0");
    		conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
    		conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
    		conn.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
    		conn.setRequestProperty("Referer", "https://."+domain+".qq.com/?&lang=zh_CN");
    		conn.setRequestProperty("Content-Type", "multipart/form-data; boundary="+boundary);
    		conn.setRequestProperty("origin", "https://."+domain+"..qq.com/");
    		conn.setRequestProperty("Connection", "Keep-Alive");
    	//	conn.setRequestProperty("Cookie", wechatMeta.getCookie());
			MyLog.e("*******************************  请求主体");
			// 请求主体
			StringBuffer sb = new StringBuffer();

			sb.append(freFix+boundary).append(newLine); //这里注意多了个freFix，来区分去请求头中的参数
			sb.append("Content-Disposition: form-data; name=\"id\"").append(newLine);
			sb.append(newLine);

			sb.append("WU_FILE_1").append(newLine);
			sb.append(freFix+boundary).append(newLine);
			sb.append("Content-Disposition: form-data; name=\"name\"").append(newLine);
			sb.append(newLine);

			sb.append(file.getName()).append(newLine);
			sb.append(freFix+boundary).append(newLine);
			sb.append("Content-Disposition: form-data; name=\"type\"").append(newLine);
			sb.append(newLine);

			sb.append("image/jpeg").append(newLine);
			sb.append(freFix+boundary).append(newLine);
			sb.append("Content-Disposition: form-data; name=\"lastModifiedDate\"").append(newLine);
			sb.append(newLine);

			sb.append("Tue Feb 14 2017 22:07:03 GMT+0800").append(newLine);
			sb.append(freFix+boundary).append(newLine);
			sb.append("Content-Disposition: form-data; name=\"size\"").append(newLine);
			sb.append(newLine);

			sb.append(file.length()).append(newLine);
			sb.append(freFix+boundary).append(newLine);
			sb.append("Content-Disposition: form-data; name=\"mediatype\"").append(newLine);
			sb.append(newLine);

			sb.append("pic").append(newLine);
			sb.append(freFix+boundary).append(newLine);
			sb.append("Content-Disposition: form-data; name=\"uploadmediarequest\"").append(newLine);
			sb.append(newLine);

			sb.append("{\"UploadType\":2,\"BaseRequest\":{\"Uin\":"+uIn+",\"Sid\":\""+sId+"\",\"Skey\":\""+sKey+"\",\"DeviceID\":\""+deviceid+"\"},\"ClientMediaId\":"+System.currentTimeMillis()+",\"TotalLen\":"+file.length()+",\"StartPos\":0,\"DataLen\":"+file.length()+",\"MediaType\":4,\"FromUserName\":\""+fromUserName+"\",\"ToUserName\":\""+"ToUserName"+"\",\"FileMd5\":\"7a392dfff5a45cc29d494434a1fbaf15\"}").append(newLine);
			sb.append(freFix+boundary).append(newLine);
			sb.append("Content-Disposition: form-data; name=\"webwx_data_ticket\"").append(newLine);
			sb.append(newLine);

			sb.append(webwxDataTicket).append(newLine);
			sb.append(freFix+boundary).append(newLine);
			sb.append("Content-Disposition: form-data; name=\"pass_ticket\"").append(newLine);
			sb.append(newLine);

			sb.append(passTicket).append(newLine);
			sb.append(freFix+boundary).append(newLine);
			sb.append("Content-Disposition: form-data; name=\"filename\"; filename=\""+file.getName()+"\"").append(newLine);
			sb.append("Content-Type: application/octet-stream");
			sb.append(newLine);
			sb.append(newLine);

			MyLog.e("%d, available -%d, %d", file.length(), fis.available(), sb.length());
		//	conn.setRequestProperty("Content-Length", ""+file.length());   不能加上这个限制

			MyLog.e("***  请求主体 ---- 1111 ");
			OutputStream outputStream = new DataOutputStream(conn.getOutputStream());
			outputStream.write(sb.toString().getBytes("utf-8"));//写入请求参数

			DataInputStream dis = new DataInputStream(new FileInputStream(file));
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = dis.read(bufferOut)) != -1) {
				outputStream.write(bufferOut,0,bytes);//写入图片
			}
			outputStream.write(newLine.getBytes());
			outputStream.write((freFix+boundary+freFix+newLine).getBytes("utf-8"));//标识请求数据写入结束
			dis.close();
			outputStream.close();
			MyLog.e("***  请求主体 ---- 2222 ");
			//读取响应信息
			inputStream = conn.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			response = buffer.toString();
			MyLog.e("SendImagUtils ---- response++++++++"+response);
		} catch (Exception e) {
			MyLog.e("Exception -- "+e.getMessage());
		}finally{
			if(conn!=null){
				conn.disconnect();
			}
			try {
				if(bufferedReader!=null){
					bufferedReader.close();
				}
			    if(inputStreamReader!=null){
					inputStreamReader.close();
					inputStream.close();
				}
			} catch (IOException execption) {
               MyLog.e(execption.getMessage());
			}
		}
    	return response;
    }

}
