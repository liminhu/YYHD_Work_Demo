package com.gameassist.plugin.wechat.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.gameassist.plugin.bean.RcontactTb;
import com.gameassist.plugin.common.DateBaseMonitor;
import com.gameassist.plugin.common.InitDataManager;
import com.gameassist.plugin.mm.robot.PluginEntry;
import com.gameassist.plugin.utils.MyLog;
import com.gameassist.plugin.wechat.exception.WechatException;
import com.gameassist.plugin.wechat.model.WechatMeta;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by hulimin on 2017/10/18.
 */

public class CommonUtils {
    public static final String LOGIN_URL= "https://login.weixin.qq.com/cgi-bin/mmwebwx-bin/login";



    public static final String[] SYNC_HOST = {
            "webpush.weixin.qq.com",
            "webpush2.weixin.qq.com",
            "webpush.wechat.com",
            "webpush1.wechat.com",
            "webpush2.wechat.com",
            "webpush1.wechatapp.com",

            "wx2.qq.com",
            "webpush.wx2.qq.com",
            "wx8.qq.com",
            "webpush.wx8.qq.com",
            "qq.com",
            "web2.wechat.com",
            "webpush.web2.wechat.com",
            "wechat.com",
            "webpush.web.wechat.com",
            "webpush.wx.qq.com",
            "webpush2.wx.qq.com"
    };

    public static String getCurrentTime() {
        Long time=System.currentTimeMillis();
        return ""+(time/1000);
    }

    public static String genLoginUrlByUUID(String uuid) {
         StringBuilder sb=new StringBuilder();
        sb.append(LOGIN_URL);
        sb.append("?tip=1&uuid="+uuid+"&_="+getCurrentTime());
        return sb.toString();
    }



    public static String genSyncUrl(String syncUrlHost) {
        return "https://" + syncUrlHost + "/cgi-bin/mmwebwx-bin/synccheck";
    }

    public static String genInitWebwxUrl(WechatMeta urlBean) {
        StringBuilder sb=new StringBuilder();
        sb.append(urlBean.base_uri);
        sb.append("/webwxinit?pass_ticket=");
        sb.append(urlBean.pass_ticket);
        sb.append("&skey=");
        sb.append(urlBean.skey);
        sb.append("&r=");
        String currentTime=getCurrentTime();
        sb.append(currentTime);
        String result=sb.toString();
        MyLog.e("genInitWebwxUrl : "+result);
        return result;
    }





    public static boolean parserDocument(String srcData, WechatMeta wechatMeta) {
        try {
            MyLog.e(srcData);
            DocumentBuilder db= DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc=db.parse(new InputSource(new ByteArrayInputStream(srcData.getBytes("utf-8"))));
            Element root=doc.getDocumentElement();
            NodeList nodeList=root.getChildNodes();
            //	System.out.println("1111" +"\t"+nodeList.getLength());
            for(int i=0; i<nodeList.getLength(); i++) {
                Node node=nodeList.item(i);
                String nodeName=node.getNodeName();
                if(node.getNodeName().equals(WechatMeta.PASS_TICKET)) {
                    wechatMeta.pass_ticket=node.getFirstChild().getNodeValue();
                }else if(node.getNodeName().equals(WechatMeta.WXUIN)) {
                    wechatMeta.wxuin=node.getFirstChild().getNodeValue();
                }else if(node.getNodeName().equals(WechatMeta.WXSID)) {
                    wechatMeta.wxsid=node.getFirstChild().getNodeValue();
                }else if(node.getNodeName().equals(WechatMeta.SKEY)) {
                    wechatMeta.skey=node.getFirstChild().getNodeValue();
                }
            }
        } catch (Exception e) {

        }
        if(wechatMeta.pass_ticket!=null && wechatMeta.wxsid!=null && wechatMeta.wxuin!=null && wechatMeta.skey!=null) {
            wechatMeta.deviceID=genRandomDeviceId(15);
            MyLog.e(wechatMeta.toString());
            return  true;
        }
        return false;
    }




    public static String genRandom(int len) {
        Random rand = new Random();
        StringBuilder sb=new StringBuilder();
        for(int i=0; i<15; i++) {
            int num = rand.nextInt(10); //生成0-100以内的随机数
            sb.append(""+num);
        }
        return sb.toString();
    }
    public static String  genRandomDeviceId(int length) {
        StringBuilder sb=new StringBuilder("e");
        sb.append(genRandom(length));
        MyLog.e("genRandomDeviceId  -- "+sb.toString());
        return sb.toString();
    }



    public static String genContactUrl(WechatMeta urlBean) {
        StringBuilder sb=new StringBuilder();
        sb.append(urlBean.base_uri);
        sb.append("/webwxgetcontact?pass_ticket=");
        sb.append(urlBean.pass_ticket);
        sb.append("&skey=");
        sb.append(urlBean.skey);
        sb.append("&r=");
        String currentTime=getCurrentTime();
        sb.append(currentTime);
        String result=sb.toString();
        MyLog.e("genContactUrl : "+result);
        return result;
    }




    public static JSONObject getInitWebwxParmentJsonStr(WechatMeta wechatMeta) {
      //  MyLog.e("getInitWebwxParmentJsonStr ****");
        JSONObject js=new JSONObject();
        try {
            js.put("Uin", Long.valueOf(wechatMeta.wxuin));
            js.put("Skey", wechatMeta.skey);
            js.put("Sid", wechatMeta.wxsid);
            js.put("DeviceID", wechatMeta.deviceID);
           // MyLog.e(js.toString());
            // result.put(urlBean.base_request, result);
        } catch (Exception e) {
            // TODO: handle exception
            MyLog.e("exception --- "+e.getMessage());
        }
        return js;
    }




    public static boolean copyFile(InputStream inStream, String fileName){
        deleteAllGitFile();
        File f=new File(fileName);
        try {
            if(!f.exists()){
                f.createNewFile();
                MyLog.e("文件已经创建。。。"+f.getAbsolutePath());
            }
            String newPath=f.getAbsolutePath();
            MyLog.e("newPath : "+newPath);
            int bytesum = 0;
            int byteread = 0;
            FileOutputStream fs = new FileOutputStream(newPath);
            byte[] buffer = new byte[2048];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread; // 字节数 文件大小
                //System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
            inStream.close();
            MyLog.e("file size --- "+bytesum);
            return true;
        } catch (Exception e) {
            MyLog.e("复制单个文件操作出错"+ e.getMessage());
        }
        return false;
    }


    public static void  deleteAllGitFile() {
        File f = new File(Environment.getExternalStorageDirectory(), "/aaa");
        String[] str=f.list();
        for(int i=0; i<str.length; i++){
            if(str[i].contains(".gif")){
                MyLog.e("delete -- "+str[i]);
                File newFile=new File(f, str[i]);
                newFile.delete();
            }
        }
    }




     public static String getChatRoomNickName(Context context, Activity currentActivity) {
         PluginEntry.currentChatRoomName = InitDataManager.getCurrentChatRoomNameByActivity(currentActivity);
         if (  PluginEntry.currentChatRoomName == null || !  PluginEntry.currentChatRoomName.contains("@chatroom")) {
             Toast.makeText(context, "请先进入聊天群界面操作。。。", Toast.LENGTH_SHORT).show();
         } else {
             DateBaseMonitor dateBaseMonitor = new DateBaseMonitor(context);
             RcontactTb rcontactTb = RcontactTb.getChatroomNickNameByUserNameFromDb(  PluginEntry.currentChatRoomName, dateBaseMonitor);
             return rcontactTb.nickname;
         }
         return null;
     }
}
