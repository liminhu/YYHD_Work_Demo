package com.gameassist.plugin.mm.robot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gameassist.plugin.ActivityCallback;
import com.gameassist.plugin.Plugin;
import com.gameassist.plugin.bean.ChatroomTb;
import com.gameassist.plugin.bean.InstructionsBean;
import com.gameassist.plugin.bean.MessageTb;
import com.gameassist.plugin.bean.RcontactTb;
import com.gameassist.plugin.bean.SendMessage;
import com.gameassist.plugin.common.CommonData;
import com.gameassist.plugin.common.DateBaseMonitor;
import com.gameassist.plugin.common.InitDataManager;
import com.gameassist.plugin.common.SendManager;
import com.gameassist.plugin.common.UrlManager;
import com.gameassist.plugin.hongbao.HongBaoView;
import com.gameassist.plugin.hongbao.WalletLuckyMoneyDetail;
import com.gameassist.plugin.utils.DialogUtils;
import com.gameassist.plugin.utils.HttpUtils;
import com.gameassist.plugin.utils.MyLog;
import com.gameassist.plugin.utils.ProcessUtils;
import com.gameassist.plugin.utils.SharedPreferenceUtils;
import com.gameassist.plugin.utils.TestUtils;
import com.gameassist.plugin.websocket.WebSocketService;
import com.gameassist.plugin.wechat.cookie.PersistentCookieStore;
import com.gameassist.plugin.wechat.model.WechatGroup;
import com.gameassist.plugin.wechat.model.WechatMeta;
import com.gameassist.plugin.wechat.service.WechatService;
import com.gameassist.plugin.wechat.service.WechatServiceImpl;
import com.gameassist.plugin.wechat.utils.CommonUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.POWER_SERVICE;

/**
 * Created by hulimin on 2017/9/4.
 */
//todo:需要优化：期间指令的交互 --  通知服务器更改期间指令
//查看微信是怎样生成登录的名字--算法 --- 91c8a5c0e536486eff63cae774335bb2
public class PluginEntry extends Plugin {
    //https://login.weixin.qq.com/cgi-bin/mmwebwx-bin/login?tip=1&uuid=AbtDCTj1ZQ==&_=757250847

    private static final String LIB_HOOK_PLUGIN = "Robot";
    private static boolean HAVE_INIT_WEBSECKET_FLAG = false;  //websecket 是否初始化
    private HashMap<Activity, PluginManagerView> pluginManagerViewHashMap = new HashMap();
    private PluginManagerView pmView;

    public static PluginEntry getInstance() {
        return instance;
    }
    private static PluginEntry instance;
    public static Activity currentActivity;

    public static String currentChatRoomName;   //全局当前群号
    private static Long currentTime = -1L;       //当前时间
    private static DateBaseMonitor dateBaseMonitor;   //数据库操作
    private static ChatroomTb chatroomTb;  //当前群信息
    public static String chatroom_nickname;  //群昵称
    public boolean is_monitor = false;  //是否轮询数据库
    public static int period = -1;
    public static List<SendMessage> sendMessages = new ArrayList<>();  //需要转发的消息
    private static int hongbao_state=-1; //红包状态，0：表示开始， 1表示完成
    public static int versionCode=-1;
    private View pluginView;
    private static String key_sendid;
    private  Intent websecketServiceIntent = null;
    private  boolean isMainProce;
    private  static  SendMessageThread sendThread;

    private  static WechatService webChatService;
    private static PersistentCookieStore persistentCookieStore;


    private Handler mainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    MonitorDatabseThread dbThead = new MonitorDatabseThread();
                    Thread thread = new Thread(dbThead);
                    thread.start();

                    if(sendThread==null){
                        sendThread = new SendMessageThread();
                        Thread thread1 = new Thread(sendThread);
                        thread1.start();
                    }
                    break;


                case 2:
                    MyLog.e("mainHandler .... ");
                    if(sendThread==null){
                        sendThread = new SendMessageThread();
                        Thread thread1 = new Thread(sendThread);
                        thread1.start();
                    }
                    break;

