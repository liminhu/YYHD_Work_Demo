package com.gameassist.plugin.mm.robot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import com.gameassist.plugin.hongbao.HongBaoView;
import com.gameassist.plugin.hongbao.WalletLuckyMoneyDetail;
import com.gameassist.plugin.utils.DialogUtils;
import com.gameassist.plugin.utils.MyLog;
import com.gameassist.plugin.utils.ProcessUtils;
import com.gameassist.plugin.utils.StringUtils;
import com.gameassist.plugin.utils.TestUtils;
import com.gameassist.plugin.websocket.WebSocketService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hulimin on 2017/9/4.
 */

public class PluginEntry extends Plugin {
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

    private View pluginView;
    private static String key_sendid;
    private  Intent websecketServiceIntent = null;
    private  boolean isMainProce;
    private  SendMessageThread sendThread;
    private Handler mainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    MonitorDatabseThread dbThead = new MonitorDatabseThread();
                    Thread thread = new Thread(dbThead);
                    thread.start();

                    //专门发送消息线程
                    sendThread = new SendMessageThread();
                    Thread thread1 = new Thread(sendThread);
                    thread1.start();
                    break;

                //红包监听
                case 1:
                    MyLog.e("case 1: -- 红包监听");
                    break;
            }
        }
    };


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
                        handler.sendEmptyMessageDelayed(1, 2500);
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
                        handler.sendEmptyMessageDelayed(3, 2500);
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
                    String user_name = chatroomTb.getRoom_owner();
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
                        SendMessage send = new SendMessage(data194.getMsg(), data194.getMsg_type(), 0);
                        sendMessages.add(send);
                        sendThread.sendMyMessage();
                    }
                    break;


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
                        String js202 = intstruct.genSendInstructionJsonString();
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


                //默认选择相册第一张图
                case 213:
                    MyLog.e(" 默认选择相册第一张图 case 203: 更换activity  -- period:" + period + "\t" + sendMessages.toString());
                    SendManager.chooseDefaultGIF(handler, 0);
                    break;


                case 214:
                    MyLog.e("发送图 --case 214:" + currentActivity);
                    SendManager.sendLastGIF(handler, 0);
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
                                    String js = hongbao.genHongBaoInsJsonStr(list);
                                    MyLog.e(js);
                                    hongbao_state=1;
                                    WebSocketService.sendMessage(js);
                                    SendManager.onBack();
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


                //测试用的
                case 2005:
                    MyLog.e("保存当前的view  --case 2005:" + currentActivity);
                    View topView1 = currentActivity.getWindow().getDecorView();
                    final ViewGroup topViewGp1 = (ViewGroup) topView1;
                    TestUtils.testSaveImage(topViewGp1, mainHandler);
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
                    int leng=sendMessages.size() <1 ? 1 : sendMessages.size();
                    Thread.sleep(2000/leng);
                } catch (InterruptedException e) {
                }
            }
        }

        private void sendMyMessage() {
            if (sendMessages.size() <= 0) {
                return;
            }
            SendMessage send = getFirstSendMessage();
            if (send.getSendState() == 2) {
                sendMessages.remove(0);
                sendMyMessage();
            }
            if (send != null) {
                MyLog.e(sendMessages.size()+"\t time 次数: "+send.getSendTime()+"  --  send 正在处理--  "+send.getMsg()+ "\tstates : "+send.getSendState()+ " type :"+send.getType()+" \t "+sendMessages.toString());
                if (send.getType() == 1) { //文字处理
                    if (send.getSendState() == 0) {
                        MyLog.e("文字正在处理。。。 " + send.getMsg());
                        send.setSendState(1);
                        send.setSendTime(0);
                        SendManager.sendTextMessage(send.getMsg(), handler, 0);
                    }else if(send.getSendTime() > 5){
                        send.setSendState(0);  //重新发送
                    }else{
                        send.setSendTime(send.getSendTime()+1);
                    }
                } else if (send.getType() == 2) { //表情处理
                    if (send.getSendState() == 0) {
                        MyLog.e("表情正在处理。。。 " + send.getMsg());
                        send.setSendState(1);
                        SendManager.sendEmojiMessageByFileMd5(getContext(), handler, send.getMsg(), 0);
                    }else if(send.getSendTime() > 10){
                        send.setSendState(0);  //重新发送
                    }else{
                        send.setSendTime(send.getSendTime()+1);
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
                        MyLog.e(" --- mess = " + mess.genJSONData().toString());
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


    // TODO: 2017/9/22 注意不同的进程都会初始化插件入口一次，注意不同进程的数据共享
    @Override
    public boolean OnPluginCreate() {
        StringUtils.getImei(getContext());

        instance = this;
        isMainProce=ProcessUtils.isMainProcess(getContext());
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

/*                int pid=android.os.Process.myPid();
                String proceName=ProcessUtils.getCurrentPrecessName(getContext(), pid);*/
               // MyLog.e("name: +"+proceName+"\tperiod +\t" + period + "---\tOnActivityResume -- " + acName);

                if(acName.contains("com.tencent.mm.plugin.luckymoney.ui.En_fba4b94f")){
                    if(period==4 && hongbao_state==0){
                        handler.sendEmptyMessageDelayed(2007,1000);
                    }
                }else if (acName.contains("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI")) {  //红包
                    if(period==4 && hongbao_state==0){
                        handler.sendEmptyMessageDelayed(16, 1000);
                    }
                } else if (acName.contains("com.tencent.mm.plugin.gallery.ui.AlbumPreviewUI")) {
                    handler.sendEmptyMessageDelayed(213,500);
                } else if (acName.contains("com.tencent.mm.plugin.gallery.ui.ImagePreviewUI")) {
                    handler.sendEmptyMessageDelayed(214,500);
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
        if (currentActivity.getClass().getName().contains(CommonData.FIRST_MAIN_ACTIVITY) && isMainProce) {
            WebSocketService.closeWebsocket();
            getContext().stopService(websecketServiceIntent);
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
