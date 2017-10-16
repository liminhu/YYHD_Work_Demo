package com.gameassist.plugin.websocket;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.gameassist.plugin.bean.InstructionsBean;
import com.gameassist.plugin.common.UrlManager;
import com.gameassist.plugin.mm.robot.PluginEntry;
import com.gameassist.plugin.utils.MyLog;
import com.gameassist.plugin.utils.TestUtils;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketHandler;
import de.tavendo.autobahn.WebSocketOptions;

public class WebSocketService extends Service {
    public   static Context context;
    private static final String TAG=WebSocketService.class.getSimpleName();

    private BroadcastReceiver connectionReceiver;
    public static boolean isClosed = true;
    private static WebSocketConnection webSocketConnection;
    private static WebSocketOptions options=new WebSocketOptions();
    public static Handler handler;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(connectionReceiver == null){
            connectionReceiver= new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
                    MyLog.e("hook_BroadcastReceiver --- networkInfo -----");
                    if(networkInfo == null || !networkInfo.isAvailable()){
                        Toast.makeText(getApplicationContext(), "网络异常，服务已断开，请重新开始。。。", Toast.LENGTH_SHORT).show();
                    }else{
                        if(webSocketConnection != null){
                            webSocketConnection.disconnect();
                            isClosed=true;
                        }
                        if(isClosed){
                            webSocketConnet();
                        }
                    }
                }
            };
            MyLog.e("hook_"+TAG +"onStartCommand");
            IntentFilter intentFilter=new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(connectionReceiver, intentFilter);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }


    public static void closeWebsocket(){
        MyLog.e("hook_1111  --  "+TAG+"  ---- closeWebsocket");
        isClosed=true;
        if(webSocketConnection !=null && webSocketConnection.isConnected()){
            webSocketConnection.disconnect();
            webSocketConnection=null;
        }
    }


    public static void openWebSocket(WebSocketConnection webSocketConnection){
        try{
            options.setMaxMessagePayloadSize(1024*1024*2);
            options.setMaxFramePayloadSize(1024*1024*2);
            if(TextUtils.isEmpty(PluginEntry.chatroom_nickname)){
                handler.sendEmptyMessageDelayed(1, 100);
                return;
            }
            final String webSocketHost= UrlManager.genWebSocketUrl(context, PluginEntry.currentChatRoomName, PluginEntry.chatroom_nickname);
            MyLog.e("%s is "+webSocketConnection.isConnected(), webSocketHost);
            webSocketConnection.connect(webSocketHost, new WebSocketHandler(){
                //websocket 启动时回调
                @Override
                public void onOpen() {
                    MyLog.e("hook_"+TAG+"Open --- " +"\t"+webSocketHost.toString());
                    Toast.makeText(context, "服务已连接。。。", Toast.LENGTH_SHORT).show();
                    isClosed=false;
                }


                @Override
                public void onClose(int code, String reason) {
                    MyLog.e("hook_onClose ---- "+TAG+"  ----  code="+code+"\treason ="+reason);
                    isClosed=true;
                    closeWebsocket();
                    switch (code){
                        case 1:
                            break;
                        default:
                            break;
                    }
                }


                //接收到消息时后回调
                @Override
                public void onTextMessage(String payload) {
                   // Toast.makeText(context, "回调接收到的消息："+payload, Toast.LENGTH_SHORT).show();
                    InstructionsBean bean=InstructionsBean.getInsBeanFromJSON(payload);
                    MyLog.e("get Message :%s\t ---  payload= "+payload, bean.getMsg());
                    if(bean.getMsg_type()==0) {  //更新期间指令
                        MyLog.e("hook_更新期间指令 payload=   " + payload);
                       Message mess = Message.obtain();
                        mess.what = 194; //201;
                        mess.obj = bean;
                        handler.sendMessage(mess);
                    }else if(bean.getMsg_type() != 3){ //类型3不转发
                        MyLog.e("转发文字图片指令 payload=   " + payload);
                        Message mess = new Message();
                        mess.what = 194;
                        mess.obj = bean;
                       // handler.sendMessageDelayed(mess, 500);
                        handler.sendMessage(mess);
                    }
                }
            }, options);
        }catch (Exception e){

        }
    }




    public static void restartConnectionWebSocket(int index){
        MyLog.e("重连--- "+TAG+"\twebSocketConnet");
        if(isClosed==true){
            webSocketConnection=null;
            webSocketConnet();
        }
        handler.sendEmptyMessageDelayed(index, 2000);   //延时
    }

    public static void webSocketConnet(){
        MyLog.e("hook_"+TAG+"\twebSocketConnet");
        if(webSocketConnection==null) {
            webSocketConnection=new WebSocketConnection();
        }
        if(!webSocketConnection.isConnected()){
            openWebSocket(webSocketConnection);
        }
    }


    public static void sendMessage(String s){
        MyLog.e("hook_sendMessage \t "+"-- sendMsg = "+s);
        if(!TextUtils.isEmpty(s)){
            if(webSocketConnection != null && webSocketConnection.isConnected()){
                webSocketConnection.sendTextMessage(s);
            }else{
                Toast.makeText(context, "未连接上服务器，消息发送失败！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroy() {
        MyLog.e("hook_onDestroy"+TAG+"--- onDestroy");
        super.onDestroy();
        if(connectionReceiver!=null){
            unregisterReceiver(connectionReceiver);
        }
    }
}