                //红包监听
                case 1:
                    MyLog.e("case 1: -- 红包监听");
                    break;
            }
        }
    };

    private PowerManager.WakeLock m_wklk;
    private static  String uuid;
    private  static WechatMeta wechatMeta;
    private static   List<WechatGroup>  wechatGroupList, contactList;
    public static String ToUserName="";
    public static  boolean isChoiceSyncLine=false;
    public static  int Web_weatChat_IsLogin=0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            MyLog.e("handld = " + msg.what);
            switch (msg.what) {
                case 0:
                    MyLog.e("case 0  ---- 初始化 websecketServiceIntent --- HAVE_INIT_WEBSECKET_FLAG : " + HAVE_INIT_WEBSECKET_FLAG);
                    currentChatRoomName = InitDataManager.getCurrentChatRoomNameByActivity(currentActivity);
                    if (currentChatRoomName == null || !currentChatRoomName.contains("@chatroom")) {
                        Toast.makeText(getTargetApplication(), "请先进入聊天群界面操作。。。", Toast.LENGTH_SHORT).show();
                    }else{
                        if(dateBaseMonitor==null){
                            dateBaseMonitor = new DateBaseMonitor(getTargetApplication());
                        }
                        RcontactTb rcontactTb=RcontactTb.getChatroomNickNameByUserNameFromDb(currentChatRoomName, dateBaseMonitor);
                        chatroom_nickname=rcontactTb.nickname;

                        if(!TextUtils.isEmpty(chatroom_nickname)){
                            ToUserName=WechatGroup.getToUserNameFromGroup(wechatGroupList,contactList, chatroom_nickname);
                            if(!TextUtils.isEmpty(ToUserName) && Web_weatChat_IsLogin==1){
                                if (!HAVE_INIT_WEBSECKET_FLAG) {
                                    MyLog.e("case 1: 获取当前群号 。。。 period ---   " + period + "\t" + currentChatRoomName);
                                    if (websecketServiceIntent == null  && isMainProce) {
                                        //开始websecket
                                        WebSocketService.handler = handler;
                                        WebSocketService.context = getTargetApplication();
                                        websecketServiceIntent = new Intent(getContext(), WebSocketService.class);
                                        getContext().startService(websecketServiceIntent);
                                        //开始连接
                                        WebSocketService.webSocketConnet();
                                        HAVE_INIT_WEBSECKET_FLAG = true;
                                    }
                                }
                                handler.sendEmptyMessageDelayed(1, 3000);
                            }else{
                                Toast.makeText(getTargetApplication(), "获取网页版群信息失败，请稍后再操作。。。", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getTargetApplication(), "群的名称为空，请重命名后再操作。。。", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;


                case 1:   //获取当前群号
                    if (WebSocketService.isClosed) {
                        //Toast.makeText(context, "WebSocketService  is close ! 请退出重试。。。 ", Toast.LENGTH_SHORT).show();
                        DialogUtils.showWebSocketDialog(currentActivity, 1);
                    }else if(is_monitor==false){
                        //获取当前的群聊相关信息
                        chatroomTb = dateBaseMonitor.getChatRoomInfoByUserName(DateBaseMonitor.CHATROOM_TB_NAME, currentChatRoomName);
                        chatroomTb.updateMemberMap();
                        currentTime = InitDataManager.initBeginMesasgeTime(dateBaseMonitor, currentChatRoomName);
                        is_monitor = true;
                        mainHandler.sendEmptyMessage(0);  //监听数据库变化
                        MyLog.e("case 200  ----  开始第一条指令 -- 更新当前群用户信息");
                        String insturct_0 = InstructionsBean.genFirstBeginInstruct(chatroomTb, 0, "stb", currentChatRoomName);
                        WebSocketService.sendMessage(insturct_0);
                    }else {
                        Toast.makeText(context, "已经在标庄期间。。。 ", Toast.LENGTH_SHORT).show();
                    }
                    break;


                //设置配置
                case 2:
                    if(websecketServiceIntent!=null){
                        WebSocketService.closeWebsocket();
                        getContext().stopService(websecketServiceIntent);
                        websecketServiceIntent=null;
                    }
                    currentChatRoomName = InitDataManager.getCurrentChatRoomNameByActivity(currentActivity);
                    if (currentChatRoomName == null || !currentChatRoomName.contains("@chatroom")) {
                        Toast.makeText(getTargetApplication(), "请先进入聊天群界面操作。。。", Toast.LENGTH_SHORT).show();
                    }else{
                        if(dateBaseMonitor==null){
                            dateBaseMonitor = new DateBaseMonitor(getTargetApplication());
                        }
                        RcontactTb rcontactTb=RcontactTb.getChatroomNickNameByUserNameFromDb(currentChatRoomName, dateBaseMonitor);
                        MyLog.e("username:%s, nickname:%s",rcontactTb.username, rcontactTb.nickname);
                        chatroom_nickname=rcontactTb.nickname;
                        //开始websecket
                        WebSocketService.handler = handler;
                        WebSocketService.context = getTargetApplication();
                        websecketServiceIntent = new Intent(getContext(), WebSocketService.class);
                        getContext().startService(websecketServiceIntent);
                        //开始连接
                        WebSocketService.webSocketConnet();
                        HAVE_INIT_WEBSECKET_FLAG = true;
                        handler.sendEmptyMessageDelayed(3, 3000);
                    }
                    break;
                case 3:
                    if (WebSocketService.isClosed) {
                        DialogUtils.showWebSocketDialog(currentActivity, 3);
                    }else{
                        Toast.makeText(getTargetApplication(), "已连上服务器，请到后台更新配置。。。", Toast.LENGTH_SHORT).show();
                    }
                    break;


                //发送指令
                case 5:
                    MessageTb bean5 = (MessageTb) msg.obj;
                    MyLog.e("case 5: -- 发送指令" + bean5.genJSONData());
                    String user_name = RcontactTb.getLoginWeixinUsername(getTargetApplication());
                    if(TextUtils.isEmpty(user_name)) {
                        user_name=chatroomTb.getRoom_owner();
                    }
                    String nick_name = chatroomTb.getMap().get(user_name);
                    String code = bean5.getContent();
                    MyLog.e("case 5:" + code + " user_name : " + user_name + ": time-- " + bean5.getCreateTime());


                    // TODO: 2017/9/26  优化取发送者的username,默认为群主
                    if (bean5.getType() == 1) {
                        String username=ChatroomTb.getUserNameFormContent(bean5.getContent(), user_name);
                        user_name=username;
                        nick_name=chatroomTb.getMap().get(user_name);
                        if (bean5.getContent().contains(":\n")) {
                            String[] temp_name = bean5.getContent().split(":\n");
                            code = temp_name[1];
                        }
                    }

                    InstructionsBean instructionsBean = new InstructionsBean(bean5.getInstruct_type(), code, currentChatRoomName, user_name, nick_name, bean5.getCreateTime());
                    if (bean5.getType() == 436207665) {   //红包类型
                        instructionsBean.setCode("srp");
                        key_sendid = WalletLuckyMoneyDetail.getSendidFromContent(bean5.getContent());
                        user_name = WalletLuckyMoneyDetail.getSendUserNameFromContent(bean5.getContent());
                        nick_name = chatroomTb.getMap().get(user_name);
                        instructionsBean.setUser_name(user_name);
                        instructionsBean.setNick_name(nick_name);
                        instructionsBean.setKey_sendid(key_sendid);
                    } else if (bean5.getType() == 10000) {
                        instructionsBean.setCode("cfrp");
                    }
                    String instruct_str = instructionsBean.genSendInstructionJsonString();
                    MyLog.e(instruct_str);
                    WebSocketService.sendMessage(instruct_str);
                    break;


                //优化合并发送的文字与图片
                case 194:
                    InstructionsBean data194 = (InstructionsBean) msg.obj;
                    MyLog.e("msg -- case 194: " + data194.getMsg() );
                    if (data194 != null && !TextUtils.isEmpty(data194.getMsg())) {
                        SendMessage send = new SendMessage(data194.getMsg(), data194.getMsg_type(), 0, data194.getPeriod());
                        sendMessages.add(send);
                        sendThread.sendMyMessage();
                    }
                    break;


                //更新期间指令也放到队列中
                case 201:
                    InstructionsBean bean201 = (InstructionsBean) msg.obj;
                    period = bean201.getPeriod();
                    MyLog.e("case 201: 更新期间指令  -- period:" + period);
                    break;


                case 202:
                    MyLog.e("case 202: 续庄  -- period:" + period);
                    if (period == 5) {
                        InstructionsBean intstruct = new InstructionsBean();
                        intstruct.setInstruct_type(6);
                        intstruct.setGroup_id(currentChatRoomName);
                        intstruct.setCode("xuz");
                        String js202 = intstruct.genSendInstructionJsonString(chatroomTb);
                        MyLog.e(js202);
                        WebSocketService.sendMessage(js202);
                    } else {
                        Toast.makeText(getTargetApplication(), "非续庄期间，操作无效。。。", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case 203:
                    if(!WebSocketService.isClosed){
                        MyLog.e("case 203: 下庄  -- period:" + period + "\t" + currentActivity.toString());
                        InstructionsBean intstruct = new InstructionsBean();
                        intstruct.setInstruct_type(7);
                        intstruct.setGroup_id(currentChatRoomName);
                        intstruct.setCode("xiaz");
                        String js203 = intstruct.genSendInstructionJsonString();
                        MyLog.e(js203);
                        WebSocketService.sendMessage(js203);
                        is_monitor = false;
                    }else {
                        Toast.makeText(getTargetApplication(), "此期间，操作无效。。。", Toast.LENGTH_SHORT).show();
                    }
                    break;



                case 16:
                    try {
                        MyLog.e("case 16:    红包详情 信息。。。 period ---" + period + "\t" + currentActivity.toString());
                        if (period == 4) {
                            WalletLuckyMoneyDetail.initFieldMethodMap();
                            List<WalletLuckyMoneyDetail> list = WalletLuckyMoneyDetail.getCurrentHongBaoDetails(currentActivity);
                            if (list != null && list.size() > 0) {
                                MyLog.e("case 16:  list.size() " + list.size());
                                if (WalletLuckyMoneyDetail.hongbao_is_finish) {
                                    InstructionsBean hongbao = new InstructionsBean();
                                    hongbao.setInstruct_type(5);
                                    hongbao.setGroup_id(currentChatRoomName);
                                    hongbao.setCode("clrp");
                                    final String js = hongbao.genHongBaoInsJsonStr(list);
                                    MyLog.e(js);
                                    hongbao_state=1;
                                    SendManager.onBack();
                                    WebSocketService.sendMessage(js);
                                }
                            }
                        }
                    } catch (Exception e) {
                        MyLog.e("case 16:  ---- " + e.getMessage());
                    }
                    break;
                default:
                    break;


                case 2007:
                    HongBaoView.getHongBaoDetailView(currentActivity, handler, 0);
                    break;







                case 3006:
                    new Thread() {
                        public void run() {
                            while (true){
                                try{
                                    if(isChoiceSyncLine) {
                                       // webChatService.choiceSyncLine(wechatMeta);
                                    }
                                    Thread.sleep(3000);
                                }catch (Exception e){
                                }

                            }
                        };
                    }.start();
                    break;


/*
                case 3007:
                    new Thread() {
                        public void run() {
                           // webChatService.handleTextMsg(wechatMeta, "test -- "+System.currentTimeMillis());
                            try {
                                if(isChoiceSyncLine==false){
                                    isChoiceSyncLine=true;
                                   // sendEmptyMessage(3006);
                                }
                               // webChatService.choiceSyncLine(wechatMeta);
                               Thread.sleep(1000);
                               webChatService.handleEmotionMsg(getContext(), wechatMeta, "03190e70835a35ed1b2b066f415f9eb0");  //03190e70835a35ed1b2b066f415f9eb0  1df16adb7f44e4950a8c3684e9c035a8
                            }catch (Exception e){

                            }
                        };
                    }.start();
                    break;

*/




                case 3005:
                    Web_weatChat_IsLogin=1;
                    showToastMsg("网页版微信已经登录, 请重新标庄。。。");
                    mainHandler.sendEmptyMessage(2);
                    break;





                case 3003:
                    if(TextUtils.isEmpty(wechatMeta.base_uri)){
                        sendEmptyMessage(2005);
                    }else{
                        Web_weatChat_IsLogin=2;
                        MyLog.e("初始化微信。。。 ");
                        new Thread() {
                            public void run() {
                                String data=webChatService.wxInit(wechatMeta);
                                if(!TextUtils.isEmpty(data)){
                                    try{
                                        JSONObject js=new JSONObject(data);
                                        JSONObject baseResponse=js.getJSONObject("BaseResponse");
                                        int Ret=baseResponse.getInt("Ret");
                                        if(Ret!=0) {
                                            // MyLog.e(baseResponse.toString());
                                            Web_weatChat_IsLogin=0;
                                            sendEmptyMessage(2005);
                                        }else{
                                            //   MyLog.e("-----"+data);
                                            contactList=WechatGroup.getContactList(data);
                                            wechatMeta.user.initWechatUser(data);
                                            wechatGroupList=webChatService.getWechatGroup(wechatMeta);
                                            sendEmptyMessage(3005);
                                        }
                                    }catch (Exception e){

                                    }
                                }
                            };
                        }.start();
                    }
                    break;


                //获取登录信息
                case 3002:
                    String redirect_uri=(String)msg.obj;
                    wechatMeta=new WechatMeta(redirect_uri+"&fun=new");
                    new Thread() {
                        public void run() {
                           String data=webChatService.loginWx(wechatMeta.redirect_uri, 0);
                           if(!TextUtils.isEmpty(data)){
                               CommonUtils.parserDocument(data,wechatMeta);
                               wechatMeta.saveWechatMetaToSharedPre(getTargetApplication());
                               sendEmptyMessage(3003);
                           }
                        };
                    }.start();
                    break;


//这个是微信在8月左右版本更新的限制，就是让所有扫码登录的码，只能通过app扫一扫方式获取，如果图片识别的二维码，直接就跳出去了。原理其实是app做的限制

/*
                case 3000:
                    MyLog.e("测试打开扫码的activie.....");
                    Intent intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setClassName(getTargetApplication().getPackageName(), "com.tencent.mm.plugin.webwx.ui.ExtDeviceWXLoginUI");
                    intent.putExtra("intent.key.title.string","网页版微信登录确认");
                    intent.putExtra("intent.key.login.url","https://login.weixin.qq.com/l/"+uuid);
                    intent.putExtra("intent.key.title.string","网页版微信登录确认");

                    intent.putExtra("intent.key.cancel.string","取消登录");
                    intent.putExtra("intent.key.title.string","网页版微信登录确认");
                    intent.putExtra("intent.key.ok.string","登录");
                    intent.putExtra("intent.key.ok.session.list","5509122859@chatroom,filehelper,newsapp,gh_4cc9ddf0c3b7,officialaccounts,wxid_kw5j68dk4wy022,wxid_83kmdbom0g9a22,4578971958@chatroom");
                    getTargetApplication().startActivity(intent);
                    break;
*/





                case 3010:
                    DialogUtils.showQrcodeDialog(currentActivity, 3001, handler);
                    Web_weatChat_IsLogin=2;
                    new Thread() {
                        public void run() {
                            boolean flag=false;
                            while (!flag){
                                String data=webChatService.loginWx(uuid,1);
                                if(!TextUtils.isEmpty(data)) {
                                    MyLog.e(data);
                                    //showToastMsg("第一次登录。。 : " + data);
                                    Message msg = Message.obtain();
                                    msg.obj = data;
                                    msg.what = 3002;
                                    sendMessage(msg);
                                    flag=true;
                                }else{
                                    showToastMsg("获取登录失败,请扫码重试。。。");
                                    try {
                                        Thread.sleep(5000);
                                    }catch (Exception e){

                                    }
                                }
                            }
                        };
                    }.start();
                    break;

                    //测试用的
                case 2005:
                    new Thread() {
                        public void run() {
                            uuid=webChatService.getUUID();
                            if(!TextUtils.isEmpty(uuid)){
                                MyLog.e(uuid);
                                showToastMsg("获取UUID。。。success : "+uuid);
                              //  sendEmptyMessage(3000);  //跳过扫码登录
                                String login_uuid = "https://login.weixin.qq.com/l/"+uuid;
                                String url=UrlManager.getUpload_qrcode(UrlManager.HTTP_PROTOCOL);
                                Map map=new HashMap();
                                map.put("url",login_uuid);
                                //map.put("group_id", currentChatRoomName);
                                String token1= SharedPreferenceUtils.getTokenStrFromSharedPre(context);
                                map.put("token", token1);
                                HttpUtils.submitPostData(url, map, "utf-8");
                              //  QrcodeUtils.saveQRcodeFileToDCIM(getContext(), login_uuid);
                                sendEmptyMessage(3010);
                               // sendEmptyMessageDelayed(3001,6000);
                            }else{
                                showToastMsg("获取UUID失败。。。");
                            }
                        };
                    }.start();

                    break;






                case 2006:
                    MyLog.e("测试红包view --case 2006:" + currentActivity);
                    View topView2 = currentActivity.getWindow().getDecorView();
                    final ViewGroup topViewGp2 = (ViewGroup) topView2;
                    HongBaoView.getHongBaoView(topViewGp2);
                    break;
            }
        }
    };


    private void showToastMsg(final String msg){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getTargetApplication(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public SendMessage getFirstSendMessage() {
        if (sendMessages.size() > 0) {
            return sendMessages.get(0);
        }
        return null;
    }


    class SendMessageThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                if (is_monitor && isMainProce && InitDataManager.isMainActivity(currentActivity)) {
                    if(period==4 && hongbao_state==0){
                        handler.sendEmptyMessage(2006);   //自动打开红包
                    }
                    sendMyMessage();
                }

                try {
                    if(Web_weatChat_IsLogin==1 ){
                        //webChatService.handleTextMsg(wechatMeta, "");  //心跳
                        if(sendMessages!=null && sendMessages.size()>0){

                        }else{
                            List list=webChatService.getWechatGroup(wechatMeta);
                            if(list!=null){

                            }
                        }
                    }
                  //  handler.sendEmptyMessage(2006);   //自动打开红包
                    int leng=sendMessages.size() <1 ? 1 : sendMessages.size();
                    Thread.sleep(4000/leng);
                } catch (InterruptedException e) {
                }
            }
        }









        private void sendMyMessage() {
            if (sendMessages==null || sendMessages.size() <= 0) {
                return;
            }
            MyLog.e("sendMyMessage  ---- ");
            SendMessage send = getFirstSendMessage();
            period=send.getPeriod();
            if (send.getSendState() == 2  || send.getType()==0) {
                if(send.getType()==0){
                    period=send.getPeriod();
                    MyLog.e("更新期间到：%d", period);
                }
                sendMessages.remove(0);
                sendMyMessage();
            }else if (send != null) {
                MyLog.e("消息还有："+sendMessages.size()+"\t 期间：%d, time 次数: "+send.getSendTime()+"  --  send 正在处理--  "+send.getMsg()+ "\tstates : "+send.getSendState()+ " type :"+send.getType()+" \t "+sendMessages.toString() ,period);
                if (send.getType() == 1) { //文字处理
                    if (send.getSendState() == 0) {
                        MyLog.e("文字正在处理。。。 " + send.getMsg());
                        send.setSendState(1);
                        send.setSendTime(0);
                       // SendManager.sendTextMessage(send.getMsg(), handler, 0);
                        String result=webChatService.handleTextMsg(wechatMeta, send.getMsg());
                        try {
                            Thread.sleep(2000);
                        }catch (Exception e){

                        }
                        if(result==null){
                            send.setSendState(0);  //重新发送
                        }
                    }
                } else if (send.getType() == 2) { //表情处理
                    if (send.getSendState() == 0) {
                        MyLog.e("表情正在处理。。。 " + send.getMsg());
                        send.setSendState(1);
                       // SendManager.sendEmojiMessageByFileMd5(getContext(), handler, send.getMsg(), 0);
                        String result=webChatService.handleEmotionMsg(getContext(), wechatMeta, send.getMsg());
                        try {
                            Thread.sleep(2000);
                        }catch (Exception e){

                        }
                        if(result==null){
                            send.setSendState(0);  //重新发送
                        }
                    }
                }
            }
        }
    }


    class MonitorDatabseThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                if (is_monitor && isMainProce && currentChatRoomName!=null && InitDataManager.isMainActivity(currentActivity)) {
                    SendMessage send = getFirstSendMessage();
                    //StringUtils.hexStringToString(CommonData.sqlQuery);
                    String sqlQuery4 = "select * from message  where " + MessageTb._createTime + " > " + currentTime + " order by createTime asc";
                    List<MessageTb> list4 = dateBaseMonitor.queryMessageFromDB(sqlQuery4);
                    //  MyLog.e("case 4:" +list4.size()+"----"+ sqlQuery4);
                    while (list4.size() > 0) {
                        MessageTb mess = list4.remove(0);
                        if(!mess.getTalker().equals(currentChatRoomName)){
                            continue;
                        }
                        MyLog.e("period: %d --- mess = " + mess.genJSONData().toString(), period);
                        currentTime = mess.getCreateTime();
                        //更改消息队列状态
                        if(send!=null){
                            StringBuffer sb1=new StringBuffer(send.getMsg());
                            String temp=sb1.length()>10 ? sb1.substring(0,5) : sb1.toString();
                            if (send != null && mess.genJSONData().toString().contains(temp)) {
                                send.setSendState(2);
                            }
                        }

                        StringBuilder sb = new StringBuilder(mess.getContent());
                        if ((mess.getType() == 1) && InstructionsBean.isContainsQset(sb.toString())) {
                            if (period == 0 || period == 1) {
                                MyLog.e("用户标庄 --- 识别q40, q60, q80, q100, q120....");
                                Message message = Message.obtain();
                                message.what = 5;
                                message.obj = mess;
                                mess.setInstruct_type(1);
                                handler.sendMessage(message);
                            }
                        } else if ((period == 2) && (mess.getType() == 1) && InstructionsBean.isNumeric(sb.toString())) {
                            MyLog.e("开始压注 --- 识别字符串数字 --- 40, 60, 80, 100， sh120....");
                            Message message = Message.obtain();
                            message.what = 5;
                            message.obj = mess;
                            mess.setInstruct_type(2);
                            handler.sendMessage(message);
                        } else if ((period == 3) && (mess.getType() == 436207665)) {
                            MyLog.e("识别首发红包指令 ---436207665 --- sendid ....");
                            Message message = Message.obtain();
                            message.what = 5;
                            message.obj = mess;
                            mess.setInstruct_type(3);
                            handler.sendMessage(message);
                            hongbao_state=0; //开始有包
                        }
                    }

                    try {
                        int leng=sendMessages.size() <1 ? 1 : sendMessages.size();
                        Thread.sleep(2000/leng);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

//com.tencent.mm.plugin.webwx.ui.ExtDeviceWXLoginUI

    // TODO: 2017/9/22 注意不同的进程都会初始化插件入口一次，注意不同进程的数据共享
    @Override
    public boolean OnPluginCreate() {
        versionCode=ProcessUtils.getAppVersionCode(getTargetApplication());
        //MyLog.e("versionCode : %d", versionCode);
        boolean isSupport= CommonData.isSupportCurrentVersion(versionCode);
    //    MyLog.e("supported_version_number  -- %s", isSupport);
        if(!isSupport){
            String vesionName=ProcessUtils.getAppVersionName(getTargetApplication());
            List list = Arrays.asList(CommonData.SUPPORTED_VERSION_NAME);
            String data=String.format("红牛机器人暂时不支持微信版本名为：%s (%d)，仅支持版本：%s, 请联系更新。。。",vesionName,versionCode, list.toString());
            MyLog.e("supported_version_number  -- %s", data);
            Toast.makeText(getTargetApplication(), data, Toast.LENGTH_LONG).show();
            return false;
        }
        CommonData.initDifferVersionCommonData(versionCode);
        instance = this;
        isMainProce=ProcessUtils.isMainProcess(getContext());
        if(isMainProce){
            PowerManager pm = (PowerManager)getContext().getSystemService(POWER_SERVICE);
            m_wklk = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "cn");
            m_wklk.acquire();
            if(webChatService==null){
                wechatMeta=new WechatMeta();
                persistentCookieStore=new PersistentCookieStore(getTargetApplication(),wechatMeta);
                webChatService=new WechatServiceImpl(persistentCookieStore);
            }
        }else{
            MyLog.e("非主线程，不注册回调。。。");
            return false;
        }
       //  handler.sendEmptyMessageDelayed(3003, 5000);
        registerActivityCallback(new ActivityCallback() {
            @Override
            public void OnActivityCreate(Activity activity, Bundle bundle) {
                if (isMainProce && !pluginManagerViewHashMap.containsKey(activity)) {
                    PluginManagerView pm = new PluginManagerView(activity);
                    pluginManagerViewHashMap.put(activity, pm);
                    pm.onActivityCreate(activity, bundle);
                }
            }

            @Override
            public void OnActivityResume(Activity activity) {
                currentActivity = activity;
                String acName = activity.getClass().getName();

                if(InitDataManager.isMainActivity(currentActivity)){
                    m_wklk.acquire();
                }

/*                int pid=android.os.Process.myPid();
                String proceName=ProcessUtils.getCurrentPrecessName(getContext(), pid);*/

              //  MyLog.e(isMainProce+"\tperiod +\t" + period + "---\tOnActivityResume -- " + acName);
             //   TestUtils.printActivityLog(activity);

                if(acName.contains("com.tencent.mm.plugin.luckymoney.ui.En_fba4b94f")){
                    if(period==4 && hongbao_state==0){
                        handler.sendEmptyMessageDelayed(2007,1000);
                    }
                }else if (acName.contains("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI")) {  //红包
                    if(period==4 && hongbao_state==0){
                        handler.sendEmptyMessageDelayed(16, 1000);
                    }
                } else if (acName.contains("com.tencent.mm.plugin.gallery.ui.AlbumPreviewUI")) {
                   // handler.sendEmptyMessageDelayed(213,500);
                } else if (acName.contains("com.tencent.mm.plugin.gallery.ui.ImagePreviewUI")) {
                  //  handler.sendEmptyMessageDelayed(214,500);
                }
                try {
                    pmView = pluginManagerViewHashMap.get(activity);
                    if (pmView != null) {
                        pmView.onActivityResumed(activity);
                    }

                } catch (Exception e) {
                    MyLog.e("exception : " + e.getMessage());
                }
            }

            @Override
            public void OnActivityPause(Activity activity) {
               // MyLog.e("OnActivityPause  -- --period :" + period + "  " + activity.getClass().getName());
                PluginManagerView pm = pluginManagerViewHashMap.get(activity);
                if (pm != null) {
                    pm.onActivityPaused(activity);
                }
            }



            @Override
            public void OnActivityDestroy(Activity activity) {
               // MyLog.e("OnActivityDestroy  --period :" + period + "--  " + activity.toString());
                PluginManagerView pm = pluginManagerViewHashMap.remove(activity);
                if (pm != null) {
                    pm.onActivityDestroy(activity);
                }
            }
        });
        return false;
    }


    public static Plugin getPlugins() {
        return instance;
    }

    @Override
    public void OnPlguinDestroy() {
        MyLog.e("OnPlguinDestroy-- " + currentActivity.toString());
        if (currentActivity.getClass().getName().contains(CommonData.default_first_main_activity) && isMainProce) {
            WebSocketService.closeWebsocket();
            getContext().stopService(websecketServiceIntent);
            m_wklk.release();
        }
    }


    @Override
    public View OnPluginUIShow() {
        if (pluginView == null && isMainProce) {
            pluginView = new PluginVIew(getContext(), handler);
        }
        return pluginView;
    }

    @Override
    public void OnPluginUIHide() {
    }


}
